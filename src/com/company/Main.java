package com.company;

import java.sql.SQLOutput;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        System.out.println(Polynomial.add("4x^2", "2x^2 - 5 + 2x^-2"));
    }
}
