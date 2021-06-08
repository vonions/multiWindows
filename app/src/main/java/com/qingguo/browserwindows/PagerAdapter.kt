package com.qingguo.browserwindows

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class PagerAdapter(val context: Context) : RecyclerView.Adapter<PagerAdapter.ViewHolder>() {

    var TAG = "PagerAdapter"
    var listener: ItemClickListener? = null

    var isWindows = false

    var list=ArrayList<Fragment>()
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    fun remove(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.pager_item_layout, parent, false)




        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var downx = 0.0f
        var downy = 0.0f
        var canDel = false
        var movex = 0f
        var movey = 0f
        holder.itemView.setOnTouchListener(
            object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                    if (!isWindows) {
                        return false
                    }
                    when (event!!.action) {

                        MotionEvent.ACTION_DOWN -> {
                            v!!.background.alpha=255
                            downx = event!!.rawX
                            downy = event!!.rawY

                            holder.itemView.parent.requestDisallowInterceptTouchEvent(true)
                            Log.e(TAG, "x down ${downx}:y ${downy}")
                        }
                        MotionEvent.ACTION_MOVE -> {

                            movex = event!!.rawX - downx
                            movey = event!!.rawY - downy

                            Log.e(TAG, "x ACTION_MOVE ${movex}:y ${movey}")
                            if (Math.abs(movex) > Math.abs(movey) + 5) {
                                //横向滑动
                                holder.itemView.parent.requestDisallowInterceptTouchEvent(false)
                                v!!.x = 0.0f
                                v!!.y = 0.0f
                                v!!.background.alpha=255
                                return true
                            } else {
                                //纵向滑动
                                holder.itemView.parent.requestDisallowInterceptTouchEvent(true)
//                                v!!.x = movex
                                v!!.y = movey

                                if (Math.abs(movey) >= 120) {
                                    v!!.background.alpha=128
                                    canDel = true
                                    Log.e(TAG, "move$canDel")
                                    return true
                                } else {
                                    canDel = false
                                    Log.e(TAG, "move$canDel")
                                    v!!.background.alpha=255
                                }
                                return true

                            }

                        }
                        MotionEvent.ACTION_UP -> {

                            if (Math.abs(movex) <= 10 || Math.abs(movey) <= 10) {
                                //说明是点击
                                Log.e(TAG, "click")
                                v!!.performClick()
                                return true
                            }
                            if (canDel) {
                                v!!.x = 0.0f
                                v!!.y = 0.0f
                                remove(position)
                                Log.e(TAG, "remove")
                                holder.itemView.parent.requestDisallowInterceptTouchEvent(true)

                            } else {
                                Log.e(TAG, "ACTION_UP  ")
                                v!!.x = 0.0f
                                v!!.y = 0.0f
                                holder.itemView.parent.requestDisallowInterceptTouchEvent(false)
                            }

                            v!!.background.alpha=255
                            var downx = 0.0f
                            var downy = 0.0f
                            var canDel = false
                            var movex = 0f
                            var movey = 0f
                            return true


                        }
                        MotionEvent.ACTION_CANCEL -> {
                            Log.e(TAG, "ACTION_CANCEL  ")
                            holder.itemView.parent.requestDisallowInterceptTouchEvent(false)
                            return true
                        }
                    }
                    return true
                }

            }
        )
        holder.itemView.setOnClickListener {

            listener?.onClick()

        }

    }


    override fun getItemCount(): Int {

        return list.size
    }

    interface ItemClickListener {
        fun onClick()
    }
}