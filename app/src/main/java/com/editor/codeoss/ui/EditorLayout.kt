package com.editor.codeoss.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup.LayoutParams
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.caverock.androidsvg.SVGImageView
import com.editor.codeoss.R
import com.editor.codeoss.utils.AppSetting
import com.editor.codeoss.utils.EditorJavascriptInterface
import com.editor.codeoss.utils.FileManager
import com.editor.codeoss.utils.FileType
import com.editor.codeoss.widget.FileNameView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import java.io.File

class EditorLayout : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var editorWebView: WebView
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor_layout)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val nav = findViewById<NavigationView>(R.id.navigation_view)

        editorWebView = findViewById(R.id.mainEditor)


        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close)

        drawerLayout.addDrawerListener(drawerToggle)

        drawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initEditor()

        initiateFileManager()

    }

    private fun initiateFileManager() {
        val layoutCompat = findViewById<FileNameView>(R.id.file_tree)
       listDirs(AppSetting(applicationContext).getOpenedFolders(), layoutCompat)
    }

    private fun listDirs(files: ArrayList<File>?, layoutCompat: FileNameView) {
        if (layoutCompat.isOpened()){
            layoutCompat.removeAllViews()
            layoutCompat.setOpened(false)
            return
        }

        files?.forEach{ file ->
            val view = layoutInflater.inflate(R.layout.file_button_layout, null, false)

            val textView = view.findViewById<TextView>(R.id.file_name_text)

            val layoutCompat2 = view.findViewById<FileNameView>(R.id.file_list)

            val fileIcon = view.findViewById<SVGImageView>(R.id.file_icon)

            if (file.isFile)
                fileIcon.setImageAsset("icon/" + file.extension.lowercase() + ".svg")
            else
                fileIcon.setImageAsset("icon/folder.svg")

            textView.text = file.name

            layoutCompat.addView(view)

            layoutCompat.setOpened(true)
            textView.setOnClickListener { _ ->
                if (file.isDirectory) {
                    runOnUiThread {
                        val allFiles = FileManager(applicationContext).getFileList(file)
                        listDirs(allFiles, layoutCompat2)
                    }

                }
                else if (file.isFile) {
                    if (FileType().getSupportedExt(file.extension))
                        openFileInEditor(file)
                    else
                        Toast.makeText(this, "File type not supported", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openFileInEditor(file: File) {
        if (drawerLayout.isOpen)
            drawerLayout.close()
        val dialog = MaterialAlertDialogBuilder(this)
        dialog.setTitle("Loading File...")
        val progress = ProgressBar(this)
        progress.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        dialog.setView(progress)
        val build = dialog.create()
        runOnUiThread {
            build.show()
            editorWebView.loadUrl("javascript:setCode('" + file.absolutePath + "');")
            build.cancel()
        }
    }


    private fun initEditor() {
        val setting = editorWebView.settings

        setting.javaScriptEnabled = true
        setting.domStorageEnabled = true
        setting.userAgentString = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36"

        editorWebView.addJavascriptInterface(EditorJavascriptInterface(applicationContext), "Android")

        editorWebView.webViewClient = WebViewClient()
        editorWebView.webChromeClient = WebChromeClient()


        editorWebView.loadUrl("file:///android_asset/ace/index.html")


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.three_dot_options, menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.go_to_setting)
            startActivity(Intent(this, SettingActivity::class.java))
        else if (drawerToggle.onOptionsItemSelected(item))
            return true

        return super.onOptionsItemSelected(item)

    }
}
