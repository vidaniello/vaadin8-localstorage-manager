package com.github.vidaniello.vaadin8.localstoragemanager;

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
	public void getClientLocalStorageItem(String key, LocalStorageRequestListener listener) throws Exception {
		getLocalStorageComponent().getClientLocalStorageItem(key, listener);
	}
	
	@Override
	public void setClientLocalStorageItem(String key, String value, LocalStorageRequestListener listener)
			throws Exception {
		getLocalStorageComponent().setClientLocalStorageItem(key, value, listener);
	}
	
	@Override
	public void setClientLocalStorageItem(String key, String value) throws Exception {
		getLocalStorageComponent().setClientLocalStorageItem(key, value);
	}
	
	@Override
	public void removeClientLocalStorageItem(String key, LocalStorageRequestListener listener) throws Exception {
		getLocalStorageComponent().removeClientLocalStorageItem(key, listener);
	}

	@Override
	public void removeClientLocalStorageItem(String key) throws Exception {
		getLocalStorageComponent().removeClientLocalStorageItem(key);
	}
	
	@Override
	public void getRemoteOrigin(LocalStorageInfoListener listener) {
		getLocalStorageComponent().getRemoteOrigin(listener);
	}
	
}
