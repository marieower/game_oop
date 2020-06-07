/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipgame.model.field.cell_objects;

import battleshipgame.model.field.AbstractCellObject;
import battleshipgame.model.field.AbstractFieldObject;
import battleshipgame.model.field.field_objects.Ship;

/**
 *
 * @author Мария
 */
public class Deck extends AbstractCellObject {
    
    @Override
    public void setParentObject(AbstractFieldObject parent) {
        // Родительским элементом для палубы может являться только корабль
        if (parent instanceof Ship)
            this._parentObject = parent;
        else try {
            throw new Exception();
        } catch (Exception ex) {
            System.out.print("Invalid type of parent element! Must be Ship");
        }
    }
    
    @Override
    public String toString() {
        if (_isDefeated)
            return "X";
        return "D";
    }
}
