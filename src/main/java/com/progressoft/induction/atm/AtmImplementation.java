package com.progressoft.induction.atm;

import com.progressoft.induction.atm.exceptions.AccountNotFoundException;
import com.progressoft.induction.atm.exceptions.InsufficientFundsException;
import com.progressoft.induction.atm.exceptions.NotEnoughMoneyInATMException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AtmImplementation implements ATM, BankingSystem{
  HashMap<String, BigDecimal> accountBalanceMap = new HashMap<>();
  HashMap<Banknote, BigDecimal> ATMBalanceMap = new HashMap<>();
  
  public AtmImplementation(){
   initAccounts();
   initATM();
  }
  
  private void initAccounts(){
   accountBalanceMap.put("123456789", new BigDecimal("1000.0"));
   accountBalanceMap.put("111111111", new BigDecimal("1000.0"));
   accountBalanceMap.put("222222222", new BigDecimal("1000.0"));
   accountBalanceMap.put("333333333", new BigDecimal("1000.0"));
   accountBalanceMap.put("444444444", new BigDecimal("1000.0"));
  }
  
  private void initATM(){
   ATMBalanceMap.put(Banknote.FIVE_JOD, new BigDecimal("100"));
   ATMBalanceMap.put(Banknote.TEN_JOD, new BigDecimal("100"));
   ATMBalanceMap.put(Banknote.TWENTY_JOD, new BigDecimal("20"));
   ATMBalanceMap.put(Banknote.FIFTY_JOD, new BigDecimal("10"));
  }
  
  private boolean accExist(String accountNumber){
   return accountBalanceMap.containsKey(accountNumber);  
  }
  
  private boolean amountSuff(String accountNumber, BigDecimal amount){
   return getAccountBalance(accountNumber).compareTo(amount) >= 0;
  }
  
  private boolean ATMSuff(BigDecimal amount){
   BigDecimal ATMsum = BigDecimal.ZERO;
   for (Banknote i:ATMBalanceMap.keySet()){
        ATMsum = ATMsum.add(ATMBalanceMap.get(i).multiply(i.getValue()));
  }
   
   return ATMsum.compareTo(amount) >= 0;
  }
  
  public HashMap<Banknote, BigDecimal> giveBankNotes(BigDecimal amount){
   HashMap<Banknote, BigDecimal>  giveBankNotesMap = new HashMap<>();
   giveBankNotesMap.put(Banknote.FIFTY_JOD, new BigDecimal("0"));
   giveBankNotesMap.put(Banknote.TWENTY_JOD, new BigDecimal("0"));
   giveBankNotesMap.put(Banknote.TEN_JOD, new BigDecimal("0"));
   giveBankNotesMap.put(Banknote.FIVE_JOD, new BigDecimal("0"));
  
   if (!isEven(amount)){
     BigDecimal five = Banknote.FIVE_JOD.getValue();
     Banknote fiveNote = Banknote.FIVE_JOD;
     ATMBalanceMap.put(fiveNote, ATMBalanceMap.get(fiveNote).subtract(BigDecimal.ONE));
     amount = amount.subtract(five);
     giveBankNotesMap.put(fiveNote, giveBankNotesMap.get(fiveNote).add(BigDecimal.ONE));
   }
  
   for (Banknote i :giveBankNotesMap.keySet()){ 
     BigDecimal givenNumberofBankNotes = BigDecimal.ZERO; 
     if(amount.compareTo(BigDecimal.ZERO) == 1){ 
        givenNumberofBankNotes = subtractNumberOfBankNotesFromATM(i, amount); 
        giveBankNotesMap.put(i, givenNumberofBankNotes);
        amount = amount.subtract(givenNumberofBankNotes.multiply(i.getValue()));
     }
   }
  
  return giveBankNotesMap; 
 }
 
 public boolean isEven(BigDecimal amount){
   return amount.remainder(new BigDecimal(2)).compareTo(BigDecimal.ZERO) == 0;
 }
 
  public BigDecimal subtractNumberOfBankNotesFromATM(Banknote BN, BigDecimal amount){
   BigDecimal givenNumberofBankNotes = ATMBalanceMap.get(BN);
   BigDecimal BNValue = BN.getValue();
   BigDecimal amountBN = givenNumberofBankNotes.multiply(BNValue);
   if (amount.compareTo(amountBN) == 1){
     ATMBalanceMap.put(BN, BigDecimal.ZERO);
   } else {
     BigDecimal neededNumberofBankNotes = amount.divide(BNValue);
     ATMBalanceMap.put(BN, givenNumberofBankNotes.subtract(neededNumberofBankNotes));
     givenNumberofBankNotes = neededNumberofBankNotes;
   }
 
  return givenNumberofBankNotes;
 }

    @Override
    public List<Banknote> withdraw(String accountNumber, BigDecimal amount) {
     if (!accExist(accountNumber)){
         throw new AccountNotFoundException();
     } else if (!amountSuff(accountNumber, amount)){
         throw new InsufficientFundsException();
     } else if (!ATMSuff(amount)){
         throw new NotEnoughMoneyInATMException();
     }
     
     debitAccount(accountNumber, amount);
     HashMap<Banknote, BigDecimal>  giveBankNotesMap = new HashMap<>(giveBankNotes(amount)); 
     List <Banknote> receivedBanknotes = new ArrayList<>(); 
     
     for (Banknote i:giveBankNotesMap.keySet()){
        int bankNotesFreq = giveBankNotesMap.get(i).intValue();
        while(bankNotesFreq > 0){
          receivedBanknotes.add(i);
          bankNotesFreq--;
        }  
     }

    return receivedBanknotes;
    }
    
    @Override
    public BigDecimal getAccountBalance(String accountNumber) {  
     if (!accExist(accountNumber)){
         throw new AccountNotFoundException();
     }

    return accountBalanceMap.get(accountNumber); 
    }

    @Override
    public void debitAccount(String accountNumber, BigDecimal amount) {
     BigDecimal balanceAfterDebit = accountBalanceMap.get(accountNumber);
     balanceAfterDebit = balanceAfterDebit.subtract(amount);
     accountBalanceMap.put(accountNumber, balanceAfterDebit);   
    }
}
