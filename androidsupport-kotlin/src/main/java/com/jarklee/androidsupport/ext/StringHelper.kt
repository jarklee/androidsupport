/*
 * ******************************************************************************
 *  Copyright Ⓒ 2016. TrinhQuan. All right reserved
 *  Author: TrinhQuan. Created on 2016/12/20
 *  Contact: trinhquan.171093@gmail.com
 * ******************************************************************************
 */

package com.jarklee.androidsupport.ext

object StringHelper {

    fun join(joiner: String,
             firstObject: Any,
             secondObject: Any,
             vararg objects: Any): String {
        return Joiner.on(joiner).skipNull().join(firstObject, secondObject, *objects)
    }

    fun equalIgnoreCase(lhs: String?, rhs: String?): Boolean {
        return lhs == null && rhs == null || lhs != null && rhs != null && lhs.equals(rhs, ignoreCase = true)
    }

    fun isEmpty(s: String?): Boolean {
        return s == null || s.length == 0
    }

    private class Joiner internal constructor(private val joiner: Any) {
        private var isSkipNull: Boolean = false

        init {
            this.isSkipNull = false
        }

        internal fun skipNull(): Joiner {
            isSkipNull = true
            return this
        }

        internal fun join(firstObject: Any, secondObject: Any, vararg objects: Any): String {
            val includeNull = !isSkipNull
            val builder = StringBuilder("")
            val joiner = this.joiner.toString()
            appendText(builder, joiner, firstObject, includeNull)
            appendText(builder, joiner, secondObject, includeNull)
            if (objects.size != 0) {
                for (`object` in objects) {
                    appendText(builder, joiner, `object`, includeNull)
                }
            }
            if (builder.length > 0) {
                builder.delete(builder.length - joiner.length, builder.length)
            }
            return builder.toString()
        }

        private fun appendText(builder: StringBuilder,
                               joiner: String,
                               `object`: Any?,
                               includeNull: Boolean) {
            if (`object` == null && !includeNull) {
                return
            }
            if (`object` == null) {
                builder.append("null").append(joiner)
            } else {
                builder.append(`object`).append(joiner)
            }
        }

        companion object {
            @JvmStatic fun on(joiner: Any): Joiner {
                return Joiner(joiner)
            }
        }
    }
}
