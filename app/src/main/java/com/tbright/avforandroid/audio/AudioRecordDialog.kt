package com.tbright.avforandroid.audio

import androidx.fragment.app.FragmentManager
import com.tbright.avforandroid.dialog.BaseAudioRecordDialog
import com.tbright.avforandroid.utils.AudioRecordUtils

class AudioRecordDialog : BaseAudioRecordDialog(){
    private var recordPath = ""
    private var audioRecordUtils = AudioRecordUtils()
    override fun start() {
        audioRecordUtils.start(recordPath)
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun stop() {
        audioRecordUtils.stop()
    }

    override fun play() {

    }

    override fun release() {
        audioRecordUtils.release()
    }

    override fun getVolume() {

    }

    fun show(manager: FragmentManager, tag: String?, recordPath: String) {
        this.recordPath = recordPath
        super.show(manager, tag)
    }
}