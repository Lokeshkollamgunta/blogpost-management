package com.blogpost.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

	
	// post
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

	// get all posts
	public List<PostsEntity> getposts() {
		return postRepository.findAll();
	}
	
//	public List<PostsEntity> getPostsByAuthor(){
//		return postRepository.findAll();
//	}

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

	// get post by id
	public Post getPost(String postId) {
		Optional<PostsEntity> postOptional = postRepository.findById(postId);
		PostsEntity pe = postOptional.get();
		return buildPostObj(pe);
	}
	
	// search by author name
	public Post getpostByauthorname(String postAuthor) {
		Optional<PostsEntity> postOptional = postRepository.findByAuthor(postAuthor);
		
		if(postOptional.isPresent()) {
		PostsEntity pe = postOptional.get();
		return buildPostObj(pe);
		}else {
			return null;
		}
	}
	
	//search by content
	
	public List<PostsEntity> searchbycontent(String keyword){
		List<PostsEntity> posts =postRepository.findByContentContaining(keyword);
		//return postRepository.findByContentContaining(keyword);
		if(posts.isEmpty()) {
            System.out.println("No posts found containing the keyword: " + keyword);
        }
        return posts;
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
	
	
	 // update post by id
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

	//delete post by id
	public void deletePost(String postId) {
		postRepository.deleteById(postId);

	}


	


	

}
