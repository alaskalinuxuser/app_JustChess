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

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TheEngine {

    static int wKingNeverMove, wKRNeverMove,wQRNeverMove,
            bKingNeverMove,bKRNeverMove,bQRNeverMove;
    static boolean whiteTurn, stopNow;
    static int whiteKing, blackKing, plyTurn;
    static String promoteToW = "Q", getPromoteToB = "q", lastMove = "xxxxxx",
    stringBoard = "RNBQKBNRPPPPPPPP********************************pppppppprnbqkbnr";

    static char[] theBoard = {'R','N','B','Q','K','B','N','R','P','P','P','P','P','P','P','P','*','*','*','*',
            '*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*',
            '*','*','*','*','*','p','p','p','p','p','p','p','p','r','n','b','q','k','b','n','r'};

    //Modified from http://chessprogramming.wikispaces.com/Simplified+evaluation+function
    static int pawnBoard[]=
            { 500, 500, 500, 500, 500, 500, 500, 500,
                    50, 50, 50, 50, 50, 50, 50, 50,
                    0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0, 10, 10,  0,  0,  0,
                    10,  0, 10, 10, 10, 10,  0,  10,
                    5, 10, 10,-10,-10, 10, 10,  5,
                    0,  0,  0,  0,  0,  0,  0,  0};
    static int rookBoard[]=
            { 0,  0,  0,  0,  0,  0,  0,  0,
                    5, 10, 10, 10, 10, 10, 10,  5,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                    -5,  0,  0,  0,  0,  0,  0, -5,
                    1,  0,  0,  0,  0,  0,  0,  1,};
    static int knightBoard[]=
            {-50,-40,-30,-30,-30,-30,-40,-50,
                    -40,-20,  0,  0,  0,  0,-20,-40,
                    -30,  0, 10, 15, 15, 10,  0,-30,
                    -30,  5, 15,  0,  0, 15,  5,-30,
                    -30,  0, 15,  0,  0, 15,  0,-30,
                    -30,  5, 10, 15, 15, 10,  5,-30,
                    -40,-20,  0,  5,  5,  0,-20,-40,
                    -50,-30,-30,-30,-30,-30,-30,-50};
    static int bishopBoard[]=
            {-20,-10,-10,-10,-10,-10,-10,-20,
                    -10,  0,  0,  0,  0,  0,  0,-10,
                    -10,  0,  5, 10, 10,  5,  0,-10,
                    -20,  5,  5, 10, 10,  5,  5,-20,
                    -10,  0, 10, 10, 10, 10,  0,-10,
                    -10, 10, 10, 10, 10, 10, 10,-10,
                    -10,  5,  0,  0,  0,  0,  5,-10,
                    -20,-10,-10,-10,-10,-10,-10,-20};
    static int queenBoard[]=
            {-20,-10,-10, -5, -5,-10,-10,-20,
                    -10,  0,  0,  0,  0,  0,  0,-10,
                    -10,  0,  5,  5,  5,  5,  0,-10,
                    -5,  0,  5,  5,  5,  5,  0, -5,
                    0,  0,  5,  5,  5,  5,  0, -5,
                    -10,  5,  5,  5,  5,  5,  0,-10,
                    -10,  0,  5,  0,  0,  0,  0,-10,
                    -20,-10,-10, -5, -5,-10,-10,-20};
    static int kingBoardW[]=
            { 0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0, 0,
                    0,  0,  0,  0,  0,  0,  0, 0,
                    0,  0,  0,  0,  0,  0,  0, 0,
                    0,  0,  0,  0,  0,  0,  0, 0,
                    0,  0,  0,  0,  0,  0,  0, 0,
                    0,  0,  0,  0,  0,  0,  0, 0,
                    0,  110,  -40,  0,  -40,  100,  0,  0,};
    static int kingBoardB[]=
            { 0,  0,  0,  0,  0,  0,  0,  0,
                    0,  0,  0,  0,  0,  0,  0, 0,
                    0,  0,  0,  0,  0,  0,  0, 0,
                    0,  0,  0,  0,  0,  0,  0, 0,
                    0,  0,  0,  0,  0,  0,  0, 0,
                    0,  0,  0,  0,  0,  0,  0, 0,
                    0,  0,  0,  0,  0,  0,  0, 0,
                    0,  0,  110,  -40,  0,  -40,  100,  0,};

    public static String terminal (String s) {
        String term = "";
        // Debugging only // Log.i("WJH", s);
        if (s.length()<1){
            // Too short! Do nothing!
        } else if (s.equals("newGame")) {
            newGame();
        } else if (s.startsWith("makeMove")) {
            String part[] = s.split(",");
            int strength = Integer.parseInt(part[1]);
            callForMove(strength);
        } else if (s.startsWith("availMoves")) {
            String part[] = s.split(",");
            boolean turn;
            if (part[1].equals("true")) {turn = true;} else {turn = false;}
            term = allMoves(turn);
        } else if (s.startsWith("whoseTurn")) {
            if (whiteTurn) {term = "white";} else {term = "black";}
        } else if (s.startsWith("setTurn")) {
            String part[] = s.split(",");
            if (part[1].equals("white")) {
                whiteTurn = true;
                term = "set white";
            } else {whiteTurn = false;
                term = "set black";}
        } else if (s.startsWith("suggestMove")) {
            String part[] = s.split(",");
            if (part[1].equals("white")) {
                term = makeRatedMove(true);
            } else {term = makeRatedMove(false);}
        } else if (s.startsWith("getBoard")) {
            term = Arrays.toString(theBoard);
        } else if (s.startsWith("setBoard")) {
            String part[] = s.split(",");
            for (int i = 0; i < 64; i++) {
                theBoard[i] = Character.valueOf(part[1].charAt(i));
            }
            term = Arrays.toString(theBoard);
        } else if (s.startsWith("pieceMoves")) {
            String part[] = s.split(",");
            int i = Integer.parseInt(part[2]);
            switch (part[1]) {
                case "N": term += nightMoves(i); break;
                case "R": term += rookMoves(i); break;
                case "B": term += bishopMoves(i); break;
                case "Q": term += queenMoves(i); break;
                case "K": term += kingMoves(i); break;
                case "P": term += pawnMoves(i); break;
                case "n": term += nightMovesB(i); break;
                case "r": term += rookMovesB(i); break;
                case "b": term += bishopMovesB(i); break;
                case "q": term += queenMovesB(i); break;
                case "k": term += kingMovesB(i); break;
                case "p": term += pawnMovesB(i); break;
            }
        } else if (s.startsWith("undoMove")) {
            String part[] = s.split(",");
            undoMove(part[1]);
            term = "undone";
        } else if (s.startsWith("undoLastMove")) {
            undoMove(lastMove);
            term = "undid last move";
        } else if (s.startsWith("moveNow")) {
            stopNow = true;
            term = "movedNow";
        }  else if (s.startsWith("myMove")) {
            String part[] = s.split(",");
            makeMove(part[1]);
            term = "moved";
            if (whiteTurn) {whiteTurn = false;} else {whiteTurn = true;}
        }

        // Return the term output.
        return term;
    }

    public static boolean newGame() {
        // Temporary spot for this king movement status for castle.
        wKingNeverMove=0;wKRNeverMove=0;wQRNeverMove=0;
        bKingNeverMove=0;bKRNeverMove=0;bQRNeverMove=0;
        whiteTurn=true;
        plyTurn = 0;
        theBoard = new char[]{'R','N','B','Q','K','B','N','R','P','P','P','P','P','P','P','P','*','*','*','*',
                '*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*','*',
                '*','*','*','*','*','p','p','p','p','p','p','p','p','r','n','b','q','k','b','n','r'};
        whiteKing=4;
        blackKing=60;
        return true;
    } // End New game.

    public static void callForMove(int engineStrength) {
        //Write down the board, before we start calculating moves.
        writeBoard();
        if (engineStrength < 1) {
            // Random weak moves.
            String theMoves = allMoves(whiteTurn);
            int movesSize = theMoves.length()/7;
            Random generator = new Random();
            int choiceMove = generator.nextInt(movesSize);
            String randomMove = theMoves.substring(choiceMove*7, (choiceMove*7)+7);
            // Make sure our board matches the board prior to a move, in case it was
            // messed up when we calculated moves.
            rewriteBoard();
            makeMove(randomMove);
            plyTurn++;
        } else if (engineStrength == 1) {
            // Weak move based solely on  current material.
            String bestMove = makeRatedMove(whiteTurn);
            // Make sure our board matches the board prior to a move, in case it was
            // messed up when we calculated moves.
            rewriteBoard();
            makeMove(bestMove);
            plyTurn++;
        } else {
            // Engine strength is 2 or higher, so make a min/max move.
            String bestMove = "";
            stopNow = false;
            if (whiteTurn) {bestMove = minimaxRoot(engineStrength, true);}
            else {bestMove = minimaxRoot(engineStrength, false);}
            // Make sure our board matches the board prior to a move, in case it was
            // messed up when we calculated moves.
            rewriteBoard();
            makeMove(bestMove);
            plyTurn++;
        }
        // Trade sides after making a move....
        if (whiteTurn) {whiteTurn = false;} else {whiteTurn = true;}
    } // End call for move....

    public static void writeBoard() {
        // Write down the board, as it is.
        stringBoard = "";
        for (int i = 0; i < 64; i++) {stringBoard = stringBoard + String.valueOf(theBoard[i]);}
    }

    public static void rewriteBoard() {
        // Make sure our board matches the board prior to a move, in case it was
        // messed up when we calculated moves.
        for (int i = 0; i < 64; i++) {theBoard[i] = stringBoard.charAt(i);}
    }

    public static String makeRatedMove(boolean wturn) {
        String theMoves = allMoves(wturn);
        int movesSize = theMoves.length()/7;
        String bestMove = "";
        int bestValue= 0;
        if (wturn){bestValue=-9999;} else {bestValue=9999;}
        for (int i = 0; i < movesSize; i++) {
            String newGameMove = theMoves.substring(i*7, (i*7)+7);
            makeMove(newGameMove);
            int boardValue = rating(movesSize,wturn);
            // Debugging only //Log.i("WJH", "Made move, rating = "+newGameMove + String.valueOf(boardValue));// Debugging only //
            undoMove(newGameMove);
            if (wturn && boardValue > bestValue) {
                bestValue = boardValue;
                bestMove = newGameMove;
            } else if (!wturn && boardValue < bestValue) {
                bestValue = boardValue;
                bestMove = newGameMove;}}
        return bestMove;
    } // End rated move

    public static String minimaxRoot(int depth, boolean wturn) {
        String bestMove = "";
        int bestValue;
        if (wturn) {bestValue = -9999;} else {bestValue = 9999;}
        String theMoves = allMoves(wturn);
        int theseMovesSize = theMoves.length()/7;
        int boardValue;

        for (int i = 0; i < theseMovesSize; i++) {
            String newGameMove = theMoves.substring(i*7, (i*7)+7);
            makeMove(newGameMove);
            String alternateMove = makeRatedMove(!wturn);
            makeMove(alternateMove);
            boardValue = minimax(bestValue,theseMovesSize,depth-1,wturn);
            undoMove(alternateMove);
            undoMove(newGameMove);
            if (wturn && boardValue > bestValue) {
                bestValue = boardValue;
                bestMove = newGameMove;
            } else if (!wturn && boardValue < bestValue) {
                bestValue = boardValue;
                bestMove = newGameMove;}}
        return bestMove;} // End min/max.

    public static int minimax(int bestValue,int movesSize, int depth, boolean wturn) {

        if (stopNow) {
            if (depth < 1) {
                // Debugging only // Log.i("WJH", "rating = " + String.valueOf(rating(movesSize,wturn)));
                // Debugging only // Log.i("WJH", "board=" + Arrays.toString(theBoard));
                return rating(movesSize,wturn);
            } else if (depth > 0) {
                String theMoves = allMoves(wturn);
                int theseMovesSize = theMoves.length() / 7;

                for (int i = 0; i < theseMovesSize; i++) {
                    String newGameMove = theMoves.substring(i * 7, (i * 7) + 7);
                    makeMove(newGameMove);
                    String alternateMove = makeRatedMove(!wturn);
                    makeMove(alternateMove);
                    minimax(bestValue, movesSize, depth - 1, !wturn);
                    undoMove(alternateMove);
                    undoMove(newGameMove);
                }
            }}return rating(movesSize,wturn);} // End min/max.

    public static int rating(int list, boolean wTurn) {
        int counter=0;
        if (wTurn){
            counter+=rateMoveablitly(list);
            counter+=ratePositional(wTurn);
        } else {
            counter-=rateMoveablitly(list);
            counter-=ratePositional(wTurn);
        }
        counter+=rateMaterial();
        // Debugging only //Log.i("WJH", "Total Counter="+String.valueOf(counter));
        return counter;
    } // End rating

    public static int rateMoveablitly(int listLength) {
        int counter=0;
        counter+=listLength/5;//1 point per valid move
        if (listLength==0) {//current side is in checkmate or stalemate
            if (!isKingSafe()) {//if checkmate
                counter+=-200000;
            } else {//if stalemate
                counter+=-150000; }} else {
            // Debugging only //Log.i("WJH", "movability Counter="+String.valueOf(counter));
            return counter;
        }return counter;} // Rate moveability....

    public static int ratePositional(boolean wturn) {
        int counter=0;
        if (wturn) {
            for (int i=0;i<64;i++) {
                switch (theBoard[i]) {
                    case 'P': counter+=pawnBoard[63-i];
                        break;
                    case 'R': counter+=rookBoard[63-i];
                        break;
                    case 'N': counter+=knightBoard[63-i];
                        break;
                    case 'B': counter+=bishopBoard[63-i];
                        break;
                    case 'Q': counter+=queenBoard[63-i];
                        break;
                    case 'K': counter+=kingBoardW[63-i];
                        break;
                }
            } } else {
            for (int i=0;i<64;i++) {
                switch (theBoard[i]) {
                    case 'p': counter+=pawnBoard[i];
                        break;
                    case 'r': counter+=rookBoard[i];
                        break;
                    case 'n': counter+=knightBoard[i];
                        break;
                    case 'b': counter+=bishopBoard[i];
                        break;
                    case 'q': counter+=queenBoard[i];
                        break;
                    case 'k': counter+=kingBoardB[i];
                        break;
                }
            }
        }
        // Debugging only //Log.i("WJH", "Positional Counter="+String.valueOf(counter));
        return counter;
    }// End rate positional

    public static int rateMaterial(){
        int materialScore = 0;
        for (int i = 0; i < 64; i++) {
            switch (theBoard[i]) {
                case 'N': materialScore = materialScore + 30;break;
                case 'R': materialScore = materialScore + 50;break;
                case 'B': materialScore = materialScore + 35;break;
                case 'Q': materialScore = materialScore + 90;break;
                case 'K': materialScore = materialScore + 900;break;
                case 'P': materialScore = materialScore + 10;break;
                case 'n': materialScore = materialScore - 30;break;
                case 'r': materialScore = materialScore - 50;break;
                case 'b': materialScore = materialScore - 35;break;
                case 'q': materialScore = materialScore - 90;break;
                case 'k': materialScore = materialScore - 900;break;
                case 'p': materialScore = materialScore - 10;break;
            }
        }
        // Debugging only //Log.i("WJH", "Material Counter="+String.valueOf(materialScore));
        return materialScore;
    } // End rateMaterial.

    public static void makeMove(String move) {
        /*
         * In theory, if there are no moves, then you are in checkmate or stalemate....
         */
        // Debugging only // Log.i("WJH", "Move="+move);
        lastMove = move;
        boolean checkStaleMate = false;
        int to,from,other;
        char piece, promote;
        if (move.length() < 6 || move.charAt(0) == '-') {
            Log.i("WJH", "Checkmate or stalemate. " + move);
            checkStaleMate = true;
        } else { //  turn moves....
            promote = move.charAt(2);
            piece = move.charAt(0);
            if ("k-0-0r".equals(move)) {
                theBoard[60] = '*';
                theBoard[63] = '*';
                theBoard[62] = 'k';
                theBoard[61] = 'r';
            } else if ("k0-0-0".equals(move)) {
                theBoard[60] = '*';
                theBoard[56] = '*';
                theBoard[58] = 'k';
                theBoard[59] = 'r';
                theBoard[57] = '*';
            } else if ("K-0-0R".equals(move)) {
                theBoard[7] = '*';
                theBoard[6] = 'K';
                theBoard[5] = 'R';
                theBoard[4] = '*';
                wKingNeverMove++;
            } else if ("K0-0-0".equals(move)) {
                theBoard[4] = '*';
                theBoard[1] = '*';
                theBoard[2] = 'K';
                theBoard[3] = 'R';
                theBoard[0] = '*';
                wKingNeverMove++;
            } else {
                if (piece == 'p') {
                    // check for Pawn special moves....
                    if (move.charAt(1)=='e') {
                        if (move.charAt(2)=='l') {
                            to = Integer.parseInt(move.substring(3, 5));
                            from = to + 9;
                            other = to + 8;
                            theBoard[to] = 'p';
                            theBoard[from] = '*';
                            theBoard[other] = '*';
                        } else if (move.charAt(2)=='r') {
                            to = Integer.parseInt(move.substring(3, 5));
                            from = to + 7;
                            other = to + 8;
                            theBoard[to] = 'p';
                            theBoard[from] = '*';
                            theBoard[other] = '*';}
                    } else if (move.charAt(1)=='u') {
                        to = Integer.parseInt(move.substring(3, 5));
                        from = to+8;
                        theBoard[to] = promote;
                        theBoard[from] = '*';
                    } else if (move.charAt(1)=='r') {
                        to = Integer.parseInt(move.substring(3, 5));
                        from = to+7;
                        theBoard[to] = promote;
                        theBoard[from] = '*';
                    } else if (move.charAt(1)=='l') {
                        to = Integer.parseInt(move.substring(3, 5));
                        from = to+9;
                        theBoard[to] = promote;
                        theBoard[from] = '*';
                    } else {
                        from = Integer.parseInt(move.substring(1, 3));
                        to = Integer.parseInt(move.substring(3, 5));
                        theBoard[to] = piece;
                        theBoard[from] = '*';
                    }
                } else if (move.charAt(0) == 'P') {
                    // check for Pawn special moves....
                    if (move.charAt(1)=='E') {
                        if (move.charAt(2)=='L') {
                            to = Integer.parseInt(move.substring(3, 5));
                            from = to-7;
                            other = to-8;
                            theBoard[to] = 'P';
                            theBoard[from] = '*';
                            theBoard[other] = '*';
                        } else if (move.charAt(2)=='R') {
                            to = Integer.parseInt(move.substring(3, 5));
                            from = to-9;
                            other = to-8;
                            theBoard[to] = 'P';
                            theBoard[from] = '*';
                            theBoard[other] = '*';
                        }} else if (move.charAt(1)=='u') {
                        to = Integer.parseInt(move.substring(3, 5));
                        from = to-8;
                        theBoard[to] = promote;
                        theBoard[from] = '*';
                    } else if (move.charAt(1)=='r') {
                        to = Integer.parseInt(move.substring(3, 5));
                        from = to-9;
                        theBoard[to] = promote;
                        theBoard[from] = '*';
                    } else if (move.charAt(1)=='l') {
                        to = Integer.parseInt(move.substring(3, 5));
                        from = to-7;
                        theBoard[to] = promote;
                        theBoard[from] = '*';
                    } else {
                        from = Integer.parseInt(move.substring(1, 3));
                        to = Integer.parseInt(move.substring(3, 5));
                        theBoard[to] = piece;
                        theBoard[from] = '*';
                    }
                } else if (piece == 'K') {
                    from = Integer.parseInt(move.substring(1, 3));
                    to = Integer.parseInt(move.substring(3, 5));
                    theBoard[to] = piece;
                    theBoard[from] = '*';
                    whiteKing = to;
                    wKingNeverMove++;
                } else if (piece == 'k') {
                    from = Integer.parseInt(move.substring(1, 3));
                    to = Integer.parseInt(move.substring(3, 5));
                    theBoard[to] = piece;
                    theBoard[from] = '*';
                    blackKing = to;
                    bKingNeverMove++;
                } else {
                    from = Integer.parseInt(move.substring(1, 3));
                    to = Integer.parseInt(move.substring(3, 5));
                    theBoard[to] = piece;
                    theBoard[from] = '*';
                    if (from == 00) {wQRNeverMove++;}
                    if (from == 07) {wKRNeverMove++;}
                    if (from == 56) {bQRNeverMove++;}
                    if (from == 63) {bKRNeverMove++;}
                }}
        }
    } // End makeMove

    public static void undoMove(String move) {
        /*
         * In theory, if there are no moves, then you are in checkmate or stalemate....
         */
        boolean checkStaleMate = false;
        int to,from,other;
        char piece, take;
        if (move.length() < 6 || move.charAt(0) == '-') {
            Log.i("WJH", "Checkmate or stalemate. " + move);
            checkStaleMate = true;
        } else { // undo turn moves....
            piece = move.charAt(0);
            take = move.charAt(5);
            if ("k-0-0r".equals(move)) {
                theBoard[60] = 'k';
                theBoard[63] = 'r';
                theBoard[61] = '*';
                theBoard[62] = '*';
                bKingNeverMove--;
            } else if ("k0-0-0".equals(move)) {
                theBoard[60] = 'k';
                theBoard[56] = 'r';
                theBoard[58] = '*';
                theBoard[59] = '*';
                theBoard[57] = '*';
                bKingNeverMove--;
            } else if ("K-0-0R".equals(move)) {
                theBoard[5] = '*';
                theBoard[6] = '*';
                theBoard[4] = 'K';
                theBoard[7] = 'R';
                wKingNeverMove--;
            } else if ("K0-0-0".equals(move)) {
                theBoard[1] = '*';
                theBoard[2] = '*';
                theBoard[4] = 'K';
                theBoard[0] = 'R';
                theBoard[3] = '*';
                wKingNeverMove--;
            } else {
                if (piece == 'P') {
                    // check for Pawn special moves....
                    if (move.charAt(1)=='E') {
                        if (move.charAt(2)=='L') {
                            to = Integer.parseInt(move.substring(3, 5));
                            from = to-7;
                            other = to-8;
                            theBoard[to] = take;
                            theBoard[from] = 'P';
                            theBoard[other] = 'p';
                        } else if (move.charAt(2)=='R') {
                            to = Integer.parseInt(move.substring(3, 5));
                            from = to-9;
                            other = to-8;
                            theBoard[to] = take;
                            theBoard[from] = 'P';
                            theBoard[other] = 'p';}
                    } else if (move.charAt(1)=='u') {
                        to = Integer.parseInt(move.substring(3, 5));
                        from = to-8;
                        theBoard[to] = take;
                        theBoard[from] = 'P';
                    } else if (move.charAt(1)=='r') {
                        to = Integer.parseInt(move.substring(3, 5));
                        from = to-9;
                        theBoard[to] = take;
                        theBoard[from] = 'P';
                    } else if (move.charAt(1)=='l') {
                        to = Integer.parseInt(move.substring(3, 5));
                        from = to-7;
                        theBoard[to] = take;
                        theBoard[from] = 'P';
                    } else {
                        from = Integer.parseInt(move.substring(1, 3));
                        to = Integer.parseInt(move.substring(3, 5));
                        theBoard[to] = take;
                        theBoard[from] = 'P';
                    }
                } else if (piece == 'p') {
                    // check for Pawn special moves....
                    if (move.charAt(1)=='e') {
                        if (move.charAt(2)=='l') {
                            to = Integer.parseInt(move.substring(3, 5));
                            from = to-7;
                            other = to-8;
                            theBoard[to] = take;
                            theBoard[from] = 'p';
                            theBoard[other] = 'P';
                        } else if (move.charAt(2)=='r') {
                            to = Integer.parseInt(move.substring(3, 5));
                            from = to-9;
                            other = to-8;
                            theBoard[to] = take;
                            theBoard[from] = 'p';
                            theBoard[other] = 'P';}
                    } else if (move.charAt(1)=='u') {
                        to = Integer.parseInt(move.substring(3, 5));
                        from = to+8;
                        theBoard[to] = '*';
                        theBoard[from] = 'p';
                    } else if (move.charAt(1)=='r') {
                        to = Integer.parseInt(move.substring(3, 5));
                        from = to+7;
                        theBoard[to] = take;
                        theBoard[from] = 'p';
                    } else if (move.charAt(1)=='l') {
                        to = Integer.parseInt(move.substring(3, 5));
                        from = to+9;
                        theBoard[to] = take;
                        theBoard[from] = 'p';
                    } else {
                        from = Integer.parseInt(move.substring(1, 3));
                        to = Integer.parseInt(move.substring(3, 5));
                        theBoard[to] = take;
                        theBoard[from] = 'p'; }
                } else if (piece == 'K') {
                    from = Integer.parseInt(move.substring(1, 3));
                    to = Integer.parseInt(move.substring(3, 5));
                    theBoard[to] = take;
                    theBoard[from] = 'K';
                    whiteKing = from;
                    wKingNeverMove--;
                } else if (piece == 'k') {
                    from = Integer.parseInt(move.substring(1, 3));
                    to = Integer.parseInt(move.substring(3, 5));
                    theBoard[to] = take;
                    theBoard[from] = 'k';
                    blackKing = from;
                    bKingNeverMove--;
                } else {
                    from = Integer.parseInt(move.substring(1, 3));
                    to = Integer.parseInt(move.substring(3, 5));
                    theBoard[to] = take;
                    theBoard[from] = piece;
                    if (to == 00) {wQRNeverMove--;}
                    if (to == 07) {wKRNeverMove--;}
                    if (to == 56) {bQRNeverMove--;}
                    if (to == 63) {bKRNeverMove--;}
                }}
        } // End undo
    } // End undo moves

    public static String allMoves(boolean wTurn) {
        String list = "";
        if (wTurn){
            for (int i = 0; i < 64; i++) {
                switch (theBoard[i]) {
                    case 'N': list+=nightMoves(i);break;
                    case 'R': list+=rookMoves(i);break;
                    case 'B': list+=bishopMoves(i);break;
                    case 'Q': list+=queenMoves(i);break;
                    case 'K': list+=kingMoves(i);break;
                    case 'P': list+=pawnMoves(i);break;
                }
            }} else {
            for (int i = 0; i < 64; i++) {
                switch (theBoard[i]) {
                    case 'n': list+=nightMovesB(i);break;
                    case 'r': list+=rookMovesB(i);break;
                    case 'b': list+=bishopMovesB(i);break;
                    case 'q': list+=queenMovesB(i);break;
                    case 'k': list+=kingMovesB(i);break;
                    case 'p': list+=pawnMovesB(i);break;
                }
            }}
        //Debugging only // Log.i("WJH", list);
        return list;
        /*
         * The list is in this format 123456,
         * 1 = Moving piece
         * 2,3 = 2 digit from square
         * 4,5 = 2 digit to square
         * 6 = captured piece
         * followed by a comma.
         */
    } // End possible moves.

    public static boolean isKingSafe() {

        // For checking if the king is safe.
        int z;
        if (whiteTurn){
            z = whiteKing;
            int g = z%8;
            int h = z/8;
            boolean notI=true;
            // Bishop or Queen
            int k;
            if (h < 7) {
                // Up diagonal moves.
                if (g < 7) {
                    k = z + 9;
                    while (theBoard[k] == '*' && notI) {
                        if (k/8 < 7 && k%8 < 7) {
                            k = k + 9;
                        } else {notI = false;}} // While it's empty.
                    if (theBoard[k]=='b'||theBoard[k]=='q') {
                        return false;} // When there is an enemy.
                }
                notI = true;
                if (g > 0) {
                    k = z + 7;
                    while (theBoard[k] == '*' && notI) {
                        if (k%8 > 0 && k/8 < 7) {
                            k = k + 7;
                        } else {notI = false;}} // While it's empty.
                    if (theBoard[k]=='b'||theBoard[k]=='q') {
                        return false;} // When there is an enemy.
                }}

            if (h > 0) {
                // down diagonal moves.
                notI = true;
                if (g > 0) {
                    k = z - 9;
                    while (theBoard[k] == '*' && notI) {
                        if (k%8 > 0 && k/8 > 0) {
                            k = k - 9;
                        } else {notI = false;}} // While it's empty.
                    if (theBoard[k]=='b'||theBoard[k]=='q') {
                        return false;} // When there is an enemy.
                }
                notI = true;
                if (g < 7) {
                    k = z - 7;
                    while (theBoard[k] == '*' && notI) {
                        if (k%8 < 7 && k/8 > 0) {
                            k = k - 7;
                        } else { notI = false;}} // While it's empty.
                    if (theBoard[k]=='b'||theBoard[k]=='q') {
                        return false;} // When there is an enemy.
                }}
            // Rook or Queen
            // Up moves
            notI = true;
            int j = 1;
            int vert = 8;
            k = z;
            if (z < 56) {
                k = z + (vert * j);
            }
            while (theBoard[k] == '*' && notI) {
                vert += 8;
                if (k < 56) {
                    k = z + (vert * j);
                } else {notI = false;}} // While it's empty.
            if (theBoard[k]=='r'||theBoard[k]=='q') {
                return false;} // When there is an enemy..

            // Down moves
            notI = true;
            j = -1;
            vert = 8;
            k = z;
            if (z > 7) {
                k = z + (vert * j);
            }
            while (theBoard[k] == '*' && notI) {
                vert += 8;
                if (k >7) {
                    k = z + (vert * j);
                } else {notI = false;}} // While it's empty.
            if (theBoard[k]=='r'||theBoard[k]=='q') {
                return false;} // When there is an enemy..

            // Right side....
            notI = true;
            int rj = 1;
            int rk = z;
            if (g < 7) {
                rk = z + rj;
            }
            while (theBoard[rk] == '*' && notI) {
                rj++;
                if (rk%8 < 7) {
                    rk = z + rj;
                } else {notI = false;}} // While it's empty.
            if (theBoard[rk]=='r'||theBoard[rk]=='q') {
                return false;} // When there is an enemy..

            // Left side....
            notI=true;
            rj = 1;
            rk = z;
            if (g > 0) {
                rk = z - rj;
            }
            while (theBoard[rk] == '*' && notI) {
                rj++;
                if (rk%8 > 0) {
                    rk = z - rj;
                } else {notI = false;}} // While it's empty.
            if (theBoard[rk]=='r'||theBoard[rk]=='q') {
                return false;} // When there is an enemy..
            // Knight
            if (h < 7 ) {
                if (g > 1 && theBoard[z+6]=='n') {
                    return false;}
                if (g < 6 && theBoard[z+10]=='n') {
                    return false;}}
            if (h < 6 ) {
                if (g > 0 && theBoard[z+15]=='n') {
                    return false;}
                if (g < 7 && theBoard[z+17]=='n') {
                    return false;}}
            if (h > 0 ) {
                if (g < 6 && theBoard[z-6]=='n') {
                    return false;}
                if (g > 1 && theBoard[z-10]=='n') {
                    return false;}}
            if (h > 1 ) {
                if (g < 7 && theBoard[z-15]=='n') {
                    return false;}
                if (g > 0 && theBoard[z-17]=='n') {
                    return false;}}
            // King check // Don't move next to another king! // Also includes pawns.
            if (h < 7 ) {
                if (theBoard[z+8]=='k') {
                    return false;}
                if (g > 0) {
                    if (theBoard[z+7]=='k' || theBoard[z+7]=='p') {
                        return false;}}
                if (g < 7) {
                    if (theBoard[z+9]=='k' || theBoard[z+9]=='p') {
                        return false;}}}
            if (h > 0 ) {
                if (theBoard[z-8]=='k') {
                    return false;}
                if (g > 0) {
                    if (theBoard[z-9]=='k') {
                        return false;}}
                if (g < 7) {
                    if (theBoard[z-7]=='k') {
                        return false;}}}
            if (g > 0) {
                if (theBoard[z-1]=='k') {
                    return false;}}
            if (g < 7) {
                if (theBoard[z+1]=='k') {
                    return false;}}
            // End white king is safe.
        } else {
            z = blackKing;
            int g = z%8;
            int h = z/8;
            boolean notI=true;
            // Bishop or Queen
            int k;
            if (h < 7) {
                // Up diagonal moves.
                if (g < 7) {
                    k = z + 9;
                    while (theBoard[k] == '*' && notI) {
                        if (k/8 < 7 && k%8 < 7) {
                            k = k + 9;
                        } else {notI = false;}} // While it's empty.
                    if (theBoard[k]=='B'||theBoard[k]=='Q') {
                        return false;} // When there is an enemy.
                }
                notI = true;
                if (g > 0) {
                    k = z + 7;
                    while (theBoard[k] == '*' && notI) {
                        if (k%8 > 0 && k/8 < 7) {
                            k = k + 7;
                        } else {notI = false;}} // While it's empty.
                    if (theBoard[k]=='B'||theBoard[k]=='Q') {
                        return false;} // When there is an enemy.
                }}

            if (h > 0) {
                // down diagonal moves.
                notI = true;
                if (g > 0) {
                    k = z - 9;
                    while (theBoard[k] == '*' && notI) {
                        if (k%8 > 0 && k/8 > 0) {
                            k = k - 9;
                        } else {notI = false;}} // While it's empty.
                    if (theBoard[k]=='B'||theBoard[k]=='Q') {
                        return false;} // When there is an enemy.
                }
                notI = true;
                if (g < 7) {
                    k = z - 7;
                    while (theBoard[k] == '*' && notI) {
                        if (k%8 < 7 && k/8 > 0) {
                            k = k - 7;
                        } else { notI = false;}} // While it's empty.
                    if (theBoard[k]=='B'||theBoard[k]=='Q') {
                        return false;} // When there is an enemy.
                }}
            // Rook or Queen
            // Up moves
            notI = true;
            int j = 1;
            int vert = 8;
            k = z;
            if (z < 56) {
                k = z + (vert * j);
            }
            while (theBoard[k] == '*' && notI) {
                vert += 8;
                if (k < 56) {
                    k = z + (vert * j);
                } else {notI = false;}} // While it's empty.
            if (theBoard[k]=='R'||theBoard[k]=='Q') {
                return false;} // When there is an enemy..

            // Down moves
            notI = true;
            j = -1;
            vert = 8;
            k = z;
            if (z > 7) {
                k = z + (vert * j);
            }
            while (theBoard[k] == '*' && notI) {
                vert += 8;
                if (k >7) {
                    k = z + (vert * j);
                } else {notI = false;}} // While it's empty.
            if (theBoard[k]=='R'||theBoard[k]=='Q') {
                return false;} // When there is an enemy..

            // Right side....
            notI = true;
            int rj = 1;
            int rk = z;
            if (g < 7) {
                rk = z + rj;
            }
            while (theBoard[rk] == '*' && notI) {
                rj++;
                if (rk%8 < 7) {
                    rk = z + rj;
                } else {notI = false;}} // While it's empty.
            if (theBoard[rk]=='R'||theBoard[rk]=='Q') {
                return false;} // When there is an enemy..

            // Left side....
            notI=true;
            rj = 1;
            rk = z;
            if (g > 0) {
                rk = z - rj;
            }
            while (theBoard[rk] == '*' && notI) {
                rj++;
                if (rk%8 > 0) {
                    rk = z - rj;
                } else {notI = false;}} // While it's empty.
            if (theBoard[rk]=='R'||theBoard[rk]=='Q') {
                return false;} // When there is an enemy..
            // Knight
            if (h < 7 ) {
                if (g > 1 && theBoard[z+6]=='N') {
                    return false;}
                if (g < 6 && theBoard[z+10]=='N') {
                    return false;}}
            if (h < 6 ) {
                if (g > 0 && theBoard[z+15]=='N') {
                    return false;}
                if (g < 7 && theBoard[z+17]=='N') {
                    return false;}}
            if (h > 0 ) {
                if (g < 6 && theBoard[z-6]=='N') {
                    return false;}
                if (g > 1 && theBoard[z-10]=='N') {
                    return false;}}
            if (h > 1 ) {
                if (g < 7 && theBoard[z-15]=='N') {
                    return false;}
                if (g > 0 && theBoard[z-17]=='N') {
                    return false;}}
            // King check // Don't move next to another king!
            if (h < 7 ) {
                if (theBoard[z+8]=='K') {
                    return false;}
                if (g > 0) {
                    if (theBoard[z+7]=='K') {
                        return false;}}
                if (g < 7) {
                    if (theBoard[z+9]=='K') {
                        return false;}}}
            if (h > 0 ) {
                if (theBoard[z-8]=='K') {
                    return false;}
                if (g > 0) {
                    if (theBoard[z-9]=='K' || theBoard[z-9]=='P') {
                        return false;}}
                if (g < 7) {
                    if (theBoard[z-7]=='K' || theBoard[z-7]=='P') {
                        return false;}}}
            if (g > 0) {
                if (theBoard[z-1]=='K') {
                    return false;}}
            if (g < 7) {
                if (theBoard[z+1]=='K') {
                    return false;}}
            // End black king is safe.
        }
        // Nothing returned false, so we know the king is safe.
        return true;
    } // End is king safe?

    public static String nightMovesB(int i) {
        String list = "";
        List<Integer> theseMoves = new ArrayList<Integer>();
        String moveSquare;

        int rowNum = i/8;
        int colNum = i%8;

        if (rowNum < 7 ) {
            if (colNum > 1) {
                theseMoves.add(i + 6);
            }
            if (colNum < 6) {
                theseMoves.add(i+10);
            }
        }
        if (rowNum < 6 ) {
            if (colNum > 0) {
                theseMoves.add(i + 15);
            }
            if (colNum < 7) {
                theseMoves.add(i+17);
            }
        }
        if (rowNum > 0 ) {
            if (colNum < 6) {
                theseMoves.add(i - 6);
            }
            if (colNum > 1) {
                theseMoves.add(i-10);
            }
        }
        if (rowNum > 1 ) {
            if (colNum < 7) {
                theseMoves.add(i - 15);
            }
            if (colNum > 0) {
                theseMoves.add(i-17);
            }
        }

        for(int l=0; l<theseMoves.size();l++) {
            int k = theseMoves.get(l);
            if (Character.isUpperCase(theBoard[k]) || theBoard[k] == '*') {
                moveSquare = String.valueOf(theBoard[k]);
                theBoard[k] = 'n';
                theBoard[i] = '*';
                if (isKingSafe()) {
                    String F = String.valueOf(i);
                    String T = String.valueOf(k);
                    if (i < 10) {
                        F = "0" + F;
                    }
                    if (k < 10) {
                        T = "0" + T;
                    }
                    list = list + "n" + F + T + moveSquare.charAt(0) + ",";
                }
                theBoard[k] = moveSquare.charAt(0);
                theBoard[i] = 'n';
            }
        }
        return list;
    } // End black knight moves.

    public static String rookMovesB(int i) {
        String list = "";
        List<Integer> theseMoves = new ArrayList<Integer>();
        String moveSquare;
        int g = i%8;

        // Up moves
        boolean notI = true;
        int j = 1;
        int vert = 8;
        int k = i;
        if (i < 56) {
            k = i + (vert * j);
        }
        while (theBoard[k] == '*' && notI) {
            theseMoves.add(k);
            vert += 8;
            if (k < 56) {
                k = i + (vert * j);
            } else {
                notI = false;
            }
        } // While it's empty.
        if (Character.isUpperCase(theBoard[k])) {
            theseMoves.add(k);
        } // When there is an enemy.

        // Down moves
        notI = true;
        j = -1;
        vert = 8;
        k = i;
        if (i > 7) {
            k = i + (vert * j);
        }
        while (theBoard[k] == '*' && notI) {
            theseMoves.add(k);
            vert += 8;
            if (k >7) {
                k = i + (vert * j);
            } else {
                notI = false;
            }
        } // While it's empty.
        if (Character.isUpperCase(theBoard[k])) {
            theseMoves.add(k);
        } // When there is an enemy.

        // Right side....
        notI = true;
        int rj = 1;
        int rk = i;
        if (g < 7) {
            rk = i + rj;
        }
        while (theBoard[rk] == '*' && notI) {
            theseMoves.add(rk);
            rj++;
            if (rk%8 < 7) {
                rk = i + rj;
            } else {
                notI = false;
            }
        } // While it's empty.
        if (Character.isUpperCase(theBoard[rk])) {
            theseMoves.add(rk);
        } // When there is an enemy.

        // Left side....
        notI=true;
        rj = 1;
        rk = i;
        if (g > 0) {
            rk = i - rj;
        }
        while (theBoard[rk] == '*' && notI) {
            theseMoves.add(rk);
            rj++;
            if (rk%8 > 0) {
                rk = i - rj;
            } else {
                notI=false;
            }
        } // While it's empty.
        if (Character.isUpperCase(theBoard[rk])) {
            theseMoves.add(rk);
        } // When there is an enemy.

        for(int l=0; l<theseMoves.size();l++) {
            k = theseMoves.get(l);
            moveSquare = String.valueOf(theBoard[k]);
            theBoard[k] = 'r';
            theBoard[i] = '*';
            if (isKingSafe()) {
                String F = String.valueOf(i);
                String T = String.valueOf(k);
                if (i < 10) {
                    F = "0" + F;
                }
                if (k < 10) {
                    T = "0" + T;
                }
                list = list + "r" + F + T + moveSquare.charAt(0) + ",";
            }
            theBoard[k] = moveSquare.charAt(0);
            theBoard[i] = 'r';
        }
        return list;
    } // End black Rook moves.

    public static String bishopMovesB (int i) {
        String list = "";
        List<Integer> theseMoves = new ArrayList<Integer>();
        String moveSquare;
        boolean notI=true;
        int e = i/8;
        int f = i%8;

        int k;
        if (e < 7) {
            // Up diagonal moves.
            if (f < 7) {
                k = i + 9;
                while (theBoard[k] == '*' && notI) {
                    theseMoves.add(k);
                    if (k/8 < 7 && k%8 < 7) {
                        k = k + 9;
                    } else {
                        notI = false;
                    }
                } // While it's empty.
                if (Character.isUpperCase(theBoard[k])) {
                    theseMoves.add(k);
                } // When there is an enemy.
            }
            notI = true;
            if (f > 0) {
                k = i + 7;
                while (theBoard[k] == '*' && notI) {
                    theseMoves.add(k);
                    if (k%8 > 0 && k/8 < 7) {
                        k = k + 7;
                    } else {
                        notI = false;
                    }
                } // While it's empty.
                if (Character.isUpperCase(theBoard[k])) {
                    theseMoves.add(k);
                } // When there is an enemy.
            }
        }

        if (e > 0) {
            // down diagonal moves.
            notI = true;
            if (f > 0) {
                k = i - 9;
                while (theBoard[k] == '*' && notI) {
                    theseMoves.add(k);
                    if (k%8 > 0 && k/8 > 0) {
                        k = k - 9;
                    } else {
                        notI = false;
                    }
                } // While it's empty.
                if (Character.isUpperCase(theBoard[k])) {
                    theseMoves.add(k);
                } // When there is an enemy.
            }
            notI = true;
            if (f < 7) {
                k = i - 7;
                while (theBoard[k] == '*' && notI) {
                    theseMoves.add(k);
                    if (k%8 < 7 && k/8 > 0) {
                        k = k - 7;
                    } else {
                        notI = false;
                    }
                } // While it's empty.
                if (Character.isUpperCase(theBoard[k])) {
                    theseMoves.add(k);
                } // When there is an enemy.
            }
        }

        for(int l=0; l<theseMoves.size();l++) {
            k = theseMoves.get(l);
            moveSquare = String.valueOf(theBoard[k]);
            theBoard[k] = 'b';
            theBoard[i] = '*';
            if (isKingSafe()) {
                String F = String.valueOf(i);
                String T = String.valueOf(k);
                if (i < 10) {
                    F = "0" + F;
                }
                if (k < 10) {
                    T = "0" + T;
                }
                list = list + "b" + F + T + moveSquare.charAt(0) + ",";
            }
            theBoard[k] = moveSquare.charAt(0);
            theBoard[i] = 'b';
        }
        return list;
    } // End Black Bishop moves.

    public static String queenMovesB (int i) {
        // Combined Bishop and Rook set. I could have just called them as is, but then they would
        // be stamped, B and R, instead of Q.
        String list = "";
        List<Integer> theseMoves = new ArrayList<Integer>();
        String moveSquare;
        int g = i%8;

        // Up moves
        boolean notI = true;
        int j = 1;
        int vert = 8;
        int k = i;
        if (i < 56) {
            k = i + (vert * j);
        }
        while (theBoard[k] == '*' && notI) {
            theseMoves.add(k);
            vert += 8;
            if (k < 56) {
                k = i + (vert * j);
            } else {
                notI = false;
            }
        } // While it's empty.
        if (Character.isUpperCase(theBoard[k])) {
            theseMoves.add(k);
        } // When there is an enemy.

        // Down moves
        notI = true;
        j = -1;
        vert = 8;
        k = i;
        if (i > 7) {
            k = i + (vert * j);
        }
        while (theBoard[k] == '*' && notI) {
            theseMoves.add(k);
            vert += 8;
            if (k >7) {
                k = i + (vert * j);
            } else {
                notI = false;
            }
        } // While it's empty.
        if (Character.isUpperCase(theBoard[k])) {
            theseMoves.add(k);
        } // When there is an enemy.

        // Right side....
        notI = true;
        int rj = 1;
        int rk = i;
        if (g < 7) {
            rk = i + rj;
        }
        while (theBoard[rk] == '*' && notI) {
            theseMoves.add(rk);
            rj++;
            if (rk%8 < 7) {
                rk = i + rj;
            } else {
                notI = false;
            }
        } // While it's empty.
        if (Character.isUpperCase(theBoard[rk])) {
            theseMoves.add(rk);
        } // When there is an enemy.

        // Left side....
        notI=true;
        rj = 1;
        rk = i;
        if (g > 0) {
            rk = i - rj;
        }
        while (theBoard[rk] == '*' && notI) {
            theseMoves.add(rk);
            rj++;
            if (rk%8 > 0) {
                rk = i - rj;
            } else {
                notI=false;
            }
        } // While it's empty.
        if (Character.isUpperCase(theBoard[rk])) {
            theseMoves.add(rk);
        } // When there is an enemy.

        notI=true;
        int e = i/8;
        int f = i%8;

        if (e < 7) {
            // Up diagonal moves.
            if (f < 7) {
                k = i + 9;
                while (theBoard[k] == '*' && notI) {
                    theseMoves.add(k);
                    if (k/8 < 7 && k%8 < 7) {
                        k = k + 9;
                    } else {
                        notI = false;
                    }
                } // While it's empty.
                if (Character.isUpperCase(theBoard[k])) {
                    theseMoves.add(k);
                } // When there is an enemy.
            }
            notI = true;
            if (f > 0) {
                k = i + 7;
                while (theBoard[k] == '*' && notI) {
                    theseMoves.add(k);
                    if (k%8 > 0 && k/8 < 7) {
                        k = k + 7;
                    } else {
                        notI = false;
                    }
                } // While it's empty.
                if (Character.isUpperCase(theBoard[k])) {
                    theseMoves.add(k);
                } // When there is an enemy.
            }
        }

        if (e > 0) {
            // down diagonal moves.
            notI = true;
            if (f > 0) {
                k = i - 9;
                while (theBoard[k] == '*' && notI) {
                    theseMoves.add(k);
                    if (k%8 > 0 && k/8 > 0) {
                        k = k - 9;
                    } else {
                        notI = false;
                    }
                } // While it's empty.
                if (Character.isUpperCase(theBoard[k])) {
                    theseMoves.add(k);
                } // When there is an enemy.
            }
            notI = true;
            if (f < 7) {
                k = i - 7;
                while (theBoard[k] == '*' && notI) {
                    theseMoves.add(k);
                    if (k%8 < 7 && k/8 > 0) {
                        k = k - 7;
                    } else {
                        notI = false;
                    }
                } // While it's empty.
                if (Character.isUpperCase(theBoard[k])) {
                    theseMoves.add(k);
                } // When there is an enemy.
            }
        }

        for(int l=0; l<theseMoves.size();l++) {
            k = theseMoves.get(l);
            moveSquare = String.valueOf(theBoard[k]);
            theBoard[k] = 'q';
            theBoard[i] = '*';
            if (isKingSafe()) {
                String F = String.valueOf(i);
                String T = String.valueOf(k);
                if (i < 10) {
                    F = "0" + F;
                }
                if (k < 10) {
                    T = "0" + T;
                }
                list = list + "q" + F + T + moveSquare.charAt(0) + ",";
            }
            theBoard[k] = moveSquare.charAt(0);
            theBoard[i] = 'q';
        }
        return list;
    } // End Black Queen moves.

    public static String kingMovesB (int i) {
        String list = "";
        List<Integer> theseMoves = new ArrayList<Integer>();
        String moveSquare;
        int g = i%8;
        int h = i/8;
        if (h > 0) {
            if (theBoard[i-8] == '*' || Character.isUpperCase(theBoard[i-8])) {
                theseMoves.add(i-8);}
            if (g > 0) {
                if (theBoard[i-9] == '*' || Character.isUpperCase(theBoard[i-9])) {
                    theseMoves.add(i-9);}}
            if (g < 7) {
                if (theBoard[i-7] == '*' || Character.isUpperCase(theBoard[i-7])) {
                    theseMoves.add(i-7);}}}
        if (h < 7) {
            if (theBoard[i+8] == '*' || Character.isUpperCase(theBoard[i+8])) {
                theseMoves.add(i+8);}
            if (g < 7) {
                if (theBoard[i+9] == '*' || Character.isUpperCase(theBoard[i+9])) {
                    theseMoves.add(i+9);}}
            if (g > 0) {
                if (theBoard[i+7] == '*' || Character.isUpperCase(theBoard[i+7])) {
                    theseMoves.add(i+7);}}}
        if (g < 7) {
            if (theBoard[i+1] == '*' || Character.isUpperCase(theBoard[i+1])) {
                theseMoves.add(i+1);}}
        if (g > 0) {
            if (theBoard[i-1] == '*' || Character.isUpperCase(theBoard[i-1])) {
                theseMoves.add(i-1);}}

        int k;
        for(int l=0; l<theseMoves.size();l++) {
            k = theseMoves.get(l);
            moveSquare = String.valueOf(theBoard[k]);
            theBoard[k] = 'k';
            blackKing = k;
            theBoard[i] = '*';
            if (isKingSafe()) {
                String F = String.valueOf(i);
                String T = String.valueOf(k);
                if (i < 10) {
                    F = "0" + F;
                }
                if (k < 10) {
                    T = "0" + T;
                }
                list = list + "k" + F + T + moveSquare.charAt(0) + ",";
            }
            theBoard[k] = moveSquare.charAt(0);
            blackKing = i;
            theBoard[i] = 'k';
        }
        // Need castle moves //

        if (theBoard[60] == 'k' && isKingSafe()) {
            if (theBoard[61] == '*' && theBoard[62] == '*' && theBoard[63] == 'r') {
                blackKing = 61;
                if (isKingSafe()) {
                    blackKing = 62;
                    if (isKingSafe()) {
                        list = list + "k-0-0r,";
                    } else { blackKing = 60; }
                } else { blackKing = 60; }
            }
            if (theBoard[57] == '*' && theBoard[58] == '*' &&
                    theBoard[59] == '*' && theBoard[56] == 'r') {
                blackKing = 59;
                if (isKingSafe()) {
                    blackKing = 58;
                    if (isKingSafe()) {
                        list = list + "k0-0-0,";
                    } else { blackKing = 60; }
                } else { blackKing = 60; }
            }
        }

        // Castle moves //
        return list;
    } // End Black King moves.

    public static String pawnMovesB (int i) {
        String list = "";
        List<Integer> theseMoves = new ArrayList<Integer>();
        String moveSquare;
        int g = i%8;
        int h = i/8;

        int k, j;
        k = i - 8; j = i - 16;
        if (h == 6) {
            if (theBoard[k] == '*' && theBoard[j] == '*') {
                // The double step from the home row.
                theseMoves.add(j);
            }
        } else if (h == 3) {
            // The rule of en passant...
            if (lastMove.charAt(0) == 'P') {
                int tempTo = Integer.parseInt(lastMove.substring(3, 5));
                int tempFm = Integer.parseInt(lastMove.substring(1, 3));
                if (tempFm / 8 == 1 && tempTo / 8 == 3) { // They did a double step.
                    if (tempTo == i + 1) { // They are on your right.
                        theBoard[i - 7] = 'p';
                        theBoard[i] = '*';
                        theBoard[i + 1] = '*';
                        if (isKingSafe()) {
                            list = list + "per" + String.valueOf(i - 7) + "P,";
                        }
                        theBoard[i + 1] = 'P';
                        theBoard[i - 9] = '*';
                        theBoard[i] = 'p';
                    } else if (tempTo == i - 1) { // They are on your left.
                        theBoard[i - 9] = 'p';
                        theBoard[i] = '*';
                        theBoard[i - 1] = '*';
                        if (isKingSafe()) {
                            list = list + "pel" + String.valueOf(i - 9) + "P,";
                        }
                        theBoard[i - 1] = 'P';
                        theBoard[i - 9] = '*';
                        theBoard[i] = 'p';
                    }
                }
            } // End en passant....
        } else if (h == 1) {
            // The standard catch for moving one space forward.
            k = i - 8;
            if (theBoard[k] == '*') {
                moveSquare = String.valueOf(theBoard[k]);
                theBoard[k] = 'p';
                theBoard[i] = '*';
                if (isKingSafe()) {
                    list = list + "p" + "u" + getPromoteToB + "0"+ k + moveSquare.charAt(0) + ",";
                }
                theBoard[k] = moveSquare.charAt(0);
                theBoard[i] = 'p';
            }
            k = i - 9;// Attacking to the left and down.
            if (g > 0 && Character.isUpperCase(theBoard[k])) {
                moveSquare = String.valueOf(theBoard[k]);
                theBoard[k] = 'p';
                theBoard[i] = '*';
                if (isKingSafe()) {
                    list = list + "p" + "l" + getPromoteToB + "0"+ k + moveSquare.charAt(0) + ",";
                }
                theBoard[k] = moveSquare.charAt(0);
                theBoard[i] = 'p';
            }
            k = i - 7;// Attacking to the right and down.
            if (g < 7 && Character.isUpperCase(theBoard[k])) {
                moveSquare = String.valueOf(theBoard[k]);
                theBoard[k] = 'p';
                theBoard[i] = '*';
                if (isKingSafe()) {
                    list = list + "p" + "r" + getPromoteToB + "0"+ k + moveSquare.charAt(0) + ",";
                }
                theBoard[k] = moveSquare.charAt(0);
                theBoard[i] = 'p';
            } // End Promotions.
        } // End special pawn moves.

        if (h > 1 && h < 7) {
            // The standard catch for moving one space forward.
            k = i - 8;
            if (theBoard[k] == '*') {
                theseMoves.add(k);
            }
            k = i - 9;// Attacking to the left and down.
            if (g > 0 && Character.isUpperCase(theBoard[k])) {
                theseMoves.add(k);
            }
            k = i - 7;// Attacking to the right and down.
            if (g < 7 && Character.isUpperCase(theBoard[k])) {
                theseMoves.add(k);
            }
        } // End boring pawn moves.

        for(int l=0; l<theseMoves.size();l++) {
            k = theseMoves.get(l);
            moveSquare = String.valueOf(theBoard[k]);
            theBoard[k] = 'p';
            theBoard[i] = '*';
            if (isKingSafe()) {
                String F = String.valueOf(i);
                String T = String.valueOf(k);
                if (i < 10) {
                    F = "0" + F;
                }
                if (k < 10) {
                    T = "0" + T;
                }
                list = list + "p" + F + T + moveSquare.charAt(0) + ",";
            }
            theBoard[k] = moveSquare.charAt(0);
            theBoard[i] = 'p';
        }
        return list;
    } // End black pawn moves.

    public static String nightMoves(int i) {
        String list = "";
        List<Integer> theseMoves = new ArrayList<Integer>();
        String moveSquare;

        int rowNum = i/8;
        int colNum = i%8;

        if (rowNum < 7 ) {
            if (colNum > 1) {
                theseMoves.add(i + 6);
            }
            if (colNum < 6) {
                theseMoves.add(i+10);
            }
        }
        if (rowNum < 6 ) {
            if (colNum > 0) {
                theseMoves.add(i + 15);
            }
            if (colNum < 7) {
                theseMoves.add(i+17);
            }
        }
        if (rowNum > 0 ) {
            if (colNum < 6) {
                theseMoves.add(i - 6);
            }
            if (colNum > 1) {
                theseMoves.add(i-10);
            }
        }
        if (rowNum > 1 ) {
            if (colNum < 7) {
                theseMoves.add(i - 15);
            }
            if (colNum > 0) {
                theseMoves.add(i-17);
            }
        }

        for(int l=0; l<theseMoves.size();l++) {
            int k = theseMoves.get(l);
            if (Character.isLowerCase(theBoard[k]) || theBoard[k] == '*') {
                moveSquare = String.valueOf(theBoard[k]);
                theBoard[k] = 'N';
                theBoard[i] = '*';
                if (isKingSafe()) {
                    String F = String.valueOf(i);
                    String T = String.valueOf(k);
                    if (i < 10) {
                        F = "0" + F;
                    }
                    if (k < 10) {
                        T = "0" + T;
                    }
                    list = list + "N" + F + T + moveSquare.charAt(0) + ",";
                }
                theBoard[k] = moveSquare.charAt(0);
                theBoard[i] = 'N';
            }
        }
        return list;
    } // End knight moves.

    public static String rookMoves(int i) {
        String list = "";
        List<Integer> theseMoves = new ArrayList<Integer>();
        String moveSquare;
        int g = i%8;

        // Up moves
        boolean notI = true;
        int j = 1;
        int vert = 8;
        int k = i;
        if (i < 56) {
            k = i + (vert * j);
        }
        while (theBoard[k] == '*' && notI) {
            theseMoves.add(k);
            vert += 8;
            if (k < 56) {
                k = i + (vert * j);
            } else {
                notI = false;
            }
        } // While it's empty.
        if (Character.isLowerCase(theBoard[k])) {
            theseMoves.add(k);
        } // When there is an enemy.

        // Down moves
        notI = true;
        j = -1;
        vert = 8;
        k = i;
        if (i > 7) {
            k = i + (vert * j);
        }
        while (theBoard[k] == '*' && notI) {
            theseMoves.add(k);
            vert += 8;
            if (k >7) {
                k = i + (vert * j);
            } else {
                notI = false;
            }
        } // While it's empty.
        if (Character.isLowerCase(theBoard[k])) {
            theseMoves.add(k);
        } // When there is an enemy.

        // Right side....
        notI = true;
        int rj = 1;
        int rk = i;
        if (g < 7) {
            rk = i + rj;
        }
        while (theBoard[rk] == '*' && notI) {
            theseMoves.add(rk);
            rj++;
            if (rk%8 < 7) {
                rk = i + rj;
            } else {
                notI = false;
            }
        } // While it's empty.
        if (Character.isLowerCase(theBoard[rk])) {
            theseMoves.add(rk);
        } // When there is an enemy.

        // Left side....
        notI=true;
        rj = 1;
        rk = i;
        if (g > 0) {
            rk = i - rj;
        }
        while (theBoard[rk] == '*' && notI) {
            theseMoves.add(rk);
            rj++;
            if (rk%8 > 0) {
                rk = i - rj;
            } else {
                notI=false;
            }
        } // While it's empty.
        if (Character.isLowerCase(theBoard[rk])) {
            theseMoves.add(rk);
        } // When there is an enemy.

        for(int l=0; l<theseMoves.size();l++) {
            k = theseMoves.get(l);
            moveSquare = String.valueOf(theBoard[k]);
            theBoard[k] = 'R';
            theBoard[i] = '*';
            if (isKingSafe()) {
                String F = String.valueOf(i);
                String T = String.valueOf(k);
                if (i < 10) {
                    F = "0" + F;
                }
                if (k < 10) {
                    T = "0" + T;
                }
                list = list + "R" + F + T + moveSquare.charAt(0) + ",";
            }
            theBoard[k] = moveSquare.charAt(0);
            theBoard[i] = 'R';
        }
        return list;
    } // End Rook moves.

    public static String bishopMoves (int i) {
        String list = "";
        List<Integer> theseMoves = new ArrayList<Integer>();
        String moveSquare;
        boolean notI=true;
        int e = i/8;
        int f = i%8;

        int k;
        if (e < 7) {
            // Up diagonal moves.
            if (f < 7) {
                k = i + 9;
                while (theBoard[k] == '*' && notI) {
                    theseMoves.add(k);
                    if (k/8 < 7 && k%8 < 7) {
                        k = k + 9;
                    } else {
                        notI = false;
                    }
                } // While it's empty.
                if (Character.isLowerCase(theBoard[k])) {
                    theseMoves.add(k);
                } // When there is an enemy.
            }
            notI = true;
            if (f > 0) {
                k = i + 7;
                while (theBoard[k] == '*' && notI) {
                    theseMoves.add(k);
                    if (k%8 > 0 && k/8 < 7) {
                        k = k + 7;
                    } else {
                        notI = false;
                    }
                } // While it's empty.
                if (Character.isLowerCase(theBoard[k])) {
                    theseMoves.add(k);
                } // When there is an enemy.
            }
        }

        if (e > 0) {
            // down diagonal moves.
            notI = true;
            if (f > 0) {
                k = i - 9;
                while (theBoard[k] == '*' && notI) {
                    theseMoves.add(k);
                    if (k%8 > 0 && k/8 > 0) {
                        k = k - 9;
                    } else {
                        notI = false;
                    }
                } // While it's empty.
                if (Character.isLowerCase(theBoard[k])) {
                    theseMoves.add(k);
                } // When there is an enemy.
            }
            notI = true;
            if (f < 7) {
                k = i - 7;
                while (theBoard[k] == '*' && notI) {
                    theseMoves.add(k);
                    if (k%8 < 7 && k/8 > 0) {
                        k = k - 7;
                    } else {
                        notI = false;
                    }
                } // While it's empty.
                if (Character.isLowerCase(theBoard[k])) {
                    theseMoves.add(k);
                } // When there is an enemy.
            }
        }

        for(int l=0; l<theseMoves.size();l++) {
            k = theseMoves.get(l);
            moveSquare = String.valueOf(theBoard[k]);
            theBoard[k] = 'B';
            theBoard[i] = '*';
            if (isKingSafe()) {
                String F = String.valueOf(i);
                String T = String.valueOf(k);
                if (i < 10) {
                    F = "0" + F;
                }
                if (k < 10) {
                    T = "0" + T;
                }
                list = list + "B" + F + T + moveSquare.charAt(0) + ",";
            }
            theBoard[k] = moveSquare.charAt(0);
            theBoard[i] = 'B';
        }
        return list;
    } // End Bishop moves.

    public static String queenMoves (int i) {
        // Combined Bishop and Rook set. I could have just called them as is, but then they would
        // be stamped, B and R, instead of Q.
        String list = "";
        List<Integer> theseMoves = new ArrayList<Integer>();
        String moveSquare;
        int g = i%8;

        // Up moves
        boolean notI = true;
        int j = 1;
        int vert = 8;
        int k = i;
        if (i < 56) {
            k = i + (vert * j);
        }
        while (theBoard[k] == '*' && notI) {
            theseMoves.add(k);
            vert += 8;
            if (k < 56) {
                k = i + (vert * j);
            } else {
                notI = false;
            }
        } // While it's empty.
        if (Character.isLowerCase(theBoard[k])) {
            theseMoves.add(k);
        } // When there is an enemy.

        // Down moves
        notI = true;
        j = -1;
        vert = 8;
        k = i;
        if (i > 7) {
            k = i + (vert * j);
        }
        while (theBoard[k] == '*' && notI) {
            theseMoves.add(k);
            vert += 8;
            if (k >7) {
                k = i + (vert * j);
            } else {
                notI = false;
            }
        } // While it's empty.
        if (Character.isLowerCase(theBoard[k])) {
            theseMoves.add(k);
        } // When there is an enemy.

        // Right side....
        notI = true;
        int rj = 1;
        int rk = i;
        if (g < 7) {
            rk = i + rj;
        }
        while (theBoard[rk] == '*' && notI) {
            theseMoves.add(rk);
            rj++;
            if (rk%8 < 7) {
                rk = i + rj;
            } else {
                notI = false;
            }
        } // While it's empty.
        if (Character.isLowerCase(theBoard[rk])) {
            theseMoves.add(rk);
        } // When there is an enemy.

        // Left side....
        notI=true;
        rj = 1;
        rk = i;
        if (g > 0) {
            rk = i - rj;
        }
        while (theBoard[rk] == '*' && notI) {
            theseMoves.add(rk);
            rj++;
            if (rk%8 > 0) {
                rk = i - rj;
            } else {
                notI=false;
            }
        } // While it's empty.
        if (Character.isLowerCase(theBoard[rk])) {
            theseMoves.add(rk);
        } // When there is an enemy.

        notI=true;
        int e = i/8;
        int f = i%8;

        if (e < 7) {
            // Up diagonal moves.
            if (f < 7) {
                k = i + 9;
                while (theBoard[k] == '*' && notI) {
                    theseMoves.add(k);
                    if (k/8 < 7 && k%8 < 7) {
                        k = k + 9;
                    } else {
                        notI = false;
                    }
                } // While it's empty.
                if (Character.isLowerCase(theBoard[k])) {
                    theseMoves.add(k);
                } // When there is an enemy.
            }
            notI = true;
            if (f > 0) {
                k = i + 7;
                while (theBoard[k] == '*' && notI) {
                    theseMoves.add(k);
                    if (k%8 > 0 && k/8 < 7) {
                        k = k + 7;
                    } else {
                        notI = false;
                    }
                } // While it's empty.
                if (Character.isLowerCase(theBoard[k])) {
                    theseMoves.add(k);
                } // When there is an enemy.
            }
        }

        if (e > 0) {
            // down diagonal moves.
            notI = true;
            if (f > 0) {
                k = i - 9;
                while (theBoard[k] == '*' && notI) {
                    theseMoves.add(k);
                    if (k%8 > 0 && k/8 > 0) {
                        k = k - 9;
                    } else {
                        notI = false;
                    }
                } // While it's empty.
                if (Character.isLowerCase(theBoard[k])) {
                    theseMoves.add(k);
                } // When there is an enemy.
            }
            notI = true;
            if (f < 7) {
                k = i - 7;
                while (theBoard[k] == '*' && notI) {
                    theseMoves.add(k);
                    if (k%8 < 7 && k/8 > 0) {
                        k = k - 7;
                    } else {
                        notI = false;
                    }
                } // While it's empty.
                if (Character.isLowerCase(theBoard[k])) {
                    theseMoves.add(k);
                } // When there is an enemy.
            }
        }

        for(int l=0; l<theseMoves.size();l++) {
            k = theseMoves.get(l);
            moveSquare = String.valueOf(theBoard[k]);
            theBoard[k] = 'Q';
            theBoard[i] = '*';
            if (isKingSafe()) {
                String F = String.valueOf(i);
                String T = String.valueOf(k);
                if (i < 10) {
                    F = "0" + F;
                }
                if (k < 10) {
                    T = "0" + T;
                }
                list = list + "Q" + F + T + moveSquare.charAt(0) + ",";
            }
            theBoard[k] = moveSquare.charAt(0);
            theBoard[i] = 'Q';
        }
        return list;
    } // End Queen moves.

    public static String kingMoves (int i) {
        String list = "";
        List<Integer> theseMoves = new ArrayList<Integer>();
        String moveSquare;
        int g = i%8;
        int h = i/8;
        if (h > 0) {
            if (theBoard[i-8] == '*' || Character.isLowerCase(theBoard[i-8])) {
                theseMoves.add(i-8);}
            if (g > 0) {
                if (theBoard[i-9] == '*' || Character.isLowerCase(theBoard[i-9])) {
                    theseMoves.add(i-9);}}
            if (g < 7) {
                if (theBoard[i-7] == '*' || Character.isLowerCase(theBoard[i-7])) {
                    theseMoves.add(i-7);}}}
        if (h < 7) {
            if (theBoard[i+8] == '*' || Character.isLowerCase(theBoard[i+8])) {
                theseMoves.add(i+8);}
            if (g < 7) {
                if (theBoard[i+9] == '*' || Character.isLowerCase(theBoard[i+9])) {
                    theseMoves.add(i+9);}}
            if (g > 0) {
                if (theBoard[i+7] == '*' || Character.isLowerCase(theBoard[i+7])) {
                    theseMoves.add(i+7);}}}
        if (g < 7) {
            if (theBoard[i+1] == '*' || Character.isLowerCase(theBoard[i+1])) {
                theseMoves.add(i+1);}}
        if (g > 0) {
            if (theBoard[i-1] == '*' || Character.isLowerCase(theBoard[i-1])) {
                theseMoves.add(i-1);}}

        int k;
        for(int l=0; l<theseMoves.size();l++) {
            k = theseMoves.get(l);
            moveSquare = String.valueOf(theBoard[k]);
            theBoard[k] = 'K';
            whiteKing = k;
            theBoard[i] = '*';
            if (isKingSafe()) {
                String F = String.valueOf(i);
                String T = String.valueOf(k);
                if (i < 10) {
                    F = "0" + F;
                }
                if (k < 10) {
                    T = "0" + T;
                }
                list = list + "K" + F + T + moveSquare.charAt(0) + ",";
            }
            theBoard[k] = moveSquare.charAt(0);
            whiteKing = i;
            theBoard[i] = 'K';
        }
        // Need castle moves //

        if (theBoard[4] == 'K' && isKingSafe()) {
            if (theBoard[7] == 'R' && theBoard[5] == '*' && theBoard[6] == '*') {
                whiteKing = 5;
                if (isKingSafe()) {
                    whiteKing = 6;
                    if (isKingSafe()) {
                        list = list + "K-0-0R,";
                    } else { whiteKing = 4; }
                } else { whiteKing = 4; }}
            if (theBoard[0] == 'R' && theBoard[1] == '*' &&
                    theBoard[2] == '*' && theBoard[3] == '*') {
                whiteKing = 3;
                if (isKingSafe()) {
                    whiteKing = 2;
                    if (isKingSafe()) {
                        list = list + "K0-0-0,";
                    } else { whiteKing = 4; }
                } else { whiteKing = 4; }}}

        // Castle moves //
        return list;
    } // End King moves.

    public static String pawnMoves (int i) {
        String list = "";
        List<Integer> theseMoves = new ArrayList<Integer>();
        String moveSquare;
        int g = i%8;
        int h = i/8;

        int k = i + 8, j = i + 16;
        if (h == 1) {
            if (theBoard[k] == '*' && theBoard[j] == '*') {
                // The double step from the home row.
                theseMoves.add(j);}
        } else if (h == 4) {
            // The rule of en passant...
            /* // The rule of en passant...
            if (lastMove.charAt(0) == 'P') {
                int tempTo = Integer.parseInt(lastMove.substring(3, 5));
                int tempFm = Integer.parseInt(lastMove.substring(1, 3));
                if (tempFm / 8 == 1 && tempTo / 8 == 3) { // They did a double step.
                    if (tempTo == i + 1) { // They are on your right.
                        theBoard[i - 7] = 'p';
                        theBoard[i] = '*';
                        theBoard[i + 1] = '*';
                        if (isKingSafe()) {
                            list = list + "per" + String.valueOf(i - 7) + "P,";
                        }
                        theBoard[i + 1] = 'P';
                        theBoard[i - 9] = '*';
                        theBoard[i] = 'p';
                    } else if (tempTo == i - 1) { // They are on your left.
                        theBoard[i - 9] = 'p';
                        theBoard[i] = '*';
                        theBoard[i - 1] = '*';
                        if (isKingSafe()) {
                            list = list + "pel" + String.valueOf(i - 9) + "P,";
                        }
                        theBoard[i - 1] = 'P';
                        theBoard[i - 9] = '*';
                        theBoard[i] = 'p';
                    }
                }
            } // End en passant....*/

            if (lastMove.charAt(0)=='p') {
                int tempTo = Integer.parseInt(lastMove.substring(3,5));
                int tempFm = Integer.parseInt(lastMove.substring(1,3));
                if (tempFm / 8 == 6 && tempTo / 8 == 4) { // The did a double step.
                    if (tempTo == i + 1) { // They are on your right.
                        moveSquare = String.valueOf(theBoard[i+9]);
                        theBoard[i+9] = 'P';
                        theBoard[i] = '*';
                        theBoard[i + 1] = '*';
                        if (isKingSafe()) {
                            list = list + "PER" + String.valueOf(i + 9) + "p,";}
                        theBoard[i+9] = '*';
                        theBoard[i + 1] = 'p';
                        theBoard[i] = 'P';
                    } else if (tempTo == i - 1) { // They are on your left.
                        moveSquare = String.valueOf(theBoard[i+7]);
                        theBoard[i+7] = 'P';
                        theBoard[i] = '*';
                        theBoard[i - 1] = '*';
                        if (isKingSafe()) {
                            list = list + "PEL" + String.valueOf(i + 7) + "p,";}
                        theBoard[i+7] = '*';
                        theBoard[i - 1] = 'p';
                        theBoard[i] = 'P';
                    }} // End en passant....  // Temporary removal of en passant.
            }} else if (h == 6) {
            // The standard catch for moving one space forward.
            k = i + 8;
            if (theBoard[k] == '*') {
                moveSquare = String.valueOf(theBoard[k]);
                theBoard[k] = 'P';
                theBoard[i] = '*';
                if (isKingSafe()) {
                    list = list + "P" + "u" + promoteToW + k + moveSquare.charAt(0) + ",";}
                theBoard[k] = moveSquare.charAt(0);
                theBoard[i] = 'P';}
            k = i + 7;// Attacking to the left and up.
            if (g > 0 && Character.isLowerCase(theBoard[k])) {
                moveSquare = String.valueOf(theBoard[k]);
                theBoard[k] = 'P';
                theBoard[i] = '*';
                if (isKingSafe()) {
                    list = list + "P" + "l" + promoteToW + k + moveSquare.charAt(0) + ",";}
                theBoard[k] = moveSquare.charAt(0);
                theBoard[i] = 'P';}
            k = i + 9;// Attacking to the right and up.
            if (g < 7 && Character.isLowerCase(theBoard[k])) {
                moveSquare = String.valueOf(theBoard[k]);
                theBoard[k] = 'P';
                theBoard[i] = '*';
                if (isKingSafe()) {
                    list = list + "P" + "r" + promoteToW + k + moveSquare.charAt(0) + ",";}
                theBoard[k] = moveSquare.charAt(0);
                theBoard[i] = 'P';} // End Promotions.
        } // End special pawn moves.

        if (h > 0 && h < 6) {
            // The standard catch for moving one space forward.
            k = i + 8;
            if (theBoard[k] == '*') {
                theseMoves.add(k);}
            k = i + 7;// Attacking to the left and up.
            if (g > 0 && Character.isLowerCase(theBoard[k])) {
                theseMoves.add(k);}
            k = i + 9;// Attacking to the right and up.
            if (g < 7 && Character.isLowerCase(theBoard[k])) {
                theseMoves.add(k);}
        } // End boring pawn moves.

        for(int l=0; l<theseMoves.size();l++) {
            k = theseMoves.get(l);
            moveSquare = String.valueOf(theBoard[k]);
            theBoard[k] = 'P';
            theBoard[i] = '*';
            if (isKingSafe()) {
                String F = String.valueOf(i);
                String T = String.valueOf(k);
                if (i < 10) {
                    F = "0" + F;
                }
                if (k < 10) {
                    T = "0" + T;
                }
                list = list + "P" + F + T + moveSquare.charAt(0) + ",";
            }
            theBoard[k] = moveSquare.charAt(0);
            theBoard[i] = 'P';
        }
        return list;
    } // End pawn moves.

} // End The engine.

