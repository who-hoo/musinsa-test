package com.musinsa.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.atLeastOnce;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

import com.musinsa.category.dto.CategoriesResponse;
import com.musinsa.category.dto.CategoryResponse;
import com.musinsa.category.entity.Category;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
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
			.hasMessage("존재하지 않는 [카테고리 ID]입니다.");
		verify(categoryRepository, atLeastOnce()).findCategoryAndSubCategoriesByIdJoinFetch(1000L);
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
