package br.com.ggdio.client.soap.converter;

import br.com.ggdio.client.common.converter.Converter;

/**
 * Byte element Converter
 * @author Guilherme Dio
 *
 */
public class ByteConverter implements Converter<Byte> {

	@Override
	public Byte toObject(String value) {
		return Byte.parseByte(value);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}