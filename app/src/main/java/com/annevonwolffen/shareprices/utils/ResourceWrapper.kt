package com.annevonwolffen.shareprices.utils

import androidx.annotation.RawRes

/**
 * @author Terekhova Anna
 */
interface ResourceWrapper {
    fun openRawResource(@RawRes resId: Int): String
}