package com.musinsa.category.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class UpdateCategoryRequest {

	private String korName;
	private String engName;
	private Long parentCategoryId;
	private List<Long> subCategoryIdList;

	public static UpdateCategoryRequest of(String korName, String engName, Long parentCategoryId, List<Long> subCategoryIdList) {
		return new UpdateCategoryRequest(korName, engName, parentCategoryId, subCategoryIdList);
	}
}
