package com.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.EmployeeCoupon;

@Repository
public interface EmployeeCouponRepository extends JpaRepository<EmployeeCoupon, Integer>{

}
