import java.util.ArrayList;
import java.util.Scanner;

abstract class Manager {
    protected ArrayList<String> items = new ArrayList<>();
    protected static ArrayList<String> subjects = new ArrayList<>(); 

    public abstract void addItem();

    public abstract void viewItems();

    public static void titleDisplay(String title) {
        int lineWidth = 85; 
        int padding = (lineWidth - title.length()) / 2;
        String paddingSpaces = " ".repeat(Math.max(0, padding));
    
        System.out.println("\033[96m"); 
        System.out.println("=".repeat(lineWidth));
        System.out.println(paddingSpaces + title);
        System.out.println("=".repeat(lineWidth));
        System.out.println("\033[0m"); 
    }
    

    public String manageSubjects() {
        Scanner scanner = new Scanner(System.in);
        String chosenSubject = "";
    
        while (true) {
            System.out.println("\u001B[1m\u001B[34m---- Subject Choices ----\u001B[0m");
            if (subjects.isEmpty()) {
                System.out.println("\u001B[31mNo subjects available.\u001B[0m");
            } else {
                for (int i = 0; i < subjects.size(); i++) {
                    System.out.println((i + 1) + ". " + subjects.get(i));
                }
            }
            System.out.println((subjects.size() + 1) + ". Add a new subject");
            System.out.println((subjects.size() + 2) + ". Delete a subject");
            System.out.println((subjects.size() + 3) + ". Exit");
            System.out.print("Choose an option by number: ");
    
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("\n\u001B[31mInvalid input. Please enter a valid number.\u001B[0m");
                continue;
            }
    
            if (choice >= 1 && choice <= subjects.size()) {
                chosenSubject = subjects.get(choice - 1);
                System.out.println("\nYou selected: " + chosenSubject);
                break;
            } else if (choice == subjects.size() + 1) {
                System.out.print("Enter the name of the new subject: ");
                String newSubject = scanner.nextLine().trim();
                if (newSubject.isEmpty()) {
                    System.out.println("\n\u001B[31mSubject name cannot be blank. Please try again.\u001B[0m");
                } else if (subjects.stream().anyMatch(subject -> subject.equalsIgnoreCase(newSubject))) {
                    System.out.println("\n\u001B[31mSubject already exists. Please enter a unique name.\u001B[0m");
                } else {
                    subjects.add(newSubject);
                    System.out.println("\n\u001B[32mNew subject added successfully!\u001B[0m");
                }
            } else if (choice == subjects.size() + 2) {
                if (subjects.isEmpty()) {
                    System.out.println("\n\u001B[31mNo subjects to delete.\u001B[0m");
                } else {
                    System.out.print("Enter the number of the subject to delete: ");
                    int deleteChoice;
                    try {
                        deleteChoice = Integer.parseInt(scanner.nextLine().trim());
                        if (deleteChoice >= 1 && deleteChoice <= subjects.size()) {
                            String subjectToRemove = subjects.get(deleteChoice - 1);
                            System.out.print("Are you sure you want to delete \"" + subjectToRemove + "\"? (yes/no): ");
                            String confirmation = scanner.nextLine().trim().toLowerCase();
                            if (confirmation.equals("yes")) {
                                subjects.remove(deleteChoice - 1);
                                System.out.println("\n\u001B[32mSubject \"" + subjectToRemove + "\" deleted successfully!\u001B[0m");
                            } else {
                                System.out.println("\n\u001B[31mDeletion canceled.\u001B[0m");
                            }
                        } else {
                            System.out.println("\n\u001B[31mInvalid choice. Please enter a valid number.\u001B[0m");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("\n\u001B[31mInvalid input. Please enter a valid number.\u001B[0m");
                    }
                }
            } else if (choice == subjects.size() + 3) {
                System.out.println("\n\u001B[34mExiting...\u001B[0m");
                break;
            } else {
                System.out.println("\n\u001B[31mInvalid choice. Please try again.\u001B[0m");
            }
        }
        return chosenSubject;
    }
}
    