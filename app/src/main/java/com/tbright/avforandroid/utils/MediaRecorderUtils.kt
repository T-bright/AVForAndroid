package com.tbright.avforandroid.utils

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.tbright.avforandroid.BaseApplication
import kotlinx.coroutines.*
import kotlin.math.log10


class MediaRecorderUtils {

//    companion object {
//        const val STOP_RECORD = 0//没有录制
//        const val START_RECORD = 1 //开始录制
//        const val PAUSE_RECORD = 2 //暂停录制
//    }

    //录制状态。默认是未录制
    private var recordState = RecordState.STOP_RECORD
    private var mediaRecorder: MediaRecorder? = null

    //开始录制
    fun start(path: String) {
        if (mediaRecorder == null) {
            mediaRecorder = MediaRecorder()
        }
        if (recordState == RecordState.START_RECORD) {
            return
        }

        if (recordState == RecordState.STOP_RECORD) {
            mediaRecorder?.reset()
        }

        if (recordState == RecordState.PAUSE_RECORD) {
            mediaRecorder?.reset()
        }

        try {
            mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC) //设置录音源，默认是麦克风
            mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)//所录制的音频文件的格式
            mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)//所录制的音频文件的编码
            mediaRecorder?.setAudioChannels(1)//设置音道数

            //采样率包含这几种 ：96000;88200;64000; 48000;44100;32000;24000; 22050;16000;12000;11025;8000;
            //所有android设备都支持的只有44100Hz
            mediaRecorder?.setAudioSamplingRate(44100)//设置采样率
            mediaRecorder?.setAudioEncodingBitRate(12800)//设置比特率
            mediaRecorder?.setOutputFile(path)
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            recordState = RecordState.START_RECORD
        } catch (e: Exception) {
            e.printStackTrace()
            release()
        }
    }


    //暂停录制
    @RequiresApi(Build.VERSION_CODES.N)
    fun pause() {
        try {
            if (recordState == RecordState.START_RECORD) {
                recordState = RecordState.PAUSE_RECORD
                mediaRecorder?.pause()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            release()
        }
    }

    //重新开始录制
    @RequiresApi(Build.VERSION_CODES.N)
    fun resume() {
        try {
            if (recordState == RecordState.PAUSE_RECORD) {
                recordState = RecordState.START_RECORD
                mediaRecorder?.resume()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            release()
        }
    }

    //停止录制
    fun stop() {
        try {
            if (recordState == RecordState.PAUSE_RECORD) {
                recordState = RecordState.STOP_RECORD
                mediaRecorder?.stop()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            release()
        }
    }

    //释放资源
    fun release() {
        try {
            if (recordState != RecordState.STOP_RECORD) {
                mediaRecorder?.stop()
            }
            mediaRecorder?.release()
            mediaRecorder = null
            recordState = RecordState.STOP_RECORD
            mainScope.cancel()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            e.printStackTrace()
            release()
        }
    }

    private var mainScope = MainScope()

    fun getVolume(volume: (volume: Int) -> Unit) {
        mainScope.launch {
            var db = withContext(Dispatchers.IO) {
//                val ratio = mediaRecorder.maxAmplitude * 1.0 / BASE * 1.0

                var maxAmplitude =  mediaRecorder?.maxAmplitude?.toDouble()?:0.0
                val ratio = maxAmplitude
                var ss = Math.log10(maxAmplitude / 1.toDouble())

                var db = 0
                if (ratio > 1) db = (20 * log10(ratio)).toInt()
                Log.e("AAA","maxAmplitude :${maxAmplitude} --- ratio :${ratio} --- db :${db}")
                return@withContext db
            }
            volume.invoke(db)
        }
    }

    private var mediaPlayer: MediaPlayer? = null
    fun playRecord(path: String, result: (isSuccess: Boolean) -> Unit) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
            }

            if (mediaPlayer?.isPlaying == true) {
                return
            }
            if (recordState == RecordState.START_RECORD) {
                Toast.makeText(BaseApplication.instance,"不能边放边播",Toast.LENGTH_LONG).show()
                return
            }
            mediaPlayer?.setDataSource(path)
            mediaPlayer?.prepare()
            mediaPlayer?.start()
            mediaPlayer?.setOnCompletionListener {
                mediaPlayer?.reset()
                result.invoke(true)
            }

            mediaPlayer?.setOnErrorListener { mediaPlayer, i, i2 ->
                mediaPlayer.reset()
                result.invoke(false)
                return@setOnErrorListener false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}