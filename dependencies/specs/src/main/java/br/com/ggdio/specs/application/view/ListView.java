package br.com.ggdio.specs.application.view;

import java.util.ArrayList;
import java.util.List;

/**
 * List of domain objects view for app layer.
 * 
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 30 Jul 2018
 * @since 1.0.0-RELEASE
 */
public interface ListView<I extends ItemView<?>> {

	public List<I> getItemList();
	public void setItemList(List<I> items);
	
	public default void addItem(I item) {
		List<I> list = getItemList();
		if(list == null) {
			list = new ArrayList<>();
			setItemList(list);
		}
		list.add(item);
	}
	
}