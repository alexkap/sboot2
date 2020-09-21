package ak.challenge.cities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomMapTest {

    CustomMap myMap;

    @BeforeEach
    void setUp() {
         myMap = new CustomMap();
    }

    @Test
    void loadFromFile() throws Exception {
        //just loading part, should be no exceptions
        myMap.loadFromFile("./city.txt");
    }

    @Test
    void addRoad() {
        myMap.addRoad("Boston, New York");
        assertTrue(myMap.getNeighbours("Boston").contains("New York"));
        assertTrue(myMap.getNeighbours("New York").contains("Boston"));
        assertFalse(myMap.getNeighbours("Boston").contains("Albany"));
    }

    @Test
    void areConnected() {
        myMap.addRoad("Boston, New York");
        assertTrue(myMap.areConnected("Boston", "New York"));
        assertFalse(myMap.areConnected("Boston", "Albany"));
        //now add prev -  Boston-Albany
        myMap.addRoad("Boston, Albany");
        myMap.addRoad("Philadelphia, Newark");
        myMap.addRoad("Boston, Richmond");
        assertTrue(myMap.areConnected("Boston", "Albany"));
        assertTrue(myMap.areConnected("Albany", "New York"));
        assertFalse(myMap.areConnected("Newark", "New York")); // funny
        assertTrue(myMap.areConnected("Richmond", "New York"));
    }

    @Test
    void getAllCitiesOnTheMap() {
        myMap.addRoad("Boston, New York");
        myMap.addRoad("Boston, Albany");
        myMap.addRoad("Philadelphia, Newark");
        myMap.addRoad("Boston, Richmond");
        assertEquals("Albany,Boston,New York,Newark,Philadelphia,Richmond", myMap.getAllCitiesOnTheMap());
    }

    @Test
    void getNeighbours() {
        Set<String> bostonS = new HashSet<>();
        myMap.addRoad("Boston, New York"); bostonS.add("New York");
        myMap.addRoad("Boston, Albany"); bostonS.add("Albany");
        myMap.addRoad("Philadelphia, Newark");
        myMap.addRoad("Boston, Richmond"); bostonS.add("Richmond");

        assertTrue(myMap.getNeighbours("Boston").containsAll(bostonS));
    }
}