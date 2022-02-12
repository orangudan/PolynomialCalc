package com.company;

/**
 * Stores a string representing a polynomial and provides a method to parse it term by term. Parsing is destructive
 * by deleting each extracted term from the string.
 * Can only parse strings with a certain format specification. There must be a '+' sign between every term
 * (ie. 2x^2 + -3x + 1)
 * Order of terms do not matter, neither does whitespace.
 * @author dsadeghi
 */
//TODO Implement proper exception handling
//TODO Make code not bad
public class TermParser {
    private String string;

    /**
     * Constructs the TermParser object. Removes all whitespace from the stored string
     * @param inputString - the string representing the polynomial
     */
    public TermParser(String inputString) {
        string = inputString;
        //Remove all whitespace
        string = string.replaceAll("\\s", "");

    }

    /**
     * Parses the string field for the next term and extracts it to be read and converted to a Term type
     *
     * @return A Term type containing the next parsed term found in the string
     * @throws Exception
     */
    //TODO Simplify parse algorithm (like using split method instead of substring)
    //It's easier to understand if you bang your head against the wall a couple times
    public Term parseNextTerm() throws Exception{
        if (string.isEmpty()) {
            return new Term(0,0);
        }

        int currentIndex = 0;
        //If the first character is a plus sign, delete it from the string
        if (string.charAt(0) == '+') {
            string = string.substring(1);
        }
        String termString;
        if (string.contains("+")) {
            //Extract and store the substring leading up to the first plus sign encountered
            termString = string.substring(0, string.indexOf('+'));
            //Then delete the substring from the string
            string = string.substring(string.indexOf('+'));
        }
        //If the string doesn't have a plus sign, assume all remaining characters belong to the last term
        else {
            termString = string;
            string = "";
        }

        /*
        *
        *
        *          PARSE COEFFICIENT
        *
        *
        *
         */
        //If the first character is not a minus sign or a number, throw error (it shouldn't run into the case of a plus sign since it gets removed earlier)
        if (termString.charAt(0) != '-' && !Character.isDigit(termString.charAt(0))) {
            throw new Exception("Failed to understand String");
        }
        //This factor keeps track of whether we are dealing with a positive or negative coefficient
        int coefficientFactor = 1;
        final int LAST_INDEX = termString.length() - 1;

        //If the first character is a minus sign, the coefficient factor is changed and the currentindex is iterated
        if (termString.charAt(0) == '-') {
            coefficientFactor = -1;
            currentIndex = (currentIndex < LAST_INDEX) ? currentIndex + 1 : currentIndex ;
        }
        //If next character is an int, add it to a string holding the coefficient and repeat.
        String coefficientString = "";
        while (Character.isDigit(termString.charAt(currentIndex))) {
            coefficientString += termString.charAt(currentIndex);
            if (currentIndex == LAST_INDEX) {
                break;
            }
            else {
                currentIndex++;
            }
        }

        //If no number has been read and the next character is 'x', assume coefficient is 1
        if (coefficientString.equalsIgnoreCase("") && termString.charAt(currentIndex) == 'x') {
            coefficientString = "1";
        }
        //else if no number has been read and the end of string has been reached, assume coefficient is 0
        else if (coefficientString.equalsIgnoreCase("") && currentIndex == termString.length() - 1) {
            coefficientString = "0";
        }

        int coefficient = Integer.parseInt(coefficientString) * coefficientFactor;

        /*
        *
        *
        *   PARSE EXPONENT
        *
        *
         */

        String exponentString = "";
        int exponentFactor = 1;
        //IF the next two characters are "x^"
        if (termString.charAt(currentIndex) == 'x' && termString.charAt(currentIndex+1) == '^') {
            currentIndex += 2;

            if (termString.charAt(currentIndex) == '-') {
                exponentFactor = -1;
                currentIndex = (currentIndex < LAST_INDEX) ? currentIndex + 1 : currentIndex;
            }
            else if (termString.charAt(currentIndex) == '+') {
                currentIndex = (currentIndex < LAST_INDEX) ? currentIndex + 1 : currentIndex;
            }

            //read the numbers to the end of the string
            while (Character.isDigit(termString.charAt(currentIndex))) {
                exponentString += termString.charAt(currentIndex);
                if (currentIndex == LAST_INDEX) {
                    break;
                }
                else {
                    currentIndex++;
                }
            }
            if (exponentString.equalsIgnoreCase("")) {
                throw new Exception("Failed to understand String");
            }
        }
        //Else if the next character is 'x' followed by end of string, assume order is 1
        else if (termString.charAt(currentIndex) == 'x' && currentIndex == termString.length() - 1) {
            exponentString = "1";
        }
        //Else if you are at the last index, assume the term has no 'x' and thus order is 0 (it's a constant)
        else if (currentIndex == LAST_INDEX) {
            exponentString = "0";
        }
        else {
            throw new Exception("Failed to understand String");
        }

        //If after all that, somehow the order is still empty, throw error
        if (exponentString.equalsIgnoreCase("")) {
            throw new Exception("Failed to understand String");
        }

        int exponent = Integer.parseInt(exponentString) * exponentFactor;

        return new Term(coefficient, exponent);
    }
}
