var current = '';
var imageId= 0;
var imageWebsite = {};
var virtualObjectInfo = {};

$(function() {
   
     addImage("http://anushar.com/cmpe295Images/coffeetable.png");
     addImage("http://anushar.com/cmpe295Images/sofa.png");
     setButtons();
     draggableObjects();
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
      var bringtofrontIconElement = document.getElementById('bringtofrontIcon');
          bringtofrontIconElement.style.cursor = 'pointer';
          bringtofrontIconElement.onclick = function() {
              bringToFront();
          };
     
};

var xposofcurrent, yposofcurrent;
var xposofcurrent2, yposofcurrent2;
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

      var x = document.createElement("IMG");
      x.setAttribute("src", sourceUrl);
      x.setAttribute("width", '30%');
      x.setAttribute("height", 'auto');
      x.setAttribute("id", "virtualObject"+imageId);
      x.style.position='absolute';
      x.setAttribute("z-index", 1);
      virtualObjectInfo["virtualObject"+imageId]={};
      virtualObjectInfo["virtualObject"+imageId]["scale"] = 1;
      virtualObjectInfo["virtualObject"+imageId]["source"] = sourceUrl;
      virtualObjectInfo["virtualObject"+imageId]["zValue"] = 1;
      document.body.appendChild(x);
      document.getElementById("virtualObject"+imageId).className = "enableDrag";
      draggableObjects();
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

function showInfo(){
  if (current == ''){
        alert("Select virtual object");
      }
  else{
    var website =virtualObjectInfo[current]["source"];
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
          src = "assets/emptyRoom.png";
      }
      document.body.style.backgroundImage = "url("+src+")";
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

function bringToFront(){
    valueofz = virtualObjectInfo[current]["zValue"];
    valueofz+=1;
    var x = document.getElementById(current);
    x.setAttribute("src", virtualObjectInfo[current]["source"]);
    x.setAttribute('style', 'position: absolute; z-index:'+valueofz);
    virtualObjectInfo[current]["zValue"] = valueofz;
}