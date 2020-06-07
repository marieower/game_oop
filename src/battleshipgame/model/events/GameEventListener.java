/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipgame.model.events;

import java.util.EventListener;

/**
 *
 * @author Мария
 */
public interface GameEventListener extends EventListener {
    public void gameFinished(GameEvent e);
    public void playerExchanged(GameEvent e);
    
}
