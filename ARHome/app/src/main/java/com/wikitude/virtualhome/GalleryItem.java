package com.wikitude.virtualhome;

/**
 * Created by Radhika on 8/1/2015.
 */
import android.graphics.Bitmap;

public class GalleryItem {
    private Bitmap galleryItemImage;
    private String galleryItemTitle;
    private String galleryItemDescription;
    private String galleryItemLocation;

    public String getGalleryItemLocation() {
        return galleryItemLocation;
    }

    public void setGalleryItemLocation(String galleryItemLocation) {
        this.galleryItemLocation = galleryItemLocation;
    }

    public GalleryItem(Bitmap galleryItemImage, String galleryItemTitle, String galleryItemDescription,String galleryItemLocation) {
        super();
        this.galleryItemImage = galleryItemImage;
        this.galleryItemTitle = galleryItemTitle;
        this.galleryItemDescription = galleryItemDescription;
        this.galleryItemLocation=galleryItemLocation;
    }

    public Bitmap getGalleryItemImage() {
        return galleryItemImage;
    }

    public void setGalleryItemImage(Bitmap galleryItemImage) {
        this.galleryItemImage = galleryItemImage;
    }



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
