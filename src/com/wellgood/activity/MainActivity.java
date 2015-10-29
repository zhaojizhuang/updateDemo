package com.wellgood.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.android.pc.ioc.inject.InjectLayer;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wellgood.application.APP;
import com.wellgood.camera.CameraFragment;
import com.wellgood.fragment.InterviewFragment;
import com.wellgood.fragment.Security;
import com.wellgood.fragment.Public;
import com.wellgood.fragment.Business;
import com.wellgood.fragment.Message;
import com.wellgood.fragment.Settings;
import com.wellgood.fragment.WallView;
import com.wellgood.fragment.WallView1;
/**
 * @author zhaojizhuang
 *	������������������� maintab
 * ͨ������5��fragment ����fragmenttabhost�����л�
 */
@ContentView(R.layout.main_tab_layout)
public class MainActivity extends FragmentActivity{	
	
	public static String CLASS_NAME;
	//����FragmentTabHost���󣬲���
	@ViewInject(android.R.id.tabhost)
	private FragmentTabHost mTabHost;
	
	//����һ������
	private LayoutInflater layoutInflater;
		
	//�������������Fragment����
	private Class fragmentArray[] = {
			
			CameraFragment.class,				//�������
			WallView1.class,				//���˰��
			Security.class,				//�������
			Message.class,				//��Ϣ���
			Settings.class				//���ð��
			};
	
	//�������������tab��ťͼƬ
	private int mImageViewArray[] = {
			R.drawable.tab_home_btn,
			R.drawable.tab_message_btn,
			R.drawable.tab_selfinfo_btn,
			R.drawable.tab_square_btn,
			R.drawable.tab_more_btn
			};
	
	//Tabѡ�������
	private String mTextviewArray[] = {"����", "����", "����", "��Ϣ", "����"};
	
	//���캯���õ�����
	public MainActivity(){
		CLASS_NAME=getClass().getName();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        //ע��
        ViewUtils.inject(this);
        
        //��ʼ��tabhost
        initView();

        
    }
	/**
	 * ���ؼ����˳�
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(false);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	/* @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }*/
	    
	/**
	 * ��ʼ�����
	 */
	private void initView(){
		//ʵ�������ֶ���
		layoutInflater = LayoutInflater.from(this);
				
		//ʵ����TabHost���󣬵õ�TabHost
		//mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		//setup�ڼ�����view֮�����
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);	
		
		//�õ�fragment�ĸ���
		int count = fragmentArray.length;	
				
		for(int i = 0; i < count; i++){	
			//Ϊÿһ��Tab��ť����ͼ�ꡢ���ֺ�����
			TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
			//��Tab��ť��ӽ�Tabѡ��� ��fragment
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			//����Tab��ť�ı���
			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
		
		
			 
		
		}
		//����Ĭ�ϵ�������ʾtab
		mTabHost.setCurrentTab(2);
		
	}
				
	/**
	 * ��Tab��ť����ͼ�������
	 */
	private View getTabItemView(int index){
		View view = layoutInflater.inflate(R.layout.tab_item_view, null);
	
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewArray[index]);
		
		TextView textView = (TextView) view.findViewById(R.id.textview);		
		textView.setText(mTextviewArray[index]);
	
		return view;
	}
}
