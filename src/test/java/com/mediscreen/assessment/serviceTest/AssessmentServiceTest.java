package com.mediscreen.assessment.serviceTest;

import com.mediscreen.assessment.dto.AssessmentDTO;
import com.mediscreen.assessment.model.History;
import com.mediscreen.assessment.model.Patient;
import com.mediscreen.assessment.service.AssessmentService;
import com.mediscreen.assessment.webclient.HistoryWebClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AssessmentServiceTest {

    @Mock
    private HistoryWebClient historyWebClient;

    @InjectMocks
    private AssessmentService assessmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Mock the behavior of findById method
        when(historyWebClient.findById(anyInt())).thenReturn(Mono.just(new ArrayList<>()));
    }


    @Test
    public void getAssessmentTest() {
        // GIVEN
        // there is at least a patient
        int patientId = 1;
        Patient testPatient = new Patient();
        testPatient.setId(patientId);
        testPatient.setFamily("Doe");
        testPatient.setGiven("John");
        testPatient.setDob(LocalDate.of(1980, 5, 15));
        testPatient.setSex("M");

        List<History> testHistories = new ArrayList<>();
        History history1 = new History();
        history1.setId("123");
        history1.setPatient("Doe");
        history1.setNotes("reaction abnormal height");
        testHistories.add(history1);
        when(historyWebClient.findById(patientId)).thenReturn(Mono.just(testHistories));

        // WHEN
        // I ask the assessment
        AssessmentDTO result = assessmentService.getAssessment(testPatient);

        // THEN
        int expectedAge = 43; // Manually calculated based on the patient's date of birth
        int expectedTriggerTerms = 3; // Manually calculated based on the test histories
        String expectedLevelOfRisk = "Borderline"; // Manually calculated based on the age and trigger terms

        assertEquals(patientId, result.getPatId());
        assertEquals(testPatient.getFamily(), result.getFamily());
        assertEquals(testPatient.getGiven(), result.getGiven());
        assertEquals(expectedAge, result.getAge());
        assertEquals(expectedLevelOfRisk, result.getDiabetesAssessment());
    }

    @Test
    public void calculateAgeTest() {
        // GIVEN
        LocalDate dob = LocalDate.of(1990, 10, 20);

        // WHEN
        int age = assessmentService.calculateAge(dob);

        // Then
        int expectedAge = 32; // Manually calculated based on the current date (as of writing this)
        assertEquals(expectedAge, age);
    }

    @Test
    public void toLowerCaseWithoutAccentsTest() {
        // GIVEN
        String input = "Élève";

        // WHEN
        String result = assessmentService.toLowerCaseWithoutAccents(input);

        // Then
        String expectedOutput = "eleve";
        assertEquals(expectedOutput, result);
    }

    @Test
    public void calculateTriggerTermsTest() {
        // GIVEN
        List<History> testHistories = new ArrayList<>();
        History history1 = new History();
        history1.setId("123");
        history1.setPatient("Doe");
        history1.setNotes("reaction abnormal height");
        testHistories.add(history1);

        // WHEN
        int result = assessmentService.calculateTriggerTerms(testHistories);

        // THEN
        int expectedCount = 3; // Manually calculated based on the test histories
        assertEquals(expectedCount, result);
    }

    @Test
    public void calculateLevelOfRiskTest() {
        // GIVEN
        // There are patient data

        String maleSex = "M";
        String femaleSex = "F";
        int age30 = 30;
        int age29 = 29;
        int triggerTerms1 = 1;
        int triggerTerms5 = 5;
        int triggerTerms6 = 6;

        // WHEN/THEN
        // Give the result with the level of Risk depending the patient condition
        // If the patient is more than 30yo

        assertEquals("None", assessmentService.calculateLevelOfRisk(maleSex, age30, triggerTerms1));
        assertEquals("None", assessmentService.calculateLevelOfRisk(femaleSex, age30, triggerTerms1));

        assertEquals("Borderline", assessmentService.calculateLevelOfRisk(maleSex, age30, triggerTerms5));
        assertEquals("Borderline", assessmentService.calculateLevelOfRisk(femaleSex, age30, triggerTerms5));

        assertEquals("In Danger", assessmentService.calculateLevelOfRisk(femaleSex, age30, triggerTerms6));
        assertEquals("In Danger", assessmentService.calculateLevelOfRisk(maleSex, age30, triggerTerms6));

        assertEquals("Early onset", assessmentService.calculateLevelOfRisk(maleSex, age30, triggerTerms6 + 2));
        assertEquals("Early onset", assessmentService.calculateLevelOfRisk(femaleSex, age30, triggerTerms6 + 2));

        // If the patient is less than 30yo
        // and a male
        assertEquals("None", assessmentService.calculateLevelOfRisk(maleSex, age29, triggerTerms1));
        assertEquals("In Danger", assessmentService.calculateLevelOfRisk(maleSex, age29, triggerTerms5 - 2));
        assertEquals("Early onset", assessmentService.calculateLevelOfRisk(maleSex, age29, triggerTerms5 + 2));

        // and a woman
        assertEquals("None", assessmentService.calculateLevelOfRisk(femaleSex, age29, triggerTerms1));
        assertEquals("In Danger", assessmentService.calculateLevelOfRisk(femaleSex, age29, triggerTerms5));
        assertEquals("Early onset", assessmentService.calculateLevelOfRisk(femaleSex, age29, triggerTerms5 + 2));
    }
}
