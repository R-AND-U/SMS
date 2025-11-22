package com.sms.service;

import com.sms.entity.Course;
import com.sms.entity.Student;
import com.sms.entity.Teacher;
import com.sms.repository.CourseRepository;
import com.sms.repository.StudentRepository;
import com.sms.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository; // 使用Repository而不是Service

    @Autowired
    private StudentRepository studentRepository; // 使用Repository而不是Service

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Optional<Course> findById(Long id) { // 改为Long
        return courseRepository.findById(id);
    }

    public Optional<Course> findByCode(String code) {
        return courseRepository.findByCode(code);
    }

    public List<Course> findByNameContaining(String name) {
        return courseRepository.findByNameContaining(name);
    }

    public List<Course> findByTeacherId(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId);
    }

    public List<Course> findByStudentId(Long studentId) {
        return courseRepository.findByStudentId(studentId);
    }

    public List<Course> findCoursesWithoutTeacher() {
        return courseRepository.findCoursesWithoutTeacher();
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public void deleteById(Long id) { // 改为Long
        courseRepository.deleteById(id);
    }

    public Course assignTeacherToCourse(Long courseId, Long teacherId) { // 改为Long
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        Optional<Teacher> teacherOpt = teacherRepository.findById(teacherId);

        if (courseOpt.isPresent() && teacherOpt.isPresent()) {
            Course course = courseOpt.get();
            course.setTeacher(teacherOpt.get());
            return courseRepository.save(course);
        }

        return null;
    }

    public Course addStudentToCourse(Long courseId, Long studentId) { // 改为Long
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        Optional<Student> studentOpt = studentRepository.findById(studentId);

        if (courseOpt.isPresent() && studentOpt.isPresent()) {
            Course course = courseOpt.get();
            Student student = studentOpt.get();

            course.getStudents().add(student);
            return courseRepository.save(course);
        }
        return null;
    }

    public void removeStudentFromCourse(Long id, Long id1) {
        Optional<Course> courseOpt = courseRepository.findById(id);
        Optional<Student> studentOpt = studentRepository.findById(id1);

        if (courseOpt.isPresent() && studentOpt.isPresent()) {
            Course course = courseOpt.get();
            Student student = studentOpt.get();

            course.getStudents().remove(student);
            courseRepository.save(course);
        }
    }

}