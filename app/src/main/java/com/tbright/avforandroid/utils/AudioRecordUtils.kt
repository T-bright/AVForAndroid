package com.tbright.avforandroid.utils

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.view.TextureView
import android.widget.Toast
import android.widget.VideoView
import com.blankj.utilcode.util.FileUtils
import com.tbright.avforandroid.BaseApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


class AudioRecordUtils {

    //指定音频源。默认的是 MediaRecorder.AudioSource.MIC指的是麦克风
    private val audioSource = MediaRecorder.AudioSource.MIC

    //采样率。44100这个数目前所有的android都支持
    private val sampleRateInHz = 44100

    //声道数。AudioFormat.CHANNEL_IN_MONO是单声道；AudioFormat.CHANNEL_IN_STEREO是左右双声道
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO

    //音频量化位数。16位或者8位
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT

    //指定缓冲区大小。调用AudioRecord类的getMinBufferSize方法获得。注意不能自己随便定义
    private var mBufferSizeInBytes = 0

    private var mainScope = MainScope()

    //录制状态。默认是未录制
    private var recordState = RecordState.STOP_RECORD
    private var mAudioRecord: AudioRecord? = null

    /**
     * 初始化 AudioRecord
     * @param audioSource：指定音频源。默认的是 MediaRecorder.AudioSource.MIC指的是麦克风
     * @param sampleRateInHz：采样率。44100这个数目前所有的android都支持。
     * @param channelConfig：声道数。AudioFormat.CHANNEL_IN_MONO是单声道；AudioFormat.CHANNEL_IN_STEREO是左右双声道。详情请看 [AudioFormat.CHANNEL_IN_DEFAULT]
     * @param audioFormat：音频量化位数。16位或者8位
     */
    private fun initAudioRecord(audioSource: Int, sampleRateInHz: Int, channelConfig: Int, audioFormat: Int) {
        mBufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat)
        mAudioRecord = AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, mBufferSizeInBytes)
    }

    fun start(recordPath: String) {
        if (recordState == RecordState.START_RECORD) return
        mainScope.launch(Dispatchers.IO) {
            try {
                recordState = RecordState.START_RECORD
                if (mAudioRecord == null) {
                    initAudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat)
                }
                mAudioRecord?.startRecording()
                FileUtils.createFileByDeleteOldFile(recordPath)
                val buffer = ByteArray(mBufferSizeInBytes)
                var fs = FileOutputStream(File(recordPath))
                while (mAudioRecord?.recordingState == AudioRecord.RECORDSTATE_RECORDING || recordState == RecordState.START_RECORD) {
                    var bufferRead = mAudioRecord?.read(buffer, 0, mBufferSizeInBytes) ?: 0
                    if (AudioRecord.ERROR_INVALID_OPERATION != bufferRead) {
                        fs.write(buffer, 0, bufferRead)
                    }
                }
                recordState = RecordState.STOP_RECORD
                fs.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }finally {
                recordState = RecordState.STOP_RECORD

                withContext(Dispatchers.Main){
                    Toast.makeText(BaseApplication.instance,"录音结束", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun stop(){
        recordState = RecordState.STOP_RECORD
        mAudioRecord?.stop()
    }

    fun release(){
        recordState = RecordState.STOP_RECORD
        mAudioRecord?.release()
        mAudioRecord = null
    }
}