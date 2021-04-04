package com.annevonwolffen.shareprices.utils

fun <T> List<T>.safeSubList(fromIndex: Int, toIndex: Int): List<T> =
    this.subList(fromIndex.coerceAtLeast(0).coerceAtMost(this.size), toIndex.coerceAtMost(this.size))