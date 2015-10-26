package example.demo.gyx.indexlistview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

/**
 * Created by gyx on 2015/10/25.
 */
public class SearchBar extends EditText implements
        OnFocusChangeListener,TextWatcher{

    private Drawable mDrawable;

    public SearchBar(Context context) {
        this(context, null);
    }

    public SearchBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mDrawable = getCompoundDrawables()[2];
        if (mDrawable == null){
            mDrawable = getResources().getDrawable(R.mipmap.cancelbtn);
        }
        mDrawable.setBounds(0,0,mDrawable.getIntrinsicWidth()
                ,mDrawable.getIntrinsicHeight());
        setIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null){
            if (event.getAction() == MotionEvent.ACTION_UP){
                boolean touchAble = event.getX() > (getWidth()
                        - getPaddingRight() - mDrawable.getIntrinsicWidth())
                && event.getX() < ((getWidth() - getPaddingRight()));
                if (touchAble){
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setIconVisible(getText().length() > 0);
        } else {
            setIconVisible(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setIconVisible(text.length() > 0);
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     * @param visible
     */
    public void setIconVisible(boolean visible) {
        Drawable rightPhoto = visible? mDrawable: null;
        setCompoundDrawables(getCompoundDrawables()[0]
                ,getCompoundDrawables()[1],rightPhoto,getCompoundDrawables()[3]);
    }
}
