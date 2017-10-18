package com.airse.trickynumbers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler {
	//AdView adView;
	InterstitialAd interstitialAd;
	View gameView;
	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;


	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case SHOW_ADS:
					//adView.setVisibility(View.VISIBLE);
					showOrLoadInterstitial();
					break;
				case HIDE_ADS:
					//adView.setVisibility(View.GONE);
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
//		initialize(new TrickyNumbers(), config);


		RelativeLayout main_layout = new RelativeLayout(this);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		gameView = initializeForView(new TrickyNumbers(this), config);
		main_layout.addView(gameView);

//		adView = new AdView(this);
//		adView.setAdUnitId(getResources().getString(R.string.banner_id));
//		adView.setAdSize(AdSize.BANNER);
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice("BA4A61503489F890AA607747073F1F6A")
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.build();
//		adView.loadAd(adRequest);
		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_id));
		interstitialAd.setAdListener(new AdListener() {
      		@Override
      		public void onAdLoaded() {
//				Toast.makeText(getApplicationContext(), "Finished Loading Interstitial", Toast.LENGTH_SHORT).show();
			}
     		@Override
      		public void onAdClosed() {
//				Toast.makeText(getApplicationContext(), "Closed Interstitial", Toast.LENGTH_SHORT).show();
			}
    	});
		adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

//		main_layout.addView(adView, adParams);

		setContentView(main_layout);
	}
	public void showOrLoadInterstitial() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					if (interstitialAd.isLoaded()) {
						interstitialAd.show();
//						Toast.makeText(getApplicationContext(), "Showing Interstitial", Toast.LENGTH_SHORT).show();
					}
//					else {
//						AdRequest interstitialRequest = new AdRequest.Builder()
//								.addTestDevice("BA4A61503489F890AA607747073F1F6A")
//								.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//								.build();
//						interstitialAd.loadAd(interstitialRequest);
//						Toast.makeText(getApplicationContext(), "Loading Interstitial", Toast.LENGTH_SHORT).show();
//					}
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
//					Toast.makeText(getApplicationContext(), "Loading Interstitial", Toast.LENGTH_SHORT).show();
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
