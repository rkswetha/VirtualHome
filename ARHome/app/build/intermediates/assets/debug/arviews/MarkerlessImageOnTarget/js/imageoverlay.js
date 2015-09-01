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
      x.setAttribute("height", '40%');
    //  x.setAttribute("height", 'auto');
      x.setAttribute("id", "virtualObject"+imageId)
      //x.setAttribute("style","background-color: red;");

      document.body.appendChild(x);

       var img1= document.getElementById("virtualObject"+imageId);


      /*var doc=document.createElement("CANVAS");
      doc.setAttribute("id", "document"+imageId);
      doc.setAttribute("width", '30%');
      doc.setAttribute("height", 'auto');*/

            var doc=document.createElement("CANVAS");
            doc.setAttribute("id", "document"+imageId);
          /*  doc.setAttribute("width", '200');
            doc.setAttribute("height", '100');
*/
            alert("img ht "+img1.height);
            alert("img wt "+img1.width);

            doc.setAttribute("width", img1.width);
            doc.setAttribute("height", '40');

             alert("can ht "+doc.height);
             alert("can wt "+doc.width);

        document.body.appendChild(doc);


        //Working:
       /* var c = document.getElementById("document"+imageId);
        var ctx = c.getContext("2d");
        ctx.fillStyle = "#FF0000";
        ctx.fillRect(0,0,150,75);*/




      //Trying to edit pixels
        // var ctx = document.getElementById("virtualObject"+imageId).data.getContext("2d");

        //From here

         var c = document.getElementById("document"+imageId);
         var ctx = c.getContext("2d");

         //ctx.fillStyle="rgba(0,0,0,0.5)";
         ctx.drawImage(img1, 0, 0, img1.width, img1.height);


        alert("C w2 "+ c.width);
        alert("c h2 "+ c.height);


        //HERE
        //It was doc instead of c
         var imgData = ctx.getImageData(0, 0, c.width, c.height);
         ctx.putImageData(this.adjustImage(imgData), 0, 0);








      //$("virtualObject"+imageId).addClass("enableDrag");
      document.getElementById("virtualObject"+imageId).className = "enableDrag";
      document.getElementById("document"+imageId).className = "enableDrag";
      //alert(document.getElementById("virtualObject"+imageId).className);
      imageWebsite["virtualObject"+imageId] = sourceUrl;
      //imageDescr["virtualObject"+imageId] = imageDescription;
      //alert(imageInfo["virtualObject"+imageId]);
      draggableObjects();
};


 function adjustImage(iArray) {
    var imageData = iArray.data;
    /*for (var i = 0; i < imageData.length; i+= 4) {
        if(imageData[i] === 255 && imageData[i+1] === 255 && imageData[i+2] === 255){
            imageData[i+3] = 0;
        }
    }*/

    for (var i = 0; i < imageData.length; i+= 4) {
            if((imageData[i] >= 150 && imageData[i] <=  255) && (imageData[i+1] >= 150 && imageData[i+1] <=  255) && (imageData[i+2] >= 150 && imageData[i+2] <=  255)){
                imageData[i+3] = 0;
            }
        }

    return iArray;
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

