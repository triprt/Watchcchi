package com.example.watchcchi

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import java.util.*

class GameManager (_gameActivity:GameActivity){
    var score = 0
    var highScore = 0
    var isGaming:Boolean = false
    lateinit var gameHiyoko:GameHiyoko
    private lateinit var gameActivity :GameActivity
    private var timer : Timer? = null
    private var nowObstacleAreaNo:ObstcleAreaNo = ObstcleAreaNo.NO1

    enum class ObstcleAreaNo(val id: Int) {
        NO1(R.id.obstcle_area1),
        NO2(R.id.obstcle_area2),
        NO3(R.id.obstcle_area3)
    }

    // 間隔調整パラメータ
    private var obstacleIntervalParam:Int = 15

    // 間隔
    private var obstacleInterval:Int = obstacleIntervalParam
    private var obstacleIntervalCount:Int = 0


    // 障害物
    private var obstacles: ArrayList<GameObstacle> = ArrayList()

    init{
        gameActivity = _gameActivity
        gameHiyoko = GameHiyoko( gameActivity.findViewById<ImageView>(R.id.game_hiyoko))

    }

    fun startGame(){
        // 画像消す
        val tapToStart = gameActivity.findViewById<ImageView>(R.id.game_text)
        tapToStart.visibility = View.GONE

        isGaming = true
        gameHiyoko.startWalking()
        setTimer()
    }

    fun setTimer(){
        timer = Timer()
        val task = object : TimerTask() {
            // 250ms秒おきに画像を切り替える処理を実行
            override fun run() {
                // 障害物インターバルカウントを増やす
                obstacleIntervalCount ++

                if(isAddObstacle()){
                    addObstacle()
                }
            }
        }
        timer?.schedule(task,100 ,100)
    }

    // interval分カウントがすすんだら表示する
    fun isAddObstacle():Boolean {
        return obstacleIntervalCount >= obstacleInterval
    }

    fun addObstacle(){
        // 次のインターバルを設定
        obstacleIntervalCount = 0
        obstacleInterval = obstacleIntervalParam + (-5..5).random()

        val layout = gameActivity.findViewById<LinearLayout>(nowObstacleAreaNo.id)
        val obstacle = GameObstacle(ImageView(gameActivity),layout)
        obstacles.add(obstacle)
        obstacle.startMoving()
        // 適当に3こ以上なら消す
        if(obstacles.size >= 3){
            obstacles.removeAt(0)
        }
        // 次回のlayoutセット
        nowObstacleAreaNo = when(nowObstacleAreaNo){
            ObstcleAreaNo.NO1 -> ObstcleAreaNo.NO2
            ObstcleAreaNo.NO2 -> ObstcleAreaNo.NO3
            ObstcleAreaNo.NO3 -> ObstcleAreaNo.NO1
        }
    }

    fun updateScore() {
        score++
        if (score > highScore) {
            highScore = score
        }
    }

    fun gameOver() {
        timer?.cancel()
        score = 0
        // game over表示
        val tapToStart = gameActivity.findViewById<ImageView>(R.id.game_text)
        tapToStart.visibility = View.VISIBLE
        tapToStart.setImageResource(R.drawable.game_over)
    }

    fun saveHighScore() {
        // SharedPreferencesにハイスコアを保存
        /*
        val prefs = context.getSharedPreferences("game_data", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt("high_score", highScore)
        editor.apply()*/
    }
}
