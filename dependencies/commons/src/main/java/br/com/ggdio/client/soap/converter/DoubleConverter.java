package br.com.ggdio.client.soap.converter;

import br.com.ggdio.client.common.converter.Converter;

/**
 * Double element Converter
 * @author Guilherme Dio
 *
 */
public class DoubleConverter implements Converter<Double> {

	@Override
	public Double toObject(String value) {
		return Double.parseDouble(value);
	}

	@Override
	public String toString(Object value) {
		return String.valueOf(value);
	}

}
