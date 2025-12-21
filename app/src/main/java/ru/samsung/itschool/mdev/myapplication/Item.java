package ru.samsung.itschool.mdev.myapplication;

/**
 * Базовая модель предмета для учебной игры.
 * Здесь минимум полей, чтобы новичку было проще разобраться в системе инвентаря.
 */
public class Item {

    public enum ItemType {
        CURRENCY,
        ARMOR,
        WEAPON,
        CONSUMABLE_HP,
        CONSUMABLE_MANA,
        KEY
    }

    public enum ItemSlot {
        NONE,
        HELMET,
        BODY,
        LEGS,
        BOOTS,
        WEAPON
    }

    private final String id;
    private final String name;
    private final String description;
    private final String emoji;
    private final ItemType type;
    private final ItemSlot slot;

    // Числовые параметры предмета. Для простоты держим их в одном классе.
    private final int damageBonus;
    private final int defenseBonus;
    private final int healAmount;
    private final int manaRestore;
    private final int coinAmount;

    public Item(String id, String name, String description, String emoji, ItemType type, ItemSlot slot,
                int damageBonus, int defenseBonus, int healAmount, int manaRestore, int coinAmount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.emoji = emoji;
        this.type = type;
        this.slot = slot;
        this.damageBonus = damageBonus;
        this.defenseBonus = defenseBonus;
        this.healAmount = healAmount;
        this.manaRestore = manaRestore;
        this.coinAmount = coinAmount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getIcon() {
        return emoji;
    }

    public ItemType getType() {
        return type;
    }

    public ItemSlot getSlot() {
        return slot;
    }

    public int getDamageBonus() {
        return damageBonus;
    }

    public int getDefenseBonus() {
        return defenseBonus;
    }

    public int getHealAmount() {
        return healAmount;
    }

    public int getManaRestore() {
        return manaRestore;
    }

    public int getCoinAmount() {
        return coinAmount;
    }

    public boolean isArmor() {
        return type == ItemType.ARMOR;
    }

    public boolean isWeapon() {
        return type == ItemType.WEAPON;
    }

    public boolean isConsumable() {
        return type == ItemType.CONSUMABLE_HP || type == ItemType.CONSUMABLE_MANA;
    }
}