package com.github.vidaniello.vaadin8.localstoragemanager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import com.google.gson.Gson;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

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
	
	/*
	private String lastRequestKey;
	private Class<? extends Serializable> lastObjectClass;
	private LocalStorageListener lastListener;
	*/
	private Deque<ActiveCall> calls;
	
	private String this_serverFunctionName;
	private String this_remoteSetAndRetrieveLocalStorage;
	private String this_remoteGetRemoteOrigin;
	
	/**
	 * Only on attached state.
	 */
	protected LocalStorageComponent() {
		this(UI.getCurrent());
	}
	
	protected LocalStorageComponent(HasComponents parentComponent) {
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
	
	private void copy(InputStream source, OutputStream target) throws IOException {
	    byte[] buf = new byte[8192];
	    int length;
	    while ((length = source.read(buf)) > 0) {
	        target.write(buf, 0, length);
	    }
	}
	
	private void onDetach(DetachEvent evt) {
		try {
			
			JavaScript.getCurrent().removeFunction(this_serverFunctionName);
			
		} catch (Exception e) {
			//Notification.show(e.getClass().getSimpleName(), e.getMessage(), Notification.Type.ERROR_MESSAGE);
		}
	}
	
	public Deque<ActiveCall> getCalls() {
		if(calls==null)
			calls = new ConcurrentLinkedDeque<>();
		return calls;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void call(JsonArray arguments) {

		ActiveCall call = getCalls().pollFirst();
				
		if(call!=null)
			if(call.getListener()!=null) {
				LocalStorageListener lsl = call.getListener();
				try {
					
					String value = arguments.asString();
					
					//Vaadin 'arguments.asString' evaluate null reference from javascript with string like "null". 
					//This control evaluate this eventuality and set the value with null reference instead of "null" string.
					if(arguments.length()==1)
						if(arguments.get(0).jsEquals(JreJsonNull.NULL_INSTANCE))
							value = null;
					
					if(LocalStorageRequestListener.class.isAssignableFrom(lsl.getClass()))
						LocalStorageRequestListener.class.cast(lsl).onRequestResponse(call.getRequestKey(), value);
					else if(LocalStorageRequestObjectListener.class.isAssignableFrom(lsl.getClass())) {
						Serializable ob = new Gson().fromJson(value, call.getObjectClass());
						LocalStorageRequestObjectListener.class.cast(lsl).onRequestObjectResponse(ob);
					} else if(LocalStorageInfoListener.class.isAssignableFrom(lsl.getClass()))
						LocalStorageInfoListener.class.cast(lsl).onRequestResponse(value);
					
				} finally {
					/*
					lastRequestKey = null;
					lastObjectClass = null;
					lastListener = null;
					*/
				}		
			}
		
	}
	
	
	
	private void _getClientLocalStorageItem(ActiveCall call) throws Exception {
		
		if(call.getRequestKey()==null)
			throw new Exception("a key must be specified");
		
		if(call.getRequestKey().trim().isEmpty())
			throw new Exception("a key must be specified");
		
		getCalls().addLast(call);
		
		JavaScript.getCurrent().execute(this_remoteSetAndRetrieveLocalStorage+"('"+call.getRequestKey()+"');");
	}
	
	protected void getClientLocalStorageItem(String key, LocalStorageRequestListener listener) throws Exception {
		ActiveCall call = new ActiveCall(key, null, listener);
		_getClientLocalStorageItem(call);
	}
	
	protected <T extends Serializable> void getClientLocalStorageItem(T object, LocalStorageRequestObjectListener<T> listener) throws Exception {
		ActiveCall call = new ActiveCall(object.getClass().getCanonicalName(), null, listener);
		_getClientLocalStorageItem(call);
	}
	
	protected <T extends Serializable> void getClientLocalStorageItemFromClazz(Class<T> clazz, LocalStorageRequestObjectListener<T> listener) throws Exception {
		ActiveCall call = new ActiveCall(clazz.getCanonicalName(), null, listener);
		_getClientLocalStorageItem(call);
	}
	
	
	
	
	private void _setClientLocalStorageItem(ActiveCall call, String value) throws Exception {
		
		if(call.getRequestKey()==null)
			throw new Exception("a key must be specified");
		
		if(call.getRequestKey().trim().isEmpty())
			throw new Exception("a key must be specified");
		
		if(call.getListener()!=null)
			getCalls().addLast(call);
		
		if(value!=null)
			JavaScript.getCurrent().execute(this_remoteSetAndRetrieveLocalStorage+"('"+call.getRequestKey()+"', '"+value+"');");
		else
			JavaScript.getCurrent().execute(this_remoteSetAndRetrieveLocalStorage+"('"+call.getRequestKey()+"', null);");
	}
	
	
	protected void setClientLocalStorageItem(String key, String value, LocalStorageRequestListener listener) throws Exception {
		ActiveCall call = new ActiveCall(key, null, listener);
		_setClientLocalStorageItem(call, value);
	}
	
	protected <T extends Serializable> void setClientLocalStorageItem(T object, LocalStorageRequestObjectListener<T> listener) throws Exception {
		ActiveCall call = new ActiveCall(object.getClass().getCanonicalName(), object.getClass(), listener);
		_setClientLocalStorageItem(call, new Gson().toJson(object));
	}
	
	protected void setClientLocalStorageItem(String key, String value) throws Exception {
		ActiveCall call = new ActiveCall(key, null, null);
		_setClientLocalStorageItem(call, value);
	}
	
	protected <T extends Serializable> void setClientLocalStorageItem(T object) throws Exception {
		setClientLocalStorageItem(object.getClass().getCanonicalName(), new Gson().toJson(object));
	}
	
	
	
	
	private void _removeClientLocalStorageItem(ActiveCall call) throws Exception {
		_setClientLocalStorageItem(call, null);
	}
	
	protected void removeClientLocalStorageItem(String key, LocalStorageRequestListener listener) throws Exception {
		ActiveCall call = new ActiveCall(key, null, listener);
		_removeClientLocalStorageItem(call);
	}
	
	protected <T extends Serializable> void removeClientLocalStorageItem(T object, LocalStorageRequestObjectListener<T> listener) throws Exception {
		ActiveCall call = new ActiveCall(object.getClass().getCanonicalName(), object.getClass(), listener);
		_removeClientLocalStorageItem(call);
	}
		
	protected <T extends Serializable> void removeClientLocalStorageItemFromClazz(Class<T> clazz, LocalStorageRequestObjectListener<T> listener) throws Exception {
		ActiveCall call = new ActiveCall(clazz.getCanonicalName(), clazz, listener);
		_removeClientLocalStorageItem(call);
	}
	
	protected void removeClientLocalStorageItem(String key) throws Exception {
		ActiveCall call = new ActiveCall(key, null, null);
		_setClientLocalStorageItem(call, null);
	}

	protected <T extends Serializable> void removeClientLocalStorageItem(T object) throws Exception {
		removeClientLocalStorageItem(object.getClass().getCanonicalName());
	}
		
	protected <T extends Serializable> void removeClientLocalStorageItemFromClazz(Class<T> clazz) throws Exception {
		removeClientLocalStorageItem(clazz.getCanonicalName());
	}
	
	
	
	protected void getRemoteOrigin(LocalStorageInfoListener listener) {
		ActiveCall call = new ActiveCall(null, null, listener);
		getCalls().addLast(call);
		JavaScript.getCurrent().execute(this_remoteGetRemoteOrigin+"();");
	}

}
