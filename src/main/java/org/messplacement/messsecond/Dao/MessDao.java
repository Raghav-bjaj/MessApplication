package org.messplacement.messsecond.Dao;

import org.messplacement.messsecond.Entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;

@Repository
public interface MessDao extends JpaRepository<Student, String> {


    @Query("""
            SELECT SUM(s.total) FROM Student s WHERE s.reg = :studentId""")
    int getStudentTotal(@Param("studentId") String studentId);

    @Query("SELECT s FROM Student s WHERE s.reg = :reg")
    List<Student> findByReg(@Param("reg") String reg);

    //@Query("Select s FROM Student s WHERE s.email = :email AND s.password = :password")
    //NEW ENTITY BANANI PADEGI?????

    @Modifying
    @Transactional
    @Query("DELETE FROM Student s WHERE s.reg = :reg AND s.date = :date")
    void deleteByRegAndDate(@Param("reg") String reg, @Param("date") LocalDate date);
}

