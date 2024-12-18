import java.util.HashMap;
import java.util.Map;

public abstract class User {
    private static Map<String, User> usersDatabase = new HashMap<>();
    private static int userIdCounter = 1000;

    private String firstName;
    private String middleName;
    private String surname;
    private String username;
    private String password;
    private int userId;
    private static int weeklyStudyGoal;
    private static String studyStruggleArea;
    private static String studySuggestion;

    public User(String firstName, String middleName, String surname, String username, String password) {
        if (usersDatabase.containsKey(username)) {
            this.userId = usersDatabase.get(username).getUserId();
        } else {
            this.userId = userIdCounter++;
        }
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.username = username;
        this.password = password;
    }

    public static void displayHeader(String title) {
        int lineWidth = 85;
        int padding = (lineWidth - title.length()) / 2;
        String paddingSpaces = " ".repeat(Math.max(0, padding));
    
        System.out.println("\033[36m");
        System.out.println("=".repeat(lineWidth));
        System.out.println(paddingSpaces + title);
        System.out.println("=".repeat(lineWidth));
        System.out.println("\033[0m");
    }

    public static boolean signUp(User user) {
        if (usersDatabase.containsKey(user.getUsername())) {
            System.out.println("\n\u001B[31mError: Username already exists.\u001B[0m");
            return false;
        }
        usersDatabase.put(user.getUsername(), user);
        System.out.println("\n\u001B[32mSign-up successful! Here's your account info:\u001B[0m");
        System.out.println("User ID: " + user.getUserId());
        return true;
    }

    public static User logIn(String username, String password) {
        User user = usersDatabase.get(username);
        if (user != null && user.password.equals(password)) {
            System.out.println("\n\u001B[32mLogin successful! Welcome, " + user.firstName + "!\u001B[0m\n");
            return user;
        }
        System.out.println("\n\u001B[31mError: Invalid username or password.\u001B[0m");
        return null;
    }

    public void setStudyGoal(int weeklyStudyGoal) {
        User.weeklyStudyGoal = weeklyStudyGoal;
        System.out.println("\n\u001B[32mWeekly study goal set to " + weeklyStudyGoal + " hours.\u001B[0m\n");
    }

    public abstract void showAccountDetails();

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public int getWeeklyStudyGoal() {
        return weeklyStudyGoal;
    }
    
    public String getStudyStruggleArea() {
        return studyStruggleArea;
    }
    
    public String getStudySuggestion() {
        return studySuggestion;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void displayMainMenu() {
        displayHeader("MAIN MENU");
        System.out.println("1. Sign Up");
        System.out.println("2. Log In");
        System.out.println("3. Exit");
        System.out.print("\nChoose an option: ");
    }
}
