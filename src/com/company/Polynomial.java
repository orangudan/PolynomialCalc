package com.company;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO Implement proper error handling

/**
 * Stores a polynomial. Provides functionality for addition
 * @author dsadeghi
 */
public class Polynomial {
    private List<Term> termList = new LinkedList<>();

    /**
     * Creates a Polynomial object by parsing the input string for terms and initializing them to a list
     * @param inputString string that represents a polynomial
     */
    public Polynomial(String inputString) {
        termList = parsePoly(inputString);
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
        termList = new ArrayList<>(terms);
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
     * @param polynomial polynomial to be added
     */
    public void add(Polynomial polynomial) {
        for (Term t : polynomial.termList) {
            addTerm(t);
        }
    }

    /**
     * Parses input string then adds terms to the list
     * @param input
     */
    public void add(String input) {
        List<Term> terms = parsePoly(input);

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
        //Combines like terms
        Term firstTerm, secondTerm, sumTerm;
        for (int i = 0; i < termList.size(); i++) {
            firstTerm = termList.get(i);
            for (int j = i+1; j < termList.size(); j++) {
                secondTerm = termList.get(j);
                if (firstTerm.isAddable(secondTerm)) {
                    //Take the sum of the two terms, append it to the list, and then remove the two terms from the addition
                    sumTerm = Term.add(firstTerm, secondTerm);
                    sumTerm.simplify();
                    termList.add(sumTerm);
                    termList.remove(firstTerm);
                    termList.remove(secondTerm);

                    //Whenever terms are added, resets the nested loop to start over again
                    i = -1;
                    break;
                }
            }
        }

        //Sort terms from greatest to least order
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
     * @param inputString String that represents the polynomial (ie. 3x^2 + 4x -3)
     * @return the list of Term objects parsed
     */
    private static List parsePoly(String inputString) {
        //Removes whitespace
        String compactPoly = inputString.replaceAll("\\s+", "");
        Matcher matcher;

        //Checks for illegal characters
        Pattern illegalCharacters = Pattern.compile("[^x\\d+-^]");
        matcher = illegalCharacters.matcher(compactPoly);
        if (matcher.find()) {
            throw new RuntimeException("Illegal characters in polynomial");
        }

        //Splits the string into substrings containing terms
        String[] termStrings = compactPoly.split("\\b(?=[-+ \\t])");

        List<Term> termsList = new ArrayList<>();

        //Parse each string for Terms and add them to the list
        for (String s : termStrings) {
            termsList.add(Term.parseTerm(s));
        }

        return termsList;
    }
}
