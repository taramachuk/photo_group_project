package com.example.backend.repository;

import com.example.backend.model.Photo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.backend.model.User;

import java.util.List;

@Repository
public interface PhotoRepository extends CrudRepository<Photo, Long> {
    // Pobranie wszystkich zdjęć dla danego spota
    List<Photo> findBySpotId(Long spotId);

    // Pobranie wszystkich zdjęć dla danego spota posortowanych od najnowszych
    List<Photo> findBySpotIdOrderByCreatedAtDesc(Long spotId);
    List<Photo> findByAuthor(User author);
    List<Photo> findByAuthorOrderByCreatedAtDesc(User author);
}

