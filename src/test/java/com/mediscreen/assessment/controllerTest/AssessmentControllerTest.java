package com.mediscreen.assessment.controllerTest;

import com.mediscreen.assessment.controller.AssessmentController;
import com.mediscreen.assessment.dto.AssessmentDTO;
import com.mediscreen.assessment.model.Patient;
import com.mediscreen.assessment.service.AssessmentService;
import com.mediscreen.assessment.webclient.PatientWebClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AssessmentControllerTest {

    @Mock
    private PatientWebClient patientWebClient;

    @Mock
    private AssessmentService assessmentService;

    @InjectMocks
    private AssessmentController assessmentController;

    private Patient testPatient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Create a test patient with sample data
        testPatient = new Patient();
        testPatient.setId(1);
        testPatient.setFamily("Doe");
        testPatient.setGiven("John");
        testPatient.setDob(LocalDate.of(1980, 1, 1));
        testPatient.setSex("M");
    }


    @Test
    public void testGetRisk() {
        // GIVEN
        // There is at least a patient
        AssessmentDTO sampleAssessment = new AssessmentDTO(1, "Doe", "John", 41, "None");
        when(patientWebClient.findById(any())).thenReturn(Mono.just(testPatient));
        when(assessmentService.getAssessment(any())).thenReturn(sampleAssessment);

        // WHEN
        // I ask the assessment by the patient id
        AssessmentDTO result = assessmentController.getRisk(1);
        assertEquals(sampleAssessment, result);

        //THEN
        verify(patientWebClient, times(1)).findById(1);
        verify(assessmentService, times(1)).getAssessment(testPatient);
    }

    @Test
    public void testGetAssessmentById() {
        // GIVEN
        // There is at least a patient
        when(patientWebClient.findById(any())).thenReturn(Mono.just(testPatient));
        AssessmentDTO sampleAssessment = new AssessmentDTO(1, "Doe", "John", 41, "None");
        when(assessmentService.getAssessment(any())).thenReturn(sampleAssessment);

        // WHEN
        // I ask the assessment by the patient id
        ResponseEntity<String> result = assessmentController.getAssessmentById(1);
        assertEquals(sampleAssessment.toString(), result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());

        //THEN
        verify(patientWebClient, times(1)).findById(1);
        verify(assessmentService, times(1)).getAssessment(testPatient);
    }

    @Test
    public void testGetAssessmentByName() {
        //GIVEN
        // There is at least a patient
        when(patientWebClient.findByName(any(), any())).thenReturn(Mono.just(testPatient));
        AssessmentDTO sampleAssessment = new AssessmentDTO(1, "Doe", "John", 41, "None");
        when(assessmentService.getAssessment(any())).thenReturn(sampleAssessment);

        //THEN
        // I ask the assessment by the patient name
        ResponseEntity<String> result = assessmentController.getAssessmentByName("Doe", "John");
        assertEquals(sampleAssessment.toString(), result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());

        //THEN
        verify(patientWebClient, times(1)).findByName("Doe", "John");
        verify(assessmentService, times(1)).getAssessment(testPatient);
    }
}
