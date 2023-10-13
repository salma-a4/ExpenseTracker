package dataModel;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseItem {
    private String shortDescription;
    private LocalDate dateOfExpense;
    private BigDecimal amount;

    public ExpenseItem(String shortDescription, LocalDate dateOfExpense, BigDecimal amount) {
        this.shortDescription = shortDescription;
        this.dateOfExpense = dateOfExpense;
        this.amount = amount;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public LocalDate getDateOfExpense() { return dateOfExpense;}

    public BigDecimal getAmount() { return amount; }

}
