
try{
	if(staticStorageUI==null);
	//Here code if staticStorageUI exist
} catch(error){

	//First initialization code

	staticStorageUI = {

		localstorage_setAndRetrieve:  function (key, value){

			if(key!==undefined)
		    	if(key!==null)
					if(key!==''){
		
						if(value!==undefined)
		            		if(value===null){
		                		window.localStorage.removeItem(key);
		                        return null;
		                	} else
								window.localStorage.setItem(key, value);
		
						return window.localStorage.getItem(key);
					}
		
			return null;
		}
	};

}

