package com.mediscreen.assessment.webclient;

import com.mediscreen.assessment.model.History;
import com.mediscreen.assessment.model.Patient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * WebClient component used to communicate with the History microservice to retrieve patient history data.
 */
@Component
public class HistoryWebClient {

    @Value("${HISTORY.PROXY}")
    public String URL_HISTORY;
    WebClient.Builder historyWebClient = WebClient.builder();

    /**
     * Retrieves the history records for a patient with the specified ID from the History microservice.
     *
     * @param id The ID of the patient for whom the history records are to be retrieved.
     * @return A Mono emitting a list of History objects representing the patient's history records.
     */
    public Mono<List<History>> findById(Integer id) {
        return historyWebClient.build()
                .get()
                .uri(URL_HISTORY + "/patHistory?patId={id}", id)
                .retrieve()
                /*.onStatus(httpStatus -> HttpStatus.NOT_FOUND.equals(httpStatus),
                        clientResponse -> Mono.empty())*/
                .bodyToFlux(History.class)
                .collectList();
    }

}