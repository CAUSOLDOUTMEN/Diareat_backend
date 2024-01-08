package com.diareat.diareat.util.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ResponseCode {

    // 400 Bad Request
    BAD_REQUEST(HttpStatus.BAD_REQUEST, false, "잘못된 요청입니다."),

    // 401 Unauthorized
    TOKEN_VALIDATION_FAILURE(HttpStatus.UNAUTHORIZED, false, "토큰 검증 실패"),

    // 403 Forbidden
    FORBIDDEN(HttpStatus.FORBIDDEN, false, "권한이 없습니다."),
    NOT_FOOD_OWNER(HttpStatus.FORBIDDEN, false, "음식의 주인 유저가 아닙니다."),

    // 404 Not Found
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, false, "사용자를 찾을 수 없습니다."),
    FOOD_NOT_FOUND(HttpStatus.NOT_FOUND, false, "음식을 찾을 수 없습니다."),
    FAVORITE_NOT_FOUND(HttpStatus.NOT_FOUND, false, "즐겨찾기를 찾을 수 없습니다."),

    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, false, "허용되지 않은 메소드입니다."),

    // 409 Conflict
    USER_ALREADY_EXIST(HttpStatus.CONFLICT, false, "이미 가입한 사용자입니다."),
    USER_NAME_ALREADY_EXIST(HttpStatus.CONFLICT, false, "이미 존재하는 닉네임입니다."),
    FOLLOWED_ALREADY(HttpStatus.CONFLICT, false, "이미 팔로우한 사용자입니다."),
    UNFOLLOWED_ALREADY(HttpStatus.CONFLICT, false, "이미 언팔로우한 사용자입니다."),
    FAVORITE_ALREADY_EXIST(HttpStatus.CONFLICT, false, "이미 즐겨찾기에 존재하는 음식입니다."),
    FOOD_NAME_ALREADY_EXIST(HttpStatus.CONFLICT, false, "이미 존재하는 음식 이름입니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, false, "서버에 오류가 발생하였습니다."),

    // 200 OK
    USER_READ_SUCCESS(HttpStatus.CREATED, true, "사용자 정보 조회 성공"),
    USER_UPDATE_SUCCESS(HttpStatus.OK, true, "사용자 정보 수정 성공"),
    USER_SEARCH_SUCCESS(HttpStatus.OK, true, "사용자 검색 성공"),
    USER_FOLLOW_SUCCESS(HttpStatus.OK, true, "사용자 팔로우 성공"),
    USER_UNFOLLOW_SUCCESS(HttpStatus.OK, true, "사용자 언팔로우 성공"),
    USER_LOGIN_SUCCESS(HttpStatus.OK, true, "사용자 로그인 성공"),

    FOOD_READ_SUCCESS(HttpStatus.OK, true, "음식 정보 조회 성공"),
    FOOD_UPDATE_SUCCESS(HttpStatus.OK, true, "음식 정보 수정 성공"),
    FOOD_DELETE_SUCCESS(HttpStatus.OK, true, "음식 정보 삭제 성공"),
    FOOD_FAVORITE_READ_SUCCESS(HttpStatus.OK, true, "즐겨찾기 음식 조회 성공"),
    FOOD_FAVORITE_UPDATE_SUCCESS(HttpStatus.OK, true, "즐겨찾기 음식 수정 성공"),
    FOOD_FAVORITE_DELETE_SUCCESS(HttpStatus.OK, true, "즐겨찾기 음식 삭제 성공"),

    FOOD_RANK_READ_SUCCESS(HttpStatus.OK, true, "식습관 점수 기반 랭킹 조회 성공"),

    TOKEN_CHECK_SUCCESS(HttpStatus.OK, true, "토큰 검증 완료"),


    // 201 Created
    USER_CREATE_SUCCESS(HttpStatus.CREATED, true, "사용자 생성 성공"),
    FOOD_CREATE_SUCCESS(HttpStatus.CREATED, true, "음식 생성 성공"),
    FOOD_FAVORITE_CREATE_SUCCESS(HttpStatus.OK, true, "즐겨찾기 음식 생성 성공");


    private final HttpStatus httpStatus;
    private final Boolean success;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
