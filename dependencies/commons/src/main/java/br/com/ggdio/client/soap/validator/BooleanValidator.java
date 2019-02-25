package br.com.ggdio.client.soap.validator;

import br.com.ggdio.client.common.validator.Validator;
import br.com.ggdio.client.common.validator.ValidatorException;
import br.com.ggdio.client.soap.model.Schema;

/**
 * Boolean element Validator
 * @author Guilherme Dio
 *
 */
public class BooleanValidator implements Validator{

	@Override
	public void validate(Schema schema, String value) throws ValidatorException {
		if(value != null)
			if(!value.toLowerCase().equals("true") && !value.toLowerCase().equals("false"))
				throw new ValidatorException();
	}

}