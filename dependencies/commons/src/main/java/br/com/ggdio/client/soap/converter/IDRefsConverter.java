package br.com.ggdio.client.soap.converter;

import br.com.ggdio.client.common.converter.Converter;

/**
 * IDRefs element Converter
 * @author Guilherme Dio
 *
 */
public class IDRefsConverter implements Converter<String> {

	@Override
	public String toObject(String value) {
		return String.valueOf(value);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}