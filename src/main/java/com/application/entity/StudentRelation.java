package com.application.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="sce_stud_relation" , schema = "sce_student")
public class StudentRelation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int student_relation_id;
	private String student_relation_type;
	private int is_active;
}
