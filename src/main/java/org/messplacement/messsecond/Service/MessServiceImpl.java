package org.messplacement.messsecond.Service;

import org.messplacement.messsecond.Dao.MessDao;
import org.messplacement.messsecond.Entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class MessServiceImpl implements MessService {

    //Calculate total (list of objects)
    public List<Student> calcTotal(List<Student> student) {
        int total = 0;

        for (Student value : student) {
            if (value.getBreakfast()) {
                total += 75;
            }
            if (value.getLunch()) {
                total += 100;
            }
            if (value.getDinner()) {
                total += 125;
            }
            value.setTotal(total);
            total = 0;
            if(value.getDate() == null){value.setDate(LocalDate.now());}

        }
        return student;
    }
// calculate total for single object
    public Student calcTotal(Student student ){
        int total = 0;
        if (student.getBreakfast()) {total += 75;}
        if (student.getLunch() ) {total += 100;}
        if (student.getDinner()) {total += 125;}
        student.setTotal(total);
        if(student.getDate() == null){student.setDate(LocalDate.now());}
        return student;
    }


    @Autowired
    private MessDao messDao;

     public MessServiceImpl() {

    }

    @Override
    public String getHomePage(){
        return "This is the home page, Service Layer.";
    }

    @Override
    public List<Student> getStudents(){
          return messDao.findAll();
    }

    @Override
    public List<Student> getStudent(String Reg) {

        return messDao.findByReg(Reg);
    }

    @Override
    public int getStudentTotal(String studentId) {
          return messDao.getStudentTotal(studentId);
    }


    @Override
    public String  addStudent(List<Student> student) {
        List<Student> obj = calcTotal(student);
        while (!messDao.findAll().isEmpty()) {
            if (!obj.isEmpty()) {
                messDao.save(obj.get(0));
                obj.remove(0);
            } else {
                break;
            }
        }
        return "Student added successfully";
    }

    @Override
    public String updateStudent(Student student) {
         LocalDate date = student.getDate();
         String reg = student.getreg();
         messDao.deleteByRegAndDate(reg,date);

          Student obj = calcTotal(student);
          messDao.save(obj);
        return " Updated Successfully";
    }

    @Override
    public String deleteStudent(String reg, LocalDate date) {

        messDao.deleteByRegAndDate(reg,date);
        return " Deleted Successfully";
    }


}
