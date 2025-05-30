package pro.sky.hogwarts_management_system.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.hogwarts_management_system.controller.FacultyController;
import pro.sky.hogwarts_management_system.model.Faculty;
import pro.sky.hogwarts_management_system.model.Student;
import pro.sky.hogwarts_management_system.service.FacultyService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTestSecond {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    private Faculty createFaculty() {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");
        faculty.setStudents(Collections.emptyList());
        return faculty;
    }

    private final Faculty faculty = createFaculty();

    @Test
    void getAllFacultiesTest() throws Exception {
        when(facultyService.findAll()).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].color").value("Red"));
    }

    @Test
    void saveFacultyTest() throws Exception {
        when(facultyService.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor"));
    }

    @Test
    void updateFacultyTest() throws Exception {
        when(facultyService.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(patch("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.color").value("Red"));
    }

    @Test
    void deleteFacultyTest() throws Exception {
        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isOk());
    }

    @Test
    void searchFacultiesByColorTest() throws Exception {
        when(facultyService.searchByColor("Red")).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculty/search/color?color=Red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Gryffindor"));
    }

    @Test
    void searchFacultiesByNameTest() throws Exception {
        when(facultyService.searchByName("Gryffindor")).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculty/search/name?name=Gryffindor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].color").value("Red"));
    }

    @Test
    void getStudentsInFacultyTest() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setAge(17);
        student.setFaculty(faculty);
        student.setName("Harry Potter");
        when(facultyService.getStudentsInFaculty(1L)).thenReturn(Collections.singletonList(student));

        mockMvc.perform(get("/faculty/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Harry Potter"));
    }
}
