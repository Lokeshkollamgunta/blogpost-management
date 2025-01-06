package com.blogpost.api;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.blogpost.entity.PostsEntity;
import com.blogpost.model.Post;
import com.blogpost.model.PostInput;
import com.blogpost.service.BlogpostService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(BlogpostApi.class)
class BlogpostAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogpostService blogpostService; // Assuming you have this service class

    @InjectMocks
    private BlogpostApi blogpostApi;

    @Test
    void testCreateBlog() throws Exception {
        // Given
        PostInput request = new PostInput();
        request.setTitle("Test Title");
        request.setContent("Test Content");
        request.setAuthor("Test Author");

        Post savedPost = new Post();
        savedPost.setId(UUID.randomUUID().toString()); // Assuming ID is Long
        savedPost.setTitle("Test Title");
        savedPost.setContent("Test Content");
        savedPost.setAuthor("Test Author");

        when(blogpostService.savePost(any(PostInput.class))).thenReturn(savedPost);

        // When
        ResultActions resultActions = mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Test Title\",\"content\":\"Test Content\",\"author\":\"Test Author\"}"));

        // Then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(savedPost.getId()))
                .andExpect(jsonPath("$.title").value(savedPost.getTitle()))
                .andExpect(jsonPath("$.content").value(savedPost.getContent()))
                .andExpect(jsonPath("$.author").value(savedPost.getAuthor()));

        verify(blogpostService, times(1)).savePost(any(PostInput.class));
    }

    @Test
	void testGetBlog() throws Exception {
		String postId = UUID.randomUUID().toString();
		Post post = new Post();
		post.setId(postId);
		post.setTitle("Test Title");
		post.setContent("Test Content");
		post.setAuthor("Test Author");

		when(blogpostService.getPost(postId)).thenReturn(Optional.of(post));

		ResultActions resultActions = mockMvc.perform(get("/posts/" + postId)
				.accept(MediaType.APPLICATION_JSON));

		resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(post.getId()))
				.andExpect(jsonPath("$.title").value(post.getTitle()))
				.andExpect(jsonPath("$.content").value(post.getContent()))
				.andExpect(jsonPath("$.author").value(post.getAuthor()));

		verify(blogpostService, times(1)).getPost(postId);
	}

	@Test
	void testUpdateBlog() throws Exception {
		String postId = UUID.randomUUID().toString();
		PostInput request = new PostInput();
		request.setTitle("Updated Title");
		request.setContent("Updated Content");
		request.setAuthor("Updated Author");

		Post updatedPost = new Post();
		updatedPost.setId(postId);
		updatedPost.setTitle("Updated Title");
		updatedPost.setContent("Updated Content");
		updatedPost.setAuthor("Updated Author");

		when(blogpostService.updatePost(eq(postId), any(PostInput.class))).thenReturn(updatedPost);

		ResultActions resultActions = mockMvc.perform(put("/posts/" + postId)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"title\":\"Updated Title\",\"content\":\"Updated Content\",\"author\":\"Updated Author\"}"));

		resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(updatedPost.getId()))
				.andExpect(jsonPath("$.title").value(updatedPost.getTitle()))
				.andExpect(jsonPath("$.content").value(updatedPost.getContent()))
				.andExpect(jsonPath("$.author").value(updatedPost.getAuthor()));

		verify(blogpostService, times(1)).updatePost(eq(postId), any(PostInput.class));
	}

	@Test
	void testDeleteBlog() throws Exception {
		String postId = UUID.randomUUID().toString();

		doNothing().when(blogpostService).deletePost(postId);

		ResultActions resultActions = mockMvc.perform(delete("/posts/" + postId));

		resultActions.andExpect(status().isNoContent());

		verify(blogpostService, times(1)).deletePost(postId);
	}

	@Test
	void testGetAllPosts() throws Exception {
		PostsEntity post1 = new PostsEntity();
		post1.setId(UUID.randomUUID().toString());
		post1.setTitle("Title 1");
		post1.setContent("Content 1");
		post1.setAuthor("Author 1");

		PostsEntity post2 = new PostsEntity();
		post2.setId(UUID.randomUUID().toString());
		post2.setTitle("Title 2");
		post2.setContent("Content 2");
		post2.setAuthor("Author 2");

		when(blogpostService.getposts()).thenReturn(Arrays.asList(post1, post2));

		ResultActions resultActions = mockMvc.perform(get("/posts")
				.accept(MediaType.APPLICATION_JSON));

		resultActions.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(post1.getId()))
				.andExpect(jsonPath("$[0].title").value(post1.getTitle()))
				.andExpect(jsonPath("$[0].content").value(post1.getContent()))
				.andExpect(jsonPath("$[0].author").value(post1.getAuthor()))
				.andExpect(jsonPath("$[1].id").value(post2.getId()))
				.andExpect(jsonPath("$[1].title").value(post2.getTitle()))
				.andExpect(jsonPath("$[1].content").value(post2.getContent()))
				.andExpect(jsonPath("$[1].author").value(post2.getAuthor()));

		verify(blogpostService, times(1)).getposts();
	}
}
