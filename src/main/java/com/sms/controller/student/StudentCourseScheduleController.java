package com.sms.controller.student;

import com.sms.entity.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Controller;

@Controller
public class StudentCourseScheduleController {

    @FXML private ComboBox<String> semesterComboBox;
    @FXML private ComboBox<Integer> weekComboBox;
    @FXML private TableView<String> courseTable;

    private Student currentStudent;

    public void setCurrentStudent(Student student) {
        this.currentStudent = student;
        initializeData();
    }

    @FXML
    public void initialize() {
        initializeComboBoxes();
    }

    private void initializeComboBoxes() {
        // 初始化学期选择
        ObservableList<String> semesters = FXCollections.observableArrayList(
            "2023-2024学年第一学期",
            "2023-2024学年第二学期",
            "2024-2025学年第一学期"
        );
        semesterComboBox.setItems(semesters);
        semesterComboBox.getSelectionModel().select(0);

        // 初始化周次选择
        ObservableList<Integer> weeks = FXCollections.observableArrayList();
        for (int i = 1; i <= 20; i++) {
            weeks.add(i);
        }
        weekComboBox.setItems(weeks);
        weekComboBox.getSelectionModel().select(0);
    }

    private void initializeData() {
        // 初始化数据
        loadCourseSchedule();
    }

    @FXML
    private void handleQuery() {
        loadCourseSchedule();
    }

    private void loadCourseSchedule() {
        // 模拟课表数据
        // 实际项目中应该从服务层获取数据
        System.out.println("加载课表数据...");
    }
}