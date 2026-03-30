package com.example.cse489_2022_1_60_265;

public class StudentAttendance {

    String name;
    boolean status;
    String remarks;


    public StudentAttendance(String name, boolean status, String remarks){
        this.name = name;
        this.status = status;
        this.remarks = remarks;
    }

    public String toString(){
        return name+";"+status+";"+remarks;
    }
}
