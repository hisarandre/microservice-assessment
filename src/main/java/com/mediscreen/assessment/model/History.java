package com.mediscreen.assessment.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class History {

    private String id;

    private Integer patId;

    private String patient;

    private LocalDate creationDate;

    private String notes;

}