package com.example.loan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Třída pro výpočet splátkového kalendáře úvěru.
 * Používá anuitní metodu výpočtu.
 */
public class LoanCalculator {

    // Počet desetinných míst pro finanční výpočty
    private static final int DECIMAL_PLACES = 2;
    // Režim zaokrouhlování (zaokrouhlování nahoru při 0.5 a více)
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * Vypočítá měsíční anuitní splátku úvěru.
     *
     * @param principal Výše úvěru (jistina).
     * @param annualInterestRate Roční úroková sazba v procentech (např. 5.0 pro 5%).
     * @param loanTermMonths Doba splácení úvěru v měsících.
     * @return Měsíční splátka.
     */
    public static BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal annualInterestRate, int loanTermMonths) {
        // Speciální případ pro nulovou úrokovou sazbu
        if (annualInterestRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(new BigDecimal(loanTermMonths), DECIMAL_PLACES, ROUNDING_MODE);
        }

        // Měsíční úroková sazba (roční sazba / 1200 pro procenta)
        BigDecimal monthlyInterestRate = annualInterestRate.divide(new BigDecimal(1200), 10, ROUNDING_MODE);
        // Čitatel vzorce pro anuitní splátku
        BigDecimal numerator = principal.multiply(monthlyInterestRate)
                                        .multiply(monthlyInterestRate.add(BigDecimal.ONE).pow(loanTermMonths));
        // Jmenovatel vzorce pro anuitní splátku
        BigDecimal denominator = monthlyInterestRate.add(BigDecimal.ONE).pow(loanTermMonths).subtract(BigDecimal.ONE);

        // Výpočet a zaokrouhlení měsíční splátky
        return numerator.divide(denominator, DECIMAL_PLACES, ROUNDING_MODE);
    }

    /**
     * Generuje kompletní splátkový kalendář pro úvěr.
     *
     * @param principal Výše úvěru (jistina).
     * @param annualInterestRate Roční úroková sazba v procentech.
     * @param loanTermMonths Doba splácení úvěru v měsících.
     * @return Seznam objektů Payment, reprezentujících jednotlivé splátky.
     */
    public static List<Payment> generatePaymentSchedule(BigDecimal principal, BigDecimal annualInterestRate, int loanTermMonths) {
        List<Payment> schedule = new ArrayList<>();
        // Vypočítá měsíční splátku
        BigDecimal monthlyPayment = calculateMonthlyPayment(principal, annualInterestRate, loanTermMonths);
        // Počáteční zbývající zůstatek je roven jistině
        BigDecimal remainingBalance = principal;
        // Měsíční úroková sazba
        BigDecimal monthlyInterestRate = annualInterestRate.divide(new BigDecimal(1200), 10, ROUNDING_MODE);

        // Iteruje přes každý měsíc splácení
        for (int i = 1; i <= loanTermMonths; i++) {
            // Vypočítá úrok zaplacený v tomto měsíci
            BigDecimal interestPaid = remainingBalance.multiply(monthlyInterestRate).setScale(DECIMAL_PLACES, ROUNDING_MODE);
            // Vypočítá jistinu zaplacenou v tomto měsíci
            BigDecimal principalPaid = monthlyPayment.subtract(interestPaid);
            // Aktualizuje zbývající zůstatek
            remainingBalance = remainingBalance.subtract(principalPaid);

            // Upraví poslední splátku, aby se vyrovnaly chyby zaokrouhlování
            if (i == loanTermMonths) {
                principalPaid = principalPaid.add(remainingBalance);
                remainingBalance = BigDecimal.ZERO;
            }

            // Přidá novou splátku do kalendáře
            schedule.add(new Payment(i, principalPaid, interestPaid, monthlyPayment, remainingBalance));
        }

        return schedule;
    }
}