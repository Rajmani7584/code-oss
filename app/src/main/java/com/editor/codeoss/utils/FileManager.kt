package com.editor.codeoss.utils

import android.content.Context
import android.widget.Toast
import java.io.File
import java.io.FileReader

class FileManager(private var ctx: Context) {
    fun readFile(file: File): String {
        if (file.isFile) {
            val code = FileReader(file)
            val sb = StringBuilder()

            code.use { r ->
                r.forEachLine { l -> sb.append(l).append("\n") }
            }
            return sb.toString()
        }
        return ""
    }

    fun getFileList(file: File): ArrayList<File> {
        val files: ArrayList<File> = ArrayList()
        if (file.isDirectory && file.canRead())
        {
            file.listFiles()?.forEach { r ->
                files.add(r)
            }
        } else
            Toast.makeText(ctx, "Can't access the folder", Toast.LENGTH_SHORT).show()
        return files
    }
}