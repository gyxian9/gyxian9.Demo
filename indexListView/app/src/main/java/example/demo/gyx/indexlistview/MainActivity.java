package example.demo.gyx.indexlistview;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 20:56 10-25
 * 分析+注释一次
 * 17:31 10-25
 * 完成
 * 16:49 10-25
 * 完成siderbar并能使用,可以考虑添加一个EditText来快速查看
 * 16:09 10-25
 * 基本完成ListView显示数据的效果，现在就添加一个siderbar
 * 15:25 10-25
 * 分析好汉字转化问题(暂时未完全看懂)，就加入数据到data内部
 * 13:15 10-25
 * 先把一个简单的listView声明出来并写好他的适配器，现在就只要解决数据的问题
 */
public class MainActivity extends Activity {

    private ListView mListView;
    private TextView dialog;
    private SiderBar siderBar;
    private SearchBar searchBar;

    private MyAdapter myAdapter;
    private List<Model> mDatas;//    values/arrays.xml

    private CharacterParser characterParser;
    private PinYinComparator pinYinComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        characterParser = CharacterParser.getInstance();
        pinYinComparator = new PinYinComparator();

        mListView = (ListView) findViewById(R.id.main_list_view);
        dialog = (TextView) findViewById(R.id.main_dialog);
        searchBar = (SearchBar) findViewById(R.id.main_search_bar);
        siderBar = (SiderBar) findViewById(R.id.main_sider_bar);
        siderBar.setTextView(dialog);//把dialog给siderBar管理

        //实现索引栏的接口，用来同步到某个字母索引
        siderBar.setOnTouchingLetterChangedListener(new SiderBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = myAdapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    mListView.setSelection(position);
                }
            }
        });

        //把准备好的数组放到mDatas
        mDatas = findData(getResources().getStringArray(R.array.date));
        Collections.sort(mDatas,pinYinComparator);

        myAdapter = new MyAdapter(this,mDatas);
        mListView.setAdapter(myAdapter);

//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplication()
//                        , ((Model) myAdapter.getItem(position)).getName()
//                        , Toast.LENGTH_SHORT).show();
//            }
//        });

        //根据输入框输入值的改变来过滤搜索
        searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    /**
     * EditView用的
     * Search查到数据后
     * @param s
     */
    private void filterData(String s) {
        List<Model> lists = new ArrayList<Model>();
        //当没输入东西的时候默认显示所有数据
        if(TextUtils.isEmpty(s)){
            lists = mDatas;
        }else{
            lists.clear();
            for (Model models : mDatas){
                String name = models.getName();
                if (name.indexOf(lists.toString()) != -1
                        || characterParser.getSelling(name).startsWith(s.toString())){
                    lists.add(models);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(lists, pinYinComparator);
        myAdapter.updateListView(lists);
    }

    /**
     * 初始化ListView的数据
     * @param dates
     * @return
     */
    private List<Model> findData(String [] dates) {
        List<Model> lists = new ArrayList<Model>();

        for (int i = 0; i < dates.length; i++) {
            Model model = new Model();
            //array文件是没有拼音和首字母大小写的，要自己转化
            model.setName(dates[i]);

            //汉字转换成拼音
            String pinYin = characterParser.getSelling(dates[i]);
            String sortString = pinYin.substring(0,1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")){
                model.setSortLetters(sortString.toUpperCase());
            }else{
                model.setSortLetters("#");
            }

            lists.add(model);
        }
        return lists;
    }
}
