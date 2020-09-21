package ak.challenge.cities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class container implementing basic "navigation" technique
 * <p>
 *     Internally uses graph traversal to find existing path
 *     Current implementation should be ok for relatively small files - internally almost everything is based on Strings
 *     If the real application needs to deal with thousands/millions "roads"
 *     the implementation could be improved by (not limited to):
 * <ul>
 *     <li>utilizing Streams to read file or maybe loading from the DB
 *     <li>use indices instead of Strings in some / most operations (and keeping Name<>index mapping for initial /final result)
 *     <li>use parallel() operations
 * </ul>
 * </p>
 * But one needs to be very careful with concurrent update/traverse of internal data structures
 */
@Component
@Scope("singleton")
public class CustomMap {
    private static Logger LOG = LoggerFactory.getLogger(CustomMap.class);

    private final Map<String, Set<String>> miniMap = new HashMap<>();

    /**
     * Load city pairs from the file
     * @param fName file path/name relative to work directory.
     * @throws Exception if the file is not found or cannot be read
     */
    public void loadFromFile(String fName) throws Exception {
        //TODO needs cleaning up to make a file location explicit/ unique
        LOG.debug("loading a map from {}", fName);
        URL res = getClass().getClassLoader().getResource(fName);
        InputStream is = null;
        try {
            if (res != null) {
                is = new FileInputStream(res.getFile());
            }
            else {
                is = new FileInputStream(fName);
            }
        } catch (FileNotFoundException e) {
            LOG.debug("current dir : " + (new File(".")).getAbsolutePath());
            throw new Exception("Unable to load from file "  + fName + "; " + e.getMessage());
        }
        if (is !=null) {
            Scanner scanner = new Scanner(is);
            while (scanner.hasNextLine()) {
               // - road / vertex and city names to collect unique names and index them
                String s = scanner.nextLine();
                addRoad(s);
            }
        }
        else
            throw new Exception("Unable to load from the file "  + fName);
    }

    /**
     * Add  "road" - from a String - two names comma separated
     * @param cities - a String with a comma , e.g. "Newark, New York"
     */
    public void addRoad(String cities) {
        String[] p = cities.split(",");
        addRoad(p[0].trim(), p[1].trim());
    }

    /**
     *
     * @return total number of cities currently loaded
     */
    public int getNumberOfCities() {
        return miniMap.keySet().size();
    }

    private void addCity(String name) {
        miniMap.putIfAbsent(name, new HashSet<>());
    }

    private void removeCity(String name) {
        //remove from all lists first
        miniMap.values().stream().forEach(list -> list.remove(name));
        //remove key
        miniMap.remove(name);
    }

    private void addRoad(String c1, String c2) {
        addCity(c1); addCity(c2);  // to ensure cities are there and avoid NPE below
        miniMap.get(c1).add(c2);
        miniMap.get(c2).add(c1);
    }

    /**
     * Probably main business API method.
     * Check if there is a road between two cities
     * @param city1
     * @param city2
     * @return true - if there is any way (e.g. through other cities), false otherwise
     */
    public boolean areConnected(String city1, String city2) {
        LOG.debug("trying to navigate from " + city1 + " to " + city2);
        if(!miniMap.containsKey(city1)) {
            LOG.error("Unknown point " + city1); return false;
        } else if(!miniMap.containsKey(city2)) {
            LOG.error("Unknown point " + city2); return false;
        }

        Set<String> route = breadthFirstTraversal(city1, city2);
        boolean routeFound = route.contains(city2);
        if (routeFound)
            LOG.info("Found route to navigate from " + city1 + " to " + city2 + "\n" + route);
        return routeFound;
    }

    /**
     * For future extension
     * @param c1
     * @param c2
     */
    private void removeRoad(String c1, String c2) {
        Set<String> roadsFromC1 = miniMap.get(c1);
        Set<String> roadsFromC2 = miniMap.get(c2);
        if(roadsFromC1 !=null) roadsFromC1.remove(c2);
        if(roadsFromC2 !=null) roadsFromC2.remove(c1);
    }

    private boolean areNeighbours(String city1, String city2) {
        Set<String> nb = miniMap.get(city1);
        if (nb == null) return false;
        return nb.contains(city2);
    }

    /**
     *  Utility method to be used by e.g. tests
     * @param city
     * @return set of city names adjacent  to <code>city</code>
     */
    Set<String> getNeighbours(String city) {
        return miniMap.get(city);
    }

    /**
     * Utility method
     * @return Sorted alphabetically
     */
    String getAllCitiesOnTheMap() {
        return miniMap.keySet().stream().sorted().collect(Collectors.joining(","));
    }

    /**
     * Graph traversal method, with optional <code>endPoint</code>
     * @param root  - starting point
     * @param endPoint  - optional, if null then the whole path will be collected, otherwise stops at <code>endPoint</code> if it is accessible
     * @return set of partial path : <code>root,...,endPoint</code> or full graph traversal :<code>root,...,`endPoint`,...</code>
     */
    private Set<String> breadthFirstTraversal(String root, String endPoint) {
        Set<String> visited = new LinkedHashSet<String>();
        Queue<String> queue = new LinkedList<String>();
        queue.add(root);
        visited.add(root);
        while (!queue.isEmpty()) {
            String city = queue.poll();
            for (String c : getNeighbours(city)) {
                if (!visited.contains(c)) {
                    visited.add(c);
                    queue.add(c);
                    if (endPoint != null && endPoint.equals(c)) {
                        LOG.debug("Path found to the end point: "  +visited);
                        return visited;
                    }
                }
            }
        }
        return visited;
    }

}
