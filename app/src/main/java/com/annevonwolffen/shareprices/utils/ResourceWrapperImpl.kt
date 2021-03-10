package com.annevonwolffen.shareprices.utils

import android.content.res.Resources

/**
 * @author Terekhova Anna
 */
class ResourceWrapperImpl(private val resources: Resources) : ResourceWrapper {
    override fun openRawResource(resId: Int): String {
        return resources.openRawResource(resId)
            .bufferedReader().use { it.readText() }
    }
}