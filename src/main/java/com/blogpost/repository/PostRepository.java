package com.blogpost.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogpost.entity.PostsEntity;

public interface PostRepository extends JpaRepository<PostsEntity, String> {

//	Optional<PostsEntity> findByAuthor(String postAuthor);
//	public Userdetails findByfirstName(String firstName);
	
	public Optional<PostsEntity> findByAuthor(String author);

	public Optional<PostsEntity> findByContent(String postContent);
	
	 List<PostsEntity> findByContentContaining(String keyword);
}
