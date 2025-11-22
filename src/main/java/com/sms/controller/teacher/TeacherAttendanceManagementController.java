package com.sms.controller.teacher;

import com.sms.entity.Teacher;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

@Controller
public class TeacherAttendanceManagementController {

    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> classComboBox;
    @FXML private ComboBox<String> courseComboBox;
    @FXML private TableView<String> attendanceTable;

    private Teacher currentTeacher;

    public void setCurrentTeacher(Teacher teacher) {
        this.currentTeacher = teacher;
        initializeData();
    }

    @FXML
    public void initialize() {
        initializeComboBoxes();
        datePicker.setValue(LocalDate.now());
    }

    private void initializeComboBoxes() {
        // 初始化班级选择
        classComboBox.getItems().addAll("全部班级", "计算机1班", "计算机2班", "软件工程1班");
        classComboBox.getSelectionModel().select(0);

        // 初始化课程选择
        courseComboBox.getItems().addAll("全部课程", "Java程序设计", "数据库原理", "操作系统");
        courseComboBox.getSelectionModel().select(0);
    }

    private void initializeData() {
        loadAttendanceData();
    }

    @FXML
    private void handleQuery() {
        loadAttendanceData();
    }

    @FXML
    private void handleRecordAttendance() {
        // 记录考勤
        System.out.println("记录考勤...");
    }

    private void loadAttendanceData() {
        // 加载考勤数据
        System.out.println("加载考勤数据...");
    }
}