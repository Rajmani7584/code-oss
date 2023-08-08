package com.editor.codeoss.utils

class FileType {
    val exts = arrayOf("html", "json", "xml", "java", "kt", "htm", "css", "sass", "scss", "svg", "gradle", "properties", "pro")
    fun getSupportedExt(ext: String): Boolean {
        return exts.contains(ext)
    }
}