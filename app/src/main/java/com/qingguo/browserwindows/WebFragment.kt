package com.qingguo.browserwindows

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class WebFragment : Fragment() {

    val TAG = "WebFragment"
    var isWindows = false
    lateinit var pp: LinearLayout
    lateinit var touch: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_webview_layout, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View?) {


        touch = view!!.findViewById(R.id.iv_view)


        pp = view.findViewById<LinearLayout>(R.id.ll_parent)

        setTouch()

        val web = view!!.findViewById<WebView>(R.id.webview)
        initWebViewSettings(web)

    }

    fun setTouch() {
        var downx = 0.0f
        var downy = 0.0f
        var canDel = false
        var movex = 0f
        var movey = 0f
        touch.setOnTouchListener(
            object : View.OnTouchListener {
                @SuppressLint("ClickableViewAccessibility")
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                    if (!isWindows) {
                        return false
                    }
                    when (event!!.action) {

                        MotionEvent.ACTION_DOWN -> {
                            v!!.background.alpha = 255
                            downx = event!!.rawX
                            downy = event!!.rawY

                            touch?.parent.requestDisallowInterceptTouchEvent(true)
                            Log.e(TAG, "x down ${downx}:y ${downy}")
                        }
                        MotionEvent.ACTION_MOVE -> {

                            movex = event!!.rawX - downx
                            movey = event!!.rawY - downy

                            Log.e(TAG, "x ACTION_MOVE ${movex}:y ${movey}")
                            if (Math.abs(movex) > Math.abs(movey) + 5) {
                                //横向滑动
                                view?.parent!!.requestDisallowInterceptTouchEvent(false)
                                pp!!.x = 0.0f
                                pp!!.y = 0.0f
                                pp!!.background.alpha = 255
                                return true
                            } else {
                                //纵向滑动
                                touch?.parent.requestDisallowInterceptTouchEvent(true)
                                //                                v!!.x = movex
                                pp!!.y = movey

                                if (Math.abs(movey) >= 120) {
                                    pp!!.background.alpha = 128
                                    canDel = true
                                    Log.e(TAG, "move$canDel")
                                    return true
                                } else {
                                    canDel = false
                                    Log.e(TAG, "move$canDel")
                                    pp!!.background.alpha = 255
                                }
                                return true

                            }

                        }
                        MotionEvent.ACTION_UP -> {

                            if (Math.abs(movex) <= 10 || Math.abs(movey) <= 10) {
                                //说明是点击
                                Log.e(TAG, "click")

                                EventBus.getDefault().post("full")
                                //                                pp!!.performClick()
                                return true
                            }
                            if (canDel) {
                                pp!!.x = 0.0f
                                pp!!.y = 0.0f
                                //                                remove(position)
                                Log.e(TAG, "remove")
                                EventBus.getDefault().post("del")
                                touch?.parent.requestDisallowInterceptTouchEvent(true)

                            } else {
                                Log.e(TAG, "ACTION_UP  ")
                                pp!!.x = 0.0f
                                pp!!.y = 0.0f
                                touch?.parent.requestDisallowInterceptTouchEvent(false)
                            }

                            pp!!.background.alpha = 255
                            var downx = 0.0f
                            var downy = 0.0f
                            var canDel = false
                            var movex = 0f
                            var movey = 0f
                            return true


                        }
                        MotionEvent.ACTION_CANCEL -> {
                            Log.e(TAG, "ACTION_CANCEL  ")
                            view?.parent!!.requestDisallowInterceptTouchEvent(false)
                            return true
                        }
                    }
                    return true
                }

            }
        )
    }


    private fun initWebViewSettings(web: WebView) {
        val webSetting: WebSettings = web.settings
        webSetting.setJavaScriptEnabled(true)
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true)
        webSetting.setAllowFileAccess(true)
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS)
        webSetting.setSupportZoom(true)
        webSetting.setBuiltInZoomControls(true)
        webSetting.setUseWideViewPort(true)
        webSetting.setSupportMultipleWindows(true)
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true)
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true)
        webSetting.setGeolocationEnabled(true)
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE)
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND)
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE)

        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计

        web.loadUrl("https://www.baidu.com/")
    }

    fun showWindows(show: Boolean) {
        isWindows = show

        if (show) {
            touch.visibility = View.VISIBLE
        } else {
            touch.visibility = View.GONE
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public open fun changeWindows(show: String) {

        Log.e("WebFragment", "changeWindows")
        var ss = show.equals("true")
        showWindows(ss)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean("show",isWindows)
        Log.e(TAG+"1","onSaveInstanceState"+outState.toString())

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(savedInstanceState!=null){
            Log.e(TAG+"1"," dddd${savedInstanceState.getBoolean("show")}   $isWindows")
            isWindows=savedInstanceState.getBoolean("show")
            showWindows(isWindows)
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG+"1","ondestory")
    }
}