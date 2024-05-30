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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;



public class admin_add_teachers_controller implements Initializable{
    @FXML
    private TableView<Teacher> teachers_table;
    
    @FXML
    private TableColumn<Teacher, Integer> teacher_id_column;

    @FXML
    private TableColumn<Teacher, String> name_column;

    @FXML
    private TableColumn<Teacher, String> last_name_column;

    @FXML
    private TableColumn<Teacher, DatePicker> get_job_column;

    @FXML
    private TableColumn<Teacher, String> class_column;


    @FXML
    private TextField class_input;

    @FXML
    private TextField last_name_input;

    @FXML
    private TextField name_input;

    @FXML
    private DatePicker get_job_data_input;

    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        teacher_id_column.setCellValueFactory(new PropertyValueFactory<>("TeacherId"));
        name_column.setCellValueFactory(new PropertyValueFactory<>("Name"));
        last_name_column.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        class_column.setCellValueFactory(new PropertyValueFactory<>("Classs"));
        get_job_column.setCellValueFactory(new PropertyValueFactory<>("GetJob"));

        loadData();
        teachers_table.setItems(teacherList);
        teachers_table.setFixedCellSize(25);
    }

    public ObservableList<Teacher> teacherList = FXCollections.observableArrayList();

    private void loadData() {
        File f = new File("C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\tc_database.txt");
        try {
            Scanner scan = new Scanner(f);
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    teacherList.add(new Teacher(parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            }
            scan.close();
        } 
        catch (FileNotFoundException e) {
            System.out.println();
            System.out.println("file ne naiden");
            System.out.println();
        }
    }

    
    
    int id = 0;
    @FXML
    void submit_button(ActionEvent event) {
        // add to the database
        File f = new File("C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\tc_database.txt");
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
            
            String input = id + "," + name_input.getText() + "," + last_name_input.getText() + "," + class_input.getText() + "," + get_job_data_input.getValue();
            pw.println(input);
            
            String input_login = name_input.getText() + "," + last_name_input.getText() + id + "t";
            pw_login.println(input_login);

            pw_login.close();
            scan_login.close();

            pw.close();
            scan.close();
            
        } catch (IOException e) {
            System.out.println();
            System.out.println("file st_database or login_database ne naiden");
            System.out.println();
        }
        
        // add to the table
        Teacher teacher = new Teacher(String.valueOf(id), name_input.getText(), last_name_input.getText(), class_input.getText(), String.valueOf(get_job_data_input.getValue()));
        ObservableList<Teacher> teachers = teachers_table.getItems();
        teachers.add(teacher);
        teachers_table.setItems(teachers);
    }
    
    
    @FXML
    void delete_smb_button(ActionEvent event) {        
        // delere from file
        String filePath = "C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\tc_database.txt";
        String f_login = "C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\login_database.txt";
        int selectetId = teachers_table.getSelectionModel().getSelectedIndex();
        try {
            Teacher selectTeacher = teachers_table.getItems().get(selectetId);
            
            String line_remove = selectTeacher.getTeacherId() + "," + selectTeacher.getName() + "," + selectTeacher.getLastName() + "," + selectTeacher.getClasss() + "," + selectTeacher.getGetJob(); 
            String line_remove_login = selectTeacher.getName() + "," + selectTeacher.getLastName() + selectTeacher.getTeacherId() + "t";
            
            File inp_file = new File(filePath);
            Scanner scan = new Scanner(inp_file);
            
            File tempFile = new File("tempfile.txt");
            PrintWriter pw = new PrintWriter(tempFile);
            
            
            // remove from tc_database.txt
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
                System.out.println("Ne ydalos ydalit ishodnui (tc_database) file");
                // return;
            }
            if (!tempFile.renameTo(inp_file)) {
                System.out.println("ne udalos pereimenovat vremeniu (tc_database) file");
            }
            teachers_table.getItems().remove(selectetId);
            
            
            
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
            
            


            
        } catch(IOException e) {
            // System.out.println(e.toString());
            System.out.println();
            System.out.println("File ne naiden");
            System.out.println();
        }
    }

    
    
    @FXML
    private void add_teachers_button(ActionEvent event) throws IOException {
        
    }
    
    @FXML
    private void add_students_button(ActionEvent event) throws IOException {
        App.setRoot("admin_add_students_scene");
    }
    
    
    @FXML
    private void sign_out_button(ActionEvent event) throws IOException {
        App.setSize(600, 435);
        App.setRoot("loginscene");
    }
    
    @FXML
    private void rating_button(ActionEvent event) throws IOException {
        App.setRoot("admin_raiting_scene");
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
    private void watch_profile_button(ActionEvent event) throws IOException {
        App.setRoot("admin_watch_profile_scene");
    }
}
