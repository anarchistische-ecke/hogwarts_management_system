package pro.sky.hogwarts_management_system.controller;

import org.springframework.http.ResponseEntity;
import pro.sky.hogwarts_management_system.model.Faculty;
import pro.sky.hogwarts_management_system.model.Student;
import org.springframework.web.bind.annotation.*;
import pro.sky.hogwarts_management_system.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    private StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public Collection<Student> getStudents() {
        return studentService.findAll();
    }

    @PostMapping
    public Student save(@RequestBody Student student) {
        return studentService.save(student);
    }

    @PatchMapping
    public Student update(Student student) {
        return studentService.save(student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }

    @GetMapping("/age")
    public List<Student> searchByAge(@RequestParam Integer age) {
        return studentService.searchByAge(age);
    }

    @GetMapping("/age_between")
    public List<Student> searchByAgeBetween(@RequestParam Integer age, @RequestParam Integer age2) {
        return studentService.searchByAgeBetween(age, age2);
    }

    @GetMapping("/{studentId}/faculty")
    public ResponseEntity<Faculty> getFacultyOfStudent(@PathVariable Long studentId) {
        Faculty faculty = studentService.getFacultyOfStudent(studentId);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }
}
