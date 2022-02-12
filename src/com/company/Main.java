package com.company;

public class Main {

    public static void main(String[] args) {
        Polynomial polynomial1 = new Polynomial("-3 + 1x^3");
        Polynomial polynomial2 = new Polynomial("-6 + 2x^3");
        polynomial2.addPolynomial(polynomial1);


        System.out.println(polynomial2);
    }
}
