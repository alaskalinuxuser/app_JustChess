package com.alaskalinuxuser.justchess;

/*  Copyright 2017 by AlaskaLinuxUser (https://thealaskalinuxuser.wordpress.com)
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.Arrays;

import static com.alaskalinuxuser.justchess.IntroActivity.pBlack;
import static com.alaskalinuxuser.justchess.IntroActivity.pPass;
import static com.alaskalinuxuser.justchess.TheEngine.getPromoteToB;
import static com.alaskalinuxuser.justchess.TheEngine.promoteToW;
import static com.alaskalinuxuser.justchess.TheEngine.terminal;
import static com.alaskalinuxuser.justchess.TheEngine.theBoard;
import static com.alaskalinuxuser.justchess.TheUserInterface.drawBoardPieces;

public class MainActivity extends AppCompatActivity {


    static ImageView x63,x62,x61,x60,x59,x58,x57,x56,x55,x54,x53,x52,x51,x50,x49,x48,x47,x46,x45,
            x44,x43,x42,x41,x40,x39,x38,x37,x36,x35,x34,x33,x32,x31,x30,x29,x28,x27,x26,x25,x24,
            x23,x22,x21,x20,x19,x18,x17,x16,x15,x14,x13,x12,x11,x10,x9,x8,x7,x6,x5,x4,x3,x2,x1,x0;

    static int[] imageViews = {R.id.p0,R.id.p1,R.id.p2,R.id.p3,R.id.p4,R.id.p5,R.id.p6,R.id.p7,R.id.p8,R.id.p9,
            R.id.p10,R.id.p11,R.id.p12,R.id.p13,R.id.p14,R.id.p15,R.id.p16,R.id.p17,R.id.p18,R.id.p19,
            R.id.p20,R.id.p21,R.id.p22,R.id.p23,R.id.p24,R.id.p25,R.id.p26,R.id.p27,R.id.p28,R.id.p29,
            R.id.p30,R.id.p31,R.id.p32,R.id.p33,R.id.p34,R.id.p35,R.id.p36,R.id.p37,R.id.p38,R.id.p39,
            R.id.p40,R.id.p41,R.id.p42,R.id.p43,R.id.p44,R.id.p45,R.id.p46,R.id.p47,R.id.p48,R.id.p49,
            R.id.p50,R.id.p51,R.id.p52,R.id.p53,R.id.p54,R.id.p55,R.id.p56,R.id.p57,R.id.p58,R.id.p59,
            R.id.p60,R.id.p61,R.id.p62,R.id.p63};

    static ImageView [] chessImage = {x0,x1,x2,x3,x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,
            x16,x17,x18,x19,x20,x21,x22,x23,x24,x25,x26,x27,x28,x29,x30,x31,
            x32,x33,x34,x35,x36,x37,x38,x39,x40,x41,x42,x43,x44,x45,x46,x47,
            x48,x49,x50,x51,x52,x53,x54,x55,x56,x57,x58,x59,x60,x61,x62,x63};

    static int engineStrength;
    boolean wTurn, firstClick;
    String tryMove;

    static String moveOptions;
    static int firstNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveOptions="";
                if (!wTurn){
                    moveOptions= terminal("suggestMove,black");
                } else {
                    moveOptions= terminal("suggestMove,white");
                }

                try {
                    if (moveOptions.equals("K-0-0R,")) {
                        chessImage[6].setBackgroundResource(R.drawable.suggested);;
                    } else if (moveOptions.equals("K0-0-0,")) {
                        chessImage[2].setBackgroundResource(R.drawable.suggested);;
                    } else if (moveOptions.equals("k-0-0r,")) {
                        chessImage[62].setBackgroundResource(R.drawable.suggested);;
                    } else if (moveOptions.equals("k0-0-0,")) {
                        chessImage[58].setBackgroundResource(R.drawable.suggested);;
                    } else {
                        String temp = String.valueOf(moveOptions.charAt(3)) +
                                String.valueOf(moveOptions.charAt(4));
                        int highlightThis = Integer.parseInt(temp);
                        chessImage[highlightThis].setBackgroundResource(R.drawable.suggested);
                        temp = String.valueOf(moveOptions.charAt(1)) +
                                String.valueOf(moveOptions.charAt(2));
                        highlightThis = Integer.parseInt(temp);
                        chessImage[highlightThis].setBackgroundResource(R.drawable.suggested);
                    }

                } catch (Exception e) {
                    // Do nothing.
                    Log.i("WJH", e.toString());
                }

                Snackbar.make(view, "JustChessEngine suggests: "+ moveOptions, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        firstClick = false;

        moveOptions = "Move Options";

        // Declare all of our image views programatically.
        for (int i=0; i<64; i++) {
            chessImage[i]=(ImageView)findViewById(imageViews[i]);
        } // checker board.

        //Start a new game.
        terminal("newGame");

        // Visually Draw the board....
        drawBoardPieces();

        wTurn = true;

            if (!pPass && pBlack){
                // Since this is not a pass and play game, and you chose to play as
                // black, then call move for the computer.
                getNextMove();
                wTurn = false;
            }


    }// End on create.

    // Our new class to tell the computer to think about a move....
    public class thinkMove extends AsyncTask<String, Void, String> {

        // Do this in the background.
        @Override
        protected String doInBackground(String... urls) {
            // Try this.
            try {
                terminal("makeMove,"+String.valueOf(engineStrength));
                // Have an exception clause so you don't crash.
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception";
            }
            return "Pass";
        }}// End asyncronous task of finding a move....

    public void getNextMove() {
        // Call the class to make a move...
        thinkMove task = new thinkMove();
        String result = null;
        try {
            // execute, or go on and do that task.
            result = task.execute("done").get();
            // A fail clause.
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result=="Pass"){
            if (wTurn) {
                wTurn = false;
            } else {
                wTurn = true;
            }
            // draw the board.
            drawBoardPieces();
            // rename the move button.
        } else {
            // Try again, but weaker.
            engineStrength=engineStrength-1;
            getNextMove();
        }
    } // End get next move.

    public void buttonNextMove (View view) {

        moveOptions= terminal("availMoves,"+String.valueOf(wTurn));

        /*
         * This next two lines could be used in place of getNextMove()
         * to aleviate the "application may be doing too much work on its main thread." error.
         * However, if you have this in place, and a phone is too slow, dropping or suspending a thread,
         * it may not work anymore.
         */
        //Executor executor = Executors.newSingleThreadExecutor();
        //executor.execute(new Runnable() { public void run() { getNextMove(); } });
        getNextMove();

        // To test responses // String query = "";
        // query = terminal("whoseTurn"); // To find out whose turn the computer thinks it is.
        // query = terminal("setTurn,white"); // To set the computer's turn to white. (use black for black).
        // query = terminal("suggestMove,black"); // To ask for a suggested rated move for black, use white for white.
        // query = terminal("getBoard"); // to get the logical board.

        /*
         * To set the engine board to equal your board. It is just a string of 64 characters.
         *
         * Uppercase are white, lowercase are black. Asterisk is blank.
         * RNBQKBNRPPPPPPPP********************************pppppppprnbqkbnr
         *
         * char[] myBoard = {'R','N','B','Q','K','B','N','R','P','P','P','P','P','P','P','P','*','*','*','*',
         *       '*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*',
         *       '*','*','*','*','*','p','p','p','p','p','p','p','p','r','n','b','q','k','b','n','r'};
         * String stringBoard="";
         * for (int i = 0; i < 64; i++) {stringBoard = stringBoard + String.valueOf(myBoard[i]);}
         * query = terminal("setBoard,"+ stringBoard);
         *
         */

        // query = terminal("pieceMoves,N,13"); // If a knight was at square 13, on the current board, what moves could be made?
        // This is useful for clicking a piece to see what options it has.
        // K = king, Q = queen, R = rook, B = bishop, N = knight, P = pawn.
        // Use lowercase for black, uppercase for white.
        // Note that this should only call for moves where a real piece exists, or the logical board may get messed up.

        // query = terminal("undoMove,P1220*");
        // Undo this move. Should be a real move.
        // Piece moved - square from - square to - taken

        // query = terminal("undoLastMove"); // Undo the last move done. Only works once, not sequential.

        // query = terminal("moveNow");
        // to force making a move. E.g., if the strength is so high it takes too long.
        // You can follow this with undoLastMove and setTurn as well to go back.
        // It is not perfect, but will choose the best move it has so far.
        // Usually takes 10 ms from the time it was told to stop.

        //query = terminal("myMove,p5438*");
        // To input your move. It will then flip white/black turn.

        // To test responses. // Log.i("WJH", query);

    } // End next move buton.

    public void moveablePiece (View view) {

        // Get the clicked squares tag to see what number it is.
        int number = Integer.parseInt(view.getTag().toString());
        String played;
        if (number < 10) {
            played = "0" + String.valueOf(number);
        } else {
            played = String.valueOf(number);
        }

        if (firstClick) {

            int minusNum = number-firstNum;
            int plusNum = firstNum-number;

            firstClick = false;
            String myMove = tryMove + played + String.valueOf(theBoard[number]);

            moveOptions= terminal("availMoves,"+String.valueOf(wTurn));

            String[] separated = moveOptions.split(",");

            if (Arrays.asList(separated).contains(myMove)) {

                String query = terminal("myMove,"+myMove);
                drawBoardPieces();
                wTurn = !wTurn;

                if (!pPass) {
                    // Since we moved, if it is not pass and play, make the computer move.
                    moveOptions="";
                    if (!wTurn){
                        moveOptions= terminal("suggestMove,black");
                    } else {
                        moveOptions= terminal("suggestMove,white");
                    }
                    if (moveOptions.isEmpty()) {
                        staleOrCheckMate();
                    } else {
                        getNextMove();
                    }
                }

            } else {

                if (myMove.equalsIgnoreCase("K0406*")) {
                    myMove = "K-0-0R";
                } else if (myMove.equalsIgnoreCase("K0402*")) {
                    myMove = "K0-0-0";
                } else if (myMove.equalsIgnoreCase("k6062*")) {
                    myMove = "k-0-0r";
                } else if (myMove.equalsIgnoreCase("k6058*")) {
                    myMove = "k0-0-0";
                }


                if (myMove.contains("P48") || myMove.contains("P49") || myMove.contains("P50") ||
                        myMove.contains("P51") || myMove.contains("P52") || myMove.contains("P53") ||
                        myMove.contains("P54") || myMove.contains("P55")) {
                    if (minusNum == 8) {
                        myMove = "Pu" + promoteToW + played + String.valueOf(theBoard[number]);
                    } else if (minusNum == 9) {
                        myMove = "Pr" + promoteToW + played + String.valueOf(theBoard[number]);
                    } else if (minusNum == 7) {
                        myMove = "Pl" + promoteToW + played + String.valueOf(theBoard[number]);
                    }
                }

                if (myMove.contains("p08") || myMove.contains("p09") || myMove.contains("p10") ||
                        myMove.contains("p11") || myMove.contains("p12") || myMove.contains("p13") ||
                        myMove.contains("p14") || myMove.contains("p15")) {
                    if (plusNum == 8) {
                        myMove = "pu" + getPromoteToB + played + String.valueOf(theBoard[number]);
                    } else if (plusNum == 7) {
                        myMove = "pr" + getPromoteToB + played + String.valueOf(theBoard[number]);
                    } else if (plusNum == 9) {
                        myMove = "pl" + getPromoteToB + played + String.valueOf(theBoard[number]);
                    }
                }

                if (myMove.contains("P32") || myMove.contains("P33") || myMove.contains("P34") ||
                        myMove.contains("P35") || myMove.contains("P36") || myMove.contains("P37") ||
                        myMove.contains("P38") || myMove.contains("P39")) {
                    if (minusNum == 9) {
                        myMove = "PER" + played + "p";
                    } else if (minusNum == 7) {
                        myMove = "PEL" + played + "p";
                    }
                }

                if (myMove.contains("p24") || myMove.contains("p25") || myMove.contains("p26") ||
                        myMove.contains("p27") || myMove.contains("p28") || myMove.contains("p29") ||
                        myMove.contains("p30") || myMove.contains("p31")) {
                    if (plusNum == 7) {
                        myMove = "per" + played + "P";
                    } else if (plusNum == 9) {
                        myMove = "pel" + played + "P";
                    }
                }

                if (Arrays.asList(separated).contains(myMove)) {

                    String query = terminal("myMove," + myMove);
                    drawBoardPieces();
                    wTurn = !wTurn;

                    if (!pPass) {
                        // Since we moved, if it is not pass and play, make the computer move.
                        moveOptions="";
                        if (!wTurn){
                            moveOptions= terminal("suggestMove,black");
                        } else {
                            moveOptions= terminal("suggestMove,white");
                        }
                        if (moveOptions.isEmpty()) {
                            staleOrCheckMate();
                        } else {
                            getNextMove();
                        }
                    }

                }
            }
            tryMove = "";
            myMove = "";
            drawBoardPieces();

        } else {
            firstNum = number;
            try {
                chessImage[firstNum].setBackgroundResource(R.drawable.highlight);
                firstClick = true;
                tryMove = String.valueOf(theBoard[number]) + played;
                // Testing only // Log.i("WJH", tryMove);
                String query = terminal("pieceMoves,"+ String.valueOf(theBoard[number]) +
                        "," + played);
                String[] stringArray = query.split(",");
                if (stringArray.length > 0) {
                    for (int i=0; i<stringArray.length; i++) {

                        String temp = stringArray[i];
                        if (temp.equals("K-0-0R")) {
                            chessImage[6].setBackgroundResource(R.drawable.highlight);;
                        } else if (temp.equals("K0-0-0")) {
                            chessImage[2].setBackgroundResource(R.drawable.highlight);;
                        } else if (temp.equals("k-0-0r")) {
                            chessImage[62].setBackgroundResource(R.drawable.highlight);;
                        } else if (temp.equals("k0-0-0")) {
                            chessImage[58].setBackgroundResource(R.drawable.highlight);;
                        } else {
                            temp = String.valueOf(stringArray[i].charAt(3)) +
                                    String.valueOf(stringArray[i].charAt(4));
                            int highlightThis = Integer.parseInt(temp);
                            chessImage[highlightThis].setBackgroundResource(R.drawable.highlight);
                        }
                    }
                }
            } catch (Exception e) {
                // Do nothing.
            }
        }

        moveOptions="";
        if (!wTurn){
            moveOptions= terminal("suggestMove,black");
        } else {
            moveOptions= terminal("suggestMove,white");
        }
        if (moveOptions.isEmpty()) {
            staleOrCheckMate();
        }

    } // End clicked piece.

    public void staleOrCheckMate() {
        String status = terminal("checkmate");
        if (status.equalsIgnoreCase("1")) {
            status = "checkmate!";
        } else {
            status = "stalemate!";
        }
        String turnIs = "";
        if (wTurn) {turnIs="White is in ";} else {turnIs="Black is in ";}
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(turnIs + status)
                .setMessage(
                        "Would you like to play a new game?")
                .setPositiveButton("View Board", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Do nothing.

                    }
                })
                .setNegativeButton("New Game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // First you define it.
                        Intent myintent = new Intent(MainActivity.this, IntroActivity.class);
                        myintent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        // Now you call it.
                        startActivity(myintent);

                    }
                })
                .show(); // Make sure you show your popup or it wont work very well!

    }

    public void resetGame(View view) {
        // Call for a new game and redraw the board.

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("New Game?")
                .setMessage(
                        "Would you like to play a new game?")
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // Do nothing.

                    }
                })
                .setNegativeButton("New Game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // First you define it.
                        Intent myintent = new Intent(MainActivity.this, IntroActivity.class);
                        myintent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        // Now you call it.
                        startActivity(myintent);

                    }
                })
                .show(); // Make sure you show your popup or it wont work very well!
    } // End reset game.

} // End main.
