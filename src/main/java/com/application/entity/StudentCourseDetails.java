package com.application.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sce_stud_course_details" , schema = "sce_student")
public class StudentCourseDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int stud_course_id;
	private Date course_date;
	
	@ManyToOne
	@JoinColumn(name = "stud_adms_id")
	private StudentAcademicDetails studentAcademicDetails;
	
	@ManyToOne
	@JoinColumn(name = "class_id")
	private StudentClass studentClass;
	
	@ManyToOne
	@JoinColumn(name = "section_id")
	private Section section;
	
	@ManyToOne
	@JoinColumn(name = "course_track_id")
	private CourseTrack courseTrack;
	
	@ManyToOne
	@JoinColumn(name = "course_batch_id")
	private CourseBatch courseBatch;
	
	@ManyToOne
	@JoinColumn(name = "stream_id")
	private Stream stream;
	
	@ManyToOne
	@JoinColumn(name = "program_id")
	private ProgramName programName;
	
	@ManyToOne
	@JoinColumn(name = "exam_program_id")
	private ExamProgram examProgram;
	
	@ManyToOne
	@JoinColumn(name = "cmps_course_track_id")
	private CmpsCourseTrack cmpsCourseTrack;
	
	
}
