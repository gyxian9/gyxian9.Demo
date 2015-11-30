package com.gyxian9.photobrower2.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.gyxian9.photobrower2.bean.Photo;
import com.gyxian9.photobrower2.bean.PhotoAlbum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 通过异步线程实现扫描SD卡中图片的功能
 * Created by Administrator on 2015/11/29.
 */
public class PhotoAlbumHelper extends AsyncTask<Object,Object,Object> {

    final String TAG = getClass().getSimpleName();//打印log用
    Context context;
    ContentResolver cr;

    HashMap<String,String> thumbnailHash = new HashMap<String,String>();//缩略图list

    //存放缩略图，也就是显示文件夹列表
    List<HashMap<String,String>> thumbnailList = new ArrayList<HashMap<String,String>>();
    HashMap<String,PhotoAlbum> photoAlbumHashMap = new HashMap<String,PhotoAlbum>();

    private GetAlbumInterface getAlbumInterface; //回调接口

    //获取该类的实例
    public static PhotoAlbumHelper getIstance(){
        PhotoAlbumHelper instance = new PhotoAlbumHelper();
        return instance;
    }

    /**
     * 初始化ContentResolver，获取该对象实例必须先initCr
     * @param context
     */
    public void initContentResolver(Context context){
        if (this.context == null){
            this.context = context;
            cr = context.getContentResolver();
        }
    }
    /**
     * 获取缩略图,通过图片的ID值获取的
     */
    private void getThumbnail(){
        String[] projection = {MediaStore.Images.Thumbnails._ID
                , MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.DATA};
        Cursor c = MediaStore.Images.Thumbnails.queryMiniThumbnails(cr
                , MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI
        , MediaStore.Images.Thumbnails.MINI_KIND,projection);
        getThumbnailColumnData(c);

        c.close();
    }

    /**
     * 从数据库中获取缩略图
     * 这里让 thumbnailList 拥有数据
     * @param c
     */
    private void getThumbnailColumnData(Cursor c) {
        if (c.moveToFirst()){ //目标是第一个！
            int image_id;
            String image_path;
            int image_idColumn = c.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
            int image_data = c.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
            do{
                image_id = c.getInt(image_idColumn);
                image_path = c.getString(image_data);
                thumbnailHash.put(""+image_id,image_path);//把获取到第一张图存放到集合中
            }while(c.moveToNext());
        }
    }

    boolean hasBuildImageAlbumsList = false;//是否创建了图片集

    /**
     * 得到图片集
     */
    private void buildImageAlbumsList(){
        getThumbnail(); //构造缩略图索引

        String column[] = new String[]{MediaStore.Images.Media._ID
        , MediaStore.Images.Media.PICASA_ID, MediaStore.Images.Media.DATA
        , MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.TITLE
        , MediaStore.Images.Media.SIZE, MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        , MediaStore.Images.Media.BUCKET_ID};
        //得到一个游标
        Cursor c = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, null, null
                , MediaStore.Images.Media.DATE_MODIFIED + " desc ");

        if (c.moveToFirst()){
            // 获取指定列的索引
            //照片id
            int photoIDIndex = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            //照片路径
            int photoPathIndex = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            //相册名
            int dirDisplayNameIndex = c.getColumnIndexOrThrow(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            //相册ID
            int dirIdIndex = c.getColumnIndexOrThrow(
                    MediaStore.Images.Media.BUCKET_ID);

            /**
             * Description:这里增加了一个判断：判断照片的名
             * 字是否合法，例如.jpg .png等没有名字的格式
             * 如果图片名字是不合法的，直接过滤掉
             */
            do{
                if (c.getString(photoPathIndex).substring(
                        c.getString(photoPathIndex).lastIndexOf("/") + 1,
                        c.getString(photoPathIndex).lastIndexOf("."))
                        .replace(" ", "").length() <= 0){
                    Log.d(TAG,"出现了异常图片的地址：cur.getString(photoPathIndex)=" +
                            c.getString(photoPathIndex));
                }else {
                    //图片合法
                    String _id = c.getString(photoIDIndex);
                    String path = c.getString(photoPathIndex);
                    String dirName = c.getString(dirDisplayNameIndex);
                    String dirId = c.getString(dirIdIndex);

                    PhotoAlbum photoAlbum = photoAlbumHashMap.get(dirId);

                    if (photoAlbum == null){
                        photoAlbum = new PhotoAlbum();
                        photoAlbumHashMap.put(dirId,photoAlbum);
                        photoAlbum.imageList = new ArrayList<Photo>();
                        photoAlbum.dirName = dirName;
                    }
                    photoAlbum.count ++;
                    Photo photos = new Photo();
                    photos.setImageId(_id);
                    photos.setImagePath(path);

                    photoAlbum.imageList.add(photos);
                }

            }while(c.moveToNext());
        }
        c.close();
        hasBuildImageAlbumsList = true;
    }

    /**
     * 得到图片集
     * @param refresh
     * @return
     */
    private List<PhotoAlbum> getImageDirList(boolean refresh){
        //刷新 或 没刷新没创建相册（第一次使用） 时，创建相册
        if (refresh || (!refresh && !hasBuildImageAlbumsList)){
            buildImageAlbumsList();
        }
        List<PhotoAlbum> tmpList = new ArrayList<PhotoAlbum>();
        Iterator<Map.Entry<String,PhotoAlbum>> itr = photoAlbumHashMap.entrySet().iterator();
        //hash转化List
        while(itr.hasNext()){
            Map.Entry<String,PhotoAlbum> entry = (Map.Entry<String,PhotoAlbum>)itr.next();
            tmpList.add(entry.getValue());
        }
        return tmpList;
    }

    /**
     * 获取图片原始路径
     * @param image_id
     * @return
     */
    private String getOriginalImagePath(String image_id){
        String path = null;
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        Cursor c = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection
                , MediaStore.Images.Media._ID + " = " + image_id
                ,null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (c != null){
            c.moveToFirst();
            path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
        }
        return path;
    }

    /**
     * 回调接口
     */
    public interface GetAlbumInterface{
        public void getAlbumInterface(List<PhotoAlbum> list);
    }

    /**
     * 对外开放接口方法
     * @param getAlbumInterface
     */
    public void setGetAlbumInterface(GetAlbumInterface getAlbumInterface) {
        this.getAlbumInterface = getAlbumInterface;
    }

    @Override
    protected Object doInBackground(Object... params) {
        return getImageDirList((Boolean) params[0]);
    }

    //该类使用异步线程类实现，并通过回调接口传递数据，在执行异步线程完成之后，通过如下代码传递数据。
    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        getAlbumInterface.getAlbumInterface((List<PhotoAlbum>)o);
    }
}
