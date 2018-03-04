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
import com.jarklee.androidsupport.adapter.BaseRecyclerViewAdapter
import com.jarklee.androidsupport.exception.RangeException
import java.util.*

abstract class ArrayRecyclerAdapter<VH, DATA>(context: Context?)
    : BaseRecyclerViewAdapter<VH, DATA>(context), ItemTouchHelperAdapter
where VH : BaseRecyclerViewAdapter.BaseViewHolder<DATA> {

    private val mData = ArrayList<DATA>()

    open fun addData(data: DATA) {
        val count = itemCount
        mData.add(data)
        notifyItemInserted(count)
    }

    open fun addData(data: Collection<DATA>) {
        val original = itemCount
        mData.addAll(data)
        notifyItemRangeInserted(original, data.size)
    }

    open fun setData(data: DATA) {
        clear()
        addData(data)
    }

    open fun setData(datas: Collection<DATA>) {
        clear()
        addData(datas)
    }

    open fun clear() {
        val count = itemCount
        mData.clear()
        notifyItemRangeRemoved(0, count)
    }

    open fun getData(): MutableList<DATA> {
        val des = ArrayList<DATA>(mData.size)
        Collections.copy(des, mData)
        return des
    }

    open fun remove(position: Int) {
        dataByReference.removeAt(position)
        notifyItemRemoved(position)
    }

    val dataByReference: MutableList<DATA>
        get() = mData

    override fun release() {
        super.release()
        mData.clear()
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun getItemAtPosition(position: Int): DATA? {
        if (position < 0 || position >= itemCount) {
            throw RangeException()
        }
        return mData[position]
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition..toPosition - 1) {
                Collections.swap(mData, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(mData, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        remove(position)
    }
}

interface ItemTouchHelperAdapter {

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean

    fun onItemDismiss(position: Int)
}

data class TouchHelperConfigs(val canDrag: Boolean, val canSwipe: Boolean)

class RecyclerViewTouchCallback(val adapter: ItemTouchHelperAdapter,
                                val touchConfig: TouchHelperConfigs = TouchHelperConfigs(true, true))
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
