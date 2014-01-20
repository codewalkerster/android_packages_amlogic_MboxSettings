package com.mbx.settingsmbox;

import android.app.Dialog;
import android.app.SystemWriteManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DisplayConfirmDialog extends Dialog {
	private String TAG = "DisplayConfirmDialog";
	private Handler mHandle = null ;
	Runnable run = null;
	String old_mode = null;
	String new_mode = null;
    Context mContext = null;
    SystemWriteManager sw = null;
    private  DisplayConfirmDialog instance = this;
    private final int DELAY_TIME = 15000 ;
    private static boolean running = false ; 
    private final static int UPDATE_OUTPUT_MODE_UI = 101;
    private static final String PREFERENCE_BOX_SETTING = "preference_box_settings";
    private boolean isNeedStop = false;
    
	public DisplayConfirmDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
        mContext = context;
        sw = (SystemWriteManager) mContext.getSystemService("system_write");
        
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog);

		run = new Runnable() {
			@Override
			public void run() {
			    synchronized(this){
                    if(!isNeedStop){
                        dismiss();
                        setOldDisplay();
                    }
                }
			}
		};

		Button button_ok = (Button) findViewById(R.id.ok);
		button_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
                mHandle.removeCallbacks(run);
                run = null;
			}

		});
        
		Button button_cancle = (Button) findViewById(R.id.cancle);
		button_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {    
                dismiss();
                setOldDisplay();
			}
		});

	}

	@Override
	public void show() {
	    Log.d(TAG, "====== show");
		super.show();
        mHandle = new Handler();
		mHandle.postDelayed(run,DELAY_TIME);
	}

    public void dismissAndStop(){
        mHandle.removeCallbacks(run);
        isNeedStop = true;
        dismiss();
    }

	@Override
	public void dismiss() {
		super.dismiss();
	}

	public void recordOldMode(String mode) {
        if(mode == null){
            mode = "720p" ;
        }
		old_mode = mode;
	}

	private void setOldDisplay() {
        Log.d(TAG, "====== setOldDisplay");
        if(old_mode == null){
           old_mode = "720p"; 
        }
        OutPutModeManager output = new OutPutModeManager(mContext,"hdmi");
        output.setConfirmDialogState(false);
        output.change2NewMode(sw,old_mode);
	}
}
