package com.abhikr.abhikr.fragment

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.*
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.abhikr.abhikr.Appstatus
import com.abhikr.abhikr.R
import com.abhikr.abhikr.service.AppCrash
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class AbhiKrFragment : Fragment() {

    companion object {
        fun newInstance() = AbhiKrFragment()
        const val TAG="AbhikrFragment"
    }

    private lateinit var viewModel: AbhiKrViewModel
    private var contexta:Context?=null
    private lateinit var abhiAds:AdView
    private lateinit var webview:WebView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        contexta=context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private val handler: Handler = object : Handler() {
        override fun handleMessage(message: Message) {
            when (message.what) {
                1 -> {
                    webViewGoBack()
                }
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.abhi_kr_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AbhiKrViewModel::class.java)
        webview=view.findViewById(R.id.webview_abhikr)
        abhiAds=view.findViewById(R.id.adView_abhikr)
        //pg.setMessage("Loading ...");
        val ak: WebSettings = webview.settings
        ak.loadsImagesAutomatically = true
        webview.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        ak.javaScriptEnabled = true
        // Enable responsive layout
        ak.useWideViewPort = true
        // Zoom out if the content width is greater than the width of the viewport
        ak.loadWithOverviewMode = true
        ak.setSupportZoom(true)
        ak.builtInZoomControls = true // allow pinch to zooom
        ak.displayZoomControls = false // disable the default zoom controls on the page

        //Toast.makeText(context, "To View Developer profile click on top - ", Toast.LENGTH_SHORT).show()
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url!!)
                return true
            }
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                if (!Appstatus.getInstance(AppCrash.getInstance().applicationContext).isOnline) {
                    //Toast.makeText(EXP.this, "Internet detected ! Go ahead", Toast.LENGTH_SHORT).show();
                    Toast.makeText(AppCrash.getInstance().applicationContext,"Connect with Internet to hide this window !!!! Loading cache file",Toast.LENGTH_SHORT).show()

                    //Toast.makeText(contexta, "Connect with internet to hide dialog message", Toast.LENGTH_SHORT).show()
                }
                super.onPageStarted(view, url, favicon)
            }
        }
        webview.loadUrl("https://www.abhikr.com/")
        webview.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_UP && webview.canGoBack()) {
                    handler.sendEmptyMessage(1)
                    return true
                }
               /* if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == MotionEvent.ACTION_UP
                    && webview.canGoBack()) {
                    webview.goBack()
                    return true;
                }*/
                return false
            }
        })
        //https://developers.google.com/admob/android/banner
        //mAdView.setAdSize(AdSize.SMART_BANNER);
        //https://developers.google.com/admob/android/banner
//mAdView.setAdSize(AdSize.SMART_BANNER);
        MobileAds.initialize(requireContext())
        val adRequest = AdRequest.Builder().build()
        abhiAds.loadAd(adRequest)
        abhiAds.adListener = object: AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d(TAG,"ads imp : ad loaded")
            }

            override fun onAdFailedToLoad(errorCode : Int) {
                // Code to be executed when an ad request fails.
                Log.d(TAG,"ads imp : ad loaded $errorCode")
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d(TAG,"ads imp : ad opened")

            }

            override fun onAdClicked() {
                Log.d(TAG,"ads imp : ad clicked")
                //Toast.makeText(contexta,"ads clicked",Toast.LENGTH_SHORT).show()
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                Log.d(TAG,"ads imp : ad left by user")
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.d(TAG,"ads imp : ad closed")
            }
        }
    }

    private fun webViewGoBack() {
        if (webview.canGoBack()) webview.goBack() // if there is previous page open it
    }

    override fun onPause() {
        super.onPause()
        abhiAds.pause()
    }

    override fun onResume() {
        super.onResume()
        abhiAds.resume()
    }
    override fun onDestroy() {
        super.onDestroy()
        abhiAds.destroy()
    }
    override fun onDetach() {
        super.onDetach()
        contexta=null
    }
}
