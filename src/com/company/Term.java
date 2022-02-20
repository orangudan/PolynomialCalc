package com.company;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Stores the exponent and coefficient for math terms and provides some basic functionality. Only integers are supported.
 *
 * @author dsadeghi
 */
public class Term implements Comparable<Term> {
    private int exponent;
    private int coefficient;

    /**
     * Constructor
     * @param coefficient
     * @param exponent
     */
    public Term(int coefficient, int exponent) {
        this.coefficient = coefficient;
        this.exponent = exponent;
    }

    /**
     * Default Constructor
     */
    public Term() {
        this(1,1);
    }

    /**
     * Copy constructor
     * @param copy
     */
    public Term(Term copy) {
        this.exponent = copy.exponent;
        this.coefficient = copy.coefficient;
    }

    /**
     * Constructor with parsed string parameter
     * @param term
     */
    public Term(String term) {
        //Checks for illegal characters
        Pattern illegalCharacters = Pattern.compile("[^x\\d+-^]");
        Matcher matcher = illegalCharacters.matcher(term);
        if (matcher.find()) {
            throw new RuntimeException("Illegal characters in polynomial");
        }

        //sets pattern to two groups: coefficient and exponent
        Pattern numbers = Pattern.compile("(^[+-]?\\d*)|([+-]?\\d)");
        matcher = numbers.matcher(term);
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
        else if (term.contains("x")) {
            exponent = 1;
        }
        else {
            exponent = 0;
        }
    }

    public int getExponent() {
        return exponent;
    }
    public int getCoefficient() {
        return coefficient;
    }

    public void setExponent(int exponent) {
        this.exponent = exponent;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    public void setAll(int coefficient, int exponent) {
        this.exponent = exponent;
        this.coefficient = coefficient;
    }

    /**
     * Adds together two (simplified) terms and returns the sum as another term
     * @param t1 First term added
     * @param t2 Second term added
     * @return Term object that represents their sum
     */
    public static Term add(Term t1, Term t2) {
        Term term1 = Term.getSimplifiedTerm(t1);
        Term term2 = Term.getSimplifiedTerm(t2);

        //If exponents dont match and neither are 0
        if (term1.getExponent() != term2.getExponent()) {
            if (term1.getCoefficient() != 0 && term2.getCoefficient() != 0) {
                throw new RuntimeException("Term's don't have matching exponent");
            }
        }
        //This logic works so that if one is a 0 term, then the nonzero exponent gets assigned. If both are 0, then 0 is assigned
        int exponent = (term1.getExponent() != 0) ? term1.getExponent() : term2.getExponent();
        int sumCoefficient = term1.getCoefficient() + term2.getCoefficient();

        return new Term(sumCoefficient, exponent);
    }

    /**
     * Compares the order between two terms. The terms are evaluated as simplified (exponent is 0 if coefficient is 0)
     * @param otherTerm
     * @return
     */
    public int compareTo(Term otherTerm) {
        Term thisTermSimplified = Term.getSimplifiedTerm(this);
        Term otherTermSimplified = Term.getSimplifiedTerm(otherTerm);

        return thisTermSimplified.exponent - otherTermSimplified.exponent;
    }

    /**
     * Simplifies the term by setting exponent to 0 if the coefficient is 0
     */
    public void simplify() {
        if (coefficient == 0) {
            exponent = 0;
        }
    }

    /**
     * Returns a simplified term based on the input term
     * @param term The term to be simplified
     * @return The simplified term
     */
    public static Term getSimplifiedTerm(Term term) {
        int coefficient = term.coefficient;
        int exponent = (coefficient == 0) ? 0 : term.exponent;

        return new Term(coefficient, exponent);
    }

    /**
     * Compares the (simplified) terms
     * @param other
     * @return
     */
    public boolean equals(Term other) {
        Term thisTerm = Term.getSimplifiedTerm(this);

        Term otherTerm = Term.getSimplifiedTerm(other);

        return (thisTerm.getCoefficient() == otherTerm.getCoefficient()) && (thisTerm.getExponent() == otherTerm.getExponent());
    }

    /**
     * Adds the coefficient of the input term if it has the same order
     * @param other
     */
    public void add(Term other) {
        if (this.exponent != other.exponent) {
            throw new RuntimeException("Can't add terms with different order");
        }
        coefficient += other.coefficient;
    }

    /**
     * Returns a string of the term in a simplified form. For example, if the exponent is 0, the "x^n" part is omitted.
     * @return
     */
    public String toString() {
        String str = "";
        if (coefficient >= 0) {
            str += "+";
        }
        str += String.valueOf(coefficient);

        /*
        if (coefficient != 1 || coefficient != -1 || exponent == 0) {
            str = String.valueOf(coefficient);
        }
         */
        //If e = 0, skips "x^". (ie. x^0 ->  If e = 1, skips only "^1"
        if (exponent != 0) {
            str += "x";
            if (exponent != 1) {
                str += "^" + exponent;
            }
        }
        return str;
    }

}
