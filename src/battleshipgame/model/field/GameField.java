/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipgame.model.field;

import battleshipgame.model.field.field_objects.FieldObjectPosition;
import battleshipgame.model.field.field_objects.Ship;
import battleshipgame.model.Direction;
import battleshipgame.model.events.PlayerActionEvent;
import battleshipgame.model.events.PlayerActionListener;
import battleshipgame.model.field.cell_objects.Sea;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Мария
 */
public class GameField {
    
    // размеры поля
    private int _width, _height;
    
    public void setSize(int w, int h) throws Exception {
        if (w < 3 || h < 3)
            throw new Exception("Size must be minimum 3x3");
        this._width = w;
        this._height = h;
    }
    
    public int height() {
        return this._height;
    }
    
    public int width() {
        return this._width;
    }
    
    // Клетки, принадлежащие полю
    private ArrayList<Cell> _cells = new ArrayList<Cell>();
    
    private void fillWithCells()  {
        _cells.clear();
        for(int i = 0; i < this._width; ++i) {
           for(int j = 0; j < this._height; ++j) {
               _cells.add(new Cell(new Point(i, j)));
           } 
        }
    }
    
    public Cell cell(Point pos) {
       for (Cell cell : _cells) {
           if (cell.position().equals(pos)) {
               return cell;
           }
       }
       return null;
    }
    
    public Cell cell(int x, int y) {
       
       for (Cell cell : _cells) {
           if (cell.position().equals(new Point(x,y))) {
               return cell;
           }
       }
       return null;
    }
    
    public void clear() {
        _cells.clear();
    }
    
    // Объекты, принадлежащие полю
    private ArrayList<AbstractFieldObject> _objects = new ArrayList<AbstractFieldObject>();
    
    // Тип игрока, владеющего полем
    private FieldOwnerType _owner;
            
    public FieldOwnerType owner() {
        return (FieldOwnerType)this._owner;
    }
    
    // Конструкторы
    public GameField() throws Exception {
        GameField f = new GameField(10, 10, FieldOwnerType.USER);
        this._cells = f._cells;
        this.setSize(f._width, f._height);
    }
    
    public GameField(int width, int height, FieldOwnerType owner) throws Exception {
        this.setSize(width, height);
        _owner = owner;
        this.fillWithCells();
    }
    
    // Все ли корабли на поле поражены
    public boolean isDefeated() {
        for (AbstractFieldObject ship : _objects)
            if (ship instanceof Ship && !ship.isDefeated())
                return false;
        return true;
    }
    
    public List<Ship> damagedShips() {
        List<Ship> damagedShips = new ArrayList<Ship>();
        
        for (AbstractFieldObject ship : _objects) {
            if (ship instanceof Ship && ship.isDamaged()) {
                damagedShips.add((Ship)ship);
            }
        }
        
        return Collections.unmodifiableList(damagedShips);
    }
    
    public void printField() {
        
        System.out.println();
        System.out.println(this._owner + " attacks this field:");
        System.out.println();
        
        // Print head
        String head = " ";
       
        for(int i=0; i < this._width; ++i) {
            char letterChar = (char)((int)'a' + i);
            String letter =  String.valueOf(letterChar);
            head = head.concat(" " + letter);
            
        }
        System.out.println(head);
        
        for(int i=0; i < this._width; ++i) {
           System.out.print(i+1 + " ");
           for(int j=0; j < this._height; ++j) {
               
               Point position = new Point(i, j);
               System.out.print(this.cell(position).toString() + ' ');
              
           }
           System.out.println();
           
        }
    }
    
    // Открыть содержимое всех клеток поля
    public void openField() {
        for (Cell cell : _cells)
            cell.openCell();
    }
    
    // Скрыть содержимое всех клеток поля
    public void closeField() {
        for (Cell cell : _cells)
            cell.closeCell();
    }

    // Выстрел по клетке с заданными координатами
    public boolean shoot(Point point) {
        if (!this.cellIsAvailable(point))
            return false;
        
        this.cell(point).shoot();
        fireShoot(point);
        return true;
    }
    
    // Выстрел по клетке с заданными координатами
    public boolean shoot(int x, int y) {
        Point point = new Point(x, y);
        return this.shoot(point);
        
    }
    
    // Доступна ли клетка с данными координатами для выстрела
    private boolean cellIsAvailable(Point point) {
        if (point.x < 0 || point.y < 0 || 
                point.x > height() || point.y > width()) 
            return false;
        if (this.cell(point).cellObject().isDefeated())
            return false;
        return true;
    }
    
    public Point getCoordinatesAutoShoot() {
        int x = -1, y = -1;
        
        if (this.damagedShips().size() == 0) {
            // Если нет раненых кораблей на поле, то выбираем случайную клетку
            x = getRandom(height() - 1);
            y = getRandom(width() - 1);
        } else {
            Ship ship = this.damagedShips().get(0);
            
            
            // Если поражена только одна палуба, то выбираем сулчайную соседнюю клетку
            if (ship.damagedParts().size() == 1) {
                Point damagedPos = ship.damagedParts().get(0).cell().position();
                int shootDirection = getRandom(4);
                switch (shootDirection) {
                    case 1:
                        x = damagedPos.x + 1;
                        y = damagedPos.y;
                        break;
                    case 2:
                        x = damagedPos.x - 1;
                        y = damagedPos.y;
                        break;
                    case 3:
                        x = damagedPos.x;
                        y = damagedPos.y + 1;
                        break;
                    case 4:
                        x = damagedPos.x;
                        y = damagedPos.y - 1;
                        break;
                }
            } else {
                // Если поражены две соседние клетки, то выбираем случайную 
                // клетку в том же столбце/строке, соседнюю с одной из пораженных
                Point damagedPos = ship.damagedParts().get(getRandom(ship.damagedParts().size()-1)).cell().position();
                int shootDirection = getRandom(2);
                if (ship.damagedParts().get(0).cell().position().x == 
                        ship.damagedParts().get(1).cell().position().x) {
                    // Если пораженные клетки в одной горизонтали
                    x = damagedPos.x;
                    y = (shootDirection == 1) ? damagedPos.y + 1 : damagedPos.y - 1;
                } else {
                    y = damagedPos.y;
                    x = (shootDirection == 1) ? damagedPos.x + 1 : damagedPos.x - 1;
                }
            }
        }
        return new Point(x, y);
    }
    
    // Данные об объектах на поле
    private List<FieldObjectInfo> _objectTypes = new ArrayList<FieldObjectInfo>();
    
    // Cчитает, сколько объектов какого типа помещается на поле
    public void calculateObjectTypes() {
        
        this._objectTypes.clear();
        
        switch(Math.min(width(), height()))
        {
            case 3:
                _objectTypes = objectsSet3x3;
                break;
            case 4:
                _objectTypes = objectsSet4x4;
                break;
            case 5:
                _objectTypes = objectsSet5x5;
                break;
            case 6:
                _objectTypes = objectsSet6x6;
                break;
            case 7:
                _objectTypes = objectsSet7x7;
                break;
            case 8:
                _objectTypes = objectsSet8x8;
                break;
            case 9:
                _objectTypes = objectsSet9x9;
                break;
            default:
                _objectTypes = objectsSet10x10;
        }       
    }
    
    // Разместить объекты на поле 
    public boolean putObjects() {
        
	// Рассчитываем количество кораблей для заданных размеров поля
        this.calculateObjectTypes();
        
	for (int i = 0, length = this._objectTypes.size(); i < length; i++) {
		int parts = this._objectTypes.get(i).numParts();
                int numOfType = this._objectTypes.get(i).numInstances();
                FieldObjectType type = this._objectTypes.get(i).type();
                
		for (int j = 0; j < numOfType; j++) {
			
                        // получаем координаты первой палубы и направление расположения палуб (корабля)
			FieldObjectPosition objPosition = this.getCoordinates(parts);
                        AbstractFieldObject obj = null;
                        switch(type) {
                            case SHIP:
                                // создаём объект корабля с помощью конструктора Ship
                                obj = new Ship(this, parts, objPosition);
                                break;
                            case ISLAND:
                                // создаём объект острова с помощью конструктора
                                //obj = new Island(this, parts, objPosition);
                                break;
                            case BOMB:
                                // создаём объект мины с помощью конструктора
                                //obj = new Bomb(this, objPosition);
                                break;
                        }
			
                        // Добавляем корабль в список объектов поля
			this._objects.add(obj);
		}
	}  
        
        // Заполнить пустые клетки "водой"
        for (int i = 0; i < this.height(); ++i) {
            for (int j = 0; j < this.width(); ++j) {
                Cell c = this.cell(i, j);
                if (c.isEmpty()) {
                    Sea sea = new Sea();
                    sea.setCell(c);
                }
            }
        }    
        return true;
    }
    
    int getRandom(int n) {
        return (int)(Math.random()*(n+1));
    }
    
    // Вычисление координат для объекта поля
    FieldObjectPosition getCoordinates(int parts) {
	
        Direction direction;
        
	int dir = getRandom(1);
        int x,y;
            
        direction = (dir == 0) ? Direction.HORIZONTAL : Direction.VERTICAL;
 
	// В зависимости от направления, генерируем начальные координаты
	if (direction == Direction.HORIZONTAL) {
		x = getRandom(this.height() - 1);
		y = getRandom(this.width() - parts);
	} else {
		x = getRandom(this.height() - parts);
		y = getRandom(this.width() - 1);
	}
 
	// проверяем валидность координат всех палуб корабля:
	// нет ли в полученных координатах или соседних клетках ранее созданных кораблей
	boolean result = this.checkShipLocation(x, y, direction, parts);
	
        if (result)
            return new FieldObjectPosition(new Point(x, y), direction);
        
        // если координаты невалидны, снова запускаем функцию
        return this.getCoordinates(parts);	
}

    // Проверка выбранного положения объекта на поле (расстояние хотя бы одна клетка до других объектов)
    boolean checkShipLocation(int x, int y, Direction direction, int parts) {
	// Введем переменные
	int fromX, toX = -1, fromY, toY = -1;
        
        // Если корабль примыкает к верхней границе поля, то начинаем проверять со строки с индексом 0
        // Иначе со строки перед кораблем, то есть x-1
        fromX = (x == 0) ? x : x - 1;
        
        // Аналогично если примыкает к левой границе, то со столбца 0, иначе со столбца y - 1
        fromY = (y == 0) ? y : y - 1;
        
        // Правая и нижняя границы проверяемого поля:
        if (direction == Direction.VERTICAL) {
    
            // Правая граница:
            if (y == this.width() - 1)  // Если примыкает к правой границе поля
                toY = y + 1;
            else                    // иначе
                toY = y + 2;
            
            // Нижняя граница:
            if (x + parts == this.height())  // Если примыкает к нижней границе поля
                toX = x + parts;
            else                             // иначе
                toX = x + parts + 1;
            
        } else if (direction == Direction.HORIZONTAL) {
    
            // Правая граница:
            if (y + parts == this.width())  // Если примыкает к правой границе поля
                toY = y + parts;
            else                            // иначе
                toY = y + parts + 1;
            
            // Нижняя граница:
            if(x == this.height() - 1)  // Если примыкает к нижней границе поля
                toX = x + 1;
            else                        // иначе
                toX = x + 2;
        }

 
	// запускаем цикл для выбранного диапазона ячеек
	// если проверяемая ячейка не пустая (содержит палубу или другой объект), то
	// возвращаем false 
	for (int i = fromX; i < toX; i++) {
		for (int j = fromY; j < toY; j++) {
                    Cell c = this.cell(new Point(i, j));
			if (!c.isEmpty()) 
                            return false;
		}
	}
	return true;
    }

    // ---------------------- Порождает события -----------------------------
    
    // список слушателей
    private ArrayList<PlayerActionListener> _listenerList = new ArrayList<>(); 
 
    // Присоединяет слушателя
    public void addPlayerActionListener(PlayerActionListener l) { 
        _listenerList.add(l); 
    }
    
    // Отсоединяет слушателя
    public void removePlayerActionListener(PlayerActionListener l) { 
        _listenerList.remove(l);
    } 
    
    // Оповещает слушателей о событии
    protected void fireShoot(Point point) {
        
        PlayerActionEvent evt = new PlayerActionEvent(this);
        evt.setField(this);
        evt.setCellObject(this.cell(point).cellObject());
        for (PlayerActionListener listener : _listenerList)
        {
            listener.shoot(evt);
        }
    }     
    
    // Вычисленные наборы объектов для разных размеров полей:
    private static final List<FieldObjectInfo> objectsSet3x3 = new ArrayList<FieldObjectInfo>() {
        {
            add(new FieldObjectInfo(FieldObjectType.SHIP, 1, 1));
        }
    };
    
    private static final List<FieldObjectInfo> objectsSet4x4 = new ArrayList<FieldObjectInfo>() {
        {
            add(new FieldObjectInfo(FieldObjectType.SHIP, 2, 1));
            add(new FieldObjectInfo(FieldObjectType.SHIP, 1, 2));
        }
    };
    private static final List<FieldObjectInfo> objectsSet5x5 = new ArrayList<FieldObjectInfo>() {
        {
            add(new FieldObjectInfo(FieldObjectType.SHIP, 2, 1));
            add(new FieldObjectInfo(FieldObjectType.SHIP, 1, 2));
            add(new FieldObjectInfo(FieldObjectType.ISLAND, 1, 1));
        }
    };
    private static final List<FieldObjectInfo> objectsSet6x6 = new ArrayList<FieldObjectInfo>() {
        {
            add(new FieldObjectInfo(FieldObjectType.SHIP, 3, 1));
            add(new FieldObjectInfo(FieldObjectType.SHIP, 2, 1));
            add(new FieldObjectInfo(FieldObjectType.SHIP, 1, 1));
            add(new FieldObjectInfo(FieldObjectType.ISLAND, 1, 1));
        }
    };
    private static final List<FieldObjectInfo> objectsSet7x7 = new ArrayList<FieldObjectInfo>() {
        {
            add(new FieldObjectInfo(FieldObjectType.SHIP, 3, 1));
            add(new FieldObjectInfo(FieldObjectType.SHIP, 2, 1));
            add(new FieldObjectInfo(FieldObjectType.SHIP, 1, 1));
            add(new FieldObjectInfo(FieldObjectType.ISLAND, 2, 1));
        }
    };
    private static final List<FieldObjectInfo> objectsSet8x8 = new ArrayList<FieldObjectInfo>() {
        {
            add(new FieldObjectInfo(FieldObjectType.SHIP, 4, 1));
            add(new FieldObjectInfo(FieldObjectType.SHIP, 3, 1));
            add(new FieldObjectInfo(FieldObjectType.SHIP, 2, 1));
            add(new FieldObjectInfo(FieldObjectType.SHIP, 1, 1));
            add(new FieldObjectInfo(FieldObjectType.ISLAND, 2, 1));
            add(new FieldObjectInfo(FieldObjectType.ISLAND, 1, 1));
        }
    };
    private static final List<FieldObjectInfo> objectsSet9x9 = new ArrayList<FieldObjectInfo>() {
        {
            add(new FieldObjectInfo(FieldObjectType.SHIP, 4, 1));
            add(new FieldObjectInfo(FieldObjectType.SHIP, 3, 2));
            add(new FieldObjectInfo(FieldObjectType.SHIP, 2, 1));
            add(new FieldObjectInfo(FieldObjectType.SHIP, 1, 2));
            add(new FieldObjectInfo(FieldObjectType.ISLAND, 2, 1));
            add(new FieldObjectInfo(FieldObjectType.BOMB, 1, 1));
        }
    };
    private static final List<FieldObjectInfo> objectsSet10x10 = new ArrayList<FieldObjectInfo>() {
        {
            add(new FieldObjectInfo(FieldObjectType.SHIP, 4, 1));
            add(new FieldObjectInfo(FieldObjectType.SHIP, 3, 2));
            add(new FieldObjectInfo(FieldObjectType.SHIP, 2, 1));
            add(new FieldObjectInfo(FieldObjectType.SHIP, 1, 3));
            add(new FieldObjectInfo(FieldObjectType.ISLAND, 2, 1));
            add(new FieldObjectInfo(FieldObjectType.ISLAND, 1, 1));
            add(new FieldObjectInfo(FieldObjectType.BOMB, 1, 1));
        }
    };
}

