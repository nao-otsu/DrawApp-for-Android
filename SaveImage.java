package com.example.boulder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.util.Log;

public class SaveImage {

	public static void save(String filePath, Bitmap mBitmap2) {
		// TODO 自動生成されたメソッド・スタブ
		Log.d("tag","save is called");
		FileOutputStream stream = null;
	    try {
	        stream = new FileOutputStream(filePath);
	        mBitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream);
	        stream.close();
	    } catch (FileNotFoundException e1) {
	        e1.printStackTrace();
	    } catch (IOException e) {
			e.printStackTrace();
		}
	}
}
