package com.mediscreen.assessment.service;

import com.mediscreen.assessment.dto.AssessmentDTO;
import com.mediscreen.assessment.model.History;
import com.mediscreen.assessment.model.Patient;
import com.mediscreen.assessment.webclient.HistoryWebClient;
import com.mediscreen.assessment.webclient.PatientWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class that provides methods to calculate the risk assessment for a patient.
 */
@Service
public class AssessmentService {

    @Autowired
    HistoryWebClient historyWebClient;

    // LIST OF TRIGGERS
    // in french and english
    final static List<String> TRIGGER_TERMS = Arrays.asList(
            "hemoglobine a1c",
            "hemoglobin a1c",
            "microalbumine",
            "microalbumin",
            "taille",
            "height",
            "poids",
            "weight",
            "fumeur",
            "smoker",
            "anormal",
            "abnormal",
            "cholesterol",
            "vertige",
            "dizziness",
            "rechute",
            "relapse",
            "reaction",
            "anticorps",
            "antibodies"
    );

    /**
     * Calculates the risk assessment for the given patient.
     *
     * @param patient The patient for whom the risk assessment is to be calculated.
     * @return An AssessmentDTO containing the patient's risk assessment information.
     */
    public AssessmentDTO getAssessment(Patient patient) {
        List<History> histories = historyWebClient.findById(patient.getId()).block();
        String sex = patient.getSex();

        int age = calculateAge(patient.getDob());
        int nbrOfTriggerTerms = calculateTriggerTerms(histories);
        String levelOfRisk = calculateLevelOfRisk(sex, age, nbrOfTriggerTerms);

        AssessmentDTO assessment = new AssessmentDTO(
                patient.getId(),
                patient.getFamily(),
                patient.getGiven(),
                age,
                levelOfRisk
        );

        return assessment;
    }

    /**
     * Calculates the age of the patient based on the date of birth.
     *
     * @param dob The date of birth of the patient.
     * @return The age of the patient in years.
     */
    public Integer calculateAge(LocalDate dob) {
        LocalDate currentDate = LocalDate.now();
        return Period.between(dob, currentDate).getYears();
    }

    /**
     * Removes accents and converts a string to lowercase.
     *
     * @param input The input string.
     * @return The input string with accents removed and converted to lowercase.
     */
    public String toLowerCaseWithoutAccents(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
    }

    /**
     * Calculates the number of trigger terms present in the patient's history notes.
     *
     * @param histories The list of history objects containing patient's historical medical notes.
     * @return The count of trigger terms found in the patient's history notes.
     */
    public int calculateTriggerTerms(List<History> histories) {
        // Get the list of notes for each History object
        List<String> notes = histories.stream()
                .map(History::getNotes)
                .map(this::toLowerCaseWithoutAccents)
                .collect(Collectors.toList());

        int count = 0;

        //Iterate on each term
        for (String term : TRIGGER_TERMS) {
            //Iterate on each note
            for (String note : notes) {
                //Add one if the note contain the term
                if (note.contains(term)) {
                    count += 1;
                }
            }
        }

        return count;
    }

    /**
     * Calculates the level of risk based on the patient's sex, age, and the number of trigger terms.
     *
     * @param sex The sex of the patient (M for male, F for female).
     * @param age The age of the patient in years.
     * @param NbrOfTriggerTerms The count of trigger terms found in the patient's history notes.
     * @return A String representing the level of risk for the patient.
     */
    public String calculateLevelOfRisk(String sex, int age, int NbrOfTriggerTerms) {

        // If the patient is more than 30yo
        if (age >= 30) {
            if (NbrOfTriggerTerms <= 1) {
                return "None";
            } else if (NbrOfTriggerTerms <= 5) {
                return "Borderline";
            } else if (NbrOfTriggerTerms <= 7) {
                return "In Danger";
            } else {
                return "Early onset";
            }

            // If the patient is less than 30yo and a man
        } else if (age < 30 && sex.equals("M")) {
            if (NbrOfTriggerTerms <= 2) {
                return "None";
            } else if (NbrOfTriggerTerms <= 4) {
                return "In Danger";
            } else {
                return "Early onset";
            }

            // If the patient is less than 30yo and a woman
        } else if (age < 30 && sex.equals("F")) {
            if (NbrOfTriggerTerms <= 3) {
                return "None";
            } else if (NbrOfTriggerTerms <= 6) {
                return "In Danger";
            } else {
                return "Early onset";
            }
        } else {
            // If the evaluation didn't work
            return "Patient not evaluated";
        }
    }
}
