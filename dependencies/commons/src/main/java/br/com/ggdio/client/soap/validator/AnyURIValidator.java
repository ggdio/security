package br.com.ggdio.client.soap.validator;

import java.net.URL;

import br.com.ggdio.client.common.validator.Validator;
import br.com.ggdio.client.common.validator.ValidatorException;
import br.com.ggdio.client.soap.model.Schema;

/**
 * AnyURI element Validator
 * @author Guilherme Dio
 *
 */
public class AnyURIValidator implements Validator{

	@Override
	public void validate(Schema schema, String value) throws ValidatorException {
		if(value != null)
			try {
				new URL(value).toURI();
			}
			catch(Exception e){
				throw new ValidatorException();
			}
	}
	
}