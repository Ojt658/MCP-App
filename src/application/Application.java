package application;

import com.google.gson.*;
import com.google.gson.annotations.Expose;

import carpark.*;
import vehicle.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Application class to hold the main method, the command line menu system and the program logic
 *
 * @author Ollie
 * @version 1.0
 */
public class Application {

    private final String carParkFile = "MCP.json";
    @Expose private CarPark carPark;
    private Scanner scan;
    @Expose private ArrayList<Receipt> receipts;
    @Expose private ArrayList<Token> tokens;
    private static final double hourInMillis = 3600000.0;
    @Expose private ArrayList<Attendant> attendants;

    private Application() {
        carPark = new CarPark();
        scan = new Scanner(System.in);
        receipts = new ArrayList<>();
        tokens = new ArrayList<>();
        attendants = new ArrayList<>();
    }

    private void printMenu() {
        System.out.println("**Menu**");
        System.out.println("1: Register your car to park");
        System.out.println("2: Enter receipt code and pay");
        System.out.println("3: Show availability in the car-park");
        System.out.println("4: Ask for attendant");
        System.out.println("5: Raise barrier");
        System.out.println("6: Close application");
        System.out.println("\nAttendant options");
        System.out.println("7: Edit attendant pool");
        System.out.println("8: Change busy attendant");
        System.out.print("What would you like to do? ");
    }

    private void runMenu() {
        String input;
        do {
            printMenu();
            input = scan.nextLine();
            switch (input) {
                case "1":
                    System.out.println("Register Car");
                    registerVehicle(getVehicleType());
                    break;
                case "2":
                    System.out.println("Enter receipt");
                    enterCode();
                    break;
                case "3":
                    System.out.println("Show CP");
                    showCP();
                    break;
                case "4":
                    System.out.println("Ask for attendant");
                    askForAttendant();
                    break;
                case "5":
                    System.out.println("Raise barrier");
                    raiseBarrier();
                    break;
                case "6":
                    System.out.println("Quit");
                    break;
                case "7":
                    System.out.println("Edit attendant pool");
                    editAttendants();
                    break;
                case "8":
                    changeBusy();
                    break;
                default:
                    System.err.println("Invalid entry");
            }
        } while (!(input.equals("6")));
    }

    private void vehicleRestrictions() {
        System.out.println("Vehicle Types:\n" +
                "  S - Standard sized (<2m tall, <5m long)\n" +
                "  L - Long vehicle (<3m tall, 5.1m - 6m long )\n" +
                "  T - Tall vehicle (<5m long, 2m - 3m tall)\n" +
                "  M - Motorbike\n" +
                "  C - Coach (<=15m long)");
    }

    private int registerVehicle(Vehicle vehicle) {
        int code = -1;
        vehicle = setLicense(vehicle);
        int zone = vehicle.getZone(carPark.getZones()); // Get available zone for vehicle
        if (zone != 0) {
            int space = carPark.addVehicle(vehicle, zone); //Get available space
            if (space != 0) { // If there is an available space
                 // Display where the car needs to be parked
                addLocation(zone, space, giveReceipt(vehicle.getType()));
                code = receipts.size();
            } else{
                System.err.println("We are sorry, but there are no available spaces.");
            }
        } else {
            System.err.println("We are sorry, but there are no available spaces.");
        }
        return code;
    }

    private Vehicle setLicense(Vehicle v) {
        String reg;
        System.out.print("What is the vehicle registration number?  ");
        reg = scan.nextLine().toUpperCase();
        v.setLicense(reg);
        return v;
    }

    private Vehicle getVehicleType() {
        String type;
        boolean incorrect;
        Vehicle v;
        do {
            vehicleRestrictions();
            incorrect = false;
            System.out.print("What type of vehicle do you have?  ");
            type = scan.nextLine().toUpperCase();
            switch(type) {
                case "S":
                    System.out.println("Standard sized");
                    v = new StandardVehicle();
                    break;
                case "L":
                    System.out.println("Long vehicle");
                    v = new LongVehicle();
                    break;
                case "T":
                    System.out.println("Tall vehicle");
                    v = new TallVehicle();
                    break;
                case "M":
                    System.out.println("Motorbike");
                    v = new Motorbike();
                    break;
                case "C":
                    System.out.println("Coach");
                    v = new Coach();
                    break;
                default:
                    System.err.println("Incorrect type");
                    v = null;
                    incorrect = true;
                    break;
            }
        } while (incorrect);
        return v;
    }

    private int giveReceipt(String type) {
        Receipt receipt = new Receipt(receipts.size(), type);
        System.out.println(receipt);
        receipts.add(receipt);
        System.out.println();
        return receipt.getCode();
    }

    private void addLocation(int zone, int space, int code) {
        System.out.println("\nPlease park in zone " + zone + " space " + space);
        String location = "Z" + zone + "S" + space;
        receipts.get(code).setLocation(location);
    }

    private int getCode () {
        int code = -1;
        boolean correct;
        do {
            try {
                System.out.println("Please enter your receipt code. ");
                code = scan.nextInt();
                correct = true;
            } catch (InputMismatchException e) {
                System.err.println("Please enter a number");
                correct = false;
            } finally {
                scan.nextLine();
            }
        } while (!correct);
        return code;
    }

    private boolean codeFound(int code) {
        boolean found = false;
        for (Receipt r : receipts) {
            if (r.getCode() == code) {
                System.out.println("Your car is in " + r.getLocation());
                getMoneyDue(r);
                receipts.remove(r);
                found = true;
                break;
            }
        }

        return found;
    }

    private int enterCode() {
        boolean tryAgain;
        int code;
        do {
            code = getCode();
            tryAgain = false;
            if (!codeFound(code)) {
                tryAgain = codeNotFound();
            }
        } while (tryAgain);
        return code;
    }

    private boolean codeNotFound() {
        System.err.println("Receipt code not found");
        return tryAgain();
    }


    private float getRate(Receipt receipt) {
        float rate = 0;
        String priceFile = "prices.json";
        try (FileReader fr = new FileReader(priceFile);
             BufferedReader br = new BufferedReader(fr)){
            // Get unit prices for the zones
            JsonParser parser = new JsonParser();
            JsonObject prices = parser.parse(br).getAsJsonObject(); // Get the prices
            rate = prices.get(receipt.splitLocation()[1]).getAsFloat(); // Get the price for the zone
        } catch (FileNotFoundException e) {
            System.err.println("File not found. Make sure you have the 'prices.json' file");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rate;
    }

    private void getMoneyDue(Receipt receipt) {
        // Get time difference
        long timeDifferenceInMillis = getTimeDifference(receipt);
        double hourDifference = Math.ceil(timeDifferenceInMillis / hourInMillis); // Get time in hours and round up
        // Remove car from car park
        carPark.removeVehicle(Integer.parseInt(receipt.splitLocation()[1]), Integer.parseInt(receipt.splitLocation()[2])); // Get ints from the location string and remove the vehicle from that space
        float rate = getRate(receipt);
        float moneyDue = rate*(int)hourDifference;
        if (!receipt.getType().equals("vehicle.Coach")) { // Coaches don't get discount
            moneyDue = checkDisabledBadge(moneyDue);
        }
        System.out.println("You have been here for: " + getTime(timeDifferenceInMillis));
        System.out.println(moneyDue + " units due");
        // Pay machine
        pay(moneyDue);
    }

    private long getTimeDifference(Token token) {
        Calendar initialTime = token.getTimeGiven();
        Calendar currentTime = Calendar.getInstance();
        return currentTime.getTimeInMillis() - initialTime.getTimeInMillis();
    }

    private String getTime(long timeInMillis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(timeInMillis), // Get Hours from the millis
                TimeUnit.MILLISECONDS.toMinutes(timeInMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMillis)), // get Minutes
                TimeUnit.MILLISECONDS.toSeconds(timeInMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMillis)));
    }

    private void pay(float moneyDue) {
        System.out.println("Please pay the amount due");
        boolean incorrect;
        if (moneyDue != 0) {
            do {
                incorrect = false;
                try {
                    float amountGiven = scan.nextFloat();
                    if (amountGiven == moneyDue) {
                        System.out.println("Thank you");
                        incorrect = false;
                    } else if (amountGiven < moneyDue) {
                        moneyDue -= amountGiven; // Calculate new money due
                        incorrect = true;
                        System.out.println("Amount still left: " + moneyDue + " units");
                    } else if (amountGiven > moneyDue) {
                        amountGiven -= moneyDue; // Calculate change
                        System.out.println("Thank you, here is your change: " + amountGiven + " units");
                        incorrect = false;
                    }
                } catch (InputMismatchException e) {
                    System.err.println("Please enter a number");
                    incorrect = true;
                } finally {
                    scan.nextLine();
                }
            } while (incorrect);

        }
        giveToken();

    }

    private void giveToken () {
        Token token = new Token(tokens.size());
        tokens.add(token);
        System.out.println("Here is your " + token + "\nYou have 15 minutes to get your vehicle and use the barrier");
    }

    private float checkDisabledBadge(float amountDue) {
        String blueBadge;
        do {
            System.out.println("Are you a blue badge holder? (Y/N)  ");
            blueBadge = scan.nextLine().toUpperCase();
            switch (blueBadge) {
                case "Y":
                    if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        amountDue = 0; // It is free for BBH on sunday
                    } else {
                        amountDue /= 2; // Half price on other days
                    }
                    break;
                case "N":
                    break;
                default:
                    System.err.println("Pleas type y or n");
                    break;
            }
        } while (!(blueBadge.equals("Y")||blueBadge.equals("N")));
        return amountDue;
    }

    private void raiseBarrier () {
        boolean again;
        do {
            again = false;
            try {
                System.out.println("Please enter your token code");
                int code = scan.nextInt();
                scan.nextLine();
                Token token = null;
                for (Token t : tokens) {
                    if (code == t.getCode()) {
                        token = t;
                        break;
                    }
                }
                if (token != null) { // If token was found
                    if (checkTime(token)) { // Check within the time limit
                        System.out.println("The barrier is open, have a nice day!");
                    } else {
                        System.err.println("The barrier has not opened as it has been longer than 15 minutes.\n Please seek assistance.");
                    }
                    tokens.remove(token);
                } else {
                    System.err.println("We don't recognise that code. Please check and try again.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Please enter a number");
                scan.nextLine();
                again = tryAgain();
            }

        } while (again);
    }

    private boolean tryAgain () {
        boolean again = false;
        System.out.println("Would you like to try again? Y / N ");
        String check;
        do {
            check = scan.nextLine().toUpperCase();
            switch (check) {
                case "Y":
                    again = true;
                    break;
                case "N":
                    break;
                default:
                    System.err.println("Please write Y or N");
                    break;
            }
        } while (!(check.equals("Y") || check.equals("N")));
        return again;
    }

    private boolean checkTime (Token token) {
        boolean result = false;
        long timeDifference = getTimeDifference(token);
        int barrierLimit = 900000; // 900000 is 15 mins in millis
        if (timeDifference <= barrierLimit) { // If within limit
            result = true;
        }
        return result;
    }

    private Attendant attendantsNotBusy() {
        Attendant result = null;
        for (Attendant a: attendants) {
            if (!a.isBusy()) {
                result = a;
                break;
            }
        }
        return result;
    }

    private void askForAttendant () {
        Attendant attendant = attendantsNotBusy();
        if (attendant != null) {
            System.out.println("Your attendant is " + attendant.getName());
            System.out.println("Would you like to Drop off or Pick up your vehicle? (D/P) Coaches and Motorbikes are not allowed to use this service");
            String input;
            do {
                input = scan.nextLine().toUpperCase();
                switch (input) {
                    case "D":
                        dropOff(attendant);
                        break;
                    case "P":
                        pickUp(attendant);
                        break;
                    default:
                        System.err.println("Incorrect entry. Please input D or P");
                        break;
                }
            } while (!(input.equals("D") || input.equals("P")));
        } else {
            noAttendant();
        }
    }

    private void noAttendant() {
        System.err.println("Unfortunately, there are no free attendants. Please park / collect your own car");
    }

    private void pickUp(Attendant attendant) {
        System.out.println("Please use the program to pay and receive your token. Then give it to an attendant to retrieve your vehicle.");
        System.out.println("## Enter code ##");
        Attendant a = getAttendant(enterCode());
        if (a != null) {
            attendant = a;
        }
        attendant.setBusy(false);
        if (attendant.getCode() != -1) { //So it doesn't run if the code isn't found
            attendant.setCode(-1);
            System.out.println(attendant.getName() + " will now bring your vehicle so you can leave by using the raise barrier function.");
        }
    }

    private Attendant getAttendant(int code) {
        Attendant a = null;
        for (Attendant attendant: attendants) {
            if (code == attendant.getCode()){
                a = attendant;
                break;
            }
        }
        return a;
    }

    private void dropOff(Attendant attendant) {
        Vehicle vehicle = getVehicleType();
        if (vehicleCheck(vehicle)) { // If not coach or motorbike
            System.out.println(attendant.getName() + " will now come register and park your vehicle for you.");
            attendant.setBusy(true);
            System.out.println("Please wait for your receipt");
            System.out.println("## For Attendant Use ##"); //Switch to attendant use
            System.out.println("Would you like to use app or roam to find a space? R / A");
            String choice = scan.nextLine().toUpperCase();
            switch (choice) {
                case "A":
                    attendant.setCode(registerVehicle(vehicle));
                    System.out.println("Here is your receipt, have a nice day. ");
                    break;
                case "R":
                    attendant.setCode(roam(vehicle));
                    break;
                default:
                    System.err.println("Please type A for app or R for roam.");
                    break;
            }

        } else {
            System.err.println("Coaches and motorbikes cannot be parked by an attendant. Please use the system yourself.");
        }
    }

    private int roam(Vehicle vehicle) {
        int code = receipts.size();
        giveReceipt(vehicle.getType());
        System.out.println("Give receipt to customer. ");
        System.out.println("\n##Drive to find a space##");
        boolean again = false;
        do {
            try {
                again = registerSpace(vehicle, code);
            } catch (InputMismatchException e) {
                System.err.println("Please yous numbers for zones and spaces");
                again = true;
            }
        } while(again);
        return code;
    }

    private boolean registerSpace(Vehicle vehicle, int code) throws InputMismatchException{
        boolean result = false;
        System.out.println("Which zone number is the space in? ");
        int zone = scan.nextInt();
        System.out.println("What space number are you registering? ");
        int space = scan.nextInt();
        scan.nextLine();
        boolean available = carPark.addVehicle(setLicense(vehicle), zone, space);
        if (available) {
            addLocation(zone, space, code);
            System.out.println("The space is successfully registered!");
        } else {
            System.err.println("That space is taken. Please try another");
            result = true;
        }
        return result;
    }

    private boolean vehicleCheck(Vehicle v) {
        boolean check = true;
        if (v instanceof Motorbike || v instanceof Coach) {
            check = false;
        }
        return check;
    }

    private String getAttendantName() {
        System.out.println("What is the attendants name? ");
        return scan.nextLine().toUpperCase();
    }

    private void editAttendants() {
        System.out.println("Add or remove an attendant from the pool? (A or R)");
        String choice;
        do {
            choice = scan.nextLine().toUpperCase();
            switch (choice) {
                case "A":
                    attendants.add(new Attendant(getAttendantName()));
                    break;
                case "R":
                    removeAttendant();
                    break;
                default:
                    System.out.println("Please write A or R");
                    break;
            }
        } while (!(choice.equals("A")||choice.equals("R")));
    }

    private void removeAttendant() {
        Attendant attendant = searchAttendants();
        if (attendant != null) {
            attendants.remove(attendant);
        }
    }

    private void changeBusy () {
        Attendant attendant = searchAttendants();
        if (attendant != null) {
            System.out.println("Change busy to true or false? T / F ");
            String choice = scan.nextLine().toUpperCase();
            boolean busy = false;
            switch (choice) {
                case "T":
                    busy = true;
                    break;
                case "F":
                    busy = false;
                    break;
                default:
                    System.err.println("Incorrect entry.");
            }
            attendant.setBusy(busy);
        }
    }

    private Attendant searchAttendants() {
        Attendant a = null;
        String name = getAttendantName();
        boolean found = false;
        for (Attendant attendant: attendants) {
            if (attendant.getName().equals(name)) {
                a = attendant;
                found = true;
                break;
            }
        }
        if (!found) {
            System.err.println("That attendant wasn't found.");
        }
        return a;
    }

    private void save() {
        try (FileWriter fw = new FileWriter(carParkFile);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter outfile = new PrintWriter(bw)) {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            outfile.print(gson.toJson(this)); // Print app data to json file
        } catch (FileNotFoundException e) {
            System.err.println("File not found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Application load(Application app) {
        try (FileReader fr = new FileReader(carParkFile)) {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Vehicle.class, new JsonDeserialization<Vehicle>()).create();
            app = gson.fromJson(fr, Application.class); //Load app data from json file
        } catch (FileNotFoundException e) {
            System.err.println("File not found. Make sure you have the file 'MCP.json'");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return app;
    }

    private void showCP() {
        for (Zone zone: carPark.getZones()) {
            System.out.println(zone); //Print each zone
        }
    }

    // /////////////////////////////////////
    public static void main(String[] args) {
        Application app = new Application();
        app = app.load(app);
        System.out.println("Welcome to the car-parking app!");
        app.runMenu();
        app.save();
        System.out.println("Thank you for using the parking app");
    }
}
