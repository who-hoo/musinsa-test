package com.musinsa.category.entity;

import com.musinsa.category.exception.ErrorMessage;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude = {"korName", "engName", "parent", "subCategories"})
@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long id;

	@Column(nullable = false)
	private String korName;

	@Column
	private String engName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_category_id")
	private Category parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
	private List<Category> subCategories = new ArrayList<>();

	public void updateParent(Category parent) {
		if (this.parent != null) {
			this.parent.removeSubCategory(this);
		}
		parent.addSubCategory(this);
	}

	public void addSubCategory(Category subCategory) {
		subCategory.parent = this;
		this.subCategories.add(subCategory);
	}

	public void removeSubCategory(Category subCategory) {
		subCategory.parent = null;
		this.subCategories.remove(subCategory);
	}

	public void replaceSubCategories(List<Category> subCategories) {
		//TODO: 내부 로직 성능 개선(subCategories 한번에 clear 할 수 있도록)
		while (!this.subCategories.isEmpty()) {
			this.removeSubCategory(this.subCategories.get(0));
		}
		for (Category newSubCategory : subCategories) {
			this.addSubCategory(newSubCategory);
		}
	}

	public Category update(String korName, String engName, Category parent, List<Category> subCategories) {
		if (korName != null && !korName.isBlank()) {
			this.korName = korName;
		}
		if (engName != null && engName.trim().equals("")) {
			throw new IllegalArgumentException(ErrorMessage.ILLEGAL_CATEGORY_ENG_NAME);
		}
		this.engName = engName;
		if (parent != null) {
			this.updateParent(parent);
		}
		if (subCategories != null) {
			this.replaceSubCategories(subCategories);
		}
		return this;
	}

	public static Category of(Long id, String korName, String engName, Category parent) {
		return new Category(id, korName, engName, parent, new ArrayList<>());
	}

	public static Category of(String korName, String engName) {
		return new Category(null, korName, engName, null, new ArrayList<>());
	}
}
