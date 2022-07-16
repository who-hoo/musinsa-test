package com.musinsa.category.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class CategoryTest {

	@Test
	void addSubCategory_메서드는_입력받은_카테고리를_서브_카테고리_목록에_추가하고_추가된_서브_카테고리의_부모_카테고리를_자기_자신으로_설정한다() {
		//given
		Category category = Category.of(21L, "책/음악/티켓", "Culture", null);
		Category subCategory1 = Category.of(188L, "잡지/무크지", null, category);
		Category subCategory2 = Category.of(189L, "기타 컬처", null, category);

		//when
		category.addSubCategory(subCategory1);
		category.addSubCategory(subCategory2);

		//then
		assertThat(category.getSubCategories())
			.isNotNull()
			.hasSize(2)
			.contains(subCategory1, subCategory2);
		assertThat(subCategory1.getParent()).isEqualTo(category);
		assertThat(subCategory2.getParent()).isEqualTo(category);
	}

	@Test
	void removeSubCategory_메서드는_입력받은_카테고리를_서브_카테고리_목록에서_제거하고_제거된_서브_카테고리의_부모_카테고리를_제거한다() {
		//given
		Category category = Category.of(21L, "책/음악/티켓", "Culture", null);
		Category subCategory1 = Category.of(188L, "잡지/무크지", null, category);
		Category subCategory2 = Category.of(189L, "기타 컬처", null, category);
		category.addSubCategory(subCategory1);
		category.addSubCategory(subCategory2);

		//when
		category.removeSubCategory(subCategory1);

		//then
		assertThat(category.getSubCategories())
			.isNotNull()
			.hasSize(1)
			.contains(subCategory2);
		assertThat(subCategory1.getParent()).isNull();
		assertThat(subCategory2.getParent()).isEqualTo(category);
	}

	@Test
	void replaceSubCategories_메서드는_기존_서브_카테고리들의_부모_자식_관계를_전부_제거하고_입력받은_카테고리_목록으로_서브_카테고리를_새로_등록한다() {
		//given
		Category category = Category.of(21L, "책/음악/티켓", "Culture", null);
		Category subCategory1 = Category.of(188L, "잡지/무크지", null, category);
		Category subCategory2 = Category.of(189L, "기타 컬처", null, category);
		Category subCategory3 = Category.of(190L, "LP", null, null);
		category.addSubCategory(subCategory1);
		category.addSubCategory(subCategory2);

		//when
		List<Category> newSubCategories = List.of(subCategory2, subCategory3);
		category.replaceSubCategories(newSubCategories);

		//then
		assertThat(category.getSubCategories())
			.isNotNull()
			.hasSize(2)
			.contains(subCategory2, subCategory3);
		assertThat(subCategory1.getParent()).isNull();
		assertThat(subCategory2.getParent()).isEqualTo(category);
		assertThat(subCategory3.getParent()).isEqualTo(category);
	}

	@Test
	void updateParent_메서드는_기존에_부모_카테고리가_존재하지_않는_경우_입력받은_카테고리와_부모_자식_관계를_맺는다() {
		//given
		Category subCategory = Category.of(188L, "잡지/무크지", null, null);

		//when
		Category parentCategory = Category.of(21L, "책/음악/티켓", "Culture", null);
		subCategory.updateParent(parentCategory);

		//then
		assertThat(subCategory.getParent()).isEqualTo(parentCategory);
		assertThat(parentCategory.getSubCategories()).contains(subCategory);
	}

	@Test
	void updateParent_메서드는_기존에_부모_카테고리가_존재하는_경우_기존의_부모와_관계를_제거하고_입력받은_카테고리와_부모_자식_관계를_맺는다() {
		//given
		Category prevParentCategory = Category.of(22L, "반려동물", "Pet", null);
		Category subCategory = Category.of(188L, "잡지/무크지", null, null);
		prevParentCategory.addSubCategory(subCategory);

		//when
		Category newParentCategory = Category.of(21L, "책/음악/티켓", "Culture", null);
		subCategory.updateParent(newParentCategory);

		//then
		assertThat(subCategory.getParent()).isEqualTo(newParentCategory);
		assertThat(prevParentCategory.getSubCategories()).doesNotContain(subCategory);
		assertThat(newParentCategory.getSubCategories()).contains(subCategory);
	}

	@Test
	void update_메서드는_null이나_공백_포함_빈_문자열이_입력되면_수정되지_않는다() {
		//given
		Category category = Category.of(22L, "반려동물", "Pet", null);

		//when
		category.update(" ", "Pet", null, null);

		//then
		assertThat(category.getKorName()).isNotNull().isEqualTo("반려동물");
		assertThat(category.getEngName()).isNotBlank().isEqualTo("Pet");
		assertThat(category.getParent()).isNull();
		assertThat(category.getSubCategories()).isEmpty();
	}

	@Test
	void update_메서드는_null이나_공백_포함_빈_문자열이_아닌_파라미터만_수정한다() {
		//given
		Category parentCategory = Category.of(21L, "책/음악/티켓", "Culture", null);
		Category subCategory = Category.of(189L, "기타 컬처", null, parentCategory);
		parentCategory.addSubCategory(subCategory);
		parentCategory.addSubCategory(subCategory);

		//when
		Category newSubCategory1 = Category.of(190L, "공연 티켓", null, subCategory);
		Category newSubCategory2 = Category.of(191L, "전시회 티켓", null, subCategory);
		subCategory.update(null, "etc", null, List.of(newSubCategory1, newSubCategory2));

		//then
		assertThat(subCategory.getKorName()).isEqualTo("기타 컬처");
		assertThat(subCategory.getEngName()).isEqualTo("etc");
		assertThat(subCategory.getParent()).isEqualTo(parentCategory);
		assertThat(subCategory.getSubCategories()).hasSize(2).contains(newSubCategory1, newSubCategory2);
	}
}
