package pro.sky.hogwarts_management_system.test;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import pro.sky.hogwarts_management_system.model.Faculty;
import pro.sky.hogwarts_management_system.model.Student;
import pro.sky.hogwarts_management_system.repository.FacultyRepository;
import pro.sky.hogwarts_management_system.repository.StudentRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StudentControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {baseUrl = "http://localhost:" + port + "/student";}

    @AfterEach
    void cleanup() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    private Faculty createFaculty(String name, String color) {
        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setColor(color);
        ResponseEntity<Faculty> response = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        return response.getBody();
    }

    private Student createStudent(String name, int age, Faculty faculty) {
        Student student = new Student();
        student.setName(name);
        student.setAge(age);
        student.setFaculty(faculty);
        ResponseEntity<Student> response = restTemplate.postForEntity(baseUrl, student, Student.class);
        return response.getBody();
    }

    @Test
    void getStudents_returnsAllStudents() {
        Faculty faculty = createFaculty("Gryffindor", "Red");

        createStudent("Harry Potter", 17, faculty);
        createStudent("Hermione Granger", 18, faculty);

        ResponseEntity<Student[]> response = restTemplate.getForEntity(baseUrl, Student[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length >= 2);
    }

    @Test
    void createStudent_returnsSavedStudent() {
        Faculty faculty = createFaculty("Slytherin", "Green");
        Student student = new Student();
        student.setName("Draco Malfoy");
        student.setAge(16);
        student.setFaculty(faculty);

        ResponseEntity<Student> response = restTemplate.postForEntity(baseUrl, student, Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals("Draco Malfoy", response.getBody().getName());
    }

    @Test
    void updateStudent_returnsUpdatedStudent() {
        Faculty faculty = createFaculty("Hufflepuff", "Yellow");
        Student student = createStudent("Cedric Diggory", 17, faculty);
        student.setAge(18);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Student> request = new HttpEntity<>(student, headers);

        ResponseEntity<Student> response = restTemplate.exchange(baseUrl, HttpMethod.PATCH, request, Student.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(18, response.getBody().getAge());
    }

    @Test
    void deleteStudent_removesStudentFromDatabase() {
        Faculty faculty = createFaculty("Ravenclaw", "Blue");
        Student student = createStudent("Luna Lovegood", 17, faculty);
        Long studentId = student.getId();

        restTemplate.delete(baseUrl + "/" + studentId);

        ResponseEntity<Student[]> response = restTemplate.getForEntity(baseUrl, Student[].class);
        List<Student> students = Arrays.asList(response.getBody());
        assertFalse(students.stream().anyMatch(f -> f.getId().equals(studentId)), "Student should not exist in the list after deletion");
    }

    @Test
    void searchByAge_returnsStudentsWithExactAge() {
        Student student = new Student();
        student.setName("Horny Potter");
        student.setAge(69);
        studentRepository.save(student);
        Student studentessa = new Student();
        studentessa.setName("Ginny Weasley");
        studentessa.setAge(420);
        studentRepository.save(studentessa);

        ResponseEntity<Student[]> response = restTemplate.getForEntity(baseUrl + "/age?age=69", Student[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().length);
        assertEquals("Horny Potter", response.getBody()[0].getName());
    }

    @Test
    @Transactional
    void searchByAgeBetween_returnsStudentsInRange() {
        Faculty faculty = createFaculty("Slytherin", "Green");
        createStudent("Draco Malfoy", 500, faculty);
        createStudent("Pansy Parkinson", 600, faculty);
        createStudent("Blaise Zabini", 550, faculty);

        ResponseEntity<Student[]> response = restTemplate.getForEntity(baseUrl + "/age_between?age=499&age2=599", Student[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void getFacultyOfStudent_returnsFaculty() {
        Faculty faculty = createFaculty("Gryffindor", "Red");
        Student student = createStudent("Harry Potter", 17, faculty);
        Long studentId = student.getId();
        student.setFaculty(faculty);
        studentRepository.save(student);
        facultyRepository.save(faculty);

        ResponseEntity<Student> response = restTemplate.getForEntity(baseUrl + "/" + studentId + "/faculty", Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Gryffindor", response.getBody().getName());
    }
}

