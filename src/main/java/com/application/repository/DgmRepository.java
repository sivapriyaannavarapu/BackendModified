package com.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.entity.Dgm;

@Repository
public interface DgmRepository extends JpaRepository<Dgm, Integer> {
  
	@Query("SELECT d FROM Dgm d WHERE d.dgm_id = :dgmId")
    List<Dgm> findByDgmId(@Param("dgmId") int dgmId);
	
	 @Query("SELECT d FROM Dgm d WHERE d.zone.zoneId = :zoneId")
	    List<Dgm> findByZoneId(@Param("zoneId") int zoneId);
	 
	    @Query("SELECT d FROM Dgm d WHERE d.campus.campusId = :campusId")
	    List<Dgm> findByCampusId(@Param("campusId") int campusId);
}