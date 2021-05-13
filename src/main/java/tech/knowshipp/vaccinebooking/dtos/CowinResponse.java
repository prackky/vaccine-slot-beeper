
package tech.knowshipp.vaccinebooking.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CowinResponse {

    @JsonProperty("centers")
    private List<Center> centers = null;

}
