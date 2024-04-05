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

	// posts data
	@PostMapping(consumes = "application/json", produces = "application/json", path = "/posts")
	public ResponseEntity<Post> createBlog(@Valid @RequestBody PostInput request) {
		Post post = blogpostService.savePost(request);
		return new ResponseEntity<Post>(post, HttpStatus.CREATED);
	}

	// get podts by id
	@GetMapping(produces = "application/json", path = "/posts/{postId}")
	public ResponseEntity<Post> getBlog(@PathVariable(name = "postId") String postId) {
		Post post = blogpostService.getPost(postId);
		return new ResponseEntity<Post>(post, HttpStatus.OK);
	}
	
	// search by using author
	@GetMapping(produces="application/json", path="/posts/get/{postAuthor}")
	public ResponseEntity<Post> getbyAuthor(@PathVariable(name="postAuthor") String postAuthor){
		Post post = blogpostService.getpostByauthorname(postAuthor);
		return new ResponseEntity<Post>(post, HttpStatus.OK);
	}
	
	//search by content
	@GetMapping(produces="application/json", path="/posts/getbycontent/{postContent}")
	public ResponseEntity<Post> getbyContent(@PathVariable(name="postContent") String postContent){
		Post post = blogpostService.getpostByContent(postContent);
		return new ResponseEntity<Post>(post, HttpStatus.OK);
	}
	
	@PutMapping(produces = "application/json", consumes = "application/json", path = "/posts/{postId}")
	public ResponseEntity<Post> updateBlog(@PathVariable(name = "postId") String postId, @Valid @RequestBody PostInput request) {

		Post post = blogpostService.updatePost(postId, request);
		return new ResponseEntity<Post>(post, HttpStatus.OK);
	}

	// Delete post by id
	@DeleteMapping( path = "/posts/{postId}")
	public ResponseEntity<?> deleteBlog(@PathVariable(name = "postId") String postId) {
		blogpostService.deletePost(postId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// get All posts from the table
	@GetMapping(produces="application/json", path="/posts")
	public List<PostsEntity> getAllPosts(){
		return blogpostService.getposts();
	}
	
	 
}
