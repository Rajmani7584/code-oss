package com.editor.codeoss

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.editor.codeoss.ui.EditorLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkForPermission(this)) {
            initApp()
            finish()
        } else
            showPermissionDialog(this, 0);
    }

    private fun initApp() {
        val intent = Intent(this, EditorLayout::class.java)
        startActivity(intent)
    }


    private fun checkForPermission(activity: MainActivity): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            Environment.isExternalStorageManager()
         else
            !(ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
    }


    private fun showPermissionDialog(activity: MainActivity, code: Int) {
        val builder = MaterialAlertDialogBuilder(activity)
        builder.setTitle("Storage Permission Required")
        builder.setMessage("Allow Storage Permission to use this app.\nThis app will not work without permission")
        builder.setPositiveButton("Allow") { _: DialogInterface, _: Int ->
            if (code == 0)
                requestForPermission(activity)
            else {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                startActivity(intent.setData(Uri.fromParts("package", packageName, null)))
            }
        }
        builder.setNegativeButton("No Thanks") { _: DialogInterface, _: Int ->
            finishAffinity()
        }
        builder.create().show()
    }



    private fun requestForPermission(activity: MainActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            val data = Uri.fromParts("package", packageName, null);
            intent.data = data;
            startActivityForResult(intent, 0);
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var deny = 0
        for (i in grantResults.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                deny++
                if (Manifest.permission.WRITE_EXTERNAL_STORAGE == permissions[i]) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissions[i])) {
                            showPermissionDialog(this, 0)
                        } else {
                            showPermissionDialog(this, 1)
                        }
                    }
                }
            }
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (checkForPermission(this)) {
            initApp()
            finish()
        } else
            showPermissionDialog(this, 0)
    }
}