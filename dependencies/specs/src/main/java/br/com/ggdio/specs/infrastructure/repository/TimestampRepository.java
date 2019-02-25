package br.com.ggdio.specs.infrastructure.repository;

/**
 * Interface for acessing current timestamp
 * 
 * @author Guilherme Dio
 *
 */
public interface TimestampRepository {

	public default long currentTimestamp() {
		return System.currentTimeMillis();
	}
	
}