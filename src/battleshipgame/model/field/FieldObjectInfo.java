/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipgame.model.field;

/**
 *
 * @author Мария
 */
public class FieldObjectInfo {
    
    // Тип объекта (Корабль, остров, мина)
    private FieldObjectType _type;
    
    public FieldObjectType type() {
        return this._type;
    }
    
    // Количество частей объекта поля
    private int _numParts;
    
    public int numParts() {
        return this._numParts;
    }
    
    // Количество экземпляров на поле
    private int _numInstances;
    
    public int numInstances() {
        return this._numInstances;
    }
    
    public FieldObjectInfo(FieldObjectType type, int numParts, int numInstances) {
        this._type = type;
        this._numParts = numParts;
        this._numInstances = numInstances;  
    }
}
