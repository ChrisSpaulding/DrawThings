package c.spaulding.drawthings

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Bitmap
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.AXIS_X
import android.view.MotionEvent.AXIS_Y
import android.view.View
import android.widget.ImageView


import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    /*
    ToDO here let players set custom colors

     */

    var currentPlayer =1
    var x1  = -1f
    var x2 = -1f
    var y1 = -1f
    var y2  = -1f
    var dir = 0

    val ROWSOFDOTS = 4
    val COLUMNSOFDOTS = 5
    var game : GameLogic = GameLogic(ArrayList<ArrayList<GameNode>>(),ROWSOFDOTS,COLUMNSOFDOTS, this  )
    val dotSpacingValue= 6f

     var bitmap :Bitmap ?= null
    var canvas: Canvas ?= null
    var paint: Paint = Paint(Color.BLACK)



       override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        btn_Undo.visibility = View.INVISIBLE

        fab.setOnClickListener {

            setUpGame()
            if(gameHasData()){
                Log.i("game data", "game has data")
                game.clearBoard()
                drawGame()
            }
        }
        iv.setOnTouchListener(View.OnTouchListener() {
            v, event ->  onTouchEvent(v, event)
           })

        btn_SubmitMove.setOnClickListener{
            drawGame()
            x1=-1.0f
            x2=-1.0f
            btn_Undo.visibility= View.INVISIBLE
        }
        btn_Undo.setOnClickListener{
            undoLastMove(dir)
            setUpGame()
            dir=0
            x1=-1f
            x2=-1f
            drawGame()
        }

           btn_Player.setOnClickListener{
               if(btn_Player.isChecked){
                   currentPlayer=2
                   Log.i("is checked","player 2: $currentPlayer")
               }else{
                   currentPlayer=1
                   Log.i("is not checked","player 1: $currentPlayer")
               }
           }
    }

    //must be called first after the view has been drawn. This sets up the game with the correct number of rows and columns
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

        if(game.gameBoard.size<1) {
            Log.i("Size of gameBoard", "${game.gameBoard.size}")
            for (i in 1..ROWSOFDOTS) {
                game.gameBoard.add(ArrayList<GameNode>())
                for (j in 1..COLUMNSOFDOTS) {
                    canvas!!.drawCircle(j * wSpace, i * hSpace, 1f, paint)
                    game.gameBoard.get(i - 1).add(GameNode(j * wSpace, i * hSpace, wSpace, hSpace, false, false, 0))
                }
            }
        }

        var mImageView: ImageView = findViewById(R.id.iv) as ImageView
        mImageView.setImageBitmap(bitmap)

    }

    //should refresh the game board based on the current canvas
    fun drawGame(){
        this.canvas= game.drawBoard(canvas!!,paint= Paint( Color.BLUE))
        var mImageView: ImageView = findViewById(R.id.iv) as ImageView
        mImageView.setImageBitmap(bitmap)

    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true

    }

    //captures two sequential touch events
    private fun onTouchEvent(view: View, event: MotionEvent ):Boolean {
    when (event.action) {
        MotionEvent.ACTION_DOWN -> {
            if (x1>-1 && x2<0) {

                x2 = event.x
                y2 = event.y


                val temp2 = findClosestDot(x1,y1)
                if(closeDoubleTap()){
                    btn_Undo.visibility= View.INVISIBLE
                    scoreBox(temp2[0], temp2[1])
                }
                else{
                Log.i("first tap x","${temp2[0]}")
                Log.i("first tap y", "${temp2[1]}")
                dir = findDirection()
                //storing this for easy undo
                x1=temp2[0].toFloat()
                y1= temp2[1].toFloat()
                drawLine(temp2[0],temp2[1],dir)}
                drawGame()


            }
            if (x1<0) {
                x1 = event.x
                y1 = event.y
                Log.i("X1 tap", "$x1")
                Log.i("Y1 tap ", "$y1")
            }
            return true
        }

    }
    return false
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


    // function that finds the dot that is below and to the right of the touch
    fun findClosestDot(x :Float, y :Float) : Array<Int> {
        val ySpace = (iv.height/dotSpacingValue).toInt()
        val xSpace = (iv.width/dotSpacingValue).toInt()
        var yfound = false;
        var i=0
        var yLevel=-1
        Log.i("tap Y Space", "$ySpace")
        while(!yfound){
            Log.i("tap if controll", "y: $y i: $i ySpace: $ySpace")
            if(y <i*ySpace){
                yfound=true
                yLevel=i-1
            }
            i++
            if(i==100){
                yfound=true
                yLevel=-1
            }
        }
        var xfound = false;
        i=0
        var xLevel= -1
        while(!xfound){
            if(x<i*xSpace){
                xfound=true
                xLevel=i-1
            }
            i++
            if(i==100){
                xfound=true
                xLevel=-1
            }
        }
        Log.i("xLevel", "$xLevel")
        Log.i("yLevel", "$yLevel")
        if(yLevel>=ROWSOFDOTS){
            yLevel=ROWSOFDOTS-1
        }
        if(xLevel>=COLUMNSOFDOTS){
            xLevel=COLUMNSOFDOTS-1
        }
        btn_Undo.visibility = View.VISIBLE
        return arrayOf(xLevel,yLevel)
    }

    //Deturmins if a line should be drown down or accross 1 for a line to the right -1 for a line down
    fun findDirection():Int{
        var dx = x1-x2
        var dy =y1-y2

        dx = Math.abs(dx)
        dy = Math.abs(dy)
        if(dx>dy){
            return 1
        }
        else{
            return -1
        }
    }

    //adds a line that should be drawn to the gameboard.
    fun drawLine(x:Int, y: Int, dir : Int){

        if(dir<0){
            game.gameBoard[y][x].lineDown=true
        }
        else{
            game.gameBoard[y][x].lineRight=true
        }
    }

    //only undoes lines will not kill program when it is an option
    fun undoLastMove(dir : Int){
        if( x1<0){

        }
        else {
            if(dir<0){
                Log.i("tap Direction down", "y1= $y1 x1= $x1")
                game.gameBoard[y1.toInt()][x1.toInt()].lineDown=false
                Log.i("dot state tap", "${game.gameBoard[y1.toInt()][x1.toInt()].lineDown}")
            }
            else{
                game.gameBoard[y1.toInt()][x1.toInt()].lineRight=false
            }
        }
        btn_Undo.visibility = View.INVISIBLE
        drawGame()
    }


    fun closeDoubleTap(): Boolean{

        return (Math.abs(x2-x1)<50&& Math.abs(y2-y1)<50)
    }

    //will take in a node position and then score it for a player
    fun scoreBox(x :Int, y: Int){
        game.gameBoard[y][x].playerScored=currentPlayer
        pointUpdate()
    }

    fun pointUpdate(){
         var temp = game.updateGameScores()
        txt_player1score.text = "P1: ${temp[0]}"
        txt_player2score.text= "P2: ${temp[1]}"
    }

    fun gameHasData():Boolean{
        for (i in 0 until ROWSOFDOTS){
            for(j in 0 until COLUMNSOFDOTS){
                if(game.gameBoard[i][j].lineRight==true||
                game.gameBoard[i][j].lineDown==true||
                !(game.gameBoard[i][j].playerScored==0)){
                    return true
                }
            }
        }
        return false
    }


}


