package com.library.controller;

import com.library.model.Book;
import com.library.model.Student;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class OverdueReportController {
    @FXML
    private Label currentDateLabel;
    
    @FXML
    private Text totalOverdueLabel;
    
    @FXML
    private Text totalFeesLabel;
    
    @FXML
    private Text averageDaysLabel;
    
    @FXML
    private TableView<OverdueBook> overdueTable;
    
    @FXML
    private TableColumn<OverdueBook, String> idColumn;
    
    @FXML
    private TableColumn<OverdueBook, String> titleColumn;
    
    @FXML
    private TableColumn<OverdueBook, String> userColumn;
    
    @FXML
    private TableColumn<OverdueBook, LocalDate> borrowDateColumn;
    
    @FXML
    private TableColumn<OverdueBook, LocalDate> dueDateColumn;
    
    @FXML
    private TableColumn<OverdueBook, Integer> daysLateColumn;
    
    @FXML
    private TableColumn<OverdueBook, Double> feeColumn;
    
    @FXML
    private PieChart categoryChart;
    
    @FXML
    private ListView<String> topUsersList;
    
    private static final double DAILY_LATE_FEE = 0.50; // R$0,50 por dia
    
    public static class OverdueBook {
        private final String id;
        private final String title;
        private final String category;
        private final String userName;
        private final LocalDate borrowDate;
        private final LocalDate dueDate;
        private final int daysLate;
        private final double fee;
        
        public OverdueBook(String id, String title, String category, String userName, 
                          LocalDate borrowDate, LocalDate dueDate) {
            this.id = id;
            this.title = title;
            this.category = category;
            this.userName = userName;
            this.borrowDate = borrowDate;
            this.dueDate = dueDate;
            
            // Calcular dias de atraso e multa
            this.daysLate = (int) ChronoUnit.DAYS.between(dueDate, LocalDate.now());
            this.fee = this.daysLate * DAILY_LATE_FEE;
        }
        
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getCategory() { return category; }
        public String getUserName() { return userName; }
        public LocalDate getBorrowDate() { return borrowDate; }
        public LocalDate getDueDate() { return dueDate; }
        public int getDaysLate() { return daysLate; }
        public double getFee() { return fee; }
    }
    
    @FXML
    private void initialize() {
        // Configurar data atual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        currentDateLabel.setText(LocalDate.now().format(formatter));
        
        // Configurar colunas da tabela
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        userColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUserName()));
        borrowDateColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getBorrowDate()));
        dueDateColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDueDate()));
        daysLateColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getDaysLate()).asObject());
        feeColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getFee()).asObject());
        
        // Formatadores para as colunas de data e valor
        borrowDateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
            }
        });
        
        dueDateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
            }
        });
        
        feeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double fee, boolean empty) {
                super.updateItem(fee, empty);
                if (empty || fee == null) {
                    setText(null);
                } else {
                    setText(String.format("R$ %.2f", fee));
                }
            }
        });
        
        // Carregar dados de exemplo
        loadSampleData();
    }
    
    private void loadSampleData() {
        // Dados fictícios para demonstração
        ObservableList<OverdueBook> overdueBooks = FXCollections.observableArrayList(
            new OverdueBook("B003", "1984", "Ficção", "João Silva", 
                           LocalDate.now().minusDays(25), LocalDate.now().minusDays(11)),
            new OverdueBook("B007", "O Senhor dos Anéis", "Fantasia", "Ana Oliveira", 
                           LocalDate.now().minusDays(35), LocalDate.now().minusDays(21)),
            new OverdueBook("B012", "Dom Quixote", "Clássico", "Carlos Santos", 
                           LocalDate.now().minusDays(18), LocalDate.now().minusDays(4)),
            new OverdueBook("B015", "Cem Anos de Solidão", "Ficção", "Mariana Costa", 
                           LocalDate.now().minusDays(22), LocalDate.now().minusDays(8)),
            new OverdueBook("B023", "Harry Potter e a Pedra Filosofal", "Fantasia", "Pedro Alves", 
                           LocalDate.now().minusDays(20), LocalDate.now().minusDays(6))
        );
        
        overdueTable.setItems(overdueBooks);
        
        // Atualizar estatísticas
        int totalBooks = overdueBooks.size();
        double totalFees = overdueBooks.stream().mapToDouble(OverdueBook::getFee).sum();
        double avgDays = overdueBooks.stream().mapToInt(OverdueBook::getDaysLate).average().orElse(0);
        
        totalOverdueLabel.setText(String.valueOf(totalBooks));
        totalFeesLabel.setText(String.format("R$ %.2f", totalFees));
        averageDaysLabel.setText(String.format("%.1f dias", avgDays));
        
        // Dados para o gráfico de pizza
        Map<String, Integer> categoryCounts = new HashMap<>();
        for (OverdueBook book : overdueBooks) {
            categoryCounts.put(book.getCategory(), categoryCounts.getOrDefault(book.getCategory(), 0) + 1);
        }
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        categoryChart.setData(pieChartData);
        
        // Dados para a lista de usuários com mais atrasos
        Map<String, Integer> userCounts = new HashMap<>();
        for (OverdueBook book : overdueBooks) {
            userCounts.put(book.getUserName(), userCounts.getOrDefault(book.getUserName(), 0) + 1);
        }
        
        List<Map.Entry<String, Integer>> userEntries = new ArrayList<>(userCounts.entrySet());
        userEntries.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
        
        ObservableList<String> topUsers = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : userEntries) {
            topUsers.add(entry.getKey() + " - " + entry.getValue() + " livro(s)");
        }
        
        topUsersList.setItems(topUsers);
    }
    
    @FXML
    private void handleExport() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exportar Relatório");
        alert.setHeaderText(null);
        alert.setContentText("Relatório exportado com sucesso para 'livros_atrasados.pdf'");
        alert.showAndWait();
    }
    
    @FXML
    private void handleNotifyUsers() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notificar Usuários");
        alert.setHeaderText(null);
        alert.setContentText("Notificações enviadas para 5 usuários com livros atrasados");
        alert.showAndWait();
    }
}