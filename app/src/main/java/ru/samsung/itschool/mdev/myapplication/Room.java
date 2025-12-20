package ru.samsung.itschool.mdev.myapplication;

public class Room {

    public enum RoomType {
        START,
        NORMAL,
        BOSS
    }

    private String description;
    private Room north;
    private Room south;
    private Room east;
    private Room west;

    private Enemy enemy;  // ← добавили врага
    private int x;
    private int y;
    private int floorLevel;
    private RoomType type = RoomType.NORMAL;
    private boolean discovered;
    private boolean visited;
    private boolean hasChest;
    private boolean chestOpened;
    private boolean chestHasKey;
    private boolean hasLootBag;
    private boolean lootCollected;
    private boolean bossRevealed;

    public Room(String description) {
        this.description = description;
    }

    public Room(String description, int x, int y, int floorLevel, RoomType type) {
        this.description = description;
        this.x = x;
        this.y = y;
        this.floorLevel = floorLevel;
        this.type = type;
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getFloorLevel() {
        return floorLevel;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public boolean isBossRoom() {
        return type == RoomType.BOSS;
    }

    public boolean isStartRoom() {
        return type == RoomType.START;
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public void discover() {
        this.discovered = true;
    }

    public boolean isVisited() {
        return visited;
    }

    public void visit() {
        this.visited = true;
        this.discovered = true;
    }

    public boolean hasChest() {
        return hasChest;
    }

    public void setHasChest(boolean hasChest) {
        this.hasChest = hasChest;
    }

    public boolean isChestOpened() {
        return chestOpened;
    }

    public void openChest() {
        this.chestOpened = true;
    }

    public boolean chestHasKey() {
        return chestHasKey;
    }

    public void setChestHasKey(boolean chestHasKey) {
        this.chestHasKey = chestHasKey;
    }

    public boolean hasLootBag() {
        return hasLootBag;
    }

    public void setHasLootBag(boolean hasLootBag) {
        this.hasLootBag = hasLootBag;
    }

    public boolean isLootCollected() {
        return lootCollected;
    }

    public void collectLoot() {
        this.lootCollected = true;
    }

    public boolean isBossRevealed() {
        return bossRevealed;
    }

    public void setBossRevealed(boolean bossRevealed) {
        this.bossRevealed = bossRevealed;
    }
}