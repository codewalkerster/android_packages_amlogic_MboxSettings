package com.mbx.settingsmbox;

import android.app.SystemWriteManager;
import android.app.MboxOutputModeManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle ;

public class OutPutModeManager {
    private static final String TAG = "OutPutModeManager";

	private static Context mContext = null;

    private static  String[] ALL_HDMI_MODE_VALUE_LIST = {"480i","480p","576i","576p","720p50hz","720p","1080i","1080p","1080i50hz","1080p50hz","4k2k24hz","4k2k25hz", "4k2k30hz", "4k2ksmpte"};
    private static  String[] ALL_HDMI_MODE_TITLE_LIST = {"HDMI 480I","HDMI 480P","HDMI 576I","HDMI 576P","HDMI 720P 50HZ","HDMI 720P 60HZ","HDMI 1080I 60HZ","HDMI 1080P 60HZ","HDMI 1080I 50HZ","HDMI 1080P 50HZ" ,"HDMI 4K 24HZ","HDMI 4K 25HZ","HDMI 4K 30HZ","HDMI 4K SMPTE"};
    private static final String[] HDMI_MODE_VALUE_LIST = {"480i","480p","576i","576p","720p","1080i","1080p","720p50hz","1080i50hz","1080p50hz"};
    private static final String[] HDMI_MODE_TITLE_LIST = {"HDMI 480I","HDMI 480P","HDMI 576I","HDMI 576P","HDMI 720P 60HZ","HDMI 1080I 60HZ","HDMI 1080P 60HZ","HDMI 720P 50HZ","HDMI 1080I 50HZ","HDMI 1080P 50HZ"};
    private static final String[] CVBS_MODE_VALUE_LIST = {"480cvbs","576cvbs"}; 
    private static final String[] CVBS_MODE_TITLE_LIST = {"480 CVBS","576 CVBS"}; 
    private static final String HDMI_MODE_PROP = "ubootenv.var.hdmimode";
    private static final String COMMON_MODE_PROP = "ubootenv.var.outputmode";
    private static final String NO_FILTER_SET="no_filter_set";
   
	private final static String DISPLAY_MODE_SYSFS = "/sys/class/display/mode";
    private final static String HDMI_ONLY_MODE = "ro.platform.hdmionly";

    //=== hdmi + cvbs dual
    private final static String DISPLAY_MODE_SYSFS_DUAL = "/sys/class/display2/mode";
    private final static String HDMI_CVBS_DUAL = "ro.platform.has.cvbsmode"; 

    private ArrayList<String> mTitleList = null;
    private ArrayList<String> mValueList = null;
    private ArrayList<String> mSupportList = null;
    
	OutPutListAdapter adapter = null;
	private static SystemWriteManager sw;
    private static MboxOutputModeManager mom;

    private static String mUiMode = "hdmi";
    private Runnable setBackRunnable = null;
    private	static Handler myHandler = null;
    private boolean hasRealOutput = false;
    private final static int UPDATE_OUTPUT_MODE_UI = 101;
    private boolean isNeedShowConfirmDialog = false;
    private static final String PREFERENCE_BOX_SETTING = "preference_box_settings";
    private static final int hdmi4KmodeNum =  4 ;
    
    public OutPutModeManager(Context context){
        mContext = context;
        mUiMode = "hdmi";
		sw = (SystemWriteManager) mContext.getSystemService("system_write");
        mom = (MboxOutputModeManager) mContext.getSystemService(Context.MBOX_OUTPUTMODE_SERVICE);
        
        mTitleList = new ArrayList<String>();
        mValueList = new ArrayList<String>();
        initModeValues(mUiMode);    
    }
	
	public OutPutModeManager(Context context, ListView listview , String mode) {
		mContext = context;
		sw = (SystemWriteManager) mContext.getSystemService("system_write");
    
        mUiMode = mode;
        initModeValues(mUiMode);
		adapter = new OutPutListAdapter(mContext,mTitleList,getCurrentModeIndex());
		listview.setAdapter(adapter);
	}

    public int getCurrentModeIndex(){
         String currentHdmiMode = sw.readSysfs(DISPLAY_MODE_SYSFS);
         for(int i=0 ; i < mValueList.size();i++){
             if(currentHdmiMode.equals(mValueList.get(i))){
                return i ;
             }
         }
         
         if(mUiMode.equals("hdmi")){
            return 4;
         }else{
            return 0;
         }   
    }

    private void initModeValues(String mode){
        mTitleList = new ArrayList<String>();
        mValueList = new ArrayList<String>();
        mSupportList = new ArrayList<String>();

        filterOutputMode();
        
        hasRealOutput = sw.getPropertyBoolean("ro.platform.has.realoutputmode", false);
        if(mode.equalsIgnoreCase("hdmi")){
            for(int i=0 ; i< ALL_HDMI_MODE_VALUE_LIST.length; i++){
                mTitleList.add(ALL_HDMI_MODE_TITLE_LIST[i]);
                mValueList.add(ALL_HDMI_MODE_VALUE_LIST[i]);}      
        }else if(mode.equalsIgnoreCase("cvbs")){          
            for(int i = 0 ; i< CVBS_MODE_VALUE_LIST.length; i++){
                mTitleList.add(CVBS_MODE_VALUE_LIST[i]);
            }          
            for(int i=0 ; i < CVBS_MODE_VALUE_LIST.length ; i++){
                mValueList.add(CVBS_MODE_VALUE_LIST[i]);
            }
        } 
   
    }

	public static String getCurrentOutPutModeTitle(int type) {
        if (Utils.DEBUG) Log.d(TAG,"==== getCurrentOutPutMode() " );
        String currentHdmiMode = sw.readSysfs(DISPLAY_MODE_SYSFS);
        if(type==0){  // cvbs
        if(currentHdmiMode.contains("cvbs")){
            for(int i=0 ; i < CVBS_MODE_VALUE_LIST.length ; i++){
                if(currentHdmiMode.equals(CVBS_MODE_VALUE_LIST[i])){
                    return CVBS_MODE_TITLE_LIST[i] ;
                    }
                }
            }
            return CVBS_MODE_TITLE_LIST[0];
        }else{      // hdmi
            
            for(int i=0 ; i< ALL_HDMI_MODE_VALUE_LIST.length ; i++){
                if(currentHdmiMode.equals(ALL_HDMI_MODE_VALUE_LIST[i])){
                    if (Utils.DEBUG) Log.d(TAG,"==== get the title is :" + ALL_HDMI_MODE_TITLE_LIST[i]);
                    return ALL_HDMI_MODE_TITLE_LIST[i] ;
                }
            }
            return ALL_HDMI_MODE_TITLE_LIST[4];
        }
	}

	public void selectItem(int index) {
        final String oldMode = sw.readSysfs(DISPLAY_MODE_SYSFS);
        String mode = mValueList.get(index) ;
        if(mode.equals(oldMode)){
            if (Utils.DEBUG) Log.d(TAG,"===== the same mode with current !");
            return ;
        }
        
        isNeedShowConfirmDialog = true;
        change2NewMode(mode); 
    	adapter.setSelectItem(index);	
	}


    public void setHandler(Handler handler){
        myHandler = handler;
    }

    void setSharedPrefrences(String key ,boolean value) {
	    Editor editor = mContext.getSharedPreferences(PREFERENCE_BOX_SETTING,Context.MODE_PRIVATE).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

    public void change2NewMode(final String mode) {
        SettingsMboxActivity.oldMode = Utils.readSysFile(sw, DISPLAY_MODE_SYSFS);
        
        mom.setOutputMode(mode);
        
        if(myHandler != null){
            Message msg = myHandler.obtainMessage();
            msg.what = UPDATE_OUTPUT_MODE_UI ;
            myHandler.sendMessageDelayed(msg,2000);
        }
        
        if(isNeedShowConfirmDialog){
            Intent i2 = new Intent(); 
            if(sw.getPropertyBoolean("ro.platform.has.realoutputmode", false)){
                 i2.setAction("action.show.dialog");
                 mContext.sendBroadcastAsUser(i2, UserHandle.ALL);
                 if (Utils.DEBUG) Log.d(TAG,"===== send broadcast to start confirm dialog");
                 isNeedShowConfirmDialog = false;
            }else{
                i2.setAction("action.show.dialog");
                i2.putExtra("old_output_mode", SettingsMboxActivity.oldMode);
                mContext.sendBroadcastAsUser(i2, UserHandle.ALL);
                if (Utils.DEBUG) Log.d(TAG,"===== send broadcast to start confirm dialog");
                isNeedShowConfirmDialog = false;
            }           
        }  
        
        return; 
	}

    public String getBestMatchResolution() {
        return mom.getBestMatchResolution();
    }

    public void hdmiPlugged(){
        Log.d(TAG,"===== hdmiPlugged()");
        mom.setHdmiPlugged();
    }

    public void hdmiUnPlugged(){
        Log.d(TAG,"===== hdmiUnPlugged()");
        mom.setHdmiUnPlugged();
    }

    public boolean isHDMIPlugged() {
        return mom.isHDMIPlugged();
    }

	public boolean isHdmiCvbsDual() {
		return Utils.getPropertyBoolean(sw, "ro.platform.has.cvbsmode", false);
	}
    
    public boolean ifModeIsSetting() {
        return mom.ifModeIsSetting();
    }

    public void  filterOutputMode() {
        String str_filter_mode = sw.getPropertyString("ro.platform.filter.modes", "");
        
        if(str_filter_mode == null || str_filter_mode.length() == 0){
            return;
        }
        
        String[] array_filter_mode = str_filter_mode.split(",");
        List<String> list_value = new ArrayList<String>();
        List<String> list_title = new ArrayList<String>();

        for (int i = 0; i < ALL_HDMI_MODE_VALUE_LIST.length; i++){
            list_value.add(ALL_HDMI_MODE_VALUE_LIST[i]);
            list_title.add(ALL_HDMI_MODE_TITLE_LIST[i]);
        }

        for (int i = 0; i < array_filter_mode.length; i++){
            for (int j = 0; j < list_value.size(); j++){
                if((list_value.get(j).toString()).equals(array_filter_mode[i])){
                    list_value.remove(j);
                    list_title.remove(j);
                }
            }
        }

        ALL_HDMI_MODE_VALUE_LIST = list_value.toArray(new String[list_value.size()]);
        ALL_HDMI_MODE_TITLE_LIST = list_title.toArray(new String[list_title.size()]);
	}

    void setConfirmDialogState(boolean isNeed){
        isNeedShowConfirmDialog = isNeed;
    }

}
