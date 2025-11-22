package com.sms.controller.student;

import com.sms.entity.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Controller;

@Controller
public class StudentGradesController {

    @FXML private ComboBox<String> semesterComboBox;
    @FXML private TableView<String> gradeTable;
    @FXML private Label totalCreditsLabel;
    @FXML private Label averageScoreLabel;
    @FXML private Label gpaLabel;

    private Student currentStudent;

    public void setCurrentStudent(Student student) {
        this.currentStudent = student;
        initializeData();
    }

    @FXML
    public void initialize() {
        initializeComboBox();
    }

    private void initializeComboBox() {
        ObservableList<String> semesters = FXCollections.observableArrayList(
            "全部学期",
            "2023-2024学年第一学期",
            "2023-2024学年第二学期",
            "2024-2025学年第一学期"
        );
        semesterComboBox.setItems(semesters);
        semesterComboBox.getSelectionModel().select(0);
    }

    private void initializeData() {
        loadGrades();
        calculateStatistics();
    }

    @FXML
    private void handleQuery() {
        loadGrades();
        calculateStatistics();
    }

    private void loadGrades() {
        // 加载成绩数据
        System.out.println("加载成绩数据...");
    }

    private void calculateStatistics() {
        // 计算统计数据
        totalCreditsLabel.setText("25");
        averageScoreLabel.setText("85.6");
        gpaLabel.setText("3.5");
    }
}