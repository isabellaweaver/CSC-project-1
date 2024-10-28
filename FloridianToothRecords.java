import java.util.Scanner;

/**
 * Class represents the tooth records for a Floridian family.
 * Allows recording tooth types, extracting teeth, and calculating root canal indices.
 * @author Isabella Weaver
 * @version 1.0
 */

public class FloridianToothRecords {

    final static int MAX_FAMILY_MEMBERS = 6;
    final static int MAX_TEETH = 8;

    /**
     * Main method to run the program.
     * Initializes and manages the program flow, handling family member input, teeth records,
     * and menu options for printing, extracting, and calculating root canals.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numFamilyMembers;
        char choice;
        String extractedFamilyMember = " ";

        // Display the welcome message
        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

        // Prompt the user to enter the number of family members
        System.out.printf("%-50s: ", "Please enter the number of people in the family");
        do {
            numFamilyMembers = scanner.nextInt();
            // Check if the input is within allowed range
            if (numFamilyMembers <= 0 || numFamilyMembers > MAX_FAMILY_MEMBERS) {
                System.out.printf("%-50s: ", "Invalid number of people, try again");
            }
        } while (numFamilyMembers <= 0 || numFamilyMembers > MAX_FAMILY_MEMBERS);

        scanner.nextLine(); // Consume the newline character left by nextInt

        String[] names = new String[numFamilyMembers];
        char[][][] teethRecords = new char[numFamilyMembers][2][MAX_TEETH];

        // Loop to input each family member's name and tooth records
        for (int i = 0; i < numFamilyMembers; i++) {
            System.out.printf("%-50s: ", "Please enter the name for family member " + (i + 1));
            names[i] = scanner.nextLine();

            // Loop to input upper and lower teeth for the current family member
            for (int j = 0; j < 2; j++) {
                String upperOrLower = (j == 0) ? "uppers" : "lowers";
                String teethInput;
                System.out.printf("%-50s: ", "Please enter the " + upperOrLower + " for " + names[i]);

                // Loop until valid input is provided for teeth (matching "B", "M", "I" and spaces)
                do {
                    teethInput = scanner.nextLine().toUpperCase();

                    // Check length and valid tooth types
                    if (teethInput.length() > MAX_TEETH) {
                        System.out.printf("%-50s: ", "Too many teeth, try again");
                    } else if (!teethInput.matches("[BMI ]*")) {
                        System.out.printf("%-50s: ", "Invalid teeth types, try again");
                    } else {
                        break;
                    }
                } while (true);


                // Store valid teeth input into the teethRecords array
                teethRecords[i][j] = teethInput.toCharArray();
            }
        }

        // Main menu loop to process user's menu choices
        do {
            choice = menu(scanner);
            switch (choice) {
                case 'P':
                    printTeethRecords(names, teethRecords);
                    break;
                case 'E':
                    extractTooth(scanner, names, teethRecords);
                    break;
                case 'R':
                    calculateRootCanalIndices(names, teethRecords);
                    break;
            }
        } while (choice != 'X');

        System.out.println("\nExiting the Floridian Tooth Records :-)");
    }

    /**
     * Displays the menu options and retrieves the user's choice.
     *
     * @param scanner Scanner instance for user input
     * @return the character representing the user's menu choice
     */
    private static char menu(Scanner scanner) {
        char choice;
        System.out.printf("%-51s: ", "\n(P)rint, (E)xtract, (R)oot, e(X)it");
        choice = scanner.next().charAt(0);
        scanner.nextLine();
        choice = Character.toUpperCase(choice);

        // Validate that choice is a valid menu option
        while (!(choice == 'P' || choice == 'E' || choice == 'R' || choice == 'X')) {
            System.out.printf("%-50s: ", "Invalid menu option, try again");
            choice = Character.toUpperCase(scanner.next().charAt(0));
            scanner.nextLine();
        }
        return choice;
    }

    /**
     * Prints the teeth records of each family member.
     *
     * @param names        Array of family member names
     * @param teethRecords 3D array containing teeth information for each family member
     */
    public static void printTeethRecords(String[] names, char[][][] teethRecords) {
        System.out.println();

        // Loop to print each family member's name and corresponding teeth records
        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i]);
            System.out.println("  Uppers: " + formatTeeth(teethRecords[i][0]));
            System.out.println("  Lowers: " + formatTeeth(teethRecords[i][1]));
        }
    }

    /**
     * Allows the user to extract a tooth for a specified family member and layer.
     *
     * @param scanner      Scanner instance for user input
     * @param names        Array of family member names
     * @param teethRecords 3D array containing teeth information for each family member
     * @return the name of the family member whose tooth was extracted
     */
    public static String extractTooth(Scanner scanner, String[] names, char[][][] teethRecords) {
        System.out.printf("%-50s: ", "Which family member");
        String familyMember = " ";
        int index = -1;
        boolean validInput = false;

        // Loop until a valid family member name is entered
        while (!validInput) {
            familyMember = scanner.nextLine().toUpperCase();
            index = findFamilyMemberIndex(names, familyMember);
            if (index == -1) {
                System.out.printf("%-50s: ", "Invalid family member, try again");
            } else {
                validInput = true;
            }
        }

        // Prompt for the tooth layer (Upper or Lower)
        System.out.printf("%-50s: ", "Which tooth layer (U)pper or (L)ower");
        char layer;
        do {
            layer = Character.toUpperCase(scanner.nextLine().charAt(0));
            if (layer != 'U' && layer != 'L') {
                System.out.printf("%-50s: ", "Invalid layer, try again");
            }
        } while (layer != 'U' && layer != 'L');

        // Prompt for the specific tooth number within the layer
        System.out.printf("%-50s: ", "Which tooth number");
        int toothNumber;
        boolean extracted = false;

        // Loop until a valid tooth number is provided for extraction
        while (!extracted) {
            toothNumber = scanner.nextInt() - 1; // Adjust for 0-based index
            scanner.nextLine(); // Consume the newline character
            if (toothNumber < 0 || toothNumber >= MAX_TEETH) {
                System.out.printf("%-50s: ", "Invalid tooth number, try again");
            } else if (teethRecords[index][layer == 'U' ? 0 : 1][toothNumber] == 'M') {
                System.out.printf("%-50s: ", "Missing tooth, try again");
            } else {
                teethRecords[index][layer == 'U' ? 0 : 1][toothNumber] = 'M'; // Mark tooth as missing
                extracted = true;
            }
        }

        return familyMember;
    }

    /**
     * Calculates and displays root canal indices for the entire family.
     *
     * @param names        Array of family member names
     * @param teethRecords 3D array containing teeth information for each family member
     */
    public static void calculateRootCanalIndices(String[] names, char[][][] teethRecords) {
        int totalI = 0, totalB = 0, totalM = 0;

        // Aggregate counts for each tooth type across all family members
        for (int i = 0; i < names.length; i++) {
            totalI += countTeeth(teethRecords[i], 'I');
            totalB += countTeeth(teethRecords[i], 'B');
            totalM += countTeeth(teethRecords[i], 'M');
        }

        // Calculate the discriminant and check for real roots
        double discriminant = totalB * totalB - 4 * totalI * -totalM;

        if (discriminant >= 0 && totalI != 0) {
            double root1 = (-totalB + Math.sqrt(discriminant)) / (2 * totalI);
            double root2 = (-totalB - Math.sqrt(discriminant)) / (2 * totalI);
            System.out.printf("One root canal at     %.2f%n", root1);
            System.out.printf("Another root canal at %.2f%n", root2);
        } else {
            System.out.println("No real roots (no root canals needed).");
        }
    }

    /**
     * Counts occurrences of a specific tooth type for a family member.
     *
     * @param teeth 2D array representing a family member's teeth
     * @param type  Character representing the tooth type to count
     * @return the count of the specified tooth type
     */
    public static int countTeeth(char[][] teeth, char type) {
        int count = 0; // Initialize count of specified tooth type to zero
        // Iterate through each row in the teeth array
        for (char[] row : teeth) {
            // Iterate through each tooth in the current row
            for (char tooth : row) {
                // Check if the current tooth matches the specified type
                if (tooth == type) {
                    count++; // Increment count if there's a match
                }
            }
        }
        return count; // Return the total count of specified tooth type
    }

    /**
     * Finds the index of a family member in the names array.
     *
     * @param names         Array of family member names
     * @param familyMember  Name of the family member to find
     * @return the index of the family member, or -1 if not found
     */
    public static int findFamilyMemberIndex(String[] names, String familyMember) {
        for (int i = 0; i < names.length; i++) {
            // Check if the current name matches the family member name (case-insensitive)
            if (names[i].equalsIgnoreCase(familyMember)) {
                return i; // Return the index if found
            }
        }
        return -1; // Return -1 if the family member is not found
    }

    /**
     * Formats an array of teeth for display purposes.
     *
     * @param teeth Array of teeth characters to format
     * @return a string representation of the teeth
     */
    public static String formatTeeth(char[] teeth) {
        StringBuilder formatted = new StringBuilder();
        // Iterate through the teeth array to format each tooth
        for (int i = 0; i < teeth.length; i++) {
            // Append the index and corresponding tooth character to StringBuilder
            formatted.append(i + 1).append(":").append(teeth[i]).append(" ");
        }
        return formatted.toString().trim();
    }
}
