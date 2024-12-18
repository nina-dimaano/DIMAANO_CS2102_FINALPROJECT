import java.util.Scanner;

public class StyleAppName {
    public static void displayAppName() {
        String appName = 
            "       ,--.                                                                             \n" +
            "   ,--/  /|                                        ___                                 \n" +
            ",---,': / '                                      ,--.'|_    ,--,     .--.,             \n" +
            ":   : '/ /       ,---,    ,---.           .---.  |  | :,' ,--.'|   ,--.'  \\            \n" +
            "|   '   ,    ,-+-. /  |  '   ,',\\         /. ./|  :  : ' : |  |,    |  | \\/\\            \n" +
            "'   |  /    ,--.'|'   | /   /   |     .-'-. ' |.;__,'  /  `--'_    :  : :        .--,  \n" +
            "|   ;  ;   |   |  ,\"' |.   ; ,. :    /___/ \\: ||  |   |   ,' ,'|   :  | |-    /_ ./|  \n" +
            ":   '   \\  |   | /  | |'   | |: : .-'.. '   ' .:__,'| :   '  | |   |  : :/| , ' , ' :  \n" +
            "|   |    ' |   | |  | |'   | .; :/___/ \\:     '  '  : |__ |  | :   |  |  .'/___/ \\: |  \n" +
            "'   : |.  \\|   | |  |/ |   :    |.   \\  ' .\\     |  | '.'|'  : |__ '  : '   .  \\  ' |  \n" +
            "|   | '_\\.'|   | |--'   \\   \\  /  \\   \\   ' \\ |  ;  :    ;|  | '.'||  | |    \\  ;   :  \n" +
            "'   : |    |   |/        `----'    \\   \\  |--\"   |  ,   / ;  :    ;|  : \\     \\  \\  ;  \n" +
            ";   |,'    '---'                    \\   \\ |       ---`-'  |  ,   / |  |,'      :  \\  \\ \n" +
            "'---'                                '---\"                 ---`-'  `--'         \\  ' ; \n" +
            "                                                                                 `--`  ";

            String reset = "\u001B[0m";         
            String bold = "\u001B[1m";          
            String blue = "\u001B[34m";
            String cyan = "\u001B[96m";         
            String subName = "P e r s o n a l   L e a r n i n g   C o m p a n i o n";
            int consoleWidth = 80;
            int textLength = subName.length();
            int padding = (consoleWidth - textLength) / 2;
            String spaces = " ".repeat(padding);

        System.out.println(bold + cyan + appName + reset);
        System.out.println(spaces + bold + blue + subName + reset + "\n\n");
    }
    
    public static void displayThankYou() {
        String thankYou = 
            " \n_________  ___  ___  ________  ________   ___  __             ___    ___ ________  ___  ___     \n" +
            "|\\___   ___\\\\  \\|\\  \\|\\   __  \\|\\   ___  \\|\\  \\|\\  \\          |\\  \\  /  /|\\   __  \\|\\  \\|\\  \\    \n" +
            "\\|___ \\  \\_\\ \\  \\\\\\  \\ \\  \\|\\  \\ \\  \\\\ \\  \\ \\  \\/  /|_        \\ \\  \\/  / | \\  \\|\\  \\ \\  \\\\\\  \\   \n" +
            "     \\ \\  \\ \\ \\   __  \\ \\   __  \\ \\  \\\\ \\  \\ \\   ___  \\        \\ \\    / / \\ \\  \\\\\\  \\ \\  \\\\\\  \\  \n" +
            "      \\ \\  \\ \\ \\  \\ \\  \\ \\  \\ \\  \\ \\  \\\\ \\  \\ \\  \\\\ \\  \\        \\/  /  /   \\ \\  \\\\\\  \\ \\  \\\\\\  \\ \n" +
            "       \\ \\__\\ \\ \\__\\ \\__\\ \\__\\ \\__\\ \\__\\\\ \\__\\ \\__\\\\ \\__\\     __/  / /      \\ \\_______\\ \\_______\\\n" +
            "        \\|__|  \\|__|\\|__|\\|__|\\|__|\\|__| \\|__|\\|__| \\|__|    |\\___/ /        \\|_______|\\|_______|\n" +
            "                                                             \\|___|/                          \n ";

            String reset = "\u001B[0m";         
            String bold = "\u001B[1m";          
            String blue = "\u001B[34m";
            String cyan = "\u001B[96m";         
            String subMessage = "F o r   U s i n g   K n o w T i f y !";
            int consoleWidth = 100;
            int textLength = subMessage.length();
            int padding = (consoleWidth - textLength) / 2;
            String spaces = " ".repeat(padding);

        System.out.println(bold + cyan + thankYou + reset);
        System.out.println(spaces + bold + blue + subMessage + reset);
    }

        public static void waitForUserInput() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
        }
        
}
