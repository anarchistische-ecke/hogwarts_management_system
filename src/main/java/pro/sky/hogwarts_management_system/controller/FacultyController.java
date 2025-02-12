package pro.sky.hogwarts_management_system.controller;

import org.springframework.http.ResponseEntity;
import pro.sky.hogwarts_management_system.model.Faculty;
import org.springframework.web.bind.annotation.*;
import pro.sky.hogwarts_management_system.model.Student;
import pro.sky.hogwarts_management_system.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    private FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public List<Faculty> getFaculties() {
        return facultyService.findAll();
    }

    @PostMapping
    public Faculty faculty(@RequestBody Faculty faculty) {
        return facultyService.save(faculty);
    }

    @PatchMapping
    public Faculty update(@RequestBody Faculty faculty) {
        return facultyService.save(faculty);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        facultyService.delete(id);
    }

    @GetMapping("/search/color")
    public List<Faculty> searchFacultiesByColor(@RequestParam String color) {
        return facultyService.searchByColor(color);
    }

    @GetMapping("/search/name")
    public List<Faculty> searchFacultiesByName(@RequestParam String name) {
        return facultyService.searchByName(name);
    }

    @GetMapping("/{facultyId}/students")
    public ResponseEntity<List<Student>> getStudentsInFaculty(@PathVariable Long facultyId) {
        List<Student> students = facultyService.getStudentsInFaculty(facultyId);
        return ResponseEntity.ok(students);
    }

}
