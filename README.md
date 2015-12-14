# VirtualHome
Android application for Real-estate with Augmented Reality and Data mining

# VirtualHome
--------------
> Group project developed for Master project CMPE294A/B 
> at San Jose State University
 - Title: VirtualHome - Augmented Reality and Data mining for Real Estate

##Requirements
--------------
To run this project locally JDK 1.7, Gradle should be installed. The project should contain ARView target
Android device needs to support Android 4.0+(API level 14+), capable CPU (armv7a and NEON support)
Wikitude licence is required. (Wikitude EDU license has been provided in the package)

##Basic Configuration
--------------
Install app on android phone either by downloading from Google Play or install the apk via IDE
 
##Features supported
--------------
 Features supported as part of Phase 1(CMPE295A)
 * User signup/register page
 * User Preference configuration
 * Display list of Furniture gallery
 * Overlay augmented image(marker & markerless)
 * Edit options for overlay image.
 * Web server backend with database support. 
 
Features supported as part of Phase 2(CMPE295B)
 * User login from Facebook
 * Display Furniture gallery based on product category
 * Cache implementation while loading gallery images. 
 * Markerless additional features: Multiple images, change background, Display produt recommendations
 * Marker based additional features: Replace image, Display product recommendations
 * Web server integration with Cloudinary.
 * Image transaparency conversion in web server.
 * Data mining based on user preference dataset.
 * Data mining based on user transaction history.

Demo video link:
https://www.youtube.com/watch?v=oCGHkgZ2UAc

Google play deployed name: Virtual Home

##Developed
--------------
Following directory information:


* [/RESTBackend/version1.0/*] - Webserver, datamining code implemented in Spring framework
* [/ARHome/app/src/main/assets/arviews/ImageOnTarget/*] - Marker based Augmented Reality code 
* [/ARHome/app/src/main/assets/arviews/ImageOnTarget/VirtualHomeTarget.pdf] - ARView target file
* [/ARHome/app/src/main/assets/arviews/MarkerlessImageOnTarget/*] - Markerless based Augmented Reality code
* [/ARHome/app/src/main/res/*] - Android resource folders
* [/ARHome/app/src/main//java/com/wikitude/virtualhome/*] - Activity files for android client 

##Technology Used
--------------
Following technologies have been used in order to develop this project

* Java - Andoid development
* Wikitude SDK 5.0- Augmented activity development
* Spring Framework - Rest API for server code
* MongoDB - Database technologgy used
* WEKA - For datamining capabilities
* AWS - To host the application

##Tools being used
--------------
* Android Studio (API 22)
* Html/Jquery/JS
* Spring Tool Suite
* MongoLab
* MongoChef 
* Weka
* Cloudinary
* Gradle

##Team Members
--------------
* Anusha Ravikumar
* Karanjot Singh
* Radhika SNM
* Swetha Rathna Kempahanumaiah





