package com.example.loan;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Třída pro export splátkového kalendáře do CSV souboru.
 */
public class CsvExporter {

    /**
     * Exportuje seznam splátek do CSV souboru.
     *
     * @param schedule Seznam objektů Payment (splátkový kalendář).
     * @param filePath Cesta k souboru, kam se má CSV uložit.
     * @throws IOException Pokud dojde k chybě při zápisu do souboru.
     */
    public static void exportScheduleToCsv(List<Payment> schedule, String filePath) throws IOException {
        // Použití try-with-resources pro automatické uzavření PrintWriteru
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Zápis hlavičky CSV souboru
            writer.println("Month,Principal Paid,Interest Paid,Total Payment,Remaining Balance");

            // Zápis dat jednotlivých splátek
            for (Payment payment : schedule) {
                writer.printf("%d,%.2f,%.2f,%.2f,%.2f%n",
                              payment.getMonthNumber(),
                              payment.getPrincipalPaid(),
                              payment.getInterestPaid(),
                              payment.getTotalPayment(),
                              payment.getRemainingBalance());
            }
        }
    }
}