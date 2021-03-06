package com.example.MenueEnd.View;




import com.example.MenueEnd.R;
import com.nineoldandroids.view.ViewHelper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
	/**
	 * 滚动发生时
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		//调用属性动画，设置TranslationX
		
		/*为什么要导入nineoldandroids的包呢，因为Android3.0以下的机子不支持
		 * ViewHelper动画，所以要兼容的话就需要导入这个包，来兼容ViewHelper动画
		 * */
		
		//l是当前scrollX的值

		float scale =l*1.0f/mMenuWidth;//1~0		

		/*
		 * 和之前的区别
		 * 内容区域1.0-0.7的缩放
		 * 0.7+0.3*scale
		 * 
		 * 菜单的偏移量
		 * 
		 * 菜单的显示有缩放和透明度
		 * 缩放 0.7-1	.0
		 * 1.0-0.3*scale
		 * 透明度0.6~1.0
		 * 1-0.4*scale
		 */
		float rightScale=0.7f+0.3f*scale;
		float leftScale=1.0f-0.3f*scale;
		float leftAlpha=1.0f-0.4f*scale;
//		先将Menu想右移动mMenuWidth个宽度，然后随着屏幕的左移，mMenu也跟着向左移动
		ViewHelper.setTranslationX(mMenu,mMenuWidth*scale*0.7f);
		
//		对菜单进行缩放以及透明度
		ViewHelper.setScaleX(mMenu, leftScale);
		ViewHelper.setScaleY(mMenu, leftScale);
		ViewHelper.setAlpha(mMenu, leftAlpha);
		
		
//		将中心点设置到左侧，这样他的偏移就不会向右了
		ViewHelper.setPivotX(mContent, 0);
		ViewHelper.setPivotY(mContent, mContent.getHeight()/2);
//		对主界面进行缩放
		ViewHelper.setScaleX(mContent, rightScale);
		ViewHelper.setScaleY(mContent, rightScale);
		
		
	}
}
