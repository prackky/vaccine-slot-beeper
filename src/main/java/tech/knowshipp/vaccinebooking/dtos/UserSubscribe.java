package tech.knowshipp.vaccinebooking.dtos;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class UserSubscribe {

	@Length(min = 6, max = 6)
	@Pattern(regexp = "^[1-9]{1}[0-9]{2}[0-9]{3}$", message = "Please enter correct pin code")
	private String pinCode;

}
