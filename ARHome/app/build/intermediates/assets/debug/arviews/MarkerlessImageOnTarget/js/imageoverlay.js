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

    //addImage("http://anushar.com/cmpe295Images/coffeetable.png");
    setButtons();

    //draggableObjects();
});

function saveBeforeUnload() {
    //clearLSVirtualObjectInfo();
    // localStorage.setItem("VirtualObjectInfoLS", JSON.stringify(virtualObjectInfo);
    setLSVirtualObjectInfo(virtualObjectInfo);
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
                  saveBeforeUnload();
                }
       });
};

//var isloadedfromLS = false;
function loadVirtualObjectsFromLS(){
    var tempCount=0;
    //alert(Object.keys(virtualObjectInfo).length);
    //if (localStorage.getItem("VirtualObjectInfoLS") != null)
    var virtualObjectInfo2 = getLSVirtualObjectInfo();
    if (Object.keys(virtualObjectInfo2).length >0)
    {
        //alert("doing loadVirtualObjectsFromLS");
        //virtualObjectInfo = JSON.parse(getLSVirtualObjectInfo());
        //if ((Object.keys(virtualObjectInfo).length >0) && virtualObjectInfo != null )
        for (var key in virtualObjectInfo2) {
            //alert(key);
          var x = document.createElement("IMG");
          x.setAttribute("id", key);
          tempCount++;
          var obj = virtualObjectInfo2[key];
          x.setAttribute("src", obj["source"])
          x.style.position='absolute';
          x.setAttribute("z-index", obj["zValue"]);
          x.classList.add("image");
          //scale image according to storage
          var changeWdth = x.width * obj["scale"];
          var changeHt = x.height * obj["scale"];
          //x.height = changelHt;
          //x.width = changeWdth;
          x.setAttribute("width", changeWdth);
          x.setAttribute("height", changeHt);
          document.body.appendChild(x);
          x.style.left = obj["offsetLeftValue"] + "px";
          x.style.top = obj["offsetTopValue"] + "px";
          document.getElementById(key).className = "enableDrag";
        }

    }
    draggableObjects();
    alert("after loadVirtualObjectsFromLS and adding this many objects" + tempCount);
    imageId = tempCount;
    virtualObjectInfo = virtualObjectInfo2;
    //isloadedfromLS = true;
};


function addImage(sourceUrl){
      loadVirtualObjectsFromLS();
      //alert("after load");
      imageId++;
      //Canvas editing - To make the background transparent.
      var img = new Image;
      img.src = sourceUrl;

      var doc = document.createElement('canvas');
      doc.setAttribute("id", "document"+imageId);
      var ctx = doc.getContext("2d");


          // First create the image...
      img.onload = function(){
            //console.log("inside on load");
          // ...then set the onload handler...
            //console.log("ht "+img.height);
            //console.log("wt "+img.width);
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
            x.style.position='absolute';
            x.setAttribute("z-index", 1);
            x.classList.add("image");
            x.classList.add("enableDrag");
            virtualObjectInfo["virtualObject"+imageId]={};
            virtualObjectInfo["virtualObject"+imageId]["scale"] = 1;
            virtualObjectInfo["virtualObject"+imageId]["source"] = imgU;
            virtualObjectInfo["virtualObject"+imageId]["zValue"] = 2;
            document.body.appendChild(x);
            //document.getElementById("virtualObject"+imageId).className = "enableDrag";
            ot = document.getElementById("virtualObject"+imageId).offsetTop;
            ol = document.getElementById("virtualObject"+imageId).offsetLeft;
            virtualObjectInfo["virtualObject"+imageId]["offsetTopValue"] = ot;
            virtualObjectInfo["virtualObject"+imageId]["offsetLeftValue"] = ol;
            draggableObjects();
            //alert("done");
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
    saveBeforeUnload();
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
    //saveBeforeUnload();
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
            saveBeforeUnload();
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
    //saveBeforeUnload();
  }
};


function setLSVirtualObjectInfo(vo){
    if (typeof(Storage) !== "undefined") {
        localStorage.setItem("VirtualObjectInfoLS", JSON.stringify(vo));
    };
};

function getLSVirtualObjectInfo(){
   //alert(localStorage.getItem("VirtualObjectInfoLS"));
    var temp = localStorage.getItem("VirtualObjectInfoLS");
    return JSON.parse(temp);
};


function setLSAndroidImagePath(path){
    if (typeof(Storage) !== "undefined") {
        localStorage.setItem("AndroidImagePath", path);
    };
};

function getLSAndroidImagePath(){
    return localStorage.getItem("AndroidImagePath");
};

//ar imgpath;
function setBackgroundImageUsingImagePath(url){
    //imagepath = url;
    setLSAndroidImagePath(url);
};

var Usebackground = "off"
function setImage(){
    var src;
    src = getLSAndroidImagePath();
    if(Usebackground == "off"){
    var img = new Image();
    var orient;
    img.onload = function() {
       //alert(this.width +" and " + this.height);

       if (this.width > this.height)
            {
               orient = 1;
               var x = true;
               while(x){
                    if(window.orientation == 0){
                        alert("Switch to landscape mode");
                    }
                    else{
                        setbgk(src);
                        x = false;
                    }

                }

            }
        else{
                orient = 0;
                var x = true;
                while(x){
                     if(window.orientation != 0){
                          alert("Switch to portrait mode");
                     }
                     else{
                         setbgk(src);
                        x = false;
                     }

                 }
            }
        }
    img.src = src;
    Usebackground = "on";
     window.addEventListener('orientationchange', function(){
         switch(window.orientation)
             {
             case -90:
                //alert('landscape');
                if (orient == 0){
                    document.body.style.backgroundImage = "none";
                    Usebackground = "off"
                }
                break;
             case 90:
                //alert('landscape');
                if (orient == 0){
                     document.body.style.backgroundImage = "none";
                     Usebackground = "off"
                }
                break;
             default:
                //alert('portrait');
                if (orient == 1){
                    document.body.style.backgroundImage = "none";
                    Usebackground = "off"
                }
                break;
             }

       });

     }
    else{
      document.body.style.backgroundImage = "none";
      Usebackground = "off"
    }
};

function setbgk(imageSrc){

        document.body.background= imageSrc;
        document.body.style.backgroundSize = 'cover';
        document.body.style.backgroundRepeat = "no-repeat";

}

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
    //x.style.left= xpos + "px";
    x.style.left = ol + "px";
    x.style.top = ot + "px";
    saveBeforeUnload();
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

function clearLSVirtualObjectInfo(){
    localStorage.removeItem(VirtualObjectInfoLS);
    }
