package com.application.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sce_cmps_orientation_batch_fee", schema = "sce_course")
public class CmpsOrientationBatchFeeView {

    @Id
    @Column(name = "section_id")
    private int sectionId;
    @Column(name = "section_name")
    private String sectionName;
    @Column(name = "cmps_id")
    private int cmpsId;

    @Column(name = "cmps_name")
    private String cmpsName;

    @Column(name = "orientation_id")
    private int orientationId;

    @Column(name = "orientation_name")
    private String orientationName;

    @Column(name = "orientation_batch_id")
    private int orientationBatchId;

    @Column(name = "orientation_batch_name")
    private String orientationBatchName;

    @Column(name = "orientation_start_date")
    private Date orientationStartDate;

    @Column(name = "orientation_end_date")
    private Date orientationEndDate;

    @Column(name = "orientation_fee")
    private float orientationFee;
    
    @Column(name = "class_id")
    private int classId;
    
    @Column(name = "class_name")
    private String className;
}
