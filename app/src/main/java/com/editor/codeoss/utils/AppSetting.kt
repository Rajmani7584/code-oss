package com.editor.codeoss.utils

import android.content.Context
import android.content.SharedPreferences
import java.io.File

class AppSetting(ctx: Context) {
    private var preferences: SharedPreferences = ctx.getSharedPreferences("setting", Context.MODE_PRIVATE)
    fun resetSettings(): Boolean {
        val editor = preferences.edit()
        editor.putBoolean("enableLiveAutocompletion", true)
        editor.putBoolean("wrap", false)
        editor.putBoolean("useSoftTabs", true)
        editor.putBoolean("enableAutoIndent", true)
        editor.putBoolean("showPrintMargin", true)
        editor.putStringSet("openedFolder", setOf("/storage/emulated/0"))
        editor.apply()
        return true
    }

    fun setOpenedFolders(file: File): Boolean {
        val openedFolder = preferences.getStringSet("openedFolder", setOf("/storage/emulated/0"))
        val success = openedFolder!!.add(file.absolutePath)
        val editor = preferences.edit()
        editor.putStringSet("openedFolder", openedFolder)
        editor.apply()
        return success
    }

    fun getOpenedFolders(): ArrayList<File> {
        val files: ArrayList<File> = ArrayList()
        preferences.getStringSet("openedFolder", setOf("/storage/emulated/0"))?.forEach { r ->
            files.add(File(r))
        }
        return files
    }
}