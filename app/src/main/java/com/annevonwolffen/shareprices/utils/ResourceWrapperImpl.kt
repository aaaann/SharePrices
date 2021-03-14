package com.annevonwolffen.shareprices.utils

import android.content.Context
import androidx.annotation.StringRes

/**
 * @author Terekhova Anna
 */
class ResourceWrapperImpl(private val context: Context) : ResourceWrapper {
    override fun openRawResource(resId: Int): String {
        return context.resources.openRawResource(resId)
            .bufferedReader().use { it.readText() }
    }

    override fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String? {
        return context.resources.getString(resId, *formatArgs)
    }
}