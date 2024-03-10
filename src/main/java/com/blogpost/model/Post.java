package com.blogpost.model;

import java.io.Serializable;
import java.util.Set;

import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
public class Post implements Serializable{

 
	private static final long serialVersionUID = 5328699924149150369L;
	private String id;
	private String title;
	private String content;
	private String author;
	private String publicationDate;
	private Set<String> categories;
	 
}
