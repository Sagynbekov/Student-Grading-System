package student_grading_system;

public class Student {
    int id;
    String studentId, name, lastName, birthDate, gender, year, semestr;

    public Student(String studentId, String name, String lastName, String birthDate, String gender, String year, String semestr) {
        this.studentId = studentId;
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.year = year;
        this.semestr = semestr;
    }

    public String getStudentId() {return studentId;}
    public String getName() {return name;}
    public String getLastName() {return lastName;}
    public String getBirthDate() {return birthDate;}
    public String getGender() {return gender;}
    public String getYear() {return year;}
    public String getSemestr() {return semestr;}

    public void setStudentId(String studentId) {this.studentId = studentId;}
    public void setName(String name) {this.name = name;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setBirthDate(String birthDate) {this.birthDate = birthDate;}
    public void setGender(String gender) {this.gender = gender;}
    public void setYear(String year) {this.year = year;}
    public void setSemestr(String semestr) {this.semestr = semestr;}
}
