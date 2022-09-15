package ge.tbabunashvili.memorygame

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    var success = 0
    var fail = 0
    var current = 0
    var ftpic=0
    var stpic=0
    val imageViewIds = listOf<Int>(R.id.image0, R.id.image1, R.id.image2, R.id.image3,
                        R.id.image4, R.id.image5)

    val images = listOf<Int>(R.drawable.doggo1, R.drawable.doggo1, R.drawable.doggo2,
            R.drawable.doggo2,R.drawable.doggo3,R.drawable.doggo3)
    var remimages = images.toMutableList()
    var imageMap = mutableMapOf<Int,Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeScoreBoard()
        initializeImages()
        initializeMap()
        addButtonListener()
    }

    fun initializeScoreBoard() {
        success = 0
        fail = 0
        current = 0
        findViewById<TextView>(R.id.successScore).text = success.toString()
        findViewById<TextView>(R.id.failScore).text = fail.toString()
        colorText(R.color.grey, R.color.grey)
    }

    fun initializeMap() {
        var i = 0
        remimages = images.toMutableList()
        while(!remimages.isEmpty()) {
            var imgidx = Random.nextInt(remimages.size)
            imageMap[imageViewIds[i]] = remimages[imgidx]
            remimages.removeAt(imgidx)
            i++
        }
    }

    fun turnPicture(it : Int) {
        findViewById<ImageView>(it).setImageResource(imageMap[it]?:R.drawable.background)
        if (current == 2) {
            if (stpic != 0) {
                findViewById<ImageView>(ftpic).setImageResource(R.drawable.background)
                findViewById<ImageView>(stpic).setImageResource(R.drawable.background)
            }
            findViewById<ImageView>(it).setImageResource(imageMap[it]?:R.drawable.background)
            current = 1
            colorText(R.color.grey,R.color.grey)
            stpic = 0
            ftpic = it
            return
        }
        if (current == 0) {
            ftpic = it
        }   else if (current == 1) {
            if (ftpic == it) {
                return
            }
            stpic = it
            if (imageMap[ftpic] == imageMap[stpic]) {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    findViewById<ImageView>(ftpic).visibility = View.INVISIBLE
                    findViewById<ImageView>(stpic).visibility = View.INVISIBLE
                    ftpic = 0
                    stpic = 0
                    winRound()
                }, 100)
            }   else {
                loseRound()
            }
        }
        current++
    }

    fun winRound() {
        success++
        findViewById<TextView>(R.id.successScore).text = success.toString()
        colorText(R.color.green,R.color.grey)
    }

    fun loseRound() {
        fail++
        findViewById<TextView>(R.id.failScore).text = fail.toString()
        colorText(R.color.grey, R.color.red)
    }

    fun colorText(successColour : Int, failColour: Int) {
        findViewById<TextView>(R.id.successLabel).setTextColor(getResources().getColor(successColour))
        findViewById<TextView>(R.id.successScore).setTextColor(getResources().getColor(successColour))
        findViewById<TextView>(R.id.failLabel).setTextColor(getResources().getColor(failColour))
        findViewById<TextView>(R.id.failScore).setTextColor(getResources().getColor(failColour))
    }
    fun initializeImages() {
        imageViewIds.forEach{
            findViewById<ImageView>(it).setImageResource(R.drawable.background)
            findViewById<ImageView>(it).visibility = View.VISIBLE
            findViewById<ImageView>(it).setOnClickListener{ view ->
                turnPicture(it)
            }
        }
    }

    fun addButtonListener() {
        findViewById<Button>(R.id.restart).setOnClickListener{
            initializeScoreBoard()
            initializeImages()
            initializeMap()
        }
    }
}