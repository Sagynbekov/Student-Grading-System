package student_grading_system;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class teacher_add_grades_controller implements Initializable {
    @FXML
    private ComboBox<String> class_name_filter_input;

    @FXML
    private TableView<Grade> grades_table;

    @FXML
    private TableColumn<Grade, String> full_name_column;
    
    @FXML
    private TableColumn<Grade, String> quizes_column;
    
    @FXML
    private TableColumn<Grade, String> midterms_column;

    @FXML
    private TableColumn<Grade, String> finals_column;

    

    @FXML
    private TextField name_input;

    @FXML
    private TextField last_name_input;

    @FXML
    private ComboBox<String> class_name_input;

    @FXML
    private ComboBox<String> type_input;

    @FXML
    private TextField grade_input;
    

    private ObservableList<Grade> gradesList = FXCollections.observableArrayList();
    private FilteredList<Grade> filteredGrades;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        full_name_column.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        quizes_column.setCellValueFactory(cellData -> {
            Grade grade = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(grade.getQuizes(class_name_filter_input.getValue()));
        });
        midterms_column.setCellValueFactory(cellData -> {
            Grade grade = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(grade.getMidterms(class_name_filter_input.getValue()));
        });
        finals_column.setCellValueFactory(cellData -> {
            Grade grade = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(grade.getFinals(class_name_filter_input.getValue()));
        });

        loadData();
        
        filteredGrades = new FilteredList<>(gradesList, p -> true);
        grades_table.setItems(filteredGrades);
        grades_table.setFixedCellSize(25);

        class_name_input.getItems().addAll("PL1", "PL2", "Probability and Statistic", "Computer Science", "Discrete Math", "Computer Architecture", "Linear Algebra", "English");
        class_name_filter_input.getItems().addAll("PL1", "PL2", "Probability and Statistic", "Computer Science", "Discrete Math", "Computer Architecture", "Linear Algebra", "English");
        type_input.getItems().addAll("Quiz", "Midterm", "Final");

        class_name_filter_input.setOnAction(event -> filterGrades());
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

    private void filterGrades() {
        String selectedClass = class_name_filter_input.getValue();
        if (selectedClass == null || selectedClass.isEmpty()) {
            filteredGrades.setPredicate(grade -> true);
        } else {
            filteredGrades.setPredicate(grade -> grade.getClassGrades().containsKey(selectedClass));
        }
        grades_table.refresh();
    }

    @FXML
    void submit_button(ActionEvent event) {
        String fullName = name_input.getText() + " " + last_name_input.getText();
        String className = class_name_input.getValue();
        String type = type_input.getValue();
        int grade = Integer.parseInt(grade_input.getText());

        Grade studentGrade = findGrade(fullName);
        if (studentGrade == null) {
            studentGrade = new Grade(fullName);
            gradesList.add(studentGrade);
        }

        studentGrade.addGrade(className, type, grade);
        saveAllGradesToFile();
        filterGrades();
    }

    private Grade findGrade(String fullName) {
        for (Grade grade : gradesList) {
            if (grade.getFullName().equals(fullName)) {
                return grade;
            }
        }
        return null;
    }

    private void saveAllGradesToFile() {
        File f = new File("C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\grades_database.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            for (Grade grade : gradesList) {
                pw.println(grade.getGradesAsString());
            }
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }




    @FXML
    private void add_grade_button(ActionEvent event) {

    }

    @FXML
    private void watch_profile_button(ActionEvent event) throws IOException {
        App.setRoot("teacher_watch_profile_scene");
    }

    @FXML
    private void raiting_button(ActionEvent event) throws IOException {
        App.setRoot("teacher_raiting_scene");
    }

    @FXML
    private void attendance_button(ActionEvent event) throws IOException {
        App.setRoot("teacher_attendance_scene");
    }

    @FXML
    private void schedule_button(ActionEvent event) throws IOException {
        App.setRoot("teacher_schedule_scene");
    }


    @FXML
    private void sign_out_button(ActionEvent event) throws IOException {
        App.setSize(600, 435);
        App.setRoot("loginscene");
    }
}
