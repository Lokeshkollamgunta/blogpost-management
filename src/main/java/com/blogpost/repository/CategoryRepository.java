package com.blogpost.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blogpost.entity.CategoryEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {

}
