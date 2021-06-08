package com.GameLoop;

import com.Main;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Klasa zarzada wciskaniem klawiszy przez uzytkownika. Implementuje wzorzec
 * singletona w celu mozliwego dostepu z kazdego miejsca.
 */
public class InputManager {
    private static InputManager m_instance;

    private final Queue<KeyEvent> m_inputList;

    private int m_counter = 0;

    /**
     * Prywatny konstruktor pozwala na implementacje wzorca singletona.
     * Tworzona jest lista, dostępna z poziomu wielu wątków oraz dodawany jest
     * EventFilter przechwytujacy wszystkie wcisniete klawisze, i dodajacy je do
     * listy jesli jeszcze ich tam nie ma.
     */
    private InputManager() {
        m_inputList = new ConcurrentLinkedQueue<>();
        Main.getInstance().getStage().addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
            if (!m_inputList.stream().anyMatch(x -> x.getCode().getCode() == e.getCode().getCode())) {
                m_inputList.add(e);
            }
        });
    }

    /**
     * Metoda zwraca instancje singletonu. Jesli nie istnieje, tworzy nowa i ja zwraca.
     * @return  instancja singletonu
     */
    public static InputManager getInstance() {
        if (m_instance == null) {
            m_instance = new InputManager();
        }
        return m_instance;
    }
    /**
     * Metoda konczy przechwytywanie klawiszy w aktualnej klatce.
     * Co 5 klatek lista jest czyszczona z klawiszy.
     */
    public void endFrame() {
        m_counter++;
        if (m_counter / 5 == 0) {
            m_inputList.clear();
            m_counter=0;
        }
    }
    /**
     * Metoda pozwala na sprawdzenie czy dany klawisz zostal wcisniety
     * @param keyCode - klawisz, ktory powinien byc nacisniety
     * @return true jesli klawisz byl nacisniety, false inaczej
     */
    public boolean isKeyPressed(KeyCode keyCode) {
        return m_inputList.stream().anyMatch(x -> x.getCode() == keyCode);
    }


}
