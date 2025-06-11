import java.util.List;
import java.util.ArrayList;

class Member {
    private String name;
    private int memberId;
    private List<LibraryItem> borrowedItems = new ArrayList<>();

    public Member(String name, int memberId) {
        this.name = name;
        this.memberId = memberId;
    }

    public Member() {
    }

    public String borrow(LibraryItem item, int days) {
        if (item.isBorrowed())
            throw new IllegalStateException("Item tidak tersedia.");
        String result = item.borrowItem(days);
        borrowedItems.add(item);
        return result;
    }

    public String returnItem(LibraryItem item, int daysLate) {
        if (!borrowedItems.contains(item))
            return "Item tidak ditemukan dalam daftar pinjaman.";
        borrowedItems.remove(item);
        String result = item.returnItem();
        double fine = item.calculateFine(daysLate);
        return result + " dengan denda: Rp " + String.format("%,.0f", fine).replace(',', '.');
    }

    public List<LibraryItem> getBorrowedItems() {
        return new ArrayList<>(borrowedItems);
    }

    public String getBorrowedItemsText() {
        if (borrowedItems.isEmpty())
            return "Tidak ada item yang dipinjam";
        StringBuilder sb = new StringBuilder();
        for (LibraryItem item : borrowedItems) {
            sb.append(item.getItemId()).append(" - ").append(item.getTitle()).append("\n");
        }
        return sb.toString();
    }

    public void addBorrowedItem(LibraryItem item) {
        if (!borrowedItems.contains(item) && item.isBorrowed()) {
            borrowedItems.add(item);
        }
    }

    public String getName() {
        return name;
    }

    public int getMemberId() {
        return memberId;
    }
}
