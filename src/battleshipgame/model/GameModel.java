/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipgame.model;

import battleshipgame.model.field.FieldOwnerType;
import battleshipgame.model.field.GameField;
import battleshipgame.model.events.GameEvent;
import battleshipgame.model.events.GameEventListener;
import battleshipgame.model.events.PlayerActionEvent;
import battleshipgame.model.events.PlayerActionListener;
import battleshipgame.model.field.cell_objects.Sea;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Мария
 */
public class GameModel {
    
    // Поле
    private List<GameField> _fields = new ArrayList<GameField>();
    
    public List<GameField> fields() {
        return Collections.unmodifiableList(_fields);
    }
    
    // Состояние игры
    private GameStatus _status = GameStatus.GAME_IS_ON;
    
    public void setStatus(GameStatus status) {
        this._status = status;
    }
    
    public GameStatus status() {
        return this._status;
    }
    
    // В чье поле осуществляются выстрелы в текущий ход
    private int _attackedPlayer;
    
    public GameField attackedPlayer() {
        return _fields.get(_attackedPlayer);
    }
    
    public GameModel(int width, int height) throws Exception {
        
        // Создаем поля для двух игроков
        generateFields(width, height);
        
        PlayerObserver observer = new PlayerObserver();
        
        _fields.get(0).addPlayerActionListener(observer); // "Следим" за игроком пользователя
        _fields.get(1).addPlayerActionListener(observer); // "Следим" за игроком компьютера
        
        // Передаем ход первому игроку
        exchangePlayer();
    }
    
    public GameModel() throws Exception {
        // Создаем поля для двух игроков
        generateFields();
        
        PlayerObserver observer = new PlayerObserver();
        
        _fields.get(0).addPlayerActionListener(observer); // "Следим" за игроком пользователя
        _fields.get(1).addPlayerActionListener(observer); // "Следим" за игроком компьютера
        
        // Передаем ход первому игроку
        exchangePlayer();
    }
    
    // Генерация обстановки с заданными параметрами
    private void generateFields(int width, int height) throws Exception {
        // Создаем новое поле заданного размера для пользователя
        _fields.add( new GameField(width, height, FieldOwnerType.USER));
        
        // Заполняем кораблями
        _fields.get(0).putObjects();
        
        // Открываем содержимое пользователю
        _fields.get(0).closeField();
        
        // Создаем новое поле заданного размера для компьютера
        _fields.add( new GameField(width, height, FieldOwnerType.COMPUTER));
        
        // Заполняем кораблями
        _fields.get(1).putObjects();
        
        // Скрываем содержимое от пользователя
        _fields.get(1).openField();
    }
    
    private void generateFields() throws Exception {
        // Генерация обстановки по умолчанию
        generateFields(10,10);
    }
    
    public GameField determineWinner() {
        if (_fields.get(0).isDefeated()) {
            this.setStatus(GameStatus.WINNER_FOUND);
            System.out.print(_fields.get(0).owner() + " wins");
            return _fields.get(1);
        }
        if (_fields.get(1).isDefeated()) {
            this.setStatus(GameStatus.WINNER_FOUND);
            System.out.print(_fields.get(1).owner() + " wins");
            return _fields.get(0);
        }
        return null;
        
    }
    
    private void exchangePlayer(){

        // Сменить игрока
        _attackedPlayer++;
        if(_attackedPlayer >= _fields.size())
            _attackedPlayer = 0;
        
        // Генерируем событие
        firePlayerExchanged(attackedPlayer());
    }
       
    
    private class PlayerObserver implements PlayerActionListener {

        @Override
        public void shoot(PlayerActionEvent e) {
            
            //  Транслируем событие дальше для активного игрока
            fireShoot(e);
            
            // Определяем победителя
            GameField winner = determineWinner();
            
            // Меняем игрока, если игра продолжается
            if(winner == null && e.cellObject() instanceof Sea) {
                exchangePlayer();
            } else if (winner != null) {
                // Генерируем событие
                fireGameFinished(winner);
            }
        }
    }
    
    // ------------------------ Порождает события игры ----------------------------
    
    // Список слушателей
    private ArrayList _listenerList = new ArrayList(); 
 
    // Присоединяет слушателя
    public void addGameEventListener(GameEventListener l) { 
        _listenerList.add(l); 
    }
    
    // Отсоединяет слушателя
    public void removeGameEventListener(GameEventListener l) { 
        _listenerList.remove(l); 
    } 
    
    // Оповещает слушателей о событии
    protected void fireGameFinished(GameField winner) {
        
        GameEvent event = new GameEvent(this);
        event.setField(winner);
        for (Object listener : _listenerList)
        {
            ((GameEventListener)listener).gameFinished(event);
        }
    }     

    protected void firePlayerExchanged(GameField p) {
        
        GameEvent event = new GameEvent(this);
        event.setField(p);
        for (Object listener : _listenerList)
        {
            ((GameEventListener)listener).playerExchanged(event);
        }
    } 
    
// --------------------- Порождает события, связанные с игроками -------------
    
    // Список слушателей
    private ArrayList _playerListenerList = new ArrayList(); 
 
    // Присоединяет слушателя
    public void addPlayerActionListener(PlayerActionListener l) { 
        _playerListenerList.add(l); 
    }
    
    // Отсоединяет слушателя
    public void removePlayerActionListener(PlayerActionListener l) { 
        _playerListenerList.remove(l); 
    }

    // Оповещает слушателей о событии
    protected void fireShoot(PlayerActionEvent e) {
        
        for (Object listener : _playerListenerList)
        {
            ((PlayerActionListener)listener).shoot(e);
        }
    }
    
}
    

