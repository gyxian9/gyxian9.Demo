package com.gyxian9.photobrower2.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/29.
 */
public class Photo implements Serializable {

    private String imageId; //图片ID
    private String imagePath;//图片路径
    private boolean isSelected = false;//是否被选中？

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
