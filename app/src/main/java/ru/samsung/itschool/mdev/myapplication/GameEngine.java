package ru.samsung.itschool.mdev.myapplication;

public class GameEngine {

    private World world;
    private Room currentRoom;
    private Player player;

    public GameEngine() {
        world = new World();
        currentRoom = world.getStartRoom();
        player = new Player();
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

            case "go north": return move(currentRoom.getNorth());
            case "go south": return move(currentRoom.getSouth());
            case "go east":  return move(currentRoom.getEast());
            case "go west":  return move(currentRoom.getWest());

            default:
                return "Неизвестная команда. Введите 'help'.";
        }
    }

    private String look() {
        String desc = "Здоровье игрока: " + player.getHealth() + "\n";
        desc += currentRoom.getDescription();

        Enemy e = currentRoom.getEnemy();
        if (e != null && !e.isDead()) {
            desc += "\nЗдесь враг: " + e.getName() +
                    " (здоровье: " + e.getHealth() + ")";
        }

        return desc;
    }

    private String move(Room next) {
        if (next == null) {
            return "Вы не можете туда идти.";
        } else {
            currentRoom = next;
            return "Вы переместились.\n" + look();
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
}
