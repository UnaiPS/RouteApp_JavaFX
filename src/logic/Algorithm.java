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
            int currentCost = lastCost;
            String history = oldHistory;
            if (history.contains(Integer.toString(i))) {
                continue;
            }
            currentCost += matrix[Character.getNumericValue(history.charAt(history.length() - 1))][i];
            if (currentCost < minimumCost) {
                history += Integer.toString(i);
                if (history.length() < points) {
                    anotherLevel(currentCost, history);
                } else {
                    minimumCost = currentCost;
                    route = history;
                }
            }
        }
    }

}
