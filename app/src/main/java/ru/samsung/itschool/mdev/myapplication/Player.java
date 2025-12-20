package ru.samsung.itschool.mdev.myapplication;

public class Player {

    private int health;
    private int damage;

    public Player() {
        this.health = 30;  // здоровье игрока
        this.damage = 8;   // урон игрока
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public void takeDamage(int dmg) {
        health -= dmg;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public void heal(int amount) {
        health += amount;
    }
}
