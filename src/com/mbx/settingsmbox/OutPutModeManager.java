package com.mbx.settingsmbox;

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

    private static final String[] HDMI_LIST = {"1080p","1080p50hz","1080p24hz","720p","720p50hz","4k2k24hz","4k2k25hz","4k2k30hz","4k2ksmpte","576p","480p","1080i","1080i50hz","576i","480i"};
    private static final String[] HDMI_TITLE = {"1080p-60hz","1080p-50hz","1080p-24hz","720p-60hz","720p-50hz","4k2k-24hz","4k2k-25hz","4k2k-30hz","4k2k-smpte","576p-50hz","480p-60hz","1080i-60hz","1080i-50hz","576i-50hz","480i-60hz" };
    private static String[] ALL_HDMI_MODE_VALUE_LIST;
    private static String[] ALL_HDMI_MODE_TITLE_LIST;
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
	private static SystemControlManager sw;
    private static MboxOutputMode mom;

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
		sw = new SystemControlManager(mContext);
        mom = new MboxOutputMode(mContext);;
        
        mTitleList = new ArrayList<String>();
        mValueList = new ArrayList<String>();
        initModeValues(mUiMode);    
    }
	
	public OutPutModeManager(Context context, ListView listview , String mode) {
		mContext = context;
		sw = new SystemControlManager(mContext);
    
        mUiMode = mode;
        initModeValues(mUiMode);
		adapter = new OutPutListAdapter(mContext,mTitleList,getCurrentModeIndex());
		listview.setAdapter(adapter);
	}

    public int getCurrentModeIndex(){
         String currentHdmiMode = sw.readSysFs(DISPLAY_MODE_SYSFS);
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
                if (ALL_HDMI_MODE_TITLE_LIST[i] != null && ALL_HDMI_MODE_TITLE_LIST[i].length() != 0){
                    mTitleList.add(ALL_HDMI_MODE_TITLE_LIST[i]);
                    mValueList.add(ALL_HDMI_MODE_VALUE_LIST[i]);
                } 
            }
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
        String currentHdmiMode = sw.readSysFs(DISPLAY_MODE_SYSFS);
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
        final String oldMode = sw.readSysFs(DISPLAY_MODE_SYSFS);
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
        SettingsMboxActivity.oldMode = sw.readSysFs(DISPLAY_MODE_SYSFS);
        
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
                 mContext.sendBroadcast(i2);
                 if (Utils.DEBUG) Log.d(TAG,"===== send broadcast to start confirm dialog");
                 isNeedShowConfirmDialog = false;
            }else{
                i2.setAction("action.show.dialog");
                i2.putExtra("old_output_mode", SettingsMboxActivity.oldMode);
                mContext.sendBroadcast(i2);
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
		return sw.getPropertyBoolean("ro.platform.has.cvbsmode", false);
	}
    
    public boolean ifModeIsSetting() {
        return mom.ifModeIsSetting();
    }

    public void  filterOutputMode() { 
        List<String> list_value = new ArrayList<String>();
        List<String> list_title = new ArrayList<String>();

        ALL_HDMI_MODE_VALUE_LIST = HDMI_LIST;
        ALL_HDMI_MODE_TITLE_LIST = HDMI_TITLE;

        for (int i = 0; i < ALL_HDMI_MODE_VALUE_LIST.length; i++){
            if(ALL_HDMI_MODE_VALUE_LIST[i] != null){
                list_value.add(ALL_HDMI_MODE_VALUE_LIST[i]);
                list_title.add(ALL_HDMI_MODE_TITLE_LIST[i]);
            }
        }
        
        String str_filter_mode = sw.getPropertyString("ro.platform.filter.modes", "");
        if(str_filter_mode != null && str_filter_mode.length() != 0){
            String[] array_filter_mode = str_filter_mode.split(",");
            for (int i = 0; i < array_filter_mode.length; i++){
                for (int j = 0; j < list_value.size(); j++){
                    if((list_value.get(j).toString()).equals(array_filter_mode[i])){
                        list_value.remove(j);
                        list_title.remove(j);
                    }
                }
            }
        }

        String str_edid = getHdmiSupportList();
        if (str_edid != null && str_edid.length() != 0 && !str_edid.contains("null")){
            List<String> list_hdmi_mode = new ArrayList<String>();
            List<String> list_hdmi_title = new ArrayList<String>();
            for (int i = 0; i < list_value.size(); i++){
                if (str_edid.contains(list_value.get(i))){
                    list_hdmi_mode.add(list_value.get(i));
                    list_hdmi_title.add(list_title.get(i));
                }

            }
            ALL_HDMI_MODE_VALUE_LIST = list_hdmi_mode.toArray(new String[list_value.size()]);
            ALL_HDMI_MODE_TITLE_LIST = list_hdmi_title.toArray(new String[list_title.size()]);    
        } else {
            ALL_HDMI_MODE_VALUE_LIST = list_value.toArray(new String[list_value.size()]);
            ALL_HDMI_MODE_TITLE_LIST = list_title.toArray(new String[list_title.size()]);
        }
	}

    public String getHdmiSupportList(){
        String str = null;
        StringBuilder value = new StringBuilder();    
        try {
            FileReader fr = new FileReader("/sys/class/amhdmitx/amhdmitx0/disp_cap");
            BufferedReader br = new BufferedReader(fr);
            try {
                while ((str = br.readLine()) != null) {
                    if(str != null){ 
                        if(str.contains("*")){
                            value.append(str.substring(0,str.length()-1));
                        }else{
                            value.append(str);
                        }
                        value.append(",");
                    }
                };
                fr.close();
                br.close();
                if(value != null){
                    return value.toString();
                }
                else 
                    return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    void setConfirmDialogState(boolean isNeed){
        isNeedShowConfirmDialog = isNeed;
    }

}
