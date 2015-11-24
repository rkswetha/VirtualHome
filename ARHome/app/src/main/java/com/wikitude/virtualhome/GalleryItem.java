package com.wikitude.virtualhome;

import android.graphics.Bitmap;

public class GalleryItem {
    //private String galleryItemImageURL;
    private String galleryItemTitle;
    private String galleryItemDescription;
    private String galleryItemLocation;
    private String galleryItemProductID;

    public String getGalleryItemProductID() {
        return galleryItemProductID;
    }

    public void setGalleryItemProductID(String galleryItemProductID) {
        this.galleryItemProductID = galleryItemProductID;
    }




    public String getGalleryItemLocation() {
        return galleryItemLocation;
    }

    public void setGalleryItemLocation(String galleryItemLocation) {
        this.galleryItemLocation = galleryItemLocation;
    }

    public GalleryItem(String galleryItemTitle, String galleryItemDescription,String galleryItemLocation,String galleryItemProductID) {
        super();
        //this.galleryItemImageURL = galleryItemImage;
        this.galleryItemTitle = galleryItemTitle;
        this.galleryItemDescription = galleryItemDescription;
        this.galleryItemLocation=galleryItemLocation;
        this.galleryItemProductID=galleryItemProductID;
    }

   /* public String getGalleryItemImage() {
        return galleryItemImageURL;
    }

   public void setGalleryItemImage(String galleryItemImageURL) {
        this.galleryItemImageURL = galleryItemImageURL;
    }*/



    public String getGalleryItemTitle() {
        return galleryItemTitle;
    }

    public void setGalleryItemTitle(String galleryItemTitle) {
        this.galleryItemTitle = galleryItemTitle;
    }


    public String getGalleryItemDescription() {
        return galleryItemDescription;
    }

    public void setGalleryItemDescription(String galleryItemDescription) {
        this.galleryItemDescription = galleryItemDescription;
    }
}
