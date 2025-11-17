package com.example.backend.repository;

import com.example.backend.model.SpotTag;
import com.example.backend.model.SpotTagId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotTagRepository extends CrudRepository<SpotTag, SpotTagId> {

    // Wyszukiwanie wszystkich tagow przypisanych do spota
    List<SpotTag> findById_SpotId(Long spotId);

    // Wyszukiwanie wszystkich spotow przypisanych do taga
    List<SpotTag> findById_TagId(Long tagId);
}

