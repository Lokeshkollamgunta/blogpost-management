package com.blogpost.model;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Data
@Validated
public class PostInput implements Serializable {

	private static final long serialVersionUID = 1593955452982899835L;
	@NotBlank
	@Size(min = 1, max = 100)
	private String title;
	@NotBlank
	@Size(min =1)
	private String content;
	@NotBlank
	@Size(min =1, max = 50)
	private String author;
	@NotBlank
	private String publicationDate;
	
	private Set<String> categories;

}
