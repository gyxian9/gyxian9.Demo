package com.gyxian9.photobrower2.adapter;

import android.content.Context;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyxian9.photobrower2.R;
import com.gyxian9.photobrower2.bean.PhotoAlbum;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


import java.util.ArrayList;
import java.util.List;

import static com.nostra13.universalimageloader.core.assist.QueueProcessingType.*;

/**
 * Created by Administrator on 2015/11/29.
 */
public class AlbumsAdapter extends BaseAdapter {

    private String TAG = AlbumsAdapter.class.getSimpleName();
    private List<PhotoAlbum> photoAlbumList;
    private LayoutInflater inflater;
    /***
     * http://blog.csdn.net/vipzjyno1/article/details/23206387
     * Android-Universal-Image-Loader用法
     */
    private ImageLoader imageLoader = ImageLoader.getInstance(); //
    private DisplayImageOptions options; //显示的图片的各种格式


    public AlbumsAdapter(Context context){
        inflater = LayoutInflater.from(context);
        photoAlbumList = new ArrayList<PhotoAlbum>();

        //初始化第三方控件
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(LIFO)
                .memoryCacheExtraOptions(96,120)//即保存的每个缓存文件的最大长宽
                .build();

        imageLoader.init(config);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.album_default_loading_pic)// 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.album_default_loading_pic)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.album_default_loading_pic)// 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)    // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)      // 设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();

    }

    @Override
    public int getCount() {
        return photoAlbumList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoAlbumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView ==null){
            convertView = inflater.inflate(R.layout.item_album_gridview,parent,false);
            holder = new ViewHolder();
            holder.firstImg = (ImageView) convertView.findViewById(R.id.image);
            holder.dirName = (TextView) convertView.findViewById(R.id.name);
            holder.count = (TextView) convertView.findViewById(R.id.count);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.count.setText("" + photoAlbumList.get(position).getCount());
        holder.dirName.setText(photoAlbumList.get(position).getDirName());

        //String imageUri = "file:///mnt/sdcard/image.png"; // from SD card  
        imageLoader.displayImage("file://"+photoAlbumList.get(position)
                .getImageList().get(0).getImagePath()
                ,holder.firstImg,options);

        return convertView;
    }

    public void setArrayList(List<PhotoAlbum> arrayList) {
        this.photoAlbumList = arrayList;
    }

    class ViewHolder{
        ImageView firstImg;
        TextView dirName;
        TextView count;
    }
}
