package ru.samsung.itschool.mdev.myapplication;

public class Player {

    private int maxHealth;
    private int health;
    private int maxMana;
    private int mana;
    private int damage;
    private int coins;

    public Player() {
        this.maxHealth = 100;
        this.health = 100;  // здоровье игрока
        this.maxMana = 50;
        this.mana = 50;
        this.damage = 8;   // урон игрока
        this.coins = 0;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
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

    public void heal(int amount) {
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getMana() {
        return mana;
    }

    public void spendMana(int amount) {
        mana -= amount;
        if (mana < 0) {
            mana = 0;
        }
    }

    public void restoreMana(int amount) {
        mana += amount;
        if (mana > maxMana) {
            mana = maxMana;
        }
    }

    public void addCoins(int amount) {
        coins += amount;
    }

    public int getCoins() {
        return coins;
    }
}