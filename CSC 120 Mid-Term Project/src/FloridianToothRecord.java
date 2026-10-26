import java.util.Scanner;

/**
 * Program FloridianToothRecord
 *
 * Records the teeth of the members of one Florida family. The user
 * enters the number of people in the family (max = 6), then each
 * person's name and a string of tooth types for their uppers and for
 * their lowers (max = 8 teeth each). Each tooth is an incisor ('I'),
 * bicuspid ('B'), or missing tooth ('M'). The names are stored in an
 * array of strings, and the teeth in a 3D array of
 * characters: one plane per person, two rows (uppers and lowers), and one
 * column per tooth.
 *
 * A menu then lets the user print the family's teeth record, extract a
 * tooth, report the family's root canal indices (the roots of the
 * quadratic Ix^2 + Bx - M), or exit.
 *
 * @author  Alastair W. Shen
 * @version Oct. 26 2026
 */
public class FloridianToothRecord {

    /** Keyboard input, shared by every method that reads from the user. */
    private static final Scanner keyboard = new Scanner(System.in);

    /** Smallest number of people allowed in a family. */
    private static final int MIN_PEOPLE = 1;
    /** Largest number of people allowed in a family. */
    private static final int MAX_PEOPLE = 6;
    /** Largest number of teeth, including missing teeth, in one layer. */
    private static final int MAX_TEETH = 8;
    /** Number of tooth layers each person has (uppers and lowers). */
    private static final int NUM_LAYERS = 2;
    /** Row of the teeth array that holds a person's uppers. */
    private static final int UPPER = 0;
    /** Row of the teeth array that holds a person's lowers. */
    private static final int LOWER = 1;

    /** Tooth type letter for an incisor. */
    private static final char INCISOR = 'I';
    /** Tooth type letter for a bicuspid. */
    private static final char BICUSPID = 'B';
    /** Tooth type letter for a missing tooth. */
    private static final char MISSING = 'M';

    /** Menu choice to print the family's teeth record. */
    private static final char PRINT = 'P';
    /** Menu choice to extract a tooth. */
    private static final char EXTRACT = 'E';
    /** Menu choice to report the root canal indices. */
    private static final char ROOT = 'R';
    /** Menu choice to exit the program. */
    private static final char EXIT = 'X';

    /** Layer choice for the upper teeth. */
    private static final char UPPER_CHOICE = 'U';
    /** Layer choice for the lower teeth. */
    private static final char LOWER_CHOICE = 'L';

    /** Number of tooth types that are counted (I, B, and M). */
    private static final int NUM_TOOTH_TYPES = 3;
    /** Position of the incisor total in the tooth counts array. */
    private static final int INCISOR_COUNT = 0;
    /** Position of the bicuspid total in the tooth counts array. */
    private static final int BICUSPID_COUNT = 1;
    /** Position of the missing tooth total in the tooth counts array. */
    private static final int MISSING_COUNT = 2;

    /** Marks that a name was not found among the family members. */
    private static final int NOT_FOUND = -1;

    /** Format for every prompt, so all the colons line up. */
    private static final String PROMPT_FORMAT = "%-44s: ";
    /** Format for one root canal index line. */
    private static final String ROOT_FORMAT = "%-22s%.2f%n";

    /**
     * Runs the program: welcome message, records the family's teeth,
     * runs the menu until the user chooses to exit, then says goodbye.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        String[] names;
        char[][][] teeth;
        int numPeople;

        //Make room for the largest family; only numPeople are used
        names = new String[MAX_PEOPLE];
        teeth = new char[MAX_PEOPLE][NUM_LAYERS][];

        displayWelcome();
        numPeople = inputFamily(names, teeth);
        runMenu(names, teeth, numPeople);
        displayExit();

    }//end of main method

    /**
     * Displays the welcome message.
     */
    private static void displayWelcome() {

        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

    }//end of displayWelcome method

    /**
     * Reads the number of people in the family, then each person's name,
     * uppers, and lowers. The names and teeth are stored in the arrays
     * that are passed in.
     *
     * @param names the array to store each family member's name in
     * @param teeth the array to store each family member's teeth in,
     *              indexed [person][layer][tooth]
     * @return the number of people in the family
     */
    private static int inputFamily(String[] names, char[][][] teeth) {
        int numPeople;
        int person;

        numPeople = inputNumberOfPeople();

        for (person = 0; person < numPeople; person++) {
            System.out.printf(PROMPT_FORMAT,
                    "Please enter the name for family member " + (person + 1));
            names[person] = keyboard.next();

            //Each row sized by the teeth string the user enters
            System.out.printf(PROMPT_FORMAT, "Please enter the uppers for " + names[person]);
            teeth[person][UPPER] = inputTeethString();
            System.out.printf(PROMPT_FORMAT, "Please enter the lowers for " + names[person]);
            teeth[person][LOWER] = inputTeethString();
        }

        return numPeople;

    }//end of inputFamily method

    /**
     * Reads the number of people in the family, asking again until the
     * number is between MIN_PEOPLE and MAX_PEOPLE.
     *
     * @return the number of people in the family
     */
    private static int inputNumberOfPeople() {
        int numPeople;

        System.out.printf(PROMPT_FORMAT, "Please enter number of people in the family");
        numPeople = keyboard.nextInt();

        while (numPeople < MIN_PEOPLE || numPeople > MAX_PEOPLE) {
            System.out.printf(PROMPT_FORMAT, "Invalid number of people, try again");
            numPeople = keyboard.nextInt();
        }

        return numPeople;

    }//end of inputNumberOfPeople method

    /**
     * Reads one layer of teeth as a string of tooth type letters, asking
     * again until every letter is I, B, or M (in either case) and there
     * are no more than MAX_TEETH teeth. The caller displays the first
     * prompt.
     *
     * @return the teeth as an array of upper case tooth type letters
     */
    private static char[] inputTeethString() {
        String teethString;
        boolean validTypes;
        char toothType;
        char[] teethRow;
        int tooth;

        do {
            teethString = keyboard.next();

            //Check that every character is a tooth type
            validTypes = true;
            for (tooth = 0; tooth < teethString.length(); tooth++) {
                toothType = Character.toUpperCase(teethString.charAt(tooth));
                if (toothType != INCISOR && toothType != BICUSPID && toothType != MISSING) {
                    validTypes = false;
                }
            }

            //Types are checked before length, like in the sample run
            if (!validTypes) {
                System.out.printf(PROMPT_FORMAT, "Invalid teeth types, try again");
            }
            else if (teethString.length() > MAX_TEETH) {
                System.out.printf(PROMPT_FORMAT, "Too many teeth, try again");
            }
        } while (!validTypes || teethString.length() > MAX_TEETH);

        //Store the teeth as upper case letters, one per column
        teethRow = new char[teethString.length()];
        for (tooth = 0; tooth < teethString.length(); tooth++) {
            teethRow[tooth] = Character.toUpperCase(teethString.charAt(tooth));
        }

        return teethRow;

    }//end of inputTeethString method

    /**
     * Repeatedly offers the menu and carries out the chosen option, until
     * the user chooses to exit.
     *
     * @param names     the family members' names
     * @param teeth     the family members' teeth, [person][layer][tooth]
     * @param numPeople the number of people in the family
     */
    private static void runMenu(String[] names, char[][][] teeth, int numPeople) {
        char option;

        do {
            System.out.println();
            option = inputMenuOption();

            if (option == PRINT) {
                System.out.println();
                printRecords(names, teeth, numPeople);
            }
            else if (option == EXTRACT) {
                extractTooth(names, teeth, numPeople);
            }
            else if (option == ROOT) {
                reportRoots(teeth, numPeople);
            }
            //EXIT needs no action here; the loop condition ends the menu
        } while (option != EXIT);

    }//end of runMenu method

    /**
     * Reads a menu option, asking again until it is P, E, R, or X (in
     * either case).
     *
     * @return the menu option, in upper case
     */
    private static char inputMenuOption() {
        char option;

        System.out.printf(PROMPT_FORMAT, "(P)rint, (E)xtract, (R)oot, e(X)it");
        option = Character.toUpperCase(keyboard.next().charAt(0));

        while (option != PRINT && option != EXTRACT && option != ROOT && option != EXIT) {
            System.out.printf(PROMPT_FORMAT, "Invalid menu option, try again");
            option = Character.toUpperCase(keyboard.next().charAt(0));
        }

        return option;

    }//end of inputMenuOption method

    /**
     * Prints each family member's name, uppers, and lowers.
     *
     * @param names     the family members' names
     * @param teeth     the family members' teeth, [person][layer][tooth]
     * @param numPeople the number of people in the family
     */
    private static void printRecords(String[] names, char[][][] teeth, int numPeople) {
        int person;

        for (person = 0; person < numPeople; person++) {
            System.out.println(names[person]);
            printRow("  Uppers:", teeth[person][UPPER]);
            printRow("  Lowers:", teeth[person][LOWER]);
        }

    }//end of printRecords method

    /**
     * Prints one layer of teeth on one line, as tooth number and tooth
     * type pairs, after a label.
     *
     * @param label the label printed at the start of the line
     * @param row   the teeth in the layer
     */
    private static void printRow(String label, char[] row) {
        int tooth;

        System.out.print(label);
        for (tooth = 0; tooth < row.length; tooth++) {
            //Tooth numbers start at 1 for the user, indexes at 0
            System.out.print("  " + (tooth + 1) + ":" + row[tooth]);
        }
        System.out.println();

    }//end of printRow method

    /**
     * Extracts one tooth chosen by the user, by marking it as missing. The
     * user chooses the family member, the layer, and the tooth number. A
     * tooth that is already missing cannot be extracted.
     *
     * @param names     the family members' names
     * @param teeth     the family members' teeth, [person][layer][tooth]
     * @param numPeople the number of people in the family
     */
    private static void extractTooth(String[] names, char[][][] teeth, int numPeople) {
        int person;
        int layer;
        int rowLength;
        int toothIndex;
        int teethPresent;
        int tooth;

        person = inputFamilyMember(names, numPeople);
        layer = inputLayer();
        rowLength = teeth[person][layer].length;

        //Count the teeth that are there, so we never ask forever
        teethPresent = 0;
        for (tooth = 0; tooth < rowLength; tooth++) {
            if (teeth[person][layer][tooth] != MISSING) {
                teethPresent++;
            }
        }

        if (teethPresent == 0) {
            System.out.println("There are no teeth to extract in that layer");
        }
        else {
            toothIndex = inputToothNumber("Which tooth number", rowLength);

            //A missing tooth cannot be extracted, so ask again
            while (teeth[person][layer][toothIndex] == MISSING) {
                toothIndex = inputToothNumber("Missing tooth, try again", rowLength);
            }

            teeth[person][layer][toothIndex] = MISSING;
        }

    }//end of extractTooth method

    /**
     * Reads the name of a family member, asking again until the name
     * matches one in the family (upper and lower case are treated as the
     * same).
     *
     * @param names     the family members' names
     * @param numPeople the number of people in the family
     * @return the index of the matching family member
     */
    private static int inputFamilyMember(String[] names, int numPeople) {
        String name;
        int personIndex;
        int person;

        System.out.printf(PROMPT_FORMAT, "Which family member");

        do {
            name = keyboard.next();

            //Look for the name among family members
            personIndex = NOT_FOUND;
            for (person = 0; person < numPeople; person++) {
                if (names[person].equalsIgnoreCase(name)) {
                    personIndex = person;
                }
            }

            if (personIndex == NOT_FOUND) {
                System.out.printf(PROMPT_FORMAT, "Invalid family member, try again");
            }
        } while (personIndex == NOT_FOUND);

        return personIndex;

    }//end of inputFamilyMember method

    /**
     * Reads the tooth layer, asking again until it is U or L (in either
     * case).
     *
     * @return UPPER for the uppers, or LOWER for the lowers
     */
    private static int inputLayer() {
        char layerChoice;
        int layer;

        System.out.printf(PROMPT_FORMAT, "Which tooth layer (U)pper or (L)ower");
        layerChoice = Character.toUpperCase(keyboard.next().charAt(0));

        while (layerChoice != UPPER_CHOICE && layerChoice != LOWER_CHOICE) {
            System.out.printf(PROMPT_FORMAT, "Invalid layer, try again");
            layerChoice = Character.toUpperCase(keyboard.next().charAt(0));
        }

        //Turn user's letter into a row of the teeth array
        if (layerChoice == UPPER_CHOICE) {
            layer = UPPER;
        }
        else {
            layer = LOWER;
        }

        return layer;

    }//end of inputLayer method

    /**
     * Reads a tooth number, asking again until it is between 1 and the
     * number of teeth in the layer.
     *
     * @param prompt    the first prompt to display
     * @param rowLength the number of teeth in the layer
     * @return the index of the tooth (one less than the tooth number)
     */
    private static int inputToothNumber(String prompt, int rowLength) {
        int toothNumber;

        System.out.printf(PROMPT_FORMAT, prompt);
        toothNumber = keyboard.nextInt();

        while (toothNumber < 1 || toothNumber > rowLength) {
            System.out.printf(PROMPT_FORMAT, "Invalid tooth number, try again");
            toothNumber = keyboard.nextInt();
        }

        //User counts teeth from 1, the array indexes from 0
        return toothNumber - 1;

    }//end of inputToothNumber method

    /**
     * Reports the family's root canal indices, which are the roots of the
     * quadratic Ix^2 + Bx - M, where I, B, and M are the family's numbers
     * of incisors, bicuspids, and missing teeth.
     *
     * @param teeth     the family members' teeth, [person][layer][tooth]
     * @param numPeople the number of people in the family
     */
    private static void reportRoots(char[][][] teeth, int numPeople) {
        int[] toothCounts;
        int numIncisors;
        int numBicuspids;
        int numMissing;
        double discriminant;
        double firstRoot;
        double secondRoot;

        toothCounts = countTeeth(teeth, numPeople);
        numIncisors = toothCounts[INCISOR_COUNT];
        numBicuspids = toothCounts[BICUSPID_COUNT];
        numMissing = toothCounts[MISSING_COUNT];

        //If no incisors there is no x^2 term, so no quadratic
        if (numIncisors == 0) {
            System.out.println("Cannot find root canal indices without incisors");
        }
        else {
            //B^2 - 4(I)(-M) = B^2 + 4IM, never negative
            discriminant = numBicuspids * numBicuspids + 4 * numIncisors * numMissing;
            firstRoot = (-numBicuspids + Math.sqrt(discriminant)) / (2 * numIncisors);
            secondRoot = (-numBicuspids - Math.sqrt(discriminant)) / (2 * numIncisors);

            System.out.printf(ROOT_FORMAT, "One root canal at", firstRoot);
            System.out.printf(ROOT_FORMAT, "Another root canal at", secondRoot);
        }

    }//end of reportRoots method

    /**
     * Counts the family's incisors, bicuspids, and missing teeth.
     *
     * @param teeth     the family members' teeth, [person][layer][tooth]
     * @param numPeople the number of people in the family
     * @return an array holding the incisor, bicuspid, and missing tooth
     *         totals, at INCISOR_COUNT, BICUSPID_COUNT, and MISSING_COUNT
     */
    private static int[] countTeeth(char[][][] teeth, int numPeople) {
        int[] toothCounts;
        int person;
        int layer;
        int tooth;

        //A new int array starts with every total at 0
        toothCounts = new int[NUM_TOOTH_TYPES];

        for (person = 0; person < numPeople; person++) {
            for (layer = 0; layer < NUM_LAYERS; layer++) {
                for (tooth = 0; tooth < teeth[person][layer].length; tooth++) {
                    if (teeth[person][layer][tooth] == INCISOR) {
                        toothCounts[INCISOR_COUNT]++;
                    }
                    else if (teeth[person][layer][tooth] == BICUSPID) {
                        toothCounts[BICUSPID_COUNT]++;
                    }
                    else if (teeth[person][layer][tooth] == MISSING) {
                        toothCounts[MISSING_COUNT]++;
                    }
                }
            }
        }

        return toothCounts;

    }//end of countTeeth method

    /**
     * Displays the exit message, with a smile.
     */
    private static void displayExit() {

        System.out.println();
        System.out.println("Exiting the Floridian Tooth Records :D");

    }//end of displayExit method

}//end of FloridianToothRecord class