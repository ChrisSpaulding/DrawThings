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
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.AXIS_X
import android.view.MotionEvent.AXIS_Y
import android.view.View
import android.widget.ImageView


import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    var x1  = 0f
    var x2 = 0f
    var y1 = 0f
    var y2  = 0f
    var dx = 0f
    var dy = 0f
    val ROWSOFDOTS = 4
    val COLUMNSOFDOTS = 5
    var game : GameLogic = GameLogic(ArrayList<ArrayList<GameNode>>(),ROWSOFDOTS,COLUMNSOFDOTS  )
    val dotSpacingValue= 6f

     var bitmap :Bitmap ?= null
    var canvas: Canvas ?= null
    var paint: Paint = Paint(Color.RED)

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

        btn_SubmitMove.setOnClickListener{
            drawGame()
        }
    }

    fun setUpGame() {
        //drawing
        bitmap = Bitmap.createBitmap(iv.width, iv.height, Bitmap.Config.ARGB_8888)
        this.canvas = Canvas(bitmap)
        paint.style = Paint.Style.STROKE
        //Line Width in pixels
        paint.strokeWidth = 8f
        paint.isAntiAlias = true
        var hSpace: Float = (canvas!!.height - 4) / dotSpacingValue
        var wSpace: Float = (canvas!!.width - 4) / dotSpacingValue

        //create nodes
        for (i in 1..ROWSOFDOTS) {
            game.gameBoard.add(ArrayList<GameNode>())
            for (j in 1..COLUMNSOFDOTS ) {
                canvas!!.drawCircle(j * wSpace, i * hSpace, 1f, paint)
                game.gameBoard.get(i-1).add(GameNode(j * wSpace, i * hSpace, wSpace, hSpace, false ,false))
            }
        }


        var mImageView: ImageView = findViewById(R.id.iv) as ImageView
        mImageView.setImageBitmap(bitmap)


    }
    
    fun drawGame(){
        this.canvas= game.drawBoard(canvas!!,paint)
        var mImageView: ImageView = findViewById(R.id.iv) as ImageView
        mImageView.setImageBitmap(bitmap)

    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true

    }
//https://stackoverflow.com/questions/3148741/how-to-capture-finger-movement-direction-in-android-phone/3151831?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    private fun onTouchEvent(view: View, event: MotionEvent ):Boolean {



    var direction  = ""

        when (event.action){
            MotionEvent.ACTION_DOWN -> {
                x1= event.x
                y1= event.y
                Log.i("X1","$x1")
                Log.i("Y1", "$y1")
            }

            MotionEvent.ACTION_UP -> {
                x2 = event.getAxisValue(AXIS_X,event.pointerCount-1)
                y2 = event.getAxisValue(AXIS_Y, event.pointerCount-1)
                Log.i("X2", "$x2")
                Log.i("Y2", "$y2")
                dx = x2-x1
                dy = y2-y1

                //why are x1 and x2 not updateing correctly?
                if(y1< y2){
                    Log.i("y1<y2 x1", "$x1")
                    Log.i("y: ", "$y1")
                   val ans =findClosestDot(x1,y1)
                    this.canvas= game.drawLineDown(ans[0],ans[1], canvas!!,Paint(Color.BLACK))
                            direction= direction + "down"
                    drawGame()
                }
                else {
                    direction += "up"

                }
            }
        }
        Log.i("touch direction", direction)
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


    //getting wrong values
    fun findClosestDot(x :Float, y :Float) : Array<Int> {
        val ySpace = (iv.height/dotSpacingValue).toInt()
        val xSpace = (iv.width/dotSpacingValue).toInt()
        var yfound = false;
        var i=1
        var yLevel=-1
        while(!yfound){
            if(y<i*ySpace){
                yfound=true   
                yLevel=i-1
            }
            i++
            if(i==100)
                yfound=true
                yLevel=0
        }
        var xfound = false;
        i=-1
        var xLevel= -1
        while(!xfound){
            if(x<i*xSpace){
                xfound=true
                xLevel=i-1
            }
            i++
            if(i==100){
                xfound=true
                xLevel=0
            }
        }
        Log.i("xLevel", "$xLevel")
        Log.i("yLevel", "$yLevel")
        return arrayOf(xLevel,yLevel)
    }




}


