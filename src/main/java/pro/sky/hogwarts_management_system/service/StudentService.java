package pro.sky.hogwarts_management_system.service;

import jakarta.transaction.Transactional;
import pro.sky.hogwarts_management_system.model.Faculty;
import pro.sky.hogwarts_management_system.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.hogwarts_management_system.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Optional<Student> fingById(Long id) {
        return studentRepository.findById(id);
    }
    @Transactional
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> searchByAge(Integer age) {
        return studentRepository.findAllByAge(age);
    }

    public List<Student> searchByAgeBetween(Integer from, Integer to) {
        return studentRepository.findAllByAgeBetween(from, to);
    }

    public Faculty getFacultyOfStudent(Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        return (student != null) ? student.getFaculty() : null;
    }

}