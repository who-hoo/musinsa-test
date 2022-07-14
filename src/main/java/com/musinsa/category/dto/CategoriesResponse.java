package com.musinsa.category.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoriesResponse {

	private List<CategoryResponse> categories;

	public static CategoriesResponse from(List<CategoryResponse> categories) {
		return new CategoriesResponse(categories);
	}
}
