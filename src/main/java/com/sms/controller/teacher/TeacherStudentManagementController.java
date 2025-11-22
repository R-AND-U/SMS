package com.sms.controller.teacher;

import com.sms.entity.Student;
import com.sms.entity.Teacher;
import com.sms.entity.Course;
import com.sms.service.StudentService;
import com.sms.service.CourseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TeacherStudentManagementController {

    @FXML private ComboBox<String> classComboBox;
    @FXML private ComboBox<Course> courseComboBox; // 改为Course对象
    @FXML private TextField nameField;
    @FXML private TableView<Student> studentTable;

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    private Teacher currentTeacher;

    public void setCurrentTeacher(Teacher teacher) {
        this.currentTeacher = teacher;
        initializeData();
    }

    @FXML
    public void initialize() {
        initializeComboBoxes();
    }

    private void initializeComboBoxes() {
        // 初始化班级选择
        classComboBox.getItems().addAll("全部班级", "计算机1班", "计算机2班", "软件工程1班");
        classComboBox.getSelectionModel().select(0);

        // 初始化课程选择：加载教师所教课程
        if (currentTeacher != null) {
            List<Course> courses = courseService.findByTeacherId(currentTeacher.getId());
            courseComboBox.getItems().addAll(courses);
            courseComboBox.getItems().add(0, null); // 添加一个空选项表示全部课程
            courseComboBox.getSelectionModel().select(0);
        }
    }

    private void initializeData() {
        loadStudentData();
    }

    @FXML
    private void handleQuery() {
        loadStudentData();
    }

    @FXML
    private void handleReset() {
        classComboBox.getSelectionModel().select(0);
        courseComboBox.getSelectionModel().select(0);
        nameField.clear();
        loadStudentData();
    }

    @FXML
    private void handleExport() {
        // 导出学生信息
        System.out.println("导出学生信息...");
    }

    private void loadStudentData() {
        // 根据班级和课程过滤学生
        String selectedClass = classComboBox.getSelectionModel().getSelectedItem();
        Course selectedCourse = courseComboBox.getSelectionModel().getSelectedItem();

        List<Student> students;
        if (selectedCourse != null) {
            // 如果选择了课程，则查询该课程的学生
            students = studentService.findByCourseId(selectedCourse.getId());
        } else {
            // 否则查询所有学生
            students = studentService.findAll();
        }

        // 如果选择了班级，则过滤班级
        if (selectedClass != null && !selectedClass.equals("全部班级")) {
            students = students.stream()
                    .filter(s -> selectedClass.equals(s.getClassName()))
                    .collect(Collectors.toList());
        }

        // 如果输入了姓名，则过滤姓名
        String name = nameField.getText().trim();
        if (!name.isEmpty()) {
            students = students.stream()
                    .filter(s -> s.getName().contains(name))
                    .collect(Collectors.toList());
        }

        studentTable.setItems(FXCollections.observableArrayList(students));
    }
}