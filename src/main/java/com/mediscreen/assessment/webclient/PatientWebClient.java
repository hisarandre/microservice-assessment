package com.mediscreen.assessment.webclient;

import com.mediscreen.assessment.model.Patient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * WebClient component used to communicate with the Patient microservice to retrieve patient data.
 */
@Component
public class PatientWebClient {

    @Value("${PATIENT.PROXY}")
    public String URL_PATIENT;
    WebClient.Builder patientWebClient = WebClient.builder();

    /**
     * Retrieves all patient records from the Patient microservice.
     *
     * @return A Mono emitting a list of Patient objects representing all patients' records.
     */
    public Mono<List<Patient>> findAll() {
        return patientWebClient.build()
                .get()
                .uri(URL_PATIENT + "/patient/all" )
                .retrieve()
                .bodyToFlux(Patient.class)
                .collectList();
    }

    /**
     * Retrieves the patient record for a patient with the specified ID from the Patient microservice.
     *
     * @param id The ID of the patient for whom the record is to be retrieved.
     * @return A Mono emitting a Patient object representing the patient's record.
     */
    public Mono<Patient> findById(Integer id) {
        System.out.println(URL_PATIENT);

        return patientWebClient.build()
                .get()
                .uri( URL_PATIENT +"/patient/" + id)
                .retrieve()
                /*.onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus),
                        clientResponse -> Mono.empty())*/
                .bodyToMono(Patient.class);
    }

    /**
     * Retrieves the patient record for a patient with the specified family name and given name from the Patient microservice.
     *
     * @param family The family name of the patient.
     * @param given The given name of the patient.
     * @return A Mono emitting a Patient object representing the patient's record.
     */
    public Mono<Patient> findByName(String family, String given) {
        return patientWebClient.build()
                .get()
                .uri( URL_PATIENT + "/patient?family={family}&given={given}", family, given)
                .retrieve()
                /*.onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus),
                        clientResponse -> Mono.empty())*/
                .bodyToMono(Patient.class);
    }
}
