package com.github.vidaniello.vaadin8.localstoragemanager;

import java.io.Serializable;

public interface LocalStorageIf extends Serializable {

	public void getClientLocalStorageItem(String key, LocalStorageRequestListener listener) throws Exception;
	
	public void setClientLocalStorageItem(String key, String value, LocalStorageRequestListener listener) throws Exception;
	
	public void setClientLocalStorageItem(String key, String value) throws Exception;
	
	public void removeClientLocalStorageItem(String key, LocalStorageRequestListener listener) throws Exception;
	
	public void removeClientLocalStorageItem(String key) throws Exception;
	
	public void getRemoteOrigin(LocalStorageInfoListener listener);
}
