package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.ApiResponse;
import com.example.studentmanagement.dto.StudentRequestDTO;
import com.example.studentmanagement.dto.StudentResponseDTO;
import com.example.studentmanagement.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Student Controller", description = "APIs for managing students")
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "Create a new student")
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponseDTO>> createStudent(
            @Valid @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO createdStudent = studentService.createStudent(requestDTO);
        ApiResponse<StudentResponseDTO> response = new ApiResponse<>(true, "Student created successfully",
                createdStudent);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all students")
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponseDTO>>> getAllStudents() {
        List<StudentResponseDTO> students = studentService.getAllStudents();
        ApiResponse<List<StudentResponseDTO>> response = new ApiResponse<>(true, "Students retrieved successfully",
                students);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get a student by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> getStudentById(@PathVariable Long id) {
        StudentResponseDTO student = studentService.getStudentById(id);
        ApiResponse<StudentResponseDTO> response = new ApiResponse<>(true, "Student retrieved successfully", student);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update an existing student by ID")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponseDTO>> updateStudent(@PathVariable Long id,
            @Valid @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO updatedStudent = studentService.updateStudent(id, requestDTO);
        ApiResponse<StudentResponseDTO> response = new ApiResponse<>(true, "Student updated successfully",
                updatedStudent);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete an existing student by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        ApiResponse<Void> response = new ApiResponse<>(true, "Student deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}
