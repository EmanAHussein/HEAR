package com.hear.hear.Repositories;
import com.hear.hear.entities.Class;
import com.hear.hear.entities.ClassType;
import com.hear.hear.entities.Course;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.time.LocalTime;

public interface ClassRepository extends JpaRepository<Class,Integer> {

    boolean existsByRoom(String room);

    boolean existsByDay(DayOfWeek day);

    boolean existsByStartTimeAndEndTime(@NotBlank(message = "start time is required") LocalTime startTime, @NotBlank(message = "end time is required") LocalTime endTime);

    boolean existsByCourseAndType(Course course, ClassType type);
}
