var current = '';
var imageId= 0;
var virtualObjectInfo = {};
var recommendedUrls = [];
/* DATA STRUCTURE
    "VirtualObjectId": {
      "src": value,
      "zValue": value,
      "width":value,
      "height":value,
      "offsetTopValue": value,
      "offsetLeftValue": value
    }
*/

$(function() {
    setButtons();
    draggableObjects();
    /*getRecommendedProducts("http://res.cloudinary.com/cmpe295b/image/upload/c_fit,h_500,w_500/116.PNG",
                  "http://res.cloudinary.com/cmpe295b/image/upload/c_fit,h_500,w_500/brusaliik.PNG",
                  "http://res.cloudinary.com/cmpe295b/image/upload/c_fit,h_500,w_500/135.PNG"); */

});

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

      var recommendationIconElement = document.getElementById('recommendationIcon');
          recommendationIconElement.style.cursor = 'pointer';
          recommendationIconElement.onclick = function() {
               showDataMiningThumbnail();
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

function updateImageElements(){
    var temp = virtualObjectInfo;
    var virtualObjectInfo2 = getLSVirtualObjectInfo();
    var imgs = document.getElementsByClassName("enableDrag");
    if ((Object.keys(virtualObjectInfo2).length >0) && imgs.length>0 ) {
        var imgSrcs = [];

        for (var i = 0; i < imgs.length; i++) {
            imgSrcs.push(imgs[i].id);
        };

        for (var x=0; x<imgSrcs.length; x++){
            var k = imgSrcs[x];
            if (virtualObjectInfo2.hasOwnProperty(k)){
                var e = document.getElementById(k);
                var obj = virtualObjectInfo2[k];
                e.setAttribute("z-index", obj["zValue"]);
                e.setAttribute("width", obj["width"]);
                e.setAttribute("height", obj["height"]);
                e.style.left = obj["offsetLeftValue"] + "px";
                e.style.top = obj["offsetTopValue"] + "px";
                };
        };
        virtualObjectInfo = virtualObjectInfo2;
        //console.log("TEST: Restore Element from Saved State: " + assert(virtualObjectInfo2 == temp));
    }
};


function getRecommendedProducts(url1, url2, url3){
    recommendedUrls.push(url1);
    recommendedUrls.push(url2);
    recommendedUrls.push(url3);

    showDataMiningThumbnail();
};

function showDataMiningThumbnail(){

    var elem = document.getElementById("thumbnails");
    var d1 = document.getElementById("datamining1");
    var d2 = document.getElementById("datamining2");
    var d3 = document.getElementById("datamining3");
    //var d4 = document.getElementById("datamining4");

    if (elem.style.display == "none"){
        if (recommendedUrls[0] != null){
            d1.src = recommendedUrls[0];
            d2.src = recommendedUrls[1];
            d3.src = recommendedUrls[2];
        } else
        {
            d1.src = "";
            d2.src = "";
            d3.src = "";
        }
        elem.style.display = 'inline-block';
    }else
    {
        elem.style.display = 'none';
    }
};

function addThumbnailImage(inputSrc){
    if (inputSrc != ""){
        addImage(inputSrc);
    }
}

function addImage(sourceUrl){

        //if (document.getElementsByClassName("enableDrag").length>=1){
        //updateImageElements();
        //};
        imageId++;
        var x = new Image();
        x.setAttribute("src", sourceUrl);
        x.setAttribute("width", '50%');
        x.setAttribute("height", 'auto');
        x.setAttribute("id", "virtualObject"+imageId);
        x.style.position='absolute';
        x.setAttribute("z-index", 1);
        x.classList.add("image");
        x.classList.add("enableDrag");
        virtualObjectInfo["virtualObject"+imageId]={};
        virtualObjectInfo["virtualObject"+imageId]["source"] = sourceUrl;
        virtualObjectInfo["virtualObject"+imageId]["zValue"] = 2;
        virtualObjectInfo["virtualObject"+imageId]["width"] = x.width;
        virtualObjectInfo["virtualObject"+imageId]["height"] = x.height;
        document.body.appendChild(x);
        // console.log("TEST: AddImage function converting image transparency: " + assert(sourceUrl != imgU));
        // console.log("TEST: AddImage function inserted new image to data structure: " + assert(virtualObjectInfo.hasOwnProperty("virtualObject"+imageId)));
        // console.log("TEST: AddImage function inserted image to DOM: " + assert(document.getElementById("virtualObject"+imageId).hasAttribute("id")));
        ot = document.getElementById("virtualObject"+imageId).offsetTop;
        ol = document.getElementById("virtualObject"+imageId).offsetLeft;
        virtualObjectInfo["virtualObject"+imageId]["offsetTopValue"] = ot;
        virtualObjectInfo["virtualObject"+imageId]["offsetLeftValue"] = ol;
        draggableObjects();
};


var scaleIncrement = 0.2;
function scaleUp(){
    if (current == ''){
        alert("Please choose image");
        var testresult = assert(current == '');
        //console.log("TEST: Increase Image Size When Image not selected results in no change = " + testresult);
    }
    else{
       var x = document.getElementById(current);
       var beforeWidth = x.width;
       var beforeHeight = x.height;
       x.width = x.width + x.width*scaleIncrement;
       x.height = x.height + x.height*scaleIncrement;
       //console.log("TEST: Decrease image size on valid input: "+ assert((beforeWidth<x.width) && (beforeHeight<x.height)));
       virtualObjectInfo[current]["width"] = x.width;
       virtualObjectInfo[current]["height"] =  x.height;
       saveBeforeUnload();
    }
};


function scaleDown(){
  if (current == ''){
        alert("Please choose image");
        var testresult = assert(current == '');
        //console.log("TEST: Decrease Image when Image not selected results in no change = " + testresult);
      }
  else{
     var x = document.getElementById(current);
     var beforeWidth = x.width;
     var beforeHeight = x.height;
     x.width = x.width - x.width*scaleIncrement;
     x.height = x.height - x.height*scaleIncrement;
     //console.log("TEST: Decrease image size on valid input: "+ assert((beforeWidth>x.width) && (beforeHeight>x.height)));
     virtualObjectInfo[current]["width"] = x.width;
     virtualObjectInfo[current]["height"] =  x.height;
     saveBeforeUnload();
    }
};

var angle = 0;
function rotate(){
  if (current == ''){
        alert("Please choose image");
        var testresult = assert(angle==0);
        //console.log("TEST: Rotate Image not selected results in no change = " + testresult);
  }
  else{
    var x = document.getElementById(current);
    angle = (angle+90)%360;
    x.className = "rotate"+angle;
    var testresult = assert(angle>0);
    //console.log("TEST: Rotate Image Angle Changed = " + testresult);
  }
};

function deleteObject(){
  var itemsInDataStructure = Object.keys(virtualObjectInfo).length;
  if (current == ''){
        alert("Please choose image");
        var testresult = assert(itemsInDataStructure==itemsInDataStructure);
        //console.log("TEST: Delete Image When Image not selected results in no change = " + testresult);
  }
  else{
        var child = document.getElementById(current);
        var deleteconfirmation = confirm("Delete object?");
        if (deleteconfirmation == true) {
            child.parentNode.removeChild(child);
            delete virtualObjectInfo[current];
            //console.log("TEST: Delete selected image results in deletion from DOM = " + assert(!document.getElementById(current).hasAttribute("id")));
            saveBeforeUnload();
            current = '';
            //console.log("TEST: Delete selected image results in deletion from Data Structure = " + assert(itemsInDataStructure< Object.keys(virtualObjectInfo).length));
            //console.log("TEST: Delete selected image results in Image Unselected = " + assert(current == ''));
        }
  }
};

var flipValue = "off";
function flipImage(){
  if (current == ''){
        alert("Please choose image");
        //console.log("TEST: Flip Image When Image not Selected Results in no change = " + assert(current == ''));
  }
  else{
    var x = document.getElementById(current);
    if(flipValue == "off"){
        x.className = "img";
        flipValue = "on";
        //console.log("TEST: Flip Image modifications applied = " + assert(x.className == "img"));
    }
    else{
        x.classList.remove("img");
        flipValue = "off";
        //console.log("TEST: Flip Image modifications applied = " + assert(x.className != "img"));
    }
  }
};

function saveBeforeUnload() {
    if (typeof(Storage) != "undefined") {
        localStorage.setItem("VirtualObjectInfoLS", JSON.stringify(virtualObjectInfo));
        //console.log("TEST: Local Storage Updated with Data Structure = " + assert(Object.keys(getLSVirtualObjectInfo()).length == Object.keys(virtualObjectInfo).length));
    };
};

function getLSVirtualObjectInfo(){
    var temp = localStorage.getItem("VirtualObjectInfoLS");
    if (temp === null){
        temp = {};
    }
    return JSON.parse(temp);
};

function getLSAndroidImagePath(){
    if (localStorage.getItem("AndroidImagePath") == null){
        //alert("Choose image from Android Gallery");
        return null;
    } else{
        return localStorage.getItem("AndroidImagePath");
    };
};
var Usebackground = "off";

function setBackgroundImageUsingImagePath(url){
    if (typeof(Storage) != "undefined") {
        localStorage.setItem("AndroidImagePath", url);
        //console.log("TEST: Local Storage Updated with Android Gallery Image Path = " + assert(localStorage.getItem("AndroidImagePath")));
    };
    document.body.background= url;
    document.body.style.backgroundSize = 'cover';
    document.body.style.backgroundRepeat = "no-repeat";
    Usebackground = "on";
};


function setImage(){
    var src;
    src = getLSAndroidImagePath();
    if (src){
      //console.log("TEST: Background Image = " + assert(src));
      if(Usebackground == "off"){
         document.body.background= src;
         document.body.style.backgroundSize = 'cover';
         document.body.style.backgroundRepeat = "no-repeat";
         Usebackground = "on";
       }
      else{
        document.body.style.backgroundImage = "none";
        Usebackground = "off";
      }
    } else
    {
        alert("Please Choose Image from Android Gallery");
    }
};

function captureScreenFn() {
    document.location = "architectsdk://button1?action=captureScreen";
};

function bringToFront(){
    if (current == ''){
        alert("Please choose image");
        //console.log("TEST: Bring Image to Front When No Image Selected Results In No Change = " + assert(current == ''));
    }
    else{
      valueofz = virtualObjectInfo[current]["zValue"];
      var before = valueofz;
      valueofz+=1;
      var x = document.getElementById(current);
      x.setAttribute("src", virtualObjectInfo[current]["source"]);
      x.setAttribute('style', 'position: absolute; z-index:'+valueofz);
      virtualObjectInfo[current]["zValue"] = valueofz;
      x.style.left = ol + "px";
      x.style.top = ot + "px";
      //console.log("TEST: Bring Image to Front With Valid Image = " + assert(before<virtualObjectInfo[current]["zValue"]));
      saveBeforeUnload();
    }
};

var ot, ol;
function getImagePosition(event) {
    ot = document.getElementById(current).offsetTop;
    ol = document.getElementById(current).offsetLeft;
    virtualObjectInfo[current]["offsetTopValue"] = ot;
    virtualObjectInfo[current]["offsetLeftValue"] = ol;
};

function clearLocalStorage(){
    localStorage.removeItem("VirtualObjectInfoLS");
    //console.log("TEST: Clear Local Storage = " + assert(!localStorage.getItem("VirtualObjectInfoLS") && !localStorage.getItem("AndroidImagePath")));
};

function assert(outcome) { 
    var ans = outcome ? 'PASS' : 'FAIL';
    return ans;
};

function disableRecommendation(){

    var recommendationIconElement = document.getElementById('recommendationIcon');
    recommendationIconElement.style.display = 'none';
/*
    var elem = document.getElementById("loadingMessage1");
    elem.style.display = 'inline-block';

    setTimeout(function() {
      		var e = document.getElementById('loadingMessage1');
     		e.parentElement.removeChild(e);
    }, 10000); */

}