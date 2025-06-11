# DIGIPUS

## Deskripsi Aplikasi

DIGIPUS merupakan aplikasi pengelolaan perpustakaan digital berbasis GUI yang dibangun menggunakan bahasa pemrograman Java dengan menerapkan konsep Object-Oriented Programming (OOP). Admin berperan sebagai pengelola perpustakaan yang dapat menambahkan item koleksi, mencatat proses peminjaman dan pengembalian, serta memantau aktivitas anggota. Setiap interaksi pengguna tercatat dalam log aktivitas dan membantu dalam pemantauan status perpustakaan secara menyeluruh. Fitur-fiturnya meliputi sistem manajemen item, peminjaman dan pengembalian buku, pencatatan log, tampilan status perpustakaan, penambahan anggota, dan opsi keluar aplikasi.

---

## Fitur Utama

**Sistem Manajemen Item**  
Fitur ini memungkinkan admin atau petugas perpustakaan untuk menambahkan item koleksi digital seperti buku atau majalah. Setiap item dapat dilengkapi dengan informasi seperti judul, penulis, ID, kategori, dan ketersediaan item, sehingga memudahkan dalam pengelolaan koleksi secara terstruktur.

**Peminjaman dan Pengembalian Item**  
Fitur ini digunakan untuk mencatat aktivitas peminjaman dan pengembalian item oleh anggota perpustakaan. Sistem akan mencatat item yang dipinjam, pengembalian item, denda keterlambatan pengembalian item, serta status item (dipinjam atau tersedia) secara otomatis.

**Pencatatan Log Aktivitas**  
Setiap interaksi penting dalam aplikasi, seperti penambahan item, peminjaman, pengembalian, dan penambahan anggota, akan dicatat dalam log aktivitas. Fitur ini membantu admin memantau seluruh aktivitas pengguna dan menjaga transparansi dalam pengelolaan perpustakaan.

**Tampilan Status Perpustakaan**  
Fitur ini menyajikan informasi terkini tentang kondisi perpustakaan, seperti jumlah total item, jumlah item yang sedang dipinjam, jumlah item yang tersedia, dan jumlah anggota. Tampilan ini berguna untuk analisis cepat dan pemantauan status perpustakaan secara langsung.

**Penambahan Anggota**  
Admin dapat menambahkan data anggota baru ke dalam sistem, termasuk informasi penting seperti nama dan ID anggota. Setiap anggota yang terdaftar akan memiliki hak untuk meminjam item dan tercatat dalam sistem.

**Opsi Keluar Aplikasi**  
Fitur ini memungkinkan admin keluar dari halaman utama dengan aman. Sistem akan menampilkan pesan otomatis untuk konfirmasi saat pengguna memilih keluar, lalu mengarahkan kembali ke halaman login.

---

## Penerapan Pilar OOP

### **Encapsulation**

Encapsulation diterapkan dengan menjaga atribut kelas bersifat private dan menyediakan akses terbatas melalui method getter dan setter. Ini memastikan bahwa perubahan terhadap data hanya bisa diakses dan dimodifikasi melalui fungsi yang telah ditentukan.

Contoh Penerapan:

- Di kelas `Book`, atribut seperti `title`, `author`, dan `isbn` disembunyikan (private), dan hanya bisa diakses atau diubah melalui method publik `getTitle()`, `getAuthor()`, dan `setTitle(...)`.

- Di kelas `Member`, data pribadi seperti `name` dan `ID` dienkapsulasi agar tidak bisa diubah langsung dari luar objek.

- `LibraryLogger` juga menggunakan enkapsulasi dalam menyimpan log aktivitas dan hanya membukanya melalui method tertentu, seperti ``log(...)```.

```
private String title;
```

```
public String getTitle() {
    return title;
}
```

### **Inheritance**

Inheritance digunakan untuk menghindari duplikasi kode dan membuat hubungan antara kelas-kelas dalam sistem jadi lebih terstruktur. Fungsi-fungsi umum dapat digunakan ulang oleh semua turunan LibraryItem.

Contoh Penerapan:

- `LibraryItem` adalah superclass abstrak yang mendefinisikan struktur umum dari semua item perpustakaan. Kelas ini memiliki atribut seperti `ID` dan `title`, serta method seperti `getId()` dan `getTitle()`.

- Kelas `Book` dan `Magazine` mewarisi dari `LibraryItem`, dan menambahkan atribut spesifik masing-masing seperti `author`, `isbn` (untuk Book), atau `issueNumber` (untuk Magazine).

```
public class Book extends LibraryItem { ... }
```

```
public class Magazine extends LibraryItem { ... }
```

### **Abstraction**

Abstraction menyembunyikan proses internal dan hanya menunjukkan fungsi utama yang dibutuhkan pengguna kelas.

Contoh Penerapan:

- Kelas `LibraryItem` adalah abstrak, dengan method `displayInfo()` yang juga abstrak. Subclass `Book` dan `Magazine` diwajibkan untuk mengimplementasikan method ini sesuai kebutuhan masing-masing.

- Hal ini memungkinkan sistem memperlakukan semua item perpustakaan sebagai `LibraryItem`, tanpa peduli apakah itu buku, majalah, atau jenis item lain yang akan ditambahkan nanti.

- Di kelas `Library`, metode seperti `addItem` atau `displayItems()` bekerja secara umum terhadap referensi `LibraryItem`, tanpa mengetahui implementasi detail dari setiap subclass.

```
public abstract class LibraryItem {
```

```
    public abstract String getDetails();
}
```

### **Polymorpishm**

Polimorfisme memungkinkan objek dengan bentuk yang berbeda untuk diproses melalui antarmuka yang sama, Polimorfisme membuat sistem fleksibel dan siap dikembangkan tanpa perlu mengubah banyak kode saat menambah tipe item baru.

Contoh Penerapan:

- Dalam `Library`, `daftar item` (ArrayList<LibraryItem>) dapat menyimpan objek `Book` dan `Magazine`, dan jenis item lain, selama mereka merupakan turunan `LibraryItem`.

- Saat method `displayInfo()` dipanggil pada objek `LibraryItem`, Java secara otomatis memanggil implementasi spesifik berdasarkan tipe asli objek, apakah itu dari kelas `Book` atau `Magazine`.

- Ini juga terlihat dalam Main.java, di mana saat menambahkan item ke perpustakaan, cukup gunakan:

```
library.addItem(new Book(...));
library.addItem(new Magazine(...));
```

- dan saat ditampilkan:

```
item.displayInfo(); // memanggil method sesuai jenis objek
```

---

## Struktur Kode

---

## Cara Menjalankan Aplikasi Digipus

1. Pastikan Anda telah menginstal Java JDK & VsCode (atau IDE lain seperti IntelliJ IDEA atau Eclipse)

2. Clone repositori melalui Command Prompt :

```bash
git clone https://github.com/Faderu/DigiPus.git
```

3. Jalankan file Main.java dari IDE.

4. Input dan Interaksi :

- Program akan berjalan melalui console (CLI).
- Ikuti instruksi yang muncul, seperti menambahkan data buku, melihat daftar item, atau mencari informasi anggota.

## Pembagian Tugas Per Anggota

- MUHAMMAD FADEL ARYASATYA MAKKULAU (H071241077)

- MUHAMMAD ALIF SAKTI (H071241018)

- DIESTY MENDILA TAPPO (H071241077)
