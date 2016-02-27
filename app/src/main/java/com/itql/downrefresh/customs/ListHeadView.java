package com.itql.downrefresh.customs;

import android.content.Context;

import android.util.AttributeSet;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itql.downrefresh.R;

/**
 * 自定义带下拉刷新的ListView
 */
public class ListHeadView extends ListView {

    private View headerView;
    private ImageView iv_rotate;
    private ProgressBar pb_refresh_head;
    private TextView tv_down_refresh_head;
    private TextView tv_refresh_time;
    private int headerViewHeight;

    private View footerView;
    private ProgressBar pb_refresh_foot;
    private TextView tv_down_refresh_foot;
    private int footerViewHeight;

    private int downY;                          //按下时Y坐标

    private final int PULL_REFRESH = 0;         //下拉刷新的状态
    private final int RELEASE_REFRESH = 1;      //松开刷新的状态
    private final int REFRESHING = 2;           //正在刷新的状态
    private int currentState = PULL_REFRESH;

    private RotateAnimation upAnimation;        //向上的旋转动画
    private RotateAnimation downAnimation;      //向下的旋转动画

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

    /**
     * 初始化
     */
    private void init(){
        initHeaderView();
        initFooterView();
        initAnimation();
    }

    /**
     * 初始化 herderView
     */
    private void initHeaderView() {
        headerView = View.inflate(getContext(), R.layout.view_list_head, null);
        iv_rotate= (ImageView) headerView.findViewById(R.id.iv_rotate);
        pb_refresh_head= (ProgressBar) headerView.findViewById(R.id.pb_refresh_head);
        tv_down_refresh_head= (TextView) headerView.findViewById(R.id.tv_down_refresh_head);
        tv_refresh_time= (TextView) headerView.findViewById(R.id.tv_refresh_time);

        headerView.measure(0,0);                        //主动通知系统去测量该view;
        headerViewHeight=headerView.getMeasuredHeight();
        headerView.setPadding(0, -headerViewHeight, 0, 0); //设置padding为负数，可以隐藏headerView

        addHeaderView(headerView);
    }

    /**
     * 初始化 footView
     */
    private void initFooterView(){
        footerView = View.inflate(getContext(), R.layout.view_list_foot, null);
        pb_refresh_foot= (ProgressBar) footerView.findViewById(R.id.pb_refresh_foot);
        tv_down_refresh_foot= (TextView) footerView.findViewById(R.id.tv_down_refresh_foot);

        footerView.measure(0,0);                        //主动通知系统去测量该view;
        footerViewHeight=footerView.getMeasuredHeight();
        footerView.setPadding(0,-footerViewHeight,0,0); //设置padding为负数，可以隐藏headerView

        addFooterView(footerView);
    }

    /**
     * 初始化旋转动画
     */
    private void initAnimation(){
        upAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setDuration(300);
        upAnimation.setFillAfter(true);

        downAnimation = new RotateAnimation(-180, -360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setDuration(300);
        downAnimation.setFillAfter(true);
    }



}
