package com.example.borderimagelibs;

import android.app.Activity;
import android.content.ClipData.Item;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity extends Activity {
	ImageView imageImv;
	ImageView textImv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Contour with image
		imageImv = (ImageView) findViewById(R.id.imv2);
		Bitmap icon = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		Border mBorderImg = new Border();
		imageImv.setImageBitmap(mBorderImg.process(icon));
		
		//contour with text
		imageImv = (ImageView) findViewById(R.id.imv4);
		Bitmap textImg = BitmapFactory.decodeResource(getResources(),
				R.drawable.text);
		Border mBorderText = new Border();

		imageImv.setImageBitmap(mBorderText.process(textImg));
	}

	
}
