package com.company;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Stores the exponent and coefficient for math terms and provides some basic functionality. Only integers are supported.
 * Zero terms (terms where coefficient = 0) are not simplified until performing operations with them or explicitly calling simplify()
 *
 * @author dsadeghi
 */
public class Term implements Comparable<Term> {
    private int exponent;
    private int coefficient;

    /**
     * Constructs a term with coefficient 1 and exponent 1 (basically 'x')
     */
    public Term() {
        this(1,1);
    }

    /**
     * Constructs a term with a given coefficient and exponent
     * @param coefficient The integer coefficient of the term (ie. 4x)
     * @param exponent The integer exponent of the term (ie. x^3)
     */
    public Term(int coefficient, int exponent) {
        this.coefficient = coefficient;
        this.exponent = exponent;
    }

    /**
     * Copies the exponent and coefficient of a given Term
     * @param copy the Term object to be copied
     */
    public Term(Term copy) {
        this.exponent = copy.exponent;
        this.coefficient = copy.coefficient;
    }

    /**
     * Constructs a term by parsing the input string
     * @param string the string to be parsed
     */
    public Term(String string) {
        this(parseTerm(string));
    }

    /**
     * Parses a string for a term and returns one as a Term object if found
     * @param string The string containing a term
     * @return The Term object parsed
     */
    public static Term parseTerm(String string) {
        //Checks for illegal characters
        Pattern illegalCharacters = Pattern.compile("[^x\\d+-^]");
        Matcher matcher = illegalCharacters.matcher(string);
        if (matcher.find()) {
            throw new RuntimeException("Illegal characters in polynomial");
        }

        //sets pattern to two groups: coefficient and exponent
        Pattern numbers = Pattern.compile("(^[+-]?\\d*)|([+-]?\\d+)");
        matcher = numbers.matcher(string);
        if (!matcher.find()) {
            throw new RuntimeException("Cannot parse string");
        }

        int coefficient;
        //If the extracted coefficient is just a "+" or empty string (ie. +x or x), coefficient is implicitly 1
        if (matcher.group(1).equals("+") || matcher.group(1).equals("")) {
            coefficient = 1;
        }
        //If the extracted coefficient is a "-" (ie. -x), coefficient is implicitly -1
        else if (matcher.group(1).equals("-")) {
            coefficient = -1;
        }
        //otherwise the coefficient is the match
        else {
            coefficient = Integer.parseInt(matcher.group(1));
        }

        //If the second match is found, store it as the exponent. If no match is found and there is an "x", exponent is 1
        //If neither, exponent is 0
        int exponent;
        if (matcher.find()) {
            exponent = Integer.parseInt(matcher.group(2));
        }
        else if (string.contains("x")) {
            exponent = 1;
        }
        else {
            exponent = 0;
        }

        return new Term(coefficient, exponent);
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

    /**
     * Sets both the coefficient and exponent of the calling Term
     * @param coefficient The integer coefficient
     * @param exponent The integer exponent
     */
    public void setAll(int coefficient, int exponent) {
        this.exponent = exponent;
        this.coefficient = coefficient;
    }

    /**
     * The class-wide condition used to check whether two terms can be added together
     * @param term The term used to check if addition can be performed
     * @return returns true if they can be added and false if not
     */
    public boolean isAddable(Term term) {
        return compareTo(term) == 0 || coefficient == 0 || term.coefficient == 0;
    }

    /**
     * Adds the input term to the calling term
     * @param term
     */
    public void add(Term term) {
        //if the exponents are different and neither have a 0 coefficient, throw exception
        if (!isAddable(term)) {
            throw new RuntimeException("Terms can't be added");
        }
        //In the condition you are adding, for example, 0x^3 + 5x^2, the result should have the exponent of the nonzero term
        if (coefficient == 0 && term.coefficient != 0) {
            exponent = term.exponent;
        }

        coefficient += term.coefficient;
    }

    /**
     * Returns the sum of the two input terms
     * @param firstTerm First term added
     * @param secondTerm Second term added
     * @return Term object that represents their sum
     */
    public static Term add(Term firstTerm, Term secondTerm) {
        //Deep copy first term, add second term to it, then return that sum
        Term sumTerm = new Term(firstTerm);
        sumTerm.add(secondTerm);
        return sumTerm;
    }

    /**
     * Compares the order between two terms (as simplified)
     * @param term the Term to be compared
     * @return The difference between their order
     */
    public int compareTo(Term term) {
        //Because exponent of Terms are mutable without conditions, you can have 0 terms with nonzero exponents (ie. 0x^3)
        //Comparison between such terms should still be considered "equal" in order in this comparison logic
        Term thisTermSimplified = getSimplifiedTerm();
        Term thatTermSimplified = term.getSimplifiedTerm();

        return thisTermSimplified.exponent - thatTermSimplified.exponent;
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
     * Returns a simplified term of the calling Term (Does not change state)
     * @return The simplified term
     */
    public Term getSimplifiedTerm() {
        Term simplifiedTerm = new Term(this);
        simplifiedTerm.simplify();
        return simplifiedTerm;
    }

    /**
     * Compares the (simplified) terms
     * @param other
     * @return
     */
    public boolean equals(Term other) {
        Term thisTerm = getSimplifiedTerm();

        Term otherTerm = other.getSimplifiedTerm();

        return (thisTerm.getCoefficient() == otherTerm.getCoefficient()) && (thisTerm.getExponent() == otherTerm.getExponent());
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
