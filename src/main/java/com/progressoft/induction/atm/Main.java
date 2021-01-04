package com.progressoft.induction.atm;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static Scanner s = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("Welcome!");
        ATMprocess();
        String str1 = s.next();
        while(str1.equalsIgnoreCase("yes")){
            ATMprocess();   
            str1 = s.next();
        }
        System.out.println("Thank you for using ATM");
    }
 
    private static BigDecimal amountInvalid (){
        BigDecimal amount = BigDecimal.ZERO;
        boolean amountIsInvalid = true;
        while (amountIsInvalid){
           try {
                amount = s.nextBigDecimal();
                amountIsInvalid = false;
           } catch(InputMismatchException e){
                System.out.println("Error! Incorrect value, please insert a numerical value");
                s.nextLine();   
           }
     }
        
    return amount;
    }
    
    public static void ATMprocess(){
        AtmImplementation atm = new AtmImplementation();
        System.out.println("Please input your account number: ");
        String accountNumber = s.next();
        System.out.println("Please input the amount: ");
        BigDecimal amount=amountInvalid();
        atm.withdraw(accountNumber, amount);
        System.out.println("Withdraw sucessfull! your balanace is now "+atm.getAccountBalance(accountNumber));
        System.out.println("Please type yes to proceed with a new transaction");
    }
}
