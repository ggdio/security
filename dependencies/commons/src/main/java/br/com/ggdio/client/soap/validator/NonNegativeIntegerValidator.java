package br.com.ggdio.client.soap.validator;

import br.com.ggdio.client.common.validator.Validator;
import br.com.ggdio.client.common.validator.ValidatorException;
import br.com.ggdio.client.soap.model.Schema;

/**
 * NonNegativeInteger element Validator
 * @author Guilherme Dio
 *
 */
public class NonNegativeIntegerValidator implements Validator{

	@Override
	public void validate(Schema schema, String value) throws ValidatorException {
		if(value != null){
			try{
				int intValue = Integer.parseInt(value);
				if(intValue < 0)
					throw new ValidatorException();
			}
			catch(NumberFormatException e){
				throw new ValidatorException();
			}
		}
	}

}