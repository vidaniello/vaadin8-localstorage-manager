package com.github.vidaniello.vaadin8.localstoragemanager;

import java.io.Serializable;

public interface LocalStorageIf extends Serializable {

	public void getClientLocalStorageItem(String key, LocalStorageRequestListener listener) throws Exception;
	
	public <T extends Serializable> void getClientLocalStorageItem(T object, LocalStorageRequestObjectListener<T> listener) throws Exception;
	
	public <T extends Serializable> void getClientLocalStorageItemFromClazz(Class<T> clazz, LocalStorageRequestObjectListener<T> listener) throws Exception;
	
	
	public void setClientLocalStorageItem(String key, String value, LocalStorageRequestListener listener) throws Exception;
	
	public <T extends Serializable> void setClientLocalStorageItem(T object, LocalStorageRequestObjectListener<T> listener) throws Exception;
	
	public void setClientLocalStorageItem(String key, String value) throws Exception;
	
	public <T extends Serializable> void setClientLocalStorageItem(T object) throws Exception;
	
	
	public void removeClientLocalStorageItem(String key, LocalStorageRequestListener listener) throws Exception;
	
	public <T extends Serializable> void removeClientLocalStorageItem(T object, LocalStorageRequestObjectListener<T> listener) throws Exception;
	
	public <T extends Serializable> void removeClientLocalStorageItemFromClazz(Class<T> clazz, LocalStorageRequestObjectListener<T> listener) throws Exception;
	
	public void removeClientLocalStorageItem(String key) throws Exception;
	
	public <T extends Serializable> void removeClientLocalStorageItem(T object) throws Exception;
	
	public <T extends Serializable> void removeClientLocalStorageItemFromClazz(Class<T> clazz) throws Exception;
	
	
	public void getRemoteOrigin(LocalStorageInfoListener listener);
}
