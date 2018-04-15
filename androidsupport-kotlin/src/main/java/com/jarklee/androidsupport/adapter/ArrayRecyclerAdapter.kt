/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. Dotohsoft.com. All right reserved
 *  Author TrinhQuan. Create on 2016/12/24
 * ******************************************************************************
 */

package com.jarklee.androidsupport.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.jarklee.androidsupport.exception.RangeException
import java.util.*

abstract class ArrayRecyclerAdapter<VH, DATA>(context: Context?)
    : BaseRecyclerViewAdapter<VH, DATA>(context), ItemTouchHelperAdapter
        where VH : BaseRecyclerViewAdapter.BaseViewHolder<DATA> {

    val mOriginal = ArrayList<DATA>()
    var mFilteredData: MutableList<DATA> = mOriginal

    private fun internalAddData(data: DATA) {
        val count = itemCount
        mOriginal.add(data)
        val filter = this.filter
        if (filter != null) {
            if (filter.itemValid(data)) {
                mFilteredData.add(data)
                notifyItemInserted(count)
            }
        } else {
            notifyItemInserted(count)
        }
    }

    private fun internalAddData(data: Collection<DATA>) {
        val original = itemCount
        var insert = data.size
        mOriginal.addAll(data)
        val filter = this.filter
        if (filter != null) {
            val filtered = data.filter(filter::itemValid)
            insert = filtered.size
            mFilteredData.addAll(filtered)
            notifyItemRangeInserted(original, insert)
        } else {
            notifyItemRangeInserted(original, insert)
        }
    }

    private fun internalClear() {
        val count = itemCount
        mOriginal.clear()
        mFilteredData.clear()
        notifyItemRangeRemoved(0, count)
    }

    open fun addData(data: DATA) {
        internalAddData(data)
    }

    open fun addData(data: Collection<DATA>) {
        internalAddData(data)
    }

    open fun setData(data: DATA) {
        clear()
        internalAddData(data)
    }

    open fun setData(datas: Collection<DATA>) {
        clear()
        internalAddData(datas)
    }

    open fun clear() {
        internalClear()
    }

    open fun remove(position: Int) {
        mFilteredData.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun release() {
        super.release()
        mOriginal.clear()
        mFilteredData.clear()
    }

    override fun getItemCount(): Int {
        return mFilteredData.size
    }

    override fun getItemAtPosition(position: Int): DATA? {
        if (position < 0 || position >= itemCount) {
            throw RangeException()
        }
        return mFilteredData[position]
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(mFilteredData, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(mFilteredData, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        remove(position)
    }

    private var filter: Filter<DATA>? = null
    fun setFilter(filter: Filter<DATA>) {
        this.filter = filter
        performFullFilter()
    }

    fun performFullFilter() {
        val filter = this.filter
        if (filter == null) {
            this.mFilteredData = mOriginal
        } else {
            this.mFilteredData = mOriginal.filter(filter::itemValid).toMutableList()
        }
        notifyDataSetChanged()
    }
}

interface Filter<in DATA> {
    fun itemValid(data: DATA): Boolean
}

interface ItemTouchHelperAdapter {

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean

    fun onItemDismiss(position: Int)
}

data class TouchHelperConfigs(val canDrag: Boolean, val canSwipe: Boolean)

class RecyclerViewTouchCallback(private val adapter: ItemTouchHelperAdapter,
                                private val touchConfig: TouchHelperConfigs = TouchHelperConfigs(true, true))
    : ItemTouchHelper.Callback() {


    override fun isLongPressDragEnabled(): Boolean {
        return touchConfig.canDrag
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return touchConfig.canSwipe
    }

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        return adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemDismiss(viewHolder.adapterPosition)
    }
}
