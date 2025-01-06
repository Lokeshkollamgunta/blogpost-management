package com.blogpost;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.blogpost.entity.CategoryEntity;
import com.blogpost.entity.PostsEntity;
import com.blogpost.model.Post;
import com.blogpost.model.PostInput;
import com.blogpost.repository.CategoryRepository;
import com.blogpost.repository.PostRepository;
import com.blogpost.service.BlogpostService;



class BlogpostServiceTest {

	@Mock
	private PostRepository postRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private BlogpostService blogpostService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSavePost() {
		PostInput postInput = new PostInput();
		postInput.setTitle("Test Title");
		postInput.setContent("Test Content");
		postInput.setAuthor("Test Author");
		postInput.setCategories(new HashSet<>(Arrays.asList("Category1", "Category2")));

		PostsEntity savedEntity = new PostsEntity();
		savedEntity.setId(UUID.randomUUID().toString());
		savedEntity.setTitle("Test Title");
		savedEntity.setContent("Test Content");
		savedEntity.setAuthor("Test Author");
		savedEntity.setPublicationDate(Timestamp.valueOf(LocalDateTime.now()));

		when(postRepository.save(any(PostsEntity.class))).thenReturn(savedEntity);

		Post savedPost = blogpostService.savePost(postInput);

		assertNotNull(savedPost);
		assertEquals(savedEntity.getId(), savedPost.getId());
		assertEquals(savedEntity.getTitle(), savedPost.getTitle());
		assertEquals(savedEntity.getContent(), savedPost.getContent());
		assertEquals(savedEntity.getAuthor(), savedPost.getAuthor());
		verify(postRepository, times(1)).save(any(PostsEntity.class));
	}

	//@Test
	void testGetPosts() {
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

		when(postRepository.findAll()).thenReturn(Arrays.asList(post1, post2));

		assertEquals(2, blogpostService.getposts().size());
		verify(postRepository, times(1)).findAll();
	}

	//@Test
	void testGetPost() {
		String postId = UUID.randomUUID().toString();
		PostsEntity postEntity = new PostsEntity();
		postEntity.setId(postId);
		postEntity.setTitle("Test Title");
		postEntity.setContent("Test Content");
		postEntity.setAuthor("Test Author");

		when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));

		Optional<Post> postOptional = blogpostService.getPost(postId);

		assertTrue(postOptional.isPresent());
		Post post = postOptional.get();
		assertEquals(postId, post.getId());
		assertEquals("Test Title", post.getTitle());
		assertEquals("Test Content", post.getContent());
		assertEquals("Test Author", post.getAuthor());
		verify(postRepository, times(1)).findById(postId);
	}

	@Test
	void testUpdatePost() {
		String postId = UUID.randomUUID().toString();
		PostInput postInput = new PostInput();
		postInput.setTitle("Updated Title");
		postInput.setContent("Updated Content");
		postInput.setAuthor("Updated Author");
		postInput.setCategories(new HashSet<>(Arrays.asList("Category1", "Category2")));

		PostsEntity updatedEntity = new PostsEntity();
		updatedEntity.setId(postId);
		updatedEntity.setTitle("Updated Title");
		updatedEntity.setContent("Updated Content");
		updatedEntity.setAuthor("Updated Author");
		updatedEntity.setPublicationDate(Timestamp.valueOf(LocalDateTime.now()));

		when(postRepository.save(any(PostsEntity.class))).thenReturn(updatedEntity);

		Post updatedPost = blogpostService.updatePost(postId, postInput);

		assertNotNull(updatedPost);
		assertEquals(postId, updatedPost.getId());
		assertEquals("Updated Title", updatedPost.getTitle());
		assertEquals("Updated Content", updatedPost.getContent());
		assertEquals("Updated Author", updatedPost.getAuthor());
		verify(postRepository, times(1)).save(any(PostsEntity.class));
	}

	@Test
	void testDeletePost() {
		String postId = UUID.randomUUID().toString();

		doNothing().when(postRepository).deleteById(postId);

		blogpostService.deletePost(postId);

		verify(postRepository, times(1)).deleteById(postId);
	}
}
