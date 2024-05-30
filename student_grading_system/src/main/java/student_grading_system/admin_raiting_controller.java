package student_grading_system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;



public class admin_raiting_controller implements Initializable {

    @FXML
    private TableView<Grade> raiting_table;

    @FXML
    private TableColumn<Grade, String> place_column;

    @FXML
    private TableColumn<Grade, String> full_name_column;

    @FXML
    private TableColumn<Grade, String> average_grade_column;

    @FXML
    private ComboBox<String> class_name_filter_input;

    @FXML
    private ComboBox<String> type_filter_input;

    private ObservableList<Grade> gradesList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        class_name_filter_input.getItems().addAll("PL1", "PL2", "Probability and Statistic", "Computer Science", "Discrete Math", "Computer Architecture", "Linear Algebra", "English");
        type_filter_input.getItems().addAll("Quiz", "Midterm", "Final");

        class_name_filter_input.setOnAction(event -> filterAndDisplayGrades());
        type_filter_input.setOnAction(event -> filterAndDisplayGrades());

        full_name_column.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        average_grade_column.setCellValueFactory(cellData -> {
            Grade grade = cellData.getValue();
            String className = class_name_filter_input.getValue();
            String type = type_filter_input.getValue();
            double average = grade.getAverageGrade(className, type);
            return new javafx.beans.property.SimpleStringProperty(String.format("%.2f", average));
        });

        place_column.setCellValueFactory(cellData -> {
            Grade grade = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(Integer.toString(gradesList.indexOf(grade) + 1));
        });

        loadData();
    }

    private void loadData() {
        File f = new File("C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\grades_database.txt");
        try (Scanner scan = new Scanner(f)) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] parts = line.split(",");
                String fullName = parts[0];
                Grade grade = new Grade(fullName);
                for (int i = 1; i < parts.length; i++) {
                    String[] classInfo = parts[i].split(":");
                    String className = classInfo[0];
                    String[] typeGrades = classInfo[1].split(";");
                    for (String typeGrade : typeGrades) {
                        String[] tg = typeGrade.split("-");
                        if (tg.length == 2) {
                            String type = tg[0];
                            String[] grades = tg[1].trim().split(" ");
                            for (String g : grades) {
                                if (!g.isEmpty()) {
                                    grade.addGrade(className, type, Integer.parseInt(g));
                                }
                            }
                        }
                    }
                }
                gradesList.add(grade);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    private void filterAndDisplayGrades() {
        String selectedClass = class_name_filter_input.getValue();
        String selectedType = type_filter_input.getValue();

        if (selectedClass == null || selectedClass.isEmpty() || selectedType == null || selectedType.isEmpty()) {
            raiting_table.setItems(FXCollections.observableArrayList());
            return;
        }

        List<Grade> filteredGrades = new ArrayList<>();
        for (Grade grade : gradesList) {
            if (grade.getClassGrades().containsKey(selectedClass) && grade.getClassGrades().get(selectedClass).containsKey(selectedType)) {
                filteredGrades.add(grade);
            }
        }

        filteredGrades.sort((g1, g2) -> Double.compare(g2.getAverageGrade(selectedClass, selectedType), g1.getAverageGrade(selectedClass, selectedType)));

        raiting_table.setItems(FXCollections.observableArrayList(filteredGrades));
        raiting_table.refresh();
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

    }

    @FXML
    private void watch_profile_button(ActionEvent event) throws IOException {
        App.setRoot("admin_watch_profile_scene");
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


