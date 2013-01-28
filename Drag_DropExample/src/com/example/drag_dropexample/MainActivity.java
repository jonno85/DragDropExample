package com.example.drag_dropexample;

import java.util.LinkedList;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
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
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnDragListener {

	public final String 				TAG = "element";	
	private LinkedList<LinearLayout>	layouts = new LinkedList<LinearLayout>();
	private LinearLayout.LayoutParams	linearParams;
	private LayoutInflater				inflater;
	private GridLayout 					gridLeft;
	private Integer						index = 0;
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
		gridLeft	= (GridLayout)findViewById(R.id.grid);
		
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
			int rID		= imgs.getResourceId(i, 0);
			int rIcon	= icons.getResourceId(i%icons.length(), 0);
			ImageView img = (ImageView)findViewById(rID);
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
	private LinearLayout buildRow(DDTag tag, boolean added){
		LinearLayout	l = (LinearLayout) inflater.inflate(R.layout.filled_row, null);
		Button b		= (Button)		l.findViewById(R.id.button);
		TextView t		= (TextView)	l.findViewById(R.id.text);
		ImageView img	= (ImageView)	l.findViewById(R.id.image);

		// CHECK WHY IS NOT WORKING
		Log.i("IMAGEVIEW id", ""+tag.getrImage());
		img.setImageResource(tag.getrImage());

		if(tag.getProbablyInt() != -1){
			tag.setProbablyInt(index++);
		}

		l.setTag(tag);
		b.setText("Push");
		t.setText(tag.getrDescription());

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
		LinearLayout empty_el = null, element;
		index = 0;
		gridLeft.removeAllViews();
		for(i=0, j=0; i<layouts.size(); i++){
			empty_el = (LinearLayout) inflater.inflate(R.layout.empty_row, null);
			empty_el.setTag(new DDTag(i));
			empty_el.setOnDragListener(this);
			element = layouts.get(i);

			gridLeft.addView(empty_el, j++);
			gridLeft.addView(element, j++);
		}
		empty_el = (LinearLayout) inflater.inflate(R.layout.empty_row, null);
		empty_el.setTag(new DDTag(i));
		empty_el.setOnDragListener(this);
		gridLeft.addView(empty_el, j);
	}

	/**
	 * Element's list Listener
	 * @author F31999A
	 *
	 */
	private class ElementTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				ClipData data = ClipData.newPlainText("CLIP", ((DDTag)v.getTag()).getrDescription());
				Intent intent = new Intent();
				intent.putExtra("ICON", ((DDTag)v.getTag()).getrImage());
				data.addItem(new ClipData.Item(intent));

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
			DDTag tag = new DDTag(event.getClipData().getItemAt(1).getIntent().getIntExtra("ICON", 0), event.getClipData().getItemAt(0).getText().toString());
			DDTag tag1 = (DDTag)v.getTag(); //TAG from LinearLayout
			Log.i("ACTION_DROP IMG GetTag id", ""+tag.getrImage());
			layouts.add(tag1.getProbablyInt(), buildRow(tag, true));
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
