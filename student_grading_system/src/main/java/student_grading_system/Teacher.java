package student_grading_system;


public class Teacher {
    String name, lastName, teacherId, classs, getJob;
    
    public Teacher(String teacherId, String name, String lastName, String classs, String getJob) {
        this.teacherId = teacherId;
        this.name = name;
        this.lastName = lastName;
        this.classs = classs;
        this.getJob = getJob;
    }

    public String getTeacherId() {return this.teacherId; }
    public String getName() {return this.name; }
    public String getLastName() {return this.lastName; }
    public String getClasss() {return this.classs; }
    public String getGetJob() {return this.getJob; }

    public void setTeacherId(String new_teacherId) {this.teacherId = new_teacherId;}
    public void setName(String new_name) {this.name = new_name;}
    public void setLastName(String new_lastName) {this.lastName = new_lastName;}
    public void setClasss(String new_classs) {this.classs = new_classs;}
    public void setGetJob(String new_getJob) {this.getJob = new_getJob;}
}
