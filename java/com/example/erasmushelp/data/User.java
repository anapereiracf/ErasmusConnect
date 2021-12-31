package com.example.erasmushelp.data;

import java.io.Serializable;

public class User implements Serializable {
    //personal data//
    private String image, name, age, homeCountry, motherTongue, homeUniversity;
    private String birthdate;

    //security///
    private String email;
    private String password;

    public User() {

    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String image, String name, String age, String homeCountry, String motherTongue, String homeUni, String birthDate, String email, String password) {
        this.image = image;
        this.name = name;
        this.age = age;
        this.homeCountry = homeCountry;
        this.motherTongue = motherTongue;
        this.homeUniversity = homeUni;
        this.birthdate = birthDate;
        this.email = email;
        this.password = password;
    }

    //////////////// GETS BEGIN ////////////////
    public String getImage() {
        return image;
    }

    public String getName() {
        if (name != null && !name.isEmpty())
            return name;
        else
            return "NA";
    }

    public String getEmail() {
        return email;
    }

    public String getAge() {
        String idade = "Age: ";
        if (age != null && !age.isEmpty())
            return age;
        else
            return "NA";
    }

    public String getBirthdate() {
        String bd = "Birthdate: ";
        if (birthdate != null && !birthdate.isEmpty())
            return birthdate;
        else
            return "DD-MM-YYYY";
    }

    public String getHomeCountry() {
        String home = "Home Country: ";
        if (homeCountry != null && homeCountry.length() != 0)
            return homeCountry;
        else
            return "NA";
    }

    public String getMotherTongue() {
        String nat = "Mother Tongue: ";
        if (motherTongue != null && !motherTongue.isEmpty())
            return motherTongue;
        else
            return "NA";
    }

    public String getHomeUniversity() {
        String uni = "Home University: ";
        if (homeUniversity != null && !homeUniversity.isEmpty())
            return homeUniversity;
        else
            return "NA";
    }

    public String getPassword() {
        return password;
    }

    //////////////// GETS END ////////////////
    //////////////// SETS BEGIN ////////////////
    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setHomeCountry(String homeCountry) {
        this.homeCountry = homeCountry;
    }

    public void setMotherTongue(String motherTongue) {
        this.motherTongue = motherTongue;
    }

    public void setHomeUniversity(String homeUniversity) {
        this.homeUniversity = homeUniversity;
    }
    //////////////// SETS END ////////////////
}
