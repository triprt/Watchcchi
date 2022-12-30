package com.example.watchcchi

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.view.MotionEvent
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.watchcchi.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var  watchcchi : Watchcchi
    private var  tapSum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // うぉっちっちのimageView取得
        val imageView = findViewById<ImageView>(R.id.watchcchi_image)

        // バイブ
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        //　うぉっちっち状態管理クラス
        watchcchi = Watchcchi(imageView,vibrator)


    }

    // 画面タッチ処理はoverride fun onTouchEventの中に書く
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // ここに処理を追記していく
        if (watchcchi.evolveLevel == EvolveLevel.EGG){

            when(event?.action) {
                android.view.MotionEvent.ACTION_UP -> {
                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(100)
                    tapSum ++
                    kotlin.io.println(tapSum)
                    if(tapSum == 10) {
                        watchcchi.eggToHiyoko()
                        tapSum = 0
                    }
                }
            }
        }


        return super.onTouchEvent(event)
    }
}