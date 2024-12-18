import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        StyleAppName.displayAppName();
        StyleAppName.waitForUserInput();

        while (true) {
            User.displayMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> ConcreteUser.handleSignUp(scanner);
                case "2" -> { 
                    User loggedInUser = ConcreteUser.handleLogIn(scanner); 
                    if (loggedInUser != null) {
                    System.out.println("\n\u001B[32mLogin successful! Redirecting to the dashboard...\u001B[0m");
                    
                    Dashboard dashboard = new Dashboard(
                            loggedInUser.getFirstName(),
                            loggedInUser.getMiddleName(),
                            loggedInUser.getSurname(),
                            loggedInUser.getUsername(),
                            loggedInUser.getPassword()  
                        );

                    dashboard.displayMenu();                    }
                }
                case "3" -> {
                    StyleAppName.displayThankYou();
                    scanner.close();
                    return;
                }
                default -> System.out.println("\u001B[31mInvalid choice. Please select again.\u001B[0m");
            }
        }
    } 
}
