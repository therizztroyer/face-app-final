package com.example.smartattendance.Model;

public class UploadFile {

    private String name;
    private String url;
    private String data;

    private String student;
    private String date;
    private String time;
    private int attended;

    public UploadFile() {
    }

    public UploadFile(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public UploadFile(String student, String date, String time,int attendance){
        this.student = student;
        this.date = date;
        this.time = time;
       this.attended=attendance;
    }

    public String getStudent() {
        return student;
    }
    public  int getAttended(){return attended;}

    public void setStudent(String student) {
        this.student = student;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getData() {
        return data;
    }


    public String getData(String key) {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
