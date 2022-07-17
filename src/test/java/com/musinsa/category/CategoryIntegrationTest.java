package com.musinsa.category;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.category.dto.AddCategoryRequest;
import com.musinsa.category.dto.UpdateCategoryRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class CategoryIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void 카테고리_등록_성공() throws Exception {
		//given
		AddCategoryRequest request = AddCategoryRequest.of("전시회 티켓", null, 21L);

		//when
		ResultActions resultActions = mvc.perform(post("/api/categories")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
			.accept(MediaType.APPLICATION_JSON)).andDo(print());

		//then
		resultActions
			.andExpect(status().isCreated())
			.andExpect(jsonPath("category_id", is(194)))
			.andExpect(jsonPath("category_name", is("전시회 티켓")))
			.andExpect(jsonPath("category_english_name", nullValue()))
			.andExpect(jsonPath("parent_category_id", is(21)))
			.andExpect(jsonPath("sub_categories", emptyIterable()));
	}

	@Test
	void korName이_null_또는_공백포함_빈_문자열이면_카테고리_등록_실패() throws Exception {
		//given
		AddCategoryRequest request = AddCategoryRequest.of(" ", null, 21L);

		//when
		ResultActions resultActions = mvc.perform(post("/api/categories")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
			.accept(MediaType.APPLICATION_JSON)).andDo(print());

		//then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("message", is("카테고리 한글이름(kor_name)은 필수 입력 값(공백 불가)입니다.")));
	}

	@Test
	void parentCategoryId가_존재하지_않는_카테고리의_아이디면_카테고리_등록_실패() throws Exception {
		//given
		AddCategoryRequest request = AddCategoryRequest.of("전시회 티켓", null, 1L);

		//when
		ResultActions resultActions = mvc.perform(post("/api/categories")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
			.accept(MediaType.APPLICATION_JSON)).andDo(print());

		//then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("message", is("상위 카테고리의 ID가 존재하지 않는 [카테고리 ID]입니다.")));
	}

	@Test
	void 카테고리_수정_성공() throws Exception {
		//given
		UpdateCategoryRequest request =
			UpdateCategoryRequest.of("문화", "Culture", null, List.of(189L));

		//when
		ResultActions resultActions = mvc.perform(patch("/api/categories/{id}", 21L)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
			.accept(MediaType.APPLICATION_JSON)).andDo(print());

		//then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("category_id", is(21)))
			.andExpect(jsonPath("category_name", is("문화")))
			.andExpect(jsonPath("category_english_name", is("Culture")))
			.andExpect(jsonPath("parent_category_id", nullValue()))
			.andExpect(jsonPath("sub_categories", iterableWithSize(1)));
	}

	@Test
	void id가_존재하지_않는_카테고리를_수정하면_카테고리_수정_실패() throws Exception {
		//given
		UpdateCategoryRequest request =
			UpdateCategoryRequest.of("문화", "Culture", null, List.of(189L));

		//when
		ResultActions resultActions = mvc.perform(patch("/api/categories/{id}", 1L)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
			.accept(MediaType.APPLICATION_JSON)).andDo(print());

		//then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("message", is("존재하지 않는 [카테고리 ID]입니다.")));
	}

	@Test
	void parentCategoryId가_존재하지_않는_카테고리의_아이디면_카테고리_수정_실패() throws Exception {
		//given
		UpdateCategoryRequest request =
			UpdateCategoryRequest.of("문화", "Culture", 1L, List.of(189L));

		//when
		ResultActions resultActions = mvc.perform(patch("/api/categories/{id}", 21L)
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request))
			.accept(MediaType.APPLICATION_JSON)).andDo(print());

		//then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("message", is("상위 카테고리의 ID가 존재하지 않는 [카테고리 ID]입니다.")));
	}

	@Test
	void 카테고리_삭제_성공() throws Exception {
		//given

		//when
		ResultActions resultActions = mvc.perform(delete("/api/categories/{id}", 21L)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)).andDo(print());

		//then
		resultActions
			.andExpect(status().isOk());
	}

	@Test
	void id가_존재하지_않는_카테고리를_삭제하면_카테고리_삭제_실패() throws Exception {
		//given

		//when
		ResultActions resultActions = mvc.perform(delete("/api/categories/{id}", 1L)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)).andDo(print());

		//then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("message", is("존재하지 않는 [카테고리 ID]입니다.")));
	}

	@Test
	void 카테고리_조회_성공() throws Exception {
		//given

		//when
		ResultActions resultActions = mvc.perform(get("/api/categories/{id}", 21L)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)).andDo(print());

		//then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("category_id", is(21)))
			.andExpect(jsonPath("category_name", is("책/음악/티켓")))
			.andExpect(jsonPath("category_english_name", is("Culture")))
			.andExpect(jsonPath("parent_category_id", nullValue()))
			.andExpect(jsonPath("sub_categories", iterableWithSize(2)));
	}

	@Test
	void id가_존재하지_않는_카테고리를_조회하면_카테고리_조회_실패() throws Exception {
		//given

		//when
		ResultActions resultActions = mvc.perform(get("/api/categories/{id}", 1L)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)).andDo(print());

		//then
		resultActions
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("message", is("존재하지 않는 [카테고리 ID]입니다.")));
	}

	@Test
	void 카테고리_전체조회_성공() throws Exception {
		//given

		//when
		ResultActions resultActions = mvc.perform(get("/api/categories")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)).andDo(print());

		//then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("categories", iterableWithSize(2)))
			.andExpect(jsonPath("categories[0].category_id", is(21)))
			.andExpect(jsonPath("categories[0].category_name", is("책/음악/티켓")))
			.andExpect(jsonPath("categories[0].category_english_name", is("Culture")))
			.andExpect(jsonPath("categories[0].parent_category_id", nullValue()))
			.andExpect(jsonPath("categories[0].sub_categories", iterableWithSize(2)))
			.andExpect(jsonPath("categories[1].category_id", is(22)))
			.andExpect(jsonPath("categories[1].category_name", is("반려동물")))
			.andExpect(jsonPath("categories[1].category_english_name", is("Pet")))
			.andExpect(jsonPath("categories[1].parent_category_id", nullValue()))
			.andExpect(jsonPath("categories[1].sub_categories", iterableWithSize(2)))
			.andExpect(jsonPath("categories[1].sub_categories[0].sub_categories", emptyIterable()))
			.andExpect(jsonPath("categories[1].sub_categories[1].sub_categories", iterableWithSize(2)));
	}
}
