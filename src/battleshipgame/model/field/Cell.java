/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipgame.model.field;

import java.awt.Point;

/**
 *
 * @author Мария
 */
public class Cell {
    
    // Координаты клетки
    private Point _position;
    
    public void setPosition(Point pos) {
        _position = pos;
    }
    
    public Point position() {
        return (Point)_position.clone();
    }
    
    // Видно ли содержимое клетки
    private boolean _isOpened = false;
    
    public void openCell() {
        this._isOpened = true;
    }
    
    public void closeCell() {
        this._isOpened = false;
    }
    
    public boolean isOpened() {
        return _isOpened;
    }
    
    // Объект, содержащийся в клетке
    private AbstractCellObject _cellObject;
    
    public void setObject(AbstractCellObject obj) {
        _cellObject = obj;
    }
    
    public AbstractCellObject cellObject() {
        return this._cellObject;
    }
    
    public boolean isEmpty() {
        return this._cellObject == null;
    }
    
    
    // Конструкторы
    public Cell(Point pos) {
        _position = pos;
    }
    
    public Cell(int x, int y) {
        _position = new Point(x,y);
    }
    
    
    // Выстрел по клетке
    public boolean shoot() {
        
        // Если в клетку уже стреляли, то больше нельзя, вернем false
        if(this._cellObject.isDefeated()) {
            //System.out.println("cell " + this.position().x + "," + this.position().y + " is already shot");
            return false;
        }
     
        // Иначе сообщим объекту, находящемуся в ней, что он атакован
        this._cellObject.shoot();
        
        // Открываем содержимое клетки
        this.openCell();
        
        // Описание результата выстрела
//        if (this._cellObject == null) {
//            System.out.println("nothing in cell " + this.position().x +"," + this.position().y);
//        } else {
//            System.out.println(_cellObject.toString() +" in cell " + this.position().x +"," + this.position().y);
//        }
        
        return true;
    }
    
    @Override
    public String toString() {
        if (!this.isOpened()) {    
            return "-";
        }
        if (this._cellObject == null) 
            return "0";
        
        return this._cellObject.toString();
    }
}
