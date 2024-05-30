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

