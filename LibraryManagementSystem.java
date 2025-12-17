import java.io.*;
import java.util.*;

class Book implements Serializable {
    int bookId;
    String title;
    String author;
    boolean isIssued;
    String issuedTo;
    String dueDate;
    Book(int bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isIssued = false;
        this.issuedTo = null;
        this.dueDate = null;
    }
    @Override
    public String toString() {
        return "Book ID: " + bookId + ", Title: " + title + ", Author: " + author +
                (isIssued ? ", Issued To: " + issuedTo + ", Due Date: " + dueDate : ", Available");
    }
}
public class LibraryManagementSystem {
    private static final String FILE_NAME = "library_records.data";
    private static List<Book> books = new ArrayList<>();
    @SuppressWarnings("unchecked")
    private static void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            books = (ArrayList<Book>) ois.readObject();
        } catch (Exception e) {
            books = new ArrayList<>(); 
        }
    }
    private static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(books);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
    private static void addBook(Scanner sc) {
        System.out.print("Enter Book ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Title: ");
        String title = sc.nextLine();
        System.out.print("Enter Author: ");
        String author = sc.nextLine();
        books.add(new Book(id, title, author));
        saveData();
        System.out.println("✅ Book added successfully!");
    }
    private static void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        for (Book b : books) {
            System.out.println(b);
        }
    }
    private static void issueBook(Scanner sc) {
        System.out.print("Enter Book ID to issue: ");
        int id = sc.nextInt();
        sc.nextLine();
        for (Book b : books) {
            if (b.bookId == id) {
                if (b.isIssued) {
                    System.out.println("❌ Book already issued to " + b.issuedTo);
                    return;
                }
                System.out.print("Enter Student Name: ");
                String student = sc.nextLine();
                System.out.print("Enter Due Date (dd-mm-yyyy): ");
                String dueDate = sc.nextLine();
                b.isIssued = true;
                b.issuedTo = student;
                b.dueDate = dueDate;
                saveData();
                System.out.println("✅ Book issued successfully!");
                return;
            }
        }
        System.out.println("❌ Book not found!");
    }
    private static void returnBook(Scanner sc) {
        System.out.print("Enter Book ID to return: ");
        int id = sc.nextInt();
        for (Book b : books) {
            if (b.bookId == id) {
                if (!b.isIssued) {
                    System.out.println("❌ This book is not issued.");
                    return;
                }
                b.isIssued = false;
                b.issuedTo = null;
                b.dueDate = null;
                saveData();
                System.out.println("✅ Book returned successfully!");
                return;
            }
        }
        System.out.println("❌ Book not found!");
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        loadData();
        while (true) {
            System.out.println("\n===== 📚 Library Management System =====");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 -> addBook(sc);
                case 2 -> viewBooks();
                case 3 -> issueBook(sc);
                case 4 -> returnBook(sc);
                case 5 -> {
                    saveData();
                    System.out.println("💾 Data saved. Exiting...");
                    sc.close();
                    return;
                }
                default -> System.out.println("❌ Invalid choice! Please try again.");
            }
        }
    }
}