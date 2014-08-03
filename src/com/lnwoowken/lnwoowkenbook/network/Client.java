package com.lnwoowken.lnwoowkenbook.network;

import java.io.BufferedReader;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.lnwoowken.lnwoowkenbook.model.Contant;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.Handler;

import android.util.Base64;
import android.util.Log;
@SuppressWarnings("unused")
public class Client {
	
	private Context context;
	private Handler handler;

	public Client(Context context, Handler errorHandler) {
		this.context = context;
		this.handler = errorHandler;
	}
	

	/**
	 * 下载图片
	 * sean
	 * @param url 下载图片的URL
	 * @return 下载到的图片以Bitmap形式返回
	 */
	public static Bitmap downloadBitmap(String url) {
		final AndroidHttpClient client = AndroidHttpClient
				.newInstance("Android");
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}
			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory
							.decodeStream(inputStream);
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or
			// IllegalStateException
			getRequest.abort();
			Log.d("ImageDownloader", "Error while retrieving bitmap from "
					+ url + "\n" + e.toString());
			
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return null;
	}

	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int byte_ = read();
					if (byte_ < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	
	
	public static String doPost(List<NameValuePair> params,String url) throws Exception{
		  String result = null;
		  String TIME_OUT = "操作超时";
		      // 新建HttpPost对象
		      HttpPost httpPost = new HttpPost(url);
		      // 设置字符集
		      HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		      // 设置参数实体
		      httpPost.setEntity(entity);
		      // 获取HttpClient对象
		      HttpClient httpClient = new DefaultHttpClient();
		      //连接超时
		      httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
		      //请求超时
		      httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
		      try {
		       // 获取HttpResponse实例
		          HttpResponse httpResp = httpClient.execute(httpPost);
		          // 判断是够请求成功
		          if (httpResp.getStatusLine().getStatusCode() == 200) {
		           // 获取返回的数据
		           result = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
		           Log.i("HttpPost", "HttpPost方式请求成功，返回数据如下：");
		           Log.i("result", result);
		          } else {
		           Log.i("HttpPost", "HttpPost方式请求失败");
		          }
		      } catch (ConnectTimeoutException e){
		       result = TIME_OUT;
		      }
		     
		      return result;
		 }
	

	/**
	 * sean
	 * @param urlStr 服务器的URL字符串
	 * @return 服务器返回的数据
	 */
	public static String executeHttpGetAndCheckNet(String urlStr, Context context) {
		Log.d("executeHttpGetAndCheckNet===============", "I'm in(execute):\n" + urlStr);
		String result = null;
		URL url = null;
		HttpURLConnection connection = null;

		InputStreamReader in = null;
		try {
			NetworkInfo networkInfo = ((ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE))
					.getActiveNetworkInfo();
			url = new URL(urlStr);
			if (networkInfo != null) {			
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(30000);
				connection.setReadTimeout(30000);
				connection.setDoInput(true);
			}
			else {
				Log.d("networkInfo", "no net================");
				result = Contant.NO_NET;
				return result;
				
			}
			in = new InputStreamReader(connection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(in);
			StringBuffer strBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				strBuffer.append(line);
			}
			result = strBuffer.toString();
		} catch (ConnectException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		return result;
	}

	/**
	 * sean
	 * @param res 需要编码的目标字符串
	 * @return base65编码后的字符串
	 */
	public static String encodeBase64(String res) {
		String temp = "";
		temp = Base64.encodeToString(res.getBytes(), Base64.NO_WRAP);
		return temp;
	}

	/**
	 * sean
	 * @param res 需要解码的目标字符串
	 * @return base64 解码后的字符串
	 */
	public static String decodeBase64(String res) {
		String temp = "";
		if (res != null) {
			if (res.charAt(0) == '"' && res.charAt(res.length() - 1) == '"') {

				res = res.substring(1, res.length() - 1);

			}
			try {
				temp = new String(Base64.decode(res, Base64.NO_WRAP), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return temp;
	}

	

}
