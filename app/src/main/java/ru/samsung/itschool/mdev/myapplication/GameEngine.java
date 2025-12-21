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
                        "magic - применить заклинание\n" +
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

            case "magic":
            case "cast":
            case "spell":
                return castMagic();

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

        return desc.toString();
    }

    private String move(String direction) {
        if (hasEnemyInRoom()) {
            return "Нельзя уйти во время боя.";
        }

        // Специальный переход на следующий этаж из комнаты босса
        if (currentRoom != null
                && currentRoom.isBossRoom()
                && world.getCurrentFloor().isBossDefeated()
                && world.hasNextFloor()
                && "north".equals(direction)) {
            player.removeKeyItems();
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
        currentRoom.visit();

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

        Enemy enemy = currentRoom.getEnemy();
        if (enemy != null && !enemy.isDead()) {
            desc.append("\nВ комнате враг: ").append(enemy.getName())
                    .append(" (").append(enemy.getHealth()).append("/").append(enemy.getMaxHealth())
                    .append(" HP, атака ").append(enemy.getDamage()).append(").");
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
        if (hasEnemyInRoom()) {
            return "Нельзя открыть что-либо во время боя.";
        }
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

        StringBuilder message = new StringBuilder("Вы открыли сундук: +" + chest.getCoins() + " 💰");

        if (chest.hasKey()) {
            world.getCurrentFloor().setKeyFound(true);
            Item key = ItemFactory.bossKey();
            player.addItem(key);
            message.append(", найден ").append(key.getIcon()).append(" ").append(key.getName());
        }

        for (Item item : chest.getItems()) {
            player.addItem(item);
            message.append(", получено ").append(item.getIcon()).append(" ").append(item.getName());
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
        StringBuilder message = new StringBuilder("Вы открыли мешок: +" + bag.getCoins() + " 💰");
        for (Item item : bag.getItems()) {
            player.addItem(item);
            message.append(", найдено ").append(item.getIcon()).append(" ").append(item.getName());
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
            StringBuilder sb = new StringBuilder();
            if (item.getType() == Item.ItemType.CONSUMABLE_HP) {
                int healed = player.heal(item.getHealAmount());
                sb.append("Вы используете ").append(item.getIcon()).append(" ").append(item.getName())
                        .append(": +").append(healed).append(" HP.");
            } else if (item.getType() == Item.ItemType.CONSUMABLE_MANA) {
                int restored = player.restoreMana(item.getManaRestore());
                sb.append("Вы пьёте ").append(item.getIcon()).append(" ").append(item.getName())
                        .append(": +").append(restored).append(" маны.");
            }
            player.removeItem(item);

            if (hasEnemyInRoom()) {
                Enemy enemy = currentRoom.getEnemy();
                sb.append("\nПока вы роетесь в рюкзаке, ")
                        .append(enemy.getName()).append(" пользуется моментом.");
                appendEnemyTurn(sb, enemy);
            }
            sb.append("\n").append(getPlayerStats());
            return sb.toString();
        }

        if (item.isArmor() || item.isWeapon()) {
            player.equip(item);
            return "Вы экипировали " + item.getIcon() + " " + item.getName() + ".";
        }

        if (item.getType() == Item.ItemType.KEY) {
            return "Ключ уже у вас. Он сработает у двери босса.";
        }

        return "Нельзя использовать этот предмет.";
    }

    public String getPlayerStats() {
        return "Урон: " + player.getTotalDamage() + ", защита: " + player.getTotalDefense();
    }

    public boolean isInBattle() {
        return hasEnemyInRoom();
    }

    private boolean hasEnemyInRoom() {
        Enemy enemy = currentRoom != null ? currentRoom.getEnemy() : null;
        return enemy != null && !enemy.isDead();
    }

    private String castMagic() {
        Enemy enemy = currentRoom.getEnemy();
        if (enemy == null || enemy.isDead()) {
            return "Некого атаковать.";
        }
        int manaCost = 10;
        if (player.getMana() < manaCost) {
            return "Недостаточно маны.";
        }
        player.spendMana(manaCost);
        int base = player.getTotalDamage();
        int spellDamage = Math.max(1, (int) Math.round(base * 1.2) + 2);
        enemy.takeDamage(spellDamage);

        StringBuilder sb = new StringBuilder();
        sb.append("Вы произносите ✨ Ледяной осколок: -").append(spellDamage)
                .append(" HP врагу ").append(enemy.getName()).append(".");

        if (enemy.isDead()) {
            sb.append(handleEnemyDefeat(enemy));
            return sb.toString();
        }

        sb.append("\nМана: ").append(player.getMana()).append("/").append(player.getMaxMana());
        appendEnemyTurn(sb, enemy);
        if (!player.isDead()) {
            sb.append("\nЗдоровье врага: ").append(enemy.getHealth()).append("/").append(enemy.getMaxHealth());
        }
        return sb.toString();
    }

    private String handleBossEntry() {
        Floor floor = world.getCurrentFloor();
        if (!floor.isBossDefeated()) {
            player.removeKeyItems();
            Enemy enemy = floor.getBossRoom() != null ? floor.getBossRoom().getEnemy() : null;
            if (enemy != null) {
                return "Вы открываете дверь в покои босса. Внутри ждёт " + enemy.getName() + ".";
            }
            return "Покои босса пусты, но напряжение не спадает.";
        } else {
            if (world.hasNextFloor()) {
                return "Комната босса очищена. На север ведёт лестница дальше.\n";
            }
            return "Все боссы побеждены, в зале тихо.\n";
        }
    }

    private String attackEnemy() {
        Enemy enemy = currentRoom.getEnemy();

        if (enemy == null || enemy.isDead()) {
            return "Некого атаковать.";
        }

        StringBuilder sb = new StringBuilder();
        int playerDamage = player.getDamage();
        enemy.takeDamage(playerDamage);

        sb.append("Вы ударили: -").append(playerDamage)
                .append(" HP врагу ").append(enemy.getName()).append(".");

        if (enemy.isDead()) {
            sb.append(handleEnemyDefeat(enemy));
            return sb.toString();
        }

        appendEnemyTurn(sb, enemy);

        if (player.isDead()) {
            return sb.toString();
        }

        sb.append("\nЗдоровье врага: ").append(enemy.getHealth()).append("/").append(enemy.getMaxHealth());
        return sb.toString();
    }

    private void appendEnemyTurn(StringBuilder sb, Enemy enemy) {
        int incoming = player.takeDamage(enemy.getDamage());
        sb.append("\n").append(enemy.getName()).append(" атакует: -")
                .append(incoming).append(" HP вам.");
        if (player.isDead()) {
            sb.append("\n").append(handlePlayerDeath());
        } else {
            sb.append("\nВаше здоровье: ").append(player.getHealth())
                    .append("/").append(player.getMaxHealth());
        }
    }

    private String handleEnemyDefeat(Enemy enemy) {
        Floor floor = world.getCurrentFloor();
        StringBuilder sb = new StringBuilder("\nВраг повержен!");
        if (enemy.getCoinsReward() > 0) {
            player.addCoins(enemy.getCoinsReward());
            sb.append(" +").append(enemy.getCoinsReward()).append(" 💰.");
        }
        if (!enemy.getLoot().isEmpty()) {
            sb.append(" Добыча: ");
            for (int i = 0; i < enemy.getLoot().size(); i++) {
                Item item = enemy.getLoot().get(i);
                player.addItem(item);
                sb.append(item.getIcon()).append(" ").append(item.getName());
                if (i < enemy.getLoot().size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append(".");
        }
        if (enemy.isBoss()) {
            floor.setBossDefeated(true);
            sb.append(" Комната босса очищена.");
            if (world.hasNextFloor()) {
                sb.append(" Путь на север открыт.");
            } else {
                sb.append(" Вы завершили приключение!");
            }
        }
        currentRoom.setEnemy(null);
        return sb.toString();
    }

    private String handlePlayerDeath() {
        Floor floor = world.getCurrentFloor();
        currentRoom = floor.getStartRoom();
        if (currentRoom != null) {
            currentRoom.visit();
        }
        player.restoreFull();
        return "Вы погибли... Вас оттаскивают к началу этажа. Силы восстановлены, но враг всё ещё в комате.";
    }
}