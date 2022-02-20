package com.company;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        boolean exit = false;
        Scanner input = new Scanner(System.in);
        List<Polynomial> polynomials = new ArrayList<Polynomial>();
        int selection;
        do {
            System.out.println("Polynomial Calculator");
            System.out.println();
            System.out.println("1.) Create new polynomial");
            System.out.println("2.) Delete a polynomial");
            System.out.println("3.) Show list of polynomials");
            System.out.println("4.) Add polynomials");
            System.out.println("5.) Quit");
            System.out.print(">");
            selection = input.nextInt();
            input.nextLine();

            switch (selection) {
                case 1:
                    System.out.println("Enter Polynomial");
                    System.out.print(">");
                    polynomials.add(new Polynomial(input.nextLine()));
                    break;
                case 2:
                    System.out.println("Choose a polynomial to delete");
                    System.out.println();
                    for (int i = 0; i < polynomials.size(); i++) {
                        System.out.println((i + 1) + ".) " + polynomials.get(i));
                    }
                    System.out.print(">");
                    int deleteIndex = input.nextInt();
                    input.nextLine();
                    polynomials.remove(deleteIndex);
                    break;
                case 3:
                    System.out.println("List of current polynomials");
                    System.out.println();
                    for (int i = 0; i < polynomials.size(); i++) {
                        System.out.println((i + 1) + ".) " + polynomials.get(i));
                    }
                    System.out.println("(Press any key to return to main menu)");
                    input.nextLine();
                    break;
                case 4:
                    System.out.println("The sum of all polynomials is: ");
                    Polynomial sum = new Polynomial();
                    for (Polynomial p : polynomials) {
                        sum.add(p);
                    }
                    System.out.println(sum);
                    System.out.println("(Press any key to return to main menu)");
                    input.nextLine();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid input. Please try again (press any key to continue)");
                    input.nextLine();
            }

        } while (!exit);
    }
}
