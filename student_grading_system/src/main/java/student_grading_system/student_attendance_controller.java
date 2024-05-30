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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class student_attendance_controller implements Initializable {

    @FXML
    private ComboBox<String> class_name_filter_input;

    @FXML
    private TableView<Attendance> attendance_table;

    @FXML
    private TableColumn<Attendance, String> full_name_column;

    @FXML
    private TableColumn<Attendance, Integer> amount_column;

    @FXML
    private TableColumn<Attendance, String> date_column;


    private ObservableList<Attendance> attendance_list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        class_name_filter_input.getItems().addAll("PL1", "PL2", "Probability and Statistic", "Computer Science", "Discrete Math", "Computer Architecture", "Linear Algebra", "English");

        full_name_column.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        amount_column.setCellValueFactory(new PropertyValueFactory<>("amount"));
        date_column.setCellValueFactory(new PropertyValueFactory<>("date"));

        loadData();
        attendance_table.setItems(attendance_list);

        // Добавляем слушатель для фильтрации при изменении значения в выпадающем списке
        class_name_filter_input.setOnAction(this::filter_class);
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
    void grades_button(ActionEvent event) throws IOException {
        App.setRoot("student_grades_scene");
    }

    @FXML
    void raiting_button(ActionEvent event) throws IOException {
        App.setRoot("student_raiting_scene");
    }

    @FXML
    void attendance_button(ActionEvent event) {

    }

    @FXML
    void schedule_button(ActionEvent event) throws IOException {
        App.setRoot("student_schedule_scene");
    }

    @FXML
    void sign_out_button(ActionEvent event) throws IOException {
        App.setRoot("loginscene");
        App.setSize(600, 435);
    }
}



















