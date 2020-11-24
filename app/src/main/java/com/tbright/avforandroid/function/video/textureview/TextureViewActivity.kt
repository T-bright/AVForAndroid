package com.tbright.avforandroid.function.video.textureview

import android.content.Context
import android.content.Intent
import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.TextureView
import com.tbright.avforandroid.R
import kotlinx.android.synthetic.main.activity_texture_view.*
import kotlinx.coroutines.MainScope

class TextureViewActivity : AppCompatActivity(), TextureView.SurfaceTextureListener {
    companion object{
        fun start(context : Context){
            context.startActivity(Intent(context, TextureViewActivity::class.java))
        }
    }

    private var url = "http://file.llk12.com/upload/file/887/5/e/afaceb402421000/57d675a0121_1280xx.mp4"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_texture_view)
        ttv.surfaceTextureListener = this
        ttv.setOnClickListener {
            if(mMediaPlayer?.isPlaying == true){
                mMediaPlayer?.pause()
            }else{
                mMediaPlayer?.start()
            }
        }
    }

    private var surface: Surface? = null
    private var mMediaPlayer: MediaPlayer? = null
    private var mainScope = MainScope()

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        Log.e("AAA","Changed width : ${width} === height :${height}")
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {

    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        return true
    }

    override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture?, width: Int, height: Int) {
        surface = Surface(surfaceTexture)
        surface?.release()
        mMediaPlayer = MediaPlayer()
        mMediaPlayer?.let {
            it.setDataSource(url)
            it.setSurface(surface)
            it.setAudioStreamType(AudioManager.STREAM_MUSIC)
            it.setOnPreparedListener {
                mMediaPlayer!!.start()
                Log.e("AAA","Available width : ${mMediaPlayer!!.videoWidth} === height :${mMediaPlayer!!.videoHeight}")
            }
            it.prepareAsync()
        }
    }


}