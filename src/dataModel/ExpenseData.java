package dataModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Currency;
import java.math.BigDecimal;

public class ExpenseData {
    private static ExpenseData instance = new ExpenseData();

    private static String expensesFilename = "Expense.txt";
    //used to bind the data instead of adding it explicitly
    private ObservableList<ExpenseItem> expenses;
    private DateTimeFormatter formatter;

    public static ExpenseData getInstance() {
        return instance;
    }

    private ExpenseData() {formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");}

    public ObservableList<ExpenseItem> getExpenses() {
        return expenses;
    }

    public void addExpense(ExpenseItem item){
        expenses.add(item);
    }

    public void loadExpenses() throws IOException {
        //allows us to have an arrayList in this format for FX - increased performance
        expenses = FXCollections.observableArrayList();
        Path path = Paths.get(expensesFilename);
        BufferedReader br = Files.newBufferedReader(path);

        String input;

        try {
            while ((input = br.readLine()) != null) {
                String[] itemPieces = input.split("\t");

                String shortDescription = itemPieces[0];
                String dateString = itemPieces[1];
                String amount = itemPieces[2];

                LocalDate date = LocalDate.parse(dateString, formatter);
                BigDecimal validAmount = new BigDecimal(amount);
                ExpenseItem expenseItem = new ExpenseItem(shortDescription, date, validAmount);
                expenses.add(expenseItem);
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

    public void storeExpenses() throws IOException {
        Path path = Paths.get(expensesFilename);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try {
            Iterator<ExpenseItem> it = expenses.iterator();
            while ((it.hasNext())) {
                ExpenseItem expenseItem = it.next();
                bw.write(String.format("%s\t%S\t%s",
                        expenseItem.getShortDescription(),
                        expenseItem.getDateOfExpense().format(formatter),
                        expenseItem.getAmount()));
                bw.newLine();
            }
        } finally {
            {
                if (bw != null) {
                    bw.close();
                }
            }
        }
    }

    public void deleteExpenseItem(ExpenseItem expenseItem){
        expenses.remove(expenseItem);
    }

}
