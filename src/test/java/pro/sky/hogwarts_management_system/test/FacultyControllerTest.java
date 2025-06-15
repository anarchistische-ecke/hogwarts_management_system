package pro.sky.hogwarts_management_system.test;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import pro.sky.hogwarts_management_system.controller.FacultyController;
import pro.sky.hogwarts_management_system.model.Faculty;
import pro.sky.hogwarts_management_system.model.Student;
import pro.sky.hogwarts_management_system.repository.FacultyRepository;
import pro.sky.hogwarts_management_system.repository.StudentRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class FacultyControllerTest {
    @Nested
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    public class FacultyControllerIntegrationTest {

        @LocalServerPort
        private int port;

        @Autowired
        private FacultyController facultyController;

        @Autowired
        private TestRestTemplate restTemplate;

        @Autowired
        private FacultyRepository facultyRepository;
        @Autowired
        private StudentRepository studentRepository;

        private String baseUrl;

        @BeforeEach
        void setUp() {
            baseUrl = "http://localhost:" + port + "/faculty";
        }

        @Test
        public void testGetFaculties() throws Exception {
            Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/user", String.class)).isNotNull();
        }

        @Test
        public void testGetFacultiesByColor() throws Exception {
            Faculty faculty = new Faculty();
            faculty.setName("Hufflepuff");
            faculty.setColor("Blue");
            restTemplate.postForEntity(baseUrl, faculty, Faculty.class);

            ResponseEntity<Faculty[]> response = restTemplate.getForEntity(baseUrl + "/search/color?color=Blue", Faculty[].class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Hufflepuff", response.getBody()[0].getName());
        }

        @Test
        public void testGetFacultiesByName() throws Exception {
            Faculty faculty = new Faculty();
            faculty.setName("Hufflepuff");
            faculty.setColor("Blue");
            restTemplate.postForEntity(baseUrl, faculty, Faculty.class);

            ResponseEntity<Faculty[]> response = restTemplate.getForEntity(baseUrl + "/search/name?name=Hufflepuff", Faculty[].class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Hufflepuff", response.getBody()[0].getName());
        }

        @Test
        public void getStudentsInFacultyTest() {
            Faculty faculty = new Faculty();
            faculty.setName("Hufflepuff");
            faculty.setColor("Black");
            faculty = facultyRepository.save(faculty);
            Long facultyId = faculty.getId();

            Student student = new Student();
            student.setName("Helga Hufflepuff");
            student.setAge(16);
            student.setFaculty(faculty);
            studentRepository.save(student);

            ResponseEntity<Student[]> response = restTemplate.getForEntity(baseUrl + "/" + facultyId + "/students", Student[].class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            Student[] students = response.getBody();
            assertNotNull(students);
            assertEquals(1, students.length); // Assuming 1 student was saved
            assertEquals("Helga Hufflepuff", students[0].getName());
        }

        @Test
        void saveTest() {
            Faculty faculty = new Faculty();
            faculty.setName("Hufflepuff");
            faculty.setColor("Colorfull");

            ResponseEntity<Faculty> response = restTemplate.postForEntity(baseUrl, faculty, Faculty.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Hufflepuff", response.getBody().getName());
            assertEquals("Colorfull", response.getBody().getColor());
        }

        @Test
        void updateFacultyTest() {
            Faculty faculty = new Faculty();
            faculty.setName("Hufflepuff");
            faculty.setColor("Yellow");
            ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(baseUrl, faculty, Faculty.class);
            Faculty createdFaculty = createResponse.getBody();
            createdFaculty.setColor("Blue");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Faculty> request = new HttpEntity<>(createdFaculty, headers);
            ResponseEntity<Faculty> response = restTemplate.exchange(baseUrl, HttpMethod.PATCH, request, Faculty.class);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("Hufflepuff", response.getBody().getName());
            assertEquals("Blue", response.getBody().getColor());
        }

        @Test
        void deleteFacultyTest() {
            Faculty faculty = new Faculty();
            faculty.setName("Hufflepuff");
            faculty.setColor("Abobus");
            ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(baseUrl, faculty, Faculty.class);
            Faculty createdFaculty = createResponse.getBody();
            Long facultyId = createdFaculty.getId();

            restTemplate.delete(baseUrl + "/" + facultyId);

            ResponseEntity<Faculty[]> listResponse = restTemplate.getForEntity(baseUrl, Faculty[].class);
            List<Faculty> faculties = Arrays.asList(listResponse.getBody());
            assertFalse(faculties.stream().anyMatch(f -> f.getId().equals(facultyId)));
        }
    }
}

