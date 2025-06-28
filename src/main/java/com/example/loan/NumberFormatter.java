package com.example.loan;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Utility třída pro české formátování čísel s mezerami jako oddělovači tisícovek.
 */
public class NumberFormatter {
    
    private static final DecimalFormat CZECH_FORMATTER;
    
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("cs", "CZ"));
        symbols.setGroupingSeparator(' ');  // Mezera jako oddělovač tisícovek
        symbols.setDecimalSeparator(',');   // Čárka jako desetinný oddělovač
        
        CZECH_FORMATTER = new DecimalFormat("#,##0.00", symbols);
        CZECH_FORMATTER.setGroupingSize(3); // Skupiny po 3 číslicích
    }
    
    /**
     * Formátuje BigDecimal na český formát s mezerami jako oddělovači tisícovek.
     * 
     * @param value Hodnota k formátování
     * @return Formátovaný řetězec
     */
    public static String format(BigDecimal value) {
        if (value == null) {
            return "0,00";
        }
        return CZECH_FORMATTER.format(value);
    }
    
    /**
     * Formátuje double na český formát s mezerami jako oddělovači tisícovek.
     * 
     * @param value Hodnota k formátování
     * @return Formátovaný řetězec
     */
    public static String format(double value) {
        return CZECH_FORMATTER.format(value);
    }
    
    /**
     * Formátuje číslo s měnou (Kč).
     * 
     * @param value Hodnota k formátování
     * @return Formátovaný řetězec s měnou
     */
    public static String formatCurrency(BigDecimal value) {
        return format(value) + " Kč";
    }
    
    /**
     * Formátuje číslo s měnou (Kč).
     * 
     * @param value Hodnota k formátování
     * @return Formátovaný řetězec s měnou
     */
    public static String formatCurrency(double value) {
        return format(value) + " Kč";
    }
} 