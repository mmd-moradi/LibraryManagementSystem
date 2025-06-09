package com.library.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class PopularBooksController {
    @FXML
    private Label currentDateLabel;
    
    @FXML
    private ComboBox<String> periodComboBox;
    
    @FXML
    private Text totalBorrowsLabel;
    
    @FXML
    private Text mostPopularLabel;
    
    @FXML
    private Text topCategoryLabel;
    
    @FXML
    private TableView<PopularBook> popularBooksTable;
    
    @FXML
    private TableColumn<PopularBook, Integer> rankingColumn;
    
    @FXML
    private TableColumn<PopularBook, String> idColumn;
    
    @FXML
    private TableColumn<PopularBook, String> titleColumn;
    
    @FXML
    private TableColumn<PopularBook, String> authorColumn;
    
    @FXML
    private TableColumn<PopularBook, String> categoryColumn;
    
    @FXML
    private TableColumn<PopularBook, Integer> borrowCountColumn;
    
    @FXML
    private TableColumn<PopularBook, LocalDate> lastBorrowedColumn;
    
    @FXML
    private BarChart<String, Number> popularityChart;
    
    @FXML
    private ListView<String> trendsList;
    
    public static class PopularBook {
        private final int ranking;
        private final String id;
        private final String title;
        private final String author;
        private final String category;
        private final int borrowCount;
        private final LocalDate lastBorrowed;
        
        public PopularBook(int ranking, String id, String title, String author, 
                          String category, int borrowCount, LocalDate lastBorrowed) {
            this.ranking = ranking;
            this.id = id;
            this.title = title;
            this.author = author;
            this.category = category;
            this.borrowCount = borrowCount;
            this.lastBorrowed = lastBorrowed;
        }
        
        public int getRanking() { return ranking; }
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getCategory() { return category; }
        public int getBorrowCount() { return borrowCount; }
        public LocalDate getLastBorrowed() { return lastBorrowed; }
    }
    
    @FXML
    private void initialize() {
        // Configurar data atual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        currentDateLabel.setText(LocalDate.now().format(formatter));
        
        // Configurar ComboBox de período
        periodComboBox.setItems(FXCollections.observableArrayList(
                "Últimos 30 dias", "Últimos 90 dias", "Último ano", "Todos os tempos"));
        periodComboBox.getSelectionModel().select(0);
        
        // Configurar colunas da tabela
        rankingColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getRanking()).asObject());
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        authorColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAuthor()));
        categoryColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
        borrowCountColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getBorrowCount()).asObject());
        lastBorrowedColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLastBorrowed()));
        
        // Formatador para a coluna de data
        lastBorrowedColumn.setCellFactory(column -> new TableCell<>() {
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
        
        // Adicionar listener para o ComboBox de período
        periodComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadDataForPeriod(newVal);
            }
        });
        
        // Carregar dados iniciais
        loadDataForPeriod("Últimos 30 dias");
    }
    
    private void loadDataForPeriod(String period) {
        // Dados fictícios para demonstração
        ObservableList<PopularBook> books = FXCollections.observableArrayList();
        
        if ("Últimos 30 dias".equals(period)) {
            books.addAll(
                new PopularBook(1, "B008", "Harry Potter e a Pedra Filosofal", "J.K. Rowling", "Fantasia", 32, LocalDate.now().minusDays(2)),
                new PopularBook(2, "B015", "Cem Anos de Solidão", "Gabriel García Márquez", "Ficção", 28, LocalDate.now().minusDays(3)),
                new PopularBook(3, "B003", "1984", "George Orwell", "Ficção", 25, LocalDate.now().minusDays(1)),
                new PopularBook(4, "B010", "O Pequeno Príncipe", "Antoine de Saint-Exupéry", "Infantil", 23, LocalDate.now().minusDays(5)),
                new PopularBook(5, "B007", "O Senhor dos Anéis", "J.R.R. Tolkien", "Fantasia", 21, LocalDate.now().minusDays(4)),
                new PopularBook(6, "B012", "Dom Quixote", "Miguel de Cervantes", "Clássico", 18, LocalDate.now().minusDays(7)),
                new PopularBook(7, "B025", "A Culpa é das Estrelas", "John Green", "Romance", 17, LocalDate.now().minusDays(6)),
                new PopularBook(8, "B017", "A Metamorfose", "Franz Kafka", "Ficção", 15, LocalDate.now().minusDays(8)),
                new PopularBook(9, "B030", "O Alquimista", "Paulo Coelho", "Ficção", 13, LocalDate.now().minusDays(10)),
                new PopularBook(10, "B022", "Orgulho e Preconceito", "Jane Austen", "Romance", 12, LocalDate.now().minusDays(9))
            );
            
            totalBorrowsLabel.setText("204");
        } else if ("Últimos 90 dias".equals(period)) {
            books.addAll(
                new PopularBook(1, "B008", "Harry Potter e a Pedra Filosofal", "J.K. Rowling", "Fantasia", 78, LocalDate.now().minusDays(2)),
                new PopularBook(2, "B003", "1984", "George Orwell", "Ficção", 65, LocalDate.now().minusDays(1)),
                new PopularBook(3, "B007", "O Senhor dos Anéis", "J.R.R. Tolkien", "Fantasia", 62, LocalDate.now().minusDays(4)),
                new PopularBook(4, "B015", "Cem Anos de Solidão", "Gabriel García Márquez", "Ficção", 59, LocalDate.now().minusDays(3)),
                new PopularBook(5, "B010", "O Pequeno Príncipe", "Antoine de Saint-Exupéry", "Infantil", 54, LocalDate.now().minusDays(5)),
                new PopularBook(6, "B025", "A Culpa é das Estrelas", "John Green", "Romance", 48, LocalDate.now().minusDays(6)),
                new PopularBook(7, "B012", "Dom Quixote", "Miguel de Cervantes", "Clássico", 45, LocalDate.now().minusDays(7)),
                new PopularBook(8, "B017", "A Metamorfose", "Franz Kafka", "Ficção", 43, LocalDate.now().minusDays(8)),
                new PopularBook(9, "B022", "Orgulho e Preconceito", "Jane Austen", "Romance", 39, LocalDate.now().minusDays(9)),
                new PopularBook(10, "B030", "O Alquimista", "Paulo Coelho", "Ficção", 37, LocalDate.now().minusDays(10))
            );
            
            totalBorrowsLabel.setText("530");
        } else {
            // Dados para outros períodos
            books.addAll(
                new PopularBook(1, "B008", "Harry Potter e a Pedra Filosofal", "J.K. Rowling", "Fantasia", 145, LocalDate.now().minusDays(2)),
                new PopularBook(2, "B007", "O Senhor dos Anéis", "J.R.R. Tolkien", "Fantasia", 138, LocalDate.now().minusDays(4)),
                new PopularBook(3, "B003", "1984", "George Orwell", "Ficção", 132, LocalDate.now().minusDays(1)),
                new PopularBook(4, "B010", "O Pequeno Príncipe", "Antoine de Saint-Exupéry", "Infantil", 128, LocalDate.now().minusDays(5)),
                new PopularBook(5, "B015", "Cem Anos de Solidão", "Gabriel García Márquez", "Ficção", 125, LocalDate.now().minusDays(3)),
                new PopularBook(6, "B012", "Dom Quixote", "Miguel de Cervantes", "Clássico", 115, LocalDate.now().minusDays(7)),
                new PopularBook(7, "B025", "A Culpa é das Estrelas", "John Green", "Romance", 107, LocalDate.now().minusDays(6)),
                new PopularBook(8, "B022", "Orgulho e Preconceito", "Jane Austen", "Romance", 98, LocalDate.now().minusDays(9)),
                new PopularBook(9, "B017", "A Metamorfose", "Franz Kafka", "Ficção", 92, LocalDate.now().minusDays(8)),
                new PopularBook(10, "B030", "O Alquimista", "Paulo Coelho", "Ficção", 87, LocalDate.now().minusDays(10))
            );
            
            if ("Último ano".equals(period)) {
                totalBorrowsLabel.setText("1167");
            } else { // Todos os tempos
                totalBorrowsLabel.setText("2345");
            }
        }
        
        popularBooksTable.setItems(books);
        
        // Atualizar estatísticas
        if (!books.isEmpty()) {
            mostPopularLabel.setText(books.get(0).getTitle());
        }
        
        // Calcular categoria mais popular
        Map<String, Integer> categoryCounts = new HashMap<>();
        for (PopularBook book : books) {
            categoryCounts.put(book.getCategory(), 
                              categoryCounts.getOrDefault(book.getCategory(), 0) + book.getBorrowCount());
        }
        
        String topCategory = categoryCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
        
        topCategoryLabel.setText(topCategory);
        
        // Atualizar gráfico de barras
        popularityChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        
        // Mostrar apenas os 5 primeiros livros no gráfico
        for (int i = 0; i < Math.min(5, books.size()); i++) {
            PopularBook book = books.get(i);
            series.getData().add(new XYChart.Data<>(book.getTitle(), book.getBorrowCount()));
        }
        
        popularityChart.getData().add(series);
        
        // Atualizar lista de tendências
        ObservableList<String> trends = FXCollections.observableArrayList(
            "↑ Livros de Fantasia continuam populares",
            "↑ Aumento no interesse por Ficção Científica",
            "↑ Clássicos da literatura têm alta procura",
            "↓ Diminuição no empréstimo de livros técnicos",
            "→ Romance mantém popularidade constante"
        );
        
        trendsList.setItems(trends);
    }
    
    @FXML
    private void handleExport() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exportar Relatório");
        alert.setHeaderText(null);
        alert.setContentText("Relatório exportado com sucesso para 'livros_populares.pdf'");
        alert.showAndWait();
    }
    
    @FXML
    private void handleRecommendations() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Recomendações de Aquisição");
        alert.setHeaderText("Sugestões para Novas Aquisições");
        alert.setContentText("Com base nos dados de popularidade, recomendamos a aquisição de:\n\n" +
                "1. Mais exemplares de 'Harry Potter e a Pedra Filosofal'\n" +
                "2. Outros livros da série Harry Potter\n" +
                "3. Mais livros de Fantasia e Ficção\n" +
                "4. Livros similares aos mais populares");
        alert.showAndWait();
    }
}