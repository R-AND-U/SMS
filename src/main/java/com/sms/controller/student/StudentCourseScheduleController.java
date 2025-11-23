package com.sms.controller.student;

import com.sms.entity.Course;
import com.sms.entity.Student;
import com.sms.enums.Weekday;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Set;

@Controller
public class StudentCourseScheduleController {

    @FXML private ComboBox<String> semesterComboBox;
    @FXML private ComboBox<Integer> weekComboBox;
    @FXML private TableView<Course> courseTable;
    ObservableList<Course> courseData = FXCollections.observableArrayList();
    boolean isTableInitialized = false;

    private Student currentStudent;

    public void setCurrentStudent(Student student) {
        this.currentStudent = student;
        initializeData();
    }

    @FXML
    public void initialize() {
        initializeComboBoxes();
        initializeTable(); // 需要在这里初始化表格
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

    private void initializeTable() {
        // 设置表格数据
        courseTable.setItems(courseData);
        courseTable.setEditable(true);

        // 创建时间列
        TableColumn<Course, String> timeCol = createTableColumns("时间", "time", 100, false);
        courseTable.getColumns().add(timeCol);

        // 创建星期列
        Arrays.stream(Weekday.values())
                .map(weekday -> createTableColumns(weekday.getDisplayName(), weekday.getPeopertyName(), 150, true))
                .forEach(column -> courseTable.getColumns().add(column));

        isTableInitialized = true;
    }

    private void setCellFactory(TableColumn<Course, String> column) {
        column.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                    setAlignment(Pos.CENTER);
                }
            }
        });
    }

    private TableColumn<Course, String> createTableColumns(String header, String property, double width, boolean applyCellFactory) {
        TableColumn<Course, String> column = new TableColumn<>(header);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setPrefWidth(width);

        if (applyCellFactory) {
            setCellFactory(column);
        }

        return column;
    }

    private void initializeData() {
        loadCourseSchedule();
    }

    @FXML
    private void handleQuery() {
        loadCourseSchedule();
    }

    private void loadCourseSchedule() {
        if (currentStudent != null) {
            Set<Course> courses = currentStudent.getCourses();
            courseData.setAll(courses); // 使用 setAll 来更新数据
        }
    }
}