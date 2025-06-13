import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

class Library {
    private List<LibraryItem> items = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private LibraryLogger logger = new LibraryLogger();

    public String addItem(LibraryItem item) {
        items.add(item);
        logger.logActivity(item.getTitle() + " berhasil ditambahkan");
        return item.getTitle() + " berhasil ditambahkan";
    }

    public LibraryItem findItemById(int itemId) {
        for (LibraryItem item : items) {
            if (item.getItemId() == itemId) {
                return item;
            }
        }
        throw new NoSuchElementException("Item dengan ID " + itemId + " tidak ditemukan.");
    }

    public String getLibraryStatus() {
        StringBuilder sb = new StringBuilder();
        for (LibraryItem item : items) {
            String status = item.isBorrowed() ? "Dipinjam" : "Tersedia";
            sb.append(item.getItemId()).append(" - ").append(item.getTitle()).append(" (").append(status).append(")\n");
        }
        return sb.toString().isEmpty() ? "Tidak ada item di perpustakaan." : sb.toString();
    }

    public String getAllLogs() {
        return logger.getLogs();
    }

    public void registerMember(Member member) {
        members.add(member);
    }

    public Member findMemberById(int memberId) {
        for (Member m : members) {
            if (m.getMemberId() == memberId) {
                return m;
            }
        }
        return null;
    }

    public LibraryLogger getLogger() {
        return logger;
    }

    public List<LibraryItem> getItems() {
        return items;
    }

    public List<Member> getMembers() {
        return members;
    }
}
