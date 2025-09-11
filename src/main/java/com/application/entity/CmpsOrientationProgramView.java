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
@Table(name = "sce_cmps_orientation_program" , schema = "sce_course")
public class CmpsOrientationProgramView {

		@Id
		private int cmps_id;
		private String cmps_name;
		private int orientation_id;
		private String orientation_name;
		private int program_id;
		private String program_name;
		private int exam_program_id;
		private String exam_program_name;
		

}
