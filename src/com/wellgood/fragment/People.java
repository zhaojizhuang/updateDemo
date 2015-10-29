package com.wellgood.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.wellgood.activity.R;
import com.wellgood.adapter.ChatMsgViewAdapter;
import com.wellgood.entity.ChatMsgEntity;


/**
 * {@link Security}�Ӱ��  ----��
 *@author Windows 7 
 */
public class People extends BaseFragment implements OnClickListener{
    /** Called when the activity is first created. */

	private Button mBtnSend;
	//private Button mBtnBack;
	private EditText mEditTextContent;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// ����fragment����
		  view= inflater.inflate(R.layout.chat_xiaohei, container, false); 
	        ViewUtils.inject(this, view); //ע��view���¼�
     
		
		return view;
	}
	
	
    @Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 //����activityʱ���Զ����������
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
   
		 initView();
	        
	      initData();
		
	}


    
    
    public void initView()
    {
    	mListView = (ListView) view.findViewById(R.id.listview);
    	mBtnSend = (Button) view.findViewById(R.id.btn_send);
    	mBtnSend.setOnClickListener(this);
    	
    	mEditTextContent = (EditText) view.findViewById(R.id.et_sendmessage);
    }
    
    private String[]msgArray = new String[]{"�Ѿ��յ�����", "����", "�Ѿ��յ�����", "���ϴ������", 
    										"�Ѿ��յ�����", "����·����",
    										"�Ѿ��յ�����", "��ҵ·����",};
    
    private String[]dataArray = new String[]{"2012-09-01 18:00", "2012-09-01 18:10", 
    										"2012-09-01 18:11", "2012-09-01 18:20", 
    										"2012-09-01 18:30", "2012-09-01 18:35", 
    										"2012-09-01 18:40", "2012-09-01 18:50"}; 
    private final static int COUNT = 8;
    public void initData()
    {
    	for(int i = 0; i < COUNT; i++)
    	{
    		ChatMsgEntity entity = new ChatMsgEntity();
    		entity.setDate(dataArray[i]);
    		if (i % 2 == 0)
    		{
    			entity.setName("������˾");
    			entity.setMsgType(true);
    		}else{
    			entity.setName("��");
    			entity.setMsgType(false);
    		}
    		
    		entity.setText(msgArray[i]);
    		mDataArrays.add(entity);
    	}

    	mAdapter = new ChatMsgViewAdapter(getActivity(), mDataArrays);
		mListView.setAdapter(mAdapter);
		
    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.btn_send:
			send();
			break;
		/*case R.id.btn_back:
			//finish();
			break;*/
		}
	}
	
	private void send()
	{
		String contString = mEditTextContent.getText().toString();
		if (contString.length() > 0)
		{
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setDate(getDate());
			entity.setName("��");
			entity.setMsgType(false);
			entity.setText(contString);
			
			mDataArrays.add(entity);
			mAdapter.notifyDataSetChanged();
			
			mEditTextContent.setText("");
			
			mListView.setSelection(mListView.getCount() - 1);
		}
	}
	
    private String getDate() {
        Calendar c = Calendar.getInstance();

        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH));
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));
        
        
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins); 
        						
        						
        return sbBuffer.toString();
    }
    
    
    public void head_xiaohei(View v) {     //������ ���ذ�ť
    	//Intent intent = new Intent (ChatActivity.this,InfoXiaohei.class);			
		//startActivity(intent);	
      } 
}