package com.musinsa.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.atLeastOnce;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.never;

import com.musinsa.category.dto.AddCategoryRequest;
import com.musinsa.category.dto.CategoriesResponse;
import com.musinsa.category.dto.CategoryResponse;
import com.musinsa.category.dto.UpdateCategoryRequest;
import com.musinsa.category.entity.Category;
import com.musinsa.category.exception.ErrorMessage;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

	@InjectMocks
	private CategoryService categoryService;

	@Mock
	private CategoryRepository categoryRepository;

	@Test
	void 전체_루트_카테고리를_조회하면_전체_카테고리_중_depth가_0인_루트_카테고리의_하위_카테고리_정보를_포함하는_목록이_반환된다() {
		//given
		List<Category> categoryList = createAllCategoryList();
		given(categoryRepository.findAllJoinFetch())
			.willReturn(categoryList);

		//when
		CategoriesResponse actual = categoryService.searchAllRootCategories();

		//then
		verify(categoryRepository, atLeastOnce()).findAllJoinFetch();
		assertThat(actual.getCategories()).hasSize(2);
		assertThat(actual.getCategories().get(0).getSubCategories()).hasSize(2);
		assertThat(actual.getCategories().get(1).getSubCategories()).hasSize(2);
		assertThat(actual.getCategories().get(1).getSubCategories().get(0).getSubCategories()).hasSize(0);
		assertThat(actual.getCategories().get(1).getSubCategories().get(1).getSubCategories()).hasSize(2);
	}

	@Test
	void 존재하는_카테코리_아이디로_카테고리를_조회하면_해당_아이디를_가진_카테고리와_하위_카테고리의_목록이_반환된다() {
		//given
		List<Category> categoryList = createCategoryAndSubCategoriesList();
		given(categoryRepository.findCategoryAndSubCategoriesByIdJoinFetch(21L))
			.willReturn(categoryList);

		//when
		CategoryResponse actual = categoryService.searchCategory(21L);

		//then
		verify(categoryRepository, atLeastOnce()).findCategoryAndSubCategoriesByIdJoinFetch(21L);
		assertThat(actual).isNotNull();
		assertThat(actual.getCategoryId()).isEqualTo(21L);
		assertThat(actual.getSubCategories()).hasSize(2);
	}

	@Test
	void 존재하지_않는_카테코리_아이디로_카테고리를_조회하면_존재하지_않는_카테고리_아이디라는_메시지와_함께_NoSuchElementException이_반환된다() {
		//given
		given(categoryRepository.findCategoryAndSubCategoriesByIdJoinFetch(1000L))
			.willReturn(Collections.emptyList());

		//when

		//then
		assertThatThrownBy(() -> categoryService.searchCategory(1000L))
			.isInstanceOf(NoSuchElementException.class)
			.hasMessage(ErrorMessage.NO_SUCH_CATEGORY_ID);
		verify(categoryRepository, atLeastOnce()).findCategoryAndSubCategoriesByIdJoinFetch(1000L);
	}

	@Test
	void 상위_카테고리_아이디를_지정하지_않고_카테고리를_등록하면_상위_카테고리가_없는_카테고리가_저장되고_저장된_카테고리의_정보가_반환된다() {
		//given
		Category newCategory = Category.of(null, "상의", "Top", null);
		Category savedCategory = Category.of(9L, "상의", "Top", null);
		given(categoryRepository.save(newCategory))
			.willReturn(savedCategory);
		CategoryResponse expected = CategoryResponse.from(savedCategory);

		//when
		AddCategoryRequest request = AddCategoryRequest.of("상의", "Top", null);
		CategoryResponse actual = categoryService.save(request);

		//then
		verify(categoryRepository, never()).findById(request.getParentCategoryId());
		verify(categoryRepository, atLeastOnce()).save(request.toEntity());
		assertThat(actual.getCategoryId()).isEqualTo(expected.getCategoryId());
		assertThat(actual.getParentCategoryId()).isNull();
	}

	@Test
	void 존재하는_상위_카테고리_아이디를_지정하고_카테고리를_등록하면_해당하는_상위_카테고리를_가진_카테고리가_저장되고_저장된_카테고리의_정보가_반환된다() {
		//given
		Category parentCategory = Category.of(21L, "책/음악/티켓", "Culture", null);
		Category newCategory = Category.of(null, "개발 서적", "", parentCategory);
		Category savedCategory = Category.of(9L, "개발 서적", "", parentCategory);
		given(categoryRepository.findById(21L))
			.willReturn(Optional.of(parentCategory));
		given(categoryRepository.save(newCategory))
			.willReturn(savedCategory);
		CategoryResponse expected = CategoryResponse.from(savedCategory);

		//when
		AddCategoryRequest request = AddCategoryRequest.of("개발 서적", "", 21L);
		CategoryResponse actual = categoryService.save(request);

		//then
		verify(categoryRepository, atLeastOnce()).findById(request.getParentCategoryId());
		verify(categoryRepository, atLeastOnce()).save(request.toEntity());
		assertThat(actual.getCategoryId()).isEqualTo(expected.getCategoryId());
		assertThat(actual.getParentCategoryId()).isEqualTo(expected.getParentCategoryId());
	}

	@Test
	void 존재하지_않는_상위_카테고리_아이디를_지정하고_카테고리를_등록하면_존재하지_않는_상위_카테고리_아이디라는_메시지와_함께_NoSuchElementException이_반환된다() {
		//given
		given(categoryRepository.findById(1000L))
			.willReturn(Optional.empty());

		//when
		AddCategoryRequest request = AddCategoryRequest.of("개발 서적", "", 1000L);

		//then
		assertThatThrownBy(() -> categoryService.save(request))
			.isInstanceOf(NoSuchElementException.class)
			.hasMessage(ErrorMessage.NO_SUCH_PARENT_CATEGORY_ID);
		verify(categoryRepository, atLeastOnce()).findById(request.getParentCategoryId());
		verify(categoryRepository, never()).save(request.toEntity());
	}

	@Test
	void 존재하지_않는_카테고리를_수정하면_존재하지_않는_카테고리_아이디라는_메시지와_함께_NoSuchElementException이_반환된다() {
		//given
		given(categoryRepository.findById(1000L))
			.willReturn(Optional.empty());

		//when
		UpdateCategoryRequest request =
			UpdateCategoryRequest.of("문화", "Culture", null, List.of(188L));

		//then
		assertThatThrownBy(() -> categoryService.update(1000L, request))
			.isInstanceOf(NoSuchElementException.class)
			.hasMessage(ErrorMessage.NO_SUCH_CATEGORY_ID);
		verify(categoryRepository, atLeastOnce()).findById(1000L);
		verify(categoryRepository, never()).findById(request.getParentCategoryId());
		verify(categoryRepository, never()).findAllById(request.getSubCategoryIdList());
	}

	@Test
	void 존재하지_않는_상위_카테고리_아이디를_지정하고_카테고리를_수정하면_존재하지_않는_상위_카테고리_아이디라는_메시지와_함께_NoSuchElementException이_반환된다() {
		//given
		Category parentCategory = Category.of(21L, "책/음악/티켓", "Culture", null);
		Category subCategory1 = Category.of(188L, "잡지/무크지", null, parentCategory);
		Category subCategory2 = Category.of(189L, "기타 컬처", null, parentCategory);
		parentCategory.addSubCategory(subCategory1);
		parentCategory.addSubCategory(subCategory2);
		given(categoryRepository.findById(21L))
			.willReturn(Optional.of(parentCategory));
		given(categoryRepository.findById(1000L))
			.willReturn(Optional.empty());

		//when
		UpdateCategoryRequest request =
			UpdateCategoryRequest.of("문화", "Culture", 1000L, List.of(188L));

		//then
		assertThatThrownBy(() -> categoryService.update(21L, request))
			.isInstanceOf(NoSuchElementException.class)
			.hasMessage(ErrorMessage.NO_SUCH_PARENT_CATEGORY_ID);
		verify(categoryRepository, atLeastOnce()).findById(21L);
		verify(categoryRepository, atLeastOnce()).findById(request.getParentCategoryId());
		verify(categoryRepository, never()).findAllById(request.getSubCategoryIdList());
	}

	@Test
	void 존재하지_않는_서브_카테고리_아이디를_포함한_서브_카테고리_아이디_목록을_지정하고_카테고리를_수정하면_존재하는_서브_카테고리들로만_수정된다() {
		//given
		Category parentCategory = Category.of(21L, "책/음악/티켓", "Culture", null);
		Category subCategory1 = Category.of(188L, "잡지/무크지", null, parentCategory);
		Category subCategory2 = Category.of(189L, "기타 컬처", null, parentCategory);
		parentCategory.addSubCategory(subCategory1);
		parentCategory.addSubCategory(subCategory2);
		given(categoryRepository.findById(21L))
			.willReturn(Optional.of(parentCategory));
		given(categoryRepository.findAllById(List.of(188L, 1000L)))
			.willReturn(List.of(subCategory1));

		//when
		UpdateCategoryRequest request =
			UpdateCategoryRequest.of("문화", null, null, List.of(188L, 1000L));
		CategoryResponse actual = categoryService.update(21L, request);

		//then
		verify(categoryRepository, atLeastOnce()).findById(21L);
		verify(categoryRepository, never()).findById(request.getParentCategoryId());
		verify(categoryRepository, atLeastOnce()).findAllById(request.getSubCategoryIdList());
		assertThat(actual.getCategoryId()).isEqualTo(21L);
		assertThat(actual.getCategoryName()).isEqualTo("문화");
		assertThat(actual.getCategoryEnglishName()).isNull();
		assertThat(actual.getSubCategories())
			.hasSize(1)
			.contains(CategoryResponse.from(subCategory1))
			.doesNotContain(CategoryResponse.from(subCategory2));
	}

	private List<Category> createAllCategoryList() {
		Category category1 = Category.of(21L, "책/음악/티켓", "Culture", null);
		Category category2 = Category.of(22L, "반려동물", "Pet", null);
		Category category3 = Category.of(188L, "잡지/무크지", null, category1);
		Category category4 = Category.of(189L, "기타 컬처", null, category1);
		Category category5 = Category.of(190L, "반려동물 의류", null, category2);
		Category category6 = Category.of(191L, "반려동물용품", null, category2);
		Category category7 = Category.of(192L, "반려동물간식", null, category6);
		Category category8 = Category.of(193L, "반려동물장난감", null, category6);
		category1.addSubCategory(category3);
		category1.addSubCategory(category4);
		category2.addSubCategory(category5);
		category2.addSubCategory(category6);
		category6.addSubCategory(category7);
		category6.addSubCategory(category8);

		return List.of(category1, category2, category3, category4, category5, category6, category7, category8);
	}

	private List<Category> createCategoryAndSubCategoriesList() {
		Category category = Category.of(21L, "책/음악/티켓", "Culture", null);
		Category subCategory1 = Category.of(188L, "잡지/무크지", null, category);
		Category subCategory2 = Category.of(189L, "기타 컬처", null, category);
		category.addSubCategory(subCategory1);
		category.addSubCategory(subCategory2);

		return List.of(category, subCategory1, subCategory2);
	}
}
