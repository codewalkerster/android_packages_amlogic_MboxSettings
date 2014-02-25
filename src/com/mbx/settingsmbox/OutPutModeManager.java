package com.mbx.settingsmbox;

import android.app.SystemWriteManager;
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
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle ;

public class OutPutModeManager {
    private static final String TAG = "OutPutModeManager";

	private static Context mContext = null;

	private final static String sel_480ioutput_x = "ubootenv.var.480ioutputx";
	private final static String sel_480ioutput_y = "ubootenv.var.480ioutputy";
	private final static String sel_480ioutput_width = "ubootenv.var.480ioutputwidth";
	private final static String sel_480ioutput_height = "ubootenv.var.480ioutputheight";
	private final static String sel_480poutput_x = "ubootenv.var.480poutputx";
	private final static String sel_480poutput_y = "ubootenv.var.480poutputy";
	private final static String sel_480poutput_width = "ubootenv.var.480poutputwidth";
	private final static String sel_480poutput_height = "ubootenv.var.480poutputheight";
	private final static String sel_576ioutput_x = "ubootenv.var.576ioutputx";
	private final static String sel_576ioutput_y = "ubootenv.var.576ioutputy";
	private final static String sel_576ioutput_width = "ubootenv.var.576ioutputwidth";
	private final static String sel_576ioutput_height = "ubootenv.var.576ioutputheight";
	private final static String sel_576poutput_x = "ubootenv.var.576poutputx";
	private final static String sel_576poutput_y = "ubootenv.var.576poutputy";
	private final static String sel_576poutput_width = "ubootenv.var.576poutputwidth";
	private final static String sel_576poutput_height = "ubootenv.var.576poutputheight";
	private final static String sel_720poutput_x = "ubootenv.var.720poutputx";
	private final static String sel_720poutput_y = "ubootenv.var.720poutputy";
	private final static String sel_720poutput_width = "ubootenv.var.720poutputwidth";
	private final static String sel_720poutput_height = "ubootenv.var.720poutputheight";
	private final static String sel_1080ioutput_x = "ubootenv.var.1080ioutputx";
	private final static String sel_1080ioutput_y = "ubootenv.var.1080ioutputy";
	private final static String sel_1080ioutput_width = "ubootenv.var.1080ioutputwidth";
	private final static String sel_1080ioutput_height = "ubootenv.var.1080ioutputheight";
	private final static String sel_1080poutput_x = "ubootenv.var.1080poutputx";
	private final static String sel_1080poutput_y = "ubootenv.var.1080poutputy";
	private final static String sel_1080poutput_width = "ubootenv.var.1080poutputwidth";
	private final static String sel_1080poutput_height = "ubootenv.var.1080poutputheight";
    private final static String sel_4k2k24hzoutput_x = "ubootenv.var.4k2k24hz_x";
	private final static String sel_4k2k24hzoutput_y = "ubootenv.var.4k2k24hz_y";
	private final static String sel_4k2k24hzoutput_width = "ubootenv.var.4k2k24hz_width";
	private final static String sel_4k2k24hzoutput_height = "ubootenv.var.4k2k24hz_height";
    private final static String sel_4k2k25hzoutput_x = "ubootenv.var.4k2k25hz_x";
	private final static String sel_4k2k25hzoutput_y = "ubootenv.var.4k2k25hz_y";
	private final static String sel_4k2k25hzoutput_width = "ubootenv.var.4k2k25hz_width";
	private final static String sel_4k2k25hzoutput_height = "ubootenv.var.4k2k25hz_height";
    private final static String sel_4k2k30hzoutput_x = "ubootenv.var.4k2k30hz_x";
	private final static String sel_4k2k30hzoutput_y = "ubootenv.var.4k2k30hz_y";
	private final static String sel_4k2k30hzoutput_width = "ubootenv.var.4k2k30hz_width";
	private final static String sel_4k2k30hzoutput_height = "ubootenv.var.4k2k30hz_height";
    private final static String sel_4k2ksmpteoutput_x = "ubootenv.var.4k2ksmpte_x";
	private final static String sel_4k2ksmpteoutput_y = "ubootenv.var.4k2ksmpte_y";
	private final static String sel_4k2ksmpteoutput_width = "ubootenv.var.4k2ksmpte_width";
	private final static String sel_4k2ksmpteoutput_height = "ubootenv.var.4k2ksmpte_height";
	private static final int OUTPUT480_FULL_WIDTH = 720;
	private static final int OUTPUT480_FULL_HEIGHT = 480;
	private static final int OUTPUT576_FULL_WIDTH = 720;
	private static final int OUTPUT576_FULL_HEIGHT = 576;
	private static final int OUTPUT720_FULL_WIDTH = 1280;
	private static final int OUTPUT720_FULL_HEIGHT = 720;
	private static final int OUTPUT1080_FULL_WIDTH = 1920;
	private static final int OUTPUT1080_FULL_HEIGHT = 1080;
    private static final int OUTPUT4k2k_FULL_WIDTH = 3840;
	private static final int OUTPUT4k2k_FULL_HEIGHT = 2160;
    private static final int OUTPUT4k2ksmpte_FULL_WIDTH = 4096;
	private static final int OUTPUT4k2ksmpte_FULL_HEIGHT = 2160;
    
	private static final String PpscalerRectFile = "/sys/class/ppmgr/ppscaler_rect";
    private static final String UpdateFreescaleFb0File = "/sys/class/graphics/fb0/update_freescale";
	private static final String FreescaleFb0File = "/sys/class/graphics/fb0/free_scale";
	private static final String FreescaleFb1File = "/sys/class/graphics/fb1/free_scale";
	private static final String mHdmiPluggedVdac = "/sys/class/aml_mod/mod_off";
    private static final String mHdmiUnpluggedVdac = "/sys/class/aml_mod/mod_on";
    private static final String HDMI_SUPPORT_LIST_SYSFS = "/sys/class/amhdmitx/amhdmitx0/disp_cap";
    private static final String PpscalerFile = "/sys/class/ppmgr/ppscaler";
	private static final String VideoAxisFile = "/sys/class/video/axis";
    private static final String request2XScaleFile = "/sys/class/graphics/fb0/request2XScale";
	private static final String scaleAxisOsd0File = "/sys/class/graphics/fb0/scale_axis";
	private static final String scaleAxisOsd1File = "/sys/class/graphics/fb1/scale_axis";
	private static final String scaleOsd1File = "/sys/class/graphics/fb1/scale";
    private static final String OutputModeFile = "/sys/class/display/mode";
	private static final String OutputAxisFile= "/sys/class/display/axis";
    private static final String windowAxisFile = "/sys/class/graphics/fb0/window_axis";

    private static final String[] ALL_HDMI_MODE_VALUE_LIST = {"480i","480p","576i","576p","720p50hz","720p","1080i","1080p","1080i50hz","1080p50hz","4k2k24hz","4k2k25hz", "4k2k30hz", "4k2ksmpte"};
    private static final String[] ALL_HDMI_MODE_TITLE_LIST = {"HDMI 480I","HDMI 480P","HDMI 576I","HDMI 576P","HDMI 720P 50HZ","HDMI 720P 60HZ","HDMI 1080I 60HZ","HDMI 1080P 60HZ","HDMI 1080I 50HZ","HDMI 1080P 50HZ" ,"HDMI 4K 24HZ","HDMI 4K 25HZ","HDMI 4K 30HZ","HDMI 4K SMPTE"};
    private static final String[] HDMI_MODE_VALUE_LIST = {"480i","480p","576i","576p","720p","1080i","1080p","720p50hz","1080i50hz","1080p50hz"};
    private static final String[] HDMI_MODE_TITLE_LIST = {"HDMI 480I","HDMI 480P","HDMI 576I","HDMI 576P","HDMI 720P 60HZ","HDMI 1080I 60HZ","HDMI 1080P 60HZ","HDMI 720P 50HZ","HDMI 1080I 50HZ","HDMI 1080P 50HZ"};
    private static final String[] CVBS_MODE_VALUE_LIST = {"480cvbs","576cvbs"}; 
    private static final String[] CVBS_MODE_TITLE_LIST = {"480 CVBS","576 CVBS"}; 
    private static final String[] COMMON_MODE_VALUE_LIST =  {"480i","480p","576i","576p","720p","1080i","1080p","720p50hz","1080i50hz","1080p50hz" , "480cvbs","576cvbs","4k2k24hz","4k2k25hz", "4k2k30hz", "4k2ksmpte"};
    private static final String CVBS_MODE_PROP = "ubootenv.var.cvbsmode";
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
	private static  SystemWriteManager sw;

    private static String mUiMode = "hdmi";
    private Runnable setBackRunnable = null;
    private	static Handler myHandler = null;
    private boolean hasRealOutput = false;
    private final static int UPDATE_OUTPUT_MODE_UI = 101;
    private boolean isNeedShowConfirmDialog = false;
    private static final String PREFERENCE_BOX_SETTING = "preference_box_settings";
    private static final int hdmi4KmodeNum =  4 ;
    public OutPutModeManager(Context context , String mode){
        mContext = context;
        mUiMode = mode;
		sw = (SystemWriteManager) mContext.getSystemService("system_write");
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
        hasRealOutput = sw.getPropertyBoolean("ro.platform.has.realoutputmode", false);
        if(mode.equalsIgnoreCase("hdmi")){
            if(hasRealOutput){
                for(int i=0 ; i< ALL_HDMI_MODE_VALUE_LIST.length ; i++){
                    mTitleList.add(ALL_HDMI_MODE_TITLE_LIST[i]);
                    mValueList.add(ALL_HDMI_MODE_VALUE_LIST[i]);
                } 

            }else{
                for(int i=0 ; i< ALL_HDMI_MODE_VALUE_LIST.length-hdmi4KmodeNum; i++){
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

	public static String getCurrentOutPutModeTitle(SystemWriteManager swm ,int type) {
        Log.d(TAG,"==== getCurrentOutPutMode() " );
        String currentHdmiMode = swm.readSysfs(DISPLAY_MODE_SYSFS);
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
                    Log.d(TAG,"==== get the title is :" + ALL_HDMI_MODE_TITLE_LIST[i]);
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
            Log.d(TAG,"===== the same mode with current !");
            return ;
        }
        if(mode.contains("cvbs")&& isHdmiCvbsDual()){
            setCvbsInDualMode(mode);
        }else{
            isNeedShowConfirmDialog = true;
            change2NewMode(sw,mode); 
        }
    	adapter.setSelectItem(index);	
	}

	public static int[] getPosition(SystemWriteManager swm ,String mode) {

		int[] curPosition = { 0, 0, 1280, 720 };
		int index = 4; // 720p
		for (int i = 0; i < COMMON_MODE_VALUE_LIST.length; i++) {
			if (mode.equalsIgnoreCase(COMMON_MODE_VALUE_LIST[i]))
				index = i;
		}

		switch (index) {
		case 0: // 480i
			curPosition[0] = swm.getPropertyInt(sel_480ioutput_x, 0);
			curPosition[1] = swm.getPropertyInt(sel_480ioutput_y, 0);
			curPosition[2] = swm.getPropertyInt(sel_480ioutput_width, OUTPUT480_FULL_WIDTH);
			curPosition[3] = swm.getPropertyInt(sel_480ioutput_height, OUTPUT480_FULL_HEIGHT);
			break;
		case 1: // 480p
			curPosition[0] = swm.getPropertyInt(sel_480poutput_x, 0);
			curPosition[1] = swm.getPropertyInt(sel_480poutput_y, 0);
			curPosition[2] = swm.getPropertyInt(sel_480poutput_width, OUTPUT480_FULL_WIDTH);
			curPosition[3] = swm.getPropertyInt(sel_480poutput_height, OUTPUT480_FULL_HEIGHT);
			break;
		case 2: // 576i
			curPosition[0] = swm.getPropertyInt(sel_576ioutput_x, 0);
			curPosition[1] = swm.getPropertyInt(sel_576ioutput_y, 0);
			curPosition[2] = swm.getPropertyInt(sel_576ioutput_width, OUTPUT576_FULL_WIDTH);
			curPosition[3] = swm.getPropertyInt(sel_576ioutput_height, OUTPUT576_FULL_HEIGHT);
			break;
		case 3: // 576p
			curPosition[0] = swm.getPropertyInt(sel_576poutput_x, 0);
			curPosition[1] = swm.getPropertyInt(sel_576poutput_y, 0);
			curPosition[2] = swm.getPropertyInt(sel_576poutput_width, OUTPUT576_FULL_WIDTH);
			curPosition[3] = swm.getPropertyInt(sel_576poutput_height, OUTPUT576_FULL_HEIGHT);
			break;
		case 4: // 720p
		case 7: // 720p50hz
			curPosition[0] = swm.getPropertyInt(sel_720poutput_x, 0);
			curPosition[1] = swm.getPropertyInt(sel_720poutput_y, 0);
			curPosition[2] = swm.getPropertyInt(sel_720poutput_width, OUTPUT720_FULL_WIDTH);
			curPosition[3] = swm.getPropertyInt(sel_720poutput_height, OUTPUT720_FULL_HEIGHT);
			break;

		case 5: // 1080i
		case 8: // 1080i50hz
			curPosition[0] = swm.getPropertyInt(sel_1080ioutput_x, 0);
			curPosition[1] = swm.getPropertyInt(sel_1080ioutput_y, 0);
			curPosition[2] = swm.getPropertyInt(sel_1080ioutput_width, OUTPUT1080_FULL_WIDTH);
			curPosition[3] = swm.getPropertyInt(sel_1080ioutput_height, OUTPUT1080_FULL_HEIGHT);
			break;

		case 6: // 1080p
		case 9: // 1080p50hz
			curPosition[0] = swm.getPropertyInt(sel_1080poutput_x, 0);
			curPosition[1] = swm.getPropertyInt(sel_1080poutput_y, 0);
			curPosition[2] = swm.getPropertyInt(sel_1080poutput_width, OUTPUT1080_FULL_WIDTH);
			curPosition[3] = swm.getPropertyInt(sel_1080poutput_height, OUTPUT1080_FULL_HEIGHT);
			break;
		case 10: // 480cvbs
			curPosition[0] = swm.getPropertyInt(sel_480ioutput_x, 0);
			curPosition[1] = swm.getPropertyInt(sel_480ioutput_y, 0);
			curPosition[2] = swm.getPropertyInt(sel_480ioutput_width, OUTPUT480_FULL_WIDTH);
			curPosition[3] = swm.getPropertyInt(sel_480ioutput_height, OUTPUT480_FULL_HEIGHT);
			break;
		case 11: // 576cvbs
			curPosition[0] = swm.getPropertyInt(sel_576ioutput_x, 0);
			curPosition[1] = swm.getPropertyInt(sel_576ioutput_y, 0);
			curPosition[2] = swm.getPropertyInt(sel_576ioutput_width, OUTPUT576_FULL_WIDTH);
			curPosition[3] = swm.getPropertyInt(sel_576ioutput_height, OUTPUT576_FULL_HEIGHT);
			break;
        case 12: // 4k2k24hz
			curPosition[0] = swm.getPropertyInt(sel_4k2k24hzoutput_x, 0);
			curPosition[1] = swm.getPropertyInt(sel_4k2k24hzoutput_y, 0);
			curPosition[2] = swm.getPropertyInt(sel_4k2k24hzoutput_width, OUTPUT4k2k_FULL_WIDTH);
			curPosition[3] = swm.getPropertyInt(sel_4k2k24hzoutput_height, OUTPUT4k2k_FULL_HEIGHT);
			break;
        case 13: // 4k2k25hz
			curPosition[0] = swm.getPropertyInt(sel_4k2k25hzoutput_x, 0);
			curPosition[1] = swm.getPropertyInt(sel_4k2k25hzoutput_y, 0);
			curPosition[2] = swm.getPropertyInt(sel_4k2k25hzoutput_width, OUTPUT4k2k_FULL_WIDTH);
			curPosition[3] = swm.getPropertyInt(sel_4k2k25hzoutput_height, OUTPUT4k2k_FULL_HEIGHT);
			break;
        case 14: // 4k2k30hz
			curPosition[0] = swm.getPropertyInt(sel_4k2k30hzoutput_x, 0);
			curPosition[1] = swm.getPropertyInt(sel_4k2k30hzoutput_y, 0);
			curPosition[2] = swm.getPropertyInt(sel_4k2k30hzoutput_width, OUTPUT4k2k_FULL_WIDTH);
			curPosition[3] = swm.getPropertyInt(sel_4k2k30hzoutput_height, OUTPUT4k2k_FULL_HEIGHT);
			break;
        case 15: // 4k2ksmpte
            curPosition[0] = swm.getPropertyInt(sel_4k2ksmpteoutput_x, 0);
            curPosition[1] = swm.getPropertyInt(sel_4k2ksmpteoutput_y, 0);
            curPosition[2] = swm.getPropertyInt(sel_4k2ksmpteoutput_width, OUTPUT4k2ksmpte_FULL_WIDTH);
            curPosition[3] = swm.getPropertyInt(sel_4k2ksmpteoutput_height, OUTPUT4k2ksmpte_FULL_HEIGHT);
        break;
		default: // 720p
			curPosition[0] = swm.getPropertyInt(sel_720poutput_x, 0);
			curPosition[1] = swm.getPropertyInt(sel_720poutput_y, 0);
			curPosition[2] = swm.getPropertyInt(sel_720poutput_width, OUTPUT720_FULL_WIDTH);
			curPosition[3] = swm.getPropertyInt(sel_720poutput_height, OUTPUT720_FULL_HEIGHT);
			break;
		}

		return curPosition;
	}

    public void setHandler(Handler handler){
        myHandler = handler;
    }

    void setSharedPrefrences(String key ,boolean value) {
	    Editor editor = mContext.getSharedPreferences(PREFERENCE_BOX_SETTING,Context.MODE_PRIVATE).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

    private int getModeIndex(String mode){
        int i = 0 ;
        for( i=0 ; i< ALL_HDMI_MODE_VALUE_LIST.length ; i++){
            if(ALL_HDMI_MODE_VALUE_LIST[i].equals(mode)){
                return i;
            }
        }
        return -1;
    }

    private void changeNow(SystemWriteManager swm ,String mode){
        String curMode = Utils.readSysFile(swm, DISPLAY_MODE_SYSFS);
        String newMode = mode;
        SettingsMboxActivity.oldMode = curMode;
        
        if(curMode == null || curMode.length() < 4){
            Log.d(TAG,"===== something wrong !!!" );
            curMode =  "720p";
        }    
        Log.d(TAG,"===== change mode form *" + curMode + "* to *"+ newMode+"* ");
        if(newMode.equals(curMode)){
            Log.d(TAG,"===== The same mode as current , do nothing !");
            return ;
        }
        
       if(newMode.contains("cvbs")){
             openVdac(swm,newMode);
       }else{
             closeVdac(swm,newMode);
       }

       int oldIndex = -1;
       int newIndex = -1;
       if(swm.getPropertyBoolean("ro.platform.has.realoutputmode", false)){
            Utils.shadowScreen(swm, curMode);
            
            if(isNeedShowConfirmDialog){
                oldIndex = getModeIndex(curMode);
                newIndex = getModeIndex(newMode);
                if((oldIndex >= 6 && newIndex < 6)||(oldIndex < 6 && newIndex >= 6)){
                    setSharedPrefrences("resume_show_dialog",true);
                }else{
                    setSharedPrefrences("resume_show_dialog",false);
                } 
            }else{
                setSharedPrefrences("resume_show_dialog",false);
            }
        }
        Utils.writeSysFile(swm, DISPLAY_MODE_SYSFS,newMode);
        
        int[] curPosition = getPosition(swm ,newMode);
        String mWinAxis = curPosition[0]+" "+curPosition[1]+" "+(curPosition[0]+curPosition[2]-1)+" "+(curPosition[1]+curPosition[3]-1);
        
        if(swm.getPropertyBoolean("ro.platform.has.realoutputmode", false)){ 
            //close freescale
            Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale","0"); 
            if(newMode.contains("4k2k")){    

                if(!swm.getPropertyBoolean("ro.platform.has.native4k2k", false)){ 
                    //set to 1080p as UI base
                    Utils.setDensity(newMode);
                    Utils.setDisplaySize(1920, 1080);
                           
                    //open freescale ,  scale up from 1080p to 4k
                    Utils.writeSysFile(swm, "/sys/class/graphics/fb0/freescale_mode","1");
                    Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale_axis","0 0 1919 1079"); 
                    Utils.writeSysFile(swm, "/sys/class/graphics/fb0/window_axis",mWinAxis);
                    Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale","0x10001");
                } else {
                    //set to 4k2k as UI base
                    Utils.setDensity("4k2knative");
                    Utils.setDisplaySize(3840, 2160);
                           
                    //open freescale ,  scale up from 1080p to 4k
                    Utils.writeSysFile(swm, "/sys/class/graphics/fb0/freescale_mode","1");
                    Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale_axis","0 0 3839 2159"); 
                    Utils.writeSysFile(swm, "/sys/class/graphics/fb0/window_axis",mWinAxis);
                    if (curPosition[0] == 0 && curPosition[1] == 0 && !newMode.equals("4k2ksmpte"))
                        Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale","0");
                    else
                        Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale","0x10001");
                }
                
            }else if(newMode.contains("1080")){ 
                curPosition = getPosition(swm ,newMode);
                
                Utils.setDensity(newMode);
                Utils.setDisplaySize(1920, 1080);
                Utils.writeSysFile(swm, "/sys/class/graphics/fb0/freescale_mode","1");
                Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale_axis","0 0 1919 1079");
                Utils.writeSysFile(swm, "/sys/class/graphics/fb0/window_axis",mWinAxis);
                if (curPosition[0] == 0 && curPosition[1] == 0)
                    Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale","0");
                else
                    Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale","0x10001");
                
             }else if(newMode.contains("720")){ 
                curPosition = getPosition(swm ,newMode);
                
                Utils.setDensity(newMode);
                Utils.setDisplaySize(1280, 720);
                Utils.writeSysFile(swm, "/sys/class/graphics/fb0/freescale_mode","1");
                Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale_axis","0 0 1279 719");
                Utils.writeSysFile(swm, "/sys/class/graphics/fb0/window_axis",mWinAxis);
                if (curPosition[0] == 0 && curPosition[1] == 0)
                    Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale","0");
                else
                    Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale","0x10001");
                
            }else if(newMode.contains("576")){  

                
                Utils.setDensity(newMode);
                Utils.setDisplaySize(1280,720);
                Utils.writeSysFile(swm, "/sys/class/graphics/fb0/freescale_mode","1");
                if (newMode.equals("576i"))
                    Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale_axis","0 0 1279 721");
                else
                    Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale_axis","0 0 1279 719");
                Utils.writeSysFile(swm, "/sys/class/graphics/fb0/window_axis",mWinAxis);
                Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale","0x10001");
                
            }else if(newMode.contains("480")){  

                
                Utils.setDensity(newMode);
                Utils.setDisplaySize(1280,720);
                Utils.writeSysFile(swm, "/sys/class/graphics/fb0/freescale_mode","1");
                if (newMode.equals("480i"))
                    Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale_axis","0 0 1279 721");
                else
                    Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale_axis","0 0 1279 719");
                Utils.writeSysFile(swm, "/sys/class/graphics/fb0/window_axis",mWinAxis);
                Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale","0x10001");
                
            }else{
                Log.d(TAG,"===== can't support this mode !");
            }

            String mVideoAxis = curPosition[0] + " " + curPosition[1]+ " " + (curPosition[2] + curPosition[0]-1)
				+ " "+ (curPosition[3] + curPosition[1]-1);
            Utils.writeSysFile(swm,VideoAxisFile, mVideoAxis);
            
            String mDisplayAxis = curPosition[0] + " "+ curPosition[1] +
                        Utils.getDisplayAxisByMode(newMode)+ curPosition[0]+ " " + curPosition[1]+ " " + 18+ " " + 18;
            Utils.writeSysFile(swm, OutputAxisFile, mDisplayAxis);
            Log.d(TAG,"===== mVideoAxis:"+mVideoAxis);
            //Log.d(TAG,"===== mDisplayAxis:"+mDisplayAxis);
            
        }else {
            String value = curPosition[0] + " " + curPosition[1]
                + " " + (curPosition[2] + curPosition[0] )
                + " " + (curPosition[3] + curPosition[1] )+ " " + 0;
            setM6FreeScaleAxis(swm, newMode);
            Utils.writeSysFile(swm, DISPLAY_MODE_SYSFS,newMode);	
            Utils.writeSysFile(swm, PpscalerRectFile, value);
            //===== for new sysfs
            Utils.writeSysFile(swm, UpdateFreescaleFb0File, "1");
            Log.d(TAG,"===== do nothing about freescale !");

            Utils.writeSysFile(swm, windowAxisFile, mWinAxis);
            Log.d(TAG,"===== " + windowAxisFile + " : " + mWinAxis);
            //===== for old sysfs
            //Utils.writeSysFile(swm, FreescaleFb0File, "0");
            //Utils.writeSysFile(swm, FreescaleFb1File, "0");
            //Utils.writeSysFile(swm, FreescaleFb0File, "1");
            //Utils.writeSysFile(swm, FreescaleFb1File, "1");	
        }	
        

        swm.setProperty(COMMON_MODE_PROP, newMode); 
        saveNewMode2Prop(swm,newMode);
       
        if(myHandler != null){
            Message msg = myHandler.obtainMessage();
            msg.what = UPDATE_OUTPUT_MODE_UI ;
            myHandler.sendMessageDelayed(msg,2000);
        }
        
        if(isNeedShowConfirmDialog){
            Intent i2 = new Intent(); 
            if(swm.getPropertyBoolean("ro.platform.has.realoutputmode", false)){
                    if((oldIndex >= 6 && newIndex < 6)||(oldIndex < 6 && newIndex >= 6)){
                        Log.d(TAG,"===== wait for onresume");
                    }else{
                        i2.setAction("action.show.dialog");
                        mContext.sendBroadcastAsUser(i2, UserHandle.ALL);
                        Log.d(TAG,"===== send broadcast to start confirm dialog");
                        isNeedShowConfirmDialog = false;
                    }
            }else{
                i2.setAction("action.show.dialog");
                i2.putExtra("old_output_mode",curMode);
                mContext.sendBroadcastAsUser(i2, UserHandle.ALL);
                Log.d(TAG,"===== send broadcast to start confirm dialog");
                isNeedShowConfirmDialog = false;
            }           
        }
        
        Intent i = new Intent();
        i.setAction("android.amlogic.settings.CHANGE_OUTPUT_MODE");
        mContext.sendBroadcast(i);
        
        return;
    
    } 

    public void change2NewMode(final SystemWriteManager swm ,final String mode) {
        Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized(this){
				    changeNow(swm , mode);
                }
			}
		});

        t.start();   
	}


    public static String getCurrentOutputResolution(SystemWriteManager swm){
        String mode =  swm.readSysfs(DISPLAY_MODE_SYSFS);
        if ("480cvbs".equalsIgnoreCase(mode)) {
            mode = "480i";
        } else if ("576cvbs".equalsIgnoreCase(mode)) {
            mode = "576i";
        }
        return mode;
    }

    public static void change2NewModeWithoutFreeScale(SystemWriteManager swm, String newMode){

        String curMode = Utils.readSysFile(swm, DISPLAY_MODE_SYSFS);
        Log.d(TAG,"===== change mode form *" + curMode + "* to *"+ newMode+"* , WithoutFreeScale");
        if(newMode.equals(curMode)){
            Log.d(TAG,"===== The same mode as current , do nothing !");
            return ;
        }
        
        if(newMode.contains("cvbs")){
             openVdac(swm,newMode);
        }else{
             closeVdac(swm,newMode);
        }
        Utils.shadowScreen(swm, curMode);
		swm.writeSysfs(PpscalerFile, "0");
		swm.writeSysfs(FreescaleFb0File, "0");
		swm.writeSysfs(FreescaleFb1File, "0");
        swm.writeSysfs(DISPLAY_MODE_SYSFS,newMode);
        swm.setProperty(COMMON_MODE_PROP,newMode);
        saveNewMode2Prop(swm , newMode);
        
    	int[] curPosition = {0, 0, 1280, 720};
        curPosition = getPosition(swm,newMode);    
        String value = curPosition[0] + " " + curPosition[1]+ " " + (curPosition[2] + curPosition[0] ) + " "+ (curPosition[3] + curPosition[1] ) ;

        if(swm.getPropertyBoolean("ro.platform.has.realoutputmode", false)){                     
            
            SettingsMboxActivity.mCurrentViewNum = 2 ;
            if(swm.getPropertyBoolean("ro.platform.has.native4k2k", false) && newMode.contains("4k2k")){ 
                Utils.setDensity("4k2knative");
            } else { 
                Utils.setDensity(newMode);
            }

            if(swm.getPropertyBoolean("ro.platform.has.native4k2k", false) && newMode.contains("4k2k")){ 
                Utils.setDisplaySize(3840, 2160);
            } else if(newMode.contains("1080") || newMode.contains("4k2k")){
                Utils.setDisplaySize(1920, 1080);
            } else {
                Utils.setDisplaySize(1280, 720);
            }
            String display_value = curPosition[0] + " "+ curPosition[1] + " "
                    + 1920+ " "+ 1080+ " "
                    + curPosition[0]+ " " + curPosition[1]+ " " + 18+ " " + 18;
            Utils.writeSysFile(swm, OutputAxisFile, display_value);
            Log.d("OutputSettings", "outputmode change:curPosition[2]:"+curPosition[2]+" curPosition[3]:"+curPosition[3]+"\n");
        }else {
            if((newMode.equals(COMMON_MODE_VALUE_LIST[5])) || (newMode.equals(COMMON_MODE_VALUE_LIST[6]))
                        || (newMode.equals(COMMON_MODE_VALUE_LIST[8])) || (newMode.equals(COMMON_MODE_VALUE_LIST[9]))){
                swm.writeSysfs(OutputAxisFile, ((int)(curPosition[0]/2))*2 + " " + ((int)(curPosition[1]/2))*2 
                + " 1280 720 "+ ((int)(curPosition[0]/2))*2 + " "+ ((int)(curPosition[1]/2))*2 + " 18 18");
                    swm.writeSysfs(scaleAxisOsd0File, "0 0 " + (960 - (int)(curPosition[0]/2) )
                + " " + (1080 - (int)(curPosition[1]/2) ));
                swm.writeSysfs(request2XScaleFile, "7 " + ((int)(curPosition[2]/2)) + " " + ((int)(curPosition[3]/2))*2);
                swm.writeSysfs(scaleAxisOsd1File, "1280 720 " + ((int)(curPosition[2]/2))*2 + " " + ((int)(curPosition[3]/2))*2);
                swm.writeSysfs(scaleOsd1File, "0x10001");
            }else{
                swm.writeSysfs(OutputAxisFile, curPosition[0] + " " + curPosition[1] 
                    + " 1280 720 "+ curPosition[0] + " "+ curPosition[1] + " 18 18");
                swm.writeSysfs(request2XScaleFile, "16 " + curPosition[2] + " " + curPosition[3]);
                swm.writeSysfs(scaleAxisOsd1File, "1280 720 " + curPosition[2] + " " + curPosition[3]);
                swm.writeSysfs(scaleOsd1File, "0x10001");
            }


    	    swm.writeSysfs(VideoAxisFile, curPosition[0] + " " + curPosition[1]
    					+ " " + (curPosition[2] + curPosition[0] ) + " "
    					+ (curPosition[3] + curPosition[1] ));
        }
        
    }

    private static void saveNewMode2Prop(SystemWriteManager swm, String newMode){
        Log.e(TAG,"----saveNewMode2Prop:"+newMode);
        if((newMode != null) && newMode.contains("cvbs")){
            swm.setProperty(CVBS_MODE_PROP, newMode);
        }
        else{
            swm.setProperty(HDMI_MODE_PROP, newMode);
        }         
    }

    public static void closeVdac(SystemWriteManager sw ,String outputmode){
       if(sw.getPropertyBoolean("ro.platform.hdmionly",false)){
           if(!outputmode.contains("cvbs")){
               sw.writeSysfs(mHdmiPluggedVdac,"vdac");
               Log.d(TAG,"close vdac");
           }
       }
    }
    
    public static void openVdac(SystemWriteManager sw ,String outputmode){
        if(sw.getPropertyBoolean("ro.platform.hdmionly",false)){
            if(outputmode.contains("cvbs")){
                sw.writeSysfs(mHdmiUnpluggedVdac,"vdac"); 
            }     
        }
    }
    
	public void setCvbsInDualMode(String mode) {
        String currentHdmiMode = sw.readSysfs(DISPLAY_MODE_SYSFS);
        if("480i".equals(currentHdmiMode) ||"576i".equals(currentHdmiMode)){
            Utils.writeSysFile(sw, "/sys/class/display2/mode", "null");
        }else{
            String currentMode = sw.readSysfs(DISPLAY_MODE_SYSFS_DUAL);
            if(currentMode.equals(mode)){
                Log.d(TAG,"Set the same mode with current !!!");
                return ;
            }
            if (mode.equals("480cvbs")) {
                Utils.writeSysFile(sw, "/sys/class/display2/mode", "null");
                Utils.writeSysFile(sw, "/sys/class/display2/mode", "480cvbs");
                Utils.writeSysFile(sw, "/sys/class/video2/screen_mode", "1");
                Utils.writeSysFile(sw, "ubootenv.var.cvbsmode", "480cvbs");
            } else if (mode.equals("576cvbs")) {
                Utils.writeSysFile(sw, "/sys/class/display2/mode", "null");
                Utils.writeSysFile(sw, "/sys/class/display2/mode", "576cvbs");
                Utils.writeSysFile(sw, "/sys/class/video2/screen_mode", "1");
                sw.setProperty("ubootenv.var.cvbsmode", "576cvbs");
            }else{
                Log.d(TAG,"Not supprot the cvbs mode right now !!!");
            }

            if (currentHdmiMode.contains("1080")) { 
                Utils.writeSysFile(sw, "/sys/module/amvideo2/parameters/clone_frame_scale_width","960");
            } else {
                Utils.writeSysFile(sw,"/sys/module/amvideo2/parameters/clone_frame_scale_width","0");
            }
        }
	}

	public boolean isHdmiCvbsDual() {
		return Utils.getPropertyBoolean(sw, "ro.platform.has.cvbsmode", false);
	}

    public  String getFilterModes() {
		return Utils.getPropertyString(sw, "ro.platform.filter.modes", NO_FILTER_SET);
	}

    public  ArrayList<String> getCurrentSupportList(){
        ArrayList<String> list = new ArrayList<String>();
        String[] suport_values = null;
        String[] filter_values = null;
        
        String mTVSupportMode = readSupportList(HDMI_SUPPORT_LIST_SYSFS);
        if(mTVSupportMode.indexOf("480") >= 0 || mTVSupportMode.indexOf("576") >= 0
            ||mTVSupportMode.indexOf("720") >= 0||mTVSupportMode.indexOf("1080") >= 0){
            suport_values = (mTVSupportMode.substring(0, mTVSupportMode.length()-1)).split(",");
        }else{
            suport_values = new String[ALL_HDMI_MODE_VALUE_LIST.length];
            for(int i=0; i< ALL_HDMI_MODE_VALUE_LIST.length ; i++){
                suport_values[i] = ALL_HDMI_MODE_VALUE_LIST[i];
            }
        }

        String mFilterModes = getFilterModes();
        if(NO_FILTER_SET.equals(mFilterModes))
            filter_values = null;
        else
            filter_values = mFilterModes.split(",");
        
        if(filter_values!=null){
            for(int i=0; i<suport_values.length ; i++ ){ 
                int filter_index = -1;
                for(int j= 0;j<filter_values.length ; j++){
                    if((suport_values[i].equals(filter_values[j]))){
                        filter_index = i;
                        continue;
                    }
                }
                if(i != filter_index ){
                    list.add(suport_values[i]);
                }
            } 
        }else{
             for(int i=0; i<suport_values.length ; i++ ){
                list.add(suport_values[i]);
             }
        }
            
        if(list==null || list.size()==0){
            for(int i=0; i<suport_values.length ; i++ ){ 
                list.add(suport_values[i]);
            }
        }

        for(String s : list){
            Log.d(TAG,"===== support list :" + s);
        }
        
        return list ;
    }

    public static String readSupportList(String path) {
		
        String str = null;
        StringBuilder value = new StringBuilder();    
        try {
            FileReader fr = new FileReader(path);
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
                    Log.d(TAG,"=====TV support list is : " + value.toString());
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

    void setM6FreeScaleAxis(SystemWriteManager swm, String mode){
        if(mode.contains("720") || mode.contains("1080")){
            Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale_axis","0 0 1279 719");
        } else {
            Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale_axis","0 0 1281 719");  
        }
        Utils.writeSysFile(swm, "/sys/class/graphics/fb0/free_scale","1");
    }
}
