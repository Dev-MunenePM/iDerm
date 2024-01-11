package com.example.iderm.HelperClasses;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

public class infoHelper {

    int image;
    String title;
    GradientDrawable color;

    public infoHelper(GradientDrawable color, String title) {
        this.image = image;
        this.title = title;
        this.color = color;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }


    public Drawable getgradient() {
        return color;
    }


}
