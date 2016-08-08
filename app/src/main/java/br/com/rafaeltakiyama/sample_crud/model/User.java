package br.com.rafaeltakiyama.sample_crud.model;

import java.io.Serializable;

/**
 * Created by rafaelakiyama on 07/08/16.
 */

public class User implements Serializable {

    private Integer id;
    private String firstName;
    private String lastName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompleteName () {
        return firstName + " " + lastName;
    }

    public String getFirstLetterFromUserName() {

        String letter = "a";

        if (firstName != null && !firstName.isEmpty()) {
            letter = String.valueOf(firstName.charAt(0));
        }

        return letter;
    }

    @Override
    public String toString() {
        return  "id : " + id + "\n"
                + "firstName : " + firstName + "\n"
                + "lastName : " + lastName;
    }
}
