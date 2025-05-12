package com.example.springbootweb.dto.respone;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder()
@Getter
@Setter
public class ApiResponse<T> {
    int code = 200;
    boolean success = true;
    String message;
    T result;

}