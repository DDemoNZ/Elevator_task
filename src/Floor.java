public class Floor {

    private static int currentFloor = 1;
    private static boolean direction;

    public static int getCurrentFloor() {
        return currentFloor;
    }

    public static void setCurrentFloor(int currentFloor) {
        Floor.currentFloor = currentFloor;
    }

    public static boolean getDirection(int floor) {

        if (App.getPeopleToUp(floor) > 0 && Elevator.getState() == 1 ||
                Floor.getCurrentFloor() == 1) setDirection(true);
        else if (App.getPeopleToUp(floor) > 0 && Elevator.getState() == 2 ||
                Floor.getCurrentFloor() == App.getFloorCounter()) setDirection(false);
        return direction;
    }

    public static void setDirection(boolean direction) {
        Floor.direction = direction;
    }
}
