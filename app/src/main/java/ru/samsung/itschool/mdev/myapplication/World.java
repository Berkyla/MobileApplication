package ru.samsung.itschool.mdev.myapplication;

public class World {

    private Room startRoom;

    public World() {
        generate();
    }

    private void generate() {

        Room room1 = new Room("Вы в тёмной комнате.");
        Room room2 = new Room("Вы на узком коридоре.");
        Room room3 = new Room("Вы в большой пещере.");
        Room room4 = new Room("Вы у выхода наружу.");

        // связываем комнаты
        room1.setNorth(room2);
        room2.setSouth(room1);

        room2.setEast(room3);
        room3.setWest(room2);

        room3.setNorth(room4);
        room4.setSouth(room3);

        // добавляем врага
        room3.setEnemy(new Enemy("Гоблин", 20, 5));

        startRoom = room1;
    }

    public Room getStartRoom() {
        return startRoom;
    }
}
