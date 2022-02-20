package com.company;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO Implement proper error handling

/**
 * Stores a polynomial. Provides functionality for addition
 * @author dsadeghi
 */
public class Polynomial {
    private List<Term> termList = new ArrayList<Term>();

    /**
     * Creates a Polynomial object by parsing the input string for terms and initializing them to a list
     * @param inputString string that represents a polynomial
     */
    public Polynomial(String inputString) {
        termList = parseString(inputString);
        simplify();
    }

    /**
     * Default Constructor for Polynomial "0"
     */
    public Polynomial() {
        termList.add(new Term(0,0));
    }

    /**
     * Creates a Polynomial object initialized by the input List<Term>
     * @param terms a generic list of Terms
     */
    public Polynomial(List<Term> terms) {
        termList = terms;
        simplify();
    }

    /**
     * Gets the sum of two polynomials without mutating the original polynomials
     * @param p1 the first polynomial
     * @param p2 the second polynomial
     * @return
     */
    public static Polynomial add(String p1, String p2) {
        Polynomial sum = new Polynomial();
        sum.add(p1);
        sum.add(p2);
        return sum;
    }
    /**
     * Adds input polynomial
     * @param other polynomial to be added
     */
    public void add(Polynomial other) {
        for (Term t : other.termList) {
            addTerm(t);
        }
    }

    /**
     * Parses input string then adds terms to the list
     * @param input
     */
    public void add(String input) {
        List<Term> terms = parseString(input);

        for (Term t : terms) {
            addTerm(t);
        }
    }


    /**
     * Adds a term to the list (or sums it with any existing like terms)
     * @param inputTerm
     */
    private void addTerm(Term inputTerm) {
        termList.add(inputTerm);
        simplify();
    }

    /**
     * Removes zero-terms, combines like terms, and finally sorts the list
     * Always use this after directly making any changes to the list of terms
     */
    private void simplify() {
        combineLikeTerms();
        sort();
    }

    /**
     * Dedicated procedure for simplify()
     */
    private void combineLikeTerms() {
        Term firstTerm, secondTerm, sumTerm;
        for (int i = 0; i < termList.size(); i++) {
            firstTerm = termList.get(i);
            for (int j = i+1; j < termList.size(); j++) {
                secondTerm = termList.get(j);
                if (firstTerm.compareTo(secondTerm) == 0 || firstTerm.getCoefficient() * secondTerm.getCoefficient() == 0) {
                    sumTerm = Term.add(firstTerm, secondTerm);
                    termList.add(sumTerm);
                    termList.remove(firstTerm);
                    termList.remove(secondTerm);
                    i = -1;
                    break;
                }
            }
        }
    }

    /**
     * Sorts list from greatest to least order
     */
    private void sort() {
        Collections.sort(termList, Collections.reverseOrder());
    }

    /**
     * Returns a formatted string representing the polynomial
     * @return String representing the polynomial
     */
    public String toString() {
        String str = "";
        Term currentTerm;
        for (int i = 0; i < termList.size(); i++) {
            currentTerm = termList.get(i);
            str += currentTerm.toString().substring(0,1) + ((i > 0) ? "\s" : "") + currentTerm.toString().substring(1) + ((i < termList.size()) ? "\s" : "");
        }

        return str;
    }

    /**
     * Parses a string for terms and returns them as a list of Term objects
     * @see Term
     * @param polyString the input string that represents the polynomial (ie. 3x^2 + 4x -3)
     * @return the list of Term objects parsed
     */
    private static List parseString(String polyString) {
        //Removes whitespace
        String compactPoly = polyString.replaceAll("\\s+", "");
        Matcher matcher;

        //Checks for illegal characters
        Pattern illegalCharacters = Pattern.compile("[^x\\d+-^]");
        matcher = illegalCharacters.matcher(compactPoly);
        if (matcher.find()) {
            throw new RuntimeException("Illegal characters in polynomial");
        }

        //Splits the string into terms
        String[] termStrings = compactPoly.split("\\b(?=[-+ \\t])");

        //sets pattern to two groups: coefficient and exponent
        Pattern numbers = Pattern.compile("(^[+-]?\\d*)|([+-]?\\d+)");

        //Need null value to allow for conditional assignment without needing to initialize variables to 0 (which is meaningful)
        Integer coefficient = null;
        Integer exponent = null;
        List<Term> termsList = new ArrayList<>();

        //term by term, parse for the coefficient and exponent and use them to create a Term object.
        for (String termStr : termStrings) {
            matcher = numbers.matcher(termStr);
            if (!matcher.find()) {
                throw new RuntimeException("Cannot parse string");
            }
            //If the extracted coefficient is just a "+" or empty string (ie. +x or x), coefficient is implicitly 1
            if (matcher.group(1).equals("+") || matcher.group(1).equals("")) {
                coefficient = 1;
            }
            //If the extracted coefficient is a "-" (ie. -x), coefficient is implicitly -1
            else if (matcher.group(1).equals("-")) {
                coefficient = -1;
            }
            else {
                coefficient = Integer.parseInt(matcher.group(1));
            }

            //If the second match is found, store it as the exponent. If no match is found and there is an "x", exponent is 1
            //If neither, exponent is 0
            if (matcher.find()) {
                exponent = Integer.parseInt(matcher.group(2));
            }
            else if (termStr.contains("x")) {
                exponent = 1;
            }
            else {
                exponent = 0;
            }

            termsList.add(new Term(coefficient, exponent));

        }

        return termsList;
    }
}
