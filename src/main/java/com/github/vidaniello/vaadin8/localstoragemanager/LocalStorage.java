package com.github.vidaniello.vaadin8.localstoragemanager;

import java.io.Serializable;

import com.vaadin.ui.HasComponents;

/**
 * Wrap a LocalStorageComponent
 * @author Vincenzo D'Aniello (vidaniello@gmail.com) github.com/vidaniello
 *
 */
public class LocalStorage implements LocalStorageIf {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LocalStorageComponent localStorageComponent;
	
	/**
	 * Only on attached state.
	 */
	public LocalStorage() {
		localStorageComponent = new LocalStorageComponent();
	}
	
	public LocalStorage(HasComponents parentComponent) {
		localStorageComponent = new LocalStorageComponent(parentComponent);
	}
	
	public LocalStorageComponent getLocalStorageComponent() {
		return localStorageComponent;
	}
	
	
	
	@Override
	public void getClientLocalStorageItemByStringKey(String key, LocalStorageRequestListener listener) throws Exception {
		getLocalStorageComponent().getClientLocalStorageItemByStringKey(key, listener);
	}
	
	
	@Override
	public <T extends Serializable> void getClientLocalStorageItem(T object, LocalStorageRequestObjectListener<T> listener)
			throws Exception {
		getLocalStorageComponent().getClientLocalStorageItem(object, listener);
	}
	
	
	@Override
	public <T extends Serializable> void getClientLocalStorageItemFromClazz(Class<T> clazz,
			LocalStorageRequestObjectListener<T> listener) throws Exception {
		getLocalStorageComponent().getClientLocalStorageItemFromClazz(clazz, listener);
	}
	
	
	
	@Override
	public void setClientLocalStorageItemByStringKey(String key, String value, LocalStorageRequestListener listener)
			throws Exception {
		getLocalStorageComponent().setClientLocalStorageItemByStringKey(key, value, listener);
	}
	
	@Override
	public <T extends Serializable> void setClientLocalStorageItem(T object, LocalStorageRequestObjectListener<T> listener)
			throws Exception {
		getLocalStorageComponent().setClientLocalStorageItem(object, listener);
	}
	
	@Override
	public void setClientLocalStorageItemByStringKey(String key, String value) throws Exception {
		getLocalStorageComponent().setClientLocalStorageItemByStringKey(key, value);
	}
	
	@Override
	public <T extends Serializable> void setClientLocalStorageItem(T object) throws Exception {
		getLocalStorageComponent().setClientLocalStorageItem(object);
	}
	
	
	
	@Override
	public void removeClientLocalStorageItemByStringKey(String key, LocalStorageRequestListener listener) throws Exception {
		getLocalStorageComponent().removeClientLocalStorageItemByStringKey(key, listener);
	}
	
	@Override
	public <T extends Serializable> void removeClientLocalStorageItem(T object, LocalStorageRequestObjectListener<T> listener)
			throws Exception {
		getLocalStorageComponent().removeClientLocalStorageItem(object, listener);
	}
	
	@Override
	public <T extends Serializable> void removeClientLocalStorageItemFromClazz(Class<T> clazz,
			LocalStorageRequestObjectListener<T> listener) throws Exception {
		getLocalStorageComponent().removeClientLocalStorageItemFromClazz(clazz, listener);
	}

	@Override
	public void removeClientLocalStorageItemByStringKey(String key) throws Exception {
		getLocalStorageComponent().removeClientLocalStorageItemByStringKey(key);
	}
	
	@Override
	public <T extends Serializable> void removeClientLocalStorageItem(T object) throws Exception {
		getLocalStorageComponent().removeClientLocalStorageItem(object);		
	}
	
	@Override
	public <T extends Serializable> void removeClientLocalStorageItemFromClazz(Class<T> clazz) throws Exception {
		getLocalStorageComponent().removeClientLocalStorageItemFromClazz(clazz);		
	}
	
	@Override
	public void getRemoteOrigin(LocalStorageInfoListener listener) {
		getLocalStorageComponent().getRemoteOrigin(listener);
	}
	
	
}
