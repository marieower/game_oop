/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipgame.model.field;

import battleshipgame.model.Direction;
import battleshipgame.model.field.field_objects.FieldObjectPosition;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Мария
 */
public abstract class AbstractFieldObject {
    
    // Поле, на котором расположен объект
    protected GameField _field;
    
    public void setField(GameField field) {
        this._field = field;
    }
    
    public GameField field() {
        return this._field;
    }
    
    // Количество частей, входящих в объект
    protected int _numParts;
    
    public void setNumParts(int numParts) {
        this._numParts = numParts;
    }
    
    public int numParts() {
        return this._numParts;
    }
    
    // Объекты клеток, входящие в состав данного объекта
    protected List<AbstractCellObject> _parts = new ArrayList<>();
    
    public List<AbstractCellObject> parts() {
        return Collections.unmodifiableList(_parts);
    }
    
    protected abstract void addPart(AbstractCellObject cellObject);
    
    protected void removePart(AbstractCellObject cellObject) {
        _parts.remove(cellObject);
        cellObject.setParentObject(null);
    };
    
    // Положение объекта на поле ("первая" клетка и ориентанция)
    protected FieldObjectPosition _position;
    
    public FieldObjectPosition position() {
        return this._position;
    }
    
    public void setPosition(FieldObjectPosition position) {
        this._position = position;
    }
    
    // Действие, срабатывающее при поражении объекта
    public abstract void defeatEffect();
        
    public void shoot() {
        // Если объект убит
        if (this.isDefeated()) {
            // Отмечаем все клетки вокруг объекта
            this.shootAround();
            // Применяем эффект (напр, взрыв у мины)
            this.defeatEffect();
        }
    }
    
    // Если все части объекта поражены, то он считается "убитым"
    public boolean isDefeated() {
        for (AbstractCellObject part : _parts)
            if(!part.isDefeated())
               return false;
        return true;
    }
    
    // Если хотя бы одна их частей поражена, то считается "раненым"
    public boolean isDamaged() {
        return this.damagedParts().size() > 0 && 
                this.damagedParts().size() < this.parts().size();
    }
    
    // Количество раненых частей
    public List<AbstractCellObject> damagedParts() {
        List<AbstractCellObject> damagedParts = new ArrayList<AbstractCellObject>();
        int count = 0;
        for (AbstractCellObject part : _parts)
            if(part.isDefeated())
                damagedParts.add(part);
        return Collections.unmodifiableList(damagedParts);
    }
    
    // Обстрел клеток вокруг объекта (при поражении)
    public void shootAround() {
        
        // Введем переменные
        int x = this._position.start().x;
        int y = this._position.start().y;
        Direction direction = this._position.direction();
        int parts = this._parts.size();
        	
	int fromX, toX = -1, fromY, toY = -1;
        
        // Если корабль примыкает к верхней границе поля, то начинаем проверять со строки с индексом 0
        // Иначе со строки перед кораблем, то есть x-1
        fromX =  (x == 0) ? x : x - 1;
        
        // Аналогично если примыкает к левой границе, то со столбца 0, иначе со столбца y - 1
        fromY =  (y == 0) ? y : y - 1;
        
        // Правая и нижняя границы проверяемого поля:
        if (direction == Direction.VERTICAL) {
    
            // Правая граница:
            toY = y + 2;
            
            // Нижняя граница:
            toX = x + parts + 1;
            
        } else if (direction == Direction.HORIZONTAL) {
    
            // Правая граница:
            toY = y + parts + 1;
            
            // Нижняя граница:
            toX = x + 2;
        }
        
        toY = (toY > field().width()) ? (toY - 1) : toY;
        toX = (toX > field().height()) ? (toX - 1) : toX;

	// запускаем цикл для выбранного диапазона ячеек. Стреляем по каждой
	for (int i = fromX; i < toX; i++) {
		for (int j = fromY; j < toY; j++) {
                    Cell c = this._field.cell(new Point(i, j));
                    c.shoot();
		}
	}
    }
}
