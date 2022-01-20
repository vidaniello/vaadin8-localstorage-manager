package com.github.vidaniello.vaadin8.localstoragemanager;

import java.io.Serializable;

@FunctionalInterface
public interface LocalStorageRequestObjectListener<T extends Serializable> extends LocalStorageListener {
	
	public void onRequestObjectResponse(T object);
	
}
