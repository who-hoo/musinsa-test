package com.musinsa.category;

import static org.assertj.core.api.Assertions.assertThat;

import com.musinsa.category.entity.Category;
import com.musinsa.category.entity.CategoryRelation;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase
class CategoryRepositoryTest {

	@Autowired CategoryRepository categoryRepository;

	@Test
	void 전체_카테고리를_조회하면_자신의_하위_카테고리_정보를_포함하는_전체_카테고리가_반환된다() {
		//given
		Category category1 = Category.of(21L, "책/음악/티켓", "Culture");
		Category category2 = Category.of(22L, "반려동물", "Pet");
		Category category3 = Category.of(188L, "잡지/무크지", null);
		Category category4 = Category.of(189L, "기타 컬처", null);
		Category category5 = Category.of(190L, "반려동물 의류", null);
		Category category6 = Category.of(191L, "반려동물용품", null);
		Category category7 = Category.of(192L, "반려동물간식", null);
		Category category8 = Category.of(193L, "반려동물장난감", null);
		CategoryRelation categoryRelation1 = CategoryRelation.of(1L, category1, category3, 1L);
		CategoryRelation categoryRelation2 = CategoryRelation.of(2L, category1, category4, 1L);
		CategoryRelation categoryRelation3 = CategoryRelation.of(3L, category2, category5, 1L);
		CategoryRelation categoryRelation4 = CategoryRelation.of(4L, category2, category6, 1L);
		CategoryRelation categoryRelation5 = CategoryRelation.of(5L, category6, category7, 1L);
		CategoryRelation categoryRelation6 = CategoryRelation.of(6L, category6, category8, 1L);
		category1.getSubCategories().add(categoryRelation1);
		category1.getSubCategories().add(categoryRelation2);
		category2.getSubCategories().add(categoryRelation3);
		category2.getSubCategories().add(categoryRelation4);
		category6.getSubCategories().add(categoryRelation5);
		category6.getSubCategories().add(categoryRelation6);

		//when
		List<Category> actual = categoryRepository.findAllJoinFetch();

		//then
		assertThat(actual)
			.isNotNull()
			.hasSize(8)
			.contains(category1, category2, category3, category4, category5, category6, category7, category8);
	}
}
