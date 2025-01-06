package com.blogpost.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blogpost.entity.CategoryEntity;
import com.blogpost.entity.PostsEntity;
import com.blogpost.model.Post;
import com.blogpost.model.PostInput;
import com.blogpost.repository.CategoryRepository;
import com.blogpost.repository.PostRepository;

@Service
public class BlogpostService {

	@Autowired
	PostRepository postRepository;

	@Autowired
	CategoryRepository categoryRepository;

	public Post savePost(PostInput post) {

		PostsEntity pe = new PostsEntity();
		pe.setAuthor(post.getAuthor());
		pe.setContent(post.getContent());
		pe.setId(UUID.randomUUID().toString());
		pe.setPublicationDate(Timestamp.valueOf(LocalDateTime.now()));
		pe.setTitle(post.getTitle());
		Set<CategoryEntity> ceSet = buildCategories(post, pe);
		pe.setCategories(ceSet);
		pe = postRepository.save(pe);
		return buildPost(pe, post);

	}

	public List<PostsEntity> getposts() {
		return postRepository.findAll();
	}

	public Optional<Post> getPost(String postId) {
		Optional<PostsEntity> postOptional = postRepository.findById(postId);
		PostsEntity pe = postOptional.get();
		return Optional.of(buildPostObj(pe));
	}

	public Post updatePost(String postId, @Valid PostInput post) {

		PostsEntity pe = new PostsEntity();
		pe.setAuthor(post.getAuthor());
		pe.setContent(post.getContent());
		pe.setId(postId);
		pe.setPublicationDate(Timestamp.valueOf(LocalDateTime.now()));
		pe.setTitle(post.getTitle());
		Set<CategoryEntity> ceSet = buildCategories(post, pe);
		pe.setCategories(ceSet);
		pe = postRepository.save(pe);
		return buildPost(pe, post);

	}

	public void deletePost(String postId) {
		postRepository.deleteById(postId);

	}

	private Post buildPost(PostsEntity pe, PostInput postInput) {
		Post post = new Post();
		post.setAuthor(pe.getAuthor());
		post.setCategories(postInput.getCategories());
		post.setContent(pe.getContent());
		post.setId(pe.getId());
		post.setPublicationDate(pe.getPublicationDate().toString());
		post.setTitle(pe.getTitle());
		return post;
	}

	private Set<CategoryEntity> buildCategories(PostInput post, PostsEntity pe) {
		Set<CategoryEntity> ceSet = new HashSet<CategoryEntity>();

		for (String category : post.getCategories()) {
			CategoryEntity ce = new CategoryEntity();
			ce.setName(category);
			Set<PostsEntity> peSet = new HashSet<PostsEntity>();
			peSet.add(pe);
			ceSet.add(ce);
		}

		return ceSet;
	}

	private Post buildPostObj(PostsEntity pe) {
		Post post = new Post();
		post.setAuthor(pe.getAuthor());
		Set<String> categories = pe.getCategories().stream().map(CategoryEntity::getName).collect(Collectors.toSet());
		post.setCategories(categories);
		post.setContent(pe.getContent());
		post.setId(pe.getId());
		post.setPublicationDate(pe.getPublicationDate().toString());
		post.setTitle(pe.getTitle());
		return post;
	}

}
