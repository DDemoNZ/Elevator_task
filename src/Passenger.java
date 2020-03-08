public class Passenger {

    private static int floor;

    public static int getFloor() {
        Passenger.floor = GetRandom.random(App.getFloorCounter(), 1);
        return floor;
    }
}
