/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleshipgame;

import battleshipgame.model.GameModel;
import battleshipgame.model.GameStatus;
import battleshipgame.model.events.GameEvent;
import battleshipgame.model.events.GameEventListener;
import battleshipgame.model.field.FieldOwnerType;
import java.awt.HeadlessException;
import java.awt.Point;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;



/**
 *
 * @author Мария
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        SwingUtilities.invokeLater(GamePanel::new);
    }
    
    static class GamePanel {

        private GameModel game;
        
        
        public GamePanel() throws HeadlessException {
            try {
                playGame();
            } catch (Exception ex) {
                System.out.print("oops!");
            }
        }
         
        private void playGame() throws Exception {
            game = new GameModel(5,5);
            game.addGameEventListener(new GameController());
            
            game.fields().get(0).printField();
            game.fields().get(1).printField(); 
            
            while (game.status() == GameStatus.GAME_IS_ON) {
                performMove();
            }
        }
        
        private void performMove() throws InterruptedException {
            
            Scanner in = new Scanner(System.in);
            
            // Выбираем точку для выстрела
            Point target = new Point(-1, -1);
            
            System.out.println(game.attackedPlayer().owner() + "\'s move: ");
            
            if (game.attackedPlayer().owner() == FieldOwnerType.USER) {
                // Если стреляет пользователь, что считываем координаты из консоли
                String input = in.nextLine();
                target = readCoordinates(input);
            } else {
                // Если компьютер, то генерируем согласно алгоритму
                target = game.attackedPlayer().getCoordinatesAutoShoot();
            }
            
            // Стреляем по выбранной точке
            boolean shootSuccessful = game.attackedPlayer().shoot(target);
            
            // Если выстрел прошел (точка в пределах поля и по ней еще не стреляли)
            if (shootSuccessful) {
                
                if (game.attackedPlayer().owner() == FieldOwnerType.COMPUTER)
                    Thread.sleep(1000);
                
                // Выводим точку выстрела
                System.out.print(pointToCoordinates(target));
                
                // Отображаем результат на поле
                game.fields().get(0).printField();
                game.fields().get(1).printField(); 
                
            }
        
        }
        
    
    private final class GameController implements GameEventListener {

        
        @Override
        public void gameFinished(GameEvent e) {
            System.out.println(e.field().owner() + " WINS!");
        }

        @Override
        public void playerExchanged(GameEvent e) {
            //System.out.println(e.field().owner() + "\'s move: ");
            
        }
    }
    }
    
    public static Point readCoordinates(String input) {
        
        int x = -1;
        int y = -1;
        
        input = input.toLowerCase();
        
        Pattern inputPattern = Pattern.compile("^\\s*[a-z]\\s*\\d+\\s*$");
        Matcher inputMatcher = inputPattern.matcher(input);
        
        Pattern charPattern = Pattern.compile("[a-z]");
        Matcher charMatcher = charPattern.matcher(input);
        
        Pattern intPattern = Pattern.compile("\\d+");
        Matcher intMatcher = intPattern.matcher(input);
        
        if (inputMatcher.matches()) {
            
            // Получим букву из строки
            if (charMatcher.find()) {
                int startChar = charMatcher.start();
                // Преобразуем букву в номер столбца поля
                int charCoordinateCode = (int)input.charAt(startChar);
                y = charCoordinateCode - (int)'a';
            }
            
            // Получим число из строки
            if (intMatcher.find()) {
                
                int startInt = intMatcher.start();
                int endInt = intMatcher.end();

                // Преобразуем число в координату (отнимем 1, так как нумерация массива с 0)
                String intCoordinateSubstring = input.substring(startInt, endInt);
                x = Integer.parseInt(intCoordinateSubstring) - 1;
            }
        }
        
        return new Point(x, y);
    }
    
    public static String pointToCoordinates(Point point) {
        String result = "";
        
        char charY = (char)(point.y + (int)'a');
        String y = String.valueOf(charY);
        String x = Integer.toString(point.x + 1);
        
        result = y.concat(x);
        
        return result;
    }

        
    }
