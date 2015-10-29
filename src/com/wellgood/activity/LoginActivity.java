package com.wellgood.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.pc.ioc.inject.InjectBinder;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.inject.InjectListener;
import com.android.pc.ioc.inject.InjectMethod;
import com.android.pc.ioc.inject.InjectView;
import com.android.pc.ioc.view.listener.OnClick;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wellgood.application.APP;
import com.wellgood.contract.MyData;
import com.wellgood.utils.ResponseParse;
@InjectLayer(value = R.layout.activity_login)
public class LoginActivity extends Activity {
	
	private static String CLASS_NAME;
    private String TAG = this.getClass().getSimpleName();
    /***������� **/
    private RequestQueue volleyRequestQueue;
	/**   �ַ����������  **/
	public static StringRequest stringRequest;
	
	
	/**    ����url  **/
    private String requestURL;
    /**   ��½������  **/
    private ProgressDialog pd;
    
    
    /** �������ظ����ַ���  **/
    private String responseString;
    /** ��½�ɹ����**/
    private Boolean success=false;
    /**��½�������� **/
    private String message;
    
    /**  �û���������  **/
    private String usr_name;
    private String usr_password;
    /** �Ƿ��ס����**/
    private Boolean isRemeber;					
    
  //ע��
  	@InjectView(binders = { @InjectBinder(method = "click", listeners = { OnClick.class }) })
  	Button login_button,december_button,register_button;				//�ҵ���ҵ 
  	
    @InjectView
   EditText username,password;

    public LoginActivity(){
    	CLASS_NAME=getClass().getName();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        Log.d(CLASS_NAME, "oncreate()");
        Show(CLASS_NAME+"oncreate()");
    	// 1.�����������  
	    volleyRequestQueue = Volley.newRequestQueue(this);  

    }
    @InjectInit
	private void init() {
    	
    }
    @InjectMethod(@InjectListener(ids = { R.id.login_button }, listeners = { OnClick.class }))
    public void OnClick(View view){
    	switch (view.getId()) {
		case R.id.login_button:
			Log.d(CLASS_NAME, "�����loginbutton");
			//Login();
			Intent intent = new Intent (LoginActivity.this,MainActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
    }
    
    /**
     * ��½��������������� ��½����������ͨ���첽��ʵ��
     */
    public void Login(){
    	Log.d(CLASS_NAME, "����login����");
        //�������
    	 
    	 MyData.getRememberFlag();
    	 JSONObject user = new JSONObject();  
    	 //ȡ���˺�����
	   usr_name=username.getText().toString();
	   usr_password=password.getText().toString();
	   String msg="�����룺";
	   if(usr_name==null){
		   //Show("")
		   msg=msg+"�û���";
		   if (usr_password==null) {
			msg=msg+"������";
			dialog(msg);
			return;
		}
				  
	   }
	   
	    try {
		    	 user.put("usr_name", usr_name);  
		    	 user.put("usr_pwd", usr_password);
		} catch (JSONException e) {
			
				e.printStackTrace();
		}  
	    
	    
	    pd = ProgressDialog.show(this,"���ڵ�½...","Please Wait...");
	    requestURL=new String("http://192.168.0.45:8080/platform/user/login?user="+user.toString());
	    
    	//��ʼ�첽��������	
		new PostTask().execute(requestURL);
		
    }
    public void Show(String string){
    	Toast.makeText(this, string, Toast.LENGTH_LONG);
    }
   
    /**
     * PostTask Ϊasynctask������
     * @author Windows 7
     * ��һ������ doinbackground�����е��ã��ڶ���������onprogressupdate�е��ã���������onpostexecute�е���

     */
	public final class PostTask extends AsyncTask<String, Integer, String>{				
		
		
		
		
		/**  
	     * �����String������ӦAsyncTask�еĵ�һ������   
	     * �����String����ֵ��ӦAsyncTask�ĵ���������  
	     * �÷�������������UI�̵߳��У���Ҫ�����첽�����������ڸ÷����в��ܶ�UI���еĿռ�������ú��޸�  
	     * ���ǿ��Ե���publishProgress��������onProgressUpdate��UI���в���  
	     */  
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			//
				Log.d(CLASS_NAME, "doInBackground...��������"+params[0]);
		   try {
			   
			   
			   
			//��ʼ�����������
					//����params��0��������ǵ�½��url
					    // 3.json post������  
						 stringRequest = new StringRequest(params[0],
									new Response.Listener<String>() {
										@Override
										public void onResponse(String response) {
											//Log.d(CLASS_NAME,"response"+ response);
											responseString=response;
											Log.d(CLASS_NAME,"responsestring"+ responseString);
											publishProgress(100);
										}
									}, new Response.ErrorListener() {
										@Override
										public void onErrorResponse(VolleyError error) {
											Log.e(CLASS_NAME,"error" +error.getMessage(), error);
										}
									});  
					   
					    // 4.�����������������  
					    volleyRequestQueue.add(stringRequest);  
			
				
				
				
		   } catch (Exception e) {
				// TODO: handle exception
			   e.printStackTrace();
			}
		   return responseString;
				
		}
		
		/**�����String������ӦAsyncTask�еĵ�����������Ҳ���ǽ���doInBackground�ķ���ֵ��  
	     * ��doInBackground����ִ�н���֮�������У�����������UI�̵߳��� ���Զ�UI�ռ��������  */
		@Override
		protected void onPostExecute(String resault){
			super.onPostExecute(resault);
			Log.d(CLASS_NAME, "�첽���񷵻صĽ���ǣ�"+resault);
			Toast.makeText(LoginActivity.this, resault, Toast.LENGTH_LONG).show();
			//���������
			JSONObject jsonObject=new JSONObject();
			jsonObject=ResponseParse.parseLoginResponse(resault);
			
			try {
				success=jsonObject.getBoolean("success");
				message=jsonObject.getString("message");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//��½�ɹ�֮�����һ�β���
			Show(message);
			Intent intent=new Intent(LoginActivity.this,MainActivity.class);
			startActivity(intent);
			
		}
		//UI�ϸ��½���
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			if (values[0]==100) {
				pd.dismiss();
			}
		}


	}
	protected void dialog(String string) {
			AlertDialog.Builder builder = new Builder(LoginActivity.this);
			builder.setMessage(string);
			builder.setTitle("��ʾ");
			builder.setPositiveButton("ȷ��", new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							}
			});
			builder.setNegativeButton("ȡ��", new OnClickListener() {
							 @Override
					 public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							 }
			});
			
			builder.create().show();
		}
}
