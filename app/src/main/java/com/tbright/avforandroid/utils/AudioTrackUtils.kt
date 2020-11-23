package com.tbright.avforandroid.utils

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.widget.Toast
import com.tbright.avforandroid.BaseApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream

class AudioTrackUtils {
    //指定缓冲区大小。调用AudioRecord类的getMinBufferSize方法获得。注意不能自己随便定义
    private var mBufferSizeInBytes = 0
    private var audioTrack: AudioTrack? = null
    private var mainScope = MainScope()
    private var playState = PlayState.STOP_PLAY
    private fun initAudioTrack() {
        mBufferSizeInBytes = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT)
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        val audioFormat = AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(44100)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build()

        audioTrack = AudioTrack(
            audioAttributes,
            audioFormat,
            mBufferSizeInBytes,
            AudioTrack.MODE_STREAM,
            AudioManager.AUDIO_SESSION_ID_GENERATE
        )
    }

    fun play(filePath: String) {
        if (playState == PlayState.START_PLAY) {
            return
        }
        Toast.makeText(BaseApplication.instance,"开始播放", Toast.LENGTH_SHORT).show()
        playState = PlayState.START_PLAY
        mainScope.launch(Dispatchers.IO) {
            try {
                if (audioTrack == null) {
                    initAudioTrack()
                }
                audioTrack?.play()
                val buffer = ByteArray(mBufferSizeInBytes)
                var fs = FileInputStream(File(filePath))
                while (playState == PlayState.START_PLAY && fs.available() > 0) {
                    var len = fs.read(buffer)
                    if (len == AudioTrack.ERROR_INVALID_OPERATION ||len == AudioTrack.ERROR_BAD_VALUE) continue
                    if (len != 0 && len != -1) {
                        audioTrack?.write(buffer, 0, len)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }finally {
                playState = PlayState.STOP_PLAY
                withContext(Dispatchers.Main){
                    Toast.makeText(BaseApplication.instance,"播放结束", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}