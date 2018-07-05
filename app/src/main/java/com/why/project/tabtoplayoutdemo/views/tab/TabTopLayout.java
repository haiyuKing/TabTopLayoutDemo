package com.why.project.tabtoplayoutdemo.views.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.why.project.tabtoplayoutdemo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by HaiyuKing
 * Used 顶部选项卡布局类（注意：这个是tab_top_item的父布局）
 */

public class TabTopLayout extends LinearLayout {

	private Context mContext;

	//选项卡对应的文字
	//CharSequence与String都能用于定义字符串，但CharSequence的值是可读可写序列，而String的值是只读序列。
	private CharSequence[] toptab_Titles = {"已发布","未发布"};

	//选项卡的各个选项的view的集合：用于更改背景颜色
	private List<View> toptab_Items = new ArrayList<View>();
	//选项卡的各个选项的CheckedTextView的集合：用于切换时改变图标和文字颜色
	private List<CheckedTextView> topTab_checkeds = new ArrayList<CheckedTextView>();


	public TabTopLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		mContext = context;

		List<CharSequence> tab_titleList = new ArrayList<CharSequence>();
		tab_titleList = Arrays.asList(toptab_Titles);
		//初始化view：创建多个view对象（引用tab_top_item文件），设置图片和文字，然后添加到这个自定义类的布局中
		initAddBottomTabItemView(tab_titleList);
	}

	//初始化控件
	private void initAddBottomTabItemView(List<CharSequence> tabTitleList){

		int countChild = this.getChildCount();
		if(countChild > 0){
			this.removeAllViewsInLayout();//清空控件
			//将各个选项的view添加到集合中
			toptab_Items.clear();
			//将各个选项卡的各个选项的标题添加到集合中
			topTab_checkeds.clear();
		}


		//设置要添加的子布局view的参数
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		params.weight = 1;//在tab_bottom_item文件的根节点RelativeLayout中是无法添加的，而这个是必须要写上的，否则只会展现一个view
		params.gravity = Gravity.CENTER;

		for(int index=0;index<tabTitleList.size();index++){

			final int finalIndex = index;

			//============引用选项卡的各个选项的布局文件=================
			View toptabitemView = LayoutInflater.from(mContext).inflate(R.layout.tab_top_item, this, false);

			//===========设置CheckedTextView控件的图片和文字==========
			final CheckedTextView toptab_checkedTextView = (CheckedTextView) toptabitemView.findViewById(R.id.toptab_checkedTextView);

			//设置CheckedTextView的文字
			toptab_checkedTextView.setText(tabTitleList.get(index).toString());

			//===========设置CheckedTextView控件的Tag(索引)==========用于后续的切换更改图片和文字
			toptab_checkedTextView.setTag("tag"+index);

			//添加选项卡各个选项的触发事件监听
			toptabitemView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					//设置CheckedTextView状态为选中状态
					//修改View的背景颜色
					setTabsDisplay(finalIndex);
					//添加点击事件
					if(topTabSelectedListener != null){
						//执行activity主类中的onTopTabSelected方法
						topTabSelectedListener.onTopTabSelected(finalIndex);
					}
				}
			});

			//把这个view添加到自定义的MyBottomTab布局里面
			this.addView(toptabitemView,params);

			//将各个选项的view添加到集合中
			toptab_Items.add(toptabitemView);
			//将各个选项卡的各个选项的CheckedTextView添加到集合中
			topTab_checkeds.add(toptab_checkedTextView);
		}
	}

	/**
	 * 设置底部导航中图片显示状态和字体颜色
	 */
	public void setTabsDisplay(int checkedIndex) {

		int size = topTab_checkeds.size();

		for(int i=0;i<size;i++){
			CheckedTextView checkedTextView = topTab_checkeds.get(i);
			//设置CheckedTextView状态为选中状态
			if(checkedTextView.getTag().equals("tag"+checkedIndex)){
				checkedTextView.setChecked(true);
				//修改文字颜色
				checkedTextView.setTextColor(getResources().getColor(R.color.tab_text_selected_top));
				//修改view的背景颜色
				if(0 == i) {
					toptab_Items.get(i).setBackgroundResource(R.drawable.tab_top_layout_item_shape_left_selected);
				} else if (i == size - 1){
					toptab_Items.get(i).setBackgroundResource(R.drawable.tab_top_layout_item_shape_right_selected);
				} else {
					toptab_Items.get(i).setBackgroundResource(R.drawable.tab_top_layout_item_shape_mid_selected);
				}

			}else{
				checkedTextView.setChecked(false);
				//修改文字颜色
				checkedTextView.setTextColor(getResources().getColor(R.color.tab_text_normal_top));
				//修改view的背景颜色
				if(0 == i) {
					toptab_Items.get(i).setBackgroundResource(R.drawable.tab_top_layout_item_shape_left_unselected);
				} else if (i == size - 1){
					toptab_Items.get(i).setBackgroundResource(R.drawable.tab_top_layout_item_shape_right_unselected);
				} else {
					toptab_Items.get(i).setBackgroundResource(R.drawable.tab_top_layout_item_shape_mid_unselected);
				}
			}
		}
	}

	private OnTopTabSelectListener topTabSelectedListener;

	//自定义一个内部接口，用于监听选项卡选中的事件,用于获取选中的选项卡的下标值
	public interface OnTopTabSelectListener{
		void onTopTabSelected(int index);
	}

	public void setOnTopTabSelectedListener(OnTopTabSelectListener topTabSelectedListener){
		this.topTabSelectedListener = topTabSelectedListener;
	}
}
