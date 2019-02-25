package br.com.ggdio.client.soap.validator;

import br.com.ggdio.client.common.validator.Validator;
import br.com.ggdio.client.common.validator.ValidatorException;
import br.com.ggdio.client.soap.model.Schema;

/**
 * Long element Validator
 * @author Guilherme Dio
 *
 */
public class LongValidator implements Validator{

	@Override
	public void validate(Schema schema, String value) throws ValidatorException {
		if(value != null){
			try{
				Long.parseLong(value);
			}
			catch(NumberFormatException e){
				throw new ValidatorException();
			}
		}
	}

}