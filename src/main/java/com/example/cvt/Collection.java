package com.example.cvt;

public class Collection {
    int Image;
    String Title;
    String Description;

    public Collection(int image, String title, String description){
        Image = image;
        Title = title;
        Description = description;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

}
