package com.example.backend.repository;

import com.example.backend.model.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {

    // Wyszukiwanie adresu po nazwie, kraju i regionie.
    Optional<Address> findByNameAndCountryAndRegion(String name, String country, String region);

 
    // Wyszukiwanie adresu po nazwie i kraju (Nie wiem czy wszystkie spoty mogą mieć region)
    Optional<Address> findByNameAndCountry(String name, String country);
}

