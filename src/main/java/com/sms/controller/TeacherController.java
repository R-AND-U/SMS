package com.sms.controller;

import com.sms.entity.Teacher;
import com.sms.service.TeacherService;
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
public class TeacherController {

    @FXML private Label welcomeLabel;
    @FXML private VBox contentArea;

    @Autowired
    private TeacherService teacherService;

    private Teacher currentTeacher;

    public void setCurrentTeacher(Teacher teacher) {
        this.currentTeacher = teacher;
        updateWelcomeMessage();
    }

    @FXML
    public void initialize() {
        // 初始化内容
    }

    private void updateWelcomeMessage() {
        if (currentTeacher != null) {
            welcomeLabel.setText("欢迎您，" + currentTeacher.getName());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("学生管理系统 - 登录");
        } catch (IOException e) {
            showAlert("错误", "退出登录失败: " + e.getMessage());
        }
    }

    // 菜单点击处理方法
    @FXML
    private void showStudentManagement() {
        clearContent();
        Label label = new Label("学生信息管理功能开发中...");
        contentArea.getChildren().add(label);
    }

    @FXML
    private void showAttendanceManagement() {
        clearContent();
        Label label = new Label("考勤管理功能开发中...");
        contentArea.getChildren().add(label);
    }

    @FXML
    private void showVacationManagement() {
        clearContent();
        Label label = new Label("请假信息管理功能开发中...");
        contentArea.getChildren().add(label);
    }

    @FXML
    private void showGradeManagement() {
        clearContent();
        Label label = new Label("成绩信息管理功能开发中...");
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

