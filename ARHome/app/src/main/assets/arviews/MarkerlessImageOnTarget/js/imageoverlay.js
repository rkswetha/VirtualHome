
var current;


function scaleUp(){
	//alert("increasing image size");
	current.scale++;
};

function scaleDown(){
	//alert("decreasing image size");
	current.scale--;
};

function rotate(){
	//alert("rotating image size");
	current.rotation+=30;
};

function moveNorth(){
	//alert("moving north");
	location1.northing++;
};

function moveSouth(){
	//alert("moving south");
 	location1.northing--;
};

function moveEast(){
	//alert("moving east");
	location1.easting++;
};

function moveWest(){
	//alert("moving west");
	location1.easting--;
};

function moveHigher(){
	//alert("moving up");
	location1.altitudeDelta++;
};

function moveLower(){
	//alert("moving down");
	location1.altitudeDelta--;
};

function flip(){
	//alert("image flip");
	current.rotate.heading+=180;
};

function deleteObject(){
    var deleteconfirmation = confirm("Delete virtual object?");
    if (deleteconfirmation == true) {
       current.destroy();
    }
};

function showInfo(){
  AR.context.openInBrowser("http://www.ikea.com/us/en/catalog/products/00104291/");
  
}     

	
var image = new AR.ImageDrawable(new AR.ImageResource("assets/coffeetable.png"), 1, {
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
       

  
