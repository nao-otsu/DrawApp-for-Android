package com.example.boulder;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat") 
public class MainActivity extends Activity {
   
  //TextViewオブジェクトの宣言
   	TextView textView;
	private Uri uriSelectImage;
	
   	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //TextViewの生成
        textView = (TextView)findViewById(R.id.date);        
    }
    
    public void  onStart(){
    	Log.d("tag","onStart is called");
    	super.onStart();
    }
    
    public void onResume(){
    	//Dateオブジェクトを作成
        Date date =  new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        
        //テキストの内容として、日付を設定する
        textView.setText(sdf.format(date));
    	Log.d("tag","onResume is called");
    	super.onResume();
    }
    
    public void onPause(){
    	Log.d("tag","onPause is called");
    	super.onPause();
    }
    public void onStop(){
    	Log.d("tag","onStop is called");
    	super.onStop();
    }
    
    public void selectbtn(View view){

    	Log.d("tag","selectImage is called");
    	//画像選択のIntentの生成
    	Intent intent = new Intent();
    	//端末内の画像を取得する
    	intent.setType("image/*");
    	//コンテンツのURLを取得するアクションを設定
    	intent.setAction(Intent.ACTION_GET_CONTENT);
    	//選択後、結果を受け取れるようにActivityを呼び出す
    	startActivityForResult(intent,0 );
    	
    }
    public void endbtn(View view){
    	this.finish();
    }
    public void kadaibtn(View view){
    	String uri = "http://10.0.2.2/Android/Resize.php";
    	Intent intent = new Intent(this,WebViewImage.class);
    	intent.putExtra("uri", uri);
    	startActivity(intent);
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自動生成されたメソッド・スタブ
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0 && resultCode == RESULT_OK){
				
			uriSelectImage = data.getData();
		}
		Log.d("tag","onActivityResult is called");
		Intent intent = new  Intent(this,edit.class);
		intent.putExtra("extra_uri", uriSelectImage);
		startActivity(intent);
		
		
	}
    
}
