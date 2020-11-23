package com.tbright.avforandroid

import android.Manifest
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.tbright.avforandroid.audio.AudioRecordDialog
import com.tbright.avforandroid.audio.MediaRecordDialog
import com.tbright.avforandroid.utils.AudioTrackUtils
import com.tbright.avforandroid.utils.MediaRecorderUtils
import com.tbright.avforandroid.utils.permission.checkPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var recordPath = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO){

        }
        var mediaRecorderUtils = MediaRecorderUtils()
        mediaRecorder.setOnClickListener {
            recordPath =  "${cacheDir}/aa.amr"
            MediaRecordDialog().show(supportFragmentManager,"mediaRecorder",recordPath)
        }

        audioRecord.setOnClickListener {
            recordPath =  "${cacheDir}/aa.pcm"
            AudioRecordDialog().show(supportFragmentManager,"audioRecord",recordPath)
        }

        btPlayRecord.setOnClickListener {
            recordPath = "${cacheDir}/test.pcm"
            if(recordPath.endsWith("amr")){
                mediaRecorderUtils.playRecord(recordPath){isSuccess->
                    if(isSuccess){
                        Toast.makeText(this, "播放完成", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "播放失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            if(recordPath.endsWith("pcm")){
                var au = AudioTrackUtils()
                au.play(recordPath)
            }
        }

    }




}