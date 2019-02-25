package br.com.ggdio.client.common.validator;

import br.com.ggdio.client.soap.model.Schema;


/**
 * Data validation interface
 * @author Guilherme Dio
 *
 * @param <T>
 */
public interface Validator {

	/**
	 * Validates the input information
	 * @param value
	 * @throws ValidatorException - If its not valid, or the operation fails
	 */
	public void validate(Schema schema, String value) throws ValidatorException;
}