var overlayOne;
var imagePath;
var recommendedUrls = [];
var arrayPointer = 0;
var text = {
  "results":[
    {
      "name": "KNISLINGE",
      "description": "Width: 80 3/4 , Depth: 35 , Height under furniture: 3 1/8 Width: 205 cm, Depth: 89 cm, Height under furniture: 8 cm \n Product description \n Loveseat frame:\n Back and seat frame: Fiberboard, Moisture resistant particleboard, Plywood, Solid wood\n Armrest frame: Solid wood, Fiberboard, Moisture resistant particleboard, Plywood\n Seat cushion: High-resilience polyurethane foam (cold foam) 2.2 lb/cu.ft., Polyurethane memory foam 3.1 lb/cu.ft, Polyester wadding \n Back cushion: Polyester fiber balls",
      "price": "$299.00",
      "url": "http://res.cloudinary.com/cmpe295b/image/upload/c_fit,h_500,w_500/100.PNG"
    },
    {
      "description": "Product dimensions \n Max. width: 110 1/4 '\n Min. depth: 37 3/8 ' \n Max. depth: 64 1/8 ' \n Height: 32 5/8 '\n Min. seat depth: 23 5/8 \n Max. seat depth: 48 7/8 ' \n Seat height: 17 3/4 \n Product description \n Loveseat frame:\n Back and seat frame: Fiberboard, Moisture resistant particleboard, Plywood, Solid wood\n Armrest frame: Solid wood, Fiberboard, Moisture resistant particleboard, Plywood\n Seat cushion: High-resilience polyurethane foam (cold foam) 2.2 lb/cu.ft., Polyurethane memory foam 3.1 lb/cu.ft, Polyester wadding \n Back cushion: Polyester fiber balls" ,
      "name": "TIMSFORS2",
      "price": "$1,999.00",
      "url": "http://res.cloudinary.com/cmpe295b/image/upload/c_fit,h_500,w_500/101.PNG"
    },
    {
      "description": "Product dimensions \n Max. width: 110 1/4 '\n Min. depth: 37 3/8 ' \n Max. depth: 64 1/8 ' \n Height: 32 5/8 '\n Min. seat depth: 23 5/8 \n Max. seat depth: 48 7/8 ' \n Seat height: 17 3/4 \n Product description \n Loveseat frame:\n Back and seat frame: Fiberboard, Moisture resistant particleboard, Plywood, Solid wood\n Armrest frame: Solid wood, Fiberboard, Moisture resistant particleboard, Plywood\n Seat cushion: High-resilience polyurethane foam (cold foam) 2.2 lb/cu.ft., Polyurethane memory foam 3.1 lb/cu.ft, Polyester wadding \n Back cushion: Polyester fiber balls" ,
      "name": "KLIPPAN",
      "price": "$399",
      "url": "http://res.cloudinary.com/cmpe295b/image/upload/c_fit,h_500,w_500/105.PNG"
    }
  ]
};

var World = {

	loaded: false,

	init: function initFn() {

		World.loaded = true; // Overlay is complete
	},

 	// called to read user selected image path
	readImagePath: function readImagePathFn(readPath) {

		imagePath = decodeURIComponent(readPath);
        this.createOverlays();
	},


	createOverlays: function createOverlaysFn() {
		//this.changeToTransparent();
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

		/* Create overlay for page one */
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
			},
			snapToScreen: {
            				snapContainer: document.getElementById('snapContainer')
            			},
            onEnterFieldOfVision: this.appear,
            onExitFieldOfVision: this.disappear
		});

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
		if(overlayOne.scale >= 5) // Don't support over scaling of image
		   	overlayOne.scale = 5;
		else
			overlayOne.scale++;
    },

    scaleDown: function scaleDownFn(){

    	console.log(overlayOne.scale);
        if(overlayOne.scale <= 1)
        	overlayOne.scale = 1;
        else
        	overlayOne.scale--;
    },

    rotate: function rotateFn(){
        overlayOne.rotation+=30;
    },

	changeToTransparent: function changeToTransparentFn(){

	//Canvas editing - To make the background transparent.
          var img = new Image;
          var imageId=1;

          img.src = "assets/furniture1.png";

          var doc = document.createElement('canvas');
          doc.setAttribute("id", "document"+imageId);
          var ctx = doc.getContext("2d");


              // First create the image...
          img.onload = function(){
                console.log("inside on load");
              // ...then set the onload handler...
                console.log("ht "+img.height);
                console.log("wt "+img.width);
                doc.width=img.width;
                doc.height=img.height;
                ctx.drawImage(img,0,0,img.width,img.height);
                var imgData = ctx.getImageData(0, 0, img.width,img.height);

                var imageData = imgData.data;
                      console.log("inside for loop");

                for (var i = 0; i < imageData.length; i+= 4) {
                        //console.log("inside for loop");
                                if((imageData[i] >= 170 && imageData[i] <=  255) && (imageData[i+1] >= 170 && imageData[i+1] <=  255) && (imageData[i+2] >= 170 && imageData[i+2] <=  255)){
                                    imageData[i+3] = 0;
                                }
                }
				console.log(imageData);
				console.log(imgData);
				imgData.data = imageData;

                ctx.putImageData(imgData, 0, 0);
                var x = new Image();
                var imgU=doc.toDataURL();
                console.log(imgU);

                x.setAttribute("src", imgU);
                //x.setAttribute("src", sourceUrl);
                x.setAttribute("width", '50%');
                x.setAttribute("height", 'auto');
                x.setAttribute("id", "virtualObject"+imageId);
                x.style.position='relative';
                x.setAttribute("z-index", 1);
                x.classList.add("image");

                document.body.appendChild(x);
			}
	},

	chooseAnotherImage: function chooseAnotherImagesFn(readPath) {

		window.overlayOne.destroy();

		this.tracker = new AR.ClientTracker("assets/virtualhometarget.wtc", {
			onLoaded: this.worldLoaded,
			physicalTargetImageHeights: {
                      pageTwo:    268
            }
		});


		var imgTwo = new AR.ImageResource(decodeURIComponent(readPath));
		overlayOne = new AR.ImageDrawable(imgTwo, 1, {
			offsetX: -0.15,
			offsetY: 0
		});

		/*
			The AR.Trackable2DObject for the second page uses the same tracker but with a different target name and the second overlay.
		*/
		var pageTwo = new AR.Trackable2DObject(this.tracker, "*", {
			drawables: {
				cam: overlayOne
			}
		});
    },

    displaySnapToScreen: function displaySnapToScreenFn(){

    },

    // Thumbnail Recommendations START
    getRecommendedProducts: function getRecommendedProducts(jsonInput){
        var input = jsonInput;
        for(var items in input["results"]) {
    	    recommendedUrls.push(input["results"][items]["url"]);
        };
    },


    showDataMiningThumbnail: function showDataMiningThumbnail(){

        var elem = document.getElementById("thumbnails");
        var d1 = document.getElementById("datamining1");
        var d2 = document.getElementById("datamining2");
        var d3 = document.getElementById("datamining3");

        if (elem.style.display == "none"){
            d1.src = recommendedUrls[arrayPointer];
            arrayPointer++;

            d2.src = recommendedUrls[arrayPointer];
            arrayPointer++;

            d3.src = recommendedUrls[arrayPointer];
            arrayPointer++;

            d1.addEventListener('click', function () {
                    World.chooseAnotherImage(d1.src);
                    alert("Please rescan target");
                    //d1.src = recommendedUrls[arrayPointer];
                    //World.arrayIncrement();
                });

                d2.addEventListener('click', function () {
                    World.chooseAnotherImage(d2.src);
                    alert("Please rescan target");
                    //d2.src = recommendedUrls[arrayPointer];
                    //World.arrayIncrement();
                });

                d3.addEventListener('click', function () {
                    World.chooseAnotherImage(d3.src);
                    alert("Please rescan target");
                    //d3.src = recommendedUrls[arrayPointer];
                    //World.arrayIncrement();
                });


            elem.style.display = 'inline-block';
        }else
        {
            elem.style.display = 'none';
        }
    }
    // Thumbnail Recommendations END

};

World.init();
World.getRecommendedProducts(text);