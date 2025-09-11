package com.application.entity;

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
@Table(name = "sce_cmps_orientation_stream" , schema = "sce_course")
public class CmpsOrientationStreamView {
	
	@Id
	private int cmps_id;
	private String cmps_name;
	private int orienatation_id;
	private String orientation_name;
	private int stream_id;
	private String stream_name;
	
}
