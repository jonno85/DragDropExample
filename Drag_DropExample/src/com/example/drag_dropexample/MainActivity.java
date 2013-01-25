package com.example.drag_dropexample;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
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

public class MainActivity extends Activity {

	public final String 			TAG = "element";	
	private String[]				description;
	private FrameLayout				frameLeft;
	private TextView[]				texts;
	private ArrayList<LinearLayout>	layouts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		description = getResources().getStringArray(R.array.description);
		
		ImageView img1 = (ImageView)findViewById(R.id.myimage1);
		img1.setTag(description[0]);
		img1.setOnTouchListener(new ElementTouchListener());
		ImageView img2 = (ImageView)findViewById(R.id.myimage2);
		img2.setTag(description[1]);
		img2.setOnTouchListener(new ElementTouchListener());
		ImageView img3 = (ImageView)findViewById(R.id.myimage3);
		img3.setTag(description[2]);
		img3.setOnTouchListener(new ElementTouchListener());
		
		findViewById(R.id.myimage3).setOnTouchListener(new ElementTouchListener());
		findViewById(R.id.myimage4).setOnTouchListener(new ElementTouchListener());
		findViewById(R.id.myimage5).setOnTouchListener(new ElementTouchListener());
		findViewById(R.id.myimage6).setOnTouchListener(new ElementTouchListener());
		findViewById(R.id.myimage7).setOnTouchListener(new ElementTouchListener());
		findViewById(R.id.myimage8).setOnTouchListener(new ElementTouchListener());
		findViewById(R.id.myimage9).setOnTouchListener(new ElementTouchListener());
		findViewById(R.id.myimage10).setOnTouchListener(new ElementTouchListener());
		findViewById(R.id.myimage11).setOnTouchListener(new ElementTouchListener());
		findViewById(R.id.myimage12).setOnTouchListener(new ElementTouchListener());
		findViewById(R.id.myimage13).setOnTouchListener(new ElementTouchListener());
		findViewById(R.id.myimage14).setOnTouchListener(new ElementTouchListener());
		findViewById(R.id.myimage15).setOnTouchListener(new ElementTouchListener());
		findViewById(R.id.myimage16).setOnTouchListener(new ElementTouchListener());
		findViewById(R.id.myimage17).setOnTouchListener(new ElementTouchListener());
		findViewById(R.id.myimage18).setOnTouchListener(new ElementTouchListener());
		findViewById(R.id.myimage19).setOnTouchListener(new ElementTouchListener());
		findViewById(R.id.myimage20).setOnTouchListener(new ElementTouchListener());
		
		frameLeft = (FrameLayout)findViewById(R.id.frameLeft);
			
		texts	= new TextView[10];
		layouts = new ArrayList<LinearLayout>();
		
		texts[0] = new TextView(this);
		texts[0].setText("prima riga");
		
		texts[1] = new TextView(this);
		texts[1].setText("seconda riga");
		
		texts[2] = new TextView(this);
		texts[2].setText("terza riga");
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
																	LinearLayout.LayoutParams.MATCH_PARENT);
		lp.bottomMargin = 20;
		   
		LinearLayout l = new LinearLayout(this);
		l.addView(texts[0], lp);
		l.addView(new Button(this), lp);
		
		layouts.add(l);
		
		l = new LinearLayout(this);	
		l.addView(texts[1], lp);
		l.addView(new Button(this), lp);
		layouts.add(l);		
		
		l = new LinearLayout(this);
		l.addView(texts[2], lp);
		l.addView(new Button(this), lp);
		layouts.add(l);
		
		Log.e("NUMERO ELEMENTI", " " + layouts.size());
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		GridLayout gridLeft = (GridLayout)inflater.inflate(R.layout.customizable_list, null);
		
		for(int i=0; i<layouts.size(); i++){
			LinearLayout element	= layouts.get(i);
			gridLeft.addView(element);
			LinearLayout empty_el = (LinearLayout) inflater.inflate(R.layout.empty_row, null);
			empty_el.setTag("empty");
			empty_el.setOnDragListener(new LineDragListener());
			gridLeft.addView(empty_el);
		}
		frameLeft.addView(gridLeft);
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	
	private class ElementTouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				
				ClipData data = ClipData.newPlainText("CLIP", (CharSequence) v.getTag());
				
				DragShadowBuilder shadowBuilder = new DragShadowBuilder(v);
				v.startDrag(data, shadowBuilder, v, 0);
				v.setVisibility(View.INVISIBLE);
				return true;
			default:
				return false;
			}
		}
	}
	
	private class LineDragListener implements OnDragListener{

		Drawable starDrag = getResources().getDrawable(R.drawable.start_drag);
		Drawable emptyRow = getResources().getDrawable(R.drawable.empty_row);
		Drawable clearRow = getResources().getDrawable(R.drawable.clear_row);
		private String description;
		@Override
		public boolean onDrag(View v, DragEvent event) {
			int action = event.getAction();
					
			
			switch (action) {
			case DragEvent.ACTION_DRAG_STARTED:
				//Draggable
				if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){
					v.setBackgroundDrawable(starDrag);
					return true;		
				} else {	// Not Draggable
					return false;
				}
			case DragEvent.ACTION_DRAG_ENTERED:
				v.setBackgroundDrawable(emptyRow);
				break;
			case DragEvent.ACTION_DROP:
				//CLIP DATA reachable only in ACTION_DROP
				int n = event.getClipData().getItemCount();
				Log.e("DROPPED", "testo annesso " + description + " CLIP ITEM " + n);
			case DragEvent.ACTION_DRAG_EXITED:
				v.setBackgroundDrawable(clearRow);
				break;
			default:
				break;
			}
			return true;
		}
		
	}
	
}
