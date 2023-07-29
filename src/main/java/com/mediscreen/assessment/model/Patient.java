package com.mediscreen.assessment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    private Integer id;

    private String family;

    private String given;

    private String sex;

    private LocalDate dob;

    private String address;

    private String phone;

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", familyName='" + family + '\'' +
                ", givenName=" + given + '\'' +
                ", sex=" + sex + '\'' +
                ", dateOfBirth=" + dob + '\'' +
                ", address=" + address  + '\'' +
                ", phone=" + phone +
                "}";
    }

}