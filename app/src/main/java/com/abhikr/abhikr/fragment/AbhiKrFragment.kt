package com.abhikr.abhikr.fragment

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.abhikr.abhikr.Appstatus
import com.abhikr.abhikr.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.abhi_kr_fragment.view.*

class AbhiKrFragment : Fragment() {

    companion object {
        fun newInstance() = AbhiKrFragment()
        const val TAG="AbhikrFragment"
    }

    private lateinit var viewModel: AbhiKrViewModel
    private lateinit var contexta:Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contexta=context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.abhi_kr_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AbhiKrViewModel::class.java)
        view.webview_abhikr
        //pg.setMessage("Loading ...");
        val ak: WebSettings = view.webview_abhikr.settings
        ak.loadsImagesAutomatically = true
        view.webview_abhikr.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        ak.javaScriptEnabled = true
        // Enable responsive layout
        // Enable responsive layout
        ak.useWideViewPort = true
        // Zoom out if the content width is greater than the width of the viewport
        // Zoom out if the content width is greater than the width of the viewport
        ak.loadWithOverviewMode = true
        ak.setSupportZoom(true)
        ak.builtInZoomControls = true // allow pinch to zooom

        ak.displayZoomControls = false // disable the default zoom controls on the page


        //Toast.makeText(context, "To View Developer profile click on top - ", Toast.LENGTH_SHORT).show()
        view.webview_abhikr.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                if (!Appstatus.getInstance(contexta).isOnline) {
                    //Toast.makeText(EXP.this, "Internet detected ! Go ahead", Toast.LENGTH_SHORT).show();
                    Toast.makeText(contexta,"Connect with Internet to hide this window !!!! Loading cache file",Toast.LENGTH_SHORT).show()

                    //Toast.makeText(contexta, "Connect with internet to hide dialog message", Toast.LENGTH_SHORT).show()
                }
                super.onPageStarted(view, url, favicon)
            }
        }
        view.webview_abhikr.loadUrl("https://www.abhikr.com/")
        //https://developers.google.com/admob/android/banner
        //mAdView.setAdSize(AdSize.SMART_BANNER);
        //https://developers.google.com/admob/android/banner
//mAdView.setAdSize(AdSize.SMART_BANNER);
        val adRequest = AdRequest.Builder().build()
        view.adView_abhikr.loadAd(adRequest)
        view.adView_abhikr.adListener = object : AdListener() {
            override fun onAdLoaded() { // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(errorCode: Int) { // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() { // Code to be executed when an ad opens an overlay that
// covers the screen.
            }

            override fun onAdLeftApplication() { // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() { // Code to be executed when when the user is about to return
// to the app after tapping on an ad.
            }
        }

    }
}
