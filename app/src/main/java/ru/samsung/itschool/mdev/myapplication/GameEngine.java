package ru.samsung.itschool.mdev.myapplication;

public class GameEngine {

    private World world;
    private Room currentRoom;
    private Player player;

    public GameEngine() {
        world = new World();
        player = new Player();
        currentRoom = world.getStartRoom();
    }

    public String process(String cmd) {
        cmd = cmd.toLowerCase().trim();

        switch (cmd) {
            case "help":
                return "Команды:\n" +
                        "help - помощь\n" +
                        "look - осмотреться\n" +
                        "go north/south/east/west - идти\n" +
                        "attack - атаковать врага\n";

            case "look":
                return look();

            case "attack":
            case "hit":
            case "fight":
                return attackEnemy();

            case "go north": return move("north");
            case "go south": return move("south");
            case "go east":  return move("east");
            case "go west":  return move("west");

            default:
                return "Неизвестная команда. Введите 'help'.";
        }
    }

    private String look() {
        if (currentRoom == null) {
            return "Мир ещё не загружен.";
        }

        currentRoom.visit();

        StringBuilder desc = new StringBuilder();
        desc.append(describeRoom(true));

        Enemy e = currentRoom.getEnemy();
        if (e != null && !e.isDead()) {
            desc.append("\nЗдесь враг: ").append(e.getName())
                    .append(" (здоровье: ").append(e.getHealth()).append(")");
        }

        return desc.toString();
    }

    private String move(String direction) {
        // Специальный переход на следующий этаж из комнаты босса
        if (currentRoom != null
                && currentRoom.isBossRoom()
                && world.getCurrentFloor().isBossDefeated()
                && world.hasNextFloor()
                && "north".equals(direction)) {
            world.moveToNextFloor();
            currentRoom = world.getStartRoom();
            if (currentRoom != null) {
                currentRoom.visit();
            }
            return "Вы поднимаетесь на следующий этаж.\n" + describeRoom(false);
        }

        Room next = getNextRoom(direction);
        if (next == null) {
            return "Там стена.";
        }

        Floor floor = world.getCurrentFloor();
        if (next.isBossRoom() && !floor.isKeyFound()) {
            next.setBossRevealed(true);
            return "Дверь в комнату босса заперта. Нужен ключ.";
        }

        currentRoom = next;

        StringBuilder sb = new StringBuilder("Вы переместились.\n");

        if (next.isBossRoom()) {
            sb.append(handleBossEntry());
        }

        sb.append(describeRoom(false));
        return sb.toString();
    }

    private Room getNextRoom(String direction) {
        switch (direction) {
            case "north":
                return currentRoom.getNorth();
            case "south":
                return currentRoom.getSouth();
            case "east":
                return currentRoom.getEast();
            case "west":
                return currentRoom.getWest();
            default:
                return null;
        }
    }

    private String attackEnemy() {
        Enemy enemy = currentRoom.getEnemy();

        if (enemy == null || enemy.isDead()) {
            return "Здесь нет врагов.";
        }

        // игрок атакует
        enemy.takeDamage(player.getDamage());

        if (enemy.isDead()) {
            return "Вы ударили и убили врага: " + enemy.getName();
        }

        // враг атакует
        player.takeDamage(enemy.getDamage());

        if (player.isDead()) {
            return "Вы атаковали врага, но он ударил в ответ.\n" +
                    "Вы погибли.\nИгра окончена!";
        }

        return "Вы ударили врага (" + player.getDamage() + " урона).\n" +
                enemy.getName() + " атакует в ответ (" + enemy.getDamage() + " урона).\n" +
                "Ваше здоровье: " + player.getHealth() + "\n" +
                "Здоровье врага: " + enemy.getHealth();
    }

    private String handleBossEntry() {
        Floor floor = world.getCurrentFloor();
        if (!floor.isBossDefeated()) {
            floor.setBossDefeated(true);
            if (world.hasNextFloor()) {
                return "Вы использовали ключ и вошли в покои босса. Победа за вами! Лестница на север ведёт на следующий этаж.\n";
            } else {
                return "Это последний босс, и вы одержали победу!\n";
            }
        } else {
            return "Комната босса уже очищена.\n";
        }
    }

    private String describeRoom(boolean interactWithObjects) {
        StringBuilder desc = new StringBuilder();
        Floor floor = world.getCurrentFloor();
        desc.append("Этаж ").append(floor.getIndex()).append(": ").append(floor.getName()).append("\n");
        desc.append(currentRoom.getDescription());

        revealAdjacentBossRooms();

        if (currentRoom.isStartRoom()) {
            desc.append("\nОтсюда начинается путь по этажу.");
        }

        if (currentRoom.isBossRoom()) {
            if (!floor.isKeyFound()) {
                desc.append("\nПеред вами массивная дверь босса. Нужен ключ.");
            } else if (!floor.isBossDefeated()) {
                desc.append("\nДверь босса открыта, впереди решающая схватка.");
            } else {
                desc.append("\nКомната босса уже покорена.");
                if (world.hasNextFloor()) {
                    desc.append(" Лестница на север ведёт дальше.");
                }
            }
        }

        if (currentRoom.hasChest()) {
            if (currentRoom.isChestOpened()) {
                desc.append("\nЗдесь открыт пустой сундук.");
            } else if (interactWithObjects) {
                desc.append("\n").append(openChest());
            } else {
                desc.append("\nВ комнате стоит закрытый сундук.");
            }
        }

        if (currentRoom.hasLootBag()) {
            if (currentRoom.isLootCollected()) {
                desc.append("\nНа полу лежит пустой мешочек.");
            } else if (interactWithObjects) {
                desc.append("\n").append(collectBag());
            } else {
                desc.append("\nВы замечаете мешочек с монетами.");
            }
        }

        desc.append("\nВыходы: ").append(listExits());
        return desc.toString();
    }

    private void revealAdjacentBossRooms() {
        for (Room neighbor : new Room[]{currentRoom.getNorth(), currentRoom.getSouth(), currentRoom.getEast(), currentRoom.getWest()}) {
            if (neighbor != null && neighbor.isBossRoom()) {
                neighbor.setBossRevealed(true);
            }
        }
    }

    private String openChest() {
        currentRoom.openChest();
        int coins = 10 + (int) (Math.random() * 15);
        player.addCoins(coins);
        StringBuilder builder = new StringBuilder("Вы открываете сундук и находите " + coins + " монет.");
        if (currentRoom.chestHasKey()) {
            world.getCurrentFloor().setKeyFound(true);
            builder.append(" Среди них блестит ключ от комнаты босса!");
        }
        return builder.toString();
    }

    private String collectBag() {
        currentRoom.collectLoot();
        int coins = 3 + (int) (Math.random() * 8);
        player.addCoins(coins);
        return "Вы подбираете мешочек с " + coins + " монетами.";
    }

    private String listExits() {
        StringBuilder exits = new StringBuilder();
        if (currentRoom.getNorth() != null || (currentRoom.isBossRoom() && world.hasNextFloor() && world.getCurrentFloor().isBossDefeated())) {
            exits.append("север ");
        }
        if (currentRoom.getSouth() != null) {
            exits.append("юг ");
        }
        if (currentRoom.getEast() != null) {
            exits.append("восток ");
        }
        if (currentRoom.getWest() != null) {
            exits.append("запад ");
        }
        return exits.length() == 0 ? "нет выхода" : exits.toString().trim();
    }

    public Player getPlayer() {
        return player;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public Floor getCurrentFloor() {
        return world.getCurrentFloor();
    }

    public Room[][] getCurrentFloorGrid() {
        return world.getCurrentFloor().getGrid();
    }
}