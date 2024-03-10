package com.blogpost.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogpost.entity.PostsEntity;

public interface PostRepository extends JpaRepository<PostsEntity, String> {

}
