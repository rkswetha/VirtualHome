var overlayOne;
var imagePath;

var World = {

	loaded: false,

	init: function initFn() {

		//this.createOverlays();
		World.loaded = true; // Overlay is complete
	},

 	// called to read user selected image path
	readImagePath: function readImagePathFn(readPath) {

		imagePath = decodeURIComponent(readPath);

        alert(imagePath);
        this.createOverlays();
	},


	createOverlays: function createOverlaysFn() {
		/*
			First an AR.ClientTracker needs to be created in order to start the recognition engine. It is initialized with a URL specific to the target collection. Optional parameters are passed as object in the last argument. In this case a callback function for the onLoaded trigger is set. Once the tracker is fully loaded the function worldLoaded() is called.
			Important: If you replace the tracker file with your own, make sure to change the target name accordingly.
			Use a specific target name to respond only to a certain target or use a wildcard to respond to any or a certain group of targets.
		*/
		this.tracker = new AR.ClientTracker("assets/virtualhometarget.wtc", {
			onLoaded: this.worldLoaded,
			physicalTargetImageHeights: {
                      pageOne:    268
            }
		});

		/*
			The next step is to create the augmentation. In this example an image resource is created and passed to the AR.ImageDrawable.
			A drawable is a visual component that can be connected to an IR target (AR.Trackable2DObject) or a geolocated object (AR.GeoObject).
			The AR.ImageDrawable is initialized by the image and its size. Optional parameters allow for position it relative to the recognized target.
		*/

		alert(imagePath);
		/* Create overlay for page one */
		//var imgOne = new AR.ImageResource(decodeURIComponent("http%3A%2F%2Fwww.ikea.com%2Fca%2Fen%2Fimages%2Fproducts%2Fkivik-loveseat-and-chaise-lilac__0252355_PE391172_S4.JPG"));
		var imgOne = new AR.ImageResource(decodeURIComponent(imagePath));
        overlayOne = new AR.ImageDrawable(imgOne, 1, {
			enabled:true,
			offsetX: -0.15,
			offsetY: 0
		});


		/*	The last line combines everything by creating an AR.Trackable2DObject with the previously created tracker, the name of the image target and the drawable that should augment the recognized image.
			Please note that in this case the target name is a wildcard. Wildcards can be used to respond to any target defined in the target collection. If you want to respond to a certain target only for a particular AR.Trackable2DObject simply provide the target name as specified in the target collection.
		*/
		var pageOne = new AR.Trackable2DObject(this.tracker, "*", {
			drawables: {
				cam: overlayOne
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
	},

	scaleUp: function scaleUpFn(){
		console.log(overlayOne.scale);
		if(overlayOne.scale >= 5) // Dont support over scaling of image
		   	overlayOne.scale = 5;
		else
			overlayOne.scale++;
    },

    scaleDown: function scaleDownFn(){
        if(overlayOne.scale == 0)
        	overlayOne.scale = 0;
        else
        	overlayOne.scale--;
    },

    rotate: function rotateFn(){
        overlayOne.rotation+=30;
    }

};

World.init();

