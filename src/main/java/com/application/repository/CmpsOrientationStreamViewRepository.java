package com.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.CmpsOrientationStreamView;

@Repository
public interface CmpsOrientationStreamViewRepository extends JpaRepository<CmpsOrientationStreamView, Integer>{

}
