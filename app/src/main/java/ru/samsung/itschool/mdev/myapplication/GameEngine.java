package ru.samsung.itschool.mdev.myapplication;

public class GameEngine {

    private World world;
    private Room currentRoom;
    private Player player;

    public GameEngine() {
        world = new World();
        player = new Player();
        currentRoom = world.getStartRoom();
        if (currentRoom != null) {
            currentRoom.visit();
        }
    }

    public String process(String cmd) {
        cmd = cmd.toLowerCase().trim();

        switch (cmd) {
            case "help":
                return "Команды:\n" +
                        "help - помощь\n" +
                        "look - осмотреться\n" +
                        "go north/south/east/west - идти\n" +
                        "attack - атаковать врага\n" +
                        "open - открыть сундук или мешок";

            case "look":
                return look();

            case "attack":
            case "hit":
            case "fight":
                return attackEnemy();

            case "open":
                return openContainer();

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
        desc.append(describeRoom());

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
            return "Вы поднимаетесь на следующий этаж.\n" + describeRoom();
        }

        Room next = getNextRoom(direction);
        if (next == null) {
            return "Там стена.";
        }

        Floor floor = world.getCurrentFloor();
        if (next.isBossRoom() && !floor.isBossDefeated() && !floor.isKeyFound()) {
            next.setBossRevealed(true);
            return "Дверь в комнату босса заперта. Нужен ключ.";
        }

        currentRoom = next;

        StringBuilder sb = new StringBuilder("Вы переместились.\n");

        if (next.isBossRoom()) {
            sb.append(handleBossEntry());
        }

        sb.append(describeRoom());
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
        int playerDamage = player.getDamage();
        enemy.takeDamage(playerDamage);

        if (enemy.isDead()) {
            return "Вы ударили (" + playerDamage + ") и убили врага: " + enemy.getName();
        }

        // враг атакует
        int incoming = Math.max(1, enemy.getDamage() - player.getTotalDefense());
        player.takeDamage(enemy.getDamage());

        if (player.isDead()) {
            return "Вы атаковали врага, но он ударил в ответ.\n" +
                    "Вы погибли.\nИгра окончена!";
        }

        return "Вы ударили врага (" + playerDamage + " урона).\n" +
                enemy.getName() + " атакует в ответ (" + incoming + " урона, защита учтена).\n" +
                "Ваше здоровье: " + player.getHealth() + "\n" +
                "Здоровье врага: " + enemy.getHealth();
    }

    private String handleBossEntry() {
        Floor floor = world.getCurrentFloor();
        if (!floor.isBossDefeated()) {
            floor.setKeyFound(false); // ключ израсходован
            player.removeKeyItems();
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

    private String describeRoom() {
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
                desc.append("\nПеред вами массивная дверь босса. Нужн ключ.");
            } else if (!floor.isBossDefeated()) {
                desc.append("\nДверь босса открыта, впереди решающая схватка.");
            } else {
                desc.append("\nКомната босса уже покорена.");
                if (world.hasNextFloor()) {
                    desc.append(" Лестница на север ведёт дальше.");
                }
            }
        }

        appendContainerDescription(desc);

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

    public String openContainer() {
        if (currentRoom.hasChest() && currentRoom.getChestContainer() != null && !currentRoom.getChestContainer().isOpened()) {
            return lootChest();
        }
        if (currentRoom.hasLootBag() && currentRoom.getLootContainer() != null && !currentRoom.getLootContainer().isOpened()) {
            return lootBag();
        }
        return "Нечего открывать.";
    }

    private String lootChest() {
        Container chest = currentRoom.getChestContainer();
        if (chest == null || chest.isOpened()) {
            return "Сундук пуст.";
        }

        chest.setOpened(true);
        currentRoom.openChest();
        player.addCoins(chest.getCoins());

        StringBuilder message = new StringBuilder("Вы открыли сундук: +" + chest.getCoins() + " 🪙");

        if (chest.hasKey()) {
            world.getCurrentFloor().setKeyFound(true);
            player.addItem(ItemFactory.bossKey());
            message.append(", найден ").append(ItemFactory.bossKey().getEmoji()).append(" ").append(ItemFactory.bossKey().getName());
        }

        for (Item item : chest.getItems()) {
            player.addItem(item);
            message.append(", получено ").append(item.getEmoji()).append(" ").append(item.getName());
        }

        if (chest.getItems().isEmpty() && !chest.hasKey()) {
            message.append(". Внутри только монеты.");
        }

        return message.toString();
    }

    private String lootBag() {
        Container bag = currentRoom.getLootContainer();
        if (bag == null || bag.isOpened()) {
            return "Мешок пуст.";
        }
        bag.setOpened(true);
        currentRoom.collectLoot();
        player.addCoins(bag.getCoins());
        StringBuilder message = new StringBuilder("Вы открыли мешок: +" + bag.getCoins() + " 🪙");
        for (Item item : bag.getItems()) {
            player.addItem(item);
            message.append(", найдено ").append(item.getEmoji()).append(" ").append(item.getName());
        }
        return message.toString();
    }

    private void appendContainerDescription(StringBuilder desc) {
        if (currentRoom.hasChest()) {
            if (currentRoom.isChestOpened()) {
                desc.append("\nЗдесь открыт пустой сундук.");
            } else {
                desc.append("\nВ комнате стоит закрытый сундук.");
            }
        }

        if (currentRoom.hasLootBag()) {
            if (currentRoom.isLootCollected()) {
                desc.append("\nНа полу лежит пустой мешочек.");
            } else {
                desc.append("\nВы замечаете закрытый мешок с находками.");
            }
        }
    }

    public String useItem(Item item) {
        if (item == null) return "Предмет не найден.";

        if (item.isConsumable()) {
            player.useConsumable(item);
            return "Вы используете " + item.getEmoji() + " " + item.getName() + ".";
        }

        if (item.isArmor() || item.isWeapon()) {
            player.equip(item);
            return "Вы экипировали " + item.getEmoji() + " " + item.getName() + ".";
        }

        if (item.getType() == Item.ItemType.KEY) {
            return "Ключ уже у вас. Он сработает у двери босса.";
        }

        return "Нельзя использовать этот предмет.";
    }

    public String getPlayerStats() {
        return "Урон: " + player.getTotalDamage() + ", защита: " + player.getTotalDefense();
    }
}