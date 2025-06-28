package com.example.loan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Třída reprezentující grafické uživatelské rozhraní (GUI) pro kalkulačku splátkového kalendáře.
 * Dědí od JFrame a poskytuje interaktivní prvky pro zadávání dat, zobrazení kalendáře,
 * souhrnných informací a koláčového grafu.
 */
public class LoanCalculatorUI extends JFrame {

    // Textová pole pro zadávání vstupních dat úvěru
    private JTextField principalField;
    private JTextField interestRateField;
    private JTextField loanTermField;
    // Tabulka pro zobrazení splátkového kalendáře
    private JTable paymentTable;
    // Model tabulky pro správu dat v paymentTable
    private DefaultTableModel tableModel;

    // Popisky pro zobrazení souhrnných výsledků
    private JLabel regularPaymentLabel;
    private JLabel totalPaidLabel;
    private JLabel totalInterestLabel;

    // Kontejner pro zobrazení koláčového grafu
    private JPanel chartPanelContainer;

    /**
     * Konstruktor třídy LoanCalculatorUI.
     * Inicializuje okno aplikace a jeho komponenty.
     */
    public LoanCalculatorUI() {
        setTitle("Kalkulačka splátkového kalendáře");
        setSize(1000, 700); // Zvětšená velikost okna pro umístění grafu
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrování okna na obrazovce

        initComponents();
    }

    /**
     * Inicializuje a rozmisťuje všechny GUI komponenty.
     */
    private void initComponents() {
        // Panel pro zadávání vstupních dat (výše úvěru, úroková sazba, doba splácení)
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Výše úvěru:"));
        principalField = new JTextField("100000"); // Předvyplněná hodnota
        inputPanel.add(principalField);

        inputPanel.add(new JLabel("Roční úrok (%):"));
        interestRateField = new JTextField("5.0"); // Předvyplněná hodnota
        inputPanel.add(interestRateField);

        inputPanel.add(new JLabel("Doba splácení (měsíce):"));
        loanTermField = new JTextField("120"); // Předvyplněná hodnota
        inputPanel.add(loanTermField);

        // Panel pro zobrazení souhrnných výsledků (pravidelná splátka, celkem zaplaceno, úroky)
        JPanel summaryPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Výsledek"));
        regularPaymentLabel = new JLabel("Pravidelná splátka: ");
        totalPaidLabel = new JLabel("Celkem zaplaceno: ");
        totalInterestLabel = new JLabel("Zaplacené úroky: ");
        summaryPanel.add(regularPaymentLabel);
        summaryPanel.add(totalPaidLabel);
        summaryPanel.add(totalInterestLabel);

        // Kontejner pro koláčový graf
        chartPanelContainer = new JPanel(new BorderLayout());
        chartPanelContainer.setBorder(BorderFactory.createTitledBorder("Rozdělení splátek"));
        // Nastavení preferované velikosti pro kontejner grafu, aby se graf správně zobrazil
        chartPanelContainer.setPreferredSize(new Dimension(400, 300));

        // Kontrolní panel, který sdružuje vstupní pole, souhrn a graf
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(inputPanel, BorderLayout.NORTH);
        controlPanel.add(summaryPanel, BorderLayout.CENTER);
        controlPanel.add(chartPanelContainer, BorderLayout.SOUTH);

        // Panel s tlačítky (Vypočítat, Exportovat)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton calculateButton = new JButton("Vypočítat splátkový kalendář");
        // Přidání posluchače událostí pro tlačítko Vypočítat
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateSchedule();
            }
        });
        buttonPanel.add(calculateButton);

        JButton exportButton = new JButton("Exportovat do CSV");
        // Přidání posluchače událostí pro tlačítko Exportovat
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportToCsv();
            }
        });
        buttonPanel.add(exportButton);

        // Panel s tabulkou splátkového kalendáře
        String[] columnNames = { "Měsíc", "Jistina", "Úrok", "Celkem", "Zbývá" };
        tableModel = new DefaultTableModel(columnNames, 0);
        paymentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(paymentTable); // Scrollbar pro tabulku

        // Hlavní rozložení okna JFrame
        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Vypočítá splátkový kalendář na základě uživatelského vstupu
     * a aktualizuje tabulku a souhrnné informace.
     */
    private void calculateSchedule() {
        try {
            // Získání a parsování vstupních dat z textových polí
            BigDecimal principal = new BigDecimal(principalField.getText());
            BigDecimal annualInterestRate = new BigDecimal(interestRateField.getText());
            int loanTermMonths = Integer.parseInt(loanTermField.getText());

            // Generování splátkového kalendáře pomocí LoanCalculator
            List<Payment> schedule = LoanCalculator.generatePaymentSchedule(principal, annualInterestRate,
                    loanTermMonths);

            // Vymazání stávajících dat z tabulky
            tableModel.setRowCount(0);

            BigDecimal totalInterest = BigDecimal.ZERO;
            BigDecimal totalPrincipalPaid = BigDecimal.ZERO;
            BigDecimal monthlyPayment = BigDecimal.ZERO;

            // Získání měsíční splátky z prvního záznamu (pokud existuje)
            if (!schedule.isEmpty()) {
                monthlyPayment = schedule.get(0).getTotalPayment();
            }

            // Přidání nových dat do tabulky a výpočet celkových úroků a zaplacené jistiny
            for (Payment payment : schedule) {
                tableModel.addRow(new Object[]{
                    payment.getMonthNumber(),
                    NumberFormatter.format(payment.getPrincipalPaid()),
                    NumberFormatter.format(payment.getInterestPaid()),
                    NumberFormatter.format(payment.getTotalPayment()),
                    NumberFormatter.format(payment.getRemainingBalance())
                });
                totalInterest = totalInterest.add(payment.getInterestPaid());
                totalPrincipalPaid = totalPrincipalPaid.add(payment.getPrincipalPaid());
            }

            // Výpočet celkové zaplacené částky
            BigDecimal totalPaid = monthlyPayment.multiply(new BigDecimal(loanTermMonths));

            // Aktualizace popisků se souhrnnými výsledky
            regularPaymentLabel.setText("Pravidelná splátka: " + NumberFormatter.formatCurrency(monthlyPayment));
            totalPaidLabel.setText("Celkem zaplaceno: " + NumberFormatter.formatCurrency(totalPaid));
            totalInterestLabel.setText("Zaplacené úroky: " + NumberFormatter.formatCurrency(totalInterest));

            // Aktualizace koláčového grafu
            updatePieChart(totalPrincipalPaid, totalInterest);

        } catch (NumberFormatException e) {
            // Zobrazení chybové zprávy, pokud je vstup neplatný
            JOptionPane.showMessageDialog(this,
                    "Chybný vstup: Zadejte platná čísla pro výši úvěru, úrokovou sazbu a dobu splácení.",
                    "Chyba vstupu", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Aktualizuje koláčový graf zobrazující rozdělení jistiny a úroků.
     *
     * @param principalAmount Celková zaplacená jistina.
     * @param interestAmount  Celkové zaplacené úroky.
     */
    private void updatePieChart(BigDecimal principalAmount, BigDecimal interestAmount) {
        // Vytvoření datové sady pro koláčový graf
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Jistina", principalAmount);
        dataset.setValue("Úroky", interestAmount);

        // Vytvoření koláčového grafu pomocí ChartFactory
        JFreeChart chart = ChartFactory.createPieChart(
                "Rozdělení celkových splátek", // Název grafu
                dataset, // Data
                true, // Zobrazit legendu
                true, // Zobrazit tooltips
                false // Nezobrazovat URL
        );

        // Vytvoření panelu pro zobrazení grafu
        ChartPanel chartPanel = new ChartPanel(chart);

        // Odebrání starého grafu a přidání nového do kontejneru
        chartPanelContainer.removeAll();
        chartPanelContainer.add(chartPanel, BorderLayout.CENTER);
        // Překreslení kontejneru, aby se zobrazil nový graf
        chartPanelContainer.revalidate();
        chartPanelContainer.repaint();
    }

    /**
     * Exportuje aktuální splátkový kalendář do CSV souboru.
     */
    private void exportToCsv() {
        // Kontrola, zda je tabulka prázdná
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Nelze exportovat prázdný splátkový kalendář.", "Chyba exportu",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Dialog pro delimiter
        String[] options = { "Čárka (,)", "Středník (;)", "Tabulátor", "Svislá čára (|)" };
        String[] delimiters = { ",", ";", "\t", "|" };
        int choice = JOptionPane.showOptionDialog(this,
                "Vyberte oddělovač pro CSV soubor:",
                "Výběr oddělovače",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (choice == -1)
            return; // Zrušeno
        String selectedDelimiter = delimiters[choice];

        // Otevření dialogu pro výběr souboru pro uložení
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Uložit splátkový kalendář jako CSV");
        // Filtr pro zobrazení pouze CSV souborů
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV soubory (*.csv)", "csv"));

        int userSelection = fileChooser.showSaveDialog(this);

        // Pokud uživatel vybral soubor a potvrdil uložení
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            // Přidání přípony .csv, pokud chybí
            if (!filePath.toLowerCase().endsWith(".csv")) {
                filePath += ".csv";
            }

            try {
                // Získání vstupních parametrů z textových polí
                BigDecimal principal = new BigDecimal(principalField.getText());
                BigDecimal annualInterestRate = new BigDecimal(interestRateField.getText());
                int loanTermMonths = Integer.parseInt(loanTermField.getText());

                // Znovu vygenerování splátkového kalendáře pro export (pro zajištění aktuálnosti dat)
                List<Payment> schedule = LoanCalculator.generatePaymentSchedule(principal, annualInterestRate,
                        loanTermMonths);

                // Export kalendáře do CSV souboru s metadata
                CsvExporter.exportScheduleToCsv(schedule, filePath, principal, annualInterestRate, loanTermMonths,
                        selectedDelimiter);

                JOptionPane.showMessageDialog(this, "Splátkový kalendář byl úspěšně exportován do souboru " + filePath,
                        "Export úspěšný", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                // Zobrazení chybové zprávy při problémech s exportem
                JOptionPane.showMessageDialog(this, "Chyba při exportu do CSV: " + e.getMessage(), "Chyba exportu",
                        JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                // Zobrazení chybové zprávy při neplatném formátu čísel
                JOptionPane.showMessageDialog(this, "Chyba při čtení dat pro export. Zkontrolujte vstupní pole.",
                        "Chyba", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
