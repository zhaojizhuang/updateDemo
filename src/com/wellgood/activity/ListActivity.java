package com.wellgood.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.pc.ioc.image.ImageDownloader;
import com.android.pc.ioc.image.Utils;
import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.ioc.inject.InjectLayer;
import com.android.pc.ioc.inject.InjectView;
import com.wellgood.adapter.MyAdapter;

@InjectLayer(value = R.layout.activity_main2)
public class ListActivity extends Activity {

	@InjectView
	ListView lt_demo;
	ImageDownloader imageDownloader = null;
	ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

	@InjectInit
	private void init() {

		for (int i = 0; i < 1000; i++) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("text1", "����");
			hashMap.put("text2", "����");
			hashMap.put("text3", "����");
			hashMap.put("text4", "����");
			hashMap.put("text5", "����");
			hashMap.put("image", "http://www.yjz9.com/uploadfile/2012/1231/20121231055637429.jpg?s=" + i);
			dataList.add(hashMap);
		}

		setAdapter();
	};

	// ��һ�ּ򵥷�ʽ
	private void setAdapter() {
		MyAdapter adapter = new MyAdapter(lt_demo, dataList, R.layout.list_item);
		lt_demo.setAdapter(adapter);
	}

	// �ڶ�����Ҫ�Լ���ͼƬ�������ӽ�ȥ�� ����Ҫ������������
	private void setAdapter2() {
		// --------------------------------------------------------------------------------------------------
		// �������ImageDownloader��ȥ �����Adapter���ͼƬ����
		imageDownloader = new ImageDownloader(this, 200);
		MyAdapter adapter = new MyAdapter(lt_demo, dataList, R.layout.list_item){
			@Override
			public void download(ImageView view, String url) {
			    super.download(view, url);
			}
		};
		lt_demo.setAdapter(adapter);
		// ����ֹͣ�ſ�ʼ����
		lt_demo.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView absListView, int scrollState) {
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
					if (!Utils.hasHoneycomb()) {
						imageDownloader.setPauseWork(true);
					}
				} else {
					imageDownloader.setPauseWork(false);
				}
			}

			@Override
			public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			}
		});
	}
}
