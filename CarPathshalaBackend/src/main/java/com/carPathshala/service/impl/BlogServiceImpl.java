package com.carPathshala.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.carPathshala.dto.blog.BlogRequest;
import com.carPathshala.dto.blog.BlogResponse;
import com.carPathshala.exceptions.ResourceNotFound;
import com.carPathshala.mapper.BlogMapper;
import com.carPathshala.model.Blog;
import com.carPathshala.repository.BlogRepository;
import com.carPathshala.service.BlogService;
@Service
public class BlogServiceImpl implements BlogService {

	private final BlogRepository blogRepository;

    public BlogServiceImpl(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }
	
	@Override
	public BlogResponse create(BlogRequest request) {
		// TODO Auto-generated method stub
		Blog saved = blogRepository.save(BlogMapper.toEntity(request));
        return BlogMapper.toResponse(saved);

	}

	@Override
	public BlogResponse update(Long id, BlogRequest request) {
		// TODO Auto-generated method stub
		Blog existing = blogRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFound("Blog not found with id: " + id));
	    BlogMapper.updateEntity(existing, request);
	    Blog updated = blogRepository.save(existing);

	    return BlogMapper.toResponse(updated);	
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		if (!blogRepository.existsById(id)) {
            throw new ResourceNotFound("Blog not found with id: " + id);
        }
        blogRepository.deleteById(id);
	}

	@Override
	public BlogResponse getById(Long id) {
		// TODO Auto-generated method stub
		Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Blog not found with id: " + id));
        return BlogMapper.toResponse(blog);
	}

	@Override
	public Page<BlogResponse> list(Pageable pageable) {
		// TODO Auto-generated method stub
		return blogRepository.findAll(pageable).map(BlogMapper::toResponse);
	}

	@Override
	public Page<BlogResponse> searchByTitle(String keyword, Pageable pageable) {
		// TODO Auto-generated method stub
		return blogRepository.findByTitleContainingIgnoreCase(keyword, pageable).map(BlogMapper::toResponse);
	}

	@Override
	public Page<BlogResponse> listByAuthor(String author, Pageable pageable) {
		// TODO Auto-generated method stub
		return blogRepository.findByAuthorIgnoreCase(author, pageable).map(BlogMapper::toResponse);
	}

	
	
}
