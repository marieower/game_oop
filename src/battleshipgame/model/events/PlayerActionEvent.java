/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipgame.model.events;

import battleshipgame.model.field.AbstractCellObject;
import battleshipgame.model.field.GameField;
import java.util.EventObject;

/**
 *
 * @author Мария
 */
public class PlayerActionEvent extends EventObject {
    
    // Игрок
    private GameField _field = null;
    
    public GameField field() {
        return this._field;
    }
    
    public void setField(GameField f) {
        this._field = f;
    }
    
    // Объект клетки
    private AbstractCellObject _cellObject;
    
    public AbstractCellObject cellObject() {
        return this._cellObject;
    }
    
    public void setCellObject(AbstractCellObject obj) {
        this._cellObject = obj;
    }
    
    public PlayerActionEvent(Object source) { 
        super(source); 
    } 
}
