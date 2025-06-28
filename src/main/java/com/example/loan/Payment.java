package com.example.loan;

import java.math.BigDecimal;

/**
 * Třída reprezentující jednu splátku úvěru.
 * Obsahuje detaily o měsíčním čísle, zaplacené jistině, zaplaceném úroku,
 * celkové měsíční splátce a zbývajícím zůstatku úvěru.
 */
public class Payment {
    // Číslo měsíce splátky
    private final int monthNumber;
    // Částka jistiny zaplacená v dané splátce
    private final BigDecimal principalPaid;
    // Částka úroku zaplacená v dané splátce
    private final BigDecimal interestPaid;
    // Celková měsíční splátka (jistina + úrok)
    private final BigDecimal totalPayment;
    // Zbývající zůstatek úvěru po této splátce
    private final BigDecimal remainingBalance;

    /**
     * Konstruktor pro vytvoření nové instance splátky.
     *
     * @param monthNumber Číslo měsíce splátky.
     * @param principalPaid Částka jistiny zaplacená v dané splátce.
     * @param interestPaid Částka úroku zaplacená v dané splátce.
     * @param totalPayment Celková měsíční splátka.
     * @param remainingBalance Zbývající zůstatek úvěru.
     */
    public Payment(int monthNumber, BigDecimal principalPaid, BigDecimal interestPaid, BigDecimal totalPayment, BigDecimal remainingBalance) {
        this.monthNumber = monthNumber;
        this.principalPaid = principalPaid;
        this.interestPaid = interestPaid;
        this.totalPayment = totalPayment;
        this.remainingBalance = remainingBalance;
    }

    // Gettery pro přístup k datům splátky
    public int getMonthNumber() {
        return monthNumber;
    }

    public BigDecimal getPrincipalPaid() {
        return principalPaid;
    }

    public BigDecimal getInterestPaid() {
        return interestPaid;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    /**
     * Přepis metody toString pro lepší reprezentaci objektu Payment.
     * @return Řetězcová reprezentace objektu Payment.
     */
    @Override
    public String toString() {
        return "Payment{" +
               "monthNumber=" + monthNumber +
               ", principalPaid=" + principalPaid +
               ", interestPaid=" + interestPaid +
               ", totalPayment=" + totalPayment +
               ", remainingBalance=" + remainingBalance +
               '}';
    }
}