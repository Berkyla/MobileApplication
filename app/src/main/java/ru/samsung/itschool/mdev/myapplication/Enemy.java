package ru.samsung.itschool.mdev.myapplication;

public class Enemy {

    private String name;
    private int health;
    private int damage;
    private int maxHealth;
    private int coinsReward;
    private boolean boss;
    private java.util.List<Item> loot = new java.util.ArrayList<>();

    public Enemy(String name, int health, int damage) {
        this.name = name;
        this.health = health;
        this.damage = damage;
        this.maxHealth = health;
    }

    public Enemy(String name, int health, int damage, int coinsReward, boolean boss) {
        this(name, health, damage);
        this.coinsReward = coinsReward;
        this.boss = boss;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCoinsReward() {
        return coinsReward;
    }

    public boolean isBoss() {
        return boss;
    }

    public void takeDamage(int dmg) {
        health -= dmg;
        if (health < 0) {
            health = 0;
        }
    }

    public boolean isDead() {
        return health <= 0;
    }

    public java.util.List<Item> getLoot() {
        return loot;
    }

    public void addLoot(Item item) {
        loot.add(item);
    }
}