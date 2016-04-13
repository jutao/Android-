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
	
	// menu��������Ļ�Ҳ�ľ��� ��λ:dp
	private int mMenuRightPadding = 50;

	/**
	 * δʹ���Զ���ؼ�ʱ�����ù��췽��
	 * 
	 * @param context
	 * @param attrs
	 */
	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs,0);
		
		
	}
	/**
	 * ��ʹ�����Զ�������ʱ������ô˹��췽��
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		//��ȡ���Ƕ��������
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
		
		//�ͷ�
		a.recycle();
		//�@ȡ��Ļ���� ��λdp
		WindowManager wm = (WindowManager) context
				.getSystemService(context.WINDOW_SERVICE);
		
		DisplayMetrics outMetrics = new DisplayMetrics();
		
		wm.getDefaultDisplay().getMetrics(outMetrics);
		
		mScreenWidth = outMetrics.widthPixels;
		
		
		// ��dpת��Ϊ��λpx
//		mMenuRightPadding = (int) TypedValue.applyDimension(
//				TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources()
//						.getDisplayMetrics());
		
	}

	public SlidingMenu(Context context) {
		//��������������ζ���ִ�����������Ĺ��췽��
		this(context,null);	
	}

	// �Զ��� ViewGroupһ����д������������
	/**
	 * �����ڲ�View����View���Ŀ�͸ߣ��Լ��Լ��Ŀ�͸�
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		//����ǵ�һ���L��
		if (once) {
			
			//�@ȡscrollView�������ӵ��ǂ�LinearLayout
			mWapper = (LinearLayout) getChildAt(0);
			
			//�@ȡLinearLayout��ĵ�һ���ؼ���Ҳ��������left_menu
			mMenu = (ViewGroup) mWapper.getChildAt(0);

			//�@ȡ�Ķ����ؼ���Ҳ���������棬LinearLayout
			mContent = (ViewGroup) mWapper.getChildAt(1);
			
			//�O��Menu�Č��ȣ�Ҳ������Ļ���Ȝpȥ���g��
			mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth
					- mMenuRightPadding;
			
			//�O��������Č��Ƞ���Ļ�Č���
			mContent.getLayoutParams().width = mScreenWidth;
			
			
			once = false;
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	/**
	 * ����View�ķ���λ�� ͨ������ƫ��������Menu����
	 * @param changed �ò���ָ����ǰViewGroup�ĳߴ����λ���Ƿ����˸ı�
	 * @param left top right bottom ��ǰViewGroup������丸�ؼ�������λ��
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		
		//�@�Ychenged��true��ԭ����onMeasure�������҂����Ѿֵĳߴ������M�����xֵ
		if (changed) {
			//���󻬄�mMenuWidth�Č������[��Menu���档
			this.scrollTo(mMenuWidth, 0);
		}
	}
	/**
	 * ��׽�ք�
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		int action = ev.getAction();
		switch (action) {
		//���Ñ���ָ̧��r
		case MotionEvent.ACTION_UP:
			/*��������ߵĿ�ȣ����H��������Ļ���@ʾ��x���˵���Сֵ��
			��˼�����ǻ��ж��ٿ�ȿ����ƶ�*/
			int scrollX = getScrollX();
			if (scrollX >= mMenuWidth / 2) {
				// ����
				/*
				 * ���������this.scrollTo(x, y)����ƽ��
				 */
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen=false;
			} else {
				// ��ʾ
				this.smoothScrollTo(0, 0);
				isOpen=true;
			}
			return true;


		}
		return super.onTouchEvent(ev);
	}
	/**
	 * �򿪲˵�
	 */
	public void openMenu(){
		if(isOpen) return;
		this.smoothScrollTo(0, 0);
		isOpen=true;	 
	}
	/**
	 * �رղ˵�
	 */
	public void closeMenu(){
		if(!isOpen) return;
		this.smoothScrollTo(mMenuWidth, 0);
		isOpen=false;	 
	}
	/**
	 * �л��˵�
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
