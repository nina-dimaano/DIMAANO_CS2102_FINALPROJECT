import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

class TaskManager extends Manager {
    private double maxDailyTime; 

    public TaskManager() {
    }

    public TaskManager(double maxDailyTime) {
        this.maxDailyTime = maxDailyTime;
    }

    @Override
    public void addItem() {
        titleDisplay("TASK MANAGER");
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nSelect or add a subject:");
        String subject = manageSubjects(); 

        System.out.print("\nEnter your available study time per day (in hours): ");
        maxDailyTime = validateInputDouble(scanner, 1, 24);

        String title;
        do {
            System.out.print("Enter the title of the task: ");
            title = scanner.nextLine().trim();
            if (title.isEmpty()) {
                System.out.println("\n\u001B[31mTitle cannot be empty. Please try again.\u001B[0m");
            }
        } while (title.isEmpty());
        
        String details;
        do {
            System.out.print("Enter details about the task: ");
            details = scanner.nextLine().trim();
            if (details.isEmpty()) {
                System.out.println("\n\u001B[31mDetails cannot be empty. Please try again.\u001B[0m");
            }
        } while (details.isEmpty());

        System.out.println("Pick the type of the task:");
        System.out.println("1. Assignment");
        System.out.println("2. Project");
        System.out.println("3. Exam/Quiz Review");
        System.out.println("4. Presentation");
        System.out.println("5. Custom Task");
        System.out.print("Enter your choice (1-5): ");
        int taskTypeChoice = validateInputInt(scanner, 1, 5);

        String taskType;
        switch (taskTypeChoice) {
            case 1:
                taskType = "Assignment";
                break;
            case 2:
                taskType = "Project";
                break;
            case 3:
                taskType = "Exam/Quiz Review";
                break;
            case 4:
                taskType = "Presentation";
                break;
            case 5:
                taskType = "Custom Task";
                break;
            default:
                taskType = "Assignment";
        }

        System.out.print("Enter the complexity of the task (1-5, with 5 being the highest): ");
        int complexity = validateInputInt(scanner, 1, 5);

        System.out.print("\n\u001B[1m\u001B[34m -Due Date-\u001B[0m\n");
        System.out.print("Enter the year: ");
        int year = validateInputInt(scanner, 1900, 2100);
        System.out.print("Enter the month (1-12): ");
        int month = validateInputInt(scanner, 1, 12);
        int maxDaysInMonth = getMaxDaysInMonth(month, year);
        System.out.print("Enter the day (1-" + maxDaysInMonth + "): ");
        int day = validateInputInt(scanner, 1, maxDaysInMonth);
        System.out.print("Enter the hour (0-23): ");
        int hour = validateInputInt(scanner, 0, 23);
        System.out.print("Enter the minute (0-59): ");
        int minute = validateInputInt(scanner, 0, 59);

        LocalDateTime dueDateTime = LocalDateTime.of(year, month, day, hour, minute);

        long hoursUntilDue = ChronoUnit.HOURS.between(LocalDateTime.now(), dueDateTime);
        long daysUntilDue = ChronoUnit.DAYS.between(LocalDateTime.now(), dueDateTime);

        if (daysUntilDue == 0) {
            daysUntilDue = 1;  
        }

        String task = "Subject: " + subject + "\n" +
                      "Title: " + title + "\n" +
                      "Details: " + details + "\n" +
                      "Type: " + taskType + "\n" +
                      "Due: " + dueDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                      "\nTime until due: " + hoursUntilDue + " hours";

        items.add(task + "|||" + subject + "|||" + title + "|||" + details + "|||" + taskType + "|||" + dueDateTime + "|||" + complexity);  // Add task with metadata
        System.out.println("\n\u001B[32mTask added successfully!\u001B[0m\n");
    }

    @Override
    public void viewItems() {
        titleDisplay("TO DO LIST");
        Scanner scanner = new Scanner(System.in);
    
        if (items.isEmpty()) {
            System.out.println("\n\u001B[31mNo tasks added yet!\u001B[0m");
            return;
        }
    
        System.out.printf("%-5s %-20s %-30s %-15s %-10s%n", "No.", "Subject", "Title", "Due Date", "Status");
        System.out.println("------------------------------------------------------------------------------------");
    
        int index = 1;
        for (String item : items) {
            String[] taskParts = item.split("\\|\\|\\|");
            String subject = taskParts[1];
            String title = taskParts[2];
            LocalDateTime dueDateTime = LocalDateTime.parse(taskParts[5]);
            String status = taskParts.length > 7 && taskParts[7].equals("done") ? "Completed" : "Pending";
    
            System.out.printf("%-5d %-20s %-30s %-15s %-10s%n", index++, subject, title, dueDateTime.toLocalDate(), status);
        }
    
        while (true) {
            System.out.println("\n\u001B[1mOptions:\u001B[0m");
            System.out.println("1. View Details");
            System.out.println("2. Mark as Done");
            System.out.println("3. Delete All Completed Tasks");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
    
            int choice = validateInputInt(scanner, 1, 4);
    
            switch (choice) {
                case 1 -> viewTaskDetails(scanner);
                case 2 -> markTaskAsDone(scanner);
                case 3 -> deleteCompletedTasks();
                case 4 -> {
                    System.out.println("\n\u001B[32mExiting Task Manager.\u001B[0m");
                    return;
                }
            }
        }
    }
    
    private void viewTaskDetails(Scanner scanner) {
        System.out.print("\nEnter the task number to view details: ");
        int taskNumber = validateInputInt(scanner, 1, items.size());
    
        String[] taskParts = items.get(taskNumber - 1).split("\\|\\|\\|");
        String taskDetails = taskParts[0];
        LocalDateTime dueDateTime = LocalDateTime.parse(taskParts[5]);
        int complexity = Integer.parseInt(taskParts[6]);
    
        System.out.println("\n" + taskDetails);
        analyzeTask(taskParts[4], dueDateTime, complexity);
    }
    
    private void markTaskAsDone(Scanner scanner) {
        System.out.print("\nEnter the task number to mark as done: ");
        int taskNumber = validateInputInt(scanner, 1, items.size());
    
        String item = items.get(taskNumber - 1);
        items.set(taskNumber - 1, item + "|||done");
    
        System.out.println("\n\u001B[32mTask marked as done successfully!\u001B[0m");
    }
    
    private void deleteCompletedTasks() {
        items.removeIf(item -> item.endsWith("|||done"));
        System.out.println("\n\u001B[32mAll completed tasks have been deleted successfully!\u001B[0m");
    }
    

    private void analyzeTask(String taskType, LocalDateTime dueDateTime, int complexity) {
        LocalDateTime now = LocalDateTime.now();
        long hoursUntilDue = ChronoUnit.HOURS.between(now, dueDateTime);
        long daysUntilDue = ChronoUnit.DAYS.between(now, dueDateTime);

        if (daysUntilDue == 0) {
            daysUntilDue = 1;  
        }

        double approximateTime = calculateApproximateTime(hoursUntilDue, complexity);

        double dailyPrepTime = approximateTime / daysUntilDue;

        if (dailyPrepTime > maxDailyTime) {
            dailyPrepTime = maxDailyTime;
            System.out.println("Note: The recommended daily prep time exceeds your available time per day.");
            System.out.println("We recommend breaking the task into more days.");
        }

        System.out.println("\n\u001B[1m\u001B[34mTask Analysis:\u001B[0m");
        System.out.println("Time until due: " + hoursUntilDue + " hours");
        System.out.println("Approximate time to finish: " + approximateTime + " hours");
        System.out.println("Recommended daily prep time: " + dailyPrepTime + " hours per day");
        System.out.println("\u001B[1m\u001B[34m---------------------------------------------\u001B[0m");
    }

    private double calculateApproximateTime(long hoursUntilDue, int complexity) {
        double baseTime = complexity * 1.5;
    
        if (hoursUntilDue < 24) {
            baseTime *= 1.2; 
        }
    
        return Math.min(baseTime, hoursUntilDue);
    }
    
    private int validateInputInt(Scanner scanner, int min, int max) {
        int value;
        while (true) {
            try {
                value = Integer.parseInt(scanner.nextLine());
                if (value < min || value > max) {
                    System.out.println("\n\u001B[31mInvalid input. Please enter a value between " + min + " and " + max + ": \u001B[0m");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("\n\u001B[31mInvalid input. Please enter a valid number: \u001B[0m");
            }
        }
        return value;
    }

    private double validateInputDouble(Scanner scanner, double min, double max) {
        double value;
        while (true) {
            try {
                value = Double.parseDouble(scanner.nextLine());
                if (value < min || value > max) {
                    System.out.println("\n\u001B[31mInvalid input. Please enter a value between " + min + " and " + max + ": \u001B[0m");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("\n\u001B[31mInvalid input. Please enter a valid number: \u001B[0m");
            }
        }
        return value;
    }

    private int getMaxDaysInMonth(int month, int year) {
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return 31;
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return isLeapYear(year) ? 29 : 28;
            default:
                return 30; 
        }
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}

