package com.github.vidaniello.vaadin8.localstoragemanager;

import java.io.Serializable;

public class ActiveCall {
	
	private String requestKey;
	private Class<? extends Serializable> objectClass;
	private LocalStorageListener listener;
	
	public ActiveCall() {
		
	}
	
	public ActiveCall(String requestKey, Class<? extends Serializable> objectClass, LocalStorageListener listener) {
		super();
		this.requestKey = requestKey;
		this.objectClass = objectClass;
		this.listener = listener;
	}

	public String getRequestKey() {
		return requestKey;
	}

	public void setRequestKey(String requestKey) {
		this.requestKey = requestKey;
	}

	public Class<? extends Serializable> getObjectClass() {
		return objectClass;
	}

	public void setObjectClass(Class<? extends Serializable> objectClass) {
		this.objectClass = objectClass;
	}

	public LocalStorageListener getListener() {
		return listener;
	}

	public void setListener(LocalStorageListener listener) {
		this.listener = listener;
	}
	


}
