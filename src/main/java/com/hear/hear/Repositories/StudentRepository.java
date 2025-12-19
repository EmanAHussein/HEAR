package com.hear.hear.Repositories;

import com.hear.hear.entities.Class;
import com.hear.hear.entities.Course;
import com.hear.hear.entities.Student;
import com.hear.hear.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    void deleteByUser(User user);

    @Query("SELECT c FROM Class c JOIN c.students s WHERE s.user.id = :userId")
    Set<Class> findClassesForStudent(@Param("userId") Integer userId);

    @Query("SELECT c FROM Class c JOIN c.students s WHERE s.user.id = :userId AND c.day = :day")
    Set<Class> findClassesForStudentByDay(@Param("userId") Integer userId, @Param("day") DayOfWeek day);

    @Query("SELECT c FROM Course c JOIN c.students s where s.id = :studentId")
    Set<Course> findCoursesForStudentByStudentId(Integer studentId);

    @Query("SELECT s FROM Student s  WHERE s.user.id = :userId")
    Optional<Student> findStudentByUserId(@Param("userId")Integer userId);


    Optional<Student> findByUser(User user);
}
