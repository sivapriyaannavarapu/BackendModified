package com.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, Integer>{
	  // Add this line
	List<City> findByDistrictStateStateId(int stateId);
    List<City> findByDistrictDistrictId(int districtId);
}