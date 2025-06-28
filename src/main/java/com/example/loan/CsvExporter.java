package com.example.loan;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Třída pro export splátkového kalendáře do CSV souboru.
 */
public class CsvExporter {

    /**
     * Exportuje seznam splátek do CSV souboru s metadata sekcí.
     *
     * @param schedule           Seznam objektů Payment (splátkový kalendář).
     * @param filePath           Cesta k souboru, kam se má CSV uložit.
     * @param principal          Výše úvěru.
     * @param annualInterestRate Roční úroková sazba v procentech.
     * @param loanTermMonths     Doba splácení úvěru v měsících.
     * @throws IOException Pokud dojde k chybě při zápisu do souboru.
     */
    public static void exportScheduleToCsv(List<Payment> schedule, String filePath,
            BigDecimal principal, BigDecimal annualInterestRate,
            int loanTermMonths, String delimiter) throws IOException {

        // Výpočet souhrnných údajů
        BigDecimal monthlyPayment = schedule.isEmpty() ? BigDecimal.ZERO : schedule.get(0).getTotalPayment();
        BigDecimal totalInterest = schedule.stream()
                .map(Payment::getInterestPaid)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPaid = monthlyPayment.multiply(new BigDecimal(loanTermMonths));

        // Aktuální datum a čas
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Použití try-with-resources pro automatické uzavření PrintWriteru
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Metadata sekce
            writer.println("# Informace o úvěru");
            writer.printf(Locale.US, "Výše úvěru%s%.2f%n", delimiter, principal);
            writer.printf(Locale.US, "Roční úrok (%%)%s%.2f%n", delimiter, annualInterestRate);
            writer.printf("Doba splácení (měsíce)%s%d%n", delimiter, loanTermMonths);
            writer.printf(Locale.US, "Pravidelná splátka%s%.2f%n", delimiter, monthlyPayment);
            writer.printf(Locale.US, "Zaplacené úroky%s%.2f%n", delimiter, totalInterest);
            writer.printf(Locale.US, "Celkem zaplaceno%s%.2f%n", delimiter, totalPaid);
            writer.printf("Datum exportu%s%s%n", delimiter, now.format(formatter));
            writer.println();

            // Záhlaví tabulky
            writer.println("# Splátkový kalendář");
            writer.printf("Měsíc%sJistina%sÚrok%sCelkem%sZbývá%n",
                    delimiter, delimiter, delimiter, delimiter);

            // Data s oddělovačem
            for (Payment payment : schedule) {
                writer.printf(Locale.US, "%d%s%.2f%s%.2f%s%.2f%s%.2f%n",
                        payment.getMonthNumber(), delimiter,
                        payment.getPrincipalPaid(), delimiter,
                        payment.getInterestPaid(), delimiter,
                        payment.getTotalPayment(), delimiter,
                        payment.getRemainingBalance());
            }
        }
    }
}