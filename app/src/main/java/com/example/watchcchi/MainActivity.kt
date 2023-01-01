package com.example.watchcchi

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.preference.PreferenceManager
import android.view.MotionEvent
import android.widget.ImageButton
import com.example.watchcchi.databinding.ActivityMainBinding
import java.time.LocalDateTime


// こけこっこー
// http://mimimi-sound.com/?s=%E3%81%B2%E3%82%88%E3%81%93

//ひよこ
// https://taira-komori.jpn.org/welcome.html

var  watchcchiInfo : WatchcchiInfo? = null
var  watchcchi : Watchcchi? = null

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    private var  tapSum = 0
    private lateinit var  hiyokoMediaPlayer: MediaPlayer
    private lateinit var  niwatoriMediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 変数設定など
        setup()

        // ボタンタップ時のリスナー
        val statusButton =  findViewById<ImageButton>(R.id.status_button)
        statusButton.setOnClickListener {
            // 新しい画面に遷移する処理
            val intent = Intent(applicationContext, StatusActivity::class.java)
            startActivity(intent)
        }

        val hiyokoImageButton = findViewById<ImageButton>(R.id.watchcchi_image)
        hiyokoImageButton.setOnClickListener {
            // ぴよ
            onClickHiyoko()
        }
    }

    private fun setup(){
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //　うぉっちっち状態管理クラス
        watchcchi = Watchcchi(this )
        // 世代や日数など
        watchcchiInfo = WatchcchiInfo(this)

        // 何度も定義するのはあれなのでここで定義
        hiyokoMediaPlayer = MediaPlayer.create(this, R.raw.piyo)
        niwatoriMediaPlayer = MediaPlayer.create(this, R.raw.kokekokko)
    }

    // 画面タッチ処理はoverride fun onTouchEventの中に書く
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // タップして離れたら
        if ( event?.action == android.view.MotionEvent.ACTION_UP) {
            when (watchcchi!!.getEvolveLevel()) {
                EvolveLevel.EGG -> {
                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(50)
                    tapSum++
                    if (tapSum == 10) {
                        watchcchi!!.eggToHiyoko()
                        tapSum = 0
                    }
                }
                EvolveLevel.HIYOKO_WITH_EGG -> {
                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(100)
                    watchcchi!!.nextEvolveLevel()
                    watchcchi!!.walking(R.drawable.hiyoko)
                }
                EvolveLevel.HIYOKO -> {
                    //hiyokoMediaPlayer.start()
                }
                EvolveLevel.HIYOKO -> {
                    //niwatoriMediaPlayer.start()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    // ひよこtappでPIYOっていう
    fun onClickHiyoko(){
        when (watchcchi!!.getEvolveLevel()) {
            EvolveLevel.EGG -> {
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(50)
                tapSum++
                if (tapSum == 10) {
                    watchcchi!!.eggToHiyoko()
                    tapSum = 0
                }
            }
            EvolveLevel.HIYOKO_WITH_EGG -> {
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(100)
                watchcchi!!.nextEvolveLevel()
                watchcchi!!.walking(R.drawable.hiyoko)
            }
            EvolveLevel.HIYOKO -> {
                hiyokoMediaPlayer.start()
            }
            EvolveLevel.HIYOKO -> {
                niwatoriMediaPlayer.start()
            }
        }
    }



    // アプリ終了時、うぉっちっちの状態を保存
    override fun onDestroy() {
        super.onDestroy()
        // 列挙型を保存する処理
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()
        editor.putInt ("evolveLevel", watchcchi!!.getEvolveLevel().id)
        editor.apply()
        println( "アプリ終了")
        println( pref.getInt("evolveLevel",0))

    }
}