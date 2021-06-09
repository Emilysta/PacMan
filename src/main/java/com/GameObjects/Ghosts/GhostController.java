package com.GameObjects.Ghosts;

import com.GameLoop.GameLoop;
import com.Utility.*;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Klasa bazowa dla kontrolerów poszczególnych duchów, klasa działa jako osobny
 * wątek i sprawdza ruchy do wykonania
 */
public abstract class GhostController implements Runnable {
    public AtomicBoolean shouldThreadExit = new AtomicBoolean();
    public MoveDirection moveDirection = MoveDirection.None;
    protected long m_lastUpdate;
    protected Ghost m_ghost;
    protected List<Vector2> m_steps;
    private int randMin = 4;
    private int randMax = 6;
    private GhostModeController m_ghostModeController;
    private GhostMode m_GhostMode;
    private boolean isGoingHome = false;
    private Vector2 m_lastPosition = new Vector2();

    public GhostController(GhostModeController ghostModeController, Ghost ghost) {
        m_ghostModeController = ghostModeController;
        m_ghost = ghost;
        m_steps = new ArrayList<Vector2>();
    }

    /**
     * Metoda wywoływana wraz z uruchomieniem wątku, metoda sprawdza, w jakim trybie
     * znajdyje się duch i w zależności od tego wywyołuje odpowiednią metodę
     * znajdującą ścieżkę. Dodatkowo w zależności od trybu ustawia odpowiednie
     * zdjęcie do renderowania dla odpowiedniego ducha
     */
    @Override
    public void run() {
        try {
            while (true) {
                if (shouldThreadExit.get())
                    return;

                m_GhostMode = m_ghostModeController.ghostMode;
                String whichGhost = "";

                switch (m_ghost.m_ghostType) {
                    case Blinky:
                        whichGhost = "/red.png";
                        break;
                    case Inky:
                        whichGhost = "/blue.png";
                        break;
                    case Clyde:
                        whichGhost = "/orange.png";
                        break;
                    case Pinky:
                        whichGhost = "/pink.png";
                        break;
                }
                if ((m_ghost.getPosition().x % 30 == 0 && m_ghost.getPosition().y % 30 == 0 && !m_ghost.getPosition().equals(m_lastPosition))||m_steps.size()==0) {
                    m_lastPosition = new Vector2((int)m_ghost.getPosition().x,(int)m_ghost.getPosition().y);
                    m_ghost.destination=new Vector2((int)m_ghost.getPosition().x/30,(int)m_ghost.getPosition().y/30);
                    switch (m_GhostMode) {
                        case WanderingMode: {
                            m_ghost.setSprite(new Sprite(new Image("/wandering.png"), 30, 30));
                            wanderingMode();
                            break;
                        }
                        case ChaseMode: {
                            m_ghost.setSprite(new Sprite(new Image(whichGhost), 30, 30));
                            chaseMode();
                            break;
                        }
                        case DeadMode: {
                            m_ghost.setSprite(new Sprite(new Image("/dead.png"), 30, 30));
                            deadMode();
                            break;
                        }
                    }
                }

                if (m_ghost.getPosition().equals(m_ghost.homePosition.multiply(30)) && isGoingHome) {
                    Debug.Log("Jestem w domku");
                    isGoingHome = false;
                    moveDirection = MoveDirection.None;

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        shouldThreadExit.set(true);
                    }

                }

                if (m_steps.size() != 0) {
                    Vector2 positionToReach = m_steps.get(m_steps.size() - 1);
                    m_ghost.destination = new Vector2(positionToReach.x, positionToReach.y);
                    Vector2 myPosition = new Vector2(m_ghost.getPosition().x, m_ghost.getPosition().y);

                    if (myPosition.equals(positionToReach.multiply(30))) {
                        m_steps.remove(m_steps.size() - 1);
                        moveDirection = MoveDirection.None;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda do nadpisania w klasach dziedziczących, metoda wywoływana w momencie
     * gdy duch jest w trybie Chase.
     */
    public abstract void chaseMode();

    /**
     * Metoda określająca ruchy ducha w trybie wędrowania - gdy Pac-Man jest w
     * trybie Power-Up.
     */
    public void wanderingMode() {
        if (System.nanoTime() - m_lastUpdate > 2000000000) {
            m_steps.clear();
            moveDirection = MoveDirection.None;
            Vector2 randomPoint = randPointInRange();
            m_steps = findPathToPoint(randomPoint);
            m_lastUpdate = System.nanoTime();
        }
    }

    /**
     * Metoda określająca ruchy ducha, gdy umarł, wyszukuje najkrótszą ścieżkę do
     * domu.
     */
    public void deadMode() {
        if (!isGoingHome) {
            moveDirection = MoveDirection.None;
            m_steps = findPathToPoint(m_ghost.homePosition);
            isGoingHome = true;
        }
    }

    /**
     * Metoda określająca czy wartości x i y zawierają się w rozmiarze planszy
     *
     * @return true - jeśli wartości są w zakresie, false w przeciwnym razie
     */
    public boolean isCellInRange(int x, int y) {
        if ((x >= 0) && (x < 31) && (y >= 0) && (y < 28))
            return true;
        return false;
    }

    /**
     * Metoda znajdująca najkrótszą ścieżkę do danego punktu na mapie, z miejsca, w
     * którym duch się znajduje
     *
     * @return zwraca listę punktów do, których Pac-Man ma się przemieścić
     */
    public List<Vector2> findPathToPoint(Vector2 point) {
        if (point == null)
            return new ArrayList<>();
        Vector2 targetPosition = new Vector2((int) point.x, (int) point.y);
        Vector2 myPosition = new Vector2((int) m_ghost.getPosition().x / 30, (int) m_ghost.getPosition().y / 30);
        // Debug.LogError(myPosition + "->"+targetPosition);
        if (targetPosition.equals(myPosition))
            return new ArrayList<>();
        int[][] boardsPaths = GameLoop.getInstance().gameBoard.BoardsPaths;
        if (boardsPaths[(int) myPosition.y][(int) myPosition.x] != 1
                || boardsPaths[(int) targetPosition.y][(int) targetPosition.x] != 1)
            return new ArrayList<>();
        boolean[][] visited = new boolean[31][28];
        visited[(int) myPosition.y][(int) myPosition.x] = true;
        Queue<queueNode> queue = new LinkedList<>();
        queueNode start = new queueNode(myPosition, 0);
        queue.add(start);
        Vector2[] neighbourhood = {new Vector2(0, -1), new Vector2(-1, 0), new Vector2(0, 1), new Vector2(1, 0)};
        int[][] distances = new int[31][28];
        while (!queue.isEmpty()) {
            queueNode current = queue.remove();
            Vector2 currentPoint = current.point;

            if (currentPoint.x == targetPosition.x && currentPoint.y == targetPosition.y) {
                List<Vector2> steps2 = new ArrayList<Vector2>();
                steps2.add(targetPosition);
                int x = (int) targetPosition.x;
                int y = (int) targetPosition.y;
                int tempDistance = current.dist - 1;
                while (tempDistance != 0) {
                    if (shouldThreadExit.get())
                        return null;
                    for (int k = 0; k < 4; k++) {
                        int temp_x = x + (int) neighbourhood[k].x;
                        int temp_y = y + (int) neighbourhood[k].y;
                        if (distances[temp_y][temp_x] == tempDistance) {
                            x = x + (int) neighbourhood[k].x;
                            y = y + (int) neighbourhood[k].y;
                            steps2.add(new Vector2(x, y));
                            tempDistance--;
                            break;
                        }
                    }
                }
                steps2.add(new Vector2((int) m_ghost.getPosition().x / 30, (int) m_ghost.getPosition().y / 30));
                return steps2;
            }

            for (int i = 0; i < 4; i++) {
                int row = (int) currentPoint.y + (int) neighbourhood[i].y;
                int col = (int) currentPoint.x + (int) neighbourhood[i].x;

                if (isCellInRange(row, col) && boardsPaths[row][col] == 1 && !visited[row][col]) {
                    // mark cell as visited and enqueue it
                    visited[row][col] = true;
                    queueNode adjacentPoint = new queueNode(new Vector2(col, row), current.dist + 1);
                    queue.add(adjacentPoint);
                    distances[row][col] = current.dist + 1;
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * Metoda znajdująca współrzędne punktu znajdującego się przed Pac-Manem
     *
     * @return współrzędne punktu znajdującego się przed Pac-Manem
     */
    protected Vector2 find2DotsTowardsPacMan() {
        Vector2 targetPoint = new Vector2();
        MoveDirection pacManMoveDirection = GlobalReferenceManager.pacMan.getMoveDirection();
        Vector2 pacManPosition = new Vector2((int) GlobalReferenceManager.pacMan.getPosition().x / 30,
                (int) GlobalReferenceManager.pacMan.getPosition().y / 30);

        int[][] board = GameLoop.getInstance().gameBoard.BoardsPaths;
        Vector2 moveDirectionInBoard = new Vector2();
        switch (pacManMoveDirection) {
            case Up: {
                moveDirectionInBoard = new Vector2(0, -1);
            }
            case Down: {
                moveDirectionInBoard = new Vector2(0, 1);
            }
            case Left: {
                moveDirectionInBoard = new Vector2(-1, 0);
            }
            case Right: {
                moveDirectionInBoard = new Vector2(1, 0);
            }
            case None: {
                moveDirectionInBoard = new Vector2(0, 0);
            }
        }
        int positionToMoveX2 = (int) (pacManPosition.y + moveDirectionInBoard.y * 2);
        int positionToMoveY2 = (int) (pacManPosition.x + moveDirectionInBoard.x * 2);
        int positionToMoveX = (int) (pacManPosition.y + moveDirectionInBoard.y);
        int positionToMoveY = (int) (pacManPosition.x + moveDirectionInBoard.x);

        boolean is2InRange = isCellInRange(positionToMoveX2, positionToMoveY2);
        boolean isInRange = isCellInRange(positionToMoveX, positionToMoveY);
        targetPoint = pacManPosition;
        if (isInRange) {
            if (board[positionToMoveX][positionToMoveY] == 1)
                targetPoint = new Vector2(positionToMoveY, positionToMoveX);
        }
        if (is2InRange) {
            if (board[positionToMoveX2][positionToMoveY2] == 1)
                targetPoint = new Vector2(positionToMoveY, positionToMoveX);
        }
        // Debug.Log("Pinky target x: "+targetPoint.x + "y: "+targetPoint.y);
        return targetPoint;
    }

    /**
     * Metoda losująca współrzędne punktu znajdującego w danym zakresie/okręgu
     *
     * @return współrzędne wylosowanego punktu
     */
    protected Vector2 randPointInRange() {
        Vector2 newPoint = new Vector2();
        List<Vector2> localOnes = GameLoop.getInstance().gameBoard.onesList;
        int sizeOfOnesList = localOnes.size();
        int index = ThreadLocalRandom.current().nextInt(0, sizeOfOnesList);
        int x = (int) localOnes.get(index).x;
        int y = (int) localOnes.get(index).y;
        newPoint.x = y;
        newPoint.y = x;
        return newPoint;
    }

    /**
     * Klasa pomocnicza w znajdywaniu najkrótszej ścieżki
     */
    static class queueNode {
        Vector2 point;
        int dist;

        public queueNode(Vector2 point, int dist) {
            this.point = point;
            this.dist = dist;
        }
    }
}
