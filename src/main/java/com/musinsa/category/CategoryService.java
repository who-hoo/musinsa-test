package com.musinsa.category;

import com.musinsa.category.dto.CategoriesResponse;
import com.musinsa.category.dto.CategoryResponse;
import java.util.List;
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
		//TODO: N+1 개선
		List<CategoryResponse> categories = categoryRepository.findAllJoinFetch().stream()
			.filter(c -> c.getParent() == null)
			.map(CategoryResponse::from)
			.collect(Collectors.toList());
		return CategoriesResponse.from(categories);
	}
}
