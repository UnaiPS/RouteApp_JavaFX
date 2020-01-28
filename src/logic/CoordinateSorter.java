/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;

/**
 *
 * @author Jon Calvo Gaminde
 */
public class CoordinateSorter {
    public static ArrayList<String> sortByPattern (ArrayList<String> coords, String pattern){
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < pattern.length(); i++) {
            result.add(coords.get(Character.getNumericValue(pattern.charAt(i))));
        }
        return result;
    }
}
