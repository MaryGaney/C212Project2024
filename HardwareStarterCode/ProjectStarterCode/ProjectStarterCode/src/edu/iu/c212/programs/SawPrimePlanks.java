package edu.iu.c212.programs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SawPrimePlanks {
    /*
        - run through number, check if prime loop between 0 - number and keep a count of factor, if there are only 2 factor then it is prime
        -master function feeds in number, and new list, calls helper
        -helper goes from 2 to length of the big ASS BOARD, and checks if the iterator is prime, and the length of the board is compostite, then we can add the number to the list
        -call helper again with the length being (big ass length / i) or (big ass length - i), list
        -break the function
    */

    /**
     * this method takes in an integer, this integer represents a plank length (composite number)
     * we find the smallest prime number factors, that when multiplied together, create this number
     * We want the resulting planks (after recursively finding the small prime factors) to be as long as possible
     * @param longPlankLength: the length of the original plank
     * @return a list of prime planks
     */
    public static List<Integer> getPlankLengths(int longPlankLength) {
        List<Integer> hold = helper(longPlankLength);

        //new array list to return
        ArrayList<Integer> h = new ArrayList<>();
        int amountOfPlank = 1;
        for(Integer l : hold){
            amountOfPlank *= l;
        }
        int plankLength = longPlankLength / amountOfPlank;
        for (int i = 0; i < amountOfPlank; i++) {
            h.add(plankLength);
        }
        // If no prime divisor found, return the plank length itself
        return h;

    }

    /**
     * build a list of the smallest prime divisors of the number
     * @param longPlankLength: the length of the plank, integer
     * @return: the list of the smallest prime divisors
     */
    public static List<Integer> helper(int longPlankLength){
        List<Integer> result = new ArrayList<>();

        if(isPrime(longPlankLength)){
            return new ArrayList<>();
        }

        int i = sawPlank(longPlankLength);
        result.add(i);
        result.addAll(helper(longPlankLength / i));
        return result;
    }

    /**
     * return the smallest prime divisor of the input integer
     * @param plankLength: integer to find the smallest prime divisor from
     * @return the smallest prime divisor
     */
    public static int sawPlank(int plankLength) {
        // Find the smallest divisor (composite factor) of the plank length
        for (int i = 2 ; i <= plankLength; i++) {
            if ((isPrime(i)) && (plankLength % i == 0)) {
                // If the divisor is prime, return it
                return i;
            }
        }
        return -1;
    }

    /**
     * checks if the integer plugged in is prime or not
     * @param num: integer plugged
     * @return boolean value, true if the number is prime
     */
    private static boolean isPrime(int num) {
        if (num < 2) {
            return false;
        }
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

}
