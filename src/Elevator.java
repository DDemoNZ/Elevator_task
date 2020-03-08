import java.util.Arrays;

public class Elevator {

    private static final int MAX_CAPACITY = 5;
    private static int capacity = 0;
    private static int state = 0;
    private static int[] people = new int[MAX_CAPACITY];

    public static int getMaxCapacity() {
        return MAX_CAPACITY;
    }

    public static int getCapacity() {
        return capacity;
    }

    public static void setCapacity(int capacity) {
        Elevator.capacity = capacity;
    }

    public static int getState() {
        return state;
    }

    public static void setState(int state) {
        Elevator.state = state;
    }

    public static void addPeople(int[] elevator) {
        for (int i = 0; i < MAX_CAPACITY; i++) {
            if (people[i] == 0) people[i] = elevator[i];
        }
        Arrays.sort(people);
    }

    public static int[] getPeople() {
        return people;
    }
}
