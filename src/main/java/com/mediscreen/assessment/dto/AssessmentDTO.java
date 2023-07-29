package com.mediscreen.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentDTO {
    private Integer patId;

    private String family;

    private String given;

    private Integer age;

    private String diabetesAssessment;

    @Override
    public String toString() {
        return "{ Patient : " +
                given + " " + family + " " +
                "(age " + age +
                ") diabetes assessment is: " + diabetesAssessment +
                "}";
    }

}
