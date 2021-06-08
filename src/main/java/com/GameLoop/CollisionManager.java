package com.GameLoop;

import com.Utility.GlobalReferenceManager;
import com.Utility.MoveDirection;
import com.Utility.Vector2;

import static java.lang.Math.abs;

/**
 * Klasa odpowiada za sprawdzanie kolizji obiektów ze ścianami oraz z samymi
 * sobą na scenie.
 */
public class CollisionManager {
    /**
     * Metoda sprawdza czy dany ruch jest możliwy do wykonania na scenie
     * @param position - Vector2 pozycji na ekranie
     * @param moveDirection - Kierunek ruchu w którym sprawdzany jest ruch
     * @return true jeśli można się ruszyć, inaczej false
     */
    public static boolean checkIfMovePossible(Vector2 position, MoveDirection moveDirection) {
        int gridX = (int) position.x / 30;
        int gridY = (int) position.y / 30;
        //Debug.Log("X:" + position.x + "/" + gridX + ", Y:" + position.y + "/" + gridY);
        boolean isTop = (gridY == 0);
        boolean isBottom = (gridY == 30);
        boolean isRight = (gridX == 27);
        boolean isLeft = (gridX == 0);

        if (isTop && moveDirection == MoveDirection.Up) return false;
        if (isBottom && moveDirection == MoveDirection.Down) return false;
        if (isRight && moveDirection == MoveDirection.Right) return false;
        if (isLeft && moveDirection == MoveDirection.Left) return false;

        if (moveDirection == MoveDirection.Up && position.y > gridY * 30)
            return true;
        if (moveDirection == MoveDirection.Down && position.y < gridY * 30)
            return true;
        if (moveDirection == MoveDirection.Left && position.x > gridX * 30)
            return true;
        if (moveDirection == MoveDirection.Right && position.x < gridX * 30)
            return true;

        if (moveDirection == MoveDirection.Up && GameLoop.getInstance().gameBoard.BoardsPaths[gridY - 1][gridX] == 1)
            return position.x % 30 < 1;
        if (moveDirection == MoveDirection.Down && GameLoop.getInstance().gameBoard.BoardsPaths[gridY + 1][gridX] == 1)
            return position.x % 30 < 1;
        if (moveDirection == MoveDirection.Right && GameLoop.getInstance().gameBoard.BoardsPaths[gridY][gridX + 1] == 1)
            return position.y % 30 < 1;
        if (moveDirection == MoveDirection.Left && GameLoop.getInstance().gameBoard.BoardsPaths[gridY][gridX - 1] == 1)
            return position.y % 30 < 1;
        return false;
    }

    /**
     * Metoda sprawdza czy aktualnie występuja kolizja ducha z pacmanem
     * @param ghostPosition - Vector2 pozycji ducha na ekranie
     * @return true jeśli występuje kolizja, false inaczej
     */

    public static boolean checkIfCollisionWithPacMan(Vector2 ghostPosition){
        Vector2 pacmanPosition = GlobalReferenceManager.pacMan.getPosition();
        float distanceX = abs(ghostPosition.x - pacmanPosition.x);
        float distanceY = abs(ghostPosition.y - pacmanPosition.y);
        if(distanceX < 5 && distanceY < 5)
            return true;
        return false;
    }
}
