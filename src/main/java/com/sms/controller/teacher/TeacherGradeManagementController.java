package com.sms.controller.teacher;

import com.sms.entity.Teacher;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Controller;

@Controller
public class TeacherGradeManagementController {

    @FXML private ComboBox<String> semesterComboBox;
    @FXML private ComboBox<String> classComboBox;
    @FXML private ComboBox<String> courseComboBox;
    @FXML private TableView<String> gradeTable;

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
        // 初始化学期选择
        semesterComboBox.getItems().addAll("全部学期", "2023-2024第一学期", "2023-2024第二学期");
        semesterComboBox.getSelectionModel().select(0);

        // 初始化班级选择
        classComboBox.getItems().addAll("全部班级", "计算机1班", "计算机2班", "软件工程1班");
        classComboBox.getSelectionModel().select(0);

        // 初始化课程选择
        courseComboBox.getItems().addAll("全部课程", "Java程序设计", "数据库原理", "操作系统");
        courseComboBox.getSelectionModel().select(0);
    }

    private void initializeData() {
        loadGradeData();
    }

    @FXML
    private void handleQuery() {
        loadGradeData();
    }

    @FXML
    private void handleImport() {
        // 导入成绩
        System.out.println("导入成绩...");
    }

    @FXML
    private void handleExport() {
        // 导出成绩
        System.out.println("导出成绩...");
    }

    private void loadGradeData() {
        // 加载成绩数据
        System.out.println("加载成绩数据...");
    }
}