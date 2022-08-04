package com.musinsa.category.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessage {

    public static final String NO_SUCH_CATEGORY_ID = "존재하지 않는 [카테고리 ID]입니다.";
    public static final String NO_SUCH_PARENT_CATEGORY_ID = "상위 카테고리의 ID가 존재하지 않는 [카테고리 ID]입니다.";

    public static final String ILLEGAL_CATEGORY_KOR_NAME = "카테고리 한글이름(kor_name)은 필수 입력 값(공백 불가)입니다.";
    public static final String ILLEGAL_CATEGORY_ENG_NAME = "카테고리 영어이름(eng_name)은 빈 문자열(공백 불가)을 입력할 수 없습니다.(null 가능)";
}
