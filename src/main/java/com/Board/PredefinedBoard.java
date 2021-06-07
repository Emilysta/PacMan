package com.Board;

import com.Utility.Vector2;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa pozwalająca wczytać predefiniowane plansze do gry
 */
public class PredefinedBoard {

    /**
     * Tablica określająca sąsiadów konkrentego pola
     */
    public BoardTile[][] Board = new BoardTile[31][28];
    /**
     * Tablica określająca położenie ścieżek, którymi pac-man oraz duszki mogą się poruszać
     * Zawiera 0, gdy pole jest przestrzenią zabronioną
     * Zawiera 1, gdy pole jest ścieżką
     */
    public int[][] BoardsPaths = new int[31][28]; //with 0,1
    /**
     * Tablica pomocnicza, pozwala określić jaki typ drogi jest potrzebny do wczytania ze zdjęcia
     * Zawiera komórki typu: RoadTypes.
     */
    public PathTypes[][] BoardPathTypes = new PathTypes[31][28];

    /**
     * Lista indeksów miejsc, w któych tablica BoardPaths zawiera jedynki
     */
    public List<Vector2> onesList = new ArrayList<Vector2>();

    /**
     * Metoda losująca nazwę pliku zawierającego wygląd planszy
     *
     * @return zwraca nazwę pliku zwierającego wygląd planszy
     */
    public static String randFile() {
        //Random r = new Random();
        //int number = r.NextInt(3)+1;
        String fileName = "/boards/board_1.txt";
        return fileName;
    }

    /**
     * Metoda pobierająca wygląd planszy z pliku o nazwie fileName
     *
     * @param fileName nazwa pliku zawierającego planszę
     */
    public void loadFromFile(String fileName) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(fileName)));
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
        setPathTypes();
        setUpListOfOnes();
    }

    /**
     * Metoda uzupełniająca tablicę sąsiadów każdego pola na potrzeby tworzenia właściwych ścieżek na mapie
     */
    private void makeUpBoardTileArray() {
        for (int i = 0; i < Board.length; i++) {
            for (int j = 0; j < Board[i].length; j++) {
                int top = 0;
                int left = 0;
                int bottom = 0;
                int right = 0;
                if (BoardsPaths[i][j] != 0) {
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
                        } else if (i == (Board.length - 1)) { //dolny bok
                            bottom = -1;
                            left = BoardsPaths[i][j - 1];
                            top = BoardsPaths[i - 1][j];
                            right = BoardsPaths[i][j + 1];
                        } else if (j == (Board[i].length - 1)) //prawy bok
                        {
                            right = -1;
                            left = BoardsPaths[i][j - 1];
                            top = BoardsPaths[i - 1][j];
                            bottom = BoardsPaths[i + 1][j];
                        }
                    } else {
                        top = BoardsPaths[i - 1][j];
                        bottom = BoardsPaths[i + 1][j];
                        left = BoardsPaths[i][j - 1];
                        right = BoardsPaths[i][j + 1];
                    }
                } else {
                    top = -1;
                    bottom = -1;
                    left = -1;
                    right = -1;
                }
                Board[i][j] = new BoardTile();
                Board[i][j].setTilesAround(top, left, bottom, right);
            }
        }
    }

    public void setPathTypes() {
        for (int i = 0; i < Board.length; i++) {
            for (int j = 0; j < Board[i].length; j++) {
                int top = -1;
                int left = -1;
                int bottom = -1;
                int right = -1;
                Neighbourhood ngbh = Board[i][j].getTilesAround();
                top = ngbh.m_top;
                left = ngbh.m_left;
                bottom = ngbh.m_bottom;
                right = ngbh.m_right;
                if (top == -1 || left == -1 || bottom == -1 || right == -1) {
                    BoardPathTypes[i][j] = PathTypes.EMPTY;
                } else if (top == 1 && left == 1 && bottom == 1 && right == 1) {
                    BoardPathTypes[i][j] = PathTypes.JUNCTION;
                } else if (top == 1 && left == 1 && right == 0 && bottom == 0) {
                    BoardPathTypes[i][j] = PathTypes.TOP_LEFT;
                } else if (top == 1 && left == 0 && right == 0 && bottom == 1) {
                    BoardPathTypes[i][j] = PathTypes.VERTICAL;
                } else if (top == 0 && left == 1 && right == 1 && bottom == 0) {
                    BoardPathTypes[i][j] = PathTypes.HORIZONTAL;
                } else if (top == 1 && right == 1 && left == 0 && bottom == 0) {
                    BoardPathTypes[i][j] = PathTypes.TOP_RIGHT;
                } else if (bottom == 1 && left == 1 && right == 0 && top == 0) {
                    BoardPathTypes[i][j] = PathTypes.LEFT_BOTTOM;
                } else if (bottom == 1 && right == 1 && left == 0 && top == 0) {
                    BoardPathTypes[i][j] = PathTypes.RIGHT_BOTTOM;
                } else if (bottom == 1 && right == 1 && left == 1 && top == 0) {
                    BoardPathTypes[i][j] = PathTypes.LEFT_BOTTOM_RIGHT;
                } else if (bottom == 0 && right == 1 && left == 1 && top == 1) {
                    BoardPathTypes[i][j] = PathTypes.LEFT_TOP_RIGHT;
                } else if (bottom == 1 && right == 0 && left == 1 && top == 1) {
                    BoardPathTypes[i][j] = PathTypes.TOP_LEFT_BOTTOM;
                } else if (bottom == 1 && right == 1 && left == 0 && top == 1) {
                    BoardPathTypes[i][j] = PathTypes.TOP_RIGHT_BOTTOM;
                }
            }
        }
    }


    /**
     * Metoda uzupełniaąca listę wystepowania "jedynek" na mapie
     */
    private void setUpListOfOnes(){
        for(int i = 0; i<31;i++){
            for(int j =0;j<28;j++){
                if(BoardsPaths[i][j]==1)
                    onesList.add(new Vector2(i,j));
            }
        }
    }

}
