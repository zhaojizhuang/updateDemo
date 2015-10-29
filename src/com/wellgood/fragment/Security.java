package com.wellgood.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.wellgood.activity.R;
import com.wellgood.adapter.MyFragmentPagerAdapter;
import com.wellgood.fragment.Message.ViewPagerAdapter;
import com.wellgood.frame.indicator.selectView.MSelect;
import com.wellgood.frame.indicator.selectView.SecuritySelect;
import com.wellgood.frame.widgets.IndicatorTabBar;
/**
 * ������飬���� �� ��
 * ����textview ���onclicklistener ʵ��tabЧ��
 * @
 * @author ZhaoJizhuang
 *@date 20150918
 */
public class Security extends BaseFragment{
	 
	public static final String select = null;
	private IndicatorTabBar mIndicatorTabBar;
	private List<String> tabNames;
	private ViewPager mViewPager;
	private int maxColumn = 2;				//�����ʾ����
	
    //
    View view;//����Fragment view
    
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		/* if(view==null){*/
	            view=inflater.inflate(R.layout.fragment_security, null);
	  /*      }
		 //�����View��Ҫ�ж��Ƿ��Ѿ����ӹ�parent�� �����parent��Ҫ��parentɾ����Ҫ��Ȼ�ᷢ�����rootview�Ѿ���parent�Ĵ���
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null) {
	            parent.removeView(view);
	        }*/ 
	      //ע��view���¼�
        ViewUtils.inject(this, view); 

        getActivity().setTitle("����");
        
		tabNames = new ArrayList<String>();
		tabNames.add("��");
		tabNames.add("��");
		
		
		//��viewpaber
		mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
		//����������
		mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));

		mIndicatorTabBar = (IndicatorTabBar) view.findViewById(R.id.tab_indicator);
		mIndicatorTabBar.setMaxColumn(maxColumn);
		
		mIndicatorTabBar.setViewPager(mViewPager);
		mIndicatorTabBar.initView(tabNames);
        
		
		return view;
	}
	
	class ViewPagerAdapter extends FragmentPagerAdapter {

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
			return SecuritySelect.select(position);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tabNames.size();
		}

	}

	
	
}
