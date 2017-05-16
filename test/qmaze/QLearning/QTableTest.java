/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qmaze.QLearning;

import javafx.util.Pair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author katharine
 */
public class QTableTest {
    
    private Environment env;
    
    public QTableTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        env = new Environment(4,4);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetBestSurroundingRoom() {
        QTable qtable = new QTable(env);
        int row = env.getRows() -1;
        int col = env.getColumns() -1;
        Pair finalRoom = new Pair(row,col);
        Pair penultimateRoom = new Pair(row-1,col);
        qtable.update(penultimateRoom, finalRoom, 100);
        Pair resultingRoom = qtable.getBestSurroundingRoomOrRandom(penultimateRoom);
        
        assertEquals(new Pair(row,col), resultingRoom);
    }
    
     @Test
    public void testGetBestSurroundingRoomWhenBothEqual() {
        QTable qtable = new QTable(env);
        int row = env.getRows() -1;
        int col = env.getColumns() -1;
        Pair currentState = new Pair(row,col-1);
        Pair goalState = new Pair(row,col);
        Pair neightbourState = new Pair(row-1,col-1);
        qtable.update(currentState, goalState, 100);
        qtable.update(currentState, neightbourState, 100);
        
        Pair finalRoom = qtable.getBestSurroundingRoomOrRandom(currentState);
        
        assertEquals(goalState, finalRoom);
    }
    
    @Test
    public void testGetBestSurroundingRoomWhenMultipleGoodRooms() {
        QTable qtable = new QTable(env);
        int row = env.getRows() -1;
        int col = env.getColumns() -1;
        Pair currentState = new Pair(row,col-1);
        Pair goalState = new Pair(row,col);
        Pair neighbourState = new Pair(row-1,col-1);
        Pair neighbourState2 = new Pair(row,col-2);
        qtable.update(currentState, goalState, 80);
        qtable.update(currentState, neighbourState, 100);
        qtable.update(currentState, neighbourState2, 90);
        
        Pair finalRoom = qtable.getBestSurroundingRoomOrRandom(currentState);
        
        assertEquals(neighbourState, finalRoom);
    }
    
    //@Test
    //public void testGetRandomSurroundingRoom() {
    //    QTable qtable = new QTable(env);
    //    int row = env.getRows() -1;
    //    int col = env.getColumns() -1;
    //    
    //    Pair penultimateRoom = new Pair(row-2,col-2);
    //    Pair finalRoom = qtable.getRandomSurroundingRoom(penultimateRoom);
    //    
    //    assertEquals(new Pair(row,col), finalRoom);
    //}
    
}
