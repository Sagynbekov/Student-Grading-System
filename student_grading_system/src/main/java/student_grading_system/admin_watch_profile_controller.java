package student_grading_system;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class admin_watch_profile_controller implements Initializable {

    @FXML
    private TableView<Student> students_table;

    @FXML
    private TableColumn<Student, String> student_id_column;

    @FXML
    private TableColumn<Student, String> name_column;

    @FXML
    private TableColumn<Student, String> last_name_column;

    @FXML
    private TableColumn<Student, String> birth_date_column;

    @FXML
    private TableColumn<Student, String> gender_column;

    @FXML
    private TableColumn<Student, String> year_column;

    @FXML
    private TableColumn<Student, String> semestr_column;

    @FXML
    private TextField search_input;

    private ObservableList<Student> studentsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        student_id_column.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        last_name_column.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        birth_date_column.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        gender_column.setCellValueFactory(new PropertyValueFactory<>("gender"));
        year_column.setCellValueFactory(new PropertyValueFactory<>("year"));
        semestr_column.setCellValueFactory(new PropertyValueFactory<>("semestr"));

        loadStudentsData();

        FilteredList<Student> filteredData = new FilteredList<>(studentsList, p -> true);

        search_input.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (student.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (student.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Student> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(students_table.comparatorProperty());
        students_table.setItems(sortedData);
        students_table.setFixedCellSize(25);
    }

    private void loadStudentsData() {
        File file = new File("C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\st_database.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                Student student = new Student(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
                studentsList.add(student);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void make_report_button(ActionEvent event) {
        Student selectedStudent = students_table.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            try {
                FileWriter writer = new FileWriter("C:\\Users\\user\\Desktop\\student_grading_system\\reports\\" + selectedStudent.getName() + "_" + selectedStudent.getLastName() + "_report.txt");

                writer.write("Student id: " + selectedStudent.getStudentId() + "\n");
                writer.write("Name: " + selectedStudent.getName() + "\n");
                writer.write("Last name: " + selectedStudent.getLastName() + "\n");
                writer.write("Birth Date: " + selectedStudent.getBirthDate() + "\n");
                writer.write("Gender: " + selectedStudent.getGender() + "\n");
                writer.write("Year: " + selectedStudent.getYear() + "\n");
                writer.write("Semestr: " + selectedStudent.getSemestr() + "\n");
                writer.write("\n");

                // Add grades information from grades_database.txt
                File gradesFile = new File("C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\grades_database.txt");
                try (Scanner scanner = new Scanner(gradesFile)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] parts = line.split(",");
                        if (parts[0].equals(selectedStudent.getName() + " " + selectedStudent.getLastName())) {
                            for (int i = 1; i < parts.length; i++) {
                                String[] classInfo = parts[i].split(":");
                                String className = classInfo[0];
                                String[] typeGrades = classInfo[1].split(";");
                                writer.write("=======" + className + "=======\n");
                                for (String typeGrade : typeGrades) {
                                    String[] tg = typeGrade.split("-");
                                    if (tg.length == 2) {
                                        String type = tg[0];
                                        String[] grades = tg[1].trim().split(" ");
                                        double average = Arrays.stream(grades)
                                                .mapToInt(Integer::parseInt)
                                                .average()
                                                .orElse(0.0);
                                        writer.write(type + ": " + String.format("%.2f", average) + "\n");
                                    }
                                }
                            }
                        }
                    }
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void add_teachers_button(ActionEvent event) throws IOException {
        App.setRoot("admin_add_teachers_scene");
    }
    
    @FXML
    private void add_students_button(ActionEvent event) throws IOException {
        App.setRoot("admin_add_students_scene");
    }
    
    @FXML
    private void rating_button(ActionEvent event) throws IOException {
        App.setRoot("admin_raiting_scene");
    }

    @FXML
    private void watch_profile_button(ActionEvent event) throws IOException {
    }

    @FXML
    private void users_button(ActionEvent event) throws IOException {
        App.setRoot("admin_users_scene");
    }

    @FXML
    private void schedule_button(ActionEvent event) throws IOException {
        App.setRoot("admin_schedule_scene");
    }
    
    @FXML
    private void sign_out_button(ActionEvent event) throws IOException {
        App.setSize(600, 435);
        App.setRoot("loginscene");
    }
    
}
