package com.tonyhuang.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import android.os.AsyncTask;

/**
 * @author Tony Huang (starlightslo@gmail.com)
 * @version Creation time: 2014/2/13 上午11:54:23
 */
public class HttpUtil extends AsyncTask<String, Integer, String> {
	private HttpURLConnection connection;
	private HttpUtilListener listener;
	private HttpMethod method;
	private boolean isRun = false;
	private byte[] data;
	
	enum HttpMethod {
		GET, POST
	}
	
	@Override
	protected String doInBackground(String... url) {
		try {
			URL mUrl = new URL(url[0]);
			connection = (HttpURLConnection) mUrl.openConnection();
			
			switch(method) {
			case GET:
				connection.setRequestMethod("GET");
				connection.setUseCaches(false);
				break;
			case POST:
				connection.setRequestMethod("POST");
				connection.setUseCaches(false);
				processPostContent();
				break;
			}

			connection.connect();
			try {
				InputStream in = new BufferedInputStream(connection.getInputStream());
				readStream(in);
			} finally {
				connection.disconnect();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		if (isRun)
			listener.onResponse(data);
		isRun = false;
	}

	@Override
	protected void onCancelled() {
		isRun = false;
	}

	private void processPostContent() throws IOException {
		DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

		String postContent = "";
		postContent += URLEncoder.encode("channel", "UTF-8") + "=" + URLEncoder.encode("Devdiv", "UTF-8") + "&";
		postContent += URLEncoder.encode("author", "UTF-8") + "=" + URLEncoder.encode("Sodino", "UTF-8");

		dos.write(postContent.getBytes());
		dos.flush();
		dos.close();
	}

	private void readStream(InputStream is) throws IOException {
		int read = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while (isRun && (read = is.read()) != -1) {
			baos.write(read);
		}
		data = baos.toByteArray();
		baos.close();
	}

	public void requestGet(String url, HttpUtilListener listener) {
		isRun = true;
		this.listener = listener;
		this.method = HttpMethod.GET;
		super.execute(url);
	}

	public int getResponseCode() throws IOException {
		if (connection != null) {
			return connection.getResponseCode();
		}
		return -1;
	}

	public String getContentType() {
		if (connection != null) {
			return connection.getContentType();
		}
		return null;
	}

	public String getContentEncoding() {
		if (connection != null) {
			return connection.getContentEncoding();
		}
		return null;
	}
}

