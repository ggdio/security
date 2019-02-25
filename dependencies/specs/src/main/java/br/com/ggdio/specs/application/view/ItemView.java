package br.com.ggdio.specs.application.view;

/**
 * Unitary domain object view for app layer.
 * 
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 30 Jul 2018
 * @since 1.0.0-RELEASE
 * 
 * @param <D> - Domain Model Type
 */
public interface ItemView<D> {
	
	public default D unwrap() {
		return null;
	}
	
}