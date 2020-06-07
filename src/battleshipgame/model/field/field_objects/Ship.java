/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipgame.model.field.field_objects;

import battleshipgame.model.field.AbstractCellObject;
import battleshipgame.model.field.cell_objects.Deck;
import battleshipgame.model.field.AbstractFieldObject;
import battleshipgame.model.Direction;
import battleshipgame.model.field.Cell;
import battleshipgame.model.field.GameField;

/**
 *
 * @author Мария
 */
public class Ship extends AbstractFieldObject {

    // Добавить палубу к кораблю
    @Override
    protected void addPart(AbstractCellObject cellObject) {
        if (cellObject instanceof Deck) {
            this._parts.add(cellObject);
            cellObject.setParentObject(this);
        } else try {
            throw new Exception();
        } catch (Exception ex) {
            System.out.print("Invalid type. Must be Deck");
        }
    }
        
    public Ship(GameField field, int numDecks, FieldObjectPosition pos) {
        
        // на каком поле создаётся данный корабль
	this.setField(field);
	
        // количество палуб
	this._numParts = numDecks;
        
        // первая палуба и направление
	this.setPosition(pos);
	
        this.putDecks();
    }
    
    // Связь с палубами
    private void putDecks() {
        int x, y;
        Deck newDeck;
        
        for (int i = 0; i < this._numParts; i++) {
            newDeck = new Deck();
            
            if (this._position.direction() == Direction.HORIZONTAL) {
                x = this._position.start().x;
                y = this._position.start().y + i;
            } else {
                x = this._position.start().x + i;
                y = this._position.start().y;
            }
            
            Cell cell = this._field.cell(x, y);
            newDeck.setCell(cell);
            
            this.addPart(newDeck);
        }
    }

    @Override
    public void defeatEffect() {
        // no defeat effect
    }
        
    @Override
    public String toString() {
        return "S";
    }    
}
