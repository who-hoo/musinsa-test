package com.musinsa.category.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class UpdateCategoryRequest {

	private String korName;
	private String engName;
	private Long parentCategoryId;
	private List<Long> subCategoryIdList;
}
