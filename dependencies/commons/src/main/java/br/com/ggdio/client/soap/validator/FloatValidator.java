package br.com.ggdio.client.soap.validator;

import br.com.ggdio.client.common.validator.Validator;
import br.com.ggdio.client.common.validator.ValidatorException;
import br.com.ggdio.client.soap.model.Schema;

/**
 * Float element Validator
 * @author Guilherme Dio
 *
 */
public class FloatValidator implements Validator{

	@Override
	public void validate(Schema schema, String value) throws ValidatorException {
		if(value != null){
			try{
				Float.parseFloat(value);
			}
			catch(NumberFormatException e){
				throw new ValidatorException();
			}
		}
	}

}