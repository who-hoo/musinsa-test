package com.musinsa.category.dto;

import com.musinsa.category.entity.Category;
import com.musinsa.category.entity.CategoryRelation;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryResponse {

	private Long categoryId;
	private String categoryName;
	private String categoryEnglishName;
	private List<CategoryResponse> subCategories;

	public static CategoryResponse from(Category category) {
		List<CategoryResponse> subCategories = category.getSubCategories().stream()
			.map(CategoryRelation::getChildCategory)
			.map(CategoryResponse::from)
			.collect(Collectors.toList());
		return new CategoryResponse(category.getId(), category.getKorName(), category.getEngName(), subCategories);
	}
}
