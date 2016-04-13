package com.example.MenueEnd;


import com.example.MenueEnd.View.SlidingMenu;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;


public class MainActivity extends Activity {
	SlidingMenu slidingmenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        slidingmenu=(SlidingMenu) findViewById(R.id.id_menu);
    }
    public  void toogleMenu(View view){
    	slidingmenu.toggle();
    }
}
