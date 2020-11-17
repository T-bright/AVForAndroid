package com.tbright.avforandroid.dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tbright.avforandroid.R
import kotlinx.android.synthetic.main.dialog_audio_record.*

abstract class BaseAudioRecordDialog : BottomSheetDialogFragment() {

    //计数器
    private var counter = 0 //逐次递增100ms，毫秒为单位   2200毫秒显示：00:02:20
    var handler: Handler = Handler(Looper.getMainLooper())
    private var runnable = object : Runnable {
        override fun run() {
            var mm = "${(counter % (60 * 60 * 1000)) / (60 * 1000)}"
            var ss = "${(counter % (60 * 1000)) / 1000}"
            var ms = "${(counter % 1000) / 10}"
            if (mm.length == 1) mm = "0${mm}"
            if (ss.length == 1) ss = "0${ss}"
            if (ms.length == 1) ms = "0${ms}"
            time?.text = "${mm}:${ss}:${ms}"
            counter += 100
            handler.postDelayed(this, 100) //逐次递增100ms
            getVolume()
        }
    }
    private var recordState = 0 //0.未录音  1.正在录音  2.暂停录音

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_audio_record, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        remake.setOnClickListener {//重新开始录
            counter = 0
            handler.removeCallbacks(runnable)
            release() //释放之前的录的，重新开始录制
            startRecord()
        }
        done.setOnClickListener {//完成
            stopRecord()
            release()
        }
        record.setOnClickListener {
            if(recordState == 0){//未录音
                startRecord()
            }else if(recordState == 1){//正在录音
                pauseRecord()
            }else{
                resumeRecord()
            }
        }
        play.setOnClickListener {
            play()
        }
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
        release()
    }

    override fun onResume() {
        super.onResume()
        startRecord()
    }

    //设置当前的声音大小
    fun setVolume(db: Int) {
        voiceLine?.setVolume(db)
    }

    //开始录音
    private fun startRecord() {
        recordState = 1
        record.setBackgroundResource(R.mipmap.stop)
        handler.postDelayed(runnable, 0)
        start()
    }

    //暂停录音
    //TODO 暂停录音，音浪也View也暂停
    private fun pauseRecord() {
        recordState = 2
        record.setBackgroundResource(R.mipmap.start)
        handler.removeCallbacks(runnable)
        pause()
    }

    //停止录音
    private fun stopRecord() {
        recordState = 0
        counter = 0
        record.setBackgroundResource(R.mipmap.start)
        handler.removeCallbacks(runnable)
        stop()
    }

    //重录音
    private fun resumeRecord() {
        recordState = 1
        counter = 0
        record.setBackgroundResource(R.mipmap.stop)
        handler.postDelayed(runnable, 0)
        resume()
    }


    abstract fun start()

    abstract fun pause()

    abstract fun resume()

    abstract fun stop()

    abstract fun play()

    abstract fun release()

    abstract fun getVolume()
}