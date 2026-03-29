package com.banking.common.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
// If a field is null, Jackson will completely remove it from the JSON response
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private int status;

    private T data;

    private String message;

    private Object errors;

    // When using @Builder, Lombok ignores inline field initializations and
    //      defaults it to NULL, 0 or false
    // @Builder.Default forces Lombok to use inline initializations as a fallback
    //      if the field is skipped during the builder chain initialization.
    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:dd")
    private LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ApiResponse<T> success(int status, String message, T data) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(int status, String message, Object errors) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .errors(errors)
                .build();
    }

}
