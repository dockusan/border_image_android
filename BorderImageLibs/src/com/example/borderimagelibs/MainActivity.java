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
		imageImv.setImageBitmap(addBorder(icon, mBorderImg));
		
		//contour with text
		imageImv = (ImageView) findViewById(R.id.imv4);
		Bitmap textImg = BitmapFactory.decodeResource(getResources(),
				R.drawable.text);
		Border mBorderText = new Border();

		imageImv.setImageBitmap(addBorder(textImg, mBorderText));
	}

	public static Bitmap addBorder(Bitmap src, Border mBorder) {
		int width = src.getWidth();
		int height = src.getHeight();
		Log.e("src size: ", " " + src.getWidth() + " va " + src.getHeight());
		float borderSize = 0;
		// sizeBorder From 0 -> 50
		float sizeBorder = mBorder.getBorderSize();
		int sizeActual = 0;
		if (width > height) {
			sizeActual = height;
		} else {
			sizeActual = width;
		}

		Paint mPaint = new Paint();
		
		// Border size = 0 -> 3% width or height Bitmap with small bitmap
		float newSize = sizeActual * 0.3f;
		if (sizeActual < 150f) {
			borderSize = (sizeBorder / 50f) * newSize;
		} else {
			borderSize = sizeBorder;
		}
		mPaint.setStrokeWidth(borderSize);

		mPaint.setColor(mBorder.getColor());
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setFilterBitmap(true);
		mPaint.setStyle(Style.STROKE);

		/*
		 * You should store this Path for next time draw
		 */
		if (!mBorder.isGetPath()) {
			Log.e("is Run ", "is Run ");
			// init paint to get extract alpha bitmap
			Paint mPainting = new Paint();
			mPainting.setDither(true);
			mPainting.setAntiAlias(true);
			mPainting.setColor(Color.BLACK);
			int[] offset = new int[2];

			Bitmap bmAlpha = src.extractAlpha(mPainting, offset);
			Bitmap resultBitmap = Bitmap.createBitmap(width, height,
					Config.ARGB_8888);
			Canvas mCanvas = new Canvas(resultBitmap);

			Paint mainPaint = new Paint();
			mainPaint.setDither(true);
			mainPaint.setAntiAlias(true);
			mainPaint.setFilterBitmap(true);
			mainPaint.setStrokeJoin(Paint.Join.ROUND);
			mainPaint.setStrokeCap(Paint.Cap.SQUARE);
			mCanvas.drawBitmap(bmAlpha, 0, 0, mainPaint);

			if (bmAlpha != null && !bmAlpha.isRecycled()) {
				bmAlpha.recycle();
			}

			// Find contour
			ContourTracer tracer = new ContourTracer(resultBitmap);
			mBorder.setOuterContours(tracer.getOuterContours());
			mBorder.setInnerContours(tracer.getInnerContours());

			mBorder.setOuterPath(Contour.makePolygons(mBorder
					.getOuterContours()));
			mBorder.setInnerPath(Contour.makePolygons(mBorder
					.getInnerContours()));
			if (resultBitmap != null && !resultBitmap.isRecycled()) {
				resultBitmap.recycle();
			}
			mBorder.setGetPath(true);
		}

		Bitmap finalBitmap = Bitmap.createBitmap(src.getWidth(),
				src.getHeight(), src.getConfig());
		Matrix mMatrix = new Matrix();
		mMatrix.setScale((width - borderSize) / (width * 1f),
				(height - borderSize) / (height * 1f), width / 2f, height / 2f);

		Canvas canvasBorder = new Canvas(finalBitmap);

		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);

		for (Path p : mBorder.getOuterPath()) {

			Path temp = new Path(p);
			temp.transform(mMatrix);

			canvasBorder.drawPath(temp, mPaint);
		}
		for (Path p : mBorder.getInnerPath()) {
			Path temp = new Path(p);
			temp.transform(mMatrix);
			canvasBorder.drawPath(temp, mPaint);
		}
		canvasBorder.drawBitmap(src, mMatrix, mPaint);// draw

		return finalBitmap;

	}
}
