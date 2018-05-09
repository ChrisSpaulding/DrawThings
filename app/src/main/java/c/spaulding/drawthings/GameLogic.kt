package c.spaulding.drawthings

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.preference.PreferenceManager
import android.util.Log
import android.widget.ImageView
import com.google.gson.Gson


public class GameLogic (var gameBoard : ArrayList<ArrayList<GameNode>>, val ROWSOFDOTS : Int, val COLUMNSOFDOTS :Int, val context: Context  ){
    var id = 1554
    val xNumberOfDots= 5f
    val PREF_NAME= "c.spaulding.drawthings.GameLogic"


    var player1Score : Int = 0
    var player2Score : Int = 0

/*
* TODO Save and restore from file
*
* TODO experiment with bluetooth? - probably not going to happen
 */


    //works
    public fun drawBoard(canvas : Canvas, paint: Paint) : Canvas
    {
        //does not work atm
        for (i in   1 ..ROWSOFDOTS) {
            for (j in 1 ..COLUMNSOFDOTS) {
                canvas.drawCircle(j * gameBoard[0][0].lineXSize, i * gameBoard[0][0].lineYSize, 6f, paint)
            }
        }

        for (i in 0..ROWSOFDOTS-2) {
            for (j in 0..COLUMNSOFDOTS-1 ) {
                canvas.drawCircle(gameBoard.get(i).get(j).nodeX, gameBoard.get(i).get(j).nodeY, 1f, paint)

                if(gameBoard.get(i).get(j).lineDown){
                    canvas.drawLine(gameBoard.get(i).get(j).nodeX, gameBoard.get(i).get(j).nodeY, gameBoard.get(i+1).get(j).nodeX   , gameBoard.get(i+1).get(j).nodeY, paint)  }
            }

        }

        for (i in 0..ROWSOFDOTS-1){
            for(j in 0..COLUMNSOFDOTS-2){
                if(gameBoard.get(i).get(j).lineRight)
                     canvas.drawLine(gameBoard.get(i).get(j).nodeX, gameBoard.get(i).get(j).nodeY, gameBoard.get(i).get(j+1).nodeX, gameBoard.get(i).get(j+1).nodeY, paint)
            }
        }

        for(i in 1 until ROWSOFDOTS){
            for (j in 1 until COLUMNSOFDOTS){
                if(gameBoard[i][j].playerScored==1){
                    paint.color= Color.RED
                    canvas.drawRect(gameBoard[i][j-1].nodeX,gameBoard[i-1][j].nodeY,gameBoard[i][j].nodeX,gameBoard[i][j].nodeY,paint)
                }
                if(gameBoard[i][j].playerScored==2){
                    paint.color = Color.BLUE
                    canvas.drawRect(gameBoard[i][j-1].nodeX,gameBoard[i-1][j].nodeY,gameBoard[i][j].nodeX,gameBoard[i][j].nodeY,  Paint(Color.RED))

                }
            }
        }

    return canvas
    }

    //runs through all nodes that can hold a score and adjusts points scored
    fun updateGameScores() : Array<Int> {
        this.player1Score = 0
        this.player2Score = 0
        for (i in 1 until ROWSOFDOTS) {
            for (j in 1 until COLUMNSOFDOTS) {
                if (gameBoard[i][j].playerScored == 1) {
                    player1Score = player1Score + 1
                }
                if (gameBoard[i][j].playerScored == 2) {
                    player2Score = player2Score +1
                }
            }
        }

        return arrayOf(player1Score, player2Score)
    }

//need to test
    //this should save the game as is
    //dzond.com/articles/storing-objects-android
    fun saveGame(){
       // convert to Json object
        var gson: Gson = Gson()
        var user_json: String = gson.toJson(this)


        //store in SharedPreference
        var id = "" + id

        var sharedPreferences : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val sharedPreference = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
        with(sharedPreference.edit()){
            putString(id,user_json)
            apply()
        }
    }

    //this should restore the game?
    //need to test
    fun restoreGame(){
        var sharedPreferences : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val sharedPreference = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
        var gson :  Gson = Gson()
        var user_json = sharedPreference.getString(id.toString(),"")
        var oldGame = gson.fromJson( user_json, this.javaClass)
        this.gameBoard = oldGame.gameBoard

    }

    //function to clear game.
    fun clearBoard(){
        for (i in 0 until ROWSOFDOTS){
            for(j in 0 until COLUMNSOFDOTS){
                gameBoard[i][j].lineRight=false
                gameBoard[i][j].lineDown=false
                gameBoard[i][j].playerScored=0
            }
        }
    }

}