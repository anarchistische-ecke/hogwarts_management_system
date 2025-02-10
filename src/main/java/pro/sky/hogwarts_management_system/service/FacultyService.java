package pro.sky.hogwarts_management_system.service;

import pro.sky.hogwarts_management_system.model.Faculty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.hogwarts_management_system.repository.FacultyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

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

    public void delete(Long id) {
        facultyRepository.deleteById(id);
    }

    public List<Faculty> searchByColor(String color) {
        return facultyRepository.findAllByColor(color);
    }
}
