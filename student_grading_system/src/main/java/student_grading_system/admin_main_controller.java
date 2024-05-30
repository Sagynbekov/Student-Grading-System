package student_grading_system;

import java.io.IOException;
import javafx.fxml.FXML;

public class admin_main_controller {

    

    @FXML
    private void add_teachers_button() throws IOException {
        App.setRoot("admin_add_teachers_scene");
    }

    @FXML
    private void add_students_button() throws IOException {
        App.setRoot("admin_add_students_scene");
    }

    @FXML
    private void rating_button() throws IOException {
        App.setRoot("admin_raiting_scene");
    }

    @FXML
    private void watch_profile_button() throws IOException {
        App.setRoot("admin_watch_profile_scene");
    }

    @FXML
    private void users_button() throws IOException {
        App.setRoot("admin_users_scene");
    }

    @FXML
    private void schedule_button() throws IOException {
        App.setRoot("admin_schedule_scene");
    }
    
    @FXML
    private void sign_out_button() throws IOException {
        App.setSize(600, 435);
        App.setRoot("loginscene");
    }
}
