class Magazine extends LibraryItem {
    private int issueNumber;

    public Magazine(String title, int itemId, int issueNumber) {
        super(title, itemId);
        this.issueNumber = issueNumber;
    }

    @Override
    public String borrowItem(int days) {
        if (isBorrowed)
            throw new IllegalArgumentException("Majalah sudah dipinjam.");
        if (days > 7)
            throw new IllegalArgumentException("Majalah hanya bisa dipinjam maksimal 7 hari.");
        isBorrowed = true;
        return "Majalah " + title + " berhasil dipinjam selama " + days + " hari";
    }

    @Override
    public double calculateFine(int daysLate) {
        return daysLate * 5000;
    }

    public int getIssueNumber() {
        return issueNumber;
    }
}
