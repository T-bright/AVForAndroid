package com.tbright.avforandroid.function.audio

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import com.tbright.avforandroid.base.BaseAudioRecordDialog
import com.tbright.avforandroid.utils.MediaRecorderUtils

class MediaRecordDialog : BaseAudioRecordDialog() {
    private var mediaRecorderUtils = MediaRecorderUtils()
    private var recordPath = ""
    override fun start() {
        mediaRecorderUtils.start(recordPath)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun pause() {
        mediaRecorderUtils.pause()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun resume() {
        mediaRecorderUtils.resume()

    }

    override fun stop() {
        mediaRecorderUtils.stop()
    }

    override fun play() {
        mediaRecorderUtils.playRecord(recordPath){
            if(it){
                Toast.makeText(this.context,"播放完成",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this.context,"播放失败",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun release() {
        mediaRecorderUtils.release()
    }

    override fun getVolume() {
        mediaRecorderUtils.getVolume {
            setVolume(it)
        }
    }

    fun show(manager: FragmentManager, tag: String?, recordPath: String) {
        this.recordPath = recordPath
        super.show(manager, tag)
    }
}