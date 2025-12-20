package ru.samsung.itschool.mdev.myapplication;

public class World {

    private Floor currentFloor;
    private int currentFloorIndex = 0;
    private final Floor[] floors = new Floor[3];

    public World() {
        generate();
    }

    private void generate() {

        floors[0] = new Floor(1, "Руины арканистов");
        floors[1] = new Floor(2, "Затопленные галереи");
        floors[2] = new Floor(3, "Небесная обсерватория");

        currentFloor = floors[currentFloorIndex];
    }

    public Room getStartRoom() {
        return currentFloor.getStartRoom();
    }

    public Floor getCurrentFloor() {
        return currentFloor;
    }

    public boolean hasNextFloor() {
        return currentFloorIndex < floors.length - 1;
    }

    public boolean moveToNextFloor() {
        if (!hasNextFloor()) {
            return false;
        }
        currentFloorIndex++;
        currentFloor = floors[currentFloorIndex];
        return true;
    }
}