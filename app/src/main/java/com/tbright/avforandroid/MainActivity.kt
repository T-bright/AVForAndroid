package com.tbright.avforandroid

import android.Manifest
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.tbright.avforandroid.audio.MediaRecordDialog
import com.tbright.avforandroid.audio.MediaRecorderUtils
import com.tbright.avforandroid.utils.permission.checkPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var recordPath = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO){

        }
        recordPath =  "${cacheDir}/aa.amr"
        var mediaRecorderUtils = MediaRecorderUtils()
        btStartRecord.setOnClickListener {
            MediaRecordDialog().show(supportFragmentManager,"tag",recordPath)
        }
        btStopRecord.setOnClickListener {

        }
        btPlayRecord.setOnClickListener {
            mediaRecorderUtils.playRecord(recordPath){isSuccess->
                if(isSuccess){
                    Toast.makeText(this, "播放完成", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "播放失败", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }




}