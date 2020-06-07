/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipgame.model.field;

import battleshipgame.model.field.Cell;
import com.sun.istack.internal.NotNull;

/**
 *
 * @author Мария
 */
public abstract class AbstractCellObject {
    
    // Клетка, в которой расположен элемент
    protected Cell _cell;
    
    public void setCell(Cell cell) {
        this._cell = cell;
        cell.setObject(this);
    }
    
    public Cell cell() {
        return this._cell;
    }
    
    // Элемент поля, к которому относится данная часть
    protected AbstractFieldObject _parentObject;
    
    public AbstractFieldObject parentObject() {
        return this._parentObject;
    }
    
    public abstract void setParentObject(AbstractFieldObject parent);
    
    // Признак попадания по данному элементу
    protected boolean _isDefeated = false;
    
    public boolean isDefeated() {
        return this._isDefeated;
    }
    
    public void shoot() {
        // Отметим элемент подбитым и сообщим родительскому элементу, что он атакован
        this._isDefeated = true; 
        if (this._parentObject != null)
            this._parentObject.shoot();
        
    };
    
}
