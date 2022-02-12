package com.company;

/**
 * Stores the exponent and coefficient for math terms. Provides limited functionality: reading contents, checking for
 * equality, and outputting the content as a string.
 * Supports only integer values
 *
 * @author dsadeghi
 */
public class Term {
    private int exponent;
    private int coefficient;

    public Term(int coefficient, int exponent) {
        this.exponent = exponent;
        this.coefficient = coefficient;
    }

    public int getExponent() {
        return exponent;
    }
    public int getCoefficient() {
        return coefficient;
    }
    public boolean equals(Term other) {
        return (exponent == other.getExponent()) && (coefficient == other.getCoefficient());
    }
    public String toString() {
        String str = String.valueOf(coefficient);
        if (exponent != 0) {
            str += "x^" + exponent;
        }

        return str;
    }

}
