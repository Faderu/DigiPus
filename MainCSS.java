import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class MainCSS extends Application {
    private Library library;
    private static String ITEMS_CSV = "data/items.csv";
    private static String MEMBERS_CSV = "data/members.csv";
    private static String LOGS_CSV = "data/logs.csv";
    private static String USERS_CSV = "data/users.csv";
    private Map<String, String> users;
    private Stage primaryStage;
    private TextArea itemStatusArea;
    private TextArea memberList;

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
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");

        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);

        Label icon = new Label("📚");
        icon.setFont(Font.font(40));

        Label title = new Label("Library Management System");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Sistem Login Administrator");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.WHITE);

        headerBox.getChildren().addAll(icon, title, subtitle);

        VBox loginCard = new VBox(20);
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setPadding(new Insets(40));
        loginCard.setStyle(
                "-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        loginCard.setMaxWidth(400);

        Label loginTitle = new Label("🔐 LOGIN ADMIN");
        loginTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        loginTitle.setTextFill(Color.DARKSLATEGRAY);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle("-fx-padding: 12; -fx-background-radius: 25; -fx-border-radius: 25;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-padding: 12; -fx-background-radius: 25; -fx-border-radius: 25;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button loginButton = new Button("🔑 LOGIN");
        loginButton.setStyle(
                "-fx-background-color: #2ed573; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 12 24; -fx-background-radius: 25;");
        loginButton.setOnAction(_ -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (users.containsKey(username) && users.get(username).equals(password)) {
                Scene mainScene = createMainScene();
                primaryStage.setScene(mainScene);
                primaryStage.setTitle("Perpustakaan Digital");
                usernameField.clear();
                passwordField.clear();
            } else {
                showAlert("Error", "Username atau password salah.");
                usernameField.clear();
                passwordField.clear();
            }
        });

        Button registerButton = new Button("📝 REGISTER");
        registerButton.setStyle(
                "-fx-background-color: #3742fa; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 12 24; -fx-background-radius: 25;");
        registerButton.setOnAction(_ -> {
            Scene registerScene = createRegisterScene();
            primaryStage.setScene(registerScene);
            primaryStage.setTitle("Perpustakaan Digital - Registrasi");
        });

        buttonBox.getChildren().addAll(loginButton, registerButton);

        loginCard.getChildren().addAll(loginTitle, usernameField, passwordField, buttonBox);
        root.getChildren().addAll(headerBox, loginCard);

        return new Scene(root, 800, 600);
    }

    private Scene createRegisterScene() {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background: linear-gradient(to bottom, #3742fa 0%, #2f3542 100%);");

        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);

        Label icon = new Label("📝");
        icon.setFont(Font.font(40));

        Label title = new Label("Registrasi Pengguna Baru");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("Daftarkan akun administrator baru");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.WHITE);

        headerBox.getChildren().addAll(icon, title, subtitle);

        VBox registerCard = new VBox(20);
        registerCard.setAlignment(Pos.CENTER);
        registerCard.setPadding(new Insets(40));
        registerCard.setStyle(
                "-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        registerCard.setMaxWidth(400);

        Label registerTitle = new Label("👤 REGISTER ADMIN");
        registerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        registerTitle.setTextFill(Color.DARKSLATEGRAY);

        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Nama Lengkap");
        fullNameField.setStyle("-fx-padding: 12; -fx-background-radius: 25; -fx-border-radius: 25;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle("-fx-padding: 12; -fx-background-radius: 25; -fx-border-radius: 25;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-padding: 12; -fx-background-radius: 25; -fx-border-radius: 25;");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Konfirmasi Password");
        confirmPasswordField.setStyle("-fx-padding: 12; -fx-background-radius: 25; -fx-border-radius: 25;");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button registerButton = new Button("✅ DAFTAR");
        registerButton.setStyle(
                "-fx-background-color: #2ed573; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 12 24; -fx-background-radius: 25;");
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
                showAlert("Sukses", "Registrasi berhasil! Silakan login.");
                Scene loginScene = createLoginScene();
                primaryStage.setScene(loginScene);
                primaryStage.setTitle("Perpustakaan Digital - Login");
            }
        });

        Button backButton = new Button("⬅️ KEMBALI");
        backButton.setStyle(
                "-fx-background-color: #57606f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 12 24; -fx-background-radius: 25;");
        backButton.setOnAction(_ -> {
            Scene loginScene = createLoginScene();
            primaryStage.setScene(loginScene);
            primaryStage.setTitle("Perpustakaan Digital - Login");
        });

        buttonBox.getChildren().addAll(registerButton, backButton);

        registerCard.getChildren().addAll(registerTitle, fullNameField, usernameField, passwordField,
                confirmPasswordField, errorLabel, buttonBox);
        root.getChildren().addAll(headerBox, registerCard);

        return new Scene(root, 800, 700);
    }

    private Scene createMainScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background: linear-gradient(to bottom, #ff6b6b 0%, #ee5a24 100%);");

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10, 20, 10, 20));
        header.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 10;");

        Label headerTitle = new Label("👨‍💼 Dashboard Administrator");
        headerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        headerTitle.setTextFill(Color.WHITE);

        Button logoutButton = new Button("Keluar");
        logoutButton.setStyle(
                "-fx-background-color: #c0392b; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 15;");
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

        header.getChildren().addAll(headerTitle, logoutButton);

        Label dashTitle = new Label("Dashboard Administrator");
        dashTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        dashTitle.setTextFill(Color.WHITE);
        dashTitle.setAlignment(Pos.CENTER);

        GridPane menuGrid = new GridPane();
        menuGrid.setHgap(20);
        menuGrid.setVgap(20);
        menuGrid.setAlignment(Pos.CENTER);

        Button addItemButton = createMenuButton("📚", "Tambah Item", "Menambah buku/item baru");
        addItemButton.setOnAction(_ -> {
            Scene addItemScene = createAddItemScene();
            primaryStage.setScene(addItemScene);
            primaryStage.setTitle("Tambah Item");
        });

        Button addMemberButton = createMenuButton("👥", "Tambah Anggota", "Mendaftarkan anggota baru");
        addMemberButton.setOnAction(_ -> {
            Scene addMemberScene = createAddMemberScene();
            primaryStage.setScene(addMemberScene);
            primaryStage.setTitle("Tambah Anggota");
        });

        Button borrowItemButton = createMenuButton("📖", "Pinjam Item", "Memproses peminjaman");
        borrowItemButton.setOnAction(_ -> {
            Scene borrowScene = createBorrowScene();
            primaryStage.setScene(borrowScene);
            primaryStage.setTitle("Pinjam Item");
        });

        Button returnItemButton = createMenuButton("↩️", "Kembalikan Item", "Memproses pengembalian");
        returnItemButton.setOnAction(_ -> {
            Scene returnScene = createReturnScene();
            primaryStage.setScene(returnScene);
            primaryStage.setTitle("Kembalikan Item");
        });

        Button logActivityButton = createMenuButton("📝", "Log Aktivitas", "Riwayat semua aktivitas");
        logActivityButton.setOnAction(_ -> {
            Scene logScene = createLogScene();
            primaryStage.setScene(logScene);
            primaryStage.setTitle("Log Aktivitas");
        });

        Button statusButton = createMenuButton("📊", "Status Perpustakaan", "Lihat statistik perpustakaan");
        statusButton.setOnAction(_ -> {
            Scene statusScene = createLibraryStatusScene();
            primaryStage.setScene(statusScene);
            primaryStage.setTitle("Status Perpustakaan");
        });

        menuGrid.add(addItemButton, 0, 0);
        menuGrid.add(addMemberButton, 1, 0);
        menuGrid.add(borrowItemButton, 0, 1);
        menuGrid.add(returnItemButton, 1, 1);
        menuGrid.add(logActivityButton, 0, 2);
        menuGrid.add(statusButton, 1, 2);

        root.getChildren().addAll(header, dashTitle, menuGrid);

        return new Scene(root, 800, 700);
    }

    private Button createMenuButton(String icon, String title, String description) {
        VBox buttonContent = new VBox(10);
        buttonContent.setAlignment(Pos.CENTER);
        buttonContent.setPadding(new Insets(20));
        buttonContent.setStyle(
                "-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        buttonContent.setPrefSize(200, 120);

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(32));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.DARKSLATEGRAY);

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 10));
        descLabel.setTextFill(Color.GRAY);

        buttonContent.getChildren().addAll(iconLabel, titleLabel, descLabel);

        Button button = new Button();
        button.setGraphic(buttonContent);
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        button.setOnMouseEntered(_ -> buttonContent.setStyle(
                "-fx-background-color: #f8f9fa; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);"));
        button.setOnMouseExited(_ -> buttonContent.setStyle(
                "-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"));

        return button;
    }

    private Scene createAddItemScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background: linear-gradient(to right, #6b5b95, #a17e9b);");

        Label title = new Label("Form Tambah Item Baru");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);
        title.setAlignment(Pos.CENTER);

        VBox formCard = new VBox(15);
        formCard.setPadding(new Insets(20));
        formCard.setStyle(
                "-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        formCard.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Judul");
        TextField titleField = new TextField();
        titleField.setPromptText("Judul");
        titleField.setStyle(
                "-fx-padding: 12; -fx-background-radius: 25; -fx-border-radius: 25; -fx-border-color: #3498db;");

        Label authorIssueLabel = new Label("Pengarang");
        TextField authorIssueField = new TextField();
        authorIssueField.setPromptText("Pengarang");
        authorIssueField.setStyle(
                "-fx-padding: 12; -fx-background-radius: 25; -fx-border-radius: 25; -fx-border-color: #3498db;");

        Label idLabel = new Label("ID");
        TextField idField = new TextField();
        idField.setPromptText("ID");
        idField.setStyle(
                "-fx-padding: 12; -fx-background-radius: 25; -fx-border-radius: 25; -fx-border-color: #3498db;");

        Label categoryLabel = new Label("Jenis Item");
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("Buku", "Majalah");
        categoryCombo.setValue("Buku");
        categoryCombo.setStyle(
                "-fx-padding: 12; -fx-background-radius: 25; -fx-border-radius: 25; -fx-border-color: #3498db;");

        categoryCombo.setOnAction(_ -> {
            if (categoryCombo.getValue().equals("Majalah")) {
                authorIssueLabel.setText("Nomor Edisi");
                authorIssueField.setPromptText("Nomor Edisi");
            } else {
                authorIssueLabel.setText("Pengarang");
                authorIssueField.setPromptText("Pengarang");
            }
            authorIssueField.clear();
        });

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button addButton = new Button("Simpan Item");
        addButton.setStyle(
                "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 25;");
        addButton.setOnAction(_ -> {
            try {
                int id = Integer.parseInt(idField.getText());
                if (id < 1) {
                    showAlert("Error", "ID Item harus lebih besar dari atau sama dengan 1.");
                    return;
                }
                try {
                    library.findItemById(id);
                    showAlert("Error", "ID Item sudah digunakan.");
                    return;
                } catch (NoSuchElementException e) {
                }
                String itemTitle = titleField.getText();
                LibraryItem item;
                if (categoryCombo.getValue().equals("Majalah")) {
                    int issueNumber = Integer.parseInt(authorIssueField.getText());
                    item = new Magazine(itemTitle, id, issueNumber);
                } else {
                    item = new Book(itemTitle, id, authorIssueField.getText());
                }
                library.addItem(item);
                itemStatusArea.setText(library.getLibraryStatus());
                clearFields(titleField, idField, authorIssueField);
                showAlert("Sukses", "Item berhasil ditambahkan");
            } catch (NumberFormatException ex) {
                showAlert("Error",
                        categoryCombo.getValue().equals("Majalah") ? "Nomor Edisi atau ID harus berupa angka."
                                : "ID atau input tidak valid.");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        Button backButton = new Button("Tutup");
        backButton.setStyle(
                "-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 25;");
        backButton.setOnAction(_ -> {
            Scene mainScene = createMainScene();
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Perpustakaan Digital");
        });

        buttonBox.getChildren().addAll(addButton, backButton);

        itemStatusArea = new TextArea();
        itemStatusArea.setEditable(false);
        itemStatusArea.setPrefHeight(200);
        itemStatusArea.setText(library.getLibraryStatus());
        itemStatusArea.setStyle("-fx-background-radius: 10; -fx-border-radius: 10;");

        formCard.getChildren().addAll(titleLabel, titleField, authorIssueLabel, authorIssueField, idLabel, idField,
                categoryLabel, categoryCombo, buttonBox);
        root.getChildren().addAll(title, formCard, itemStatusArea);

        return new Scene(root, 800, 700);
    }

    private Scene createAddMemberScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background: linear-gradient(to right, #ff7e5f, #feb47b);");

        Label title = new Label("Form Tambah Anggota Baru");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);
        title.setAlignment(Pos.CENTER);

        VBox formCard = new VBox(15);
        formCard.setPadding(new Insets(20));
        formCard.setStyle(
                "-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        formCard.setAlignment(Pos.CENTER);

        TextField nameField = new TextField();
        nameField.setPromptText("Nama Member");
        nameField.setStyle(
                "-fx-padding: 12; -fx-background-radius: 25; -fx-border-radius: 25; -fx-border-color: #e74c3c;");

        TextField memberIdField = new TextField();
        memberIdField.setPromptText("ID");
        memberIdField.setStyle(
                "-fx-padding: 12; -fx-background-radius: 25; -fx-border-radius: 25; -fx-border-color: #e74c3c;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button addButton = new Button("Simpan Anggota");
        addButton.setStyle(
                "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 25;");
        addButton.setOnAction(_ -> {
            try {
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    showAlert("Error", "Nama anggota tidak boleh kosong.");
                    return;
                }
                int id = Integer.parseInt(memberIdField.getText());
                if (id < 1) {
                    showAlert("Error", "ID Member harus lebih besar dari atau sama dengan 1.");
                    return;
                }
                if (library.findMemberById(id) != null) {
                    showAlert("Error", "ID Member sudah digunakan.");
                    return;
                }
                library.registerMember(new Member(name, id));
                memberList.setText(getMemberList());
                clearFields(nameField, memberIdField);
                showAlert("Sukses", "Anggota berhasil ditambahkan");
            } catch (NumberFormatException ex) {
                showAlert("Error", "ID Member harus berupa angka yang valid.");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        Button backButton = new Button("Tutup");
        backButton.setStyle(
                "-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 25;");
        backButton.setOnAction(_ -> {
            Scene mainScene = createMainScene();
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Perpustakaan Digital");
        });

        buttonBox.getChildren().addAll(addButton, backButton);

        memberList = new TextArea();
        memberList.setEditable(false);
        memberList.setPrefHeight(200);
        memberList.setText(getMemberList());
        memberList.setStyle("-fx-background-radius: 10; -fx-border-radius: 10;");

        formCard.getChildren().addAll(new Label("Nama"), nameField, new Label("ID"), memberIdField, buttonBox);
        root.getChildren().addAll(title, formCard, memberList);

        return new Scene(root, 800, 500);
    }

    private Scene createBorrowScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background: linear-gradient(to right, #3498db, #8e44ad);");

        Label title = new Label("Form Pinjam Item");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);
        title.setAlignment(Pos.CENTER);

        VBox formCard = new VBox(15);
        formCard.setPadding(new Insets(20));
        formCard.setStyle(
                "-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        formCard.setAlignment(Pos.CENTER);

        TextField memberIdField = new TextField();
        memberIdField.setPromptText("ID Member");
        memberIdField.setStyle(
                "-fx-padding: 12; -fx-background-radius: 25; -fx-border-radius: 25; -fx-border-color: #2980b9;");

        TextField itemIdField = new TextField();
        itemIdField.setPromptText("ID Item");
        itemIdField.setStyle(
                "-fx-padding: 12; -fx-background-radius: 25; -fx-border-radius: 25; -fx-border-color: #2980b9;");

        TextField daysField = new TextField();
        daysField.setPromptText("Jumlah Hari");
        daysField.setStyle(
                "-fx-padding: 12; -fx-background-radius: 25; -fx-border-radius: 25; -fx-border-color: #2980b9;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button borrowButton = new Button("Pinjam");
        borrowButton.setStyle(
                "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 25;");
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
                if (itemStatusArea != null)
                    itemStatusArea.setText(library.getLibraryStatus());
                clearFields(memberIdField, itemIdField, daysField);
                showAlert("Sukses", result);
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        Button backButton = new Button("Tutup");
        backButton.setStyle(
                "-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 25;");
        backButton.setOnAction(_ -> {
            Scene mainScene = createMainScene();
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Perpustakaan Digital");
        });

        buttonBox.getChildren().addAll(borrowButton, backButton);

        formCard.getChildren().addAll(new Label("ID Member"), memberIdField, new Label("ID Item"), itemIdField,
                new Label("Jumlah Hari"), daysField, buttonBox);
        root.getChildren().addAll(title, formCard);

        return new Scene(root, 800, 450);
    }

    private Scene createReturnScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background: linear-gradient(to right, #2ecc71, #27ae60);");

        Label title = new Label("Form Kembalikan Item");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);
        title.setAlignment(Pos.CENTER);

        VBox formCard = new VBox(15);
        formCard.setPadding(new Insets(20));
        formCard.setStyle(
                "-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        formCard.setAlignment(Pos.CENTER);

        TextField memberIdField = new TextField();
        memberIdField.setPromptText("ID Member");
        memberIdField.setStyle(
                "-fx-padding: 12; -fx-background-radius: 25; -fx-border-radius: 25; -fx-border-color: #27ae60;");

        TextField itemIdField = new TextField();
        itemIdField.setPromptText("ID Item");
        itemIdField.setStyle(
                "-fx-padding: 12; -fx-background-radius: 25; -fx-border-radius: 25; -fx-border-color: #27ae60;");

        TextField daysLateField = new TextField();
        daysLateField.setPromptText("Hari Keterlambatan");
        daysLateField.setStyle(
                "-fx-padding: 12; -fx-background-radius: 25; -fx-border-radius: 25; -fx-border-color: #27ae60;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button returnButton = new Button("Kembalikan");
        returnButton.setStyle(
                "-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 25;");
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
                if (itemStatusArea != null)
                    itemStatusArea.setText(library.getLibraryStatus());
                clearFields(memberIdField, itemIdField, daysLateField);
                showAlert("Sukses", result);
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        Button backButton = new Button("Tutup");
        backButton.setStyle(
                "-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 25;");
        backButton.setOnAction(_ -> {
            Scene mainScene = createMainScene();
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Perpustakaan Digital");
        });

        buttonBox.getChildren().addAll(returnButton, backButton);

        formCard.getChildren().addAll(new Label("ID Member"), memberIdField, new Label("ID Item"), itemIdField,
                new Label("Hari Keterlambatan"), daysLateField, buttonBox);
        root.getChildren().addAll(title, formCard);

        return new Scene(root, 800, 450);
    }

    private Scene createLogScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background: linear-gradient(to right, #3498db, #2ecc71);");

        Label title = new Label("Riwayat Aktivitas Perpustakaan");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);
        title.setAlignment(Pos.CENTER);

        VBox logCard = new VBox(15);
        logCard.setPadding(new Insets(20));
        logCard.setStyle(
                "-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        logCard.setAlignment(Pos.CENTER);

        TextArea logArea = new TextArea();
        logArea.setText(library.getAllLogs());
        logArea.setEditable(false);
        logArea.setPrefRowCount(15);
        logArea.setStyle("-fx-background-radius: 10; -fx-border-radius: 10;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button refreshButton = new Button("Refresh Log");
        refreshButton.setStyle(
                "-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 25;");
        refreshButton.setOnAction(_ -> logArea.setText(library.getAllLogs()));

        Button backButton = new Button("Tutup");
        backButton.setStyle(
                "-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 25;");
        backButton.setOnAction(_ -> {
            Scene mainScene = createMainScene();
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Perpustakaan Digital");
        });

        buttonBox.getChildren().addAll(refreshButton, backButton);
        logCard.getChildren().addAll(logArea, buttonBox);
        root.getChildren().addAll(title, logCard);

        return new Scene(root, 800, 450);
    }

    private Scene createLibraryStatusScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background: linear-gradient(to right, #ff9966, #ff5e62);");

        Label title = new Label("Statistik Perpustakaan Digital");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);
        title.setAlignment(Pos.CENTER);

        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(20);
        statsGrid.setAlignment(Pos.CENTER);
        statsGrid.setPadding(new Insets(20));

        int totalBooks = library.getItems().size();
        int borrowedBooks = (int) library.getItems().stream().filter(LibraryItem::isBorrowed).count();
        int availableBooks = totalBooks - borrowedBooks;
        int totalMembers = library.getMembers().size();

        Button totalBooksButton = createStatButton("📚", "Total Buku", String.valueOf(totalBooks), "#4a90e2");
        Button borrowedBooksButton = createStatButton("📖", "Dipinjam", String.valueOf(borrowedBooks), "#ebd234");
        Button availableBooksButton = createStatButton("✔️", "Tersedia", String.valueOf(availableBooks), "#2ecc71");
        Button totalMembersButton = createStatButton("👥", "Total Anggota", String.valueOf(totalMembers), "#9b59b6");

        statsGrid.add(totalBooksButton, 0, 0);
        statsGrid.add(borrowedBooksButton, 1, 0);
        statsGrid.add(availableBooksButton, 0, 1);
        statsGrid.add(totalMembersButton, 1, 1);

        Label itemListTitle = new Label("Daftar Item Perpustakaan");
        itemListTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        itemListTitle.setTextFill(Color.WHITE);

        TextArea itemListArea = new TextArea();
        itemListArea.setEditable(false);
        itemListArea.setPrefHeight(200);
        itemListArea.setText(library.getLibraryStatus());
        itemListArea.setStyle("-fx-background-radius: 10; -fx-border-radius: 10;");

        Button closeButton = new Button("Tutup");
        closeButton.setStyle(
                "-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 25;");
        closeButton.setOnAction(_ -> {
            Scene mainScene = createMainScene();
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Perpustakaan Digital");
        });

        root.getChildren().addAll(title, statsGrid, itemListTitle, itemListArea, closeButton);

        return new Scene(root, 800, 700);
    }

    private Button createStatButton(String icon, String title, String value, String color) {
        VBox buttonContent = new VBox(10);
        buttonContent.setAlignment(Pos.CENTER);
        buttonContent.setPadding(new Insets(15));
        buttonContent.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 15; -fx-text-fill: white;");
        buttonContent.setPrefSize(150, 150);

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(40));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        buttonContent.getChildren().addAll(iconLabel, titleLabel, valueLabel);

        Button button = new Button();
        button.setGraphic(buttonContent);
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        return button;
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

    private String getMemberList() {
        StringBuilder sb = new StringBuilder();
        for (Member member : library.getMembers()) {
            sb.append(member.getMemberId()).append(" - ").append(member.getName()).append("\n");
        }
        return sb.toString().isEmpty() ? "Tidak ada member." : sb.toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}