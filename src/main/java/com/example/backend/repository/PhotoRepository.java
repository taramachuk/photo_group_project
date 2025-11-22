package com.example.backend.repository;

import com.example.backend.model.Photo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoRepository extends CrudRepository<Photo, Long> {

    Optional<Photo> findById(Long id);
}
