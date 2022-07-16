package com.musinsa.category;

import com.musinsa.category.dto.CategoriesResponse;
import com.musinsa.category.dto.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping
	public ResponseEntity<CategoriesResponse> categories() {
		CategoriesResponse categoriesResponse = categoryService.searchAllRootCategories();
		return ResponseEntity.ok(categoriesResponse);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponse> category(@PathVariable Long id) {
		CategoryResponse categoryResponse = categoryService.searchCategory(id);
		return ResponseEntity.ok(categoryResponse);
	}
}
