package c.spaulding.drawthings

import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import android.widget.ImageView


public class GameLogic (var gameBoard : ArrayList<ArrayList<GameNode>>, val ROWSOFDOTS : Int, val COLUMNSOFDOTS :Int  ){
    val xNumberOfDots= 5f

    var player1Score : Int = 0
    var player2Score : Int = 0



    //need to test this function
    public fun drawBoard(canvas : Canvas, paint: Paint) : Canvas
    {

        for (i in 0..ROWSOFDOTS-2) {
            for (j in 0..COLUMNSOFDOTS-1 ) {
                canvas.drawCircle(gameBoard.get(i).get(j).nodeX, gameBoard.get(i).get(j).nodeY, 1f, paint)

                if(!gameBoard.get(i).get(j).lineDown){
                    canvas.drawLine(gameBoard.get(i).get(j).nodeX, gameBoard.get(i).get(j).nodeY, gameBoard.get(i+1).get(j).nodeX   , gameBoard.get(i+1).get(j).nodeY, paint)  }
            }

        }

        for (i in 0..ROWSOFDOTS-1){
            for(j in 0..COLUMNSOFDOTS-2){
                if(!gameBoard.get(i).get(j).lineRight)
                     canvas.drawLine(gameBoard.get(i).get(j).nodeX, gameBoard.get(i).get(j).nodeY, gameBoard.get(i).get(j+1).nodeX, gameBoard.get(i).get(j+1).nodeY, paint)
            }
        }

    return canvas
    }





}