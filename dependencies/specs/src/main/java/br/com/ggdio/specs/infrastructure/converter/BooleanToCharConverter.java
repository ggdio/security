package br.com.ggdio.specs.infrastructure.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converter for casting Boolean(true,false) into String (Y,N)
 * 
 * @author Guiherme Dio
 *
 */
@Converter(autoApply=true)
public class BooleanToCharConverter implements AttributeConverter<Boolean, Character> {

	@Override
	public Character convertToDatabaseColumn(Boolean value) {
		return (value != null && value) ? 'Y' : 'N';
	}

	@Override
	public Boolean convertToEntityAttribute(Character value) {
		return (value != null && value == 'Y') ? Boolean.TRUE : Boolean.FALSE;
	}

}
