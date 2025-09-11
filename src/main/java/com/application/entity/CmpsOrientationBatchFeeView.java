package com.application.entity;

import java.util.Date;

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
@Table(name = "sce_cmps_orientation_batch_fee" , schema = "sce_course")
public class CmpsOrientationBatchFeeView {

	@Id
	private int cmps_id;
	private String cmps_name;
	private int orientation_id;
	private String orientation_name;
	private int orientation_batch_id;
	private String orientation_batch_name;
	private Date orientation_start_date;
	private Date orientation_end_date;
	private float orientation_fee;
	private String section_name;
	private int section_id;
}
