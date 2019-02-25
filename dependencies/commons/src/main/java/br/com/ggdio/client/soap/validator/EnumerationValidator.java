package br.com.ggdio.client.soap.validator;

import br.com.ggdio.client.common.validator.Validator;
import br.com.ggdio.client.common.validator.ValidatorException;
import br.com.ggdio.client.soap.model.Schema;

/**
 * String element Validator
 * @author Guilherme Dio
 *
 */
public class EnumerationValidator implements Validator {

	@Override
	public void validate(Schema schema, String value) throws ValidatorException {
		Schema next = schema.getInner();
		do 
			if(next.getName().equalsIgnoreCase(value)) 
				return; 
		while((next = next.getNext()) != null);
		throw new ValidatorException();
	}

}