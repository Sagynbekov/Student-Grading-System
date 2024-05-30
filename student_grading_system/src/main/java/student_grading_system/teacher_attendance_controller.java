package student_grading_system;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class teacher_attendance_controller implements Initializable {

    @FXML
    private ComboBox<String> class_name_filter_input;

    @FXML
    private ComboBox<String> class_name_input;

    @FXML
    private TableColumn<Attendance, String> full_name_column;

    @FXML
    private TableColumn<Attendance, Integer> amount_column;

    @FXML
    private TableColumn<Attendance, String> date_column;

    @FXML
    private TableView<Attendance> attendance_table;

    @FXML
    private TextField last_name_input;

    @FXML
    private TextField name_input;

    @FXML
    private DatePicker date_input;

    private ObservableList<Attendance> attendance_list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        class_name_input.getItems().addAll("PL1", "PL2", "Probability and Statistic", "Computer Science", "Discrete Math", "Computer Architecture", "Linear Algebra", "English");
        class_name_filter_input.getItems().addAll("PL1", "PL2", "Probability and Statistic", "Computer Science", "Discrete Math", "Computer Architecture", "Linear Algebra", "English");

        full_name_column.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        amount_column.setCellValueFactory(new PropertyValueFactory<>("amount"));
        date_column.setCellValueFactory(new PropertyValueFactory<>("date"));

        loadData();
        attendance_table.setItems(attendance_list);

        // Добавляем слушатель для фильтрации при изменении значения в выпадающем списке
        class_name_filter_input.setOnAction(this::filter_class);
    }

    @FXML
    void submit_button(ActionEvent event) {
        String name = name_input.getText().trim();
        String lastName = last_name_input.getText().trim();
        String className = class_name_input.getValue();
        String date = date_input.getValue().toString();

        if (name.isEmpty() || lastName.isEmpty() || className == null || date.isEmpty()) {
            System.out.println("All fields must be filled");
            return;
        }

        String fullName = lastName + " " + name;
        boolean studentExists = false;
        Attendance existingRecord = null;

        // Найти существующую запись
        for (Attendance record : attendance_list) {
            if (record.getFullName().equals(fullName) && record.getClassName().equals(className)) {
                existingRecord = record;
                studentExists = true;
                break;
            }
        }

        if (studentExists) {
            // Обновить существующую запись
            existingRecord.addDate(date);
            attendance_list.remove(existingRecord);
            attendance_list.add(existingRecord);
        } else {
            // Добавить новую запись
            Attendance newRecord = new Attendance(fullName, 1, date, className);
            attendance_list.add(newRecord);
        }

        saveData();
        filter_class(null); // Обновить таблицу после добавления новой записи
        attendance_table.refresh(); // Обновить таблицу для немедленного отображения изменений
    }


    private void loadData() {
        File file = new File("C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\attendance_database.txt");
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(" \\| ");
                    if (parts.length == 4) {
                        String fullName = parts[0];
                        int amount = Integer.parseInt(parts[1]);
                        String date = parts[2];
                        String className = parts[3];
                        Attendance record = new Attendance(fullName, amount, date, className);
                        attendance_list.add(record);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveData() {
        try (FileWriter writer = new FileWriter("C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\attendance_database.txt")) {
            for (Attendance record : attendance_list) {
                writer.write(record.getFullName() + " | " + record.getAmount() + " | " + record.getDate() + " | " + record.getClassName() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void filter_class(ActionEvent event) {
        String selectedClass = class_name_filter_input.getValue();
        if (selectedClass != null && !selectedClass.isEmpty()) {
            ObservableList<Attendance> filteredList = attendance_list.stream()
                    .filter(record -> record.getClassName().equals(selectedClass))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            attendance_table.setItems(filteredList);
        } else {
            attendance_table.setItems(attendance_list);
        }
    }

    @FXML
    void add_grade_button(ActionEvent event) throws IOException {
        App.setRoot("teacher_add_grade_scene");
    }

    @FXML
    void attendance_button(ActionEvent event) {
        // Already on the attendance scene
    }

    @FXML
    void watch_profile_button(ActionEvent event) throws IOException {
        App.setRoot("teacher_watch_profile_scene");
    }

    @FXML
    void raiting_button(ActionEvent event) throws IOException {
        App.setRoot("teacher_raiting_scene");
    }

    @FXML
    void schedule_button(ActionEvent event) throws IOException {
        App.setRoot("teacher_schedule_scene");
    }

    @FXML
    void sign_out_button(ActionEvent event) throws IOException {
        App.setRoot("loginscene");
        App.setSize(600, 435);
    }
}













