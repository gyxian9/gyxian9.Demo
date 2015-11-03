package weico.gyx.org.person_comment_client.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import weico.gyx.org.person_comment_client.R;
import weico.gyx.org.person_comment_client.entity.Category;
import weico.gyx.org.person_comment_client.util.JsonUtils;
import weico.gyx.org.person_comment_client.util.MyUrl;
import weico.gyx.org.person_comment_client.util.MyUtils;

public class AllCategoryActivity extends Activity {

    @ViewInject(R.id.all_category_listview)
    private ListView allCategoryListView;
    @ViewInject(R.id.home_nav_all_back)
    private ImageView navAllBackUp;

    private List<Category> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_category);
        ViewUtils.inject(this);
        lists = new ArrayList<Category>();
//        allCategoryListView.setAdapter(new MyAdapter());
        new AllCategoryTask().execute();
    }
    @OnClick(R.id.home_nav_all_back)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.home_nav_all_back:
                finish();
                break;
        }
    }

    class AllCategoryTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            JsonUtils utils = JsonUtils.getInstance();
            String result = utils._postHttpToGetJsonData(MyUrl.categoryURL);
            parseCityDatasJson(result);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MyAdapter adapter = new MyAdapter();
            allCategoryListView.setAdapter(adapter);
        }

        //GSON解析json文件  返回数据
        private void parseCityDatasJson(String jsonString) {
            Log.e("TAG", "JSON is" + jsonString);
            //不知道为啥Gson挂了 om.google.gson.JsonSyntaxException:
            // java.lang.IllegalStateException:
            // Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 2
            Category c;
            try {
                JSONObject jsonObj = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObj.getJSONArray("datas");
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObj = jsonArray.getJSONObject(i);
                    int _id = jsonObj.getInt("categoryId");
                    int _num = jsonObj.getInt("categoryNumber");
                    c = new Category(_id,_num);
                    lists.add(c);
                }
                for (Category category : lists) {
                    int position = category.getCategoryId();
                    MyUtils.allCategoryNumber[position - 1] = category.getCategoryNumber();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


//            Gson gson = new Gson();
//            Type type = new TypeToken<List<Category>>(){}.getType();
//            List<Category> c = gson.fromJson(jsonString, type);
//            for (Category category : c){
//                int position = Integer.parseInt(category.getCategoryId());
//                MyUtils.allCategoryNumber[position - 1] = category.getCategoryNumber();
//            }

        }
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return MyUtils.allCategray.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder myViewHolder = null;

            if (convertView == null) {
                myViewHolder = new MyViewHolder();
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activity_all_category_item, null);
                ViewUtils.inject(myViewHolder, convertView);
                convertView.setTag(myViewHolder);
            } else {
                myViewHolder = (MyViewHolder) convertView.getTag();
            }

            myViewHolder.textDesc.setText(MyUtils.allCategray[position]);
            myViewHolder.textNum.setText(MyUtils.allCategoryNumber[position]+"");
            myViewHolder.imageView.setImageResource(MyUtils.allCategrayImages[position]);


            return convertView;
        }

        class MyViewHolder {
            @ViewInject(R.id.home_nav_all_item_desc)
            public TextView textDesc;
            @ViewInject(R.id.home_nav_all_item_number)
            public TextView textNum;
            @ViewInject(R.id.home_nav_all_item_image)
            public ImageView imageView;
        }

    }

}
