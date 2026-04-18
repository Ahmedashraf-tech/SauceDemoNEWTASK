package utli;


import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import java.util.ArrayList;// Method to generate unique random numbers and save them in an ArrayList
import java.util.HashSet;
import java.util.Set;


public class Utility {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int NAME_LENGTH = 10;
    private static final String SAUDI_MOBILE_PREFIXES[] = {"050", "053", "054", "055", "056", "057", "058", "059"};
    private static final int PHONE_NUMBER_LENGTH = 9;
    private static final String CHARACTERS1 = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final String[] DOMAINS = {"gmail.com", "yahoo.com", "outlook.com"}; //added more domains
    private static final int MAX_USERNAME_LENGTH = 15;
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_+={}[]|;:'\",.<>/?";
    private static final String[] STREET_NAMES = {"Main", "Oak", "Pine", "Maple", "Cedar", "Park", "Hill", "Lake", "River", "Sunset"};
    private static final String[] STREET_TYPES = {"St", "Ave", "Blvd", "Dr", "Ct", "Ln", "Rd", "Pl", "Way", "Terrace"};
    private static final String[] CITIES = {"Cairo", "Chicago", "Houston", "Phoenix", "Philadelphia", "SanAntonio", "SanDiego", "Dallas", "SanJose"};
    private static final String[] STATES = {"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"};
    private static final String[] ZIP_CODES_PREFIXES = {"90", "10", "60", "77", "85", "19", "78", "92", "75", "95"}; // Just some sample prefixes


    private static final SecureRandom random = new SecureRandom();// Excluding the prefix
    //read from Excel
    public static String getExcelData(int RowNum, int ColNum, String SheetName) {
        XSSFWorkbook workBook;
        XSSFSheet sheet;
        String projectPath = System.getProperty("user.dir");
        String cellData = null;
        try {
            //src\test\resources\data\gptlbankdata.xlsx
            workBook = new XSSFWorkbook(projectPath + "/src/test/resources/data/gptlbankdata.xlsx");
            sheet = workBook.getSheet(SheetName);
            cellData = sheet.getRow(RowNum).getCell(ColNum).getStringCellValue();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            e.printStackTrace();
        }
        return cellData;
    }

    //parse data
    public static String getSingleJsonData(String jsonFilePath, String jsonField) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();

        FileReader fileReader = new FileReader(jsonFilePath);
        Object obj = jsonParser.parse(fileReader);

        JSONObject jsonObject = (JSONObject) obj;
        return jsonObject.get(jsonField).toString();
    }


    public static String generateName(int maxLength) {
        // Define the characters that can be part of the name (letters only)
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        // Create a Random object to generate random numbers
        Random random = new Random();

        // Use StringBuilder to build the random name
        StringBuilder randomName = new StringBuilder();

        // Generate random characters and append them to the StringBuilder
        for (int i = 0; i < maxLength; i++) {
            int index = random.nextInt(characters.length());
            randomName.append(characters.charAt(index));
        }

        return randomName.toString();
    }


    //generate Random name
    public static String generateRandomName(int n) {
        if (n <= 0 || n > NAME_LENGTH) {
            throw new IllegalArgumentException("n must be between 1 and " + NAME_LENGTH);
        }

        Random random = new Random();
        StringBuilder sb = new StringBuilder(NAME_LENGTH);

        // Generate the first 'n' random letters
        for (int i = 0; i < n; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }


        // Fill the rest with random letters as well (instead of digits)
        for (int i = n; i < NAME_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }

        return sb.toString();
    }
// generate randome state
    public static String generateRandomState() {
        Random random = new Random();
        int index = random.nextInt(STATES.length);
        return STATES[index];
    }
    // generate Random Saudi Number
    public static String generateRandomSaudiNumber() {
        Random random = new Random();

        // Choose a random prefix
        String prefix = SAUDI_MOBILE_PREFIXES[random.nextInt(SAUDI_MOBILE_PREFIXES.length)];

        // Generate the remaining 9 digits
        StringBuilder sb = new StringBuilder(PHONE_NUMBER_LENGTH);
        for (int i = 0; i < PHONE_NUMBER_LENGTH; i++) {
            sb.append(random.nextInt(10)); // Append a random digit (0-9)
        }

        return prefix + sb.toString();
    }


    public static String generateRandomSaudiNumberWithCountryCode() {
        return "+966" + generateRandomSaudiNumber().substring(1); //remove the leading zero after adding country code
    }
    private static final int ID_LENGTH = 5;


    public static String generateRandomID() {
        Random random = new Random();

        // Generate the random 7-digit ID
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            sb.append(random.nextInt(9)); // Append a random digit (0-9)
        }

        return sb.toString();
    }


    // genetate random Email
    public static String generateRandomEmail() {
        Random random = new Random();

        // Generate random username
        int usernameLength = random.nextInt(MAX_USERNAME_LENGTH) + 5; // Username length between 5 and 19 (inclusive)
        StringBuilder username = new StringBuilder(usernameLength);
        for (int i = 0; i < usernameLength; i++) {
            username.append(CHARACTERS1.charAt(random.nextInt(CHARACTERS1.length())));
        }
        String domain = DOMAINS[random.nextInt(DOMAINS.length)];

        return username.toString() + "@" + domain;
    }


    public static String generateComplexPassword(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("Password length must be at least 8 characters.");
        }

        // Ensure at least one character from each set
        StringBuilder password = new StringBuilder();
        password.append(getRandomCharacter(LOWERCASE));
        password.append(getRandomCharacter(UPPERCASE));
        password.append(getRandomCharacter(NUMBERS));
        password.append(getRandomCharacter(SPECIAL_CHARACTERS));

        // Fill the rest with random characters from all sets
        String allCharacters = LOWERCASE + UPPERCASE + NUMBERS + SPECIAL_CHARACTERS;
        for (int i = password.length(); i < length; i++) {
            password.append(getRandomCharacter(allCharacters));
        }

        // Shuffle the password to further randomize the character positions
        List<Character> passwordList = password.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(passwordList, random);

        return passwordList.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    private static char getRandomCharacter(String characterSet) {
        return characterSet.charAt(random.nextInt(characterSet.length()));
    }

    //generate dynamic
    public static int generateRandomNumber(int max) {
        Random rand = new Random();
        return rand.nextInt(max) + 1; // Generate a random number between 1 and 8 (inclusive)
    }

public static void generateshuffleRandomNumber(int max) {
    if (max <= 0) {
        throw new IllegalArgumentException("max must be greater than 0");
    }

    Random rand = new Random();
    int randomNumber = rand.nextInt(max) + 1;  // Generates a random number between 1 and max (inclusive)

    // Print the generated random number
    // System.out.println("Generated Random Number: " + randomNumber);

    // Generate a list of numbers from 1 to randomNumber-1
    List<Integer> availableNumbers = new ArrayList<>();
    for (int i = 1; i < randomNumber; i++) {
        availableNumbers.add(i);
    }

    // Shuffle the list to randomize the order
    Collections.shuffle(availableNumbers);
}


    //generate random city
public static String generateRandomCity() {
    String city = CITIES[random.nextInt(CITIES.length)];
    return city;
}

    public static String generateRandomAddress() {
        int streetNumber = random.nextInt(1000) + 1; // 1-1000
        String streetName = STREET_NAMES[random.nextInt(STREET_NAMES.length)];
        String streetType = STREET_TYPES[random.nextInt(STREET_TYPES.length)];
        String city = CITIES[random.nextInt(CITIES.length)];
        String state = STATES[random.nextInt(STATES.length)];
        String zipCode = ZIP_CODES_PREFIXES[random.nextInt(ZIP_CODES_PREFIXES.length)] + String.format("%03d", random.nextInt(1000));

        return streetNumber + " " + streetName + " " + streetType + ", " + city + ", " + state + " " + zipCode;
    }

    //genraterandomPostalCode
    public static int generateRandomPostalCode() {
        Random random = new Random();
        return random.nextInt(999999) + 1; // Generate a number between 1 and 999999
    }

    // openBrowserNetworkTab

    /* public static void openBrowserNetworkTab() throws AWTException {
        // Create Robot instance
        Robot robot = new Robot();

        // Add a delay for setup (optional)
        robot.delay(2000); // Wait for the browser window to be active

        // Step 1: Press Ctrl+Shift+I to open Developer Tools
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_I);

        // Add a delay for Developer Tools to open
        robot.delay(1000);

        // release press buttons
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.keyRelease(KeyEvent.VK_I);

        // Step 2: Navigate to the Network tab
        // Use Right Arrow key multiple times to move to the Network tab
        for (int i = 0; i < 3; i++) {
            // Press and hold Ctrl
            robot.keyPress(KeyEvent.VK_CONTROL);

            // Press and release ]
            robot.keyPress(KeyEvent.VK_CLOSE_BRACKET);
            robot.keyRelease(KeyEvent.VK_CLOSE_BRACKET);
            // Release Ctrl
            robot.keyRelease(KeyEvent.VK_CONTROL);

            // Add a small delay between presses
            robot.delay(200);
        }
    } */

    public static String getRandomZipCodePrefix() {
        // Generates a number between 0 (inclusive) and 1000 (exclusive)
        int prefix = ThreadLocalRandom.current().nextInt(1000);

        // Formats the integer as a 3-digit string with leading zeros
        return String.format("%03d", prefix);
    }

public static ArrayList<Integer> generateUniqueRandomNumbers(int max, int count) {
        // Set to ensure uniqueness of random numbers
        Set<Integer> uniqueNumbersSet = new HashSet<>();
        ArrayList<Integer> uniqueNumbersList = new ArrayList<>(); // To store the unique numbers

        // Generate unique random numbers
        for (int i = 1; i <= count; i++) {
            int uniqueRandomNum;
            do {
                uniqueRandomNum = generateRandomNumber(max); // Generate random number
            } while (uniqueNumbersSet.contains(uniqueRandomNum)); // Ensure uniqueness

            uniqueNumbersSet.add(uniqueRandomNum); // Add to the set to ensure uniqueness
            uniqueNumbersList.add(uniqueRandomNum); // Save the unique number to the list
        }

        return uniqueNumbersList;
    }


}





