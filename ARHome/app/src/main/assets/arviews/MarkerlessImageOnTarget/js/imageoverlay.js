var current = '';
var imageId= 0;
var imageWebsite = {};
var virtualObjectInfo = {};

$(function() {
     addImage("http://anushar.com/cmpe295Images/coffeetable.png");
    // addImage("http://anushar.com/cmpe295Images/sofa.png");
     setButtons();
});

function setButtons(){
      var plusIconElement = document.getElementById('plusIcon');
          plusIconElement.style.cursor = 'pointer';
          plusIconElement.onclick = function() {
              scaleUp2();
          };
      var minusIconElement = document.getElementById('minusIcon');
          minusIconElement.style.cursor = 'pointer';
          minusIconElement.onclick = function() {
              scaleDown2();
          };
      var flipIconElement = document.getElementById('flipIcon');
          flipIconElement.style.cursor = 'pointer';
          flipIconElement.onclick = function() {
              flipImage();
          };
      var deleteIconElement = document.getElementById('deleteIcon');
          deleteIconElement.style.cursor = 'pointer';
          deleteIconElement.onclick = function() {
              deleteObject();
          };
      var webIconElement = document.getElementById('rotateIcon');
          webIconElement.style.cursor = 'pointer';
          webIconElement.onclick = function() {
              rotate();
          };
      var cameraIconElement = document.getElementById('cameraIcon');
          cameraIconElement.style.cursor = 'pointer';
          cameraIconElement.onclick = function() {
              captureScreenFn();
          };
      var backgroundIconElement = document.getElementById('backgroundIcon');
          backgroundIconElement.style.cursor = 'pointer';
          backgroundIconElement.onclick = function() {
              setImage2();
          };
      var bringtofrontIconElement = document.getElementById('bringtofrontIcon');
          bringtofrontIconElement.style.cursor = 'pointer';
          bringtofrontIconElement.onclick = function() {
              bringToFront();
          };
     
};


function draggableObjects(){
      $( ".enableDrag" ).draggable({
                start: function(){
                  var getid = this.id;
                  current = getid;
                }
       });

};

function addImage(sourceUrl){
      imageId++
	  
	  //Canvas editing - To make the background transparent.
	  var img = new Image; 	  
	img.src = sourceUrl;
	
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
	ctx.putImageData(adjustImage(imgData), 0, 0);

	
	//New image:
		/*var imageMod=new Image();
		var imgU=doc.toDataURL();
		imageMod.src=imgU;				
		document.body.appendChild(img);
        document.body.appendChild(imageMod);*/
		
		
	  var x = new Image();
	  var imgU=doc.toDataURL();
     // x.setAttribute("src", sourceUrl);
	  x.setAttribute("src", imgU);
      x.setAttribute("width", '50%');
      x.setAttribute("height", 'auto');
      x.setAttribute("id", "virtualObject"+imageId);
      x.style.position='absolute';
      x.setAttribute("z-index", 1);
      x.classList.add("image");
      virtualObjectInfo["virtualObject"+imageId]={};
      virtualObjectInfo["virtualObject"+imageId]["scale"] = 1;
      virtualObjectInfo["virtualObject"+imageId]["source"] = imgU;
      virtualObjectInfo["virtualObject"+imageId]["zValue"] = 1;
      document.body.appendChild(x);
      document.getElementById("virtualObject"+imageId).className = "enableDrag";
      draggableObjects();
	  
	  
		 		
		};
     // var x = document.createElement("IMG");
	
};



function adjustImage(iArray) {
    var imageData = iArray.data;
	console.log("inside for loop");
    /*for (var i = 0; i < imageData.length; i+= 4) {
        if(imageData[i] === 255 && imageData[i+1] === 255 && imageData[i+2] === 255){
            imageData[i+3] = 0;
        }
    }*/

    for (var i = 0; i < imageData.length; i+= 4) {
		//console.log("inside for loop");
            if((imageData[i] >= 170 && imageData[i] <=  255) && (imageData[i+1] >= 170 && imageData[i+1] <=  255) && (imageData[i+2] >= 170 && imageData[i+2] <=  255)){
                imageData[i+3] = 0;
            }
        }
    return iArray;
};




var scaleIncrement = 0.2;
function scaleUp2(){
      if (current == ''){
        alert("Select virtual object");
      }
      else{
      var currentScaleValue = virtualObjectInfo[current]["scale"];
      newValue = currentScaleValue+scaleIncrement;
      imageSize(newValue);
      }
};

function scaleDown2(){
  if (current == ''){
        alert("Select virtual object");
      }
  else{
      var currentScaleValue = virtualObjectInfo[current]["scale"];
      if ((currentScaleValue-scaleIncrement)> scaleIncrement)
       {
        newValue = currentScaleValue-scaleIncrement;
        imageSize(newValue);
       }
     else{
        alert("Cannot decrease image size");
     }
    }
};

function imageSize(scaleValue){
    var x = document.getElementById(current);
    var changeWdth = x.width * scaleValue;
    var changelHt = x.height * scaleValue;
    x.height = changelHt;
    x.width = changeWdth;
    virtualObjectInfo[current]["scale"] = scaleValue;
};
/*
function showInfo(){
  if (current == ''){
        alert("Select virtual object");
      }
  else{
    var website =virtualObjectInfo[current]["source"];
    AR.context.openInBrowser(website);
  }
};
*/

var angle = 0;
function rotate(){
    var x = document.getElementById(current);
    angle = (angle+90)%360;
    x.className = "rotate"+angle;
}


function deleteObject(){
  if (current == ''){
        alert("Select virtual object");
      }
  else{
        var child = document.getElementById(current);
        var deleteconfirmation = confirm("Delete object?");
          if (deleteconfirmation == true) {
                   child.parentNode.removeChild(child);
                   delete virtualObjectInfo[current];
          }
  }
};

var flipValue = "off";
function flipImage(){
  if (current == ''){
        alert("Select virtual object");
      }
  else{
  var x = document.getElementById(current);
  if(flipValue == "off"){
        x.classList.add("img");
        flipValue = "on"
    }else{
        x.classList.remove("img");
        flipValue = "off"
    }
  }
};

var imgpath;
function setBackgroundImageUsingImagePath(url){
    imagepath = url;
    if (typeof(Storage) !== "undefined") {
        // Store
        localStorage.setItem("AndroidImagePath", url);
        }
}

var Usebackground = "off"
function setImage2(){
    var src;
    if(Usebackground == "off"){
      var bkgfromwebsite = prompt("Enter the source of the image");
      if (bkgfromwebsite != null) {
          src = bkgfromwebsite;
      }
      else
      {

          src = imagepath;
          //src = "assets/emptyRoom.png";
      }
      document.body.style.backgroundImage = "url("+src+")";
      document.body.style.backgroundRepeat = "no-repeat";
      document.body.style.backgroundSize = "cover";
      Usebackground = "on";
      }
      else{
      document.body.style.backgroundImage = "none";
      Usebackground = "off"
    }
};

function captureScreenFn() {
    document.location = "architectsdk://button1?action=captureScreen";
};

function bringToFront(){
    valueofz = virtualObjectInfo[current]["zValue"];
    valueofz+=1;
    var x = document.getElementById(current);
    x.setAttribute("src", virtualObjectInfo[current]["source"]);
    x.setAttribute('style', 'position: absolute; z-index:'+valueofz);
    virtualObjectInfo[current]["zValue"] = valueofz;
}
