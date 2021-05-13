
package tech.knowshipp.vaccinebooking.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VaccineFee {

    @JsonProperty("vaccine")
    private String vaccine;
    @JsonProperty("fee")
    private String fee;

}
