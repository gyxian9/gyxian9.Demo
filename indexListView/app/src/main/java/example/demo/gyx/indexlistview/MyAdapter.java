package example.demo.gyx.indexlistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gyx on 2015/10/25.
 */
public class MyAdapter extends BaseAdapter implements SectionIndexer {

    private List<Model> mDatas;
    private Context mContext;

    public MyAdapter(Context mContext, List<Model> mDatas){
        this.mDatas = mDatas;
        this.mContext = mContext;
    }

    /**
     * 当ListView需要更新UI
     * @param mDatas
     */
    public void updateListView(List<Model> mDatas){
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final Model model = mDatas.get(position);//当前对象

        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item,null);
            holder.catalog = (TextView) convertView.findViewById(R.id.main_catalog);
            holder.content = (TextView) convertView.findViewById(R.id.main_content);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)){
            holder.catalog.setVisibility(View.VISIBLE);
            holder.catalog.setText(model.getSortLetters());
        }else{
            holder.catalog.setVisibility(View.GONE);
        }

        holder.content.setText(this.mDatas.get(position).getName());


        return convertView;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     * @param section
     * @return
     */
    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mDatas.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    /***
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     * @param position
     * @return
     */
    @Override
    public int getSectionForPosition(int position) {
        return mDatas.get(position).getSortLetters().charAt(0);
    }


    class ViewHolder {
        TextView catalog;
        TextView content;
    }


}
