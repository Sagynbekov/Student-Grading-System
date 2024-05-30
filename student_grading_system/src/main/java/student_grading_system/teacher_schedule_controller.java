package student_grading_system;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class teacher_schedule_controller implements Initializable {
    @FXML
    private ComboBox<String> year_filter_input;

    @FXML
    private ComboBox<String> semestr_filter_input;

    @FXML
    private TableView<Schedule> schedule_table;

    @FXML
    private TableColumn<Schedule, String> time_column;

    @FXML
    private TableColumn<Schedule, String> monday_column;

    @FXML
    private TableColumn<Schedule, String> tuesday_column;
    
    @FXML
    private TableColumn<Schedule, String> wednesday_column;

    @FXML
    private TableColumn<Schedule, String> thursday_column;    
    
    @FXML
    private TableColumn<Schedule, String> friday_column;

    @FXML
    private TableColumn<Schedule, String> saturday_column;

    private Map<String, Map<String, Map<String, Map<String, String>>>> scheduleData = new HashMap<>();
    private ObservableList<Schedule> scheduleList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        year_filter_input.getItems().addAll("Year-1", "Year-2", "Year-3", "Year-4");
        semestr_filter_input.getItems().addAll("Semestr-1", "Semestr-2");

        time_column.setCellValueFactory(new PropertyValueFactory<>("time"));
        monday_column.setCellValueFactory(new PropertyValueFactory<>("monday"));
        tuesday_column.setCellValueFactory(new PropertyValueFactory<>("tuesday"));
        wednesday_column.setCellValueFactory(new PropertyValueFactory<>("wednesday"));
        thursday_column.setCellValueFactory(new PropertyValueFactory<>("thursday"));
        friday_column.setCellValueFactory(new PropertyValueFactory<>("friday"));
        saturday_column.setCellValueFactory(new PropertyValueFactory<>("saturday"));

        schedule_table.setItems(scheduleList);

        year_filter_input.setOnAction(this::filterSchedule);
        semestr_filter_input.setOnAction(this::filterSchedule);

        loadScheduleData();
        filterSchedule(); // Apply filter to load initial data into the table
    }


    @FXML
    void filterSchedule(ActionEvent event) {
        filterSchedule();
    }

    private void filterSchedule() {
        String year = year_filter_input.getValue();
        String semestr = semestr_filter_input.getValue();

        scheduleList.clear();
        if (year != null && semestr != null) {
            Map<String, Map<String, String>> semestrSchedule = scheduleData.getOrDefault(year, new HashMap<>()).getOrDefault(semestr, new HashMap<>());

            semestrSchedule.forEach((day, daySchedule) -> daySchedule.forEach((time, className) -> {
                Schedule schedule = scheduleList.stream()
                        .filter(s -> s.getTime().equals(time))
                        .findFirst()
                        .orElseGet(() -> {
                            Schedule newSchedule = new Schedule(time);
                            scheduleList.add(newSchedule);
                            return newSchedule;
                        });

                switch (day) {
                    case "Monday": schedule.setMonday(className); break;
                    case "Tuesday": schedule.setTuesday(className); break;
                    case "Wednesday": schedule.setWednesday(className); break;
                    case "Thursday": schedule.setThursday(className); break;
                    case "Friday": schedule.setFriday(className); break;
                    case "Saturday": schedule.setSaturday(className); break;
                }
            }));

            // Sort the scheduleList by time
            scheduleList.sort(Comparator.comparing(Schedule::getTime));
        }

        schedule_table.setItems(scheduleList);
    }

    private void loadScheduleData() {
        File file = new File("C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\schedule_database.txt");
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(" \\| ");
                    if (parts.length == 5) {
                        String year = parts[0];
                        String semestr = parts[1];
                        String day = parts[2];
                        String time = parts[3];
                        String className = parts[4];
                        scheduleData
                            .computeIfAbsent(year, k -> new HashMap<>())
                            .computeIfAbsent(semestr, k -> new HashMap<>())
                            .computeIfAbsent(day, k -> new HashMap<>())
                            .put(time, className);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        filterSchedule();
    }

    

    @FXML
    void add_grade_button(ActionEvent event) throws IOException {
        App.setRoot("teacher_add_grade_scene");
    }

    @FXML
    void attendance_button(ActionEvent event) throws IOException {
        App.setRoot("teacher_attendance_scene");
    }

    @FXML
    void raiting_button(ActionEvent event) throws IOException {
        App.setRoot("teacher_raiting_scene");
    }

    @FXML
    void schedule_button(ActionEvent event) {

    }
    
    @FXML
    void watch_profile_button(ActionEvent event) throws IOException {
        App.setRoot("teacher_watch_profile_scene");
    }

    @FXML
    void sign_out_button(ActionEvent event) throws IOException {
        App.setSize(600, 435);
        App.setRoot("loginscene");
    }


}