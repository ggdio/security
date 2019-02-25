package br.com.ggdio.client.soap.validator;

import br.com.ggdio.client.common.validator.Validator;
import br.com.ggdio.client.common.validator.ValidatorException;
import br.com.ggdio.client.soap.model.Schema;

/**
 * Int element Validator
 * @author Guilherme Dio
 *
 */
public class IntValidator implements Validator{

	@Override
	public void validate(Schema schema, String value) throws ValidatorException {
		if(value != null){
			try{
				Integer.parseInt(value);
			}
			catch(NumberFormatException e){
				throw new ValidatorException();
			}
		}
	}

}