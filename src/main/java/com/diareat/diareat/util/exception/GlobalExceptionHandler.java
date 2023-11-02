package com.diareat.diareat.util.exception;

import com.diareat.diareat.util.api.ApiResponse;
import com.diareat.diareat.util.api.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ApiResponse<Void> handleUserException(UserException e) {
        log.info("UserException: {}", e.getMessage());
        return ApiResponse.fail(e.getResponseCode(), null);
    }

    @ExceptionHandler(FavoriteException.class)
    public ApiResponse<Void> handleFavoriteException(FavoriteException e) {
        log.info("FavoriteException: {}", e.getMessage());
        return ApiResponse.fail(e.getResponseCode(), null);
    }

    @ExceptionHandler(FoodException.class)
    public ApiResponse<Void> handleFoodException(FoodException e) {
        log.info("FoodException: {}", e.getMessage());
        return ApiResponse.fail(e.getResponseCode(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) // 요청의 유효성 검사 실패 시
    public ApiResponse<Map<String, String>> handleInValidRequestException(MethodArgumentNotValidException e) {
        // 에러가 발생한 객체 내 필드와 대응하는 에러 메시지를 map에 저장하여 반환
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ApiResponse.fail(ResponseCode.BAD_REQUEST, errors);
    }
}
