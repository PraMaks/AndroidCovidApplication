package com.pravdin.tpfinalmobile.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Citizen implements Serializable{

    private String numAssurSoc;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private Date dateCreation;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'a' HH:mm z");
    private SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy.MM.dd");
    private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm z");

    public Citizen() { }

    public Citizen(String numAssurSoc, String firstName, String lastName, int age, String email, Date dateCreation) {
        this.numAssurSoc = numAssurSoc;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.dateCreation = dateCreation;
    }

    public String getDayFromDate(){
        return sdfDay.format(dateCreation);
    }

    public String getDayAndTimeFromDate(){
        return sdf.format(dateCreation);
    }

    @Override
    public String toString(){
        String dateString = getDayAndTimeFromDate();
        return "Le permis de " + firstName + " " + lastName + "(" + numAssurSoc +")" + " a été créé " + dateString + " à l'age de " + age + " ans";
    }
}
