package com.blogpost.api;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import com.blogpost.model.Post;
import com.blogpost.model.PostInput;
import com.blogpost.service.BlogpostService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(BlogpostApi.class)
public class BlogpostAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogpostService blogpostService; // Assuming you have this service class

    @InjectMocks
    private BlogpostApi blogpostApi;

    @Test
    public void testCreateBlog() throws Exception {
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
}
