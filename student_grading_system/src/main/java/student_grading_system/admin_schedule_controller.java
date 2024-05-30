package student_grading_system;

import java.io.File;
import java.io.FileWriter;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class admin_schedule_controller implements Initializable {
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

    @FXML
    private TextField class_input;

    @FXML
    private ComboBox<String> day_input;

    @FXML
    private ComboBox<String> semestr_input;

    @FXML
    private ComboBox<String> time_input;

    @FXML
    private ComboBox<String> year_input;

    private Map<String, Map<String, Map<String, Map<String, String>>>> scheduleData = new HashMap<>();
    private ObservableList<Schedule> scheduleList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        year_filter_input.getItems().addAll("Year-1", "Year-2", "Year-3", "Year-4");
        semestr_filter_input.getItems().addAll("Semestr-1", "Semestr-2");

        day_input.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
        semestr_input.getItems().addAll("Semestr-1", "Semestr-2");
        time_input.getItems().addAll("8.30-9.50", "10.00-11.20", "11.30-12.50", "13.00-14.20", "14.30-15.50", "16.00-17.20");
        year_input.getItems().addAll("Year-1", "Year-2", "Year-3", "Year-4");

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
    void submit_button(ActionEvent event) {
        String year = year_input.getValue();
        String semestr = semestr_input.getValue();
        String day = day_input.getValue();
        String time = time_input.getValue();
        String className = class_input.getText().trim();

        if (year == null || semestr == null || day == null || time == null || className.isEmpty()) {
            System.out.println("All fields must be filled");
            return;
        }

        scheduleData
            .computeIfAbsent(year, k -> new HashMap<>())
            .computeIfAbsent(semestr, k -> new HashMap<>())
            .computeIfAbsent(day, k -> new HashMap<>())
            .put(time, className);

        saveScheduleData();
        filterSchedule(); // Update the table to reflect new data
    }

    @FXML
    void delete_button(ActionEvent event) {
        Schedule selectedSchedule = schedule_table.getSelectionModel().getSelectedItem();
        if (selectedSchedule != null) {
            String year = year_filter_input.getValue();
            String semestr = semestr_filter_input.getValue();
            if (year != null && semestr != null) {
                scheduleData
                    .getOrDefault(year, new HashMap<>())
                    .getOrDefault(semestr, new HashMap<>())
                    .forEach((day, daySchedule) -> daySchedule.remove(selectedSchedule.getTime()));

                saveScheduleData();
                filterSchedule(); // Update the table to reflect deleted data
            }
        }
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

    private void saveScheduleData() {
        List<String> sortedData = new ArrayList<>();
        scheduleData.forEach((year, semestrs) -> 
            semestrs.forEach((semestr, days) -> 
                days.forEach((day, daySchedule) -> 
                    daySchedule.forEach((time, className) -> 
                        sortedData.add(String.join(" | ", year, semestr, day, time, className))
                    )
                )
            )
        );

        // Sort the collected data before writing to the file
        sortedData.sort(Comparator.comparing(data -> {
            String[] parts = data.split(" \\| ");
            return parts[3]; // Compare by the time field
        }));

        try (FileWriter writer = new FileWriter("C:\\Users\\user\\Desktop\\student_grading_system\\src\\main\\java\\student_grading_system\\schedule_database.txt")) {
            for (String entry : sortedData) {
                writer.write(entry + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void add_teachers_button(ActionEvent event) throws IOException {
        App.setRoot("admin_add_teachers_scene");
    }

    @FXML
    void add_students_button(ActionEvent event) throws IOException {
        App.setRoot("admin_add_students_scene");
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
    void users_button(ActionEvent event) throws IOException {
        App.setRoot("admin_users_scene");
    }

    @FXML
    void schedule_button(ActionEvent event) {
        // Already on the schedule scene
    }

    @FXML
    void sign_out_button(ActionEvent event) throws IOException {
        App.setSize(600, 435);
        App.setRoot("loginscene");
    }
}











