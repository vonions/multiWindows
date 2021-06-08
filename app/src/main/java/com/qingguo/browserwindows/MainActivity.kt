package com.qingguo.browserwindows

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_webview_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : FragmentActivity() {

    val TAG = "MainActivity"
    lateinit var adapter: FragmentAdapter
    lateinit var list: ArrayList<Fragment>
    var pos = 0
    var showWindow = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EventBus.getDefault().register(this)
        initview()

    }

    private fun initview() {


        list = ArrayList<Fragment>()
        list.add(WebFragment())



        adapter = FragmentAdapter(this)
//        adapter.listener = object : PagerAdapter.ItemClickListener {
//            override fun onClick() {
//                viewpager.setPageTransformer(WebViewPageNormalTransformer())
//
//                viewpager.isUserInputEnabled = false
//            }
//
//        }
        viewpager.offscreenPageLimit = 5
        viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewpager.adapter = adapter

        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                pos = position
                if (showWindow) {
                    EventBus.getDefault().post("true")
                }
            }

        })
        bt_zoom.setOnClickListener {

            viewpager.setPageTransformer(
                WebViewPageScaleTransformer(
                    viewpager.getWidth(),
                    viewpager.getHeight()
                )
            )
            viewpager.isUserInputEnabled = true
            showWindow = true
            EventBus.getDefault().post("true")
            adapter.notifyDataSetChanged()
            Log.e("WebFragment", "bt_zoom")
        }

        bt_recover.setOnClickListener {
            viewpager.setPageTransformer(WebViewPageNormalTransformer())
            showWindow = false
            EventBus.getDefault().post("false")
            adapter.notifyDataSetChanged()

            Log.e("WebFragment", "bt_recover")
//            adapter.isWindows=false
            viewpager.isUserInputEnabled = false
            delGone()
        }
        add.setOnClickListener {

            list.add(WebFragment())

            viewpager.currentItem = list.size - 1
            adapter.notifyDataSetChanged()
//            Thread(object : Runnable {
//                override fun run() {
//                    Thread.sleep(1000)
//
//                    runOnUiThread {
//                        bt_recover.performClick()
//                    }
//                }
//            }).start()

        }

    }

    /**
     * del
     *
     */
    fun del() {

        iv_del.visibility = View.VISIBLE
    }

    /**
     * 隐藏删除
     */
    fun delGone() {
        iv_del.visibility = View.GONE

    }

    inner class FragmentAdapter(acivity: FragmentActivity) : FragmentStateAdapter(acivity) {
        override fun getItemCount(): Int {
            return list.size
        }

        override fun createFragment(position: Int): Fragment {

            return list[position]
        }

        override fun getItemId(position: Int): Long {
            return list[position].hashCode().toLong()
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public open fun full(msg: String) {

        if (msg.equals("full")) {

            bt_recover.performClick()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public open fun del(msg: String) {

        if (msg.equals("del")) {

            Log.e(TAG, "current,pos+$pos")
            adapter.notifyItemRangeRemoved(pos, 1)
            list.removeAt(pos)
        }
    }
}
