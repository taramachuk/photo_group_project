package com.example.backend.repository;

import com.example.backend.model.Comment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long> {
    List<Comment> findByPhotoIdOrderByCreatedAtDesc(Long photoId);

    List<Comment> findBySpotIdOrderByCreatedAtDesc(Long spotId);
}

