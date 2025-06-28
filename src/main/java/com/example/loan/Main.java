package com.example.loan;

import javax.swing.SwingUtilities;

/**
 * Hlavní třída aplikace pro spuštění kalkulačky splátkového kalendáře s grafickým uživatelským rozhraním (GUI).
 */
public class Main {
    /**
     * Vstupní bod aplikace.
     * Spustí GUI aplikaci v Event Dispatch Thread (EDT).
     *
     * @param args Argumenty příkazového řádku (nevyužité v této verzi).
     */
    public static void main(String[] args) {
        // Zajištění, že GUI bude spuštěno v Event Dispatch Thread pro bezpečnou manipulaci s komponentami Swing.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Vytvoření a zobrazení hlavního okna kalkulačky.
                new LoanCalculatorUI().setVisible(true);
            }
        });
    }
}