package com.example.backend.repository;

import com.example.backend.model.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {

    // Wyszukiwanie taga po nazwie
    Optional<Tag> findByName(String name);

    // Sprawdzanie czy tag o danej nazwie istnieje
    boolean existsByName(String name);

    @Query("SELECT t.name FROM Tag t")
    List<String> findAllTagNames();
}

