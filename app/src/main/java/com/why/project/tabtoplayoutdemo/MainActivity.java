package com.why.project.tabtoplayoutdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.why.project.tabtoplayoutdemo.views.tab.TabTopLayout;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "MainActivity";

	private TabTopLayout mTabTopLayout;

	private LinearLayout mDoLayout;
	private LinearLayout mUnDoLayout;

	/**已发布索引值--需要和TabTopLayout中的数组的下标值对应*/
	public static final int Do_Fragment_Index = 0;
	/**未发布索引值*/
	public static final int UnDo_Fragment_Index = 1;

	/**保存的选项卡的下标值*/
	private int savdCheckedIndex = Do_Fragment_Index;
	/**当前的选项卡的下标值*/
	private int mCurrentIndex = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//初始化控件
		initView();
		//初始化数据
		initData();
		//初始化控件的点击事件
		initEvent();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.w(TAG, "{onResume}");
		//设置保存的或者初始的选项卡标红显示
		SwitchTab(savdCheckedIndex);

		mCurrentIndex = -1;//解决按home键后长时间不用，再次打开显示空白的问题
		//设置保存的或者初始的选项卡展现对应的fragment
		ShowFragment(savdCheckedIndex);
	}
	/**
	 * 初始化控件
	 * */
	private void initView(){
		mTabTopLayout = (TabTopLayout) findViewById(R.id.home_toptab);

		mDoLayout = (LinearLayout) findViewById(R.id.layout_do);
		mUnDoLayout = (LinearLayout) findViewById(R.id.layout_undo);
	}

	/**初始化数据*/
	private void initData() {
	}

	/**
	 * 初始化点击事件
	 * */
	private void initEvent(){
		//每一个选项卡的点击事件
		mTabTopLayout.setOnTopTabSelectedListener(new TabTopLayout.OnTopTabSelectListener() {
			@Override
			public void onTopTabSelected(int index) {
				ShowFragment(index);//独立出来，用于OnResume的时候初始化展现相应的Fragment
			}
		});
	}
	/**控制切换选项卡*/
	public void SwitchTab(int checkedIndex){
		if(mTabTopLayout != null){
			mTabTopLayout.setTabsDisplay(checkedIndex);
		}
	}

	/**
	 * 显示选项卡对应的Fragment*/
	public void ShowFragment(int checkedIndex) {
		if (mCurrentIndex == checkedIndex) {
			return;
		}

		//隐藏全部碎片
		hideFragments();
		switch (checkedIndex) {
			case Do_Fragment_Index:
				mDoLayout.setVisibility(View.VISIBLE);
				break;
			case UnDo_Fragment_Index:
				mUnDoLayout.setVisibility(View.VISIBLE);
				break;
		}
		savdCheckedIndex = checkedIndex;
		mCurrentIndex = checkedIndex;
	}

	/**隐藏全部碎片
	 * 需要注意：不要在OnResume方法中实例化碎片，因为先添加、显示，才可以隐藏。否则会出现碎片无法显示的问题*/
	private void hideFragments() {
		mDoLayout.setVisibility(View.GONE);
		mUnDoLayout.setVisibility(View.GONE);
	}

	/**
	 * http://blog.csdn.net/caesardadi/article/details/20382815
	 * */
	// 自己记录fragment的位置,防止activity被系统回收时，fragment错乱的问题【按home键返回到桌面一段时间，然后在进程里面重新打开，会发现RadioButton的图片选中状态在第二个，但是文字和背景颜色的选中状态在第一个】
	//onSaveInstanceState()只适合用于保存一些临时性的状态，而onPause()适合用于数据的持久化保存。
	protected void onSaveInstanceState(Bundle outState) {
		//http://www.cnblogs.com/chuanstone/p/4672096.html?utm_source=tuicool&utm_medium=referral
		//总是执行这句代码来调用父类去保存视图层的状态”。其实到这里大家也就明白了，就是因为这句话导致了重影的出现
		//super.onSaveInstanceState(outState);
		outState.putInt("selectedCheckedIndex", savdCheckedIndex);
		outState.putInt("mCurrentIndex", mCurrentIndex);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		savdCheckedIndex = savedInstanceState.getInt("selectedCheckedIndex");
		mCurrentIndex = savedInstanceState.getInt("mCurrentIndex");
		super.onRestoreInstanceState(savedInstanceState);
	}

}
