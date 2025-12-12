package gr.aegean.domain;

import java.io.Serializable;
import java.util.Random;

public class Citizen implements Serializable {
    private String registryNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String dateOfBirth;
    private String placeOfBirth;
    private static final Random random = new Random();

    public Citizen() {}

    public Citizen(String registryNumber, String firstName, String middleName, String lastName, String dateOfBirth, String placeOfBirth) {
        generateRegistryNumber();
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.placeOfBirth = placeOfBirth;
    }

    public String getRegistryNumber() {
        return registryNumber;
    }

    public void setRegistryNumber(String registryNumber) {
        this.registryNumber = registryNumber;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getFullName() {
        return getFirstName() + ' ' + getMiddleName() + ' ' + getLastName();
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }


    public void generateRegistryNumber() {
        char letter = (char) ('A' + random.nextInt(26));
        int number = 10000000 + random.nextInt(90000000);
        this.registryNumber = letter + String.valueOf(number);
    }
    @Override
    public String toString() {
        return "Citizen { " +
                "registry number='" + registryNumber + '\'' +
                ", full name='" + this.getFullName() + '\'' +
                ", date of birth='" + dateOfBirth + '\'' +
                ", place of birth='" + placeOfBirth + '\'' +
                " }";
    }
}
