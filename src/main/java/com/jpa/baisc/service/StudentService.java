package com.jpa.baisc.service;

import com.jpa.baisc.domain.Course;
import com.jpa.baisc.domain.Student;
import com.jpa.baisc.dto.student.StudentCreateRequestDto;
import com.jpa.baisc.dto.student.StudentCreateResponseDto;
import com.jpa.baisc.dto.student.StudentListResponseDto;
import com.jpa.baisc.repository.CourseRepository;
import com.jpa.baisc.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    /**
     * Student 생성 서비스
     * @param studentCreateRequestDto
     */
    @Transactional
    public StudentCreateResponseDto createStudent(StudentCreateRequestDto studentCreateRequestDto) {
        Long foundCourseId = studentCreateRequestDto.getCourseId();
        Course foundCourse = courseRepository.findById(foundCourseId)
                .orElseThrow(() -> new NoSuchElementException("course not found"));
        Student newStudent = Student.createFrom(studentCreateRequestDto.getStudentName(), foundCourse);
        Student savedStudent = studentRepository.save(newStudent);
        return StudentCreateResponseDto.createFrom(savedStudent);
    }

    /**
     * Student 목록 조회 서비스
     */
    public StudentListResponseDto getStudentList() {
        List<Student> foundStudentList = studentRepository.findAll();
        List<StudentListResponseDto.StudentDto> studentDtoList = foundStudentList.stream()
                .map(student -> StudentListResponseDto.StudentDto.createFrom(student, student.getCourse()))
                .toList();
        return StudentListResponseDto.createFrom(studentDtoList);
    }
}
