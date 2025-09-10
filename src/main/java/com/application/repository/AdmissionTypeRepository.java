package com.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.AdmissionType;

@Repository
public interface AdmissionTypeRepository  extends JpaRepository<AdmissionType, Integer>{

}
