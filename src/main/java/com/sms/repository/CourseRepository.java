package com.sms.repository;

import com.sms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCode(String code);

    List<Course> findByNameContaining(String name);

    @Query("SELECT c FROM Course c WHERE c.teacher.id = :teacherId")

    List<Course> findByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId")
    List<Course> findByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT c FROM Course c WHERE c.teacher IS NULL")
    List<Course> findCoursesWithoutTeacher();

}