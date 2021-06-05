package com.GameObjects.Ghosts;

import com.Board.BoardTile;
import com.Board.Neighbourhood;
import com.Board.NeighbourhoodType;
import com.GameLoop.GameLoop;
import com.Utility.Debug;
import com.Utility.GlobalReferenceManager;
import com.Utility.MoveDirection;
import com.Utility.Vector2;

import java.util.List;

public class InkyController extends GhostController {
    private long m_lastUpdate;
    private Vector2 m_startPoint = new Vector2(26 * 30, 29 * 30);
    private List<Vector2> m_stepsInScatter;

    public InkyController(GhostModeController ghostModeController, Ghost ghost) {
        super(ghostModeController, ghost);
    }

    @Override
    public void chaseMode() {
        if (System.nanoTime() - m_lastUpdate > 1000000000) {
            m_steps.clear();
            moveDirection = MoveDirection.None;
            int[][] boards = GameLoop.getInstance().gameBoard.BoardsPaths;
            Vector2 pointInFrontOfPacMan = find2DotsTowardsPacMan();
            Vector2 blinkyPosition = new Vector2((int) GlobalReferenceManager.blinky.getPosition().x / 30, (int) GlobalReferenceManager.blinky.getPosition().y / 30);
            Vector2 movementVector = new Vector2((int) ((pointInFrontOfPacMan.x - blinkyPosition.x) * 2), (int) ((pointInFrontOfPacMan.y - blinkyPosition.y) * 2));
            Vector2 positionToMove = new Vector2((int) (blinkyPosition.x + movementVector.x), (int) (blinkyPosition.y + movementVector.y));
            if (!isCellInRange((int) positionToMove.y, (int) positionToMove.x)) {
                positionToMove = findNearestInRange(positionToMove);
            }
            if (boards[(int) positionToMove.y][(int) positionToMove.x] != 1) {
                positionToMove = lookAtNeighbourhood(positionToMove);
            }
            Debug.Log("Inky x:" + positionToMove.x + " y:" + positionToMove.y);
            m_steps = findPathToPoint(positionToMove);
            Debug.Log("Inky size: "+m_steps.size());
            m_lastUpdate = System.nanoTime();
        }
    }

    @Override
    public void distractMode() {

    }

    @Override
    public void wanderingMode() {

    }

    private Vector2 findNearestInRange(Vector2 startPoint) {
        Vector2 nearestInRange = new Vector2();
        Vector2 point = new Vector2(startPoint.x, startPoint.y);
        while (!isCellInRange((int) point.y, (int) point.x)) {
            if (0 <= point.y && point.y < 31) {
                nearestInRange.y = point.y;
            } else {
                if (point.y < 0) {
                    point.y = point.y + 1;
                } else {
                    point.y = point.y - 1;
                }
            }
            if (0 <= point.x && point.x < 28) {
                nearestInRange.x = point.x;
            } else {
                if (point.x < 0) {
                    point.x = point.x + 1;
                } else {
                    point.x = point.x - 1;
                }
            }
        }
        return nearestInRange;
    }

    private Vector2 lookAtNeighbourhood(Vector2 point) {
        Vector2 newPoint = new Vector2();
        BoardTile ngbhOfPoints[][] = GameLoop.getInstance().gameBoard.Board;
        NeighbourhoodType ngbhType = NeighbourhoodType.None;
        while (!isOneInNeighbourhood(ngbhOfPoints[(int) point.y][(int) point.x], ngbhType)) {
            if(isCellInRange((int)point.y,(int)point.x+1)) {
                point.x = point.x + 1;
            }
            else{
                if(isCellInRange((int)point.y+1,(int)point.x)){
                    point.y= point.y+1;
                }
            }
        }
        newPoint = point;
        switch (ngbhType) {
            case Right: {
                newPoint.x = newPoint.x + 1;
                break;
            }
            case Left: {
                newPoint.x = newPoint.x - 1;
                break;
            }
            case Top: {
                newPoint.y = newPoint.y - 1;
                break;
            }
            case Bottom: {
                newPoint.y = newPoint.y + 1;
                break;
            }
        }
        return newPoint;
    }

    private boolean isOneInNeighbourhood(BoardTile cellToCheckNeighbourhood, NeighbourhoodType ngbhType) {
        Neighbourhood ngbh = cellToCheckNeighbourhood.getTilesAround();
        if (ngbh.m_left == 1) {
            ngbhType = NeighbourhoodType.Left;
            return true;
        }
        if (ngbh.m_bottom == 1) {
            ngbhType = NeighbourhoodType.Bottom;
            return true;
        }
        if (ngbh.m_top == 1) {
            ngbhType = NeighbourhoodType.Top;
            return true;
        }
        if (ngbh.m_right == 1) {
            ngbhType = NeighbourhoodType.Right;
            return true;
        }
        ngbhType = NeighbourhoodType.None;
        return false;
    }


}
