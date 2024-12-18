import java.util.Scanner;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Subject {
    private String academicYear;
    private String semester;
    private String subjectName;
    private String professorName;
    private String buildingRoom;
    private String startTime;
    private String endTime;
    private ArrayList<Integer> meetingDays; 

    public Subject(String academicYear, String semester, String subjectName, String professorName, 
                   String buildingRoom, String startTime, String endTime, ArrayList<Integer> meetingDays) {
        this.academicYear = academicYear;
        this.semester = semester;
        this.subjectName = subjectName;
        this.professorName = professorName;
        this.buildingRoom = buildingRoom;
        this.startTime = startTime;
        this.endTime = endTime;
        this.meetingDays = meetingDays;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getBuildingRoom() {
        return buildingRoom;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public String getSemester() {
        return semester;
    }

    public String getProfessorName() {
        return professorName;
    }

    public ArrayList<Integer> getMeetingDays() {
        return meetingDays;
    }
   
    public void displayDetails() {
        System.out.println("Subject: " + subjectName);
        System.out.println("Academic Year: " + academicYear);
        System.out.println("Semester/Grading Period: " + semester);
        System.out.println("Professor: " + professorName);
        System.out.println("Location: " + buildingRoom);
        System.out.println("Time: " + startTime + " - " + endTime);
        StringBuilder daysBuilder = new StringBuilder();
    for (int day : meetingDays) {
        daysBuilder.append(SubjectManager.getDayOfWeekString(day)).append(", ");
    }

    if (daysBuilder.length() > 0) {
        daysBuilder.setLength(daysBuilder.length() - 2);
    }

    System.out.println("Meeting Days: " + daysBuilder);
    System.out.println("-----------------------------");
    }
}

class SubjectManager extends Manager {
    private ArrayList<Subject> subjectItems = new ArrayList<>();
    private DatabaseManager dbManager;
    private int currentUserId;

    public SubjectManager(int userId) {
        this.dbManager = new DatabaseManager();
        this.currentUserId = userId;
    }

    private String ordinalSuffix(int number) {
        if (number % 100 >= 11 && number % 100 <= 13) {
            return number + "th";
        }
        switch (number % 10) {
            case 1: return number + "st";
            case 2: return number + "nd";
            case 3: return number + "rd";
            default: return number + "th";
        }
    }

    private String getTimeWithPeriod(String time, int periodChoice) {
        return time + (periodChoice == 1 ? " AM" : " PM");
    }
    private int getValidatedInteger(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("\n\u001B[31mInvalid input. Please enter a valid integer.\u001B[0m");
            }
        }
    }

    private int getValidatedChoice(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int input = Integer.parseInt(scanner.nextLine().trim());
                if (input == 1 || input == 2) {
                    return input;
                } else {
                    System.out.println("\n\u001B[31mInvalid input. Please enter either 1 or 2.\u001B[0m");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n\u001B[31mInvalid input. Please enter a valid integer.\u001B[0m");
            }
        }
    }

    private int getValidatedYear(Scanner scanner, String prompt) {
        int currentYear = java.time.Year.now().getValue(); 
        while (true) {
            int year = getValidatedInteger(scanner, prompt);
            if (year < currentYear) {
                System.out.println("\n\u001B[31mInvalid year. You cannot enter a year earlier than the current year (" + currentYear + ").\u001B[0m");
            } else if (String.valueOf(year).length() != 4) {
                System.out.println("\n\u001B[31mInvalid year. Please enter a valid 4-digit year.\u001B[0m");
            } else {
                return year; 
            }
        }
    }

    private int getValidatedPeriod(Scanner scanner, String type) {
        while (true) {
            try {
                System.out.print("Enter the number for the " + type + ": ");
                int number = Integer.parseInt(scanner.nextLine().trim());
    
                if ((type.equals("Semester") && (number == 1 || number == 2)) || 
                    (type.equals("Grading Period") && (number >= 1 && number <= 4))) {
                    return number;
                } else {
                    System.out.println("\n\u001B[31mInvalid input. For Semester, enter 1 or 2. For Grading Period, enter a number between 1 and 4.\u001B[0m");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n\u001B[31mInvalid input. Please enter a valid number.\u001B[0m");
            }
        }
    }
    

    private String getValidatedTime(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String time = scanner.nextLine().trim();
            if (time.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
                return time;
            } else {
                System.out.println("\n\u001B[31mInvalid time format. Please enter the time in HH:mm format (e.g., 08:00).\u001B[0m");
            }
        }
    }

    private ArrayList<Integer> getValidatedMeetingDays(Scanner scanner) {
        ArrayList<Integer> days = new ArrayList<>();
        
        for (int i = 1; i <= 7; i++) {
            System.out.println(i + ". " + getDayOfWeekString(i));
        }        
        while (days.isEmpty()) {
            System.out.print("Enter the numbers corresponding to the days (e.g., 1 3 5 for Monday, Wednesday, Friday): ");
            String input = scanner.nextLine().trim();
            String[] dayInputs = input.split(" ");
            try {
                for (String day : dayInputs) {
                    int dayNum = Integer.parseInt(day);
                    if (dayNum >= 1 && dayNum <= 7 && !days.contains(dayNum)) {
                        days.add(dayNum);
                    } else {
                        System.out.println("\n\u001B[31mInvalid input. Please enter valid day numbers between 1 and 7.\u001B[0m");
                        days.clear();
                        break;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("\n\u001B[31mInvalid input. Please enter numbers only.\u001B[0m");
            }
        }
        return days;
    }

    public void addItem() {
        titleDisplay("SUBJECT ORGANIZER");
        Scanner scanner = new Scanner(System.in);

        int startYear = getValidatedYear(scanner, "\nEnter the starting year of the academic year (e.g., 2024): ");
        int endYear = startYear + 1;
        String academicYear = startYear + "-" + endYear;

        int typeChoice = getValidatedChoice(scanner, "Choose 1 for Semester or 2 for Grading Period: ");
        String type = (typeChoice == 1) ? "Semester" : "Grading Period";

        int number = getValidatedPeriod(scanner, type);
        String semester = ordinalSuffix(number) + " " + type;

        System.out.print("\nPick/Add a subject: \n");
        String subjectName = manageSubjects();

        String professorName;
    do {
        System.out.print("Enter the professor's name: ");
        professorName = scanner.nextLine().trim();
        if (professorName.isEmpty()) {
            System.out.println("\n\u001B[31mProfessor's name cannot be empty. Please enter a valid name.\u001B[0m");
        }
    } while (professorName.isEmpty());

    String building;
    do {
        System.out.print("Enter the building name: ");
        building = scanner.nextLine().trim();
        if (building.isEmpty()) {
            System.out.println("\n\u001B[31mBuilding name cannot be empty. Please enter a valid name.\u001B[0m");
        }
    } while (building.isEmpty());

    String room;
    do {
        System.out.print("Enter the room number: ");
        room = scanner.nextLine().trim();
        if (room.isEmpty()) {
            System.out.println("\n\u001B[31mRoom number cannot be empty. Please enter a valid number.\u001B[0m");
        }
    } while (room.isEmpty());
    String buildingRoom = building + ", Room " + room;

        String startTimeInput = getValidatedTime(scanner, "Enter the start time (e.g., 08:00): ");
        int startPeriod = getValidatedChoice(scanner, "Choose 1 for AM or 2 for PM: ");
        String startTime = getTimeWithPeriod(startTimeInput, startPeriod);

        String endTimeInput = getValidatedTime(scanner, "Enter the end time (e.g., 10:00): ");
        int endPeriod = getValidatedChoice(scanner, "Choose 1 for AM or 2 for PM: ");
        String endTime = getTimeWithPeriod(endTimeInput, endPeriod);

        ArrayList<Integer> meetingDays = getValidatedMeetingDays(scanner);

        for (Subject subject : subjectItems) {
            for (int day : meetingDays) {
                if (subject.getMeetingDays().contains(day)) {
                    if ((startTime.compareTo(subject.getEndTime()) < 0 && endTime.compareTo(subject.getStartTime()) > 0)) {
                        System.out.println("\n\u001B[31mSchedule conflict detected for " + subject.getSubjectName() + " on day " + day + "!\u001B[0m");
                        return;
                    }
                }
            }
        }

        Subject newSubject = new Subject(academicYear, semester, subjectName, professorName, buildingRoom, startTime, endTime, meetingDays);
        subjectItems.add(newSubject);

        int subjectId = dbManager.saveSubject(currentUserId, academicYear, semester, subjectName, professorName);

        // Save schedule details for the subject
        for (int day : meetingDays) {
            dbManager.saveSchedule(subjectId, day, startTime, endTime, buildingRoom);  // Save each day individually
        }        System.out.println("Subject added successfully!");
    }

    @Override
public void viewItems() {
    titleDisplay("SCHEDULE FOR A.Y." + subjectItems.get(0).getAcademicYear());

    List<String> subjects = dbManager.getSubjectsByUserId(currentUserId);

    if (subjectItems.isEmpty()) {
        System.out.println("\n\u001B[31mNo subjects have been added yet.\u001B[0m");
        return;
    }

    for (String subject : subjects) {
        System.out.println(subject);
    }

    subjectItems.sort(Comparator.comparing(Subject::getStartTime));

    String semesterOrGradingPeriod = subjectItems.get(0).getSemester();
    int today = java.time.LocalDate.now().getDayOfWeek().getValue();
    String dayOfWeek = getDayOfWeekString(today);

    displayCenteredText(semesterOrGradingPeriod + " " + dayOfWeek + " Schedule\n");

    boolean isAnySubjectScheduledToday = false;

    int timeWidth = 15;
    int subjectWidth = 25;
    int buildingWidth = 22;

    String header = String.format("%s | %s | %s",
        centerText("Start Time", timeWidth),
        centerText("Subject Name", subjectWidth),
        centerText("Building & Room", buildingWidth));
    System.out.println(header);
    System.out.println("-".repeat(header.length()));

    for (Subject subject : subjectItems) {
        if (subject.getMeetingDays().contains(today)) {
            String row = String.format("%s | %s | %s",
                centerText(subject.getStartTime(), timeWidth),
                centerText(subject.getSubjectName(), subjectWidth),
                centerText(subject.getBuildingRoom(), buildingWidth));
            System.out.println(row);
            isAnySubjectScheduledToday = true;
        }
    }

    if (!isAnySubjectScheduledToday) {
        System.out.println("\n\u001B[31mNo subjects scheduled for today.\u001B[0m");
        return;
    }

    handleUserActions();
}

private String centerText(String text, int width) {
    int padding = (width - text.length()) / 2;
    int extraPadding = (width - text.length()) % 2;
    return " ".repeat(Math.max(0, padding)) + text + " ".repeat(Math.max(0, padding + extraPadding));
}

private void displayCenteredText(String title) {
    int lineWidth = 62;
    int padding = (lineWidth - title.length()) / 2;
    System.out.println("\033[1m\033[34m" + " ".repeat(Math.max(0, padding)) + title + "\033[0m");
}

public static String getDayOfWeekString(int dayOfWeek) {
    return switch (dayOfWeek) {
        case 1 -> "Monday";
        case 2 -> "Tuesday";
        case 3 -> "Wednesday";
        case 4 -> "Thursday";
        case 5 -> "Friday";
        case 6 -> "Saturday";
        case 7 -> "Sunday";
        default -> "Unknown Day";
    };
}

private void handleUserActions() {
    Scanner scanner = new Scanner(System.in);
    while (true) {
        System.out.println("\n1. See further details");
        System.out.println("2. Exit");
        System.out.print("Enter your choice: ");

        int action = getValidatedInteger(scanner, "");
        if (action == 1) {
            displaySubjectDetails(scanner);
        } else if (action == 2) {
            System.out.println("\n\u001B[1m\u001B[34mExiting to main menu...\u001B[0m");
            break;
        } else {
            System.out.println("\u001B[31mInvalid input. Please enter '1' or '2'.\u001B[0m");
        }
    }
}

private void displaySubjectDetails(Scanner scanner) {
    System.out.println("\n\u001B[1m\u001B[34mSubjects List:\u001B[0m");
    for (int i = 0; i < subjectItems.size(); i++) {
        System.out.println((i + 1) + ". " + subjectItems.get(i).getSubjectName());
    }

    int choice = getValidatedInteger(scanner, "\nEnter the number of the subject you want to see details for: ");
    if (choice > 0 && choice <= subjectItems.size()) {
        subjectItems.get(choice - 1).displayDetails();
    } else {
        System.out.println("\n\u001B[31mInvalid choice. Please select a valid subject number.\u001B[0m");
    }
}
}