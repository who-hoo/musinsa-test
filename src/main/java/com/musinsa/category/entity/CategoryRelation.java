package com.musinsa.category.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude={"parentCategory", "childCategory", "depth"})
@Entity
public class CategoryRelation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_category_id", nullable = false)
	private Category parentCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "child_category_id", nullable = false)
	private Category childCategory;

	@Column(nullable = false)
	private Long depth;

	public static CategoryRelation of(Long id, Category parentCategory, Category childCategory, Long depth) {
		return new CategoryRelation(id, parentCategory, childCategory, depth);
	}
}
