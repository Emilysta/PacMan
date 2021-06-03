package com.GameObjects.Ghosts;

import com.GameLoop.GameLoop;
import com.Utility.MoveDirection;
import com.Utility.Vector2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Klasa - rodzic - dla poszczególnych duchów, klasa działa jako osobny wątek i sprawdza ruchy duchów do wykonanania
 */
public abstract class GhostController implements Runnable {
    private GhostModeController m_ghostModeController;
    private GhostMode m_GhostMode;
    private Ghost m_ghost;
    public AtomicBoolean shouldThreadExit = new AtomicBoolean();
    private List<Vector2> m_steps;
    public MoveDirection moveDirection = MoveDirection.None;

    public GhostController(GhostModeController ghostModeController, Ghost ghost) {
        m_ghostModeController = ghostModeController;
        m_ghost = ghost;
        m_steps = new ArrayList<Vector2>();
    }

    @Override
    public void run() {
        while (true) {
            if (shouldThreadExit.get())
                return;
            //m_GhostMode = m_ghostModeController.ghostMode; //toDo podmiana na to
            m_GhostMode = GhostMode.DeadMode;
            if (m_GhostMode != GhostMode.DeadMode) {
                switch (m_GhostMode) {
                    case WanderingMode:
                        wanderingMode();
                    case DistractMode:
                        distractMode(); //toDo to check
                    case ChaseMode:
                        chaseMode();
                }
            } else {
                if (m_steps.size() == 0)
                    deadMode();
                else {
                    Vector2 positionToReach = m_steps.get(m_steps.size() - 1);
                    Vector2 myPosition = new Vector2(m_ghost.getPosition().x,m_ghost.getPosition().y);
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
    }

    public abstract void chaseMode();

    public abstract void distractMode();

    public abstract void wanderingMode();

    public void deadMode() {
        m_steps = findPathToHome();
    }

    static class queueNode {
        Vector2 point;
        int dist;

        public queueNode(Vector2 point, int dist) {
            this.point = point;
            this.dist = dist;
        }
    }

    ;

    public boolean isCellInRange(int x, int y) {
        if ((x >= 0) && (x < 31) && (y >= 0) && (y < 28))
            return true;
        return false;
    }

    public List<Vector2> findPathToHome() {
        Vector2 homePosition = new Vector2(m_ghost.homePosition.x / 30, m_ghost.homePosition.y / 30);
        Vector2 myPosition = new Vector2(m_ghost.getPosition().x / 30, m_ghost.getPosition().y / 30);
        int[][] boardsPaths = GameLoop.getInstance().gameBoard.BoardsPaths;
        if (boardsPaths[(int) myPosition.y][(int) myPosition.x] != 1 ||
                boardsPaths[(int) homePosition.y][(int) homePosition.x] != 1)
            return null;
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
                            x = x + (int)neighbourhood[k].x;
                            y = y + (int)neighbourhood[k].y;
                            steps2.add(new Vector2(x,y));
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

                if (isCellInRange(row, col) &&
                        boardsPaths[row][col] == 1 &&
                        !visited[row][col]) {
                    // mark cell as visited and enqueue it
                    visited[row][col] = true;
                    queueNode adjacentPoint = new queueNode
                            (new Vector2(col,row),
                                    current.dist + 1);
                    queue.add(adjacentPoint);
                    distances[row][col] = current.dist + 1;
                }
            }
        }
        return null;
    }
}
