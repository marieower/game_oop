/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipgame.model.events;

import battleshipgame.model.field.GameField;
import java.util.EventObject;

/**
 *
 * @author Мария
 */
public class GameEvent extends EventObject {
    
    private GameField _field;
    
    public void setField(GameField f) {
        this._field = f;
    }
    
    public GameField field() {
        return this._field;
    }
    
    public GameEvent(Object source) { 
        super(source); 
    } 
    
}
