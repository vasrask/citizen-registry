package gr.aegean.client;

import gr.aegean.domain.Citizen;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ClientApp {

    private static final String BASE_URL =
            "http://localhost:8080/rest-1.0-SNAPSHOT/api/citizens";

    private final Client client;

    public ClientApp() {
        this.client = ClientBuilder.newClient();
    }

    public static void main(String[] args) {
        ClientApp app = new ClientApp();
        app.runMenu();
    }

    private void runMenu() {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== CITIZEN REGISTRY CLIENT =====");
            System.out.println("1. Get all citizens");
            System.out.println("2. Get citizen by registry number");
            System.out.println("3. Create citizen");
            System.out.println("4. Update citizen");
            System.out.println("5. Delete citizen");
            System.out.println("Any other input: Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    getAll();
                    break;
                case "2":
                    getOne(scanner);
                    break;
                case "3":
                    create(scanner);
                    break;
                case "4":
                    update(scanner);
                    break;
                case "5":
                    delete(scanner);
                    break;
                default:
                    System.out.println("Exiting...");
                    return;
            }
        }
    }

    private void getAll() {
        Response response = client
                .target(BASE_URL)
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == 200) {
            Citizen[] citizens = response.readEntity(Citizen[].class);
            List<Citizen> citizen_list = Arrays.asList(citizens);
            //list.forEach(System.out::println);
            for (Citizen c : citizen_list) {
                System.out.println(c);
            }
        } else {
            System.out.println("Error: " + response.getStatus());
        }
    }

    private void getOne(Scanner sc) {
        System.out.print("Enter Registry Number: ");
        String registryNumber = sc.nextLine();

        Response response = client
                .target(BASE_URL)
                .queryParam("registryNumber", registryNumber)
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == 200) {
            Citizen c = response.readEntity(Citizen.class);
            System.out.println(c);
        } else {
            System.out.println("Not found");
        }
    }

    private void create(Scanner sc) {
        Citizen c = new Citizen();

        System.out.print("First Name: ");
        c.setFirstName(sc.nextLine());

        System.out.print("Middle Name: ");
        c.setMiddleName(sc.nextLine());

        System.out.print("Last Name: ");
        c.setLastName(sc.nextLine());

        System.out.print("Date of Birth: ");
        c.setDateOfBirth(sc.nextLine());

        System.out.print("Place of Birth: ");
        c.setPlaceOfBirth(sc.nextLine());

        Response response = client
                .target(BASE_URL)
                .request()
                .post(Entity.json(c));

        Citizen created = response.readEntity(Citizen.class);

        System.out.println("Status: " + response.getStatus());
        System.out.println(created.getRegistryNumber());
    }

    private void update(Scanner sc) {
        System.out.print("Registry number of citizen to update: ");
        String registryNumber = sc.nextLine();

        Response getResp = client.target(BASE_URL + "/" + registryNumber)
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (getResp.getStatus() != 200) {
            System.out.println("Citizen not found!");
            return;
        }

        Citizen existing = getResp.readEntity(Citizen.class);

        System.out.print("First Name [" + existing.getFirstName() + "]: ");
        String first = sc.nextLine();
        if (!first.isBlank()) existing.setFirstName(first);

        System.out.print("Middle Name [" + existing.getMiddleName() + "]: ");
        String middle = sc.nextLine();
        if (!middle.isBlank()) existing.setMiddleName(middle);

        System.out.print("Last Name [" + existing.getLastName() + "]: ");
        String last = sc.nextLine();
        if (!last.isBlank()) existing.setLastName(last);

        System.out.print("Date of Birth [" + existing.getDateOfBirth() + "]: ");
        String birthDate = sc.nextLine();
        if (!birthDate.isBlank()) existing.setDateOfBirth(birthDate);

        System.out.print("Place of Birth [" + existing.getPlaceOfBirth() + "]: ");
        String birthPlace = sc.nextLine();
        if (!birthPlace.isBlank()) existing.setPlaceOfBirth(birthPlace);

        Response putResp = client.target(BASE_URL + "/" + registryNumber)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(existing));

        if (putResp.getStatus() == 200) {
            System.out.println("Updated citizen: " + putResp.readEntity(Citizen.class));
        } else {
            System.out.println("Error: " + putResp.getStatus());
        }
    }

    private void delete(Scanner sc) {
        System.out.print("Registry number: ");
        String registryNumber = sc.nextLine();

        Response response = client
                .target(BASE_URL + "/" + registryNumber)
                .request()
                .delete();

        System.out.println("Status: " + response.getStatus());
    }
}
