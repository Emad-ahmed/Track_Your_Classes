package com.example.firebaselearning;

public class ModelForImage {

    private String imageUrl;
    public ModelForImage(){

    }

    public ModelForImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
