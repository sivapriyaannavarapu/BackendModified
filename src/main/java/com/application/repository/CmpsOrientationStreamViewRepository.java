package com.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.CmpsOrientationStreamView;

@Repository
public interface CmpsOrientationStreamViewRepository extends JpaRepository<CmpsOrientationStreamView, Integer>{
	
	List<CmpsOrientationStreamView> findByCmpsId(int cmpsId);
}
