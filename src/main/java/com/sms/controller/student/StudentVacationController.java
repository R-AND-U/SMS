package com.sms.controller.student;

import com.sms.entity.Student;
import com.sms.entity.Vacation;
import com.sms.entity.Course;
import com.sms.service.VacationService;
import com.sms.service.CourseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
public class StudentVacationController {

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<String> vacationTypeComboBox;
    @FXML private ComboBox<Course> courseComboBox;
    @FXML private TextArea reasonTextArea;
    @FXML private TableView<Vacation> vacationTable;

    @Autowired
    private VacationService vacationService;

    @Autowired
    private CourseService courseService;

    private Student currentStudent;

    public void setCurrentStudent(Student student) {
        this.currentStudent = student;
        // 使用 Platform.runLater 确保在 UI 线程中执行
        javafx.application.Platform.runLater(() -> {
            initializeData();
        });
    }

    @FXML
    public void initialize() {
        // 基础初始化，不依赖学生数据
        initializeComboBox();
        setupDatePickers();

        // 添加调试信息
        System.out.println("StudentVacationController initialized");
        System.out.println("courseComboBox is null: " + (courseComboBox == null));
    }

    private void initializeComboBox() {
        ObservableList<String> types = FXCollections.observableArrayList(
            "事假", "病假", "公假", "其他"
        );
        vacationTypeComboBox.setItems(types);
        if (!types.isEmpty()) {
            vacationTypeComboBox.getSelectionModel().select(0);
        }
    }

    private void setupDatePickers() {
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now().plusDays(1));
    }

    private void initializeData() {
        loadCourses();
        loadVacationHistory();
    }

    private void loadCourses() {
        // 安全检查
        if (courseComboBox == null) {
            System.err.println("courseComboBox is null in loadCourses!");
            return;
        }

        if (currentStudent != null) {
            try {
                // 获取学生已选的课程
                List<Course> courses = courseService.findByStudentId(currentStudent.getId());
                ObservableList<Course> courseList = FXCollections.observableArrayList(courses);
                courseComboBox.setItems(courseList);

                if (!courseList.isEmpty()) {
                    courseComboBox.getSelectionModel().select(0);
                }

                System.out.println("Loaded " + courses.size() + " courses for student");
            } catch (Exception e) {
                System.err.println("Error loading courses: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("currentStudent is null in loadCourses!");
        }
    }

    @FXML
    private void handleSubmit() {
        if (validateInput()) {
            submitVacationApplication();
        }
    }

    private boolean validateInput() {
        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            showAlert("错误", "请选择请假日期");
            return false;
        }

        if (startDatePicker.getValue().isAfter(endDatePicker.getValue())) {
            showAlert("错误", "开始日期不能晚于结束日期");
            return false;
        }

        if (courseComboBox.getSelectionModel().isEmpty()) {
            showAlert("错误", "请选择课程");
            return false;
        }

        if (reasonTextArea.getText().trim().isEmpty()) {
            showAlert("错误", "请输入请假原因");
            return false;
        }

        return true;
    }

    private void submitVacationApplication() {
        try {
            Vacation vacation = new Vacation();
            vacation.setStartDate(startDatePicker.getValue());
            vacation.setEndDate(endDatePicker.getValue());
            vacation.setReason(reasonTextArea.getText().trim());
            vacation.setStudent(currentStudent);
            vacation.setCourse(courseComboBox.getValue());

            vacationService.save(vacation);

            showAlert("成功", "请假申请提交成功");
            resetForm();
            loadVacationHistory();

        } catch (Exception e) {
            showAlert("错误", "提交失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void resetForm() {
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now().plusDays(1));
        reasonTextArea.clear();
        if (courseComboBox != null) {
            courseComboBox.getSelectionModel().clearSelection();
        }
        loadCourses();
    }

    private void loadVacationHistory() {
        if (currentStudent != null) {
            try {
                List<Vacation> vacations = vacationService.findByStudentId(currentStudent.getId());
                vacationTable.getItems().setAll(vacations);
                System.out.println("Loaded " + vacations.size() + " vacation records");
            } catch (Exception e) {
                System.err.println("Error loading vacation history: " + e.getMessage());
                e.printStackTrace();
            }
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