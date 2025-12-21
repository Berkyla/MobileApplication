package ru.samsung.itschool.mdev.myapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Простая модель этажа с сеткой комнат.
 * Здесь генерируются комнаты, сундук с ключом и связи между соседями.
 */
public class Floor {

    private final int index;
    private final String name;
    private final int width;
    private final int height;
    private Room[][] grid;
    private Room startRoom;
    private Room bossRoom;
    private boolean keyFound;
    private boolean bossDefeated;

    public Floor(int index, String name) {
        this.index = index;
        this.name = name;
        this.width = 5;
        this.height = 5;
        generate();
    }

    private void generate() {
        boolean[][] layout = getTemplateForFloor(index);
        grid = new Room[height][width];

        int[] startCoord = getStartCoords(index);
        int[] bossCoord = getBossCoords(index);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (layout[y][x]) {
                    Room.RoomType type = Room.RoomType.NORMAL;
                    if (x == startCoord[0] && y == startCoord[1]) {
                        type = Room.RoomType.START;
                    } else if (x == bossCoord[0] && y == bossCoord[1]) {
                        type = Room.RoomType.BOSS;
                    }

                    String desc = "Этаж " + index + ": комната (" + (x + 1) + "," + (y + 1) + ").";
                    Room room = new Room(desc, x, y, index, type);
                    grid[y][x] = room;

                    if (type == Room.RoomType.START) {
                        startRoom = room;
                    } else if (type == Room.RoomType.BOSS) {
                        bossRoom = room;
                    }
                }
            }
        }

        // Связываем соседние комнаты
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Room room = grid[y][x];
                if (room == null) continue;

                if (y > 0 && grid[y - 1][x] != null) {
                    room.setNorth(grid[y - 1][x]);
                }
                if (y < height - 1 && grid[y + 1][x] != null) {
                    room.setSouth(grid[y + 1][x]);
                }
                if (x > 0 && grid[y][x - 1] != null) {
                    room.setWest(grid[y][x - 1]);
                }
                if (x < width - 1 && grid[y][x + 1] != null) {
                    room.setEast(grid[y][x + 1]);
                }
            }
        }

        placeKeyChest();
        placeLootBags();
        placeEnemies();
        placeBoss();
    }

    private boolean[][] getTemplateForFloor(int idx) {
        // 5x5 сетка, true = есть комната
        switch (idx) {
            case 1:
                return new boolean[][]{
                        {false, true, true, false, false},
                        {false, true, true, true, false},
                        {true, true, true, true, true},
                        {false, false, true, false, true},
                        {false, false, true, false, true}
                };
            case 2:
                return new boolean[][]{
                        {false, true, true, false, true},
                        {false, true, true, false, true},
                        {true, true, true, true, true},
                        {true, false, true, false, false},
                        {true, false, true, false, true}
                };
            default:
                return new boolean[][]{
                        {true, true, false, false, false},
                        {true, true, true, false, false},
                        {false, true, true, true, false},
                        {false, true, false, true, true},
                        {false, true, true, true, true}
                };
        }
    }

    private int[] getStartCoords(int idx) {
        switch (idx) {
            case 1:
                return new int[]{0, 2};
            case 2:
                return new int[]{0, 3};
            default:
                return new int[]{2, 4};
        }
    }

    private int[] getBossCoords(int idx) {
        switch (idx) {
            case 1:
                return new int[]{4, 2};
            case 2:
                return new int[]{4, 0};
            default:
                return new int[]{0, 0};
        }
    }

    private void placeKeyChest() {
        List<Room> candidates = getReachableNormalRoomsWithoutBoss();

        if (candidates.isEmpty()) {
            return;
        }

        Random random = new Random(index * 1234L);
        Room keyRoom = candidates.get(random.nextInt(candidates.size()));
        keyRoom.setHasChest(true);
        keyRoom.setChestHasKey(true);
        int coins = 15 + random.nextInt(16);
        Container chest = new Container(Container.ContainerType.CHEST, coins, true);
        for (Item item : ItemFactory.randomChestLoot()) {
            chest.getItems().add(item);
        }
        keyRoom.setChestContainer(chest);
    }

    private void placeLootBags() {
        // пара мешков с монетами для атмосферы
        List<Room> normalRooms = new ArrayList<>();
        for (Room[] row : grid) {
            for (Room room : row) {
                if (room != null && room.getType() == Room.RoomType.NORMAL && !room.hasChest()) {
                    normalRooms.add(room);
                }
            }
        }
        Random random = new Random(index * 4321L);
        for (int i = 0; i < Math.min(3, normalRooms.size()); i++) {
            if (normalRooms.isEmpty()) {
                break;
            }
            Room room = normalRooms.remove(random.nextInt(normalRooms.size()));
            room.setHasLootBag(true);
            int coins = 5 + random.nextInt(8);
            Container bag = new Container(Container.ContainerType.LOOT_BAG, coins, false);
            for (Item item : ItemFactory.randomBagLoot()) {
                bag.getItems().add(item);
            }
            room.setLootContainer(bag);
        }
    }

    private List<Room> getReachableNormalRoomsWithoutBoss() {
        List<Room> result = new ArrayList<>();
        if (startRoom == null) return result;

        boolean[][] visited = new boolean[height][width];
        List<Room> queue = new ArrayList<>();
        queue.add(startRoom);
        visited[startRoom.getY()][startRoom.getX()] = true;

        for (int i = 0; i < queue.size(); i++) {
            Room room = queue.get(i);

            if (room.getType() == Room.RoomType.NORMAL) {
                result.add(room);
            }

            for (Room neighbor : new Room[]{room.getNorth(), room.getSouth(), room.getEast(), room.getWest()}) {
                if (neighbor == null) continue;
                if (neighbor.isBossRoom()) continue; // не заходим в комнату босса
                int ny = neighbor.getY();
                int nx = neighbor.getX();
                if (!visited[ny][nx]) {
                    visited[ny][nx] = true;
                    queue.add(neighbor);
                }
            }
        }

        return result;
    }

    private void placeEnemies() {
        List<Room> normalRooms = new ArrayList<>();
        for (Room[] row : grid) {
            for (Room room : row) {
                if (room != null && room.getType() == Room.RoomType.NORMAL) {
                    normalRooms.add(room);
                }
            }
        }

        int enemyCount = Math.min(3 + index, normalRooms.size());
        Random random = new Random(index * 2468L);
        for (int i = 0; i < enemyCount; i++) {
            if (normalRooms.isEmpty()) {
                break;
            }
            Room room = normalRooms.remove(random.nextInt(normalRooms.size()));
            room.setEnemy(createMob(random));
        }
    }

    private void placeBoss() {
        if (bossRoom == null) return;
        bossRoom.setEnemy(createBoss());
    }

    private Enemy createMob(Random random) {
        int roll = random.nextInt(100);
        String name;
        int health;
        int damage;

        if (index == 1) {
            if (roll < 60) {
                name = "Подземный ткач";
                health = 28;
                damage = 6;
            } else if (roll < 90) {
                name = "Мёртвый часовой";
                health = 36;
                damage = 8;
            } else {
                name = "Несущий погибель";
                health = 44;
                damage = 10;
            }
        } else if (index == 2) {
            if (roll < 40) {
                name = "Подземный ткач";
                health = 38;
                damage = 9;
            } else if (roll < 75) {
                name = "Мёртвый часовой";
                health = 50;
                damage = 12;
            } else {
                name = "Несущий погибель";
                health = 64;
                damage = 14;
            }
        } else {
            if (roll < 20) {
                name = "Подземный ткач";
                health = 50;
                damage = 12;
            } else if (roll < 60) {
                name = "Мёртвый часовой";
                health = 68;
                damage = 16;
            } else {
                name = "Несущий погибель";
                health = 82;
                damage = 20;
            }
        }

        int coins = getMobCoinReward(random);
        Enemy mob = new Enemy(name, health, damage, coins, false);
        if (random.nextInt(100) < getMobConsumableChance()) {
            mob.addLoot(ItemFactory.randomHealForFloor(index));
        }
        if (random.nextInt(100) < getMobConsumableChance() / 2 + 20) {
            mob.addLoot(ItemFactory.randomManaForFloor(index));
        }
        return mob;
    }

    private int getMobCoinReward(Random random) {
        if (index == 1) {
            return 6 + random.nextInt(7);
        } else if (index == 2) {
            return 12 + random.nextInt(9);
        } else {
            return 18 + random.nextInt(11);
        }
    }

    private int getMobConsumableChance() {
        if (index == 1) return 45;
        if (index == 2) return 55;
        return 70;
    }

    private Enemy createBoss() {
        Enemy boss;
        if (index == 1) {
            boss = new Enemy("Колдун Циан", 70, 12, 45, true);
        } else if (index == 2) {
            boss = new Enemy("Магматический лорд", 110, 18, 70, true);
        } else {
            boss = new Enemy("Тёмный кардинал", 150, 24, 100, true);
        }
        boss.addLoot(ItemFactory.randomBossReward(index));
        boss.addLoot(ItemFactory.randomArmorForFloor(index));
        boss.addLoot(ItemFactory.randomManaForFloor(index));
        return boss;
    }

    public Room[][] getGrid() {
        return grid;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Room getStartRoom() {
        return startRoom;
    }

    public Room getBossRoom() {
        return bossRoom;
    }

    public boolean isKeyFound() {
        return keyFound;
    }

    public void setKeyFound(boolean keyFound) {
        this.keyFound = keyFound;
    }

    public boolean isBossDefeated() {
        return bossDefeated;
    }

    public void setBossDefeated(boolean bossDefeated) {
        this.bossDefeated = bossDefeated;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }
}