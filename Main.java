import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class Main extends Application {
    private Library library;
    private static String ITEMS_CSV = "data/items.csv";
    private static String MEMBERS_CSV = "data/members.csv";
    private static String LOGS_CSV = "data/logs.csv";
    private static String USERS_CSV = "data/users.csv";
    private TextArea itemStatusArea;
    private Map<String, String> users;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        library = new Library();
        users = new HashMap<>();
        loadUsers();
        loadFromCSV();

        Scene loginScene = createLoginScene();
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Perpustakaan Digital - Login");
        primaryStage.show();

        primaryStage.setOnCloseRequest(_ -> saveToCSV());
    }

    private Scene createLoginScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background-color: #f0f4f8;");

        Label header = new Label("Login Sistem Perpustakaan");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        grid.add(header, 0, 0, 2, 1);
        GridPane.setHalignment(header, HPos.CENTER);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");

        loginButton.setOnAction(_ -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (users.containsKey(username) && users.get(username).equals(password)) {
                Scene mainScene = createMainScene();
                primaryStage.setScene(mainScene);
                primaryStage.setTitle("Perpustakaan Digital");
                usernameField.clear();
                passwordField.clear();
                errorLabel.setText("");
            } else {
                errorLabel.setText("Username atau password salah.");
                usernameField.clear();
                passwordField.clear();
            }
        });

        Button registerButton = new Button("Registrasi");
        registerButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        registerButton.setOnAction(_ -> {
            Scene registerScene = createRegisterScene();
            primaryStage.setScene(registerScene);
            primaryStage.setTitle("Perpustakaan Digital - Registrasi");
        });

        grid.add(new Label("Username:"), 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(loginButton, 1, 3);
        grid.add(registerButton, 1, 4);
        grid.add(errorLabel, 0, 5, 2, 1);
        GridPane.setHalignment(registerButton, HPos.RIGHT);

        return new Scene(grid, 400, 500);
    }

    private Scene createRegisterScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-background-color: #f0f4f8;");

        Label header = new Label("Registrasi Pengguna Baru");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        grid.add(header, 0, 0, 2, 1);
        GridPane.setHalignment(header, HPos.CENTER);

        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Nama Lengkap");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Konfirmasi Password");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button registerButton = new Button("Daftar");
        registerButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        Button backButton = new Button("Kembali");
        backButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

        registerButton.setOnAction(_ -> {
            String fullName = fullNameField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                errorLabel.setText("Semua field harus diisi.");
            } else if (users.containsKey(username)) {
                errorLabel.setText("Username sudah terdaftar.");
            } else if (!password.equals(confirmPassword)) {
                errorLabel.setText("Password dan konfirmasi tidak cocok.");
            } else {
                try (PrintWriter pw = new PrintWriter(new FileWriter(USERS_CSV, true))) {
                    pw.println(username + "," + password + "," + fullName);
                } catch (IOException e) {
                    showAlert("Error", "Gagal menyimpan data pengguna: " + e.getMessage());
                    return;
                }
                users.put(username, password);
                showAlert("Note", "Registrasi berhasil! Silakan login.");
                Scene loginScene = createLoginScene();
                primaryStage.setScene(loginScene);
                primaryStage.setTitle("Perpustakaan Digital - Login");
            }
        });

        backButton.setOnAction(_ -> {
            Scene loginScene = createLoginScene();
            primaryStage.setScene(loginScene);
            primaryStage.setTitle("Perpustakaan Digital - Login");
        });

        grid.add(new Label("Nama Lengkap:"), 0, 1);
        grid.add(fullNameField, 1, 1);
        grid.add(new Label("Username:"), 0, 2);
        grid.add(usernameField, 1, 2);
        grid.add(new Label("Password:"), 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(new Label("Konfirmasi Password:"), 0, 4);
        grid.add(confirmPasswordField, 1, 4);
        grid.add(registerButton, 1, 5);
        grid.add(backButton, 0, 5);
        grid.add(errorLabel, 0, 6, 2, 1);
        GridPane.setHalignment(registerButton, HPos.RIGHT);
        GridPane.setHalignment(backButton, HPos.LEFT);

        return new Scene(grid, 400, 400);
    }

    private Scene createMainScene() {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f0f4f8;");

        HBox headerBox = new HBox(10);
        headerBox.setPadding(new Insets(20));
        headerBox.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-border-radius: 10;");

        Label header = new Label("Sistem Manajemen Perpustakaan");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        HBox.setHgrow(header, Priority.ALWAYS);
        headerBox.setAlignment(Pos.CENTER);

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

        logoutButton.setOnAction(_ -> {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Konfirmasi Logout");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Apakah Anda yakin ingin logout?");
            confirmAlert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    Scene loginScene = createLoginScene();
                    primaryStage.setScene(loginScene);
                    primaryStage.setTitle("Perpustakaan Digital - Login");
                }
            });
        });

        headerBox.getChildren().addAll(header, logoutButton);
        mainLayout.setTop(headerBox);

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                createItemTab(),
                createMemberTab(),
                createBorrowTab(),
                createReturnTab(),
                createLogTab());
        mainLayout.setCenter(tabPane);

        return new Scene(mainLayout, 500, 600);
    }

    private Tab createItemTab() {
        Tab tab = new Tab("Manajemen Item");
        tab.setClosable(false);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 10; -fx-background-radius: 10;");

        TextField titleField = new TextField();
        titleField.setPromptText("Judul");
        TextField idField = new TextField();
        idField.setPromptText("ID Item");
        TextField authorIssueField = new TextField();
        authorIssueField.setPromptText("Penulis (Buku) atau Nomor Edisi (Majalah)");
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Buku", "Majalah");
        typeCombo.setValue("Buku");

        Button addButton = new Button("Tambah Item");
        addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");

        itemStatusArea = new TextArea();
        itemStatusArea.setEditable(false);
        itemStatusArea.setPrefHeight(200);
        itemStatusArea.setText(library.getLibraryStatus());

        addButton.setOnAction(_ -> {
            try {
                int id = Integer.parseInt(idField.getText());
                try {
                    library.findItemById(id);
                    showAlert("Error", "ID Item sudah digunakan.");
                    return;
                } catch (NoSuchElementException e) {
                }
                String title = titleField.getText();
                LibraryItem item;
                if (typeCombo.getValue().equals("Buku")) {
                    item = new Book(title, id, authorIssueField.getText());
                } else {
                    item = new Magazine(title, id, Integer.parseInt(authorIssueField.getText()));
                }
                library.addItem(item);
                itemStatusArea.setText(library.getLibraryStatus());
                clearFields(titleField, idField, authorIssueField);
                showAlert("Note", "Item berhasil ditambahkan");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        grid.add(new Label("Jenis Item:"), 0, 0);
        grid.add(typeCombo, 1, 0);
        grid.add(new Label("Judul:"), 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(new Label("ID Item:"), 0, 2);
        grid.add(idField, 1, 2);
        grid.add(new Label("Penulis/Nomor Edisi:"), 0, 3);
        grid.add(authorIssueField, 1, 3);
        grid.add(addButton, 1, 4);
        grid.add(itemStatusArea, 0, 5, 2, 1);

        tab.setContent(grid);
        return tab;
    }

    private Tab createMemberTab() {
        Tab tab = new Tab("Manajemen Member");
        tab.setClosable(false);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 10; -fx-background-radius: 10;");

        TextField nameField = new TextField();
        nameField.setPromptText("Nama Member");
        TextField memberIdField = new TextField();
        memberIdField.setPromptText("ID Member");

        Button addButton = new Button("Tambah Member");
        addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");

        TextArea memberList = new TextArea();
        memberList.setEditable(false);
        memberList.setPrefHeight(200);
        memberList.setText(getMemberList());

        addButton.setOnAction(_ -> {
            try {
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    showAlert("Error", "Nama member tidak boleh kosong.");
                    return;
                }
                int id = Integer.parseInt(memberIdField.getText());
                if (library.findMemberById(id) != null) {
                    showAlert("Error", "ID Member sudah digunakan.");
                    return;
                }
                library.registerMember(new Member(name, id));
                memberList.setText(getMemberList());
                clearFields(nameField, memberIdField);
                showAlert("Note", "Member berhasil ditambahkan");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        grid.add(new Label("Nama:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("ID Member:"), 0, 1);
        grid.add(memberIdField, 1, 1);
        grid.add(addButton, 1, 2);
        grid.add(new Label("Daftar Member:"), 0, 3);
        grid.add(memberList, 0, 4, 2, 1);

        tab.setContent(grid);
        return tab;
    }

    private Tab createBorrowTab() {
        Tab tab = new Tab("Pinjam");
        tab.setClosable(false);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 10; -fx-background-radius: 10;");

        TextField memberIdField = new TextField();
        memberIdField.setPromptText("ID Member");
        TextField itemIdField = new TextField();
        itemIdField.setPromptText("ID Item");
        TextField daysField = new TextField();
        daysField.setPromptText("Jumlah Hari");

        Button borrowButton = new Button("Pinjam");
        borrowButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");

        borrowButton.setOnAction(_ -> {
            try {
                int memberId = Integer.parseInt(memberIdField.getText());
                int itemId = Integer.parseInt(itemIdField.getText());
                int days = Integer.parseInt(daysField.getText());

                if (days <= 0) {
                    showAlert("Error", "Jumlah hari harus lebih besar dari 0.");
                    return;
                }

                Member member = library.findMemberById(memberId);
                if (member == null)
                    throw new IllegalArgumentException("Member tidak ditemukan");
                LibraryItem item = library.findItemById(itemId);
                String result = member.borrow(item, days);
                library.getLogger().logActivity(result);
                itemStatusArea.setText(library.getLibraryStatus());
                clearFields(memberIdField, itemIdField, daysField);
                showAlert("Note", result);
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        grid.add(new Label("ID Member:"), 0, 0);
        grid.add(memberIdField, 1, 0);
        grid.add(new Label("ID Item:"), 0, 1);
        grid.add(itemIdField, 1, 1);
        grid.add(new Label("Jumlah Hari:"), 0, 2);
        grid.add(daysField, 1, 2);
        grid.add(borrowButton, 1, 3);

        tab.setContent(grid);
        return tab;
    }

    private Tab createReturnTab() {
        Tab tab = new Tab("Kembalikan");
        tab.setClosable(false);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 10; -fx-background-radius: 10;");

        TextField memberIdField = new TextField();
        memberIdField.setPromptText("ID Member");
        TextField itemIdField = new TextField();
        itemIdField.setPromptText("ID Item");
        TextField daysLateField = new TextField();
        daysLateField.setPromptText("Hari Keterlambatan");

        Button returnButton = new Button("Kembalikan");
        returnButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

        returnButton.setOnAction(_ -> {
            try {
                int memberId = Integer.parseInt(memberIdField.getText());
                int itemId = Integer.parseInt(itemIdField.getText());
                int daysLate = Integer.parseInt(daysLateField.getText());

                Member member = library.findMemberById(memberId);
                if (member == null)
                    throw new IllegalArgumentException("Member tidak ditemukan");

                LibraryItem item = library.findItemById(itemId);
                if (item == null) {
                    showAlert("Error", "Item tidak ditemukan.");
                    return;
                }

                String result = member.returnItem(item, daysLate);
                library.getLogger().logActivity(result);
                itemStatusArea.setText(library.getLibraryStatus());
                clearFields(memberIdField, itemIdField, daysLateField);
                showAlert("Note", result);
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        grid.add(new Label("ID Member:"), 0, 0);
        grid.add(memberIdField, 1, 0);
        grid.add(new Label("ID Item:"), 0, 1);
        grid.add(itemIdField, 1, 1);
        grid.add(new Label("Hari Keterlambatan:"), 0, 2);
        grid.add(daysLateField, 1, 2);
        grid.add(returnButton, 1, 3);

        tab.setContent(grid);
        return tab;
    }

    private Tab createLogTab() {
        Tab tab = new Tab("Log Aktivitas");
        tab.setClosable(false);

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 10; -fx-background-radius: 10;");

        TextArea logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setText(library.getAllLogs());

        Button refreshButton = new Button("Refresh Log");
        refreshButton.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-font-weight: bold;");
        refreshButton.setOnAction(_ -> logArea.setText(library.getAllLogs()));

        vbox.getChildren().addAll(new Label("Log Aktivitas:"), logArea, refreshButton);
        tab.setContent(vbox);
        return tab;
    }

    private void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_CSV))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2) {
                    users.put(data[0], data[1]);
                }
            }
        } catch (IOException e) {
            users.put("admin", "admin123");
        }
    }

    private void loadFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(ITEMS_CSV))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    int id = Integer.parseInt(data[1]);
                    LibraryItem item;
                    if (data[0].equals("Book")) {
                        item = new Book(data[2], id, data[3]);
                    } else if (data[0].equals("Magazine")) {
                        item = new Magazine(data[2], id, Integer.parseInt(data[3]));
                    } else {
                        continue;
                    }
                    if (data.length >= 5) {
                        item.setBorrowed(Boolean.parseBoolean(data[4]));
                    }
                    library.addItem(item);
                }
            }
        } catch (IOException e) {
        }

        try (BufferedReader br = new BufferedReader(new FileReader(MEMBERS_CSV))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",", -1);
                if (data.length >= 2) {
                    Member member = new Member(data[0], Integer.parseInt(data[1]));
                    library.registerMember(member);
                    if (data.length >= 3 && !data[2].isEmpty()) {
                        String[] borrowedIds = data[2].split(",");
                        for (String id : borrowedIds) {
                            if (!id.isEmpty()) {
                                try {
                                    LibraryItem item = library.findItemById(Integer.parseInt(id));
                                    member.addBorrowedItem(item);
                                } catch (Exception e) {
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
        }

        try (BufferedReader br = new BufferedReader(new FileReader(LOGS_CSV))) {
            StringBuilder logContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    logContent.append(line).append("\n");
                }
            }
            library.getLogger().setLogs(logContent.toString());
        } catch (IOException e) {
        }
    }

    private void saveToCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ITEMS_CSV))) {
            for (LibraryItem item : library.getItems()) {
                if (item instanceof Book) {
                    Book book = (Book) item;
                    pw.println("Book," + book.getItemId() + "," + book.getTitle() + "," + book.getAuthor() + ","
                            + book.isBorrowed());
                } else if (item instanceof Magazine) {
                    Magazine magazine = (Magazine) item;
                    pw.println("Magazine," + magazine.getItemId() + "," + magazine.getTitle() + ","
                            + magazine.getIssueNumber() + "," + magazine.isBorrowed());
                }
            }
        } catch (IOException e) {
            showAlert("Error", "Gagal menyimpan data item: " + e.getMessage());
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(MEMBERS_CSV))) {
            for (Member member : library.getMembers()) {
                StringBuilder borrowedIds = new StringBuilder();
                for (LibraryItem item : member.getBorrowedItems()) {
                    borrowedIds.append(item.getItemId()).append(",");
                }
                pw.println(member.getName() + "," + member.getMemberId() + "," + borrowedIds);
            }
        } catch (IOException e) {
            showAlert("Error", "Gagal menyimpan data member: " + e.getMessage());
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(LOGS_CSV))) {
            String logs = library.getAllLogs();
            if (!logs.isEmpty() && !logs.equals("Tidak Ada Log Aktivitas")) {
                for (String log : logs.split("\n")) {
                    if (!log.trim().isEmpty()) {
                        pw.println(log);
                    }
                }
            }
        } catch (IOException e) {
            showAlert("Error", "Gagal menyimpan data log: " + e.getMessage());
        }
    }

    private String getMemberList() {
        StringBuilder sb = new StringBuilder();
        for (Member member : library.getMembers()) {
            sb.append(member.getMemberId()).append(" - ").append(member.getName()).append("\n");
        }
        return sb.toString().isEmpty() ? "Tidak ada member." : sb.toString();
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}