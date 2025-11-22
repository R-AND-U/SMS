package com.sms.controller.teacher;

import com.sms.entity.Teacher;
import com.sms.entity.Vacation;
import com.sms.entity.Course;
import com.sms.enums.VacationStatus;
import com.sms.service.VacationService;
import com.sms.service.CourseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TeacherVacationManagementController {

    @FXML private ComboBox<VacationStatus> statusComboBox;
    @FXML private ComboBox<String> classComboBox;
    @FXML private ComboBox<Course> courseComboBox; // 添加课程选择
    @FXML private TextField nameField;
    @FXML private TableView<Vacation> vacationTable;

    @Autowired
    private VacationService vacationService;

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
        initializeTable();
    }

    private void initializeComboBoxes() {
        // 初始化状态选择
        statusComboBox.setItems(FXCollections.observableArrayList(VacationStatus.values()));
        statusComboBox.getSelectionModel().select(0);

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

    private void initializeTable() {
        // 初始化表格列
        TableColumn<Vacation, String> studentIdCol = new TableColumn<>("学号");
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("student.studentId"));

        TableColumn<Vacation, String> studentNameCol = new TableColumn<>("姓名");
        studentNameCol.setCellValueFactory(new PropertyValueFactory<>("student.name"));

        TableColumn<Vacation, String> classNameCol = new TableColumn<>("班级");
        classNameCol.setCellValueFactory(new PropertyValueFactory<>("student.className"));

        // 添加课程列
        TableColumn<Vacation, String> courseNameCol = new TableColumn<>("课程");
        courseNameCol.setCellValueFactory(new PropertyValueFactory<>("course.name"));

        TableColumn<Vacation, String> startDateCol = new TableColumn<>("开始日期");
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startDateCol.setCellFactory(column -> new TableCell<Vacation, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(getTableView().getItems().get(getIndex()).getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
            }
        });

        TableColumn<Vacation, String> endDateCol = new TableColumn<>("结束日期");
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endDateCol.setCellFactory(column -> new TableCell<Vacation, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(getTableView().getItems().get(getIndex()).getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
            }
        });

        TableColumn<Vacation, String> reasonCol = new TableColumn<>("请假原因");
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));

        TableColumn<Vacation, VacationStatus> statusCol = new TableColumn<>("状态");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Vacation, Void> actionCol = new TableColumn<>("操作");
        actionCol.setCellFactory(new Callback<TableColumn<Vacation, Void>, TableCell<Vacation, Void>>() {
            @Override
            public TableCell<Vacation, Void> call(TableColumn<Vacation, Void> param) {
                return new TableCell<Vacation, Void>() {
                    private final Button approveButton = new Button("批准");
                    private final Button rejectButton = new Button("拒绝");

                    {
                        approveButton.setOnAction(event -> {
                            Vacation vacation = getTableView().getItems().get(getIndex());
                            handleApprove(vacation);
                        });

                        rejectButton.setOnAction(event -> {
                            Vacation vacation = getTableView().getItems().get(getIndex());
                            handleReject(vacation);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Vacation vacation = getTableView().getItems().get(getIndex());
                            if (vacation.getStatus() == VacationStatus.PENDING) {
                                setGraphic(new javafx.scene.layout.HBox(5, approveButton, rejectButton));
                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        });

        vacationTable.getColumns().setAll(studentIdCol, studentNameCol, classNameCol, courseNameCol,
                                         startDateCol, endDateCol, reasonCol, statusCol, actionCol);
    }

    private void initializeData() {
        loadVacationData();
    }

    @FXML
    private void handleQuery() {
        loadVacationData();
    }

    private void loadVacationData() {
        // 根据课程过滤请假数据
        Course selectedCourse = courseComboBox.getSelectionModel().getSelectedItem();
        VacationStatus selectedStatus = statusComboBox.getSelectionModel().getSelectedItem();

        List<Vacation> vacations;
        if (selectedCourse != null) {
            // 查询特定课程的请假
            vacations = vacationService.findByCourseId(selectedCourse.getId());
        } else {
            // 查询教师所教所有课程的请假
            List<Course> teacherCourses = courseService.findByTeacherId(currentTeacher.getId());
            vacations = teacherCourses.stream()
                    .flatMap(course -> vacationService.findByCourseId(course.getId()).stream())
                    .collect(Collectors.toList());
        }

        // 根据状态过滤
        if (selectedStatus != null) {
            vacations = vacations.stream()
                    .filter(v -> v.getStatus() == selectedStatus)
                    .collect(Collectors.toList());
        }

        vacationTable.setItems(FXCollections.observableArrayList(vacations));
    }

    private void handleApprove(Vacation vacation) {
        try {
            vacationService.approveVacation(vacation.getId(), currentTeacher.getId());
            showAlert("成功", "已批准请假申请");
            loadVacationData();
        } catch (Exception e) {
            showAlert("错误", "批准失败: " + e.getMessage());
        }
    }

    private void handleReject(Vacation vacation) {
        try {
            vacationService.rejectVacation(vacation.getId());
            showAlert("成功", "已拒绝请假申请");
            loadVacationData();
        } catch (Exception e) {
            showAlert("错误", "拒绝失败: " + e.getMessage());
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