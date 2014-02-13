package com.tonyhuang.transmission;

import com.tonyhuang.http.HttpUtil;
import com.tonyhuang.http.HttpUtilListener;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;

public class MainActivity extends Activity {
	private static final String TAG = "HighTransmission";
	private static final String SERVER = "http://192.168.2.100/test.php";

	private Button request;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// HTTP connection reuse which was buggy pre-froyo
		if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepAlive", "false");
		}

		initUI();
	}

	private void initUI() {
		request = (Button) findViewById(R.id.button1);
		request.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				request();
			}
		});
	}

	protected void request() {
		final HttpUtil httpUtil = new HttpUtil();
		httpUtil.requestGet(SERVER, new HttpUtilListener() {
			@Override
			public void onResponse(byte[] data) {
				String content = new String(data);
				Log.d(TAG, "Content: " + content);
			}
		});
	}

}
