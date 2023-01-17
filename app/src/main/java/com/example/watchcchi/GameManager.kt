package com.example.watchcchi

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import java.util.*
import kotlin.math.absoluteValue

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
            override fun run() {
                // 障害物インターバルカウントを増やす
                obstacleIntervalCount ++
                if( isAddObstacle() ){
                    // 障害物を追加する
                    addObstacle()
                }
                println( "obstacleIntervalCount:" + obstacleIntervalCount )
                // 障害物動かす
                for( obstacle in obstacles ){
                    obstacle.move()
                    val arrayO = IntArray(2)
                    obstacle.imageView.getLocationOnScreen(arrayO)

                    val arrayH = IntArray(2)
                    gameHiyoko.imageView.getLocationOnScreen(arrayH)

                    if((arrayH[0]-arrayO[0]).absoluteValue < 10 && (arrayH[1]-arrayO[1]).absoluteValue < 30){
                        obstacle.show()
                        gameOver()
                        break
                    }

                }
            }
        }
        // 100(ms)後にタイマー開始、50(ms)後ごとに、taskを実行する
        timer?.schedule(task,100 ,50)
    }

    // 障害物を追加する為のinterval分カウントが経過してるかどうか
    fun isAddObstacle(): Boolean {
        return obstacleIntervalCount >= obstacleInterval
    }

    fun addObstacle(){
        println( "obstacle.add()実行" )
        // 次のインターバルを設定
        obstacleIntervalCount = 0
        obstacleInterval = obstacleIntervalParam + (-50..50).random()

        val layout = gameActivity.findViewById<LinearLayout>(nowObstacleAreaNo.id)
        val obstacle = GameObstacle(ImageView(gameActivity),layout)
        obstacles.add(obstacle)
        obstacle.startMoving()
        //obstacle.add()

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
        println("gameover")

        timer?.cancel()
        gameHiyoko.stopWalking()
        for( obstacle in obstacles ){
            obstacle.stopMoving()
        }

        score = 0
        // game over表示

        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {
            override fun run() {
                val tapToStart = gameActivity.findViewById<ImageView>(R.id.game_text)
                tapToStart.visibility = View.VISIBLE
                tapToStart.setImageResource(R.drawable.game_over)
            }
        }
        handler.post(runnable)


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
