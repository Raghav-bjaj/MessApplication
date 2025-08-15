package org.messplacement.messsecond.Dao;
import org.messplacement.messsecond.DTO.StudentDue;
import org.messplacement.messsecond.Entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
// ... other imports
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MessDao extends JpaRepository<Student, String> {

    // It finds a specific record by its unique combination of reg and date.
    // Using Optional<Student> is a safe way to handle cases where no record is found.
    Optional<Student> findByRegAndDate(String reg, LocalDate date);

    // --- THIS IS THE NEW METHOD ---
    // Spring Data JPA will automatically create a query that finds all
    // Student entities where the 'date' property matches the provided date.
    List<Student> findByDate(LocalDate date);


    // It selects the registration number and the SUM of the 'total' column.
    // It groups the results by registration number.
    // It filters the records to be between the provided startDate and endDate.
    @Query("SELECT new org.messplacement.messsecond.DTO.StudentDue(s.reg, SUM(s.total)) FROM Student s WHERE s.date BETWEEN :startDate AND :endDate GROUP BY s.reg")
    List<StudentDue> findTotalDuesByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    // Your existing methods
    @Query("""
            SELECT SUM(s.total) FROM Student s WHERE s.reg = :studentId""")
    int getStudentTotal(@Param("studentId") String studentId);

    @Query("SELECT s FROM Student s WHERE s.reg = :reg")
    List<Student> findByReg(@Param("reg") String reg);

    @Modifying
    @Transactional
    @Query("DELETE FROM Student s WHERE s.reg = :reg AND s.date = :date")
    void deleteByRegAndDate(@Param("reg") String reg, @Param("date") LocalDate date);
}