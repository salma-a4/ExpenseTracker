import dataModel.ExpenseData;
import dataModel.ExpenseItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.DatagramSocket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class ExpenseDialog {
    @FXML
    private TextField shortDescriptionTF;

    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField amountTF;

    public ExpenseItem processResults() {
        String shortDescription = shortDescriptionTF.getText().trim();
        LocalDate date = datePicker.getValue();
        BigDecimal amount = new BigDecimal(amountTF.getText().trim());
        ExpenseItem newItem = new ExpenseItem(shortDescription, date, amount);
        ExpenseData.getInstance().addExpense(newItem);
        return newItem;
    }
}
