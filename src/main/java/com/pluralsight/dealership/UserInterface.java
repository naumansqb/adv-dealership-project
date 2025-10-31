package com.pluralsight.dealership;

import java.time.Year;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserInterface {

    private Dealership dealership;
    private Scanner scanner;

    public UserInterface() {
        scanner = new Scanner(System.in);
    }

    public void display() {
        init();
        boolean quit = false;
        while (!quit) {
            System.out.println("---------- Menu ----------");
            System.out.println("1. Get vehicles by price");
            System.out.println("2. Get vehicles by make and model");
            System.out.println("3. Get vehicles by year");
            System.out.println("4. Get vehicles by color");
            System.out.println("5. Get vehicles by mileage");
            System.out.println("6. Get vehicles by type");
            System.out.println("7. Get all vehicles");
            System.out.println("8. Add vehicle");
            System.out.println("9. Remove vehicle");
            System.out.println("10. Sell or Lease a vehicle");
            System.out.println("99. Quit");

            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    processGetByPriceRequest();
                    break;
                case "2":
                    processGetByMakeModelRequest();
                    break;
                case "3":
                    processGetByYearRequest();
                    break;
                case "4":
                    processGetByColorRequest();
                    break;
                case "5":
                    processGetByMileageRequest();
                    break;
                case "6":
                    processGetByVehicleTypeRequest();
                    break;
                case "7":
                    processGetAllVehiclesRequest();
                    break;
                case "8":
                    processAddVehicleRequest();
                    break;
                case "9":
                    processRemoveVehicleRequest();
                    break;
                case "10":
                    processSellOrLease();
                    break;
                case "99":
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void processGetByPriceRequest() {
        System.out.print("Enter minimum price: ");
        double min = scanner.nextDouble();
        System.out.print("Enter maximum price: ");
        double max = scanner.nextDouble();
        List<Vehicle> vehicles = dealership.getVehiclesByPrice(min, max);
        displayVehicles(vehicles);
    }

    public void processGetByMakeModelRequest() {
        System.out.print("Enter make: ");
        String make = scanner.nextLine();
        System.out.print("Enter model: ");
        String model = scanner.nextLine();
        List<Vehicle> vehicles = dealership.getVehiclesByMakeModel(make, model);
        displayVehicles(vehicles);
    }

    public void processGetByYearRequest() {
        System.out.print("Enter minimum year: ");
        int min = scanner.nextInt();
        System.out.print("Enter maximum year: ");
        int max = scanner.nextInt();
        List<Vehicle> vehicles = dealership.getVehiclesByYear(min, max);
        displayVehicles(vehicles);
    }

    public void processGetByColorRequest() {
        System.out.print("Enter color: ");
        String color = scanner.nextLine();
        List<Vehicle> vehicles = dealership.getVehiclesByColor(color);
        displayVehicles(vehicles);
    }

    public void processGetByMileageRequest() {
        System.out.print("Enter minimum mileage: ");
        int min = scanner.nextInt();
        System.out.print("Enter maximum mileage: ");
        int max = scanner.nextInt();
        List<Vehicle> vehicles = dealership.getVehiclesByMileage(min, max);
        displayVehicles(vehicles);
    }

    public void processGetByVehicleTypeRequest() {
        System.out.print("Enter vehicle type: ");
        String vehicleType = scanner.nextLine();
        List<Vehicle> vehicles = dealership.getVehiclesByType(vehicleType);
        displayVehicles(vehicles);
    }

    public void processGetAllVehiclesRequest() {
        List<Vehicle> vehicles = dealership.getAllVehicles();
        displayVehicles(vehicles);
    }

    public void processAddVehicleRequest() {
        System.out.print("Enter vehicle vin: ");
        int vin = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter vehicle make: ");
        String make = scanner.nextLine();

        System.out.print("Enter vehicle model: ");
        String model = scanner.nextLine();

        System.out.print("Enter vehicle year: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter vehicle price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter vehicle color: ");
        String color = scanner.nextLine();

        System.out.print("Enter vehicle mileage: ");
        int mileage = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter vehicle type (Car, Truck, SUV, Motorcycle): ");
        String type = scanner.nextLine();

        Vehicle vehicle = new Vehicle(vin, year, make, model, type, color, mileage, price);

        dealership.addVehicle(vehicle);
        System.out.println("Vehicle added successfully!");
        DealershipFileManager manager = new DealershipFileManager();
        manager.saveDealership(dealership);
    }

    public void processRemoveVehicleRequest() {
        System.out.print("Enter the VIN of the vehicle you wish to remove: ");
        int vin = scanner.nextInt();

        boolean vehicleRemoved = false;
        for (Vehicle vehicle : dealership.getAllVehicles()) {
            if (vehicle.getVin() == vin) {
                dealership.removeVehicle(vehicle);
                System.out.println("Vehicle removed successfully!");
                vehicleRemoved = true;
                break;
            }
        }

        if (!vehicleRemoved) {
            System.out.println("Vehicle not found. Please try again.");
            return;
        }

        DealershipFileManager manager = new DealershipFileManager();
        manager.saveDealership(dealership);
    }

    private void init() {
        DealershipFileManager manager = new DealershipFileManager();
        dealership = manager.getDealership();
    }

    private void displayVehicles(List<Vehicle> vehicles) {
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle.toString());
        }
    }


    private void processSellOrLease(){
        displayVehicles(dealership.getAllVehicles());

        Vehicle vehicle = null;
        int vin = -1;

        do {
            try {
                System.out.println("Please enter the vin of the vehicle you'd like to Sell/Lease: ");
                vin = scanner.nextInt();
                scanner.nextLine();

                vehicle = dealership.getVehicleByVin(vin);

                if (vehicle == null) {
                    System.out.println("Vehicle not found. Please try again.");
                    vin = -1;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid Entry. Please enter a valid VIN.");
                scanner.nextLine();
                vin = -1;
            }
        } while (vehicle == null);

        System.out.println("Enter contract date (YYYYMMDD): ");
        String date = scanner.nextLine().trim();

        System.out.println("Enter customer name: ");
        String customerName = scanner.nextLine().trim();

        System.out.println("Enter customer email: ");
        String customerEmail = scanner.nextLine().trim();

        System.out.println("Is this Sale or Lease? ");
        String type = scanner.nextLine().trim();

        Contract contract = null;

        if (type.equalsIgnoreCase("Lease")) {
            int currentYear = Year.now().getValue();
            int vehicleAge = currentYear - vehicle.getYear();

            if (vehicleAge > 3) {
                System.out.println("ERROR: Cannot lease a vehicle over 3 years old.");
                return;
            }

            contract = new LeaseContract(date, customerName, customerEmail, vehicle);


        } else if (type.equalsIgnoreCase("Sale")) {
            double salesTax = vehicle.getPrice() * 0.05;
            double recordingFee = 100.00;
            double processingFee = (vehicle.getPrice() < 10000) ? 295.00 : 495.00;

            System.out.println("Would you like to finance? (Y/N): ");
            String financeChoice = scanner.nextLine().trim().toUpperCase();
            boolean isFinance = financeChoice.equals("Y");

            contract = new SalesContract(date, customerName, customerEmail, vehicle,
                    salesTax, recordingFee, processingFee, isFinance);
            System.out.println("Sales contract created!");

        } else {
            System.out.println("Invalid option. Returning to menu.");
            return;
        }

        ContractFileManager contractManager = new ContractFileManager();
        contractManager.saveContract(contract);

        dealership.removeVehicle(vehicle);

        DealershipFileManager df = new DealershipFileManager();
        df.saveDealership(dealership);
        System.out.println("=".repeat(80));
        System.out.println("\nContract saved successfully!");
        System.out.println("=".repeat(80));
    }

}