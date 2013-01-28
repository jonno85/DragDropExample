package com.example.drag_dropexample;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnDragListener {

	public final String 				TAG = "element";	
	private FrameLayout					frameLeft;
	private ArrayList<LinearLayout>		layouts = new ArrayList<LinearLayout>();
	private LinearLayout.LayoutParams	linearParams;
	private LayoutInflater				inflater;
	private GridLayout 					gridLeft;
	private Integer						index = 0;
	private String[]					description;
	private TypedArray					imgs;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		description	= getResources().getStringArray(R.array.description);
		imgs		= getResources().obtainTypedArray(R.array.img_views);
		
		
		ImageView img1 = (ImageView)findViewById(R.id.myimage1);
		img1.setTag(description[0]);
		img1.setOnTouchListener(new ElementTouchListener());
		ImageView img2 = (ImageView)findViewById(R.id.myimage2);
		img2.setTag(description[1]);
		img2.setOnTouchListener(new ElementTouchListener());
		ImageView img3 = (ImageView)findViewById(R.id.myimage3);
		img3.setTag(description[2]);
		img3.setOnTouchListener(new ElementTouchListener());
		
		int nImgView = imgs.length();
		for(int i=0; i<nImgView; i++){
			Log.i("VALUE AT ", ""+imgs.getResourceId(i, 0));
			findViewById(imgs.getResourceId(i, 0)).setOnTouchListener(new ElementTouchListener());
		}

		frameLeft = (FrameLayout)findViewById(R.id.frameLeft);
		
		linearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
													LinearLayout.LayoutParams.MATCH_PARENT);
		linearParams.bottomMargin = 20;
		
		layouts.add(0, buildRow(description[0], 0, false));
		layouts.add(1, buildRow(description[1], 0, false));
		layouts.add(2, buildRow(description[2], 0, false));
		
		gridLeft = (GridLayout)inflater.inflate(R.layout.customizable_list, null);
		
		frameLeft.addView(gridLeft);
		
		fillGridLayout();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * build up a new line layout to add to the grid
	 * @param text
	 * @return
	 */
	private LinearLayout buildRow(String text, int resIcon, boolean added){
		LinearLayout l = (LinearLayout) inflater.inflate(R.layout.filled_row, null);
		
		Button b	= (Button)l.findViewById(R.id.button);
		TextView t	= (TextView)l.findViewById(R.id.text);

		if(resIcon != 0){
			ImageView img = (ImageView)findViewById(resIcon);
		}

		l.setTag(index++);
		b.setText("Push");
		t.setText(text);

		if(added){
			Button bCancel = new Button(this);
			bCancel.setText("X");
			bCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//layouts.remove(l.getTag());
					//fillGridLayout();
				}
			});
			l.addView(bCancel);
		}
		return l;
	}

	private void fillGridLayout(){
		int i, j;
		index = 0;
		gridLeft.removeAllViews();
		for(i=0, j=0; i<layouts.size(); i++){
			LinearLayout empty_el = (LinearLayout) inflater.inflate(R.layout.empty_row, null);
			empty_el.setTag(j);
			empty_el.setOnDragListener(this);
			gridLeft.addView(empty_el, j++);
			LinearLayout element = layouts.get(i);
			gridLeft.addView(element, j++);
		}
		LinearLayout empty_el = (LinearLayout) inflater.inflate(R.layout.empty_row, null);
		empty_el.setTag(j);
		empty_el.setOnDragListener(this);
		gridLeft.addView(empty_el, j);
	}
	
	private class ElementTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				ClipData data = ClipData.newPlainText("CLIP", (CharSequence) v.getTag());
				
				
				//data.addItem(new ClipData.Item(text));
				DragShadowBuilder shadowBuilder = new DragShadowBuilder(v);
				v.startDrag(data, shadowBuilder, v, 0);
				v.setVisibility(View.VISIBLE);
				return true;
			default:
				return false;
			}
		}
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		Drawable starDrag		= getResources().getDrawable(R.drawable.start_drag);	//effect showed on the drag's beginning 
		Drawable dragOver		= getResources().getDrawable(R.drawable.drag_over);		//effect showed on the drag's selected
		
		int action = event.getAction();

		switch (action) {
		case DragEvent.ACTION_DRAG_STARTED:
			v.setBackgroundDrawable(starDrag);
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			v.setBackgroundDrawable(dragOver);
			break;
		case DragEvent.ACTION_DROP:
			String s = "";
			int resId = 0;
			if(event.getClipData().getItemAt(0).getText() != null){
				s		= event.getClipData().getItemAt(0).getText().toString();
				resId	= Integer.parseInt(event.getClipData().getItemAt(1).getText().toString());
			} else {
				s = new String("no Description associated");
			}
			//gridLeft.addView(buildRow(s));
			Log.i("DIMMI CHE TAG HAI", ""+v.getTag());
			layouts.add(Integer.parseInt((String) v.getTag().toString()), buildRow(s, resId, true));
			fillGridLayout();
			v.setBackgroundDrawable(null);
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			v.setBackgroundDrawable(starDrag);
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			v.setBackgroundDrawable(null);
			break;
		}
		return true;
	}
}
