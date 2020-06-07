/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipgame.model.field.field_objects;

import battleshipgame.model.Direction;
import java.awt.Point;

/**
 *
 * @author Мария
 */
public class FieldObjectPosition {
    // Координаты "первой" клетки объекта
    private Point _start;
    
    public void setStart(Point point) {
        this._start = point;
    }
    
    public Point start() {
        return this._start;
    }
    
    // Расположение (горизонтально или вертикально)
    private Direction _direction;
    
    public void setDirection(Direction direction) {
        this._direction = direction;
    }
    
    public Direction direction() {
        return this._direction;
    }
    
    // Конструкторы
    public FieldObjectPosition(Point start, Direction direction) {
        this._start = start;
        this._direction = direction;
    }

    public FieldObjectPosition() {
        this._start = new Point();
        this._direction = Direction.HORIZONTAL;
    }

}
