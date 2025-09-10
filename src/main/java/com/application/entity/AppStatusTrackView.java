package com.application.entity;

import java.time.LocalDateTime;

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
@Table(name = "sce_app_status_track", schema = "sce_application")
public class AppStatusTrackView {

	@Id
	@Column(name = "app_no")
	private int num;
	private String pro_name;
	private String dgm_name;
	private String zone_name;
	private int cmps_id;
	private String cmps_name;
	private String status;
	private LocalDateTime date;
}
