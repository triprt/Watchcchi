package com.example.watchcchi

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.*

class GameActivity: Activity() {

    private var gameManager:GameManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gameManager = GameManager(this)

        // 終了ボタンおされたら戻る
        val backButton =  findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            // もとの画面に戻る
            finish()
        }
    }

    // タップしたらジャンプ
    // 画面タッチ処理はoverride fun onTouchEventの中に書く
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //スタート
        if (gameManager != null && !gameManager!!.isGaming){
            if ( event?.action == android.view.MotionEvent.ACTION_UP) {
                gameManager?.startGame()
            }
        }else{
            // タップした瞬間
            if ( event?.action == android.view.MotionEvent.ACTION_DOWN && !gameManager!!.gameHiyoko.isJumping) {
                gameManager?.gameHiyoko?.jump()

            }
        }
        return super.onTouchEvent(event)
    }

}