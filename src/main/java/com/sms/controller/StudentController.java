package com.sms.controller;

import com.sms.JavaFxApplication;
import com.sms.entity.Student;
import com.sms.service.StudentService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class StudentController {

    @FXML private Label welcomeLabel;
    @FXML private VBox contentArea;

    private final StudentService studentService;
    private Student currentStudent;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    public void setCurrentStudent(Student student) {
        this.currentStudent = student;
        updateWelcomeMessage();
    }

    @FXML
    public void initialize() {
        // 初始化内容
    }

    private void updateWelcomeMessage() {
        if (currentStudent != null) {
            welcomeLabel.setText("欢迎您，" + currentStudent.getName());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            loader.setControllerFactory(JavaFxApplication.getSpringContext()::getBean);
            Parent root = loader.load();

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("学生管理系统 - 登录");
        } catch (IOException e) {
            showAlert("错误", "退出登录失败: " + e.getMessage());
        }
    }

    // 菜单点击处理方法
    @FXML
    private void showPersonalInfo() {
        clearContent();
        Label label = new Label("个人信息查询功能开发中...");
        contentArea.getChildren().add(label);
    }

    @FXML
    private void showCourseSchedule() {
        clearContent();
        Label label = new Label("课表查询功能开发中...");
        contentArea.getChildren().add(label);
    }

    @FXML
    private void showCourseSelection() {
        clearContent();
        Label label = new Label("选课功能开发中...");
        contentArea.getChildren().add(label);
    }

    @FXML
    private void showGradeQuery() {
        clearContent();
        Label label = new Label("成绩查询功能开发中...");
        contentArea.getChildren().add(label);
    }

    @FXML
    private void showVacation() {
        clearContent();
        Label label = new Label("请假功能开发中...");
        contentArea.getChildren().add(label);
    }

    @FXML
    private void showSystemManagement() {
        clearContent();
        Label label = new Label("系统管理功能开发中...");
        contentArea.getChildren().add(label);
    }

    private void clearContent() {
        contentArea.getChildren().clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}