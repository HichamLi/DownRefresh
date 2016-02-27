package com.itql.downrefresh.activities;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;

import com.itql.downrefresh.R;
import com.itql.downrefresh.customs.ListHeadView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListHeadView lv;
    private ArrayList<String> arrayList=new ArrayList<>();

    private MyAdapter myAdapter;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            myAdapter.notifyDataSetChanged();
            lv.completeRefresh();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        lv= (ListHeadView) findViewById(R.id.lv);

        for(int i=0;i<30;i++){
            arrayList.add("第 "+ i + "个");
        }
        myAdapter = new MyAdapter();
        lv.setAdapter(myAdapter);
        lv.setOnRefreshListener(new ListHeadView.OnRefreshListener() {
            @Override
            public void onPullRefresh() {
                requestDataFromServer(false);
            }

            @Override
            public void onLoadingMore() {
                requestDataFromServer(true);
            }
        });

    }

    private void requestDataFromServer(final boolean isLoadingMore){
        new Thread(){
            @Override
            public void run() {
                SystemClock.sleep(3000);
                if(isLoadingMore){
                    arrayList.add("加载更多数据+1");
                    arrayList.add("加载更多数据+2");
                    arrayList.add("加载更多数据+3");
                }else{
                    arrayList.add(0,"下拉刷新数据");
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return arrayList.size();
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

            if (convertView == null) {
                convertView = new TextView(MainActivity.this);
            }
            ((TextView)convertView).setText(arrayList.get(position));
            ((TextView)convertView).setTextSize(20);
            return convertView;

//            textView=new TextView(MainActivity.this);
//            textView.setText(tvString[position].toString());
//            textView.setTextSize(20);
//            return textView;
        }


    }

}
