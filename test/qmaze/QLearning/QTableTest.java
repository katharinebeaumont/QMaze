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
        env = new Environment();
        env.setDefaultMaze();
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetBestSurroundingRoom() {
        QTable qtable = new QTable(env);
        int row = env.getRows() -1;
        int col = env.getColumns() -1;
        
        Pair penultimateRoom = new Pair(row-1,col);
        Pair finalRoom = qtable.getBestSurroundingRoomOrRandom(penultimateRoom);
        
        assertEquals(new Pair(row,col), finalRoom);
    }
    
    @Test
    public void testGetRandomSurroundingRoom() {
        QTable qtable = new QTable(env);
        int row = env.getRows() -1;
        int col = env.getColumns() -1;
        
        Pair penultimateRoom = new Pair(row-2,col-2);
        Pair finalRoom = qtable.getRandomSurroundingRoom(penultimateRoom);
        
        assertEquals(new Pair(row,col), finalRoom);
    }
    
}
