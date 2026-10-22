import java.util.*;
import java.io.*;

class Contact implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String phone;
    private String email;

    public Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Name: " + name
                + " | Phone: " + phone
                + " | Email: " + email;
    }
}

public class Main {
    private static final String FILE_NAME = "contacts.dat";
    private static ArrayList<Contact> contacts = new ArrayList<>();

    public static void main(String[] args) {
        loadContacts();

        Scanner sc = new Scanner(System.in);
        int choice = 0;

        while (choice != 6) {
            System.out.println("\n===== CONTACT MANAGEMENT APP =====");
            System.out.println("1. Add Contact");
            System.out.println("2. View All Contacts");
            System.out.println("3. Search Contact");
            System.out.println("4. Edit Contact");
            System.out.println("5. Delete Contact");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Please enter a number.");
                sc.nextLine();
                continue;
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addContact(sc);
                case 2 -> viewContacts();
                case 3 -> searchContact(sc);
                case 4 -> editContact(sc);
                case 5 -> deleteContact(sc);
                case 6 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice!");
            }
        }

        saveContacts();
        sc.close();
    }

    private static boolean validEmail(String email) {
        return email.matches(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        );
    }

    private static boolean validPhone(String phone) {
        return phone.matches("[0-9]{10}");
    }

    private static void addContact(Scanner sc) {
        System.out.print("Enter name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter 10-digit phone: ");
        String phone = sc.nextLine().trim();

        System.out.print("Enter email: ");
        String email = sc.nextLine().trim();

        if (name.isEmpty() || !validPhone(phone)
                || !validEmail(email)) {
            System.out.println("Invalid name, phone, or email.");
            return;
        }

        for (Contact c : contacts) {
            if (c.getPhone().equals(phone)) {
                System.out.println("Phone number already exists.");
                return;
            }
        }

        contacts.add(new Contact(name, phone, email));
        saveContacts();
        System.out.println("Contact added successfully!");
    }

    private static void viewContacts() {
        if (contacts.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }

        for (Contact c : contacts) {
            System.out.println(c);
        }
    }

    private static void searchContact(Scanner sc) {
        System.out.print("Enter name or phone to search: ");
        String query = sc.nextLine().trim().toLowerCase();
        boolean found = false;

        for (Contact c : contacts) {
            if (c.getName().toLowerCase().contains(query)
                    || c.getPhone().contains(query)) {
                System.out.println(c);
                found = true;
            }
        }

        if (!found) {
            System.out.println("Contact not found.");
        }
    }

    private static void editContact(Scanner sc) {
        System.out.print("Enter current phone number: ");
        String oldPhone = sc.nextLine().trim();

        for (Contact c : contacts) {
            if (c.getPhone().equals(oldPhone)) {
                System.out.print("Enter new name: ");
                String name = sc.nextLine().trim();

                System.out.print("Enter new phone: ");
                String phone = sc.nextLine().trim();

                System.out.print("Enter new email: ");
                String email = sc.nextLine().trim();

                if (name.isEmpty() || !validPhone(phone)
                        || !validEmail(email)) {
                    System.out.println("Invalid details.");
                    return;
                }

                for (Contact other : contacts) {
                    if (other != c
                            && other.getPhone().equals(phone)) {
                        System.out.println("Phone already exists.");
                        return;
                    }
                }

                c.setName(name);
                c.setPhone(phone);
                c.setEmail(email);
                saveContacts();
                System.out.println("Contact updated!");
                return;
            }
        }

        System.out.println("Contact not found.");
    }

    private static void deleteContact(Scanner sc) {
        System.out.print("Enter phone number to delete: ");
        String phone = sc.nextLine().trim();

        boolean removed = contacts.removeIf(
            c -> c.getPhone().equals(phone)
        );

        if (removed) {
            saveContacts();
            System.out.println("Contact deleted!");
        } else {
            System.out.println("Contact not found.");
        }
    }

    private static void saveContacts() {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(FILE_NAME))) {
            out.writeObject(contacts);
        } catch (IOException e) {
            System.out.println("Error saving contacts: "
                    + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadContacts() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(file))) {
            Object data = in.readObject();

            if (data instanceof ArrayList<?>) {
                contacts = (ArrayList<Contact>) data;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Could not load contacts.");
        }
    }
}