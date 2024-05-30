package student_grading_system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

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

public class admin_add_students_controller implements Initializable {

    @FXML
    private TableView<Student> students_table;

    @FXML
    private TableColumn<Student, Integer> student_id_column;

    @FXML
    private TableColumn<Student, String> name_column;

    @FXML
    private TableColumn<Student, String> last_name_column;

    @FXML
    private TableColumn<Student, DatePicker> birth_date_column;

    @FXML
    private TableColumn<Student, ComboBox> gender_column;
    
    @FXML
    private TableColumn<Student, ComboBox> year_column;

    @FXML
    private TableColumn<Student, ComboBox> semestr_column;



    @FXML
    private DatePicker birth_date_input;

    @FXML
    private TextField last_name_input;
    
    @FXML
    private TextField name_input;
    
    @FXML
    private ComboBox<String> gender_input;
    
    @FXML
    private ComboBox<String> semestr_input;
    
    @FXML
    private ComboBox<String> year_input;
    
    
    
    public void initialize(URL url, ResourceBundle resourceBundle) {
        student_id_column.setCellValueFactory(new PropertyValueFactory<>("StudentId"));
        name_column.setCellValueFactory(new PropertyValueFactory<>("Name"));
        last_name_column.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        birth_date_column.setCellValueFactory(new PropertyValueFactory<>("BirthDate"));
        gender_column.setCellValueFactory(new PropertyValueFactory<>("Gender"));
        year_column.setCellValueFactory(new PropertyValueFactory<>("Year"));
        semestr_column.setCellValueFactory(new PropertyValueFactory<>("Semestr"));
        
        loadData();
        students_table.setItems(students_list);
        students_table.setFixedCellSize(25);

        year_input.getItems().addAll("1-year", "2-year", "3-year", "4-year");
        gender_input.getItems().addAll("Male", "Female");
        semestr_input.getItems().addAll("1-semestr", "2-semestr");
    }

    public ObservableList<Student> students_list = FXCollections.observableArrayList();

    private void loadData() {
        File f = new File("C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\st_database.txt");
        try {
            Scanner scan = new Scanner(f);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    students_list.add(new Student(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]));
                }
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.out.println();
            System.out.println("File ne naiden");
            System.out.println();
        }
    }
    
    
    int id = 0;
    @FXML
    void submit_button(ActionEvent event) {
        // Add to the database
        File f = new File("C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\st_database.txt"); 
        File f_login = new File("C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\login_database.txt");

        try {
            PrintWriter pw = new PrintWriter(new FileWriter(f, true));
            Scanner scan = new Scanner(f);

            PrintWriter pw_login = new PrintWriter(new FileWriter(f_login, true));
            Scanner scan_login = new Scanner(f_login);

            if (!scan.hasNextLine()) {
                id = 1;
            }
            else {
                String lastLine = scan.nextLine();
                while(scan.hasNextLine()) {
                    lastLine = scan.nextLine();
                }
                String[] parts = lastLine.split(",");
                id = Integer.parseInt(parts[0]) + 1;
            }

            String input = id + "," + name_input.getText() + "," + last_name_input.getText() + "," + birth_date_input.getValue() + "," + gender_input.getValue() + "," + year_input.getValue() + "," + semestr_input.getValue();
            pw.println(input);
            pw.close();
            scan.close();

            String input_login = name_input.getText() + "," + last_name_input.getText() + id + "s";
            pw_login.println(input_login);
            pw_login.close();
            scan_login.close();
        
        } catch (IOException e) {
            System.out.println();
            System.out.println("File ne naiden");
            System.out.println();
        }  

        Student student = new Student(String.valueOf(id), name_input.getText(), last_name_input.getText(), String.valueOf(birth_date_input.getValue()), String.valueOf(gender_input.getValue()), String.valueOf(year_input.getValue()), String.valueOf(semestr_input.getValue()));
        ObservableList<Student> students = students_table.getItems();
        students.add(student);
        students_table.setItems(students);
    }
    
    @FXML
    void delete_smb_button(ActionEvent event) {
        int selectetId = students_table.getSelectionModel().getSelectedIndex();

        // delete from file
        String filePath = "C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\st_database.txt";
        String f_login = "C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\login_database.txt";
        try {
            Student selecStudent = students_table.getItems().get(selectetId);
            String line_remove = selecStudent.getStudentId() + "," + selecStudent.getName() + "," + selecStudent.getLastName() + "," + selecStudent.getBirthDate() + "," + selecStudent.getGender() + "," + selecStudent.getYear() + "," + selecStudent.getSemestr();

            String line_remove_login = selecStudent.getName() + "," + selecStudent.getLastName() + selecStudent.getStudentId() + "s";

            File inp_file = new File(filePath);
            Scanner scan = new Scanner(inp_file);

            File tempFile = new File("tempfile.txt");
            PrintWriter pw = new PrintWriter(tempFile);


            // remove from st_database.txt
            while (scan.hasNextLine()) {
                String currentLine = scan.nextLine();

                if (currentLine.equals(line_remove)) {
                    continue;
                }
                pw.println(currentLine);
            }
            pw.close();
            scan.close();

            if (!inp_file.delete()) {
                System.out.println("Ne udalos udalit ishodnui file");
                // return;
            }

            if (!tempFile.renameTo(inp_file)) {
                System.out.println("Ne udalos pereimenovat vremeniu file");
            }
            // delete from display
            students_table.getItems().remove(selectetId);
            

            
            // remove from login_database
            File inp_file_login = new File(f_login);
            Scanner scan_login = new Scanner(inp_file_login);
            ArrayList<String> arr = new ArrayList<>();
            while (scan_login.hasNextLine()) {
                String line = scan_login.nextLine();
                if (!line.equals(line_remove_login)) {
                    arr.add(line);
                }
            }
            scan_login.close();
            if (!inp_file_login.delete()) System.out.println("ne ydalilsya file login");



            File login_database = new File("C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\login_database.txt");
            PrintWriter pw_login = new PrintWriter(login_database);
            for (String i : arr) {
                pw_login.println(i);
            }
            pw_login.close();



        } catch (IOException e) {
            System.out.println();
            System.out.println("file ne naiden");
            System.out.println();
        }

    }

    @FXML
    private void add_teachers_button(ActionEvent event) throws IOException {
        App.setRoot("admin_add_teachers_scene");
    }
    
    @FXML
    private void add_students_button(ActionEvent event) throws IOException {
    }
    
    @FXML
    private void rating_button(ActionEvent event) throws IOException {
        App.setRoot("admin_raiting_scene");
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
