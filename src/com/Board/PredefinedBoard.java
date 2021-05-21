package com.Board;

import java.io.*;
import java.util.Random;

public class PredefinedBoard {

    public BoardTile[][] Board = new BoardTile[31][28];
    public int[][] BoardsPaths = new int[31][28];

    /**
     * Metoda losująca nazwę pliku zawierającego wygląd planszy
     * @return zwraca nazwę pliku zwierającego wygląd planszy
     */
    public static String randFile() {
        //Random r = new Random();
        //int number = r.NextInt(3)+1;
        String fileName = "Boards/board_1.txt";
        return fileName;
    }

    /**
     * Metoda pobierająca wygląd planszy z pliku o nazwie fileName
     * @param fileName nazwa pliku zawierającego planszę
     */
    public void loadFromFile(String fileName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            for (int i = 0; i < 31; i++) {
                String currentLine = reader.readLine();
                if (currentLine != null) {
                    String[] currentSplit = currentLine.split(" ");
                    for (int j = 0; j < 28; j++) {
                        BoardsPaths[i][j] = Integer.parseInt(currentSplit[j]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //System.out.print(BoardsPaths);
        makeUpBoardTileArray();
    }

    /**
     *  Metoda uzupełniająca tablicę sąsiadów każdego pola na potrzeby tworzenia właściwych ścieżek na mapie
     */
    private void makeUpBoardTileArray() {
        for (int i = 0; i < Board.length; i++) {
            for (int j = 0; j < Board[i].length; j++) {
                int top = 0;
                int left = 0;
                int bottom = 0;
                int right = 0;
                if (i == 0 || j == 0 || i == (Board.length - 1) || j == (Board[i].length - 1)) {
                    if (i == 0 && j == 0) { //lewy górny rarożnik
                        top = -1;
                        left = -1;
                        bottom = BoardsPaths[i + 1][j];
                        right = BoardsPaths[i][j + 1];
                    } else if (j == 0 && i == (Board.length - 1)) { //lewy dolny narożnik
                        left = -1;
                        bottom = -1;
                        right = BoardsPaths[i][j + 1];
                        top = BoardsPaths[i - 1][j];
                    } else if (i == 0 && j == (Board[i].length - 1)) { //prawy górny narożnik
                        top = -1;
                        right = -1;
                        left = BoardsPaths[i][j - 1];
                        bottom = BoardsPaths[i + 1][j];
                    } else if (i == (Board.length - 1) && j == (Board[i].length - 1)) { //prawy dolny narożnik
                        right = -1;
                        bottom = -1;
                        top = BoardsPaths[i - 1][j];
                        left = BoardsPaths[i][j - 1];
                    } else if (j == 0) { //lewy bok
                        left = -1;
                        top = BoardsPaths[i - 1][j];
                        bottom = BoardsPaths[i + 1][j];
                        right = BoardsPaths[i][j + 1];
                    } else if (i == 0) { //górny bok
                        top = -1;
                        left = BoardsPaths[i][j - 1];
                        bottom = BoardsPaths[i + 1][j];
                        right = BoardsPaths[i][j + 1];
                    } else if (i == (Board.length - 1) ) { //dolny bok
                        bottom = -1;
                        left = BoardsPaths[i][j - 1];
                        top = BoardsPaths[i -1][j];
                        right = BoardsPaths[i][j + 1];
                    } else if( j == (Board[i].length-1)) //prawy bok
                    {
                        right = -1;
                        left = BoardsPaths[i][j - 1];
                        top = BoardsPaths[i -1][j];
                        bottom = BoardsPaths[i+1][j];
                    }
                } else {
                    top = BoardsPaths[i - 1][j];
                    bottom = BoardsPaths[i + 1][j];
                    left = BoardsPaths[i][j - 1];
                    right = BoardsPaths[i][j + 1];
                }
                Board[i][j] = new BoardTile();
                Board[i][j].setTilesAround(top, left, bottom, right);
            }
        }
    }
}
