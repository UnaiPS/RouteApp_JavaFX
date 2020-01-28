/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;


/**
 *
 * @author Jon Calvo Gaminde
 */
public class Algorithm {
    private static int minimumCost;
    private static int[][] matrix;
    private static int points;
    private static String route;
    
    public static String getShortestRoute (int[][] coordinateMatrix){
        minimumCost = Integer.MAX_VALUE;
        matrix = coordinateMatrix;
        points = coordinateMatrix.length;
        anotherLevel(0, "0");
        return route;
    }
    private static void anotherLevel (int lastCost, String oldHistory) {
        for (int i = 1; i < points; i++) {
            int currentCost = lastCost;
            String history = oldHistory;
            if (history.contains(Integer.toString(i))) 
                continue;
            currentCost += matrix[Character.getNumericValue(history.charAt(history.length()-1))][i];
            if (currentCost < minimumCost){
                history += Integer.toString(i);
                if (history.length() < points)
                    anotherLevel(currentCost, history);
                else {
                    minimumCost = currentCost;
                    route = history;
                } 
            }
        }
    }
    
}
