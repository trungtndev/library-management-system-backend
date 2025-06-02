package com.example.springbootweb.mapper;

import com.example.springbootweb.dto.request.LoanCreationRequest;
import com.example.springbootweb.dto.respone.LoanDetailResponse;
import com.example.springbootweb.dto.respone.LoanSummaryResponse;
import com.example.springbootweb.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BookMapper.class})
public interface LoanMapper {
    @Mapping(target = "returnDate", ignore = true)
    @Mapping(target = "loanDate", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    Loan toLoan(LoanCreationRequest request);

//    @Mapping(target = "book", source = "book", qualifiedByName = "toBookDetailResponse")
//    LoanDetailResponse toLoanDetailResponse(Loan loan);

    @Mapping(target = "book", source = "book", qualifiedByName = "toBookSummaryWithoutQuantity")
    LoanDetailResponse toLoanDetailResponseWithoutQuantity(Loan loan);

//    @Mapping(target = "book", source = "book", qualifiedByName = "toBookSummaryResponse")
//    LoanSummaryResponse toLoanSummaryResponse(Loan loan);

    @Mapping(target = "book", source = "book", qualifiedByName = "toBookSummaryWithoutQuantity")
    LoanSummaryResponse toLoanSummaryWithoutQuantity(Loan loan);
}
