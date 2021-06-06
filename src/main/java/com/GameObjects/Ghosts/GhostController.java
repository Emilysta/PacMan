package com.GameObjects.Ghosts;

import com.GameLoop.GameLoop;
import com.Utility.Debug;
import com.Utility.GlobalReferenceManager;
import com.Utility.MoveDirection;
import com.Utility.Vector2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Klasa - rodzic - dla poszczególnych duchów, klasa działa jako osobny wątek i
 * sprawdza ruchy duchów do wykonanania
 */
public abstract class GhostController implements Runnable {
    private int randMin = 4;
    private int randMax = 6;
    private GhostModeController m_ghostModeController;
    private GhostMode m_GhostMode;
    private boolean isGoingHome = false;
    protected long m_lastUpdate;
    protected Ghost m_ghost;
    protected List<Vector2> m_steps;
    public AtomicBoolean shouldThreadExit = new AtomicBoolean();
    public MoveDirection moveDirection = MoveDirection.None;

    public GhostController(GhostModeController ghostModeController, Ghost ghost) {
        m_ghostModeController = ghostModeController;
        m_ghost = ghost;
        m_steps = new ArrayList<Vector2>();
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (shouldThreadExit.get())
                    return;
                //m_GhostMode = m_ghostModeController.ghostMode; //toDo podmiana na to
                m_GhostMode = GhostMode.ChaseMode;

                switch (m_GhostMode) {
                    case WanderingMode:
                        wanderingMode();
                        break;
                    case DistractMode:
                        distractMode();
                        break;
                    // toDo to check
                    case ChaseMode:
                        chaseMode();
                        break;
                    case DeadMode:
                        deadMode();
                        break;
                }

                if (m_ghost.getPosition().equals(m_ghost.homePosition)) {
                    Debug.Log("Jestem w domku");
                    isGoingHome = false;
                    moveDirection = MoveDirection.None;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        shouldThreadExit.set(true);
                    }
                } else if (m_steps.size() == 0)
                    ;//deadMode();
                else {
                    Vector2 positionToReach = m_steps.get(m_steps.size() - 1);
                    Vector2 myPosition = new Vector2(m_ghost.getPosition().x, m_ghost.getPosition().y);
                    if (myPosition.x == positionToReach.x * 30 && myPosition.y == positionToReach.y * 30) {
                        m_steps.remove(m_steps.size() - 1);
                        moveDirection = MoveDirection.None;
                    }
                    if (m_steps.size() != 0) {

                        Vector2 move = m_steps.get(m_steps.size() - 1);
                        myPosition.x = myPosition.x / 30;
                        myPosition.y = myPosition.y / 30;

                        if (myPosition.y != move.y) {
                            if (myPosition.y < move.y)
                                moveDirection = MoveDirection.Down;
                            else
                                moveDirection = MoveDirection.Up;
                        }
                        if (myPosition.x != move.x) {
                            if (myPosition.x < move.x)
                                moveDirection = MoveDirection.Right;
                            else
                                moveDirection = MoveDirection.Left;
                        }
                    }
                }

            }
        }

        
        catch(Exception e){
            e.printStackTrace();
        }
}

    public abstract void chaseMode();

    public abstract void distractMode();

    public void wanderingMode(){
        if(shouldThreadExit.get())
            return;
        if (System.nanoTime() - m_lastUpdate > 1000000000) {
            m_steps.clear();
            moveDirection = MoveDirection.None;
            Vector2 randomPoint = randPointInRange();
            m_steps = findPathToPoint(randomPoint);
            m_lastUpdate = System.nanoTime();
        }
    }

    public void deadMode() {
        if(!isGoingHome) {
            m_steps = findPathToHome();
            isGoingHome = true;
        }
    }

    static class queueNode {
        Vector2 point;
        int dist;

        public queueNode(Vector2 point, int dist) {
            this.point = point;
            this.dist = dist;
        }
    }

    public boolean isCellInRange(int x, int y) {
        if ((x >= 0) && (x < 31) && (y >= 0) && (y < 28))
            return true;
        return false;
    }

    public List<Vector2> findPathToHome() {
        Vector2 homePosition = new Vector2(m_ghost.homePosition.x, m_ghost.homePosition.y);
        Vector2 myPosition = new Vector2(m_ghost.getPosition().x / 30, m_ghost.getPosition().y / 30);
        if (homePosition == myPosition)
            return null;
        int[][] boardsPaths = GameLoop.getInstance().gameBoard.BoardsPaths;
        if (boardsPaths[(int) myPosition.y][(int) myPosition.x] != 1
                || boardsPaths[(int) homePosition.y][(int) homePosition.x] != 1)
            return null;
        boolean[][] visited = new boolean[31][28];
        visited[(int) myPosition.y][(int) myPosition.x] = true;
        Queue<queueNode> queue = new LinkedList<>();
        queueNode start = new queueNode(myPosition, 0);
        queue.add(start);
        Vector2[] neighbourhood = { new Vector2(0, -1), new Vector2(-1, 0), new Vector2(0, 1), new Vector2(1, 0) };
        int[][] distances = new int[31][28];
        while (!queue.isEmpty()) {
            queueNode current = queue.remove();
            Vector2 currentPoint = current.point;

            if (currentPoint.x == homePosition.x && currentPoint.y == homePosition.y) {
                List<Vector2> steps2 = new ArrayList<Vector2>();
                steps2.add(homePosition);
                int x = (int) homePosition.x;
                int y = (int) homePosition.y;
                int tempDistance = current.dist - 1;
                while (tempDistance != 0) {
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
        return null;
    }

    public List<Vector2> findPathToPoint(Vector2 point) {
        Vector2 targetPosition = new Vector2((int)point.x, (int)point.y);
        Vector2 myPosition = new Vector2((int)m_ghost.getPosition().x / 30, (int)m_ghost.getPosition().y / 30);
        //Debug.LogError(myPosition + "->"+targetPosition);
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
        Vector2[] neighbourhood = { new Vector2(0, -1), new Vector2(-1, 0), new Vector2(0, 1), new Vector2(1, 0) };
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
                    if(shouldThreadExit.get())
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

    protected Vector2 find2DotsTowardsPacMan() {
        Vector2 targetPoint = new Vector2();
        MoveDirection pacManMoveDirection = GlobalReferenceManager.pacMan.getMoveDirection();
        Vector2 pacManPosition = new Vector2((int) GlobalReferenceManager.pacMan.getPosition().x / 30, (int) GlobalReferenceManager.pacMan.getPosition().y / 30);

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
        //Debug.Log("Pinky target x: "+targetPoint.x + "y: "+targetPoint.y);
        return targetPoint;
    }
    protected Vector2 randPointInRange(){
        Vector2 newPoint =  new Vector2();
        Vector2 myPosition = new Vector2((int)m_ghost.getPosition().x/30,(int)m_ghost.getPosition().y/30);
        int myPositionX = (int)myPosition.y;
        int myPositionY = (int)myPosition.x;
        int board[][] = GameLoop.getInstance().gameBoard.BoardsPaths;
        //(x-a)^2 + (y-b)^2 = r^2 gdzie (a,b) to środek - pozycja ducha i r to promień
        List<Vector2> localOnes = GameLoop.getInstance().gameBoard.onesList;
        int sizeOfOnesList = localOnes.size();
        int index = ThreadLocalRandom.current().nextInt(0, sizeOfOnesList);
        int x = (int)localOnes.get(index).x;
        int y = (int)localOnes.get(index).y;
        while(((Math.pow(x - myPositionX,2)+Math.pow(y - myPositionY,2))>=Math.pow(randMax,2)) &&
                ((Math.pow(x - myPositionX,2)+Math.pow(y - myPositionY,2))<=Math.pow(randMin,2)) && x==myPositionX && y==myPositionY ){
            if(shouldThreadExit.get())
                return null;
            index = ThreadLocalRandom.current().nextInt(0, sizeOfOnesList);
            x = (int)localOnes.get(index).x;
            y = (int)localOnes.get(index).y;
        }
        newPoint.x = y;
        newPoint.y = x;

        return newPoint;
    }
}
