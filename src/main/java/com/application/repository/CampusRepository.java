package com.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.dto.CampusDto;
import com.application.entity.Campus;
import com.application.entity.Zone;

@Repository
public interface CampusRepository extends JpaRepository<Campus, Integer>{
	
	List<Campus> findByZoneZoneId(int zoneId);
	List<Campus> findByCityCityId(int cityId);
	
	@Query(value = """
	        SELECT c.* 
	        FROM sce_campus.sce_cmps c 
	        JOIN sce_locations.sce_campaign cam 
	          ON c.cmps_id = cam.cmps_id 
	        WHERE cam.campaign_id = :campaignId
	        """, nativeQuery = true)
	    List<Campus> findCampusByCampaignId(@Param("campaignId") int campaignId);
	
	 @Query("SELECT c.state.stateId FROM Campus c WHERE c.campusId = :campusId")
	    Integer findStateIdByCampusId(@Param("campusId") int campusId);
	 
	 @Query("SELECT new com.application.dto.CampusDto(c.campusId, c.campusName) FROM Campus c WHERE c.zone = :zone")
	    List<CampusDto> findByZoneAsDto(@Param("zone") Zone zone);
}
