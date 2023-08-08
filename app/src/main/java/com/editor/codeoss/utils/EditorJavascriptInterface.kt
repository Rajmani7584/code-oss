package com.editor.codeoss.utils

import android.content.Context
import android.webkit.JavascriptInterface
import java.io.File

class EditorJavascriptInterface(private var ctx: Context) {


    @JavascriptInterface
    fun getCode(path: String): String {
        return FileManager(ctx).readFile(File(path))
    }
}