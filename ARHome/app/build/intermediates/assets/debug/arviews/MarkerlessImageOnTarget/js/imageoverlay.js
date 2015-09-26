var current = '';
var imageId= 0;
var virtualObjectInfo = {};
/* DATA STRUCTURE
    "VirtualObjectId": {
      "src": value,
      "scale": value,
      "zValue": value,
      "offsetTopValue": value,
      "offsetLeftValue": value
    },
    "VirtualObjectId": {
    }
*/

$(function() {
    
     addImage("http://anushar.com/cmpe295Images/coffeetable.png");
    // addImage("http://anushar.com/cmpe295Images/sofa.png");
     setButtons();

});

window.onbeforeunload = saveState();
function saveState(){
        setLSVirtualObjectInfo(virtualObjectInfo);
        return 'Are you sure you want to leave?';
}

function setButtons(){
      var plusIconElement = document.getElementById('plusIcon');
          plusIconElement.style.cursor = 'pointer';
          plusIconElement.onclick = function() {
              scaleUp();
          };
      var minusIconElement = document.getElementById('minusIcon');
          minusIconElement.style.cursor = 'pointer';
          minusIconElement.onclick = function() {
              scaleDown();
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
      var cameraIconElement = document.getElementById('snapshot');
          cameraIconElement.style.cursor = 'pointer';
          cameraIconElement.onclick = function() {
              captureScreenFn();
          };
      var backgroundIconElement = document.getElementById('backgroundIcon');
          backgroundIconElement.style.cursor = 'pointer';
          backgroundIconElement.onclick = function() {
              setImage();
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
                },
                stop: function(){
                  getImagePosition(event);
                }
       });
};

var isloadedfromLS = false;
function loadVirtualObjectsFromLS(){
    var tempCount=0;
    //alert(Object.keys(virtualObjectInfo).length);
    //if (localStorage.getItem("VirtualObjectInfoLS") != null)
    if (Object.keys(virtualObjectInfo).length >0)
    {
        virtualObjectInfo = getLSVirtualObjectInfo();
        //if ((Object.keys(virtualObjectInfo).length >0) && virtualObjectInfo != null )
        for (var key in virtualObjectInfo) {
          var x = document.createElement("IMG");
          x.setAttribute("id", obj);
          tempCount++;
          var obj = validation_messages[key];
          x.setAttribute("src", obj["source"])
          x.style.position='relative';
          x.setAttribute("z-index", obj["zValue"]);
          x.classList.add("image");
          x.style.left = obj["offsetLeftValue"] + "px";
          x.style.top = obj["offsetTopValue"] + "px";
          //scale image accorind to storage
          var changeWdth = x.width * obj["scale"];
          var changelHt = x.height * obj["scale"];
          //x.height = changelHt;
          //x.width = changeWdth;
          x.setAttribute("width", changeWdth);
          x.setAttribute("height", changelHt);
          document.body.appendChild(x);
          document.getElementById(key).className = "enableDrag";
        }
        draggableObjects();     
    }
    imageId = tempCount; 
    isloadedfromLS = true;
};
  

function addImage(sourceUrl){
alert("inside add image")
      if (isloadedfromLS == false)
      {
      loadVirtualObjectsFromLS();
      }
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

            //var x = document.createElement("IMG");
            x.setAttribute("src", imgU);
            //x.setAttribute("src", sourceUrl);
            x.setAttribute("width", '50%');
            x.setAttribute("height", 'auto');
            x.setAttribute("id", "virtualObject"+imageId);
            x.style.position='relative';
            x.setAttribute("z-index", 1);
            x.classList.add("image");
            virtualObjectInfo["virtualObject"+imageId]={};
            virtualObjectInfo["virtualObject"+imageId]["scale"] = 1;
            virtualObjectInfo["virtualObject"+imageId]["source"] = imgU;
            virtualObjectInfo["virtualObject"+imageId]["zValue"] = 2;
            document.body.appendChild(x);
            document.getElementById("virtualObject"+imageId).className = "enableDrag";
            ot = document.getElementById("virtualObject"+imageId).offsetTop;
            ol = document.getElementById("virtualObject"+imageId).offsetLeft;
            virtualObjectInfo["virtualObject"+imageId]["offsetTopValue"] = ot;
            virtualObjectInfo["virtualObject"+imageId]["offsetLeftValue"] = ol;
            draggableObjects();
            //alert(Object.keys(virtualObjectInfo).length);
            }
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
function scaleUp(){
    if (current == ''){
        alert("Select virtual object");
    }
    else{
        var currentScaleValue = virtualObjectInfo[current]["scale"];
        newValue = currentScaleValue+scaleIncrement;
        imageSize(newValue);
    }
};

function scaleDown(){
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
  if (current == ''){
        alert("Select virtual object");
  }
  else{
    var x = document.getElementById(current);
    angle = (angle+90)%360;
    x.className = "rotate"+angle;
    //x.classList.add("rotate"+angle);
    //alert(x.classList);
  }
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
        //x.classList.add("img");
        x.className = "img";
        flipValue = "on"
    }
    else{
        x.classList.remove("img");
        flipValue = "off"
    }
  }
};


function setLSVirtualObjectInfo(vo){
    if (typeof(Storage) !== "undefined") {
        localStorage.setItem("VirtualObjectInfoLS", vo);
    };
};

function getLSVirtualObjectInfo(){
   //alert(localStorage.getItem("VirtualObjectInfoLS"));
    return localStorage.getItem("VirtualObjectInfoLS");
};


function setLSAndroidImagePath(path){
    if (typeof(Storage) !== "undefined") {
        localStorage.setItem("AndroidImagePath", path);
    };
};

function getLSAndroidImagePath(){
    return localStorage.getItem("AndroidImagePath");
};

var imgpath;
function setBackgroundImageUsingImagePath(url){
    imagepath = url;
    /*if (typeof(Storage) !== "undefined") {
        // Store
        localStorage.setItem("AndroidImagePath", url);
     }*/
    setLSAndroidImagePath(url);
};

var Usebackground = "off"
function setImage(){
    var src;
    src = getLSAndroidImagePath();
    if(Usebackground == "off"){
        if (src == null){
            alert("choose image from Android Gallery");
        }else {
            /*
           var z = new Image();
           z.src = src;
           document.body.background=z.src;
*/

           //var z = new Image();
           //z.setAttribute("src", src);
           //z.classList.add("rotate90");
           //document.body.background=z.src;


        //z.style.top  =
        //z.setAttribute("width", "100%");
        //z.setAttribute("height", "100%");
        //z.style.top = 0+"px";
        //z.style.left = 0+"px"
        //z.style.position='absolute';
        var x = document.createElement("IMG");
        x.setAttribute("id", "bkgimage");
        x.setAttribute("src", src);
        x.classList.add("rot90");
        x.setAttribute("width", window.innerHeight);
        x.setAttribute("height", window.innerWidth);
        x.style.top = "0px";
        x.style.left = "0px";

        x.style.position = "fixed"
        x.setAttribute("z-index", -1);
        //document.body.background=x.src;
        //document.body.background=x.src;
        //angle = (0+90)%360;
        //z.className = "rotate"+angle;

       // }
        document.body.appendChild(x);

        //document.body.style.backgroundImage = "url("+src+")";
        //document.body.style.backgroundRepeat = "no-repeat";
        //document.body.style.backgroundSize = "cover";
        //document.body.style.backgroundSize = '100% 100%';
        //AR.hardware.camera.enable = false;
        Usebackground = "on";
        }
     }
    else{
     var child = document.getElementById("bkgimage");
        child.parentNode.removeChild(child);
      //document.body.style.backgroundImage = "none";
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
    x.setAttribute('style', 'position: relative; z-index:'+valueofz);
    virtualObjectInfo[current]["zValue"] = valueofz;
    //x.style.left= xpos + "px";
    x.style.left = ol + "px";
    x.style.top = ot + "px";
};


var ot, ol;
function getImagePosition(event) {
    //xpos = event.clientX;
    //ypos = event.clientY;
    //var coords = "X coords: " + xpos + ", Y coords: " + ypos;
    //alert(coords);
    ot = document.getElementById(current).offsetTop;
    ol = document.getElementById(current).offsetLeft;
    virtualObjectInfo[current]["offsetTopValue"] = ot;
    virtualObjectInfo[current]["offsetLeftValue"] = ol;
    //alert(ot);
    //alert(virtualObjectInfo[current]["offsetTopValue"]);
};
