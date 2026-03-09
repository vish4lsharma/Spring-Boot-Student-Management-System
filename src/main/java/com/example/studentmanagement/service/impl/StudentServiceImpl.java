package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.dto.StudentRequestDTO;
import com.example.studentmanagement.dto.StudentResponseDTO;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.exception.ResourceNotFoundException;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public StudentResponseDTO createStudent(StudentRequestDTO requestDTO) {
        log.info("Creating new student with email: {}", requestDTO.getEmail());

        if (studentRepository.existsByEmail(requestDTO.getEmail())) {
            throw new IllegalArgumentException("Student with email " + requestDTO.getEmail() + " already exists");
        }

        Student student = Student.builder()
                .firstName(requestDTO.getFirstName())
                .lastName(requestDTO.getLastName())
                .email(requestDTO.getEmail())
                .department(requestDTO.getDepartment())
                .enrollmentDate(requestDTO.getEnrollmentDate())
                .build();

        Student savedStudent = studentRepository.save(student);
        log.info("Student created successfully with ID: {}", savedStudent.getId());
        
        return mapToResponseDTO(savedStudent);
    }

    @Override
    public List<StudentResponseDTO> getAllStudents() {
        log.info("Fetching all students");
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponseDTO getStudentById(Long id) {
        log.info("Fetching student with ID: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        return mapToResponseDTO(student);
    }

    @Override
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO requestDTO) {
        log.info("Updating student with ID: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));

        // Check if new email is taken by another student
        if (!student.getEmail().equals(requestDTO.getEmail()) && studentRepository.existsByEmail(requestDTO.getEmail())) {
             throw new IllegalArgumentException("Student with email " + requestDTO.getEmail() + " already exists");
        }

        student.setFirstName(requestDTO.getFirstName());
        student.setLastName(requestDTO.getLastName());
        student.setEmail(requestDTO.getEmail());
        student.setDepartment(requestDTO.getDepartment());
        student.setEnrollmentDate(requestDTO.getEnrollmentDate());

        Student updatedStudent = studentRepository.save(student);
        log.info("Student updated successfully with ID: {}", updatedStudent.getId());

        return mapToResponseDTO(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        log.info("Deleting student with ID: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
        
        studentRepository.delete(student);
        log.info("Student deleted successfully with ID: {}", id);
    }

    private StudentResponseDTO mapToResponseDTO(Student student) {
        return StudentResponseDTO.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .department(student.getDepartment())
                .enrollmentDate(student.getEnrollmentDate())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}
