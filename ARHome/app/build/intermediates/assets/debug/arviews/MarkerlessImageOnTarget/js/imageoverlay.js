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

                }
       });
};


function addImage(sourceUrl){
      //loadVirtualObjectsFromLS();
      //alert("after load");
     // alert("before"+imageId);

      imageId++;
      alert(imageId);
      alert(sourceUrl);
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
            alert("done");
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




