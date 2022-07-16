package com.musinsa.category;

import com.musinsa.category.dto.CategoriesResponse;
import com.musinsa.category.dto.CategoryResponse;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public CategoriesResponse searchAllRootCategories() {
		List<CategoryResponse> categories = categoryRepository.findAllJoinFetch().stream()
			.filter(c -> c.getParent() == null)
			.map(CategoryResponse::from)
			.collect(Collectors.toList());
		return CategoriesResponse.from(categories);
	}

	@Transactional(readOnly = true)
	public CategoryResponse searchCategory(Long id) {
		return categoryRepository.findCategoryAndSubCategoriesByIdJoinFetch(id).stream()
			.filter(c -> Objects.equals(c.getId(), id))
			.map(CategoryResponse::from)
			.findAny()
			.orElseThrow(() -> new NoSuchElementException("존재하지 않는 [카테고리 ID]입니다."));
	}
}
