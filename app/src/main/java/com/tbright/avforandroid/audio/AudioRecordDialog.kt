package com.tbright.avforandroid.audio

import androidx.fragment.app.FragmentManager
import com.tbright.avforandroid.dialog.BaseAudioRecordDialog
import com.tbright.avforandroid.utils.AudioRecordUtils
import com.tbright.avforandroid.utils.AudioTrackUtils

class AudioRecordDialog : BaseAudioRecordDialog(){
    private var recordPath = ""
    private var audioRecordUtils = AudioRecordUtils()
    private var audioTrackUtils = AudioTrackUtils()
    override fun start() {
        audioRecordUtils.start(recordPath)
    }

    override fun pause() {
        audioRecordUtils.stop()
    }

    override fun resume() {

    }

    override fun stop() {
        audioRecordUtils.stop()
    }

    override fun play() {
        audioTrackUtils.play(recordPath)
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