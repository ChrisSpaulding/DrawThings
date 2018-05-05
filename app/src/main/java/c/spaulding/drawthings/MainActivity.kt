package c.spaulding.drawthings

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView


import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    val xNumberOfDots= 5f
    val ROWSOFDOTS = 4
    val COLUMNSOFDOTS = 5
     var bitmap :Bitmap ?= null
    var canvas: Canvas ?= null

       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            setUpGame()

            //canvas.drawLine(offset,canvas.height/2f,canvas.width/2f, canvas.width-offset, paint)
        //this is what gets it to show

        }
        iv.setOnTouchListener(View.OnTouchListener() {
            v, event ->  onTouchEvent(v, event)
           })


    }
    fun setUpGame(){
       bitmap = Bitmap.createBitmap(iv.width,iv.height, Bitmap.Config.ARGB_8888)
        this.canvas = Canvas(bitmap)
       var paint : Paint = Paint(Color.RED)

        paint.style=Paint.Style.STROKE
        //Line Width in pixels
        paint.strokeWidth = 8f
        paint.isAntiAlias=true


        var offset = 2f

        var hSpace :Float = (canvas!!.height-4)/xNumberOfDots
        var wSpace : Float = (canvas!!.width-4)/xNumberOfDots
        for( i in 1..COLUMNSOFDOTS  ){
            for (j in 1..ROWSOFDOTS)
                canvas!!.drawCircle(i*wSpace,j* hSpace , 1f, paint)
        }
        var mImageView : ImageView = findViewById(R.id.iv) as ImageView
        mImageView.setImageBitmap(bitmap)

    }

    fun drawGame(){

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true

    }

    private fun onTouchEvent(view: View, event: MotionEvent ):Boolean {
        event.action.
        return drawLine(event.x,event.y)
    }

     fun drawLine(x: Float, y : Float ) : Boolean{

         return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }




}


