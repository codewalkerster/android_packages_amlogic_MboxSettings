package com.mbx.settingsmbox;

import android.content.Context;
import android.app.SystemWriteManager;

public class DigitalAudioManager {
    
    private final String DigitalRawFile = "/sys/class/audiodsp/digital_raw";
    private final String mAudoCapFile = "/sys/class/amhdmitx/amhdmitx0/aud_cap";
    private final String PASSTHROUGH_PROPERTY = "ubootenv.var.digitaudiooutput";
    private final String HDMI_AUIDO_SWITCH = "/sys/class/amhdmitx/amhdmitx0/config";

    private SystemWriteManager sw = null;

    public DigitalAudioManager(Context context , SystemWriteManager systemWrite){
		sw = systemWrite;   
    }

    public int autoSwitchHdmiPassthough (){    
        
        String mAudioCapInfo = sw.readSysfs(mAudoCapFile);
        if(mAudioCapInfo.contains("Dobly_Digital+")){
            sw.writeSysfs(DigitalRawFile,"2");
            sw.writeSysfs(HDMI_AUIDO_SWITCH, "audio_on");
            sw.setProperty(PASSTHROUGH_PROPERTY, "HDMI passthrough");
            return 2;
        }else if(mAudioCapInfo.contains("AC-3")){
            sw.writeSysfs(DigitalRawFile,"1");
            sw.writeSysfs(HDMI_AUIDO_SWITCH, "audio_off");
            sw.setProperty(PASSTHROUGH_PROPERTY, "SPDIF passthrough");
            return 1;
        }else{
            sw.writeSysfs(DigitalRawFile,"0");
            sw.writeSysfs(HDMI_AUIDO_SWITCH, "audio_on");
            sw.setProperty(PASSTHROUGH_PROPERTY, "PCM");
            return 0;
        }
    }


    public void setDigitalVoiceValue(String value) {
        // value : "PCM" ,"RAW","SPDIF passthrough","HDMI passthrough"
        sw.setProperty(PASSTHROUGH_PROPERTY, value);

        if ("PCM".equals(value)) {
            sw.writeSysfs(DigitalRawFile, "0");
            sw.writeSysfs(HDMI_AUIDO_SWITCH, "audio_on");
        } else if ("RAW".equals(value)) {
            sw.writeSysfs(DigitalRawFile, "1");
            sw.writeSysfs(HDMI_AUIDO_SWITCH, "audio_off");
        } else if ("SPDIF passthrough".equals(value)) {
            sw.writeSysfs(DigitalRawFile, "1");
            sw.writeSysfs(HDMI_AUIDO_SWITCH, "audio_off");
        } else if ("HDMI passthrough".equals(value)) {
            sw.writeSysfs(DigitalRawFile, "2");
            sw.writeSysfs(HDMI_AUIDO_SWITCH, "audio_on");
        }
    }

    public void enableDobly_DRC (boolean enable){
        if (enable){       //open DRC
            sw.writeSysfs("/sys/class/audiodsp/ac3_drc_control", "drchighcutscale 0x64");
            sw.writeSysfs("/sys/class/audiodsp/ac3_drc_control", "drclowboostscale 0x64");
        } else {           //close DRC
            sw.writeSysfs("/sys/class/audiodsp/ac3_drc_control", "drchighcutscale 0");
            sw.writeSysfs("/sys/class/audiodsp/ac3_drc_control", "drclowboostscale 0");
        }
    }

    public void setDoblyMode (String mode){
        //"CUSTOM_0","CUSTOM_1","LINE","RF"; default use "LINE" 

        int i = Integer.parseInt(mode);
        if (i >= 0 && i <= 3){
            sw.writeSysfs("/sys/class/audiodsp/ac3_drc_control", "drcmode" + " " + mode);
        } else {
            sw.writeSysfs("/sys/class/audiodsp/ac3_drc_control", "drcmode" + " " + "2");
        }
    }

    public void setDTS_DownmixMode(String mode){
        // 0: Lo/Ro;   1: Lt/Rt;  default 0

        int i = Integer.parseInt(mode);
        if (i >= 0 && i <= 1){
            sw.writeSysfs("/sys/class/audiodsp/dts_dec_control", "dtsdmxmode" + " " + mode);
        } else {
            sw.writeSysfs("/sys/class/audiodsp/dts_dec_control", "dtsdmxmode" + " " + "0");
        }
    }

    public void enableDTS_DRC_scale_control (boolean enable){
        if (enable) {
            sw.writeSysfs("/sys/class/audiodsp/dts_dec_control", "dtsdrcscale 0x64");
        } else {
            sw.writeSysfs("/sys/class/audiodsp/dts_dec_control", "dtsdrcscale 0");
        }
    }

    public void enableDTS_Dial_Norm_control (boolean enable) {
        if (enable) {
            sw.writeSysfs("/sys/class/audiodsp/dts_dec_control", "dtsdialnorm 1");
        } else {
            sw.writeSysfs("/sys/class/audiodsp/dts_dec_control", "dtsdialnorm 0");
        }
    }

}
