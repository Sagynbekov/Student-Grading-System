package student_grading_system;

import java.io.IOException;
import javafx.fxml.FXML;

public class wrong_login_controller {

    @FXML
    private void try_again_button() throws IOException {
        App.setRoot("loginscene");
    }
}