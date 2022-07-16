package com.musinsa.category;

import com.musinsa.category.entity.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	@Query("select distinct c "
		+ "from Category c "
		+ "left join fetch c.subCategories ")
	List<Category> findAllJoinFetch();
}
