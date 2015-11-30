package com.gyxian9.photobrower2.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyxian9.photobrower2.R;
import com.gyxian9.photobrower2.bean.Photo;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import static com.nostra13.universalimageloader.core.assist.QueueProcessingType.LIFO;

/**
 * Created by Administrator on 2015/11/29.
 */
public class AlbumsItemAdapter extends BaseAdapter{

    private LayoutInflater inflater;
    private List<Photo> list;

    private ImageLoader imageLoader = ImageLoader.getInstance(); //
    private DisplayImageOptions options; //显示的图片的各种格式

    private boolean isCheck = false;

    public AlbumsItemAdapter(Context context,List<Photo> list){
        inflater = LayoutInflater.from(context);
        this.list = list;

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
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView ==null){
            convertView = inflater.inflate(R.layout.item_album_item_gridview,parent,false);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.image2);
            holder.check = (CheckBox) convertView.findViewById(R.id.check);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        imageLoader.displayImage("file://"+list.get(position).getImagePath(),holder.iv,options);

        if (isCheck){
            holder.check.setVisibility(View.VISIBLE);
        }else{
            holder.check.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder{
        ImageView iv;
        CheckBox check;
    }
    public void hasCheck(boolean isCheck){
        this.isCheck = isCheck;
    }
}
