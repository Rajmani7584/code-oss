package com.editor.codeoss.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat

class FileNameView(ctx: Context, attrs: AttributeSet): LinearLayoutCompat(ctx, attrs) {
    private var opened: Boolean = false

    fun isOpened(): Boolean {
        return this.opened
    }
    fun setOpened(open: Boolean) {
        this.opened = open
    }
}