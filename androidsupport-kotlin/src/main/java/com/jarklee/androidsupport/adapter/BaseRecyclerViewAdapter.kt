/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2016/12/20
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.adapter

import android.content.Context
import android.support.annotation.CallSuper
import android.support.v7.widget.RecyclerView
import android.view.View
import com.jarklee.androidsupport.R
import com.jarklee.androidsupport.common.Releasable

abstract class BaseRecyclerViewAdapter<VH, DATA>(context: Context?)
: RecyclerView.Adapter<VH>(), Releasable
where VH : BaseRecyclerViewAdapter.BaseViewHolder<DATA> {

    var context: Context? = null
        private set


    init {
        this.context = context
    }

    abstract fun getItemAtPosition(position: Int): DATA?

    override fun onBindViewHolder(holder: VH, position: Int) {
        val data = getItemAtPosition(position)
        if (data != null) {
            holder.bindData(data, position)
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int, payloads: List<Any>?) {
        val data = getItemAtPosition(position)
        if (data != null) {
            holder.bindData(data, position, payloads)
        }
    }

    @CallSuper
    override fun release() {
        context = null
    }

    abstract class BaseViewHolder<in DATA>(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun bindData(data: DATA, position: Int)

        open fun bindData(data: DATA, position: Int, payloads: List<Any>?) {
            bindData(data, position)
        }
    }

    companion object {
        @JvmStatic val POSITION_TAG = R.id.position_tag
    }
}
