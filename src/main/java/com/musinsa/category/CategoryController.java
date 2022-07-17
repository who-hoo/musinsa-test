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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	/**
	 * [카테고리 전체 조회] GET /api/categories
	 *
	 * @return 모든 카테고리를 계층 구조로 반환합니다.
	 */
	@GetMapping
	public ResponseEntity<CategoriesResponse> categories() {
		CategoriesResponse categoriesResponse = categoryService.searchAllRootCategories();
		return ResponseEntity.ok(categoriesResponse);
	}

	/**
	 * [카테고리 조회] GET /api/categories/{id}
	 *
	 * @param id 조회할 카테고리 아이디
	 * @return 200 OK 유효한 아이디가 입력된 경우, 아이디에 해당하는 카테고리와 해당 카테고리 하위의 모든 카테고리를 계층 구조로 반환합니다. <br>
	 *         400 BAD_REQUEST 유효하지 않은 아이디가 입력된 경우, 존재하지 않는 카테고리 아이디라는 메시지를 반환합니다.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<CategoryResponse> category(@PathVariable Long id) {
		CategoryResponse categoryResponse = categoryService.searchCategory(id);
		return ResponseEntity.ok(categoryResponse);
	}

	/**
	 * [카테고리 등록] POST /api/categories
	 *
	 * @param addCategoryRequest 카테고리 등록 정보
	 * @return 201 CREATED 요청이 유효한 경우, 카테고리를 등록하고 등록된 카테고리 정보를 반환합니다. <br>
	 *         400 BAD_REQUEST 요청이 유효하지 않은 경우, 유효하지 않은 요청 정보에 대한 메시지를 반환합니다.
	 */
	@PostMapping
	public ResponseEntity<CategoryResponse> add(@Valid @RequestBody AddCategoryRequest addCategoryRequest) {
		log.debug("request : {}", addCategoryRequest);
		CategoryResponse savedCategory = categoryService.save(addCategoryRequest);
		return ResponseEntity
			.created(URI.create("http://localhost:8080/api/categories/" + savedCategory.getCategoryId()))
			.body(savedCategory);
	}

	/**
	 * [카테고리 수정] PATCH /api/categories/{id}
	 *
	 * @param id                    수정할 카테고리 아이디
	 * @param updateCategoryRequest 카테고리 수정 정보
	 * @return 200 OK 요청이 유효한 경우, 카테고리를 수정하고 수정된 카테고리 정보를 반환합니다. <br>
	 *         400 BAD_REQUEST 요청이 유효하지 않은 경우, 유효하지 않은 요청 정보에 대한 메시지를 반환합니다.
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<CategoryResponse> update(
		@PathVariable Long id,
		@RequestBody UpdateCategoryRequest updateCategoryRequest) {

		log.debug("request : {}", updateCategoryRequest);
		CategoryResponse categoryResponse = categoryService.update(id, updateCategoryRequest);
		return ResponseEntity.ok(categoryResponse);
	}

	/**
	 * [카테고리 삭제] DELETE /api/categories/{id}
	 *
	 * @param id                삭제할 카테고리 아이디
	 * @param withSubCategories ("true" or "false") 하위 카테고리 삭제 여부
	 * @return 200 OK 유효한 아이디가 입력된 경우. <br>
	 *         400 BAD_REQUEST 유효하지 않은 아이디가 입력된 경우.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(
		@PathVariable Long id,
		@RequestParam(defaultValue = "false") boolean withSubCategories) {

		categoryService.delete(id, withSubCategories);
		return ResponseEntity.ok().build();
	}
}
