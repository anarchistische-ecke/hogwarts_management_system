package pro.sky.hogwarts_management_system.service;

import jakarta.transaction.Transactional;
import pro.sky.hogwarts_management_system.model.Faculty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.hogwarts_management_system.model.Student;
import pro.sky.hogwarts_management_system.repository.FacultyRepository;
import pro.sky.hogwarts_management_system.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    @Autowired
    private StudentRepository studentRepository;

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty save(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public List<Faculty> findAll() {
        return facultyRepository.findAll();
    }

    public Optional<Faculty> fingById(Long id) {
        return facultyRepository.findById(id);
    }

    @Transactional
    public void delete(Long id) {
        facultyRepository.deleteById(id);
    }

    public List<Faculty> searchByColor(String color) {
        return facultyRepository.findAllByColorIgnoreCase(color);
    }

    public List<Faculty> searchByName(String name) {
        return facultyRepository.findAllByNameIgnoreCase(name);
    }

    public List<Student> getStudentsInFaculty(Long facultyId) {
        return studentRepository.findByFacultyId(facultyId);
    }
}
