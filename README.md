# Vaadin 8 localstorage manager

[![](https://jitpack.io/v/vidaniello/vaadin8-localstorage-manager.svg)](https://jitpack.io/#vidaniello/vaadin8-localstorage-manager)

Vaadin 8 component for client side javascript `window.localStorage` managing.

## Import in maven project

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
    ...
    <repositories>
	    <repository>
           <id>jitpack.io</id>
           <url>https://jitpack.io</url>
        </repository>
    </repositories>
    ...
    <dependencies>
    ...
    <dependency>
        <groupId>com.github.vidaniello</groupId>
        <artifactId>vaadin8-localstorage-manager</artifactId>
        <version>latest relase, check the top jitpack badge</version>
    </dependency>
    ...
    </dependencies>
    ...
</project> 
```

## Use

Work both with simple value, like `String`, or serializable objects.

The main class to take into consideration is [LocalStorage](https://github.com/vidaniello/vaadin8-localstorage-manager/blob/main/src/main/java/com/github/vidaniello/vaadin8/localstoragemanager/LocalStorage.java "com.github.vidaniello.vaadin8.localstoragemanager.LocalStorage") class

A simple POJO class with default init:
```java
public class SimplePersistentObject implements Serializable {
    
    private String name;
    private List<String> addresses;
    
    public SimplePersistentObject(){
        name = "DefaultName";
        addresses = new ArrayList<>();
        addresses.add("0x19E7E376E7C213B7E7e7e46cc70A5dD086DAff2A");
        addresses.add("0x8735015837bD10e05d9cf5EA43A2486Bf4Be156F");
    }
    
     //getters and setters or other logics...
}
```
In a vaadin 8 UI init method or in any vaadin 8 component onAttach event:
```java
public class ACustomComponent extends com.vaadin.ui.CustomComponent{
....
    private LocalStorage lsc;
....
    public ACustomComponent(){
        addAttachListener(this::onAttach);
    }
....
    private void onAttach(AttachEvent evt) {
        lsc = new LocalStorage();
        
        try {
            lsc.getClientLocalStorageItemFromClazz(SimplePersistentObject.class, persistentObject->{
                try {
                    if(persistentObject==null){
                        SimplePersistentObject spo = new SimplePersistentObject();
                        lsc.setClientLocalStorageItem(spo, retObj->{
                            Notification.show("Simple persistent object persisted in localstorage of client.");
                        });
                     }
                } catch (Exception e) {
                    Notification.show(e.getClass().getSimpleName(), e.getMessage(), Type.ERROR_MESSAGE);
                }
            });
        } catch (Exception e) {
            Notification.show(e.getClass().getSimpleName(), e.getMessage(), Type.ERROR_MESSAGE);
        }
    }
....
```
