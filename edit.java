package com.example.boulder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

@SuppressLint("SimpleDateFormat") 
public class edit extends Activity implements OnTouchListener {
	
	private Canvas canvas;
	private Paint paint;
	private float posX,posX2;
	private float posY,posY2;
	float s_x,s_y;
	ImageView mImageView;
	Bitmap mBitmap,mBitmap2;	
	
	public void onCreate(Bundle savedInstanceState) {
		Log.d("tag","edit is called");
		super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        
        mImageView = (ImageView)findViewById(R.id.imageView1);
        
        Bundle bundle = getIntent().getExtras();
        Uri uriSelectImage = (Uri)bundle.get("extra_uri");
        InputStream is = null;
        
        try{
        	
        	//mBitmap2 = (Bitmap) mImageView.getTag();
        	if(mBitmap2 != null){
        		Log.d("tag","check1 is called");
        		mBitmap2.recycle();
        		//mImageView.setTag(null);
        		mImageView.setImageBitmap(null);
        	}
        	if(uriSelectImage != null){
        		Log.d("tag","check2 is called");
        		is = this.getContentResolver().openInputStream(uriSelectImage);
        		mBitmap = BitmapFactory.decodeStream(is);
        		
        		if (metrics != null) {
        		    // 画像の大きさを最適化する
        		    Log.d("TAG", "before mBitmap.getWidth() = " + String.valueOf(mBitmap.getWidth()));
        		    Log.d("TAG", "before mBitmap.getHeight() = " + String.valueOf(mBitmap.getHeight()));
        		    float s_x = (float)mBitmap.getWidth() / (float)metrics.widthPixels;
        		    float s_y = (float)mBitmap.getHeight() / (float)metrics.heightPixels;
        		    Log.d("TAG", "s_x = " + String.valueOf(s_y));
        		    Log.d("TAG", "s_y = " + String.valueOf(s_y));
        		    float scale = Math.max(s_x, s_y);
        		    if (scale > 1){
        		        int new_x = (int)(mBitmap.getWidth() / scale);
        		        int new_y = (int)(mBitmap.getHeight() / scale);
        		        if(new_x < new_y){
        		        	double scale1 = scale*1.5;
        		        	int ne_x = (int)(mBitmap.getWidth() / scale1);
            		        int ne_y = (int)(mBitmap.getHeight() / scale1);
            		        Log.d("TAG", "ne_x = " + String.valueOf(ne_x));
            		        Log.d("TAG", "ne_y = " + String.valueOf(ne_y));
            		        mBitmap2 = Bitmap.createScaledBitmap(mBitmap,ne_x,ne_y,false);
        		        }else{
        		        Log.d("TAG", "new_x = " + String.valueOf(new_x));
        		        Log.d("TAG", "new_y = " + String.valueOf(new_y));
        		        mBitmap2 = Bitmap.createScaledBitmap(mBitmap,new_x,new_y,false);
        		        }
        		    }else{
        		    	mBitmap2 = Bitmap.createScaledBitmap(mBitmap,(int)mBitmap.getWidth(),(int)mBitmap.getHeight(),false);
        		    }
        		}
    			if(mBitmap != null){
                		mBitmap.recycle();
                }
    		 	mImageView.setTag(mBitmap2);
    		 	mImageView.setImageBitmap(mBitmap2);
    		 	Log.d("tag","check3 is called");
    		 	
        	}
    	   }catch(IOException e){
    	   e.printStackTrace();
    	   }
        // Canvasの作成:描画先のBitmapを与える
        canvas = new Canvas(mBitmap2);
        
        // Paintの作成
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        paint.setStyle(Style.FILL);
        paint.setAntiAlias(true);
         
        mImageView.setOnTouchListener(this);
	}
	
	public void savebtn(View view) {
		// TODO 自動生成されたメソッド・スタブ
		Log.d("tag","savebutton is called");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("保存確認");
		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO 自動生成されたメソッド・スタブ
				
				String fileName;
			    String filePath;
			    
				// 写真ディレクトリ用パス設定
				String strDirPath =Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/";
				
				makeDirectory(strDirPath);
				// ファイル名フォーマット取得
			    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
			 
			    // ファイル作成、書き込み
			    Date date =  new Date();
			    fileName = dateFormat.format(date) + ".png";
			 
			    filePath = strDirPath + fileName;
			    Log.d("ImageView",filePath);
			   
			    SaveImage.save(filePath,mBitmap2);
			    UploadImage task = new UploadImage(edit.this,"http://10.0.2.2//android/imageupload.php");
			    
			    Bitmap mBitmap3 = BitmapFactory.decodeFile(filePath);
			    ByteArrayOutputStream baos = new ByteArrayOutputStream();
			    mBitmap3.compress(CompressFormat.JPEG, 100, baos);
			    byte[] images = Arrays.copyOf(baos.toByteArray(), baos.toByteArray().length);
			   
				// 画像を追加
				task.addImage(filePath, images);
				// 実行
				task.execute();
			}
				
		});
		
		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO 自動生成されたメソッド・スタブ
				
			}
		
		});
		builder.show();
	}
		
	// ディレクトリの作成
		private boolean makeDirectory(String strDirPath) {
		    boolean ret = false;
		    File dir = new File(strDirPath);
		    if (!dir.exists()) {
		        ret = dir.mkdirs();
		    } else {
		        ret = true;
		    }
		    return ret;
		}
		
		

		@SuppressLint("ClickableViewAccessibility") @Override
		public boolean onTouch(View arg0, MotionEvent event) {
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				posX = event.getX();
				posY = event.getY();
				Log.d("ACTION_DOWN_X", String.valueOf(posX));
				Log.d("ACTION_DOWN_Y", String.valueOf(posY));
				break;
			case MotionEvent.ACTION_MOVE:
				posX2 = event.getX();
				posY2 = event.getY();
				Log.d("ACTION_MOVE_X", String.valueOf(posX2));
				Log.d("ACTION_MOVE_Y", String.valueOf(posY2));
				canvas.drawLine(posX, posY, posX2, posY2, paint);
				mImageView.invalidate();
			    posX = posX2;
			    posY = posY2;
				break;
			case MotionEvent.ACTION_UP:
				posX2 = event.getX();
				posY2 = event.getY();
				Log.d("ACTION_UP_X", String.valueOf(posX2));
				Log.d("ACTION_UP_Y", String.valueOf(posY2));
				mImageView.invalidate();
				break;
		    case MotionEvent.ACTION_CANCEL:
		        break;
		      	default:
		        break;
		      }
			return true;
			}

		public void backbtn(View view){
			finish();
		}
}
