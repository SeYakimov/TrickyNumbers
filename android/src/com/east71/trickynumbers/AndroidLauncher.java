package com.east71.trickynumbers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler {
	InterstitialAd interstitialAd;
	View gameView;
	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;


	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case SHOW_ADS:
					showOrLoadInterstitial();
					break;
				case HIDE_ADS:
					loadInterstitial();
					break;
			}
		}
	};
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		config.numSamples = 3;

		RelativeLayout main_layout = new RelativeLayout(this);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		gameView = initializeForView(new TrickyNumbers(this), config);
		main_layout.addView(gameView);
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_id));
		adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

		setContentView(main_layout);
	}
	public void showOrLoadInterstitial() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					if (interstitialAd.isLoaded()) {
						interstitialAd.show();
					}
					else {
						AdRequest interstitialRequest = new AdRequest.Builder()
								.addTestDevice("BA4A61503489F890AA607747073F1F6A")
								.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
								.build();
						interstitialAd.loadAd(interstitialRequest);
					}
				}
			});
		} catch (Exception e) {
		}
	}
	public void loadInterstitial() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					AdRequest interstitialRequest = new AdRequest.Builder()
							.addTestDevice("BA4A61503489F890AA607747073F1F6A")
							.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
							.build();
					interstitialAd.loadAd(interstitialRequest);
				}
			});
		} catch (Exception e) {
		}
	}
	@Override
	public void onResume() {
		super.onResume();
//		if (adView != null) {
//			adView.resume();
//		}
	}

	@Override
	public void onPause() {
		super.onPause();
//		if (adView != null) {
//			adView.pause();
//		}
	}

	@Override
	public void onDestroy() {
//		if (adView != null) {
//			adView.destroy();
//		}
		super.onDestroy();
	}

	@Override
	public void showAds(boolean show) {
		handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
	}
}
