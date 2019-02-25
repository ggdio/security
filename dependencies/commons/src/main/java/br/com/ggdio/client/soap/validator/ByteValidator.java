package br.com.ggdio.client.soap.validator;

import br.com.ggdio.client.common.validator.Validator;
import br.com.ggdio.client.common.validator.ValidatorException;
import br.com.ggdio.client.soap.model.Schema;

/**
 * Byte element Validator
 * @author Guilherme Dio
 *
 */
public class ByteValidator implements Validator{

	@Override
	public void validate(Schema schema, String value) throws ValidatorException {
		if(value != null)
			try{
				Byte.parseByte(value);
			}
			catch(NumberFormatException e){
				throw new ValidatorException();
			}
	}

}