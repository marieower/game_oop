/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipgame.model.field.cell_objects;

import battleshipgame.model.field.AbstractCellObject;
import battleshipgame.model.field.AbstractFieldObject;

/**
 *
 * @author Мария
 */
public class Sea extends AbstractCellObject {

    @Override
    public void setParentObject(AbstractFieldObject parent) {
        this._parentObject = null;
    }

    @Override
    public String toString() {
        if (this.isDefeated())
            return "+";
        return "~";
    }
    
}
