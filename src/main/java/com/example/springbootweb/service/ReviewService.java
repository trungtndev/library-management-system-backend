package com.example.springbootweb.service;

import com.example.springbootweb.dto.request.ReviewCreationRequest;
import com.example.springbootweb.dto.request.ReviewUpdateRequest;
import com.example.springbootweb.dto.respone.PaginationResponse;
import com.example.springbootweb.dto.respone.ReviewResponse;
import com.example.springbootweb.entity.Book;
import com.example.springbootweb.entity.Review;
import com.example.springbootweb.entity.User;
import com.example.springbootweb.exception.AppException;
import com.example.springbootweb.exception.ErrorCode;
import com.example.springbootweb.mapper.PaginationMapper;
import com.example.springbootweb.mapper.ReviewMapper;
import com.example.springbootweb.repository.BookRepository;
import com.example.springbootweb.repository.ReviewRepository;
import com.example.springbootweb.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {
    UserService userService;

    ReviewRepository reviewRepository;
    BookRepository bookRepository;
    UserRepository userRepository;
    ReviewMapper reviewMapper;
    PaginationMapper paginationMapper;

    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN', 'MEMBER')")
    public ReviewResponse createReview(String bookId, ReviewCreationRequest request) {
        // Check if the user is authenticated
        String username = this.getUserName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        if (reviewRepository.existsByUserAndBook(user, book)) {
            throw new AppException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }

        // Check rating
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new AppException(ErrorCode.RATING_INVALID);
        }

        Review review = reviewMapper.toReview(request);
        review.setUser(user);
        review.setBook(book);
        review.setCreatedAt(LocalDate.now());
        Review saved = reviewRepository.save(review);

        ReviewResponse response = reviewMapper.toReviewResponse(saved);
        response.setMine(true);

        return response;

    }

    public PaginationResponse<ReviewResponse> getAllReviews(String bookId, Pageable pageable) {
        String username = this.getOptionalUserName();
        Page<ReviewResponse> responses;
        if (username == null) {
            // Không có user — chỉ lấy review bình thường
            responses = reviewRepository.findAllByBookId(bookId, pageable)
                    .map(reviewMapper::toReviewResponse);
        } else {
            // Có user — tìm userId, rồi ưu tiên review của họ lên đầu
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            String userId = user.getId();

            // Custom query sắp xếp mine-first (repository đã có query này)
            Page<Review> reviews = reviewRepository
                    .findAllByBookIdOrderByMineFirst(bookId, userId, pageable);

            responses = reviews.map(review -> {
                ReviewResponse dto = reviewMapper.toReviewResponse(review);
                dto.setMine(review.getUser().getId().equals(userId));
                return dto;
            });
        }
        return paginationMapper.toPaginationResponse(responses);

    }


    @PreAuthorize("hasRole('MEMBER')")
    @Transactional
    public ReviewResponse updateReview(String reviewId, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        reviewMapper.updateReview(review, request);

        return reviewMapper.toReviewResponse(reviewRepository.save(review));
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')"
            + "or "
            + "@reviewService.isReviewOwner(#reviewId)" // Check if the user is the owner of the review
    )
    @Transactional
    public void deleteReview(String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        reviewRepository.delete(review);
    }

    private String getUserName() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication().getName();
    }

    private String getOptionalUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return auth.getName();
    }

    public boolean isReviewOwner(String reviewId) {
        String username = this.getUserName();
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        return review.getUser().getUsername().equals(username);
    }
}
