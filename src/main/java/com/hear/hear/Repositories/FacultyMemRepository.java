package com.hear.hear.Repositories;

import com.hear.hear.entities.Class;
import com.hear.hear.entities.FacultyMember;
import com.hear.hear.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.util.Optional;
import java.util.Set;

public interface FacultyMemRepository extends JpaRepository<FacultyMember,Integer> {

    @Query("SELECT c FROM FacultyMember f join f.classes c WHERE f.user.id = :userId")
    Set<com.hear.hear.entities.Class> findClassesForFaculty(Integer userId);

    @Query("SELECT c FROM Class c JOIN c.facultyMember s WHERE s.user.id = :userId AND c.day = :day")
    Set<Class> findClassesForFacultyMemberByDay(@Param("userId") Integer userId, @Param("day") DayOfWeek day);

    @Query("SELECT f FROM FacultyMember f  WHERE f.user.id = :userId")
    Optional<FacultyMember>findFacultyMemberByUserId(@Param("userId")Integer userId);

    void deleteByUser(User user);

    Optional<FacultyMember> findByUser(User user);
}
