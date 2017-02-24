package com.adlocus;

import android.app.Activity;
import android.util.Log;

import com.adlocus.Ad;
import com.adlocus.AdListener;
import com.adlocus.AdLocusLayout;
import com.adlocus.AdLocusTargeting;
import com.adlocus.InterstitialAd;
//import com.google.ads.AdRequest.Gender;
import com.google.ads.AdSize;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventBanner;
import com.google.ads.mediation.customevent.CustomEventBannerListener;
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;

public class AdLocusAdapter implements CustomEventBanner, AdListener, CustomEventInterstitial
{
	private CustomEventBannerListener bannerListener;
	private AdLocusLayout adView;

	private CustomEventInterstitialListener interstitialListener;
	private InterstitialAd interstitialAdView;

	@Override
	public void requestBannerAd(final CustomEventBannerListener listener,
								final Activity activity,
								String label,
								String serverParameter,
								AdSize adSize,
								MediationAdRequest mediationAdRequest,
								Object extra) {
		// Keep the custom event listener for use later.
		this.bannerListener = listener;

		// Determine the best ad format to use given the adSize. If the adSize
		// isn't appropriate for any format, an ad will not fill.
		int adlocusAdSize = AdLocusLayout.AD_SIZE_BANNER;
		AdSize bestAdSize = adSize.findBestSize(
				AdSize.BANNER,
				AdSize.IAB_BANNER,
				AdSize.IAB_LEADERBOARD,
				AdSize.IAB_MRECT);
		if (bestAdSize == null) {
			listener.onFailedToReceiveAd();
			return;
		}

		if(bestAdSize.equals(AdSize.BANNER) || bestAdSize.equals(AdSize.IAB_BANNER))
		{
			adlocusAdSize = AdLocusLayout.AD_SIZE_BANNER;
		}
		else if(bestAdSize.equals(AdSize.IAB_MRECT))
		{
			adlocusAdSize = AdLocusLayout.AD_SIZE_IAB_MRECT;
		}
		else if(bestAdSize.equals(AdSize.IAB_LEADERBOARD))
		{
			adlocusAdSize = AdLocusLayout.AD_SIZE_IAB_LEADERBOARD;
		}
		// Generate an ad request using custom targeting values provided in the
		// MediationAdRequest.

		AdLocusTargeting adRequest = new AdLocusTargeting();
		if(mediationAdRequest != null)
		{
			AdLocusTargeting.Gender gender;
			com.google.ads.AdRequest.Gender g = mediationAdRequest.getGender();
			if(g != null)
			{
				switch (g)
				{
					case FEMALE:
						gender = AdLocusTargeting.Gender.FEMALE;

						break;
					case MALE:
						gender = AdLocusTargeting.Gender.MALE;

						break;
					case UNKNOWN:
						gender = AdLocusTargeting.Gender.UNKNOWN;

						break;
					default:
						gender = AdLocusTargeting.Gender.UNKNOWN;

						break;
				}
				adRequest.setGender(gender);
			}
			if(mediationAdRequest.getBirthday() != null)
			{
				adRequest.setBirthday(mediationAdRequest.getBirthday());
			}

			adRequest.setTestMode(mediationAdRequest.isTesting());
		}
		// Load the ad with the ad request.
//		this.adView.loadAd(adRequest);

		// Initialize an AdView with the bestAdSize and the publisher ID.
		// The publisher ID is the server parameter that you gave when creating
		// the custom event.
		this.adView = new AdLocusLayout(activity, adlocusAdSize, serverParameter, -1, adRequest);

		float density = activity.getResources().getDisplayMetrics().density;

		int width = -1;
		int height = -1;
		width = (int) (adSize.getWidth() * density);
		height = (int) (adSize.getHeight() * density);
		adView.setLayoutSize(width, height);
		// Set the listener to register for events.
		this.adView.setListener(this);
	}


	@Override
	public void requestInterstitialAd(CustomEventInterstitialListener listener,
									  Activity activity, String label, String serverParameter, MediationAdRequest mediationAdRequest,
									  Object extra)
	{
		this.interstitialListener = listener;

		AdLocusTargeting.Gender gender;
		switch (mediationAdRequest.getGender())
		{
			case FEMALE:
				gender = AdLocusTargeting.Gender.FEMALE;
				break;
			case MALE:
				gender = AdLocusTargeting.Gender.MALE;
				break;
			case UNKNOWN:
				gender = AdLocusTargeting.Gender.UNKNOWN;
				break;
			default:
				gender = AdLocusTargeting.Gender.UNKNOWN;
				break;
		}
		AdLocusTargeting adRequest = new AdLocusTargeting()
				.setBirthday(mediationAdRequest.getBirthday())
				.setGender(gender);

		adRequest.setTestMode(mediationAdRequest.isTesting());
		interstitialAdView = new InterstitialAd(activity, serverParameter, adRequest);
		interstitialAdView.setListener(this);
		interstitialAdView.loadAd();
	}

	@Override
	public void showInterstitial()
	{
		if (this.interstitialAdView != null) {
			this.interstitialAdView.show();
		}
	}

	@Override
	public void destroy() {
		// The destroy method gets called when the mediation framework refreshes
		// and removes the custom event. Perform any necessary cleanup here.
		if (this.adView != null) {
			this.adView = null;
		}
		if (this.interstitialAdView != null) {
			this.interstitialAdView = null;
		}
	}

	@Override
	public void onReceiveAd(Ad ad) {

		if(interstitialListener != null)
		{
			interstitialListener.onReceivedAd();
		}
		if(bannerListener != null && adView != null)
		{
			bannerListener.onReceivedAd(this.adView);
			adView.invalidate();
		}
	}

	@Override
	public void onFailedToReceiveAd(Ad ad, AdLocusLayout.ErrorCode errorCode) {
		if(interstitialListener != null)
		{
			interstitialListener.onFailedToReceiveAd();
		}
		if(bannerListener != null)
		{
			bannerListener.onFailedToReceiveAd();
		}
	}

}
