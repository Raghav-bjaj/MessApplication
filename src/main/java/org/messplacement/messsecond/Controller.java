package org.messplacement.messsecond;

import org.messplacement.messsecond.Entities.Student;
import org.messplacement.messsecond.Service.MessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException; // Import for handling unique constraint violations
import org.springframework.http.HttpStatus; // Import for HTTP status codes
import org.springframework.http.ResponseEntity; // Import for returning custom HTTP responses
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://mess-application-front-end-angular.vercel.app"})
public class Controller {

    @Autowired
    MessService messService;

    @GetMapping("/home")
    public String getHomePage(){
        return messService.getHomePage();
    }

    @GetMapping("/getStudents")
    public List<Student> getStudents(){
        return messService.getStudents();
    }

    @GetMapping("/students/{Reg}")
    public List<Student> getStudent(@PathVariable String  Reg){
        return messService.getStudent(Reg);
    }

    @GetMapping("studentTotal/{reg}")
    public int getStudentTotal(@PathVariable String reg){
        int total =  messService.getStudentTotal(reg);
        return total;
    }

    // Updated addStudent method to handle DataIntegrityViolationException
    @PostMapping("/students")
    public ResponseEntity<String> addStudent(@RequestBody List<Student> students){ // Changed parameter name to 'students' for clarity
        try {
            messService.addStudent(students);
            // Return 201 Created status for successful resource creation
            return new ResponseEntity<>("Student(s) added successfully", HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            // This exception is thrown by Spring Data JPA/Hibernate when a database unique
            // constraint is violated (e.g., trying to insert a duplicate reg/date combination).
            System.err.println("Attempted to add duplicate student entry: " + e.getMessage());
            // Return 409 Conflict status for a business logic conflict (duplicate resource)
            return new ResponseEntity<>("Error: A student entry with this registration number and date already exists.", HttpStatus.CONFLICT);
        } catch (Exception e) {
            // Catch any other unexpected exceptions that might occur during the process.
            // This ensures robustness for unforeseen issues.
            System.err.println("Error adding student(s): " + e.getMessage());
            // Return 500 Internal Server Error for generic server-side issues
            return new ResponseEntity<>("An unexpected error occurred while adding student(s): " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/students")
    public String updateStudent(@RequestBody Student student){
        messService.updateStudent(student);
        return "Student updated";
    }

    @DeleteMapping("/students/{reg}/{date}")
    public String deleteStudent(@PathVariable String reg, @PathVariable String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        messService.deleteStudent(reg, parsedDate);
        return "Student deleted";
    }
}
