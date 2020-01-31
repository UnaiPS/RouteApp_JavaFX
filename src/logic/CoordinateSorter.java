package logic;

import java.util.ArrayList;

/**
 * The class that manages the order of the coordinates of the route.
 *
 * @author Jon Calvo Gaminde
 */
public class CoordinateSorter {

    /**
     * A method that sorts the directions in the list of strings using a pattern
     * string.
     *
     * @param coords The list of directions to sort, in string form.
     * @param pattern The string that dictates the order of the directions.
     * @return The sorted list of direction in string form.
     */
    public static ArrayList<String> sortByPattern(ArrayList<String> coords, String pattern) {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < pattern.length(); i++) {
            result.add(coords.get(Character.getNumericValue(pattern.charAt(i))));
        }
        return result;
    }
}
