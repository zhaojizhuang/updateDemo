package com.wellgood.camera;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.android.pc.ioc.inject.InjectInit;
import com.android.pc.util.Handler_Inject;
import com.hikvision.vmsnetsdk.CameraInfo;
import com.hikvision.vmsnetsdk.ControlUnitInfo;
import com.hikvision.vmsnetsdk.LineInfo;
import com.hikvision.vmsnetsdk.RegionInfo;
import com.hikvision.vmsnetsdk.ServInfo;
import com.hikvision.vmsnetsdk.VMSNetSDK;
import com.live.LiveActivity;
import com.playback.PlayBackActivity;
import com.wellgood.activity.R;
import com.wellgood.frame.grid.StaggeredGridView;
import com.wellgood.frame.grid.Adapter.SampleAdapter;

public class CameraFragment extends Fragment  implements AbsListView.OnItemClickListener{
	public static String CLASS_NAME="CameraFragment";
    public static final String SAVED_DATA_KEY = "SAVED_DATA";
    private static final int FETCH_DATA_TASK_DURATION = 500;

    private StaggeredGridView mGridView;
    private SampleAdapter mAdapter;

    /** ������Ϣ�Ķ��� */
    private MsgHandler          handler       = new MsgHandler();
    /** �û�������� */
   // private EditText            username;
    /** ��������� */
    //private EditText            passwd;
    /** ��¼��ť */
   // private Button              loginBtn;
    /** �Զ���¼��ѡ�� */
    //private CheckBox            autologinChk;

    /** �û�ѡ�е���· */
    private LineInfo            lineInfo;
    /** ��¼���ص����� */
    private ServInfo            servInfo=new ServInfo();
    /** �Ƿ��ǵ�һ��ִ��onResume���� */
    private boolean             isFirstResume = true;
    /** ��·ѡ�������� */
    //private Spinner             lineSpinner;
    /** ��ȡ��·��ť */
    private Button              fetchLineBtn;
    /** ��·�б������� */
    //private ResourceListAdapter lineAdapter;
    /** ��������ַ����� */
   // private EditText            serverAddrEt;
    /**  ��·�б�**/
    List<LineInfo> lineInfoList = new ArrayList<LineInfo>();
    /**  ���������б�**/
    List<ControlUnitInfo> ctrlUnitList = new ArrayList<ControlUnitInfo>();
    /**����ͷ�б�**/
    List<CameraInfo> cameraList = new ArrayList<CameraInfo>();
    /**�����б�**/
    List<RegionInfo> regionList=new ArrayList<RegionInfo>();
    
    String  servAddr = "http://112.12.17.3";
    String userName="test" ;
    String password="12345" ;
    ProgressDialog pd;
	View view;
	private Dialog mDialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//this.inflater = inflater;
		Log.d(CLASS_NAME, "onCreateView()");
		view = inflater.inflate(R.layout.activity_sgv_empy_view, container, false);
		Handler_Inject.injectOrther(this, view);
        getActivity().setTitle("����");
        
        
        mGridView = (StaggeredGridView) view.findViewById(R.id.grid_view);


    /*    View header = inflater.inflate(R.layout.list_item_header_footer, null);
        View footer = inflater.inflate(R.layout.list_item_header_footer, null);*/
/*        TextView txtHeaderTitle = (TextView) header.findViewById(R.id.txt_title);
        TextView txtFooterTitle =  (TextView) footer.findViewById(R.id.txt_title);
        txtHeaderTitle.setText("THE HEADER!");
        txtFooterTitle.setText("THE FOOTER!");*/

/*        mGridView.addHeaderView(header);
        mGridView.addFooterView(footer);*/
        mGridView.setEmptyView(view.findViewById(android.R.id.empty));
        mAdapter = new SampleAdapter(getActivity(), R.id.txt_line1);



        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(this);
		return view;
	}

	@InjectInit
	private void init(){
		Log.d(CLASS_NAME, "init()");
		 pd= ProgressDialog.show(getActivity(),"���ڻ�ȡ����ͷ�б�...","Please Wait...");
		fetchLine();
		//login();
		
	}
	
    private void fillAdapter() {
        for (CameraInfo info : cameraList) {
            mAdapter.add(info.name);
        }
    }
	
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item Clicked: " + position, Toast.LENGTH_SHORT).show();
        final Object itemData = cameraList.get(position);
        //gotoLiveOrPlayBack((CameraInfo) itemData);
        gotoLive((CameraInfo) itemData);
    }

	/** handler  ��������ֻҪ
	 * ��run������------����Message,
	 * ��Handler��handleMessage----����Message��
	 * ͨ����ͬ��Messageִ�в�ͬ������  **/
	
	
    @SuppressLint("HandlerLeak")				//����lint����
    private final class MsgHandler extends Handler {

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Constants.Login.GET_LINE_IN_PROCESS:
                	Log.d(CLASS_NAME, "��ȡ��·�С�����");
                    //showGetLineProgress();
                break;
                case Constants.Login.GET_LINE_SUCCESS:
                	Log.d(CLASS_NAME, "��ȡ��·�гɹ���");
                	List<LineInfo> lineList=(List<LineInfo>) msg.obj;
                	lineInfo=lineList.get(1);
                	login();
                    //onGetLineSuccess((List<Object>) msg.obj);
                break;
                case Constants.Login.GET_LINE_FAILED:
                	Log.d(CLASS_NAME, "��ȡ��·��ʧ�ܣ�");
                    //onGetLineFailed();
                break;
                case Constants.Login.SHOW_LOGIN_PROGRESS:
                    //showLoginProgress();
                	Log.d(CLASS_NAME, "���ڵ�½������");
                break;
                case Constants.Login.CANCEL_LOGIN_PROGRESS:
                    //cancelProgress();
                break;
                case Constants.Login.LOGIN_SUCCESS:
                    // ��¼�ɹ�
                	Log.d(CLASS_NAME, "��½�ɹ�");
                	getControlUnit();
                   // onLoginSuccess();
                break;
                case Constants.Login.LOGIN_FAILED:
                    // ��¼ʧ��
                   // onLoginFailed();
                break;
                case MsgIds.GET_C_F_NONE_FAIL:
                	Log.d(CLASS_NAME, "��ȡ���ƿ����б�ʧ��");
                	break;
                case MsgIds.GET_C_F_NONE_SUC:
                	Log.d(CLASS_NAME, "��ȡ���ƿ����б�ɹ���");
                	getRegionList();
                	break;
                	
                case MsgIds.GET_CAMERA_FAIL:
                	Log.d(CLASS_NAME, "��ȡ����ͷ�б�ʧ��");
                	break;
                case MsgIds.GET_CAMERA_SUC:
                	Log.d(CLASS_NAME, "��ȡ����ͷ�б�ɹ���");
                	pd.dismiss();
                	fillAdapter();
                	break;
                case MsgIds.GET_REGION_FAIL:
                	Log.d(CLASS_NAME, "��ȡ�����б�ʧ��");
                	break;
                case MsgIds.GET_REGION_SUC:
                	Log.d(CLASS_NAME, "��ȡ�����б�ɹ���");
                	getCameraList();
                	break;
                	
                default:
                break;
            }

        }
    }
	
        /**
         * ��¼
         */
        protected void login() {
        	Log.d(CLASS_NAME, "login()");
            
            /** �����̣߳��½�һ���߳�   **/
            new Thread(new Runnable() {
                @Override
                public void run() {
                	
                    handler.sendEmptyMessage(Constants.Login.SHOW_LOGIN_PROGRESS);
                    

                    String macAddress = getMac();
                    Log.d(CLASS_NAME, "��½����"+"servAddr"+servAddr+"userName"+userName+"password"+password+"lineInfo.lineID"+lineInfo.lineID+"macAddress"+macAddress);
                    // ��¼����
                    boolean ret = VMSNetSDK.getInstance().login(servAddr, userName, password, lineInfo.lineID, macAddress,
                            servInfo);
                    if (servInfo == null) {
                    	Log.d(CLASS_NAME, "servInfo==null");
                    }
                    if (servInfo != null) {
                        // ��ӡ����¼ʱ���ص���Ϣ
                        Log.i(Constants.LOG_TAG, "login ret : " + ret);
                        Log.i(Constants.LOG_TAG, "login response info[" + "sessionID:" + servInfo.sessionID + ",userID:"
                                + servInfo.userID + ",magInfo:" + servInfo.magInfo + ",picServerInfo:"
                                + servInfo.picServerInfo + ",ptzProxyInfo:" + servInfo.ptzProxyInfo + ",userCapability:"
                                + servInfo.userCapability + ",vmsList:" + servInfo.vmsList + ",vtduInfo:"
                                + servInfo.vtduInfo + ",webAppList:" + servInfo.webAppList + "]");
                    }
                    Log.d(CLASS_NAME, "ret"+ret);
                     if (ret) {
                        TempData.getIns().setLoginData(servInfo);
                        handler.sendEmptyMessage(Constants.Login.LOGIN_SUCCESS);
                    } else {
                        handler.sendEmptyMessage(Constants.Login.LOGIN_FAILED);
                    }

                }
            }).start();
            Log.d(CLASS_NAME, "�߳̿���");
        }
        
        
        /**
         * ��ȡ��·
         */
        protected void fetchLine() {
        	Log.d(CLASS_NAME, "fetchLine()");
            new Thread() {
                public void run() {
                    handler.sendEmptyMessage(Constants.Login.GET_LINE_IN_PROCESS);
                   // List<LineInfo> lineInfoList = new ArrayList<LineInfo>();
                    Log.i(Constants.LOG_TAG, "servAddr:" + servAddr);
                    boolean ret = VMSNetSDK.getInstance().getLineList(servAddr, lineInfoList);
                    if (ret) {
                        Message msg = new Message();
                        msg.what = Constants.Login.GET_LINE_SUCCESS;
                        msg.obj = lineInfoList;
                        //lineInfo=lineInfoList.get(1);
                        handler.sendMessage(msg);
                    } else {
                        handler.sendEmptyMessage(Constants.Login.GET_LINE_FAILED);
                    }
                };
            }.start();
        }
        /***
         * ��ȡ���������б�
         * */
        protected void  getControlUnit() {
			Log.d(CLASS_NAME, "getControlUnit()");
			  new Thread() {
	                public void run() {
					  ServInfo loginData = TempData.getIns().getLoginData();
				        if (loginData == null) {
				            Log.d(Constants.LOG_TAG, "requestFirstList loginData:" + loginData);
				            return;
				        }
				        String sessionID = loginData.sessionID;
				        Log.d(CLASS_NAME, "sessionID"+sessionID);
				        int controlUnitID = 0;// �״λ�ȡ���ݣ���ʾ��Ŀ¼
				        int numPerPage = 10000;// �˴���10000������ʵ�ʲ���������ô�࣬��ʾ��ȡ��������
				        int curPage = 1;
				       
				        // ��ȡ���������б�
				        boolean ret = VMSNetSDK.getInstance().getControlUnitList(servAddr, sessionID, controlUnitID, numPerPage,
				                curPage, ctrlUnitList);
				        Log.d(Constants.LOG_TAG, "getControlUnitList ret:" + ret);
				        if (ctrlUnitList != null && !ctrlUnitList.isEmpty()) {
				            for (ControlUnitInfo info : ctrlUnitList) {
				                Log.d(Constants.LOG_TAG, "name:" + info.name + ",controlUnitID:" + info.controlUnitID + ",parentID:"
				                        + info.parentID);
				            }
				        }
				        Log.d(Constants.LOG_TAG, "allData size is " + ctrlUnitList.size());
				        if (!ret) {
				            Log.d(Constants.LOG_TAG, "Invoke VMSNetSDK.getControlUnitList failed:");
				            Message msg = new Message();
							msg.what = MsgIds.GET_C_F_NONE_FAIL;
							msg.obj = ctrlUnitList;
							handler.sendMessage(msg);
				            
				        }if (ret) {
				        	Message msg = new Message();
							msg.what = MsgIds.GET_C_F_NONE_SUC;
							msg.obj = ctrlUnitList;
							handler.sendMessage(msg);
						}
			         };
	         }.start();
		}
        /**��ȡ�����б�**/
        protected void getRegionList() {
			Log.d(CLASS_NAME, "getRegionList");
			  new Thread() {
	                public void run() {
					  ServInfo loginData = TempData.getIns().getLoginData();
				        if (loginData == null) {
				            Log.d(Constants.LOG_TAG, "getRegion loginData:" + loginData);
				            return;
				        }
				        String sessionID = loginData.sessionID;
				        Log.d(CLASS_NAME, "region sessionID"+sessionID);
				        int controlUnitID = ctrlUnitList.get(0).controlUnitID;// �״λ�ȡ���ݣ���ʾ��Ŀ¼
				        int numPerPage = 10000;// �˴���10000������ʵ�ʲ���������ô�࣬��ʾ��ȡ��������
				        int curPage = 1;
				       
				        Log.d(CLASS_NAME, "controlUnitID"+controlUnitID);
				        // 2.�ӿ������Ļ�ȡ�����б�
				        boolean ret = VMSNetSDK.getInstance().getRegionListFromCtrlUnit(servAddr, sessionID, controlUnitID, numPerPage,
				                curPage, regionList);
				        
				        if (regionList != null && !regionList.isEmpty()) {
				            for (RegionInfo info : regionList) {
				                Log.d(CLASS_NAME, "region :" + info.name + ",regionID:" + info.regionID );
				            }
				        }
				        Log.d(Constants.LOG_TAG, "allregion size is " + cameraList.size());
				        if (!ret) {
				            Log.d(Constants.LOG_TAG, "Invoke VMSNetSDK.getControlUnitList failed:");
				            Message msg = new Message();
							msg.what = MsgIds.GET_REGION_FAIL;
							msg.obj = regionList;
							handler.sendMessage(msg);
				            
				        }if (ret) {
				        	Message msg = new Message();
							msg.what = MsgIds.GET_REGION_SUC;
							msg.obj = regionList;
							handler.sendMessage(msg);
						}
			         };
	         }.start();
		}
        
        /**��ȡ����ͷ�б�**/
        protected void getCameraList() {
			Log.d(CLASS_NAME, "getCameraList");
			  new Thread() {
	                public void run() {
					  ServInfo loginData = TempData.getIns().getLoginData();
				        if (loginData == null) {
				            Log.d(Constants.LOG_TAG, "getcamera loginData:" + loginData);
				            return;
				        }
				        String sessionID = loginData.sessionID;
				        Log.d(CLASS_NAME, "r sessionID"+sessionID);
				        int controlUnitID = regionList.get(0).regionID;// �״λ�ȡ���ݣ���ʾ��Ŀ¼
				        int numPerPage = 10000;// �˴���10000������ʵ�ʲ���������ô�࣬��ʾ��ȡ��������
				        int curPage = 1;
				       
				        Log.d(CLASS_NAME, "controlUnitID"+controlUnitID);
				        // 3.�������ȡ����ͷ�б�
				       boolean ret = VMSNetSDK.getInstance().getCameraListFromRegion(servAddr, loginData.sessionID, controlUnitID, numPerPage, curPage,
				                cameraList);
				        Log.d(CLASS_NAME, "getCameraListFromRegion ret:" + ret);
				        
				        if (cameraList != null && !cameraList.isEmpty()) {
				            for (CameraInfo info : cameraList) {
				                Log.d(CLASS_NAME, "careme :" + info.name + ",cameraType:" + info.cameraType + ",cameraType:"
				                        + info.cameraType);
				            }
				        }
				        Log.d(Constants.LOG_TAG, "allcameraData size is " + cameraList.size());
				        if (!ret) {
				            Log.d(Constants.LOG_TAG, "Invoke VMSNetSDK.getControlUnitList failed:");
				            Message msg = new Message();
							msg.what = MsgIds.GET_CAMERA_FAIL;
							msg.obj = cameraList;
							handler.sendMessage(msg);
				            
				        }if (ret) {
				        	Message msg = new Message();
							msg.what = MsgIds.GET_CAMERA_SUC;
							msg.obj = cameraList;
							handler.sendMessage(msg);
						}
			         };
	         }.start();
		}
        
        /**
         * ��ȡ��¼�豸mac��ַ
         * 
         * @return
         */
        protected String getMac() {
            WifiManager wm = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
            String mac = wm.getConnectionInfo().getMacAddress();
            return mac == null ? "" : mac;
        }
        public void initUI(){
 
        }

    	private void gotoLiveOrPlayBack(final CameraInfo info) {
    		String[] datas = new String[]{"Ԥ��","�ط�"};
    		mDialog = new AlertDialog.Builder(getActivity()).setSingleChoiceItems(datas, 0, new DialogInterface.OnClickListener() {
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				mDialog.dismiss();
    				switch (which) {

    				case 0:
    					gotoLive(info);
    					break;
    				case 1:
    					gotoPlayback(info);
    					break;
    				default:
    					break;
    				}
    			}
    		}).create();
    		mDialog.show();
    		
    	}

    	/**
    	  * ����Զ�̻ط�
    	  * @param info
    	  * @since V1.0
    	  */
    	protected void gotoPlayback(CameraInfo info) {
    	    if(info == null){
    	        Log.e(Constants.LOG_TAG,"gotoPlayback():: fail");
    	        return;
    	    }
    		Intent it = new Intent(getActivity(), PlayBackActivity.class);
    		it.putExtra(Constants.IntentKey.CAMERA_ID, info.cameraID);
    		it.putExtra(Constants.IntentKey.DEVICE_ID, info.deviceID);
    		getActivity().startActivity(it);
    		
    	}

    	/**
    	  * ����ʵʱԤ��
    	  * @param info
    	  * @since V1.0
    	  */
    	protected void gotoLive(CameraInfo info) {
    	    if(info == null){
                Log.e(Constants.LOG_TAG,"gotoLive():: fail");
                return;
            }
    		Intent it = new Intent(getActivity(), LiveActivity.class);
    		it.putExtra(Constants.IntentKey.CAMERA_ID, info.cameraID);
    		TempData.getIns().setCameraInfo(info);
    		getActivity().startActivity(it);
    	}

}
