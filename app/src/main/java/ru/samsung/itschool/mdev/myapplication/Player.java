package ru.samsung.itschool.mdev.myapplication;

public class Player {

    private int maxHealth;
    private int health;
    private int maxMana;
    private int mana;
    private int baseDamage;
    private int baseDefense;
    private int coins;
    private Item weapon;
    private Item helmet;
    private Item body;
    private Item legs;
    private Item boots;
    private java.util.List<Item> inventory;

    public Player() {
        this.maxHealth = 100;
        this.health = 100;
        this.maxMana = 50;
        this.mana = 50;
        this.baseDamage = 10;
        this.baseDefense = 1;
        this.coins = 0;
        this.inventory = new java.util.ArrayList<>();
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, maxHealth));
    }

    public int getDamage() {
        return getTotalDamage();
    }

    public int getTotalDamage() {
        int weaponBonus = weapon != null ? weapon.getDamageBonus() : 0;
        return baseDamage + weaponBonus;
    }

    public int takeDamage(int dmg) {
        int finalDamage = Math.max(1, dmg - getTotalDefense());
        health -= finalDamage;
        if (health < 0) {
            health = 0;
        }
        return finalDamage;
    }

    public int takePureDamage(int dmg) {
        int finalDamage = Math.max(1, dmg - getTotalDefense());
        health -= finalDamage;
        if (health < 0) {
            health = 0;
        }
        return finalDamage;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public int heal(int amount) {
        int before = health;
        health += amount;
        if (health > maxHealth) {
            health = maxHealth;
        }
        return health - before;
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

    public void setMana(int mana) {
        this.mana = Math.max(0, Math.min(mana, maxMana));
    }

    public void spendMana(int amount) {
        mana -= amount;
        if (mana < 0) {
            mana = 0;
        }
    }

    public int restoreMana(int amount) {
        int before = mana;
        mana += amount;
        if (mana > maxMana) {
            mana = maxMana;
        }
        return mana - before;
    }

    public void addCoins(int amount) {
        coins += amount;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = Math.max(0, coins);
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    public java.util.List<Item> getInventory() {
        return inventory;
    }

    public void setInventory(java.util.List<Item> inventory) {
        this.inventory = inventory != null ? inventory : new java.util.ArrayList<>();
    }

    public void clearInventory() {
        this.inventory.clear();
    }

    public void removeKeyItems() {
        java.util.Iterator<Item> iterator = inventory.iterator();
        while (iterator.hasNext()) {
            Item current = iterator.next();
            if (current.getType() == Item.ItemType.KEY) {
                iterator.remove();
            }
        }
    }

    public Item getWeapon() {
        return weapon;
    }

    public Item getHelmet() {
        return helmet;
    }

    public Item getBody() {
        return body;
    }

    public Item getLegs() {
        return legs;
    }

    public Item getBoots() {
        return boots;
    }

    public void setWeapon(Item weapon) {
        this.weapon = weapon;
    }

    public void setHelmet(Item helmet) {
        this.helmet = helmet;
    }

    public void setBody(Item body) {
        this.body = body;
    }

    public void setLegs(Item legs) {
        this.legs = legs;
    }

    public void setBoots(Item boots) {
        this.boots = boots;
    }

    public int getTotalDefense() {
        int defense = baseDefense;
        defense += helmet != null ? helmet.getDefenseBonus() : 0;
        defense += body != null ? body.getDefenseBonus() : 0;
        defense += legs != null ? legs.getDefenseBonus() : 0;
        defense += boots != null ? boots.getDefenseBonus() : 0;
        return defense;
    }

    public void equip(Item item) {
        if (item == null || item.getSlot() == Item.ItemSlot.NONE) return;

        switch (item.getSlot()) {
            case WEAPON:
                if (weapon != null) {
                    inventory.add(weapon);
                }
                weapon = item;
                break;
            case HELMET:
                if (helmet != null) {
                    inventory.add(helmet);
                }
                helmet = item;
                break;
            case BODY:
                if (body != null) {
                    inventory.add(body);
                }
                body = item;
                break;
            case LEGS:
                if (legs != null) {
                    inventory.add(legs);
                }
                legs = item;
                break;
            case BOOTS:
                if (boots != null) {
                    inventory.add(boots);
                }
                boots = item;
                break;
            default:
                break;
        }
        inventory.remove(item);
    }

    public boolean useConsumable(Item item) {
        if (item == null || !item.isConsumable()) return false;
        if (item.getType() == Item.ItemType.CONSUMABLE_HP) {
            heal(item.getHealAmount());
        } else if (item.getType() == Item.ItemType.CONSUMABLE_MANA) {
            restoreMana(item.getManaRestore());
        }
        inventory.remove(item);
        return true;
    }

    public void collectCoinsItem(int amount) {
        coins += amount;
    }

    public void restoreFull() {
        this.health = maxHealth;
        this.mana = maxMana;
    }
}