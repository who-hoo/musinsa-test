package com.musinsa.category;

import com.musinsa.category.dto.AddCategoryRequest;
import com.musinsa.category.dto.CategoriesResponse;
import com.musinsa.category.dto.CategoryResponse;
import com.musinsa.category.dto.UpdateCategoryRequest;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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

	@PostMapping
	public ResponseEntity<CategoryResponse> add(@Valid @RequestBody AddCategoryRequest addCategoryRequest) {
		log.debug("request : {}", addCategoryRequest);
		CategoryResponse savedCategory = categoryService.save(addCategoryRequest);
		return ResponseEntity
			.created(URI.create("http://localhost:8080/api/categories/" + savedCategory.getCategoryId()))
			.body(savedCategory);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<CategoryResponse> update(
		@PathVariable Long id,
		@RequestBody UpdateCategoryRequest updateCategoryRequest) {

		log.debug("request : {}", updateCategoryRequest);
		CategoryResponse categoryResponse = categoryService.update(id, updateCategoryRequest);
		return ResponseEntity.ok(categoryResponse);
	}
}
