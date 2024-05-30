package student_grading_system;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private PasswordField password_input;

    @FXML
    private TextField username_input;

    @FXML
    private void loginButton() throws IOException {
        String check = username_input.getText() + "," + password_input.getText();

        File f = new File("C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\login_database.txt");
        
        boolean userFound = false;

        try {
            Scanner scan = new Scanner(f);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (check.equals(line)) {
                    userFound = true;
                    String[] parts = check.split(",");
                    String[] status = parts[1].split("");
                    if (status[status.length-1].equals("t")) {
                        App.setSize(775, 615);
                        App.setRoot("teacher_add_grade_scene");
                    }
                    if (status[status.length-1].equals("s")) {
                        App.setSize(775, 615);
                        App.setRoot("student_grades_scene");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println();
            System.out.println("File ne naiden: "+ e.getMessage());
            System.out.println();
        }

        if (check.equals("Admin,123")) {
            App.setSize(775, 615);
            App.setRoot("admin_main_scene");
        } else if (!userFound) {
            App.setRoot("wrong_login_scene");
        }
    }
}
