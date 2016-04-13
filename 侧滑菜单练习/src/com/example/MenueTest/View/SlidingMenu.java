package com.example.MenueTest.View;

import com.example.MenueTest.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlidingMenu extends HorizontalScrollView {

	private LinearLayout mWapper;
	private ViewGroup mMenu;
	private ViewGroup mContent;
	private int mScreenWidth;

	private int mMenuWidth;

	private boolean once = true;
	
	private boolean isOpen=false;
	
	// menu窗口与屏幕右侧的距离 单位:dp
	private int mMenuRightPadding = 50;

	/**
	 * 未使用自定义控件时，调用构造方法
	 * 
	 * @param context
	 * @param attrs
	 */
	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		
		
	}
	/**
	 * 当使用了自定义属性时，会调用此构造方法
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		//获取我们定义的属性
		TypedArray a=context.getTheme().obtainStyledAttributes
				(attrs, R.styleable.SlidingMenu, defStyle, 0);
		int n=a.getIndexCount();
		for(int i=0;i<n;i++){
			int attr=a.getIndex(i);
			switch (attr) {
			case R.styleable.SlidingMenu_rightPadding:
				mMenuRightPadding=(int) a.getDimension(
						attr, (int) TypedValue.applyDimension
						(TypedValue.COMPLEX_UNIT_DIP, 50, 
								context.getResources().
								getDisplayMetrics()));
				break;

			default:
				break;
			}
		}
		
		//释放
		a.recycle();
		//獲取屏幕寬度 單位dp
		WindowManager wm = (WindowManager) context
				.getSystemService(context.WINDOW_SERVICE);
		
		DisplayMetrics outMetrics = new DisplayMetrics();
		
		wm.getDefaultDisplay().getMetrics(outMetrics);
		
		mScreenWidth = outMetrics.widthPixels;
		
		
		// 将dp转换为单位px
//		mMenuRightPadding = (int) TypedValue.applyDimension(
//				TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources()
//						.getDisplayMetrics());
		
	}

	public SlidingMenu(Context context) {
		//这样做，不论如何都会执行三个参数的构造方法
		this(context,null);	
	}

	// 自定义 ViewGroup一般重写以下两个方法
	/**
	 * 决定内部View（子View）的宽和高，以及自己的宽和高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		//如果是第一次訪問
		if (once) {
			
			//獲取scrollView里的最外層的那個LinearLayout
			mWapper = (LinearLayout) getChildAt(0);
			
			//獲取LinearLayout里的第一個控件，也就是整個left_menu
			mMenu = (ViewGroup) mWapper.getChildAt(0);

			//獲取的二個控件，也就是主界面，LinearLayout
			mContent = (ViewGroup) mWapper.getChildAt(1);
			
			//設置Menu的寬度，也就是屏幕寬度減去右間距
			mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth
					- mMenuRightPadding;
			
			//設置主界面的寬度爲屏幕的寬度
			mContent.getLayoutParams().width = mScreenWidth;
			
			
			once = false;
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	/**
	 * 决定View的放置位置 通过设置偏移量，将Menu隐藏
	 * @param changed 该参数指出当前ViewGroup的尺寸或者位置是否发生了改变
	 * @param left top right bottom 当前ViewGroup相对于其父控件的坐标位置
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		
		//這裏chenged爲true的原因是onMeasure方法中我們對佈局的尺寸重新進行了賦值
		if (changed) {
			//向左滑動mMenuWidth的寬度以隱藏Menu界面。
			this.scrollTo(mMenuWidth, 0);
		}
	}
	/**
	 * 捕捉手勢
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		int action = ev.getAction();
		switch (action) {
		//當用戶手指抬起時
		case MotionEvent.ACTION_UP:
			/*隐藏在左边的宽度，實際上是在屏幕上顯示的x坐標的最小值，
			意思可能是还有多少宽度可以移动*/
			int scrollX = getScrollX();
			if (scrollX >= mMenuWidth / 2) {
				// 隐藏
				/*
				 * 这个方法比this.scrollTo(x, y)更加平滑
				 */
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen=false;
			} else {
				// 显示
				this.smoothScrollTo(0, 0);
				isOpen=true;
			}
			return true;


		}
		return super.onTouchEvent(ev);
	}
	/**
	 * 打开菜单
	 */
	public void openMenu(){
		if(isOpen) return;
		this.smoothScrollTo(0, 0);
		isOpen=true;	 
	}
	/**
	 * 关闭菜单
	 */
	public void closeMenu(){
		if(!isOpen) return;
		this.smoothScrollTo(mMenuWidth, 0);
		isOpen=false;	 
	}
	/**
	 * 切换菜单
	 */	
	public void toggle(){
		if(isOpen){
			closeMenu();
		}else{
			openMenu();
		}
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(isOpen){
			mContent.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					System.out.println(123);
				}
			});
		}
		return super.onInterceptTouchEvent(ev);
	}
}
