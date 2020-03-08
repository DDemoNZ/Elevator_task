import java.util.Arrays;
import java.util.Collections;

public class App {

    private static int floorCounter = GetRandom.random(20, 5);
    private static int passengerOnTheFloor = GetRandom.random(10, 0);
    private static int[][] building = new int[floorCounter][passengerOnTheFloor];
    private static int[] temp;
    private static int peopleToUp = 0;
    private static int peopleToDown = 0;
    private static int freePlace;

    public static void main(String[] args) {

        System.out.println(floorCounter + " floors in the building.");
        System.out.println(passengerOnTheFloor + " peoples on the every floor.");
        setPassengersOnTheFloors();
        System.out.println();
        showStep();

        System.out.println("\nELEVATOR STARTED WORK.");
        System.out.println("_____________________________________________");
        int step = 1;
        while (checkPassenger(building)) {
            System.out.println("________________Step - " + step + "_______________________");
            passengerIn();
            elevatorMoving();
            passengerOut();
            showStep();
            step++;
            System.out.println("________________________________________________\n");
        }
        System.out.println("ELEVATOR ENDED WORK.");
    }

    public static void showStep() {
        for (int i = building.length - 1, n = building.length; i >= 0; i--, n--) {
            System.out.print("Floor " + n + " - ");
            for (int j = 0; j < building[i].length; j++) {
                System.out.printf("%4d", building[i][j]);
            }
            System.out.print(" | ");
            if (Elevator.getState() == 1 && Floor.getCurrentFloor() == n) {
                System.out.print("^");
                for (int b = 0; b < Elevator.getMaxCapacity(); b++) {
                    System.out.printf("%4d", temp[b]);
                }
            } else if (Elevator.getState() == 2 && Floor.getCurrentFloor() == n) {
                System.out.print("V");
                for (int b = 0; b < Elevator.getMaxCapacity(); b++) {
                    System.out.printf("%4d", temp[b]);
                }
            }
            System.out.println();
        }
    }

    public static void nextStep() {
        int[] elevator = new int[Elevator.getMaxCapacity()];
        int[] tempUp = new int[passengerOnTheFloor];
        int[] tempDown = new int[passengerOnTheFloor];
        getFreePlace();

        if (peopleToUp > 0 && Elevator.getState() == 1
                || Elevator.getState() == 0 && peopleToUp > 0) {
            for (int j = 0; j < passengerOnTheFloor; j++) {
                if (building[Floor.getCurrentFloor() - 1][j] >= Floor.getCurrentFloor()
                        && building[Floor.getCurrentFloor() - 1][j] > 0) {
                    tempUp[j] = building[Floor.getCurrentFloor() - 1][j];
                }
            }
            addPeopleToElevator(elevator, tempUp, peopleToUp);
        } else if (peopleToDown > 0 && Elevator.getState() == 2
                || Elevator.getState() == 0 && peopleToDown > 0) {
            for (int j = 0; j < passengerOnTheFloor; j++) {
                if (building[Floor.getCurrentFloor() - 1][j] < Floor.getCurrentFloor()
                        && building[Floor.getCurrentFloor() - 1][j] > 0) {
                    tempDown[j] = building[Floor.getCurrentFloor() - 1][j];
                }
            }
            addPeopleToElevator(elevator, tempDown, peopleToDown);
        }

        for (int j = 0; j < Elevator.getMaxCapacity(); j++) {
            for (int i = 0; i < building[Floor.getCurrentFloor() - 1].length; i++) {
                if (building[Floor.getCurrentFloor() - 1][i] != 0
                        && building[Floor.getCurrentFloor() - 1][i] == elevator[j]) {
                    building[Floor.getCurrentFloor() - 1][i] = 0;
                    break;
                }
            }
        }
        Elevator.addPeople(elevator);
        temp = Elevator.getPeople();
    }

    public static void setPassengersOnTheFloors() {
        for (int i = building.length - 1, n = building.length; i >= 0; i--, n--) {
            for (int j = 0; j < building[i].length; j++) {
                building[i][j] = Passenger.getFloor();
                if (building[i][j] == n) {
                    if (n <= floorCounter / 2) {
                        building[i][j] = GetRandom.random(floorCounter, n + 1);
                    } else {
                        building[i][j] = GetRandom.random(n - 1, 1);
                    }
                }
            }
        }
    }

    public static int getPeopleToUp(int floor) {
        int needUp = 0;
        for (int j = 0; j < building[floor - 1].length; j++) {
            if (building[floor - 1][j] >= floor && building[floor - 1][j] > 0) {
                needUp++;
            }
        }
        return needUp;
    }

    public static int getPeopleToDown(int floor) {
        int needDown = 0;
        for (int j = 0; j < building[floor - 1].length; j++) {
            if (building[floor - 1][j] < floor && building[floor - 1][j] > 0) {
                needDown++;
            }
        }
        return needDown;
    }

    public static void passengerIn() {
        if (Floor.getCurrentFloor() == 1) {
            Elevator.setState(1);
        } else if (Floor.getCurrentFloor() == floorCounter) {
            Elevator.setState(2);
        }

        if (passengerOnTheFloor == 0) {
            Elevator.setState(0);
            System.exit(1);
        } else if (passengerOnTheFloor > 0) {
            peopleToUp = getPeopleToUp(Floor.getCurrentFloor());
            peopleToDown = getPeopleToDown(Floor.getCurrentFloor());
            nextStep();
        }
    }

    private static void addPeopleToElevator(int[] elevator, int[] peopleDirection, int people) {
        peopleDirection = sort(peopleDirection);
        if (people >= freePlace) {
            for (int j = 0; j < freePlace; j++) {
                if (peopleDirection[j] > 0) {
                    elevator[j] = peopleDirection[j];
                }
            }
            Elevator.setCapacity(Elevator.getMaxCapacity());
        } else {
            for (int j = 0; j < people; j++) {
                if (peopleDirection[j] > 0) {
                    elevator[j] = peopleDirection[j];
                }
            }
            Elevator.setCapacity(Elevator.getCapacity() + people);
        }
    }

    private static int getMinFloor() {
        int minNeededFloor = floorCounter + 1;
        for (int j = 0; j < Elevator.getPeople().length; j++) {
            if (Elevator.getPeople()[j] != 0) {
                minNeededFloor = Math.min(minNeededFloor, Elevator.getPeople()[j]);
            }
        }
        return minNeededFloor;
    }

    private static int getMaxFloor() {
        int maxNeededFloor = -1;
        for (int j = 0; j < Elevator.getPeople().length; j++) {
            if (Elevator.getPeople()[j] != 0) {
                maxNeededFloor = Math.max(maxNeededFloor, Elevator.getPeople()[j]);
            }
        }
        return maxNeededFloor;
    }

    private static void getFreePlace() {
        freePlace = Elevator.getMaxCapacity() - Elevator.getCapacity();
    }

    private static void elevatorMoving() {
        int max = getMaxFloor();
        int min = getMinFloor();
        int floorUp = Floor.getCurrentFloor() + 1;
        int floorDown = Floor.getCurrentFloor() - 1;
        getFreePlace();

        if (Elevator.getState() == 1) {
            if (freePlace == 0) {
                Floor.setCurrentFloor(min);
            } else {
                int nextFloor = 0;
                for (int i = floorUp, k = floorCounter; i <= floorCounter; i++, k--) {
                    if (Floor.getDirection(i)) {
                        nextFloor = i;
                        break;
                    } else {
                        if (min != floorCounter + 1) {
                            nextFloor = min;
                            break;
                        } else {
                            if (!Floor.getDirection(k)) {
                                nextFloor = k;
                                break;
                            } else {
                                Elevator.setState(2);
                            }
                        }
                    }
                }
                Floor.setCurrentFloor(nextFloor);
            }

        } else if (Elevator.getState() == 2) {
            if (freePlace == 0) {
                Floor.setCurrentFloor(max);
            } else {
                int nextFloor = 0;
                for (int k = floorDown, i = 1; k >= 1; k--, i++) {
                    if (!Floor.getDirection(k)) {
                        nextFloor = k;
                        break;
                    } else {
                        if (max != -1) {
                            nextFloor = max;
                            break;
                        } else {
                            if (Floor.getDirection(i)) {
                                nextFloor = i;
                                break;
                            } else {
                                Elevator.setState(1);
                                nextFloor = 1;
                            }
                        }
                    }
                }
                Floor.setCurrentFloor(nextFloor);
            }
        }
    }

    private static int[] sort(int[] a) {
        return Arrays.stream(a).boxed().sorted(Collections.reverseOrder())
                .mapToInt(Integer::intValue)
                .toArray();
    }

    private static void passengerOut() {
        int count = 0;
        for (int j = 0; j < Elevator.getMaxCapacity(); j++) {
            if (Elevator.getPeople()[j] == Floor.getCurrentFloor()) {
                count++;
                Elevator.getPeople()[j] = 0;
            }
        }
        Elevator.setCapacity(Elevator.getCapacity() - count);
    }

    private static boolean checkPassenger(int[][] numbers) {
        int count = 0;
        for (int[] number : numbers) {
            for (int i : number) {
                if (i == 0) {
                    count++;
                }
            }
        }
        return count != passengerOnTheFloor * floorCounter;
    }

    public static int getFloorCounter() {
        return floorCounter;
    }
}
