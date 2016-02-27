package com.itql.downrefresh.customs;

import android.content.Context;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itql.downrefresh.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义带下拉刷新的ListView
 */
public class ListHeadView extends ListView implements AbsListView.OnScrollListener{

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
    private boolean isLoadingMore = false;//当前是否正在处于加载更多

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
        setOnScrollListener(this);
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
        footerView.setPadding(0,-footerViewHeight,0,0); //设置padding为负数，可以隐藏footerView

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


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //手指按下去，先记录y坐标
                downY= (int) ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                //手指移动的时候，要进行判断
                if(currentState==REFRESHING){
                    //正在刷新中
                    break;
                }
                int distanceY= (int) (ev.getY()-downY);     //滑动距离
                int paddingTop=-headerViewHeight+distanceY; //headerView 距离顶部距离随着手指滑动的改变而改变，刚好隐藏的时候是 -headerViewHeight
                if(paddingTop>-headerViewHeight && getFirstVisiblePosition()==0){
                //此时不是要滑动listview，而是要刷新，因为listview的第一个可见的数据是0，也就是第一个，只有在刷新的时候才会是处于第一个
                // 注意下面要返回true，不让listview处理该事件，也就是不让listview滑动
                    headerView.setPadding(0,paddingTop,0,0);//更新headerView距离顶部的距离

                    if(paddingTop>=0 && currentState==PULL_REFRESH){        //从下拉刷新状态进入松开刷新状态
                        currentState=RELEASE_REFRESH;
                        refreshHeaderView();
                    }else if(paddingTop<0 && currentState==RELEASE_REFRESH){//从松开刷新状态进入下拉刷新，也就是说放弃了刷新
                        currentState=PULL_REFRESH;
                        refreshHeaderView();
                    }
                    return true;    //拦截该事件，不让listview处理该事件，也就是不让listview滑动
                }
                break;
            case MotionEvent.ACTION_UP:
                if(currentState==PULL_REFRESH){ //处于下拉刷新状态时松手，此时没有刷新，要隐藏headerView
                    headerView.setPadding(0,-headerViewHeight,0,0);
                }else if(currentState==RELEASE_REFRESH){    //处于松开刷新的时候松手，此时当然要刷新
                    headerView.setPadding(0,0,0,0); //刷新时headerView是完全显示出来的
                    currentState=REFRESHING;
                    refreshHeaderView();

                    if(onRefreshListener!=null){
                        onRefreshListener.onPullRefresh();
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 根据当前状态更新 headerView的显示
     */
    private void refreshHeaderView(){
        switch (currentState){
            case PULL_REFRESH:
                tv_down_refresh_head.setText(getResources().getString(R.string.down_refresh));
                iv_rotate.startAnimation(downAnimation);
                break;

            case RELEASE_REFRESH:
                tv_down_refresh_head.setText(getResources().getString(R.string.release_refresh));
                iv_rotate.startAnimation(upAnimation);
                break;

            case REFRESHING:
                iv_rotate.clearAnimation(); //因为向上的旋转动画有可能没有执行完
                tv_down_refresh_head.setText(getResources().getString(R.string.refreshing));
                iv_rotate.setVisibility(View.INVISIBLE);
                pb_refresh_head.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 完成刷新操作，重置状态，在获取完数据并更新adapter之后，在UI线程中调用
     */
    public void completeRefresh(){
        if(isLoadingMore){
            footerView.setPadding(0,-footerViewHeight,0,0);
            isLoadingMore=false;
        }else{//重置headerView
            headerView.setPadding(0,-headerViewHeight,0,0);
            currentState=PULL_REFRESH;
            pb_refresh_head.setVisibility(View.INVISIBLE);
            iv_rotate.setVisibility(View.VISIBLE);
            tv_down_refresh_head.setText(getResources().getString(R.string.down_refresh));
            tv_refresh_time.setText("最后刷新："+getCurrentTime());
        }
    }

    /**
     * 获取当前时间并格式化
     * @return
     */
    private String getCurrentTime(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    /**
     * 定义接口，拥有“刷新”和“加载更多”方法
     */
    public interface OnRefreshListener{
        void onPullRefresh();
        void onLoadingMore();
    }

    private OnRefreshListener onRefreshListener;
    public void setOnRefreshListener(OnRefreshListener onRefreshListener){
        this.onRefreshListener=onRefreshListener;
    }
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        /*
         * SCROLL_STATE_IDLE:闲置状态，就是手指松开
         * SCROLL_STATE_TOUCH_SCROLL：手指触摸滑动，就是按着来滑动
         * SCROLL_STATE_FLING：快速滑动后松开
         */
        if(scrollState==OnScrollListener.SCROLL_STATE_IDLE && getLastVisiblePosition()==getCount()-1 && !isLoadingMore){//当前可见的是listView的最后一个
            isLoadingMore=true;
            footerView.setPadding(0, 0, 0, 0);
            setSelection(getCount());//让listView最后一条显示出来
            if(onRefreshListener!=null){
                onRefreshListener.onLoadingMore();
            }
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
