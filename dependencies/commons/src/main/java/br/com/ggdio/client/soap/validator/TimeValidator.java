package br.com.ggdio.client.soap.validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.com.ggdio.client.common.validator.Validator;
import br.com.ggdio.client.common.validator.ValidatorException;
import br.com.ggdio.client.soap.model.Schema;

/**
 * TimeValidator element Validator
 * @author Guilherme Dio
 *
 */
public class TimeValidator implements Validator{

	@Override
	public void validate(Schema schema, String value) throws ValidatorException {
		try{
			LocalDateTime.parse(value, DateTimeFormatter.ISO_TIME);
		}
		catch(Exception e){
			throw new ValidatorException();
		}
	}

}