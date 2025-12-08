package com.example.backend.repository;

import com.example.backend.model.Spot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SpotRepository extends CrudRepository<Spot, Long> {
    // Wyszukiwanie po tytule
    List<Spot> findByTitleContainingIgnoreCase(String title);

    // Wyszukiwanie spotów na aktualnie oglądanym fragmencie mapy
    List<Spot> findByLatitudeBetweenAndLongitudeBetween(
            BigDecimal minLatitude, 
            BigDecimal maxLatitude,
            BigDecimal minLongitude, 
            BigDecimal maxLongitude
    );

    // Wyszukiwanie spotów w obszarze mapy z filtrowaniem po tytule
    @Query(value = "SELECT s.* FROM spots s " +
           "WHERE s.latitude BETWEEN :minLat AND :maxLat " +
           "AND s.longitude BETWEEN :minLng AND :maxLng " +
           "AND LOWER(s.title) LIKE '%' || LOWER(:title) || '%'",
           nativeQuery = true)
    List<Spot> findByLatitudeBetweenAndLongitudeBetweenAndTitleContainingIgnoreCase(
            @Param("minLat") BigDecimal minLatitude,
            @Param("maxLat") BigDecimal maxLatitude,
            @Param("minLng") BigDecimal minLongitude,
            @Param("maxLng") BigDecimal maxLongitude,
            @Param("title") String title
    );

    // Wyszukiwanie spotów w obszarze mapy z podaniem tagu
    @Query(value = "SELECT DISTINCT s.id, s.title, s.description, s.latitude, s.longitude, " +
           "s.created_at, s.category_id, s.author_id, s.address_id " +
           "FROM spots s " +
           "INNER JOIN spot_tags st ON s.id = st.spot_id " +
           "INNER JOIN tags t ON st.tag_id = t.id " +
           "WHERE s.latitude BETWEEN :minLat AND :maxLat " +
           "AND s.longitude BETWEEN :minLng AND :maxLng " +
           "AND LOWER(t.name) = LOWER(:tagName)",
           nativeQuery = true)
    List<Spot> findSpotsInAreaByTag(
            @Param("minLat") BigDecimal minLatitude,
            @Param("maxLat") BigDecimal maxLatitude,
            @Param("minLng") BigDecimal minLongitude,
            @Param("maxLng") BigDecimal maxLongitude,
            @Param("tagName") String tagName
    );

    // Wyszukiwanie spotów w obszarze mapy z filtrowaniem po tagu i tytule
    @Query(value = "SELECT DISTINCT s.id, s.title, s.description, s.latitude, s.longitude, " +
           "s.created_at, s.category_id, s.author_id, s.address_id " +
           "FROM spots s " +
           "INNER JOIN spot_tags st ON s.id = st.spot_id " +
           "INNER JOIN tags t ON st.tag_id = t.id " +
           "WHERE s.latitude BETWEEN :minLat AND :maxLat " +
           "AND s.longitude BETWEEN :minLng AND :maxLng " +
           "AND LOWER(t.name) = LOWER(:tagName) " +
           "AND LOWER(s.title) LIKE '%' || LOWER(:title) || '%'",
           nativeQuery = true)
    List<Spot> findSpotsInAreaByTagAndTitle(
            @Param("minLat") BigDecimal minLatitude,
            @Param("maxLat") BigDecimal maxLatitude,
            @Param("minLng") BigDecimal minLongitude,
            @Param("maxLng") BigDecimal maxLongitude,
            @Param("tagName") String tagName,
            @Param("title") String title
    );
    
    // Wyszukiwanie wszystkich spotów z danym tagiem
    @Query(value = "SELECT DISTINCT s.id, s.title, s.description, s.latitude, s.longitude, " +
           "s.created_at, s.category_id, s.author_id, s.address_id " +
           "FROM spots s " +
           "INNER JOIN spot_tags st ON s.id = st.spot_id " +
           "INNER JOIN tags t ON st.tag_id = t.id " +
           "WHERE LOWER(t.name) = LOWER(:tagName)",
           nativeQuery = true)
    List<Spot> findByTagName(@Param("tagName") String tagName);

    // Wyszukiwanie spotów po tagu z filtrowaniem po tytule wielkość liter bez znaczenia
    @Query(value = "SELECT DISTINCT s.id, s.title, s.description, s.latitude, s.longitude, " +
           "s.created_at, s.category_id, s.author_id, s.address_id " +
           "FROM spots s " +
           "INNER JOIN spot_tags st ON s.id = st.spot_id " +
           "INNER JOIN tags t ON st.tag_id = t.id " +
           "WHERE LOWER(t.name) = LOWER(:tagName) " +
           "AND LOWER(s.title) LIKE '%' || LOWER(:title) || '%'",
           nativeQuery = true)
    List<Spot> findByTagNameAndTitleContainingIgnoreCase(
            @Param("tagName") String tagName,
            @Param("title") String title
    );
}


