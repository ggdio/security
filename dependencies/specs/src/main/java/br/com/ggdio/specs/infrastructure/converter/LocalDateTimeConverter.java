package br.com.ggdio.specs.infrastructure.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converter for casting Java::{@link LocalDateTime} into SQL::{@link Timestamp}
 * 
 * @author Guiherme Dio
 *
 */
@Converter(autoApply=true)
public class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Timestamp>{

	@Override
	public Timestamp convertToDatabaseColumn(LocalDateTime dateTime) {
		return (dateTime == null ? null : Timestamp.valueOf(dateTime));
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Timestamp timestamp) {
		return (timestamp == null ? null : timestamp.toLocalDateTime());
	}

}