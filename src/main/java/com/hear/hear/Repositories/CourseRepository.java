package com.hear.hear.Repositories;

import com.hear.hear.entities.Course;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Integer> {

    boolean existsByName(@NotBlank(message = "name is required") String name);

    boolean existsByCode(@NotBlank(message = "code is required") String code);
}

