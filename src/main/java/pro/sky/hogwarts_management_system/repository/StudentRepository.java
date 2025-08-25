package pro.sky.hogwarts_management_system.repository;

import org.springframework.data.jpa.repository.Query;
import pro.sky.hogwarts_management_system.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.hibernate.grammars.hql.HqlParser.SELECT;
import static org.hibernate.sql.ast.Clause.FROM;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllByAge(Integer age);

    List<Student> findAllByAgeBetween(Integer from, Integer to);

    List<Student> findByFacultyId(Long facultyId);

    @Query(value = "SELECT * FROM student", nativeQuery = true)
    List <Student> findAll();

    @Query (value = "SELECT AVG(age) FROM student", nativeQuery = true)
    List <Student> findAvgAge();

    @Query (value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List <Student> findLatest();
}
