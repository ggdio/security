package br.com.ggdio.client.soap.validator;

import br.com.ggdio.client.common.validator.Validator;
import br.com.ggdio.client.common.validator.ValidatorException;
import br.com.ggdio.client.soap.model.Schema;

/**
 * Character element Validator
 * @author Guilherme Dio
 *
 */
public class CharacterValidator implements Validator {

	@Override
	public void validate(Schema schema, String value) throws ValidatorException {
		//Check if it contains only 1 character
		if(value != null && value.length() > 1)
			throw new ValidatorException();
	}
	
}