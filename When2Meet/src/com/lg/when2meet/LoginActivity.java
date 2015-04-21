package com.lg.when2meet;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	ImageView login;
	ImageView join;
	EditText login_id;
	EditText login_pwd;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		login = (ImageView) findViewById(R.id.login);
		join = (ImageView) findViewById(R.id.join);
		login_id = (EditText) findViewById(R.id.login_id);
		login_pwd = (EditText) findViewById(R.id.login_pwd);
		
		final Handler handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				Toast.makeText(LoginActivity.this, "���̵� ��й�ȣ�� ��ġ���� �ʽ��ϴ�", Toast.LENGTH_SHORT).show();
			};
		};
		
		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				login.setImageResource(R.drawable.button_login_click);
				
				String loginId = login_id.getText().toString();
				String loginPwd = login_pwd.getText().toString();
				
				if(loginId.equals("") || loginId == null){
					Toast.makeText(LoginActivity.this, "���̵� �Է��ϼ���", Toast.LENGTH_SHORT).show();
				}else if(loginPwd.equals("") || loginPwd == null){
					Toast.makeText(LoginActivity.this, "��й�ȣ�� �Է��ϼ���", Toast.LENGTH_SHORT).show();
				}
				
				new Thread(){
					@Override
					public void run() {
						String loginId = login_id.getText().toString();
						String loginPwd = login_pwd.getText().toString();
						
						String result = SendByHttpLogin(loginId, loginPwd);
						
						SharedPreferences setting = getSharedPreferences("login", 0);
						SharedPreferences.Editor editor = setting.edit();
						
						try {
							JSONObject json = new JSONObject(result);
							String isSuccess = json.getString("isSuccess");
							
							if(isSuccess.equals("true")){
								editor.putString("id", loginId);
								editor.putString("pwd", loginPwd);
								
								editor.commit();
								Intent intent = new Intent(getApplicationContext(),
										ListActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}else{
								handler.sendMessage(Message.obtain());
							}
						
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//String[][] parsedData = jsonParserList(result);
					};
				}.start();
				
			}
		});

		join.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				join.setImageResource(R.drawable.button_join_click);
				Intent intent = new Intent(getApplicationContext(),
						JoinActivity.class);
				startActivity(intent);
			}
		});
	}
	
	/**
	 * ������ �����͸� ������ �޼ҵ�
	 */
	private String SendByHttpLogin(String phoneNo, String pwd) {
		String URL = "http://192.168.0.130:8080/checkJoin";
		
		DefaultHttpClient client = new DefaultHttpClient();
		try {
			/* üũ�� id�� pwd�� ������ ���� */
			HttpPost post = new HttpPost(URL+"?phoneNo="+phoneNo+"&pwd="+pwd);

			/* �����ð� �ִ� 5�� */
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 3000);
			HttpConnectionParams.setSoTimeout(params, 3000);

			/* ������ ���� �� �������� �����͸� �޾ƿ��� ���� */
			HttpResponse response = client.execute(post);
			BufferedReader bufreader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent(),
							"utf-8"));

			String line = null;
			String result = "";

			while ((line = bufreader.readLine()) != null) {
				result += line;
			}
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			client.getConnectionManager().shutdown();	// ���� ���� ����
			return ""; 
		}
		
	}
}
