package logic;

/**
 * This class contains the algorithm used to find the best route.
 *
 * @author Jon Calvo Gaminde
 */
public class Algorithm {

    private static int minimumCost;
    private static int[][] matrix;
    private static int points;
    private static String route;

    /**
     * A method that uses a matrix of cost values to get the best route using
     * brute-forced "branch and bound" algorithm.
     *
     * @param coordinateMatrix A matrix of cost values of the route directions.
     * @return A string with the order of the directions in the best route.
     */
    public static String getShortestRoute(int[][] coordinateMatrix) {
        minimumCost = Integer.MAX_VALUE;
        matrix = coordinateMatrix;
        points = coordinateMatrix.length;
        // The route must start with the origin, index 0.
        anotherLevel(0, "0");
        return route;
    }

    /**
     * A method that gets called by getShortestRoute one time and keeps calling
     * itself for every direction in the route.
     *
     * @param lastCost The cost it had in the last level.
     * @param oldHistory The string of directions from the last level.
     */
    private static void anotherLevel(int lastCost, String oldHistory) {
        for (int i = 1; i < points; i++) {
            // Get the current possible route.
            int currentCost = lastCost;
            String history = oldHistory;
            // It only needs to be one time in each direction.
            if (history.contains(Integer.toString(i))) {
                continue;
            }
            currentCost += matrix[Character.getNumericValue(history.charAt(history.length() - 1))][i];
            // Cut this route branch if it is too long.
            if (currentCost < minimumCost) {
                history += Integer.toString(i);
                // If it has not been in every direction, the method enters another level.
                if (history.length() < points) {
                    anotherLevel(currentCost, history);
                } else {
                    // The new best route is saved.
                    minimumCost = currentCost;
                    route = history;
                }
            }
        }
    }

}
