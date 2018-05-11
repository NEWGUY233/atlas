package com.atlas.crmapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atlas.crmapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 流失布局
 * 
 * @author HaiPeng
 * 
 */
public class FlowLayout extends ViewGroup {
	/**
	 * 创建一个键值对类型的集合 来存储子布局的位置
	 */
	private HashMap<Integer, Rect> childLocationList;
	private int downX;
	private int downY;
	private long downTime;
	private OnLabelSelectedListener onLabelSelectedListener;
	private int back_selected;// 选中时的背景资源id
	private int back_unselected;// 未选中时的背景资源id

	private int tvSelectColor = R.color.white_color ;
	private int tvUnSelectColor = R.color.gray_simple;

	public FlowLayout(Context context) {
		this(context, null);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public void setTvColor(int tvSelectColor, int tvUnSelectColor){
		this.tvSelectColor = tvSelectColor;
		this.tvUnSelectColor = tvUnSelectColor;
	}

	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		childLocationList = new HashMap<Integer, Rect>();
		// 获取自定义属性的值
		TypedArray typedArray = context.getTheme().obtainStyledAttributes(
				attrs, R.styleable.FlowLayout, defStyle, 0);
		back_selected = typedArray.getResourceId(
				R.styleable.FlowLayout_flow_selected, 0);// 选中时的背景资源id
		back_unselected = typedArray.getResourceId(
				R.styleable.FlowLayout_flow_un_selected, 0);// 未选中时的背景资源id
		typedArray.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// 父控件传进来的宽度和高度以及对应的测量模式
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

		// 如果当前ViewGroup的宽高为wrap_content的情况
		int width = 0;// 自己测量的 宽度
		int height = 0;// 自己测量的高度
		// 记录每一行的宽度和高度
		int lineWidth = 0;
		int lineHeight = 0;

		// 获取子view的个数
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			child.setBackgroundResource(back_unselected);
			// 测量子View的宽和高
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			// 得到LayoutParams
			MarginLayoutParams lp = (MarginLayoutParams) child
					.getLayoutParams();
			// 子View占据的宽度
			int childWidth = child.getMeasuredWidth() + lp.leftMargin
					+ lp.rightMargin;
			// 子View占据的高度
			int childHeight = child.getMeasuredHeight() + lp.topMargin
					+ lp.bottomMargin;
			// 换行时候
			if (lineWidth + childWidth > sizeWidth) {
				// 对比得到最大的宽度
				width = Math.max(width, lineWidth);
				// 重置lineWidth
				lineWidth = childWidth;
				// 记录行高
				height += lineHeight;
				lineHeight = childHeight;
			} else {// 不换行情况
					// 叠加行宽
				lineWidth += childWidth;
				// 得到最大行高
				lineHeight = Math.max(lineHeight, childHeight);
			}
			// 处理最后一个子View的情况
			if (i == childCount - 1) {
				width = Math.max(width, lineWidth);
				height += lineHeight;
			}
		}
		// wrap_content
		setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth
				: width, modeHeight == MeasureSpec.EXACTLY ? sizeHeight
				: height);
	}

	// 存储所有子View
	private List<List<View>> mAllChildViews = new ArrayList<List<View>>();
	// 每一行的高度
	private List<Integer> mLineHeight = new ArrayList<Integer>();

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		mAllChildViews.clear();
		mLineHeight.clear();
		// 获取当前ViewGroup的宽度
		int width = getWidth();

		int lineWidth = 0;
		int lineHeight = 0;
		// 记录当前行的view
		List<View> lineViews = new ArrayList<View>();
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = getChildAt(i);
			MarginLayoutParams lp = (MarginLayoutParams) child
					.getLayoutParams();
			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();

			// 如果需要换行
			if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width) {
				// 记录LineHeight
				mLineHeight.add(lineHeight);
				// 记录当前行的Views
				mAllChildViews.add(lineViews);
				// 重置行的宽高
				lineWidth = 0;
				lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
				// 重置view的集合
				lineViews = new ArrayList();
			}
			lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
			lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
					+ lp.bottomMargin);
			lineViews.add(child);
		}
		// 处理最后一行
		mLineHeight.add(lineHeight);
		mAllChildViews.add(lineViews);

		// 设置子View的位置
		int left = 0;
		int top = 0;
		// 获取行数
		int lineCount = mAllChildViews.size();
		for (int i = 0; i < lineCount; i++) {
			// 当前行的views和高度
			lineViews = mAllChildViews.get(i);
			lineHeight = mLineHeight.get(i);
			for (int j = 0; j < lineViews.size(); j++) {
				View child = lineViews.get(j);
				// 判断是否显示
				if (child.getVisibility() == View.GONE) {
					continue;
				}
				MarginLayoutParams lp = (MarginLayoutParams) child
						.getLayoutParams();
				int cLeft = left + lp.leftMargin;
				int cTop = top + lp.topMargin;
				int cRight = cLeft + child.getMeasuredWidth();
				int cBottom = cTop + child.getMeasuredHeight();
				// 进行子View进行布局
				child.layout(cLeft, cTop, cRight, cBottom);
				left += child.getMeasuredWidth() + lp.leftMargin
						+ lp.rightMargin;
			}
			left = 0;
			top += lineHeight;
		}
		getAllChildViewLocation();
	}

	/**
	 * 获取所有的子控件的位置并记录
	 */
	private void getAllChildViewLocation() {
		int countBefore = 0;
		for (int i = 0; i < mAllChildViews.size(); i++) {
			if (i > 0) {
				countBefore += mAllChildViews.get(i - 1).size();
			}
			for (int j = 0; j < mAllChildViews.get(i).size(); j++) {
				View view = mAllChildViews.get(i).get(j);
				Rect rect = new Rect(view.getLeft(), view.getTop(),
						view.getRight(), view.getBottom());
				childLocationList.put(countBefore + j, rect);
			}
		}

	}

	/**
	 * 与当前ViewGroup对应的LayoutParams
	 */
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		// TODO Auto-generated method stub

		return new MarginLayoutParams(getContext(), attrs);
	}

	/**
	 * 这里我们需要判断子控件是否被点击 点击的是哪个子控件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			downY = (int) event.getY();
			downTime = SystemClock.currentThreadTimeMillis();
			break;
		case MotionEvent.ACTION_UP:
			long upTime = SystemClock.currentThreadTimeMillis();
			if (upTime - downTime <= 50) {// 如果手指按下和手指离开键盘的时间小于50毫秒有效
				for (int i = 0; i < childLocationList.size(); i++) {
					if (childLocationList.get(i).contains(downX, downY)) {// 如果子控件所在位置(矩形)包括这个点

						setSelcetTextView(i);
						break;
					}
				}
			}
			break;

		default:
			break;
		}
		return true;
	}


	/**
	 * 子控件(标签)选中监听
	 * 
	 * @author HaiPeng
	 * 
	 */
	public interface OnLabelSelectedListener {
		void onSelected(int position, TextView selectTextView);
	}

	/**
	 * 设置子控件选中时的监听
	 * 
	 * @param onLabelSelectedListener
	 */
	public void setOnLabelSelectedListener(
			OnLabelSelectedListener onLabelSelectedListener) {
		this.onLabelSelectedListener = onLabelSelectedListener;
	}

	/**
	 * 记录上一次点击的是哪个子控件
	 */
	private int lastSelectedPosition = -1;

	public void setSelcetTextView(int i){
		// 将记录的上一个控件颜色改成未选中状态
		if (lastSelectedPosition != -1) {
			TextView lastSelectedView = (TextView) getChildAt(lastSelectedPosition);
			lastSelectedView.setBackgroundResource(back_unselected);
			lastSelectedView.setTextColor(ContextCompat.getColor(getContext(), tvUnSelectColor));
		}
		// 将当前选中的控件背景改成选中状态 并记录
		TextView childAt = (TextView) getChildAt(i);
		childAt.setBackgroundResource(back_selected);
		childAt.setTextColor(ContextCompat.getColor(getContext(), tvSelectColor));

		if (onLabelSelectedListener != null) {
			onLabelSelectedListener.onSelected(i, childAt);
		}
		lastSelectedPosition = i;
	}
}
