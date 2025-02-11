package pro.sky.hogwarts_management_system.controller;

import pro.sky.hogwarts_management_system.model.Faculty;
import org.springframework.web.bind.annotation.*;
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
    public Faculty update(Faculty faculty) {
        return facultyService.save(faculty);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        facultyService.delete(id);
    }

    @GetMapping("/search")
    public List<Faculty> searchFaculties(@RequestParam String color) {
        return facultyService.searchByColor(color);
    }

}
