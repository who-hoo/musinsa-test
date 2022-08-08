package com.musinsa.category.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Category 클래스")
class CategoryTest {

	@Nested
	@DisplayName("addSubCategory 메서드는")
	class Describe_addSubCategory {

		@Test
		@DisplayName("입력받은 카테고리를 서브 카테고리 목록에 추가하고, 추가된 서브 카테고리의 부모 카테고리를 자기 자신으로 설정한다")
		void 입력받은_카테고리를_서브_카테고리_목록에_추가하고_추가된_서브_카테고리의_부모_카테고리를_자기_자신으로_설정한다() {
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
	}

	@Nested
	@DisplayName("removeSubCategory 메서드는")
	class Describe_removeSubCategory {

		@Test
		@DisplayName("입력받은 카테고리를 서브 카테고리 목록에서 제거하고, 제거된 서브 카테고리의 부모 카테고리를 제거한다")
		void 입력받은_카테고리를_서브_카테고리_목록에서_제거하고_제거된_서브_카테고리의_부모_카테고리를_제거한다() {
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
	}

	@Nested
	@DisplayName("replaceSubCategories 메서드는")
	class Describe_replaceSubCategories {

		@Test
		@DisplayName("기존 서브 카테고리들의 부모 자식 관계를 전부 제거하고, 입력받은 카테고리 목록으로 서브 카테고리를 새로 등록한다")
		void 기존_서브_카테고리들의_부모_자식_관계를_전부_제거하고_입력받은_카테고리_목록으로_서브_카테고리를_새로_등록한다() {
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
	}

	@Nested
	@DisplayName("updateParent 메서드는")
	class Describe_updateParent {

		@Nested
		@DisplayName("기존에 부모 카테고리가 존재하지 않는 경우")
		class Context_with_no_parent_category {

			final Category givenCategory = Category.of(188L, "잡지/무크지", null, null);

			@Test
			@DisplayName("입력받은 카테고리와 부모 자식 관계를 맺는다")
			void 입력받은_카테고리와_부모_자식_관계를_맺는다() {
				//given

				//when
				Category parentCategory = Category.of(21L, "책/음악/티켓", "Culture", null);
				givenCategory.updateParent(parentCategory);

				//then
				assertThat(givenCategory.getParent()).isEqualTo(parentCategory);
				assertThat(parentCategory.getSubCategories()).contains(givenCategory);
			}
		}

		@Nested
		@DisplayName("기존에 부모 카테고리가 존재하는 경우")
		class Context_with_a_parent_category {

			final Category prevParentCategory = Category.of(22L, "반려동물", "Pet", null);
			final Category givenCategory = Category.of(188L, "잡지/무크지", null, null);

			@BeforeEach
			void setUp() {
				prevParentCategory.addSubCategory(givenCategory);
			}

			@Test
			@DisplayName("기존의 부모와 관계를 제거하고 입력받은 카테고리와 부모 자식 관계를 맺는다")
			void 기존의_부모와_관계를_제거하고_입력받은_카테고리와_부모_자식_관계를_맺는다() {
				//given

				//when
				Category newParentCategory = Category.of(21L, "책/음악/티켓", "Culture", null);
				givenCategory.updateParent(newParentCategory);

				//then
				assertThat(givenCategory.getParent()).isEqualTo(newParentCategory);
				assertThat(prevParentCategory.getSubCategories()).doesNotContain(givenCategory);
				assertThat(newParentCategory.getSubCategories()).contains(givenCategory);
			}
		}
	}

	@Nested
	@DisplayName("update 메서드는")
	class Describe_update {

		@Nested
		@DisplayName("null이나 공백 포함 빈 문자열이 함께 입력되면")
		class Context_with_invalid_input {

			@Test
			@DisplayName("null이나 공백 포함 빈 문자열이 아닌 파라미터만 수정한다")
			void null이나_공백_포함_빈_문자열이_아닌_파라미터만_수정한다() {
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
	}
}
