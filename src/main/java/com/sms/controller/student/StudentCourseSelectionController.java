package com.sms.controller.student;

import com.sms.entity.Student;
import com.sms.entity.Course;
import com.sms.service.CourseService;
import com.sms.service.StudentService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StudentCourseSelectionController {

    @FXML private TextField searchField;
    @FXML private TableView<Course> courseTable;
    @FXML private TableView<Course> selectedCourseTable;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    private Student currentStudent;

    public void setCurrentStudent(Student student) {
        this.currentStudent = student;
        loadAvailableCourses();
        loadSelectedCourses();
    }

    @FXML
    public void initialize() {
        initializeTables();
    }

    private void initializeTables() {
        // 初始化可选课程表格
        TableColumn<Course, String> codeCol = new TableColumn<>("课程代码");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));

        TableColumn<Course, String> nameCol = new TableColumn<>("课程名称");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Course, String> teacherCol = new TableColumn<>("授课教师");
        teacherCol.setCellValueFactory(new PropertyValueFactory<>("teacher.name"));

        TableColumn<Course, Void> actionCol = new TableColumn<>("操作");
        actionCol.setCellFactory(new Callback<TableColumn<Course, Void>, TableCell<Course, Void>>() {
            @Override
            public TableCell<Course, Void> call(TableColumn<Course, Void> param) {
                return new TableCell<Course, Void>() {
                    private final Button selectButton = new Button("选课");

                    {
                        selectButton.setOnAction(event -> {
                            Course course = getTableView().getItems().get(getIndex());
                            handleSelectCourse(course);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(selectButton);
                        }
                    }
                };
            }
        });

        courseTable.getColumns().setAll(codeCol, nameCol, teacherCol, actionCol);

        // 初始化已选课程表格
        TableColumn<Course, String> selectedCodeCol = new TableColumn<>("课程代码");
        selectedCodeCol.setCellValueFactory(new PropertyValueFactory<>("code"));

        TableColumn<Course, String> selectedNameCol = new TableColumn<>("课程名称");
        selectedNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Course, String> selectedTeacherCol = new TableColumn<>("授课教师");
        selectedTeacherCol.setCellValueFactory(new PropertyValueFactory<>("teacher.name"));

        TableColumn<Course, Void> selectedActionCol = new TableColumn<>("操作");
        selectedActionCol.setCellFactory(new Callback<TableColumn<Course, Void>, TableCell<Course, Void>>() {
            @Override
            public TableCell<Course, Void> call(TableColumn<Course, Void> param) {
                return new TableCell<Course, Void>() {
                    private final Button dropButton = new Button("退课");

                    {
                        dropButton.setOnAction(event -> {
                            Course course = getTableView().getItems().get(getIndex());
                            handleDropCourse(course);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(dropButton);
                        }
                    }
                };
            }
        });

        selectedCourseTable.getColumns().setAll(selectedCodeCol, selectedNameCol, selectedTeacherCol, selectedActionCol);
    }

    private void loadAvailableCourses() {
        // 加载所有课程，但排除已选课程
        List<Course> allCourses = courseService.findAll();
        List<Course> selectedCourses = courseService.findByStudentId(currentStudent.getId());
        List<Course> availableCourses = allCourses.stream()
                .filter(course -> !selectedCourses.contains(course))
                .collect(Collectors.toList());
        courseTable.getItems().setAll(availableCourses);
    }

    private void loadSelectedCourses() {
        List<Course> selectedCourses = courseService.findByStudentId(currentStudent.getId());
        selectedCourseTable.getItems().setAll(selectedCourses);
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().trim();
        if (!keyword.isEmpty()) {
            // 搜索课程
            List<Course> courses = courseService.findByNameContaining(keyword);
            // 过滤已选课程
            List<Course> selectedCourses = courseService.findByStudentId(currentStudent.getId());
            List<Course> availableCourses = courses.stream()
                    .filter(course -> !selectedCourses.contains(course))
                    .collect(Collectors.toList());
            courseTable.getItems().setAll(availableCourses);
        } else {
            loadAvailableCourses();
        }
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        loadAvailableCourses();
    }

    private void handleSelectCourse(Course course) {
        try {
            courseService.addStudentToCourse(course.getId(), currentStudent.getId());
            showAlert("成功", "选课成功");
            loadAvailableCourses();
            loadSelectedCourses();
        } catch (Exception e) {
            showAlert("错误", "选课失败: " + e.getMessage());
        }
    }

    private void handleDropCourse(Course course) {
        try {
            // 需要实现退课方法，这里假设CourseService有removeStudentFromCourse方法
            courseService.removeStudentFromCourse(course.getId(), currentStudent.getId());
            showAlert("成功", "退课成功");
            loadAvailableCourses();
            loadSelectedCourses();
        } catch (Exception e) {
            showAlert("错误", "退课失败: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}