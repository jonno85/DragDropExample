package com.example.drag_dropexample;

import java.util.LinkedList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class RunScreenScrollView extends ScrollView implements OnDragListener{

	private int	yScroll = 0;
	private int	scrollTo = 0;
	
	public LinkedList<LinearLayout>	layouts = new LinkedList<LinearLayout>();

	public RunScreenScrollView(Context context) {
		super(context);
	}

	public RunScreenScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public RunScreenScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		int pos = (int) v.getY();
		Log.e("RunScreenScrollView", "position: " + pos);
		return true;
	}
}
