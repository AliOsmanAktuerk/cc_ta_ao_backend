package de.aktuerk.brothers.cuc_ta_allaince_manager.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.Collection;
import java.util.Map;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
public class CCOutputDTO {

    private Map<String, CCInputDTO> brain;

    @Override
    public String toString() {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Collection<CCInputDTO> values = brain.values();
            return objectMapper.writeValueAsString(values.stream().toList());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
