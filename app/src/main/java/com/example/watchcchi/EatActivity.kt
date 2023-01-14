package com.example.watchcchi
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock.sleep
import android.os.Vibrator
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView

class EatActivity : Activity() {

    private lateinit var watchicchiApp: WatchicchiApp
    private var runnable:Runnable? = null
    private lateinit var handler:Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eat)
        handler = Handler(Looper.getMainLooper())
        watchicchiApp = this.application as WatchicchiApp
        // プラス1する
        watchicchiApp.getWatchicchi().feed()
        // 食べてるアニメーション
        eating()
    }

    private fun eating(){

        val images = getEatImage()
        var index = 0
        val delayMillis = getDelayMillis()
        val imageView = findViewById<ImageView>(R.id.eating_image)
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        runnable = object : Runnable {
            override fun run() {
                // 画像切り替え
                imageView.setImageResource(images[index])
                index = (index + 1) % images.size
                if (index == 0) {
                    // 画像をすべて表示したら、もどる
                    finish()
                    return
                }
                handler.postDelayed(this, delayMillis)
                // 振動
                vibrator.vibrate(100)
            }
        }
        handler.post(runnable!!)
    }

    private fun getEatType() : EatType{
        // たべてる画像切り替え
        return if(watchicchiApp.getWatchicchi().getHunger().canFeed()){
            if(isHiyoko()){
                EatType.HIYOKO_EAT
            }else{
                EatType.NIWATORI_EAT
            }
        }else{
            if(isHiyoko()){
                EatType.HIYOKO_NOEAT
            }else{
                EatType.NIWATORI_NOEAT
            }
        }
    }

    // 表示させる画像はひよこか？
    private fun isHiyoko():Boolean{
        return when(watchicchiApp.getWatchicchi().getEvolveLevel()){
            WatchicchiApp.EvolveLevel.EGG -> false
            WatchicchiApp.EvolveLevel.HIYOKO_WITH_EGG -> false
            WatchicchiApp.EvolveLevel.HIYOKO -> true
            WatchicchiApp.EvolveLevel.HIYOKO_TO_NIWATORI -> true
            WatchicchiApp.EvolveLevel.NIWATORI -> false
            WatchicchiApp.EvolveLevel.NIWATORI_LAY_EGG -> false

            else -> {false}
        }


    }

    private fun getEatImage(): Array<Int> {
        return  when( getEatType() ){
            EatType.HIYOKO_EAT -> arrayOf(
                R.drawable.hiyoko_eating1,
                R.drawable.hiyoko_eating2,
                R.drawable.hiyoko_eating3,
                R.drawable.hiyoko_eating4,
                R.drawable.hiyoko_eating5,
                R.drawable.hiyoko_eating6,
                R.drawable.hiyoko_eating7,
                R.drawable.hiyoko_eating7,
            )
            EatType.HIYOKO_NOEAT -> arrayOf(
                R.drawable.hiyoko_no_eating1,
                R.drawable.hiyoko_no_eating2,
                R.drawable.hiyoko_no_eating1,
                R.drawable.hiyoko_no_eating2,
                R.drawable.hiyoko_no_eating1,
                R.drawable.hiyoko_no_eating2,
                R.drawable.hiyoko_no_eating2,
                )
            EatType.NIWATORI_NOEAT -> arrayOf(
                R.drawable.niwatori_no_eating1,
                R.drawable.niwatori_no_eating2,
                R.drawable.niwatori_no_eating1,
                R.drawable.niwatori_no_eating2,
                R.drawable.niwatori_no_eating1,
                R.drawable.niwatori_no_eating2,
                R.drawable.niwatori_no_eating2,
            )
            EatType.NIWATORI_EAT -> arrayOf(
                R.drawable.niwatori_eating1,
                R.drawable.niwatori_eating2,
                R.drawable.niwatori_eating3,
                R.drawable.niwatori_eating4,
                R.drawable.niwatori_eating5,
                R.drawable.niwatori_eating6,
                R.drawable.niwatori_eating7,
                R.drawable.niwatori_eating7,
                )
            else -> {arrayOf(
                R.drawable.hiyoko_eating1,
                R.drawable.hiyoko_eating2,
                R.drawable.hiyoko_eating3,
                R.drawable.hiyoko_eating4,
                R.drawable.hiyoko_eating5,
                R.drawable.hiyoko_eating6,
                R.drawable.hiyoko_eating7,
                R.drawable.hiyoko_eating7,
            )}
        }
    }

    private fun getDelayMillis():Long{
        return  when( getEatType()  ){
            EatType.HIYOKO_EAT -> 800
            EatType.HIYOKO_NOEAT -> 500
            EatType.NIWATORI_EAT -> 800
            EatType.NIWATORI_NOEAT -> 500
            else -> 800
        }
    }

    enum class EatType{
        HIYOKO_EAT,
        HIYOKO_NOEAT,
        NIWATORI_EAT,
        NIWATORI_NOEAT,
    }

    // activity終了でhandlerも終了
    override fun onDestroy() {
        super.onDestroy()
        if (runnable != null){
            handler.removeCallbacks(runnable!!)
        }
    }
}