Skip to content
This repository
Pull requests
Issues
Gist
 @RadhikaSNM
 Unwatch 3
  Star 0
  Fork 0
rkswetha/VirtualHome
Branch: master  VirtualHome/ARHome/app/src/main/assets/arviews/MarkerlessImageOnTarget/js/imageoverlay.js
@anusharavikumaranusharavikumar 11 days ago update delete functionality
2 contributors @rkswetha @anusharavikumar
RawBlameHistory     Executable File  184 lines (166 sloc)  5.284 kB
var current = '';
var imageId= 0;
var imageWebsite = {};
//var imageDescr = {}
$(function() {
     //addImage("http://www.endicottfurniture.com/uploads/6/0/6/7/6067323/8303341_orig.jpg");
     addImage("http://www.cheapwallarts.com/images/Orchard%20Apples.jpg");
     //addImage("http://www.cheapwallarts.com/images/Orchard%20Apples.jpg");
     setButtons();
     draggableObjects();
     //alert(current);
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
      var webIconElement = document.getElementById('webIcon');
          webIconElement.style.cursor = 'pointer';
          webIconElement.onclick = function() {
              showInfo();
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
};

function draggableObjects(){
      $( ".enableDrag" ).draggable({
                start: function(){
                  var getid = this.id;
                  current = getid;
                  //alert(current);
                }
       });
};

function addImage(sourceUrl){
      imageId++

      var x = document.createElement("IMG");
      x.setAttribute("src", sourceUrl);
      x.setAttribute("width", '30%');
      x.setAttribute("height", 'auto');
      x.setAttribute("id", "virtualObject"+imageId)
      document.body.appendChild(x);
      //$("virtualObject"+imageId).addClass("enableDrag");
      document.getElementById("virtualObject"+imageId).className = "enableDrag";
      //alert(document.getElementById("virtualObject"+imageId).className);
      imageWebsite["virtualObject"+imageId] = sourceUrl;
      //imageDescr["virtualObject"+imageId] = imageDescription;
      //alert(imageInfo["virtualObject"+imageId]);
      draggableObjects();
};

var scale = 1;
var scaleIncrement = 0.2;

function scaleUp2(){
      if (current == ''){
        alert("Select virtual object");
      }
      else{
      scale+=scaleIncrement;
      //alert(scale);
      imageSize(scale);
      }
};

function scaleDown2(){
  if (current == ''){
        alert("Select virtual object");
      }
  else{
     if ((scale-scaleIncrement)> scaleIncrement)
       {
        scale-=scaleIncrement;
        imageSize(scale);
       }
     else{
        alert("Cannot decrease image size");
     }
    }
};

function imageSize(scaleValue){
    var x = document.getElementById(current);
    //var img = document.getElementById(id);
    var changeWdth = x.width * scaleValue;
    var changelHt = x.height * scaleValue;
    //console.log(img.width, img.height);
    x.height = changelHt;
    x.width = changeWdth;
};


function showInfo(){
  if (current == ''){
        alert("Select virtual object");
      }
  else{
		var website = imageWebsite[current];
  	AR.context.openInBrowser(website);
  }
};

function deleteObject(){

  if (current == ''){
        alert("Select virtual object");
      }
  else{
      	var child = document.getElementById(current);
      	var deleteconfirmation = confirm("Delete object?");
          if (deleteconfirmation == true) {
                   child.parentNode.removeChild(child);
                   current = '';
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
	//currentvalue = document.getElementById("button4").value;
  if(flipValue == "off"){
  			x.classList.add("img");
    		//document.getElementById("button4").value="on";
        flipValue = "on"
    }else{
  			x.classList.remove("img");
   			//document.getElementById("button4").value="off";
        flipValue = "off"
  	}
  }
};

var Usebackground = "off"
function setImage2(){

    if(Usebackground == "off"){
      document.body.style.backgroundImage = "url('assets/emptyRoom.png')";
      document.body.style.backgroundRepeat = "no-repeat";
      document.body.style.backgroundSize = "cover";
      Usebackground = "on";
    } else{
      document.body.style.backgroundImage = "none";
      Usebackground = "off"
    }
};

function captureScreenFn() {
    document.location = "architectsdk://button1?action=captureScreen";
};

