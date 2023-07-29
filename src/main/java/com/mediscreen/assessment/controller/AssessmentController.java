package com.mediscreen.assessment.controller;

import com.mediscreen.assessment.dto.AssessmentDTO;
import com.mediscreen.assessment.model.Patient;
import com.mediscreen.assessment.service.AssessmentService;
import com.mediscreen.assessment.webclient.PatientWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class responsible for handling assessment-related HTTP requests.
 */
@RestController
public class AssessmentController {

    @Autowired
    PatientWebClient patientWebClient;

    @Autowired
    AssessmentService assessmentService;

    private static Logger logger = LoggerFactory.getLogger(AssessmentController.class);

    /**
     * Retrieves the risk assessment for a given patient based on their patient ID.
     *
     * @param patientId The ID of the patient for whom the risk assessment is requested.
     * @return the risk assessment information.
     */
    @GetMapping(value = "assess/risk/{patientId}")
    public AssessmentDTO getRisk(@PathVariable("patientId") Integer patientId){
        logger.info("Patient " + patientId + " assessment requested");

        Patient patient = patientWebClient.findById(patientId).block();
        AssessmentDTO assessment = assessmentService.getAssessment(patient);

        return assessment;
    }

    /**
     * Retrieves the risk assessment for a patient based on their patient ID provided in the request body.
     *
     * @param patId The ID of the patient for whom the risk assessment is requested.
     * @return A ResponseEntity containing the risk assessment information as a String.
     */
    @PostMapping(value="/assess/id", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> getAssessmentById(Integer patId)  {
        logger.info("Patient " + patId + " assessment requested");

        Patient patient = patientWebClient.findById(patId).block();
        AssessmentDTO assessment = assessmentService.getAssessment(patient);

        return new ResponseEntity<>(assessment.toString(), HttpStatus.OK);
    }

    /**
     * Retrieves the risk assessment for a patient based on their family name and given name provided in the request body.
     *
     * @param family The family name of the patient.
     * @param given The given name of the patient.
     * @return A ResponseEntity containing the risk assessment information as a String.
     */
    @PostMapping(value="/assess/name", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> getAssessmentByName(String family, String given)  {
        logger.info("Patient " + family + " " + given + " assessment requested");

        Patient patient = patientWebClient.findByName(family, given).block();
        AssessmentDTO assessment = assessmentService.getAssessment(patient);

        return new ResponseEntity<>(assessment.toString(), HttpStatus.OK);
    }
}
