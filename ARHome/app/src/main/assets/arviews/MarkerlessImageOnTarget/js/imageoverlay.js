var current;

$(function() {
     $( "#draggable" ).draggable({
          start: function(){
          	var getid = this.id;
          	//alert(getid);
          	current = getid;
          }
 });


	$( "#droppable" ).droppable({
           drop: function( event, ui ) {
              var currentId = $(ui.draggable).attr("id");
              //alert(currentId);
              var deleteconfirmation = confirm("Delete object?");
    		  if (deleteconfirmation == true) {
              	deleteObject(currentId);
              }
              //currentId.parentNode.removeChild(currentId);
            }
          });

 	alert("Markerless Augmented Reality: \n\n Drag the image to move image around camera \n\n Drag image to div bar to delete image \n\n Click on use background image to use a image rather than camera background. Click again to get back to camera background.");
});


function scaleUp(){
        var x = document.getElementById(current);
        x.height+=100;
		x.width+=100;
}

function scaleDown(){
        var x = document.getElementById(current);
        x.height-=50;
		x.width-=50;
}


function showInfo(){
		var website = "http://www.ikea.com/us/en/catalog/products/00104291/";
  		AR.context.openInBrowser(website);
}

function deleteObject(id){
	var child = document.getElementById(id);
	child.parentNode.removeChild(child);
}

function flipImage(){
	var x = document.getElementById(current);
	currentvalue = document.getElementById("button4").value;
  	if(currentvalue == "off"){
  			x.classList.add("img");
    		document.getElementById("button4").value="on";
  	}else{
  			x.classList.remove("img");
   			document.getElementById("button4").value="off";
  	}
}

function setImage(){
	currentvalue = document.getElementById("button7").value;
  	if(currentvalue == "off"){
  		document.body.style.backgroundImage = "url('assets/emptyRoom.png')";
  		document.body.style.backgroundRepeat = "no-repeat";
  		document.body.style.backgroundSize = "cover";
    	document.getElementById("button7").value="on";
  	} else{
  		document.body.style.backgroundImage = "none";
   		document.getElementById("button7").value="off";
  	}
}

function captureScreen() {
	document.location = "architectsdk://button1?action=captureScreen";
}


function displayProductInfo() {
    document.location = "architectsdk://button2?action=productInfo";
}
