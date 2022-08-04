package com.musinsa.category.dto;

import com.musinsa.category.entity.Category;
import com.musinsa.category.exception.ErrorMessage;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class AddCategoryRequest {

	@NotBlank(message = ErrorMessage.ILLEGAL_CATEGORY_KOR_NAME)
	private String korName;

	private String engName;

	private Long parentCategoryId;

	public Category toEntity() {
		return Category.of(korName, engName);
	}

	public static AddCategoryRequest of(String korName, String engName, Long parentCategoryId) {
		return new AddCategoryRequest(korName, engName, parentCategoryId);
	}
}
