package com.blogpost.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping(produces = "application/json", path = "/posts")
	public ResponseEntity<Post> getBlog() {

		return new ResponseEntity<Post>(HttpStatus.OK);
	}
}
