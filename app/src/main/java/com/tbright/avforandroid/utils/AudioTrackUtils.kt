package com.tbright.avforandroid.utils

import android.media.*

class AudioTrackUtils {
    //指定缓冲区大小。调用AudioRecord类的getMinBufferSize方法获得。注意不能自己随便定义
    private var mBufferSizeInBytes = 0
    private var audioTrack: AudioTrack? = null
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

    fun play(){
        if(audioTrack == null){
            initAudioTrack()
        }
        audioTrack?.play()
    }
}