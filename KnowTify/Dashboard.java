import java.util.Scanner;

class Dashboard extends User {
    private TaskManager taskManager;
    private SubjectManager subjectManager;

    public Dashboard() {
        super("", "", "", "", "");
        this.taskManager = new TaskManager();
        this.subjectManager = new SubjectManager(getUserId());
    }
    
    public Dashboard(String firstName, String middleName, String surname, String username, String password) {
        super(firstName, middleName, surname, username, password);
        this.taskManager = new TaskManager();
        this.subjectManager = new SubjectManager(getUserId());
    }

    // Display Main Menu
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayHeader("DASHBOARD");
            System.out.println("1. Profile");
            System.out.println("2. Add Subject");
            System.out.println("3. Add Task");
            System.out.println("4. View Schedule");
            System.out.println("5. View Task List");
            System.out.println("6. Exit");
            System.out.print("\nEnter your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("\n\u001B[31mInvalid input. Please enter a number.\u001B[0m");
                scanner.next(); 
                continue;
            }
            
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1 -> showAccountDetails(); 
                case 2 -> subjectManager.addItem(); 
                case 3 -> taskManager.addItem(); 
                case 4 -> subjectManager.viewItems(); 
                case 5 -> taskManager.viewItems(); 
                case 6 -> {
                    System.out.println("\n\u001B[34m\nReturning to the main menu...\u001B[0m");
                    return; 
                }
                default -> System.out.println("\n\u001B[31mInvalid choice! Please try again.\u001B[0m");
            }
        }
    }

    @Override
    public void showAccountDetails() {
        displayHeader("PROFILE");
        System.out.println("Name: " + getFirstName() + " " + getMiddleName() + " " + getSurname());
        System.out.println("Username: " + getUsername());
        System.out.println("User ID: " + getUserId());
        System.out.println("Weekly Study Goal: " + getWeeklyStudyGoal());
        System.out.println("Struggle Area: " + getStudyStruggleArea());
        System.out.println("Study Suggestion: " + getStudySuggestion());
    }
}
