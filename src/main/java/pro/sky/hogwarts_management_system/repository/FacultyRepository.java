package pro.sky.hogwarts_management_system.repository;

import pro.sky.hogwarts_management_system.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    List<Faculty> findAllByColorIgnoreCase(String color);

    List<Faculty> findAllByNameIgnoreCase(String name);
}