package com.diareat.diareat.util.exception;

import com.diareat.diareat.util.api.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ApiResponse<Void> handleUserException(UserException e) {
        log.info("UserException: {}", e.getMessage());
        return ApiResponse.fail(e.getResponseCode());
    }

    @ExceptionHandler(FavoriteException.class)
    public ApiResponse<Void> handleFavoriteException(FavoriteException e) {
        log.info("FavoriteException: {}", e.getMessage());
        return ApiResponse.fail(e.getResponseCode());
    }

    @ExceptionHandler(FoodException.class)
    public ApiResponse<Void> handleFoodException(FoodException e) {
        log.info("FoodException: {}", e.getMessage());
        return ApiResponse.fail(e.getResponseCode());
    }
}
