package com.GameObjects.Ghosts;

import com.GameLoop.GameLoop;
import com.Utility.GlobalReferenceManager;
import com.Utility.MoveDirection;
import com.Utility.Vector2;

import java.util.ArrayList;
import java.util.List;

public class PinkyController extends GhostController {
    private long m_lastUpdate;
    private Vector2 m_startPoint = new Vector2(26 * 30, 30);
    private List<Vector2> m_stepsInScatter;

    public PinkyController(GhostModeController ghostModeController, Ghost ghost) {
        super(ghostModeController, ghost);
        m_stepsInScatter = new ArrayList<Vector2>();
    }

    @Override
    public void chaseMode() {
        if (System.nanoTime() - m_lastUpdate > 1000000000) {
            m_steps.clear();
            moveDirection = MoveDirection.None;
            m_steps = findPathToPoint(find2DotsTowardsPacMan());
            m_lastUpdate = System.nanoTime();
        }
    }

    @Override
    public void distractMode() {

    }

    @Override
    public void wanderingMode() {

    }

    private Vector2 find2DotsTowardsPacMan() {
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
        return targetPoint;
    }

}
