package com.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.CmpsOrientationBatchFeeView;

@Repository
public interface CmpsOrientationBatchFeeViewRepository extends JpaRepository<CmpsOrientationBatchFeeView, Integer>{

}
