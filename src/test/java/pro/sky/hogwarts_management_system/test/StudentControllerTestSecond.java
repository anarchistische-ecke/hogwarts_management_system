package pro.sky.hogwarts_management_system.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.hogwarts_management_system.controller.StudentController;
import pro.sky.hogwarts_management_system.model.Faculty;
import pro.sky.hogwarts_management_system.model.Student;
import pro.sky.hogwarts_management_system.service.StudentService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTestSecond {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Student createStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setAge(17);
        student.setName("Harry Potter");
        return student;
    }

    private final Student student = createStudent();

    @Test
    void getAllStudentsTest() throws Exception {
        when(studentService.findAll()).thenReturn(Collections.singletonList(student));

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Harry Potter"));
    }

    @Test
    void saveStudentTest() throws Exception {
        when(studentService.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Harry Potter"));
    }

    @Test
    void updateStudentTest() throws Exception {
        when(studentService.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(patch("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(17));
    }

    @Test
    void deleteStudentTest() throws Exception {
        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isOk());
    }

    @Test
    void searchByAgeTest() throws Exception {
        when(studentService.searchByAge(17)).thenReturn(Collections.singletonList(student));

        mockMvc.perform(get("/student/age?age=17"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].age").value(17));
    }

    @Test
    void searchByAgeBetweenTest() throws Exception {
        when(studentService.searchByAgeBetween(16, 18)).thenReturn(Collections.singletonList(student));

        mockMvc.perform(get("/student/age_between?age=16&age2=18"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].age").value(17));
    }

    @Test
    void getFacultyOfStudentTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");
        when(studentService.getFacultyOfStudent(1L)).thenReturn(faculty);

        mockMvc.perform(get("/student/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor"));
    }

    @Test
    void getFacultyOfStudentNotFoundTest() throws Exception {
        when(studentService.getFacultyOfStudent(anyLong())).thenReturn(null);

        mockMvc.perform(get("/student/99/faculty"))
                .andExpect(status().isNotFound());
    }
}