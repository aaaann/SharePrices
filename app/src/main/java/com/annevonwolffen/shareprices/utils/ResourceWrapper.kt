package com.annevonwolffen.shareprices.utils

import androidx.annotation.RawRes
import androidx.annotation.StringRes

/**
 * @author Terekhova Anna
 */
interface ResourceWrapper {
    fun openRawResource(@RawRes resId: Int): String

    fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String?
}