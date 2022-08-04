package com.musinsa.category;

import com.musinsa.category.dto.AddCategoryRequest;
import com.musinsa.category.dto.CategoriesResponse;
import com.musinsa.category.dto.CategoryResponse;
import com.musinsa.category.dto.UpdateCategoryRequest;
import com.musinsa.category.entity.Category;
import com.musinsa.category.exception.ErrorMessage;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@EnableCaching
@Service
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;

	@Cacheable("category")
	@Transactional(readOnly = true)
	public CategoriesResponse searchAllRootCategories() {
		List<CategoryResponse> categories = categoryRepository.findAllJoinFetch().stream()
			.filter(c -> c.getParent() == null)
			.map(CategoryResponse::from)
			.collect(Collectors.toList());
		return CategoriesResponse.from(categories);
	}

	@Cacheable(value = "category")
	@Transactional(readOnly = true)
	public CategoryResponse searchCategory(Long id) {
		return categoryRepository.findCategoryAndSubCategoriesByIdJoinFetch(id).stream()
			.filter(c -> Objects.equals(c.getId(), id))
			.map(CategoryResponse::from)
			.findAny()
			.orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_SUCH_CATEGORY_ID));
	}

	@CacheEvict(value = "category", allEntries = true)
	@Transactional
	public CategoryResponse save(AddCategoryRequest addCategoryRequest) {
		Category newCategory = addCategoryRequest.toEntity();
		if (addCategoryRequest.getParentCategoryId() != null) {
			Category parent = categoryRepository.findById(addCategoryRequest.getParentCategoryId())
				.orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_SUCH_PARENT_CATEGORY_ID));
			newCategory.updateParent(parent);
		}
		return CategoryResponse.from(categoryRepository.save(newCategory));
	}

	@CacheEvict(value = "category", allEntries = true)
	@Transactional
	public CategoryResponse update(Long id, UpdateCategoryRequest updateCategoryRequest) {
		//TODO: category, parent, subCategories 조회 후에 이들의 subCategories를 조회하기 위한 N+1 개선
		Category category = categoryRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_SUCH_CATEGORY_ID));

		Category parent = null;
		if (updateCategoryRequest.getParentCategoryId() != null) {
			parent = categoryRepository.findById(updateCategoryRequest.getParentCategoryId())
				.orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_SUCH_PARENT_CATEGORY_ID));
		}

		List<Category> subCategories = null;
		if (updateCategoryRequest.getSubCategoryIdList() != null) {
			subCategories = categoryRepository.findAllById(updateCategoryRequest.getSubCategoryIdList());
		}

		Category updatedCategory = category.update(
			updateCategoryRequest.getKorName(),
			updateCategoryRequest.getEngName(),
			parent,
			subCategories
		);
		return CategoryResponse.from(updatedCategory);
	}

	@CacheEvict(value = "category", allEntries = true)
	@Transactional
	public void delete(Long id, boolean withSubCategories) {
		Category category = categoryRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_SUCH_CATEGORY_ID));
		if (!withSubCategories) {
			category.replaceSubCategories(Collections.emptyList());
		}
		categoryRepository.delete(category);
	}
}
