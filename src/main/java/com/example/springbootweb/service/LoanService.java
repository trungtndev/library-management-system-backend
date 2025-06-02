package com.example.springbootweb.service;

import com.example.springbootweb.dto.request.LoanCreationRequest;
import com.example.springbootweb.dto.request.LoanStatusRequest;
import com.example.springbootweb.dto.respone.LoanDetailResponse;
import com.example.springbootweb.dto.respone.LoanSummaryResponse;
import com.example.springbootweb.dto.respone.PaginationResponse;
import com.example.springbootweb.entity.Book;
import com.example.springbootweb.entity.Loan;
import com.example.springbootweb.entity.User;
import com.example.springbootweb.enums.LoanStatus;
import com.example.springbootweb.exception.AppException;
import com.example.springbootweb.exception.ErrorCode;
import com.example.springbootweb.mapper.LoanMapper;
import com.example.springbootweb.mapper.PaginationMapper;
import com.example.springbootweb.repository.BookRepository;
import com.example.springbootweb.repository.LoanRepository;
import com.example.springbootweb.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoanService {
    private static final Map<LoanStatus, Set<LoanStatus>> VALID_TRANSITIONS = Map.of(
            LoanStatus.PENDING, Set.of(LoanStatus.APPROVED, LoanStatus.REJECTED, LoanStatus.CANCELLED),
            LoanStatus.APPROVED, Set.of(LoanStatus.RETURNED, LoanStatus.LOST, LoanStatus.RENEWED),
            LoanStatus.RENEWED, Set.of(LoanStatus.RETURNED, LoanStatus.LOST),
            LoanStatus.RETURNED, Set.of(LoanStatus.LATE),
            LoanStatus.LATE, Set.of(),
            LoanStatus.LOST, Set.of(),
            LoanStatus.REJECTED, Set.of(),
            LoanStatus.CANCELLED, Set.of()
    );
    LoanRepository loanRepository;
    UserRepository userRepository;
    BookRepository bookRepository;
    LoanMapper loanMapper;
    PaginationMapper paginationMapper;

    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN','MEMBER')")
    @Transactional
    public LoanDetailResponse createLoan(String bookId, LoanCreationRequest request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user;

        if (checkIsLibrarianOrAdmin()) {
            if (request.getUserId() == null) {
                throw new AppException(ErrorCode.USER_ID_NULL);
            }
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        } else {
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        int quantity = book.getQuantity();
        if (quantity <= 0) {
            throw new AppException(ErrorCode.BOOK_NOT_AVAILABLE);
        } else {
            quantity = quantity - 1;
        }

        // check due date
        if (request.getDueDate().isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.INVALID_DUE_DATE);
        }

        book.setQuantity(quantity);

        // update book quantity
        bookRepository.save(book);

        Loan loan = loanMapper.toLoan(request);
        loan.setUser(user);
        loan.setBook(book);
        loan.setStatus(LoanStatus.PENDING);
        loan.setLoanDate(LocalDate.now());
        loan.setCreatedAt(LocalDate.now());

        return loanMapper.toLoanDetailResponseWithoutQuantity(
                loanRepository.save(loan)
        );
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
    public PaginationResponse<LoanSummaryResponse> getAllLoans(Pageable pageable) {
        Page<LoanSummaryResponse> loans = loanRepository.findAll(pageable)
                .map(loanMapper::toLoanSummaryWithoutQuantity);
        return paginationMapper.toPaginationResponse(loans);
    }

    @PreAuthorize(
            "hasAnyRole('LIBRARIAN','ADMIN')" +
                    "or " +
                    "@loanRepository.getById(#loanId).user.getUsername() == authentication.name"
    )
    public LoanDetailResponse getLoanById(String loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new AppException(ErrorCode.LOAN_NOT_FOUND));
        return loanMapper.toLoanDetailResponseWithoutQuantity(loan);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteLoan(String loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new AppException(ErrorCode.LOAN_NOT_FOUND));
        loanRepository.delete(loan);
    }

    @PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN') " +
            "or " +
            "(hasRole('MEMBER'))"
    )
    public PaginationResponse<LoanSummaryResponse> getLoansByUserId(String userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Page<LoanSummaryResponse> loans = loanRepository.findAllByUser(user, pageable)
                .map(loanMapper::toLoanSummaryWithoutQuantity);

        return paginationMapper.toPaginationResponse(loans);

    }

    @PreAuthorize("hasRole('MEMBER')")
    public PaginationResponse<LoanSummaryResponse> getMyLoans(Pageable pageable) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Page<Loan> userPage = loanRepository.findAllByUser(user, pageable);

        Page<LoanSummaryResponse> loans = userPage.map(loanMapper::toLoanSummaryWithoutQuantity);

        return paginationMapper.toPaginationResponse(loans);
    }

    @PreAuthorize(
            "hasAnyRole('LIBRARIAN','ADMIN')" +
                    "or " +
                    "@loanRepository.getById(#loanId).getUser().getUsername() == authentication.name "
    )
    @Transactional
    public LoanDetailResponse updateLoanStatus(String loanId, LoanStatusRequest request) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new AppException(ErrorCode.LOAN_NOT_FOUND));
        LoanStatus requestStatus = request.getStatus();
        LoanStatus currentStatus = loan.getStatus();

        if (!checkIsLibrarianOrAdmin()) {
            if (requestStatus != LoanStatus.CANCELLED || currentStatus != LoanStatus.PENDING) {
                throw new AppException(ErrorCode.DO_NOT_HAVE_PERMISSION);
            }
            returnBook(loan.getBook());
        } else {
            validateStatusTransition(currentStatus, requestStatus);
        }

        // update book quantity if the loan is returned
        if ((currentStatus == LoanStatus.APPROVED || currentStatus == LoanStatus.RENEWED)
                && requestStatus == LoanStatus.RETURNED) {
            loan.setReturnDate(LocalDate.now());
            returnBook(loan.getBook());
        } else if (currentStatus == LoanStatus.APPROVED
                && requestStatus == LoanStatus.RENEWED) {
            renewLoan(loan, request.getDueDate());
        }

        loan.setStatus(requestStatus);
        return loanMapper.toLoanDetailResponseWithoutQuantity(loanRepository.save(loan));
    }

    private void returnBook(Book book) {
        int quantity = book.getQuantity();
        quantity = quantity + 1;
        book.setQuantity(quantity);
        bookRepository.save(book);
    }

    private void renewLoan(Loan loan, LocalDate newDueDate) {
        if (newDueDate == null) {
            throw new AppException(ErrorCode.DUE_DATE_NULL);
        }
        if (newDueDate.isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.INVALID_DUE_DATE);
        }
        loan.setDueDate(newDueDate);
        loanRepository.save(loan);
    }

    private boolean checkIsLibrarianOrAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_LIBRARIAN") || auth.getAuthority().equals("ROLE_ADMIN"));
    }

    private void validateStatusTransition(LoanStatus from, LoanStatus to) {
        Set<LoanStatus> allowed = VALID_TRANSITIONS.getOrDefault(from, Set.of());
        if (!allowed.contains(to))
            throw new AppException(ErrorCode.INVALID_STATUS_CHANGE);

    }

}
