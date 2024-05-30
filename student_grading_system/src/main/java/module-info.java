module student_grading_system {
    requires javafx.controls;
    requires javafx.fxml;

    opens student_grading_system to javafx.fxml;
    exports student_grading_system;
}
