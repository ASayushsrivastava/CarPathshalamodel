package com.carPathshala.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carPathshala.model.Blog;
@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
	Page<Blog> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Blog> findByAuthorIgnoreCase(String author, Pageable pageable);
}
