package com.example.boulder;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class UploadImage extends AsyncTask<String , Integer, byte[]> {
	final static private String BOUNDARY = "MyBoundaryString";
	private String mURL;
	private HashMap<String, byte[]> mImages;
	private Context mContext;
	private Boolean isShowProgress;
	private ProgressDialog mProgressDialog;
	
	public UploadImage(Context me,String string) {
		// TODO 自動生成されたコンストラクター・スタブ
		super();
		mContext = me;
		mURL = string;
		mImages = new HashMap<String, byte[]>();
	}

	public Boolean getIsShowProgress() {  
        return isShowProgress;  
    }  

	@Override  
    protected void onPreExecute() {  
        super.onPreExecute();  
        isShowProgress = true;  
        showDialog();  
    } 

	public void addImage(String key, byte[] data) {
		mImages.put(key, data);
	}

	private byte[] send(byte[] data) {
		if (data == null)
			return null;

		byte[] result = null;
		HttpURLConnection connection = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;

		try {
			URL url = new URL(mURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			// 接続
			connection.connect();

			// 送信
			OutputStream os = connection.getOutputStream();
			os.write(data);
			os.close();
			
			// レスポンスを取得する
			byte[] buf = new byte[10240];
			int size;
			is = connection.getInputStream();
			while ((size = is.read(buf)) != -1) {
				baos.write(buf, 0, size);
			}
			result = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Exception e) {
			}

			try {
				connection.disconnect();
			} catch (Exception e) {
			}

			try {
				baos.close();
			} catch (Exception e) {
			}
		}

		return result;
	}

	private byte[] makePostData() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			// 画像の設定
			for (Entry<String, byte[]> entry : mImages.entrySet()) {
				String key = entry.getKey();
				byte[] data = entry.getValue();
				String name = "upfile";

				baos.write(("--" + BOUNDARY + "\r\n").getBytes());
				baos.write(("Content-Disposition: form-data;").getBytes());
				baos.write(("name=\"" + name + "\";").getBytes());
				baos.write(("filename=\"" + key + "\"\r\n").getBytes());
				baos.write(("Content-Type: image/jpeg\r\n\r\n").getBytes());
				baos.write(data);
				baos.write(("\r\n").getBytes());
			}

			// 最後にバウンダリを付ける
			baos.write(("--" + BOUNDARY + "--\r\n").getBytes());

			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				baos.close();
			} catch (Exception e) {
			}
		}
	}
	@Override
	protected byte[] doInBackground(String... params) {
		// TODO 自動生成されたメソッド・スタブ
		byte[] data = makePostData();
		byte[] result = send(data);
		return result;
	}

	@Override
	protected void onPostExecute(byte[] result) {
		dismissDialog();
		if (result != null) {
			Toast.makeText(mContext, "アップロードしました！", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(mContext, "アップロードに失敗しました...", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override  
    protected void onProgressUpdate(Integer... values) {  
        super.onProgressUpdate(values);  
        mProgressDialog.setProgress(values[0]);    
    }
	
	public void showDialog() {   
        mProgressDialog = new ProgressDialog(mContext);  
        mProgressDialog.setMessage("アップロード中...");  
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);  
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();  
    }  
	
    public void dismissDialog() {  
        mProgressDialog.dismiss();  
        mProgressDialog = null;  
    }

	
	
}

	
 
