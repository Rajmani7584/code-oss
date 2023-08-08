package com.editor.codeoss.ui

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.LinearLayoutCompat
import com.editor.codeoss.R

class SettingActivity : AppCompatActivity() {

    private lateinit var themeSetting: LinearLayoutCompat
    private lateinit var editorFont: LinearLayoutCompat
    private lateinit var autocomplete: AppCompatCheckBox
    private lateinit var wordWrap: AppCompatCheckBox
    private lateinit var useSoftTab: AppCompatCheckBox
    private lateinit var autoIndent: AppCompatCheckBox
    private lateinit var showSpace: AppCompatCheckBox
    private lateinit var preferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        themeSetting = findViewById(R.id.theme_setting)
        editorFont = findViewById(R.id.font_size_setting)
        autocomplete = findViewById(R.id.autocomplete)
        wordWrap = findViewById(R.id.word_wrap)
        useSoftTab = findViewById(R.id.use_soft_tab)
        autoIndent = findViewById(R.id.auto_indent)
        showSpace = findViewById(R.id.show_space)
        preferences = getSharedPreferences("setting", MODE_PRIVATE)


        autocomplete.isChecked = preferences.getBoolean("enableLiveAutocompletion", true)
        wordWrap.isChecked = preferences.getBoolean("wrap", false)
        useSoftTab.isChecked = preferences.getBoolean("useSoftTabs", true)
        autoIndent.isChecked = preferences.getBoolean("enableAutoIndent", true)
        showSpace.isChecked = preferences.getBoolean("showPrintMargin", true)


        themeSetting.setOnClickListener { optionChooseDialog() }
        editorFont.setOnClickListener { optionChooseDialog() }

        autocomplete.setOnCheckedChangeListener { button: CompoundButton, _ -> changeSettingListener(button, 0) }
        wordWrap.setOnCheckedChangeListener { button: CompoundButton, _ -> changeSettingListener(button, 1) }
        useSoftTab.setOnCheckedChangeListener { button: CompoundButton, _ -> changeSettingListener(button, 2) }
        autoIndent.setOnCheckedChangeListener { button: CompoundButton, _ -> changeSettingListener(button, 3) }
        showSpace.setOnCheckedChangeListener { button: CompoundButton, _ -> changeSettingListener(button, 4) }

    }

    private fun changeSettingListener(v: CompoundButton, i: Int) {
        val editor = preferences.edit()
        when (i) {
            0 -> editor.putBoolean("enableLiveAutocompletion", v.isChecked)
            1 -> editor.putBoolean("wrap", v.isChecked)
            2 -> editor.putBoolean("useSoftTabs", v.isChecked)
            3 -> editor.putBoolean("enableAutoIndent", v.isChecked)
            4 -> editor.putBoolean("showPrintMargin", v.isChecked)
        }
        editor.apply()

    }

    private fun optionChooseDialog() {

    }
}