package com.diareat.diareat.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserDto {

    @NotNull(message = "userId는 null이 될 수 없습니다.")
    private Long userId;

    @NotEmpty(message = "검색 문자열은 비어있을 수 없습니다.")
    private String inputName;

    public static SearchUserDto of(Long userId, String inputName) {
        return new SearchUserDto(userId, inputName);
    }
}
