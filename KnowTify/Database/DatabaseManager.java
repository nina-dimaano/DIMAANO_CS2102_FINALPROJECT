import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement; 

public class DatabaseManager {

    public void saveUserPersonalInfo(String firstName, String middleName, String surname) {
        String query = "INSERT INTO users_personal_info (first_name, middle_name, surname) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, firstName);
            stmt.setString(2, middleName);
            stmt.setString(3, surname);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getUserId(String firstName, String surname) {
        String query = "SELECT user_id FROM users_personal_info WHERE first_name = ? AND surname = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, firstName);
            stmt.setString(2, surname);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void saveUserLogin(int userId, String username, String password) {
        String query = "INSERT INTO users_login (user_id, username, password) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveStudyStruggle(int userId, String struggleArea, String suggestedMethod, int weeklyGoal) throws SQLException {
        String query = "INSERT INTO study_struggles (user_id, struggle_area, suggested_method, weekly_goal) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, struggleArea);
            stmt.setString(3, suggestedMethod);
            stmt.setInt(4, weeklyGoal); 
            stmt.executeUpdate();
        }
    }
    
    public int getUserIdByLogin(String username, String password) throws SQLException {
        String query = "SELECT user_id FROM users_login WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error retrieving userId by login.");
        }
        return -1; // If no user found, return -1
    }  
    
    public boolean isUsernameTaken(String username) {
        String query = "SELECT 1 FROM users_login WHERE username = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If a result exists, the username is taken
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateUserStudyGoal(int userId, int weeklyGoal) throws SQLException {
        String query = "UPDATE study_struggles SET weekly_goal = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, weeklyGoal);
            stmt.setInt(2, userId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update study goal for user with ID: " + userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error updating study goal.");
        }
    }

    public int saveSubject(int userId, String academicYear, String semester, String subjectName, String professorName) {
        String query = "INSERT INTO subjects (user_id, academic_year, semester, subject_name, professor_name) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
    
            stmt.setInt(1, userId);
            stmt.setString(2, academicYear);
            stmt.setString(3, semester);
            stmt.setString(4, subjectName);
            stmt.setString(5, professorName);
    
            stmt.executeUpdate();
    
            // Retrieve the auto-generated subject_id
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // Return the generated subject ID
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if the insertion fails
    }
    

    // Retrieve all subjects for a specific user
    public List<String> getSubjectsByUserId(int userId) {
        List<String> subjects = new ArrayList<>();
        String query = "SELECT subject_id, academic_year, semester, subject_name, professor_name FROM subjects WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int subjectId = rs.getInt("subject_id");
                String academicYear = rs.getString("academic_year");
                String semester = rs.getString("semester");
                String subjectName = rs.getString("subject_name");
                String professorName = rs.getString("professor_name");

                subjects.add("ID: " + subjectId + ", " +
                             "Year: " + academicYear + ", " +
                             "Semester: " + semester + ", " +
                             "Subject: " + subjectName + ", " +
                             "Professor: " + professorName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving subjects.");
        }
        return subjects;
    }

    // Save a schedule to the schedules table
    public void saveSchedule(int subjectId, int dayOfWeek, String startTime, String endTime, String buildingRoom) {
        String query = "INSERT INTO schedules (subject_id, day_of_week, start_time, end_time, building_room) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, subjectId);
            stmt.setInt(2, dayOfWeek);
            stmt.setString(3, startTime);
            stmt.setString(4, endTime);
            stmt.setString(5, buildingRoom);

            stmt.executeUpdate();
            System.out.println("Schedule saved successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error saving schedule to database.");
        }
    }

    // Retrieve schedules for a specific subject
    public List<String> getSchedulesBySubjectId(int subjectId) {
        List<String> schedules = new ArrayList<>();
        String query = "SELECT schedule_id, day_of_week, start_time, end_time, building_room FROM schedules WHERE subject_id = ?";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, subjectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int scheduleId = rs.getInt("schedule_id");
                int dayOfWeek = rs.getInt("day_of_week");
                String startTime = rs.getString("start_time");
                String endTime = rs.getString("end_time");
                String buildingRoom = rs.getString("building_room");

                schedules.add("ID: " + scheduleId + ", " +
                              "Day: " + dayOfWeek + ", " +
                              "Start: " + startTime + ", " +
                              "End: " + endTime + ", " +
                              "Room: " + buildingRoom);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error retrieving schedules.");
        }
        return schedules;
    }

    public static void addTask(int userId, int subjectId, String taskTitle, String taskDescription, String dueDate) {
        String query = "INSERT INTO tasks (user_id, subject_id, task_title, task_description, due_date) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, subjectId);
            statement.setString(3, taskTitle);
            statement.setString(4, taskDescription);
            statement.setString(5, dueDate);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Task added successfully!");
            } else {
                System.out.println("Failed to add task.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateTaskStatus(int taskId, String newStatus, int userId) {
        String query = "UPDATE tasks SET status = ? WHERE task_id = ? AND user_id = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newStatus);
            statement.setInt(2, taskId);
            statement.setInt(3, userId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Task status updated to " + newStatus);
            } else {
                System.out.println("Failed to update task status.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addTaskAnalysis(int userId, int taskId, int totalTasks, int completedTasks, int timeUntilDue,
                                       double approxTimeToFinish, double recommendedDailyPrepTime, String analysisDate) {
        String query = "INSERT INTO task_analysis (user_id, task_id, total_tasks, completed_tasks, time_until_due, " +
                "approx_time_to_finish, recommended_daily_prep_time, analysis_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, taskId);
            statement.setInt(3, totalTasks);
            statement.setInt(4, completedTasks);
            statement.setInt(5, timeUntilDue);
            statement.setDouble(6, approxTimeToFinish);
            statement.setDouble(7, recommendedDailyPrepTime);
            statement.setString(8, analysisDate);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Task analysis added successfully!");
            } else {
                System.out.println("Failed to add task analysis.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}