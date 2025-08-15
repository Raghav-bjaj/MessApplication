package org.messplacement.messsecond.DTO;

// This class is a simple container for our query result.
public class StudentDue {
    private String reg;
    private int totalDue;

    // The JPA query will use this constructor to create instances.
    public StudentDue(String reg, long totalDue) {
        this.reg = reg;
        // The SUM function in SQL returns a Long, so we cast it to int.
        this.totalDue = (int) totalDue;
    }

    // Standard getters and setters
    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public int getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(int totalDue) {
        this.totalDue = totalDue;
    }
}