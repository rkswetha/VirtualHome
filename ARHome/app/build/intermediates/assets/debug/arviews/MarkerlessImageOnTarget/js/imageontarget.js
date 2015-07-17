
var World = {

	loaded: false,

	init: function initFn() {
		this.createOverlays();
		World.loaded = true; // Overlay is complete
	},

	createOverlays: function createOverlaysFn() {

		var image = new AR.ImageDrawable(new AR.ImageResource("assets/furniture1.png"), 1, {
                                  enabled: true,
                                  scale: 2,
                                  onClick: function(){
                                	  //alert("image selected");
                                 	  var div = document.getElementById('loadingMessage');
                            		    if (div.style.display !== 'none') {
                               			  div.style.display = 'none';
                            		    }
                            		    else {
                                		  div.style.display = 'block';
                            		    }
                                 	  current=this;
                                 	}
                        });

                		var location1 = new AR.RelativeLocation(null, 10, 0, 0);

                        var geoObject1 = new AR.GeoObject(location1, {
                                drawables: {
                                   cam: image
                                }
                        });

	},

	// reload places from content source
    captureScreen: function captureScreenFn() {
    	if (World.loaded) {
    			document.location = "architectsdk://button1?action=captureScreen";
    		}
    },

// reload places from content source
    displayProductInfo: function displayProductInfoFn() {
    	if (World.loaded) {
    			document.location = "architectsdk://button2?action=productInfo";
    		}
    },

	worldLoaded: function worldLoadedFn() {
			// Remove Scan target message after 10 sec.
    		setTimeout(function() {
    			var e = document.getElementById('loadingMessage1');
    			e.parentElement.removeChild(e);
    			var e = document.getElementById('loadingMessage2');
                    			e.parentElement.removeChild(e);
    		}, 10000);
	}
};

World.init();
