package com.musinsa.category.dto;

import com.musinsa.category.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class AddCategoryRequest {

	private String korName;
	private String engName;
	private Long parentCategoryId;

	public Category toEntity() {
		return Category.of(korName, engName);
	}
}
