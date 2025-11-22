package com.sms.entity;


import com.sms.enums.Gender;
import lombok.Data;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

@Data
@Table(name = "student")
@Entity
@EqualsAndHashCode(callSuper = true)
public class Student extends User {

// private:

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private Gender gender;

    @Column(name = "student_id", length = 10, nullable = false)
    private String studentId;

    @Column(name = "class_name", length = 50, nullable = false)
    private String className;

    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id")
    )
    private Set<Course> courses;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vacation> vacations;

}
