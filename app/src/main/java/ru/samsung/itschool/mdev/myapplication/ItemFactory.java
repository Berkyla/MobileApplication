package ru.samsung.itschool.mdev.myapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemFactory {

    private static final Random DEFAULT_RANDOM = new Random(51234L);

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

    public static Item rogueHelmet() {
        return new Item("armor_rogue_helmet", "Шлем разбойника",
                "Лёгкая защита головы.", "🧢", Item.ItemType.ARMOR, Item.ItemSlot.HELMET,
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

    public static Item midgardHelmet() {
        return new Item("armor_midgard_helmet", "Шлем защитника Мидгарда",
                "Отражает удары.", "⚔️🛡️", Item.ItemType.ARMOR, Item.ItemSlot.HELMET,
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

    public static Item randomHealForFloor(int floor) {
        return randomHealForFloor(null, floor);
    }

    public static Item randomHealForFloor(Random random, int floor) {
        Random rng = random != null ? random : DEFAULT_RANDOM;
        if (floor <= 1) {
            return rng.nextBoolean() ? weakHealScroll() : healScroll();
        } else if (floor == 2) {
            return rng.nextBoolean() ? healScroll() : strongHealScroll();
        } else {
            return strongHealScroll();
        }
    }

    public static Item randomManaForFloor(int floor) {
        return randomManaForFloor(null, floor);
    }

    public static Item randomManaForFloor(Random random, int floor) {
        Random rng = random != null ? random : DEFAULT_RANDOM;
        if (floor <= 1) {
            return rng.nextBoolean() ? smallManaPotion() : mediumManaPotion();
        } else if (floor == 2) {
            return rng.nextBoolean() ? mediumManaPotion() : largeManaPotion();
        } else {
            return largeManaPotion();
        }
    }

    public static Item randomBossReward(int floor) {
        return randomBossReward(null, floor);
    }

    public static Item randomBossReward(Random random, int floor) {
        Random rng = random != null ? random : DEFAULT_RANDOM;
        if (floor == 1) {
            return rng.nextBoolean() ? darkBlade() : randomFromList(rogueArmorSet(), rng);
        } else if (floor == 2) {
            return rng.nextBoolean() ? kingFlame() : randomFromList(midgardArmorSet(), rng);
        } else {
            return rng.nextBoolean() ? kingFlame() : randomFromList(shadowArmorSet(), rng);
        }
    }

    public static Item randomArmorForFloor(int floor) {
        return randomArmorForFloor(null, floor);
    }

    public static Item randomArmorForFloor(Random random, int floor) {
        Random rng = random != null ? random : DEFAULT_RANDOM;
        if (floor == 1) {
            return randomFromList(rogueArmorSet(), rng);
        } else if (floor == 2) {
            return randomFromList(midgardArmorSet(), rng);
        } else {
            return randomFromList(shadowArmorSet(), rng);
        }
    }

    public static Item bossKey() {
        return new Item("key_boss", "Ключ босса", "Открывает запертую дверь.", "🗝️",
                Item.ItemType.KEY, Item.ItemSlot.NONE, 0, 0, 0, 0, 0);
    }

    public static List<Item> randomChestLoot() {
        return randomChestLoot(null);
    }

    public static List<Item> randomChestLoot(Random random) {
        Random rng = random != null ? random : DEFAULT_RANDOM;
        List<Item> loot = new ArrayList<>();
        int roll = rng.nextInt(100);
        if (roll < 40) {
            loot.add(rogueSword());
        } else if (roll < 70) {
            loot.add(darkBlade());
        } else {
            loot.add(kingFlame());
        }

        if (rng.nextBoolean()) {
            loot.add(randomArmorPiece(rng));
        }
        return loot;
    }

    public static List<Item> randomBagLoot() {
        return randomBagLoot(null);
    }

    public static List<Item> randomBagLoot(Random random) {
        Random rng = random != null ? random : DEFAULT_RANDOM;
        List<Item> loot = new ArrayList<>();
        int roll = rng.nextInt(100);
        if (roll < 40) {
            loot.add(weakHealScroll());
        } else if (roll < 70) {
            loot.add(healScroll());
        } else {
            loot.add(strongHealScroll());
        }

        int manaRoll = rng.nextInt(100);
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
        return randomArmorPiece(null);
    }

    public static Item randomArmorPiece(Random random) {
        Random rng = random != null ? random : DEFAULT_RANDOM;
        int roll = rng.nextInt(100);
        if (roll < 40) {
            return randomFromList(rogueArmorSet(), rng);
        } else if (roll < 75) {
            return randomFromList(midgardArmorSet(), rng);
        } else {
            return randomFromList(shadowArmorSet(), rng);
        }
    }

    private static Item randomFromList(List<Item> items, Random random) {
        Random rng = random != null ? random : DEFAULT_RANDOM;
        return items.get(rng.nextInt(items.size()));
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

    public static Item fromId(String id) {
        switch (id) {
            case "scroll_heal_small": return weakHealScroll();
            case "scroll_heal_medium": return healScroll();
            case "scroll_heal_big": return strongHealScroll();
            case "potion_mana_small": return smallManaPotion();
            case "potion_mana_medium": return mediumManaPotion();
            case "potion_mana_large": return largeManaPotion();
            case "weapon_rogue": return rogueSword();
            case "weapon_dark": return darkBlade();
            case "weapon_flame": return kingFlame();
            case "armor_rogue_helmet": return rogueHelmet();
            case "armor_rogue_body": return rogueJacket();
            case "armor_rogue_legs": return rogueLegs();
            case "armor_rogue_boots": return rogueBoots();
            case "armor_midgard_helmet": return midgardHelmet();
            case "armor_midgard_body": return midgardArmor();
            case "armor_midgard_legs": return midgardLegs();
            case "armor_midgard_boots": return midgardBoots();
            case "armor_shadow_helmet": return shadowHelmet();
            case "armor_shadow_body": return shadowArmor();
            case "armor_shadow_legs": return shadowLegs();
            case "armor_shadow_boots": return shadowBoots();
            case "key_boss": return bossKey();
            default:
                return null;
        }
    }
}