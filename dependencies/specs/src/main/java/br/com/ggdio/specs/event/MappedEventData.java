package br.com.ggdio.specs.event;

import java.util.Map;

/**
 * Mapped impl. for EventData
 * @author Guilherme Dio
 *
 */
public class MappedEventData implements EventData {

	private static final long serialVersionUID = -8042048515307269147L;
	
	private final Map<String, Object> data;
	
	public MappedEventData(Map<String, Object> data) {
		this.data = data;
	}
	
	public Map<String, Object> getData() {
		return data;
	}
	
}