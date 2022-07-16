package com.musinsa.category.dto;

import com.musinsa.category.entity.Category;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class AddCategoryRequest {

	@NotBlank(message = "카테고리 한글이름(kor_name)은 필수 입력 값(공백 불가)입니다.")
	private String korName;

	private String engName;

	private Long parentCategoryId;

	public Category toEntity() {
		return Category.of(korName, engName);
	}
}
