package com.example.springbootweb.mapper;

import com.example.springbootweb.dto.request.ReviewCreationRequest;
import com.example.springbootweb.dto.request.ReviewUpdateRequest;
import com.example.springbootweb.dto.respone.ReviewResponse;
import com.example.springbootweb.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "user", ignore = true)
    Review toReview(ReviewCreationRequest request);

    @Mapping(target = "mine", ignore = true)
    @Mapping(target = "fullName", source = "user.fullName")
    ReviewResponse toReviewResponse(Review review);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateReview(@MappingTarget Review review, ReviewUpdateRequest request);
}
