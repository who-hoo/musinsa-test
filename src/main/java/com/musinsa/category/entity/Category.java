package com.musinsa.category.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude={"korName", "engName", "subCategories"})
@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String korName;

	@Column
	private String engName;

	@OneToMany(mappedBy = "parentCategory")
	private List<CategoryRelation> subCategories = new ArrayList<>();

	public static Category of(Long id, String korName, String engName) {
		return new Category(id, korName, engName, new ArrayList<>());
	}
}
