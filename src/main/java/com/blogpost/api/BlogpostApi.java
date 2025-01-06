package com.blogpost.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.blogpost.entity.PostsEntity;
import com.blogpost.model.Post;
import com.blogpost.model.PostInput;
import com.blogpost.service.BlogpostService;

@RestController
public class BlogpostApi {

	@Autowired
	private BlogpostService blogpostService;

	@PostMapping(consumes = "application/json", produces = "application/json", path = "/posts")
	public ResponseEntity<Post> createBlog(@Valid @RequestBody PostInput request) {
		Post post = blogpostService.savePost(request);
		return new ResponseEntity<Post>(post, HttpStatus.CREATED);
	}

	@GetMapping(produces = "application/json", path = "/posts/{postId}")
	public ResponseEntity<Post> getBlog(@PathVariable(name = "postId") String postId) {
		Post post = blogpostService.getPost(postId).orElseThrow(() -> new RuntimeException("Post not found"));
		return new ResponseEntity<Post>(post, HttpStatus.OK);
	}
	
	@PutMapping(produces = "application/json", consumes = "application/json", path = "/posts/{postId}")
	public ResponseEntity<Post> updateBlog(@PathVariable(name = "postId") String postId, @Valid @RequestBody PostInput request) {

		Post post = blogpostService.updatePost(postId, request);
		return new ResponseEntity<Post>(post, HttpStatus.OK);
	}

	@DeleteMapping( path = "/posts/{postId}")
	public ResponseEntity<?> deleteBlog(@PathVariable(name = "postId") String postId) {
		blogpostService.deletePost(postId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	
	@GetMapping(produces="application/json", path="/posts")
	public List<PostsEntity> getAllPosts(){
		return blogpostService.getposts();
	}
	
	 
}
