package student_grading_system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class admin_users_controller implements Initializable {

    @FXML
    private TableView<String[]> users_table;

    @FXML
    private TableColumn<String[], String> user_column;

    @FXML
    private TableColumn<String[], String> username_column;

    @FXML
    private TableColumn<String[], String> password_column;

    private ObservableList<String[]> users_list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user_column.setCellValueFactory(cellData -> {
            String[] row = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(row[0]);
        });
        username_column.setCellValueFactory(cellData -> {
            String[] row = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(row[1]);
        });
        password_column.setCellValueFactory(cellData -> {
            String[] row = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(row[2]);
        });

        loadData();
        users_table.setItems(users_list);
        users_table.setFixedCellSize(55);
    }

    private void loadData() {
        File file = new File("C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\login_database.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String username = parts[0];
                    String password = parts[1];

                    String status = password.substring(password.length() - 1);
                    String id = password.substring(password.length() - 2, password.length() - 1);
                    String lastName = password.substring(0, password.length() - 2);

                    String statusText = status.equals("t") ? "Teacher" : "Student";
                    String fullName =  "User ID: " + id + "\nFull Name: " + username + " " + lastName + "\nStatus: " + statusText;

                    String[] row = {fullName, username, password};
                    users_list.add(row);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    @FXML
    void add_students_button(ActionEvent event) throws IOException {
        App.setRoot("admin_add_students_scene");
    }

    @FXML
    void add_teachers_button(ActionEvent event) throws IOException {
        App.setRoot("admin_add_teachers_scene");
    }

    @FXML
    void rating_button(ActionEvent event) throws IOException {
        App.setRoot("admin_raiting_scene");
    }

    @FXML
    void watch_profile_button(ActionEvent event) throws IOException {
        App.setRoot("admin_watch_profile_scene");
    }

    @FXML
    void users_button(ActionEvent event) {
    }

    @FXML
    void schedule_button(ActionEvent event) throws IOException {
        App.setRoot("admin_schedule_scene");
    }

    @FXML
    void sign_out_button(ActionEvent event) throws IOException {
        App.setSize(600, 435);
        App.setRoot("loginscene");
    }
}





