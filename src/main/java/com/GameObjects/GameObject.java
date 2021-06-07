package com.GameObjects;

import com.GameLoop.GameLoop;
import com.Utility.Sprite;
import com.Utility.Vector2;
/**
 * Klasa abstrakcyjna - klasa bazowa dla wszystkich obiektów gry.
 * Implementuje obserwatora dodając siebie do listy słuchaczy w głównej pętli gry
 */
public abstract class GameObject {

    public Sprite objectSprite;

    protected Vector2 m_position = new Vector2(0, 0);
    /**
     * Konstruktor klasy, Tworzy nowy obiekt i dodaje do listy słuchaczy, ustawia element klasy jakim jest zdjęcia,
     * które podczas gry będzie renderowane.
     * @param sprite - zdjęcie do wyrenderowania
     */
    public GameObject(Sprite sprite) {
        objectSprite = sprite;
        GameLoop.getInstance().addListener(this);
    }
    /**
     * Metoda wywoływana na poczatku gry lub przy stworzeniu obiektu
     */
    public final void start() {
        onStart();
    }
    /**
     * Metoda aktualizująca obiekt, wywoływana podczas każdej klatki
     */
    public final void update() {
        onUpdate();
    }
    /** 
     * Metoda wywoływana na końcu gry lub w momencie zniszenia obiektu
     */
    public final void exit() {
        onExit();
    }
    /**
     * Metoda zwracająca aktualną pozycję obiektu na scenie
     * @return vector2 zwierający obecną pozycję
     */
    public Vector2 getPosition() {
        return m_position;
    }
    /**
     * Metoda ustawiająca zdjęcie przynależne do obiektu
     */
    public void setSprite(Sprite sprite){
        objectSprite = sprite;
    }
    /**
     * Metoda wywoływana na poczatku gry lub przy stworzeniu obiektu
     */
    protected abstract void onStart();
    /**
     * Metoda aktualizująca obiekt, wywoływana podczas każdej klatki
     */
    protected abstract void onUpdate();
    /**
     * Metoda wywoływana na końcu gry lub w momencie zniszenia obiektu
     */
    protected abstract void onExit();


}
