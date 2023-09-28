package mockdata.peoplesoft;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mockdata.peoplesoft.model.Person;
import mockdata.peoplesoft.model.Role;

public class UserGenerator {

    private Long id = 1221000000L;
    private Map<String, Integer> asuriteMap = new HashMap<>();
    private RandomAccessFile maleName;
    private RandomAccessFile femaleName;
    private RandomAccessFile surname;

    public UserGenerator() {
        try {
            maleName = new RandomAccessFile("male_names.txt", "r");
            femaleName = new RandomAccessFile("female_names.txt", "r");
            surname = new RandomAccessFile("surnames.txt", "r");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new person with a unique 10-digit ASU ID and ASURITE
     * @return Person if generated successfully; null otherwise
     */
    public Person nextPerson(Role role, String position) {

        // generate name
        String nextName;
        try {
            nextName = nextName(maleName, femaleName, surname);
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }

        // generate and store asurite
        String[] nameTokens = nextName.toLowerCase().split(" ");
        String asurite = nameTokens[0].charAt(0) + nameTokens[1];
        if (asuriteMap.containsKey(asurite)) {
            asuriteMap.put(asurite, asuriteMap.get(asurite) + 1);
            asurite += asuriteMap.get(asurite);
        } else {
            asuriteMap.put(asurite, 0);
        }

        Person p = new Person(
                Long.toString(id++),
                asurite,
                nextName,
                List.of(role),
                position);
        // System.out.printf("%s (%s)%n", p.name(), p.asurite());

        return p;
    }

    private static String nextName(RandomAccessFile maleName, RandomAccessFile femaleName, RandomAccessFile surname)
            throws IOException {
        RandomAccessFile firstName = Math.random() < 0.5 ? maleName : femaleName;
        firstName.seek((long) (Math.random() * firstName.length()));
        while ((char) firstName.readByte() != '\n') {
            if (firstName.getFilePointer() - 2 < 0) {
                firstName.seek(0);
                break;
            }
            firstName.seek(firstName.getFilePointer() - 2);
        }
        String firstNameStr = firstName.readLine();
        firstNameStr = firstNameStr.charAt(0) + firstNameStr.substring(1).toLowerCase();

        surname.seek((long) (Math.random() * surname.length()));
        while ((char) surname.readByte() != '\n') {
            if (surname.getFilePointer() - 2 < 0) {
                surname.seek(0);
                break;
            }
            surname.seek(surname.getFilePointer() - 2);
        }
        String surnameStr = surname.readLine();
        surnameStr = surnameStr.charAt(0) + surnameStr.substring(1).toLowerCase();

        return firstNameStr + " " + surnameStr;
    }
}
