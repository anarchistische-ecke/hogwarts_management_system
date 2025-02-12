package pro.sky.hogwarts_management_system.repository;

import pro.sky.hogwarts_management_system.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllByAge(Integer age);

    List<Student> findAllByAgeBetween(Integer from, Integer to);

    List<Student> findByFacultyId(Long facultyId);
}
