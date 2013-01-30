package com.example.drag_dropexample;

import java.util.LinkedList;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnDragListener {

	private final String				ICON = "icon";
	private final String				TAG = "element";	
	private LinkedList<LinearLayout>	layouts = new LinkedList<LinearLayout>();
	private LinearLayout.LayoutParams	linearParams;
	private LayoutInflater				inflater;
	private GridLayout 					gridLeft;
	private ScrollView					scrollView;
	private String[]					description;
	private TypedArray					imgs, icons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		description	= getResources().getStringArray(R.array.description);
		imgs		= getResources().obtainTypedArray(R.array.img_views);
		icons		= getResources().obtainTypedArray(R.array.icons);
		scrollView	= (ScrollView)findViewById(R.id.scrollViewLeft);
		gridLeft	= (GridLayout)findViewById(R.id.grid);

		int nImgView = imgs.length();
		for(int i=0; i<nImgView; i++){
			int rID			= imgs.getResourceId(i, 0);
			int rIcon		= icons.getResourceId(i%icons.length(), 0);
			ImageView img	= (ImageView)findViewById(rID);
			img.setImageResource(rIcon);
			img.setTag(new DDTag(rIcon, description[i%description.length]));
			img.setOnTouchListener(new ElementTouchListener());
		}

		linearParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
													LinearLayout.LayoutParams.MATCH_PARENT);
		linearParams.bottomMargin = 20;
		
		layouts.add(buildRow(new DDTag(icons.getResourceId(0, 0), description[0]), false));
		layouts.add(buildRow(new DDTag(icons.getResourceId(0, 0), description[1]), false));
		layouts.add(buildRow(new DDTag(icons.getResourceId(0, 0), description[2]), false));
		
		fillGridLayout(0);
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
	private LinearLayout buildRow(DDTag tag, boolean added){
		final LinearLayout	l 	= (LinearLayout) inflater.inflate(R.layout.filled_row, null);
		Button				b	= (Button)		l.findViewById(R.id.button);
		TextView			t	= (TextView)	l.findViewById(R.id.text);
		ImageView			img	= (ImageView)	l.findViewById(R.id.image);

		l.setTag(tag);
		b.setText("Push");
		t.setText(tag.getrDescription());
		img.setImageResource(tag.getrImage());

		if(added){
			Button bCancel = new Button(this);
			bCancel.setText("X");
			bCancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					layouts.remove(l);
					fillGridLayout(0);
				}
			});
			l.addView(bCancel);
		}
		return l;
	}

	/**
	 * inflate and parameterizing new empty LinearLayout line
	 * @param i
	 * @return
	 */
	private LinearLayout getNewEmptyLine(int i){
		LinearLayout empty_el = (LinearLayout) inflater.inflate(R.layout.empty_row, null);
		empty_el.setTag(i);
		empty_el.setOnDragListener(this);
		return empty_el;
	}

	/**
	 * Refill the left side grid with all the entries from layouts LinkedList<LinearLayout>
	 */
	private void fillGridLayout(int scroll){
		int i = 0;
		LinearLayout element;
		gridLeft.removeAllViews();
		for(i=0; i<layouts.size(); i++){
			element = layouts.get(i);
			gridLeft.addView(getNewEmptyLine(i));
			gridLeft.addView(element);
		}
		gridLeft.addView(getNewEmptyLine(i));
		if(scroll != 0){
			int value = scroll * 5;
			scrollView.requestFocus();
			scrollView.scrollTo(scroll, value);
			Log.d("SCROLL", "" + value);
		}
	}

	/**
	 * Element's list Listener
	 */
	private class ElementTouchListener implements OnTouchListener {
		ClipData 			data;
		Intent 				intent;
		DragShadowBuilder 	shBuilder;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				intent		= new Intent();
				data		= ClipData.newPlainText("CLIP", ((DDTag)v.getTag()).getrDescription());
				shBuilder	= new DragShadowBuilder(v);

				intent.putExtra(ICON, ((DDTag)v.getTag()).getrImage());
				data.addItem(new ClipData.Item(intent));

				v.startDrag(data, shBuilder, v, 0);
				v.setVisibility(View.VISIBLE);
				break;
			default:
				return false;
			}
			return true;
		}
	}

	/**
	 * DragListener method that manage single element grid destination
	 */
	@Override
	public boolean onDrag(View v, DragEvent event) {
		Drawable starDrag	= getResources().getDrawable(R.drawable.start_drag);	//effect showed on the drag's beginning 
		Drawable dragOver	= getResources().getDrawable(R.drawable.drag_over);		//effect showed on the drag's selected

		switch (event.getAction()) {
		case DragEvent.ACTION_DRAG_STARTED:
			v.setBackgroundDrawable(starDrag);
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			v.setBackgroundDrawable(dragOver);
			break;
		case DragEvent.ACTION_DROP:
			//Item 0: Description
			//Item 1: Icon
			DDTag dtTag = new DDTag(event.getClipData().getItemAt(1).getIntent().getIntExtra(ICON, 0),
									event.getClipData().getItemAt(0).getText().toString());
			//getTag from LinearLayout
			int index = (Integer)v.getTag();
			layouts.add(index, buildRow(dtTag, true));
			fillGridLayout(index);
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
