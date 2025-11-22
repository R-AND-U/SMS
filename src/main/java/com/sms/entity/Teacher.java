package com.sms.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@Entity
@Table(name = "teacher")
@EqualsAndHashCode(callSuper = true)
public class Teacher extends User {

    @Column(name = "teacher_id", length = 10, nullable = false, unique = true)
    private String teacherId;

    @Column(length = 50)
    private String department;

    @Column(name = "office_location", length = 50)
    private String officeLocation;

    @OneToMany(mappedBy = "teacher")
    private Set<Course> courses;

}
