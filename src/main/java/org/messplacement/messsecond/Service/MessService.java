package org.messplacement.messsecond.Service;

import org.messplacement.messsecond.DTO.StudentDue;
import org.messplacement.messsecond.Entities.Student;

import java.time.LocalDate;
import java.util.List;

public interface MessService {
     String getHomePage();

     List<Student> getStudents(LocalDate date);

     List<StudentDue> getTotalDues(LocalDate startDate, LocalDate endDate);



     List<Student> getStudent(String Reg);

     int getStudentTotal(String studentId);

     String  addStudent(List<Student>  student);

     String updateStudent(Student student);

     String deleteStudent(String reg, LocalDate date);
}

