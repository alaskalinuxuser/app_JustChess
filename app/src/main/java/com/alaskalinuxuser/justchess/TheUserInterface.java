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

import static com.alaskalinuxuser.justchess.MainActivity.chessImage;
import static com.alaskalinuxuser.justchess.TheEngine.theBoard;

public class TheUserInterface {

    public static void drawBoardPieces() {

        for (int i=0; i<64; i++) {
            chessImage[i].setBackgroundResource(R.drawable.dark);
            if (i==1 || i==3 || i==5 || i==7 ||
                    i==8 || i==10 || i==12 || i==14 ||
                    i==17 || i==19 || i==21 || i==23 ||
                    i==24 || i==26 || i==28 || i==30 ||
                    i==33 || i==35 || i==37 || i==39 ||
                    i==40 || i==42 || i==44 || i==46 ||
                    i==49 || i==51 || i==53 || i==55 ||
                    i==56 || i==58 || i==60 || i==62) {
                chessImage[i].setBackgroundResource(R.drawable.light);
            }

            switch (theBoard[i]) {
                case '*': chessImage[i].setImageResource(R.drawable.empty);
                    break;
                case 'P': chessImage[i].setImageResource(R.drawable.wp);
                    break;
                case 'R': chessImage[i].setImageResource(R.drawable.wr);;
                    break;
                case 'N': chessImage[i].setImageResource(R.drawable.wn);;
                    break;
                case 'B': chessImage[i].setImageResource(R.drawable.wb);;
                    break;
                case 'Q': chessImage[i].setImageResource(R.drawable.wq);;
                    break;
                case 'K': chessImage[i].setImageResource(R.drawable.wk);;
                    break;
                case 'p': chessImage[i].setImageResource(R.drawable.bp);
                    break;
                case 'r': chessImage[i].setImageResource(R.drawable.br);;
                    break;
                case 'n': chessImage[i].setImageResource(R.drawable.bn);;
                    break;
                case 'b': chessImage[i].setImageResource(R.drawable.bb);;
                    break;
                case 'q': chessImage[i].setImageResource(R.drawable.bq);;
                    break;
                case 'k': chessImage[i].setImageResource(R.drawable.bk);;
                    break;
            }
        } //Done for loop for drawing board.

    } // End draw board pieces

}

