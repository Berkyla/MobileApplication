package ru.samsung.itschool.mdev.myapplication;

public class Room {

    private String description;
    private Room north;
    private Room south;
    private Room east;
    private Room west;

    private Enemy enemy;  // ← добавили врага

    public Room(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Room getNorth() { return north; }
    public Room getSouth() { return south; }
    public Room getEast()  { return east; }
    public Room getWest()  { return west; }

    public void setNorth(Room room) { this.north = room; }
    public void setSouth(Room room) { this.south = room; }
    public void setEast(Room room)  { this.east = room; }
    public void setWest(Room room)  { this.west = room; }

    public Enemy getEnemy() { return enemy; }
    public void setEnemy(Enemy enemy) { this.enemy = enemy; }
}
