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

        // 食べてるアニメーション
        eating()
    }

    private fun eating(){
        // たべてる画像切り替え
        val images = when(watchcchi?.getEvolveLevel()){
            EvolveLevel.HIYOKO -> arrayOf(
                R.drawable.hiyoko_eating1,
                R.drawable.hiyoko_eating2,
                R.drawable.hiyoko_eating3,
                R.drawable.hiyoko_eating4,
                R.drawable.hiyoko_eating5,
                R.drawable.hiyoko_eating6,
                R.drawable.hiyoko_eating7,
                R.drawable.hiyoko_eating8,
                R.drawable.hiyoko_eating9,
                R.drawable.hiyoko_eating9,
            )
            EvolveLevel.NIWATORI -> arrayOf(
                R.drawable.hiyoko_eating1,
                R.drawable.hiyoko_eating2,
                R.drawable.hiyoko_eating3,
                R.drawable.hiyoko_eating4,
                R.drawable.hiyoko_eating5,
                R.drawable.hiyoko_eating6,
                R.drawable.hiyoko_eating7,
                R.drawable.hiyoko_eating8,
                R.drawable.hiyoko_eating9,
                R.drawable.hiyoko_eating9,
            )
            else -> arrayOf(
                R.drawable.hiyoko_eating1,
                R.drawable.hiyoko_eating2,
                R.drawable.hiyoko_eating3,
                R.drawable.hiyoko_eating4,
                R.drawable.hiyoko_eating5,
                R.drawable.hiyoko_eating6,
                R.drawable.hiyoko_eating7,
                R.drawable.hiyoko_eating8,
                R.drawable.hiyoko_eating9,
                R.drawable.hiyoko_eating9,
            )
        }

        val handler = Handler()
        var index = 0
        val imageView = findViewById<ImageView>(R.id.eating_image)
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val runnable = object : Runnable {
            override fun run() {
                // 振動
                vibrator.vibrate(50)
                // 画像切り替え
                imageView.setImageResource(images[index])
                index = (index + 1) % images.size
                if (index == 0) {
                    // 画像をすべて表示したら、もどる
                    finish()
                    return
                }
                handler.postDelayed(this, 800)
            }
        }
        handler.post(runnable)
    }
}