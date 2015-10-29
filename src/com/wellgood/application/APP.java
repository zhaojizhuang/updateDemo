package com.wellgood.application;

import com.android.pc.ioc.app.Ioc;
import com.android.pc.util.Handler_SharedPreferences;
import com.hik.mcrsdk.MCRSDK;
import com.hik.mcrsdk.rtsp.RtspClient;

import android.app.Application;
import android.content.SharedPreferences;
/**
 * ���ȫ�ֱ���
 * {@link SharedPreferences} ����
 * @author ZhaoJizhuang
 *
 */
public class APP extends Application {

public static APP app;
	
	
	@Override
	public void onCreate() {
		 app = this;
		 
		 //��ʼ��ioc���
		Ioc.getIoc().init(this);
	    super.onCreate();
	    /** ��ʼ����Ƶ��� **/
	    MCRSDK.init();
        RtspClient.initLib();
        MCRSDK.setPrint(1, null);
	    
	    
	}

	/**
	 * ���ݴ洢���������ݿ�
	 * ������loonAndroid�Ĺ��߿��  android.pc.utils���
	 * @author Windows 7
	 * @param key
	 * @param value
	 * @return void
	 */
	public void setData(String key, String value) {
		Handler_SharedPreferences.WriteSharedPreferences(getApplicationContext(), "Cache", key, value);
	}

	/**
	 * ȡ����������
	 * ������loonAndroid�Ĺ��߿��  android.pc.utils���
	 * @author Windows 7
	 * @param key
	 * @return
	 * @return String
	 */
	public String getData(String key) {
		return Handler_SharedPreferences.getValueByName(getApplicationContext(), "Cache", key, Handler_SharedPreferences.STRING).toString();
	}

	//��Ƶ���õ�
	public static APP getIns() {
		return app;
	}
	
}
