package com.musinsa.category;

import com.musinsa.category.dto.CategoriesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping
	public ResponseEntity<CategoriesResponse> categories() {
		CategoriesResponse categoriesResponse = categoryService.searchAllCategories();
		return ResponseEntity.ok(categoriesResponse);
	}
}
