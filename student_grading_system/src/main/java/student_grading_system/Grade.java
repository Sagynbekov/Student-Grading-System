package student_grading_system;

import java.util.*;

public class Grade {
    private String fullName;
    private Map<String, Map<String, List<Integer>>> classGrades;

    public Grade(String fullName) {
        this.fullName = fullName;
        this.classGrades = new HashMap<>();
    }

    public String getFullName() {
        return fullName;
    }

    public Map<String, Map<String, List<Integer>>> getClassGrades() {
        return classGrades;
    }

    public void addGrade(String className, String type, int grade) {
        classGrades.putIfAbsent(className, new HashMap<>());
        classGrades.get(className).putIfAbsent(type, new ArrayList<>());
        classGrades.get(className).get(type).add(grade);
    }

    public String getQuizes(String className) {
        return getGradesAsString(className, "Quiz");
    }

    public String getMidterms(String className) {
        return getGradesAsString(className, "Midterm");
    }

    public String getFinals(String className) {
        return getGradesAsString(className, "Final");
    }

    private String getGradesAsString(String className, String type) {
        if (classGrades.containsKey(className) && classGrades.get(className).containsKey(type)) {
            List<Integer> grades = classGrades.get(className).get(type);
            // ? если пустой, вернет пустую строку, если нет то вернет все оценки без [] ([\\[\\]]) - удаляет все []
            return grades.isEmpty() ? "" : grades.toString().replaceAll("[\\[\\]]", "");
        }
        return "";
    }

    public String getGradesAsString() {
        StringBuilder sb = new StringBuilder(fullName);
        for (String className : classGrades.keySet()) {
            sb.append(",").append(className).append(":");
            Map<String, List<Integer>> types = classGrades.get(className);
            for (String type : types.keySet()) {
                sb.append(type).append("-");
                sb.append(types.get(type).toString().replaceAll("[\\[\\]]", "").replaceAll(", ", " "));
                sb.append(";");
            }
        }
        return sb.toString();
    }


    public double getAverageGrade(String className, String type) {
        if (classGrades.containsKey(className) && classGrades.get(className).containsKey(type)) {
            List<Integer> grades = classGrades.get(className).get(type);
            return grades.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        }
        return 0.0;
    }
}

