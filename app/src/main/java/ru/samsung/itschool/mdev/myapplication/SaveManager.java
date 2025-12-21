package ru.samsung.itschool.mdev.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveManager {

    private static final String PREFS = "game_save";
    private static final String KEY_DATA = "data";
    private static final String KEY_HAS_SAVE = "has_save";

    private final SharedPreferences preferences;

    public SaveManager(Context context) {
        this.preferences = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public boolean hasSave() {
        return preferences.getBoolean(KEY_HAS_SAVE, false) && preferences.contains(KEY_DATA);
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    public void save(GameEngine engine) {
        if (engine == null || engine.getCurrentRoom() == null) return;

        try {
            JSONObject root = new JSONObject();
            Floor floor = engine.getCurrentFloor();
            Player player = engine.getPlayer();
            Room[][] grid = floor.getGrid();

            root.put("floorIndex", floor.getIndex());
            root.put("playerHealth", player.getHealth());
            root.put("playerMana", player.getMana());
            root.put("coins", player.getCoins());
            root.put("posX", engine.getCurrentRoom().getX());
            root.put("posY", engine.getCurrentRoom().getY());
            root.put("keyFound", floor.isKeyFound());
            root.put("bossDefeated", floor.isBossDefeated());
            root.put("completed", engine.isAdventureCompleted());

            root.put("visited", encodeGrid(grid, GridField.VISITED));
            root.put("chestOpened", encodeGrid(grid, GridField.CHEST_OPENED));
            root.put("bagOpened", encodeGrid(grid, GridField.BAG_OPENED));
            root.put("enemyDead", encodeGrid(grid, GridField.ENEMY_DEAD));

            int keyFloor = -1;
            for (Item item : player.getInventory()) {
                if (item.getType() == Item.ItemType.KEY) {
                    keyFloor = floor.getIndex();
                    break;
                }
            }
            root.put("keyFloor", keyFloor);

            root.put("inventory", encodeInventory(player.getInventory()));
            root.put("equipment", encodeEquipment(player));

            preferences.edit()
                    .putString(KEY_DATA, root.toString())
                    .putBoolean(KEY_HAS_SAVE, true)
                    .apply();
        } catch (JSONException e) {
        }
    }

    public SavedState load() {
        if (!hasSave()) {
            return null;
        }
        String raw = preferences.getString(KEY_DATA, null);
        if (raw == null || raw.isEmpty()) {
            return null;
        }
        try {
            JSONObject root = new JSONObject(raw);
            SavedState state = new SavedState();
            state.floorIndex = root.optInt("floorIndex", 1);
            state.playerHealth = root.optInt("playerHealth", 100);
            state.playerMana = root.optInt("playerMana", 50);
            state.coins = root.optInt("coins", 0);
            state.posX = root.optInt("posX", 0);
            state.posY = root.optInt("posY", 0);
            state.keyFound = root.optBoolean("keyFound", false);
            state.bossDefeated = root.optBoolean("bossDefeated", false);
            state.keyFloor = root.optInt("keyFloor", -1);
            state.adventureCompleted = root.optBoolean("completed", false);

            state.visited = decodeGrid(root.optJSONArray("visited"));
            state.chestOpened = decodeGrid(root.optJSONArray("chestOpened"));
            state.bagOpened = decodeGrid(root.optJSONArray("bagOpened"));
            state.enemyDead = decodeGrid(root.optJSONArray("enemyDead"));

            state.inventory = decodeInventory(root.optJSONArray("inventory"));
            state.equipment = decodeEquipment(root.optJSONObject("equipment"));
            return state;
        } catch (JSONException e) {
            return null;
        }
    }

    private JSONArray encodeGrid(Room[][] grid, GridField field) throws JSONException {
        JSONArray rows = new JSONArray();
        for (int y = 0; y < grid.length; y++) {
            JSONArray row = new JSONArray();
            for (int x = 0; x < grid[y].length; x++) {
                Room room = grid[y][x];
                boolean value = false;
                if (room != null) {
                    switch (field) {
                        case VISITED:
                            value = room.isVisited();
                            break;
                        case CHEST_OPENED:
                            value = room.isChestOpened();
                            break;
                        case BAG_OPENED:
                            value = room.isLootCollected();
                            break;
                        case ENEMY_DEAD:
                            value = room.getEnemy() == null || room.getEnemy().isDead();
                            break;
                    }
                }
                row.put(value);
            }
            rows.put(row);
        }
        return rows;
    }

    private boolean[][] decodeGrid(JSONArray array) throws JSONException {
        if (array == null) return null;
        boolean[][] result = new boolean[array.length()][];
        for (int y = 0; y < array.length(); y++) {
            JSONArray row = array.optJSONArray(y);
            if (row == null) continue;
            result[y] = new boolean[row.length()];
            for (int x = 0; x < row.length(); x++) {
                result[y][x] = row.optBoolean(x, false);
            }
        }
        return result;
    }

    private JSONArray encodeInventory(List<Item> items) throws JSONException {
        Map<String, Integer> counts = new HashMap<>();
        for (Item item : items) {
            String id = item.getId();
            counts.put(id, counts.getOrDefault(id, 0) + 1);
        }
        JSONArray array = new JSONArray();
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            JSONObject obj = new JSONObject();
            obj.put("id", entry.getKey());
            obj.put("count", entry.getValue());
            array.put(obj);
        }
        return array;
    }

    private List<ItemStack> decodeInventory(JSONArray array) throws JSONException {
        List<ItemStack> list = new ArrayList<>();
        if (array == null) return list;
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.optJSONObject(i);
            if (obj == null) continue;
            String id = obj.optString("id", "");
            int count = obj.optInt("count", 1);
            if (id.isEmpty()) continue;
            list.add(new ItemStack(id, count));
        }
        return list;
    }

    private JSONObject encodeEquipment(Player player) throws JSONException {
        JSONObject obj = new JSONObject();
        if (player.getWeapon() != null) obj.put("weapon", player.getWeapon().getId());
        if (player.getHelmet() != null) obj.put("helmet", player.getHelmet().getId());
        if (player.getBody() != null) obj.put("body", player.getBody().getId());
        if (player.getLegs() != null) obj.put("legs", player.getLegs().getId());
        if (player.getBoots() != null) obj.put("boots", player.getBoots().getId());
        return obj;
    }

    private EquipmentState decodeEquipment(JSONObject obj) {
        if (obj == null) return new EquipmentState();
        EquipmentState state = new EquipmentState();
        state.weapon = obj.optString("weapon", null);
        state.helmet = obj.optString("helmet", null);
        state.body = obj.optString("body", null);
        state.legs = obj.optString("legs", null);
        state.boots = obj.optString("boots", null);
        return state;
    }

    private enum GridField {
        VISITED,
        CHEST_OPENED,
        BAG_OPENED,
        ENEMY_DEAD
    }

    public static class SavedState {
        public int floorIndex;
        public int playerHealth;
        public int playerMana;
        public int coins;
        public int posX;
        public int posY;
        public boolean keyFound;
        public boolean bossDefeated;
        public boolean adventureCompleted;
        public int keyFloor;
        public boolean[][] visited;
        public boolean[][] chestOpened;
        public boolean[][] bagOpened;
        public boolean[][] enemyDead;
        public List<ItemStack> inventory;
        public EquipmentState equipment;
    }

    public static class ItemStack {
        public final String id;
        public final int count;

        public ItemStack(String id, int count) {
            this.id = id;
            this.count = count;
        }
    }

    public static class EquipmentState {
        public String weapon;
        public String helmet;
        public String body;
        public String legs;
        public String boots;
    }
}
