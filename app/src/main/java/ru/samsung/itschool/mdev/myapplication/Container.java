package ru.samsung.itschool.mdev.myapplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Простой контейнер (сундук или мешок).
 * Хранит предметы и монеты, умеет отмечать состояние "открыт".
 */
public class Container {

    public enum ContainerType {
        CHEST,
        LOOT_BAG
    }

    private final ContainerType type;
    private final List<Item> items = new ArrayList<>();
    private final int coins;
    private final boolean hasKey;
    private boolean opened;

    public Container(ContainerType type, int coins, boolean hasKey) {
        this.type = type;
        this.coins = coins;
        this.hasKey = hasKey;
    }

    public ContainerType getType() {
        return type;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getCoins() {
        return coins;
    }

    public boolean hasKey() {
        return hasKey;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }
}