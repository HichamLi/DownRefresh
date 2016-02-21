package com.itql.downrefresh.customs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.itql.downrefresh.R;

/**
 * 自定义带下拉刷新的ListView
 */
public class ListHeadView extends ListView {

    private View headerView;

    public ListHeadView(Context context) {
        super(context);
        init();
    }

    public ListHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListHeadView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        initHeaderView();
    }

    /**
     * 初始化 herderView
     */
    private void initHeaderView() {
        headerView = View.inflate(getContext(), R.layout.view_list_head, null);
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
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
            return null;
        }
    }

}
