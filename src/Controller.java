import dataModel.ExpenseData;
import dataModel.ExpenseItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Controller {
    @FXML
    private TableView<ExpenseItem> table;
    @FXML
    private TableColumn<ExpenseItem, String> shortDescColumn;
    @FXML
    private TableColumn<ExpenseItem, LocalDate> dateColumn;
    @FXML
    private TableColumn<ExpenseItem, BigDecimal> amountColumn;

    //borderpane gets references from its parent, by getScene() - to get the correct window
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ContextMenu listContextMenu;

    public void initialize() throws IOException {
        //to delete an item
        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ExpenseItem item = table.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });
        //when you right-click the context menu appears
        listContextMenu.getItems().addAll(deleteMenuItem);

        shortDescColumn.setCellValueFactory(new PropertyValueFactory<>("shortDescription"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfExpense"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        table.setItems(ExpenseData.getInstance().getExpenses());

        //change the look and feel of a UI, prioritise items
        table.setRowFactory(new Callback<TableView<ExpenseItem>, TableRow<ExpenseItem>>() {
            @Override
            public TableRow<ExpenseItem> call(TableView<ExpenseItem> param) {
                TableRow<ExpenseItem> cell = new TableRow<ExpenseItem>();
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) ->{
                            if (isNowEmpty){
                                cell.setContextMenu(null);
                            }
                            else{
                                cell.setContextMenu(listContextMenu);
                            }
                        });
                return cell;
            }
        });
    }

    @FXML
    public void showNewItemDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add a new expense");
        dialog.setHeaderText("Use this dialog to add a new expense");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("expenseItemDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ExpenseDialog controller = fxmlLoader.getController();
            ExpenseItem newItem = controller.processResults();
            table.getSelectionModel().select(newItem);
        }
    }

    public void deleteItem(ExpenseItem item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Expense Item");
        alert.setHeaderText("Delete item: " + item.getShortDescription());
        alert.setContentText("Do you want to delete? Press OK or cancel");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            ExpenseData.getInstance().deleteExpenseItem(item);
        }
    }
}

