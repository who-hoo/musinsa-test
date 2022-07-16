package com.musinsa.category.dto;

import com.musinsa.category.entity.Category;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude = {"categoryName", "categoryEnglishName", "parentCategoryId", "subCategories"})
public class CategoryResponse {

	private Long categoryId;
	private String categoryName;
	private String categoryEnglishName;
	private Long parentCategoryId;
	private List<CategoryResponse> subCategories;

	public static CategoryResponse from(Category category) {
		Long parentCategoryId = category.getParent() == null ? null : category.getParent().getId();
		List<CategoryResponse> subCategories = category.getSubCategories().stream()
			.map(CategoryResponse::from)
			.collect(Collectors.toList());

		return new CategoryResponse(
			category.getId(),
			category.getKorName(),
			category.getEngName(),
			parentCategoryId,
			subCategories
		);
	}
}
