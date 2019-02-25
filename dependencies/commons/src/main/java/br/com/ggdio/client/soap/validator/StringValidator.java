package br.com.ggdio.client.soap.validator;

import br.com.ggdio.client.common.validator.Validator;
import br.com.ggdio.client.common.validator.ValidatorException;
import br.com.ggdio.client.soap.model.Schema;

/**
 * String element Validator
 * @author Guilherme Dio
 *
 */
public class StringValidator implements Validator {

	@Override
	public void validate(Schema schema, String value) throws ValidatorException {
		//Nothing to validate yet
	}

}
