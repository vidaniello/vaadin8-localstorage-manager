package com.github.vidaniello.vaadin8.localstoragemanager;

@FunctionalInterface
public interface LocalStorageInfoListener extends LocalStorageListener {
	
	public void onRequestResponse(String value);
	
}
