package com.wellgood.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lidroid.xutils.ViewUtils;
import com.wellgood.activity.R;
/**
 * ���˰��
 * @author Windows 7
 *
 */
public class Business extends BaseFragment{
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {	
		
		 //ViewUtils.inject(this, view); //ע��view���¼�
		
		 if(view==null){
	            view=inflater.inflate(R.layout.fragment_business, null);
	        }
		 //�����View��Ҫ�ж��Ƿ��Ѿ����ӹ�parent�� �����parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null) {
	            parent.removeView(view);
	        } 
		
	      //ע��view���¼�
  ViewUtils.inject(this, view); 
  return view;
	}	
}