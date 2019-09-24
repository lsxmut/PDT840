package com.redphase.view;

import de.felixroske.jfxsupport.SplashScreen;
import lombok.Data;

@Data
public class CustomSplash extends SplashScreen {
    private String imagePath;
    private boolean visible;

    @Override
    public String getImagePath() {
        return imagePath;
    }

    @Override
    public boolean visible() {
//        return super.visible();
        return visible;
    }
}