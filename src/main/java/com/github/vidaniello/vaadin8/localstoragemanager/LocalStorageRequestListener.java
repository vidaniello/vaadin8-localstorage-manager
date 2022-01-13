package com.github.vidaniello.vaadin8.localstoragemanager;

@FunctionalInterface
public interface LocalStorageRequestListener extends LocalStorageListener {
	
	public void onRequestResponse(String key, String value);
	
}
