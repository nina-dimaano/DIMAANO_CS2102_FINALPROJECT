import java.sql.SQLException;
import java.util.Scanner;

public class ConcreteUser extends User {

    public ConcreteUser(String firstName, String middleName, String surname, String username, String password) {
        super(firstName, middleName, surname, username, password);
    }

    public static void handleSignUp(Scanner scanner) {
        displayHeader("SIGN UP");
        DatabaseManager dbManager = new DatabaseManager();
    
        String firstName, middleName, surname, username, password;
    
        while (true) {
            System.out.print("Enter your first name: ");
            firstName = scanner.nextLine().trim();
            if (firstName.isEmpty() || containsNumber(firstName)) {
                System.out.println("\n\u001B[31mInvalid first name. It cannot be blank or contain numbers. Please try again.\u001B[0m");
                continue;
            }
            break;
        }
    
        while (true) {
            System.out.print("Enter your middle name (optional): ");
            middleName = scanner.nextLine().trim();
            if (!middleName.isEmpty() && containsNumber(middleName)) {
                System.out.println("\n\u001B[31mInvalid middle name. It cannot contain numbers. Please try again.\u001B[0m");
                continue;
            }
            break;
        }
    
        while (true) {
            System.out.print("Enter your surname: ");
            surname = scanner.nextLine().trim();
            if (surname.isEmpty() || containsNumber(surname)) {
                System.out.println("\n\u001B[31mInvalid surname. It cannot be blank or contain numbers. Please try again.\u001B[0m");
                continue;
            }
            break;
        }
    
        // Check if the user's personal info already exists in the database
        int userId = dbManager.getUserId(firstName, surname);
        if (userId != -1) {
            System.out.println("\n\u001B[31mThis user already exists in the system. Please try again or log in.\u001B[0m");
            return;
        }
    
        // Save personal info if it doesn't already exist
        dbManager.saveUserPersonalInfo(firstName, middleName, surname);
        userId = dbManager.getUserId(firstName, surname);
        if (userId == -1) {
            System.out.println("\n\u001B[31mAn error occurred while saving your personal information. Please try again later.\u001B[0m");
            return;
        }
    
        while (true) {
            System.out.print("Enter a username: ");
            username = scanner.nextLine().trim();
    
            if (username.isEmpty()) {
                System.out.println("\n\u001B[31mUsername cannot be blank. Please try again.\u001B[0m");
                continue;
            }
    
            if (username.length() < 4) {
                System.out.println("\n\u001B[31mUsername must be at least 4 characters long. Please try again.\u001B[0m");
                continue;
            }
    
            // Check if username already exists
            boolean usernameExists = dbManager.isUsernameTaken(username);
            if (usernameExists) {
                System.out.println("\n\u001B[31mThis username is already taken. Please try a different one.\u001B[0m");
                continue;
            }
    
            break;
        }
    
        while (true) {
            System.out.print("Enter a password (at least 8 characters): ");
            password = scanner.nextLine().trim();
            if (password.isEmpty() || password.length() < 8) {
                System.out.println("\n\u001B[31mInvalid password. It must be at least 8 characters long. Please try again.\u001B[0m");
                continue;
            }
            break;
        }
    
        // Save login info
        dbManager.saveUserLogin(userId, username, password);
    
        User newUser = new ConcreteUser(firstName, middleName, surname, username, password);
        boolean success = User.signUp(newUser);
    
        if (!success) {
            System.out.println("\n\u001B[31mAn error occurred while signing up. Please try again.\u001B[0m");
        } else {
            ((ConcreteUser) newUser).showAccountDetails();
        }
    }
    

public static User handleLogIn(Scanner scanner) {
    displayHeader("LOG IN");

    System.out.print("Enter your username: ");
    String username = scanner.nextLine().trim();

    System.out.print("Enter your password: ");
    String password = scanner.nextLine().trim();

    User loggedInUser = User.logIn(username, password);
    if (loggedInUser != null) {
        DatabaseManager dbManager = new DatabaseManager(); // Assuming DatabaseManager is used for DB operations

        // Retrieve userId based on username and password from users_login table
        int userId = -1;
        try {
            userId = dbManager.getUserIdByLogin(username, password); // Fetch userId from the database
            if (userId == -1) {
                System.out.println("\n\u001B[31mUser not found in the database. Please try again.\u001B[0m");
                return null;
            }
        } catch (Exception e) {
            System.out.println("\n\u001B[31mError retrieving user information. Please try again later.\u001B[0m");
            e.printStackTrace();
            return null;
        }

        // Input weekly study goal
        int weeklyGoal = 0; // Declare outside the loop to save it later in the study_struggles table
        while (true) {
            System.out.print("Enter your weekly study goal (hours): ");
            String weeklyGoalInput = scanner.nextLine().trim();
            try {
                weeklyGoal = Integer.parseInt(weeklyGoalInput);
                if (weeklyGoal <= 0) {
                    System.out.println("\n\u001B[31mWeekly goal must be a positive number. Please try again.\u001B[0m");
                    continue;
                }
                loggedInUser.setStudyGoal(weeklyGoal);

                // Save weekly study goal in the database (if applicable)
                try {
                    dbManager.updateUserStudyGoal(userId, weeklyGoal); // Update users_login table
                } catch (SQLException e) {
                    System.out.println("\n\u001B[31mError saving weekly study goal. Please try again later.\u001B[0m");
                    e.printStackTrace();
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("\n\u001B[31mInvalid input. Please enter a valid number.\u001B[0m");
            }
        }

        // Input study struggle area
        System.out.println("What aspect of studying do you struggle with the most?");
        System.out.println("1. Focus\n2. Comprehension\n3. Memorization\n4. Demotivation\n5. Procrastination");
        System.out.print("\nEnter your choice: ");

        String choice = scanner.nextLine().trim();
        String struggleArea = switch (choice) {
            case "1" -> "Focus";
            case "2" -> "Comprehension";
            case "3" -> "Memorization";
            case "4" -> "Demotivation";
            case "5" -> "Procrastination";
            default -> {
                System.out.println("\n\u001B[31mInvalid choice. Defaulting to 'Unknown'.\u001B[0m");
                yield "Unknown";
            }
        };

        // Assess struggle area and generate suggestion inline
        String studySuggestion = switch (struggleArea.toLowerCase()) {
            case "focus" -> "Screen Time Blocking";
            case "comprehension" -> "Feynman Technique";
            case "memorization" -> "The SQ3R Method";
            case "demotivation" -> "The 3-2-1 Method";
            case "procrastination" -> "The Pomodoro Technique";
            default -> "Identify specific areas where you need improvement.";
        };

        System.out.println("\n\u001B[1mSuggested Study Method: \u001B[0m" + studySuggestion);

        // Save study struggle, weekly goal, and suggestion in the database
        try {
            dbManager.saveStudyStruggle(userId, struggleArea, studySuggestion, weeklyGoal);
        } catch (SQLException e) {
            System.out.println("\n\u001B[31mError saving study struggle information. Please try again later.\u001B[0m");
            e.printStackTrace();
        }

        return loggedInUser;
    } else {
        System.out.println("\n\u001B[31mInvalid username or password. Please try again.\u001B[0m");
        return null;
    }
}

    @Override
    public void showAccountDetails() {
        System.out.println("\n\u001B[1m\u001B[34m----- Login Credentials -----\u001B[0m");
        System.out.println("Name: " + getFirstName() + " " + getMiddleName() + " " + getSurname());
        System.out.println("Username: " + getUsername());
        System.out.println("User ID: " + getUserId());
        System.out.println("\u001B[1m\u001B[34m-----------------------------\u001B[0m");
    }

    private static boolean containsNumber(String input) {
        return input.matches(".*\\d.*");
    }
}
