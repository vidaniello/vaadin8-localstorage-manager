package com.github.vidaniello.vaadin8.localstoragemanager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Notification;

import elemental.json.JsonArray;
import elemental.json.impl.JreJsonNull;

/**
 * Read and write on 'wnidow.localStorage' remote browser client API.
 * @author Vincenzo D'Aniello (vidaniello@gmail.com) github.com/vidaniello
 *
 */
public class LocalStorageComponent extends AbstractComponent implements JavaScriptFunction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String localJsFilename = "staticStorage_1.0.0.js";
	private static final String serverFunctionName_prefix = "com.github.vidaniello.vaadin8.localstoragemanager.LocalStorageComponent.call";
	private static final String remoteSetAndRetrieveLocalStorage_prefix = "setAndRetrieveLocalStorage";
	private static final String remoteGetRemoteOrigin_prefix = "getRemoteOrigin";
	private static final String staticStorageUI_globalVar = "staticStorageUI";
	
	private HasComponents parentComponent;
	private String lastRequestKey;
	private LocalStorageListener lastListener;
	
	private String this_serverFunctionName;
	private String this_remoteSetAndRetrieveLocalStorage;
	private String this_remoteGetRemoteOrigin;
	
	private LocalStorageComponent() {
		
	}
	
	public LocalStorageComponent(HasComponents parentComponent) {
		this();
		this.parentComponent = parentComponent;
		this.parentComponent.addAttachListener(this::onAttach);
		this.parentComponent.addDetachListener(this::onDetach);
		
		setParent(parentComponent);
		
		if(parentComponent.isAttached())
			onAttach(null);
	}
	
	private void onAttach(AttachEvent evt) {
		try {

			//Multiple javascript methods are created because there can be multiple components of the type loaded
			//in the same UI. We use the connectorId to identify the instance-specific return function to call.
			
			this_serverFunctionName = serverFunctionName_prefix+"_"+getConnectorId();
			
			JavaScript.getCurrent().addFunction(this_serverFunctionName, this);
			
			this_remoteSetAndRetrieveLocalStorage = staticStorageUI_globalVar+"."+remoteSetAndRetrieveLocalStorage_prefix+"_"+getConnectorId();
			this_remoteGetRemoteOrigin = staticStorageUI_globalVar+"."+remoteGetRemoteOrigin_prefix+"_"+getConnectorId();
			
			InputStream is = this.getClass().getResourceAsStream(localJsFilename);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			copy(is, os);
			is.close();
			String js = new String(os.toByteArray());
			os.close();
			
			JavaScript.getCurrent().execute(
					js+
					this_remoteSetAndRetrieveLocalStorage+" = function(key, value){"+
						this_serverFunctionName+"("+staticStorageUI_globalVar+".localstorage_setAndRetrieve(key, value));"+
					"};"+
					this_remoteGetRemoteOrigin+" = function(){"+
						this_serverFunctionName+"(window.origin);"+
					"};"		
			);
			
			
			
		} catch (Exception e) {
			Notification.show(e.getClass().getSimpleName(), e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}
	
	private void onDetach(DetachEvent evt) {
		try {
			
			JavaScript.getCurrent().removeFunction(this_serverFunctionName);
			
		} catch (Exception e) {
			//Notification.show(e.getClass().getSimpleName(), e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}

	@Override
	public void call(JsonArray arguments) {
		
		if(lastListener!=null)
			try {
				
				String value = arguments.asString();
				
				if(arguments.length()==1)
					if(arguments.get(0).jsEquals(JreJsonNull.NULL_INSTANCE))
						value = null;
				
				if(LocalStorageRequestListener.class.isAssignableFrom(lastListener.getClass()))
					LocalStorageRequestListener.class.cast(lastListener).onRequestResponse(lastRequestKey, value);
				else if(LocalStorageInfoListener.class.isAssignableFrom(lastListener.getClass()))
					LocalStorageInfoListener.class.cast(lastListener).onRequestResponse(value);
				
			} finally {
				lastRequestKey = null;
				lastListener = null;
			}		
		
	}
	
	public void getClientLocalStorageItem(String key, LocalStorageRequestListener listener) throws Exception {
		
		if(key==null)
			throw new Exception("a key must be specified");
		
		if(key.trim().isEmpty())
			throw new Exception("a key must be specified");
		
		lastRequestKey = key;
		lastListener = listener;
		
		JavaScript.getCurrent().execute(this_remoteSetAndRetrieveLocalStorage+"('"+key+"');");
	}
	
	public void setClientLocalStorageItem(String key, String value, LocalStorageRequestListener listener) throws Exception {
		
		if(key==null)
			throw new Exception("a key must be specified");
		
		if(key.trim().isEmpty())
			throw new Exception("a key must be specified");
		
		lastRequestKey = key;
		lastListener = listener;		
		
		if(value!=null)
			JavaScript.getCurrent().execute(this_remoteSetAndRetrieveLocalStorage+"('"+key+"', '"+value+"');");
		else
			JavaScript.getCurrent().execute(this_remoteSetAndRetrieveLocalStorage+"('"+key+"', null);");
	}
	
	public void setClientLocalStorageItem(String key, String value) throws Exception {
		setClientLocalStorageItem(key, value, null);
	}
	
	public void removeClientLocalStorageItem(String key, LocalStorageRequestListener listener) throws Exception {
		setClientLocalStorageItem(key, null, listener);
	}
	
	public void removeClientLocalStorageItem(String key) throws Exception {
		setClientLocalStorageItem(key, null, null);
	}
	
	public void getRemoteOrigin(LocalStorageInfoListener listener) {
		lastListener = listener;
		JavaScript.getCurrent().execute(this_remoteGetRemoteOrigin+"();");
	}

	private void copy(InputStream source, OutputStream target) throws IOException {
	    byte[] buf = new byte[8192];
	    int length;
	    while ((length = source.read(buf)) > 0) {
	        target.write(buf, 0, length);
	    }
	}
}
