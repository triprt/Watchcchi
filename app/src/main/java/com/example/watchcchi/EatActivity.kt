package com.example.watchcchi
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock.sleep
import android.os.Vibrator
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView

class EatActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eat)
        // プラス1する
        watchcchi?.feed()
        // 食べてるアニメーション
        eating()
    }

    private fun eating(){

        val images = getEatImage()
        val handler = Handler()
        var index = 0
        val delayMillis = getDelayMillis()
        val imageView = findViewById<ImageView>(R.id.eating_image)
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val runnable = object : Runnable {
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
                vibrator.vibrate(30)
            }
        }
        handler.post(runnable)
    }

    private fun getEatType() : EatType{
        // たべてる画像切り替え
        if(watchcchi?.hunger?.canFeed() == true){
            if( watchcchi?.getEvolveLevel() == EvolveLevel.HIYOKO){
                return EatType.HIYOKO_EAT
            }else{
                return EatType.NIWATORI_EAT
            }
        }else{
            if( watchcchi?.getEvolveLevel() == EvolveLevel.HIYOKO){
                return EatType.HIYOKO_NOEAT
            }else{
                return EatType.NIWATORI_NOEAT
            }
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
}