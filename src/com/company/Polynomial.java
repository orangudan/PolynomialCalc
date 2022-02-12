package com.company;


import java.util.ArrayList;
import java.util.List;

//TODO sort the list of terms to decreasing order
//TODO Remove terms with 0 coefficient
//TODO Implement proper error handling

/**
 *Stores polynomials by using a List of Term objects. Provides functionality for adding terms and other polynomials
 * @see Term
 * @author dsadeghi
 */
public class Polynomial {
    private List<Term> termList = new ArrayList<Term>();

    /**
     * Uses TermParser to parse the input string and adds
     * @param inputString
     */
    public Polynomial(String inputString) {
        TermParser polynomialParser = new TermParser(inputString);
        Term zeroTerm = new Term(0,0);
        try {
            while(true) {
                Term nextTerm = polynomialParser.parseNextTerm();

                if (nextTerm.equals(zeroTerm))
                    break;

                termList.add(nextTerm);
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    /**
     * Adds input polynomial to calling polynomial object
     * @param other polynomial to be added
     */
    public void addPolynomial(Polynomial other) {
        for (Term t : other.termList) {
            addTerm(t);
        }

        return;
    }

    /**
     * Adds a term to the termlist or sums it with an existing term that has the same exponent
     * @param inputTerm
     */
    private void addTerm(Term inputTerm) {
        //search for term of equal exponent first
        for (Term t : termList) {
            //If there is a term with the same exponent, create a new term where the coefficient is the sum of their coefficients
            //then remove the old term
            if (t.getExponent() == inputTerm.getExponent()) {
                int exponent = t.getExponent();
                int summedCoefficient = t.getCoefficient() + inputTerm.getCoefficient();
                Term summedTerm = new Term(summedCoefficient, exponent);
                termList.add(summedTerm);
                termList.remove(t);
                return;
            }
        }

        //This should only be reached if there aren't any terms that match with the input term's order
        termList.add(inputTerm);
        return;
    }

    /**
     * Returns a formatted string representing the polynomial
     * @return String representing the polynomial
     */
    public String toString() {
        String str = "";
        int i = 0;
        for (Term t : termList) {
            str += t.toString();
            if (i++ != termList.size() - 1) {
                str += " + ";
            }
        }

        return str;
    }
}
