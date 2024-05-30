package student_grading_system;

public class Attendance {
    private String fullName;
    private int amount;
    private String date;
    private String className;

    public Attendance(String fullName, int amount, String date, String className) {
        this.fullName = fullName;
        this.amount = amount;
        this.date = date;
        this.className = className;
    }

    public String getFullName() {return fullName;}
    public int getAmount() {return amount;}
    public String getDate() {return date;}
    public String getClassName() {return className;}

    public void setFullName(String fullName) {this.fullName = fullName;}
    public void setAmount(int amount) {this.amount = amount;}
    public void setDate(String date) {this.date = date;}
    public void setClassName(String className) {this.className = className;}

    
    public void addDate(String newDate) {
        this.date += ", " + newDate;
        this.amount += 1;
    }
}



















// import javafx.beans.property.SimpleIntegerProperty;
// import javafx.beans.property.SimpleStringProperty;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.StringJoiner;

// public class Attendance {

//     private final SimpleStringProperty fullName;
//     private final SimpleIntegerProperty amount;
//     private final SimpleStringProperty date;
//     private final String className;
//     private final List<String> dates;

//     public Attendance(String fullName, int amount, String date, String className) {
//         this.fullName = new SimpleStringProperty(fullName);
//         this.amount = new SimpleIntegerProperty(amount);
//         this.date = new SimpleStringProperty(date);
//         this.className = className;
//         this.dates = new ArrayList<>();
//         this.dates.add(date);
//         updateDateString();
//     }

//     public String getFullName() {
//         return fullName.get();
//     }

//     public int getAmount() {
//         return amount.get();
//     }

//     public String getDate() {
//         return date.get();
//     }

//     public String getClassName() {
//         return className;
//     }

//     public void addDate(String date) {
//         this.dates.add(date);
//         this.amount.set(this.dates.size());
//         updateDateString();
//     }

//     private void updateDateString() {
//         StringJoiner joiner = new StringJoiner(", ");
//         for (String date : dates) {
//             joiner.add(date);
//         }
//         this.date.set(joiner.toString());
//     }
// }
