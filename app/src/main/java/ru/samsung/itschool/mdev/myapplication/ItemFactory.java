package ru.samsung.itschool.mdev.myapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Набор готовых предметов и простых фабричных методов.
 * Здесь хранятся базовые характеристики, чтобы ими удобно пользоваться при генерации лута.
 */
public class ItemFactory {

    private static final Random RANDOM = new Random();

    // --- Расходники HP ---
    public static Item weakHealScroll() {
        return new Item("scroll_heal_small", "Свиток слабого лечения",
                "+20 HP", "📜", Item.ItemType.CONSUMABLE_HP, Item.ItemSlot.NONE,
                0, 0, 20, 0, 0);
    }

    public static Item healScroll() {
        return new Item("scroll_heal_medium", "Свиток лечения",
                "+45 HP", "📜", Item.ItemType.CONSUMABLE_HP, Item.ItemSlot.NONE,
                0, 0, 45, 0, 0);
    }

    public static Item strongHealScroll() {
        return new Item("scroll_heal_big", "Свиток сильного лечения",
                "+80 HP", "📜", Item.ItemType.CONSUMABLE_HP, Item.ItemSlot.NONE,
                0, 0, 80, 0, 0);
    }

    // --- Расходники Mana ---
    public static Item smallManaPotion() {
        return new Item("potion_mana_small", "Малое зелье маны",
                "+15 Mana", "🧪", Item.ItemType.CONSUMABLE_MANA, Item.ItemSlot.NONE,
                0, 0, 0, 15, 0);
    }

    public static Item mediumManaPotion() {
        return new Item("potion_mana_medium", "Среднее зелье маны",
                "+30 Mana", "🧪", Item.ItemType.CONSUMABLE_MANA, Item.ItemSlot.NONE,
                0, 0, 0, 30, 0);
    }

    public static Item largeManaPotion() {
        return new Item("potion_mana_large", "Большое зелье маны",
                "+50 Mana", "🧪", Item.ItemType.CONSUMABLE_MANA, Item.ItemSlot.NONE,
                0, 0, 0, 50, 0);
    }

    // --- Оружие ---
    public static Item rogueSword() {
        return new Item("weapon_rogue", "Меч разбойника",
                "Лёгкий клинок.", "🗡️", Item.ItemType.WEAPON, Item.ItemSlot.WEAPON,
                4, 0, 0, 0, 0);
    }

    public static Item darkBlade() {
        return new Item("weapon_dark", "Скованный тьмой меч",
                "Холодно блестит.", "⚔️", Item.ItemType.WEAPON, Item.ItemSlot.WEAPON,
                8, 0, 0, 0, 0);
    }

    public static Item kingFlame() {
        return new Item("weapon_flame", "Пламя короля",
                "Пылающий клинок.", "🔥", Item.ItemType.WEAPON, Item.ItemSlot.WEAPON,
                12, 0, 0, 0, 0);
    }

    // --- Броня: сет разбойника ---
    public static Item rogueHelmet() {
        return new Item("armor_rogue_helmet", "Шлем разбойника",
                "Лёгкая защита головы.", "🪖", Item.ItemType.ARMOR, Item.ItemSlot.HELMET,
                0, 1, 0, 0, 0);
    }

    public static Item rogueJacket() {
        return new Item("armor_rogue_body", "Куртка разбойника",
                "Прочная кожаная куртка.", "🧥", Item.ItemType.ARMOR, Item.ItemSlot.BODY,
                0, 2, 0, 0, 0);
    }

    public static Item rogueLegs() {
        return new Item("armor_rogue_legs", "Штаны разбойника",
                "Подвижность важнее.", "👖", Item.ItemType.ARMOR, Item.ItemSlot.LEGS,
                0, 1, 0, 0, 0);
    }

    public static Item rogueBoots() {
        return new Item("armor_rogue_boots", "Ботинки разбойника",
                "Тихая поступь.", "🥾", Item.ItemType.ARMOR, Item.ItemSlot.BOOTS,
                0, 1, 0, 0, 0);
    }

    // --- Броня: защитник Мидгарда ---
    public static Item midgardHelmet() {
        return new Item("armor_midgard_helmet", "Шлем защитника Мидгарда",
                "Отражает удары.", "🪖", Item.ItemType.ARMOR, Item.ItemSlot.HELMET,
                0, 2, 0, 0, 0);
    }

    public static Item midgardArmor() {
        return new Item("armor_midgard_body", "Доспех защитника Мидгарда",
                "Сталь и руны.", "🛡️", Item.ItemType.ARMOR, Item.ItemSlot.BODY,
                0, 4, 0, 0, 0);
    }

    public static Item midgardLegs() {
        return new Item("armor_midgard_legs", "Штаны защитника Мидгарда",
                "Закалённые пластины.", "👖", Item.ItemType.ARMOR, Item.ItemSlot.LEGS,
                0, 2, 0, 0, 0);
    }

    public static Item midgardBoots() {
        return new Item("armor_midgard_boots", "Ботинки защитника Мидгарда",
                "Стойко держат землю.", "🥾", Item.ItemType.ARMOR, Item.ItemSlot.BOOTS,
                0, 2, 0, 0, 0);
    }

    // --- Броня: шагнувший в тень ---
    public static Item shadowHelmet() {
        return new Item("armor_shadow_helmet", "Шлем шагнувшего в тень",
                "Закрывает лицо.", "🪖", Item.ItemType.ARMOR, Item.ItemSlot.HELMET,
                0, 3, 0, 0, 0);
    }

    public static Item shadowArmor() {
        return new Item("armor_shadow_body", "Доспех шагнувшего в тень",
                "Поглощает свет.", "🛡️", Item.ItemType.ARMOR, Item.ItemSlot.BODY,
                0, 6, 0, 0, 0);
    }

    public static Item shadowLegs() {
        return new Item("armor_shadow_legs", "Штаны шагнувшего в тень",
                "Гасит звук шагов.", "👖", Item.ItemType.ARMOR, Item.ItemSlot.LEGS,
                0, 3, 0, 0, 0);
    }

    public static Item shadowBoots() {
        return new Item("armor_shadow_boots", "Ботинки шагнувшего в тень",
                "Тень под ногами.", "🥾", Item.ItemType.ARMOR, Item.ItemSlot.BOOTS,
                0, 3, 0, 0, 0);
    }

    public static Item bossKey() {
        return new Item("key_boss", "Ключ босса", "Открывает запертую дверь.", "🗝️",
                Item.ItemType.KEY, Item.ItemSlot.NONE, 0, 0, 0, 0, 0);
    }

    public static List<Item> randomChestLoot() {
        List<Item> loot = new ArrayList<>();
        int roll = RANDOM.nextInt(100);
        if (roll < 40) {
            loot.add(rogueSword());
        } else if (roll < 70) {
            loot.add(darkBlade());
        } else {
            loot.add(kingFlame());
        }

        if (RANDOM.nextBoolean()) {
            loot.add(randomArmorPiece());
        }
        return loot;
    }

    public static List<Item> randomBagLoot() {
        List<Item> loot = new ArrayList<>();
        int roll = RANDOM.nextInt(100);
        if (roll < 40) {
            loot.add(weakHealScroll());
        } else if (roll < 70) {
            loot.add(healScroll());
        } else {
            loot.add(strongHealScroll());
        }

        int manaRoll = RANDOM.nextInt(100);
        if (manaRoll < 40) {
            loot.add(smallManaPotion());
        } else if (manaRoll < 70) {
            loot.add(mediumManaPotion());
        } else {
            loot.add(largeManaPotion());
        }
        return loot;
    }

    public static Item randomArmorPiece() {
        int roll = RANDOM.nextInt(100);
        if (roll < 40) {
            return randomFromList(rogueArmorSet());
        } else if (roll < 75) {
            return randomFromList(midgardArmorSet());
        } else {
            return randomFromList(shadowArmorSet());
        }
    }

    private static Item randomFromList(List<Item> items) {
        return items.get(RANDOM.nextInt(items.size()));
    }

    private static List<Item> rogueArmorSet() {
        List<Item> list = new ArrayList<>();
        list.add(rogueHelmet());
        list.add(rogueJacket());
        list.add(rogueLegs());
        list.add(rogueBoots());
        return list;
    }

    private static List<Item> midgardArmorSet() {
        List<Item> list = new ArrayList<>();
        list.add(midgardHelmet());
        list.add(midgardArmor());
        list.add(midgardLegs());
        list.add(midgardBoots());
        return list;
    }

    private static List<Item> shadowArmorSet() {
        List<Item> list = new ArrayList<>();
        list.add(shadowHelmet());
        list.add(shadowArmor());
        list.add(shadowLegs());
        list.add(shadowBoots());
        return list;
    }
}