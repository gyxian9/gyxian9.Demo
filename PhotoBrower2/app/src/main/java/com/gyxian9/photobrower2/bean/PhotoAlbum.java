package com.gyxian9.photobrower2.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 相册bean
 * Created by Administrator on 2015/11/29.
 */
public class PhotoAlbum implements Serializable {

    public int count = 0; //文件夹的总数
    public String dirName;//文件夹的名称
    public List<Photo> imageList;//其里面的图片属性

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public List<Photo> getImageList() {
        return imageList;
    }

    public void setImageList(List<Photo> imageList) {
        this.imageList = imageList;
    }
}
