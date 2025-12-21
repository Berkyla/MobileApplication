package ru.samsung.itschool.mdev.myapplication;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private LinearLayout logContainer;

    private Button btnHelp;
    private Button btnLook;
    private Button btnAttack;
    private Button btnMagic;
    private Button btnMove;
    private Button btnInventory;
    private Button btnOpen;

    private Button btnMoveForward;
    private Button btnMoveBack;
    private Button btnMoveLeft;
    private Button btnMoveRight;
    private Button btnReturnFromMove;

    private Button btnInventoryBack;
    private Button btnMagicBack;
    private Button btnCastIceShard;

    private CommandParser parser;

    private LinearLayout mainMenuContainer;
    private LinearLayout moveMenuContainer;
    private LinearLayout inventoryMenuContainer;
    private LinearLayout magicMenuContainer;
    private TextView subtitleText;
    private ProgressBar hpBar;
    private ProgressBar manaBar;
    private TextView hpValue;
    private TextView manaValue;
    private GridLayout minimapGrid;
    private GameEngine engine;
    private LinearLayout inventoryList;
    private TextView inventoryEmpty;
    private TextView equipmentWeapon;
    private TextView equipmentHelmet;
    private TextView equipmentBody;
    private TextView equipmentLegs;
    private TextView equipmentBoots;
    private TextView equipmentDamage;
    private TextView equipmentDefense;
    private TextView equipmentCoins;

    private UiMode currentMode = null;

    private enum UiMode {
        MAIN,
        MOVE,
        INVENTORY,
        MAGIC
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Edge-to-edge обработка
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Подключение элементов интерфейса
        scrollView = findViewById(R.id.scrollView);
        logContainer = findViewById(R.id.logContainer);

        mainMenuContainer = findViewById(R.id.mainMenuContainer);
        moveMenuContainer = findViewById(R.id.moveMenuContainer);
        inventoryMenuContainer = findViewById(R.id.inventoryMenuContainer);
        magicMenuContainer = findViewById(R.id.magicMenuContainer);
        subtitleText = findViewById(R.id.subtitleText);
        hpBar = findViewById(R.id.hpBar);
        manaBar = findViewById(R.id.manaBar);
        hpValue = findViewById(R.id.hpValue);
        manaValue = findViewById(R.id.manaValue);
        minimapGrid = findViewById(R.id.minimapGrid);
        inventoryList = findViewById(R.id.inventoryList);
        inventoryEmpty = findViewById(R.id.inventoryEmpty);
        equipmentWeapon = findViewById(R.id.equipmentWeapon);
        equipmentHelmet = findViewById(R.id.equipmentHelmet);
        equipmentBody = findViewById(R.id.equipmentBody);
        equipmentLegs = findViewById(R.id.equipmentLegs);
        equipmentBoots = findViewById(R.id.equipmentBoots);
        equipmentDamage = findViewById(R.id.equipmentDamage);
        equipmentDefense = findViewById(R.id.equipmentDefense);
        equipmentCoins = findViewById(R.id.equipmentCoins);

        btnHelp = findViewById(R.id.btnHelp);
        btnLook = findViewById(R.id.btnLook);
        btnAttack = findViewById(R.id.btnAttack);
        btnMagic = findViewById(R.id.btnMagic);
        btnMove = findViewById(R.id.btnMove);
        btnInventory = findViewById(R.id.btnInventory);
        btnOpen = findViewById(R.id.btnOpen);

        btnMoveForward = findViewById(R.id.btnMoveForward);
        btnMoveBack = findViewById(R.id.btnMoveBack);
        btnMoveLeft = findViewById(R.id.btnMoveLeft);
        btnMoveRight = findViewById(R.id.btnMoveRight);
        btnReturnFromMove = findViewById(R.id.btnReturnFromMove);

        btnInventoryBack = findViewById(R.id.btnInventoryBack);
        btnMagicBack = findViewById(R.id.btnMagicBack);
        btnCastIceShard = findViewById(R.id.btnCastIceShard);

        // Создаём игровой движок и парсер
        engine = new GameEngine();
        parser = new CommandParser(engine);

        btnHelp.setOnClickListener(v -> {
            animateButtonPress(v);
            sendCommand("ПОМОЩЬ", "help");
        });
        btnLook.setOnClickListener(v -> {
            animateButtonPress(v);
            sendCommand("ОСМОТРЕТЬСЯ", "look");
        });
        btnAttack.setOnClickListener(v -> {
            animateButtonPress(v);
            sendCommand("АТАКА", "attack");
        });
        btnMagic.setOnClickListener(v -> {
            animateButtonPress(v);
            appendAction("МАГИЯ");
            renderMenu(UiMode.MAGIC);
        });
        btnOpen.setOnClickListener(v -> {
            animateButtonPress(v);
            sendCommand("ОТКРЫТЬ", "open");
        });

        btnMove.setOnClickListener(v -> {
            animateButtonPress(v);
            if (engine.isInBattle()) {
                appendAction("ИДТИ");
                appendLog("Нельзя уйти во время боя.");
            } else {
                appendAction("ИДТИ");
                renderMenu(UiMode.MOVE);
            }
        });

        btnInventory.setOnClickListener(v -> {
            animateButtonPress(v);
            openInventoryMenu();
        });

        btnMoveForward.setOnClickListener(v -> {
            animateButtonPress(v);
            sendCommand("ВПЕРЁД", "go north");
        });
        btnMoveBack.setOnClickListener(v -> {
            animateButtonPress(v);
            sendCommand("НАЗАД", "go south");
        });
        btnMoveRight.setOnClickListener(v -> {
            animateButtonPress(v);
            sendCommand("ВПРАВО", "go east");
        });
        btnMoveLeft.setOnClickListener(v -> {
            animateButtonPress(v);
            sendCommand("ВЛЕВО", "go west");
        });
        btnReturnFromMove.setOnClickListener(v -> {
            animateButtonPress(v);
            appendAction("ВЕРНУТЬСЯ");
            renderMenu(UiMode.MAIN);
        });

        btnInventoryBack.setOnClickListener(v -> {
            animateButtonPress(v);
            appendAction("ВЕРНУТЬСЯ");
            renderMenu(UiMode.MAIN);
        });

        btnMagicBack.setOnClickListener(v -> {
            animateButtonPress(v);
            appendAction("НАЗАД");
            renderMenu(UiMode.MAIN);
        });

        btnCastIceShard.setOnClickListener(v -> {
            animateButtonPress(v);
            sendCommand("ЛЕДЯНОЙ ОСКОЛОК", "magic");
            renderMenu(UiMode.MAIN);
        });

        appendLog("Добро пожаловать в Arcane Frontier");
        appendLog(parser.handle("look"));
        renderMenu(UiMode.MAIN);
        updateUiFromEngine();
    }

    private void appendLog(String text) {
        addLogEntry("◆ " + text, false);
    }

    private void appendAction(String action) {
        addLogEntry("> " + action, true);
    }

    private void sendCommand(String action, String command) {
        appendAction(action);
        String result = parser.handle(command);
        appendLog(result);
        updateUiFromEngine();
    }

    private void addLogEntry(String text, boolean isPlayer) {
        TextView entry = new TextView(this);
        entry.setText(text);
        entry.setTextSize(15f);
        entry.setLineSpacing(0f, 1.12f);
        entry.setLetterSpacing(0.02f);
        entry.setTextColor(ContextCompat.getColor(this,
                isPlayer ? R.color.arcane_accent_gold : R.color.arcane_text_primary));
        entry.setBackgroundResource(isPlayer ? R.drawable.bg_log_entry_player : R.drawable.bg_log_entry_system);
        entry.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

        int horizontalPadding = dpToPx(2);
        entry.setPadding(horizontalPadding, dpToPx(4), horizontalPadding, dpToPx(4));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = dpToPx(8);
        entry.setLayoutParams(params);

        logContainer.addView(entry);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.log_fade_in);
        entry.startAnimation(animation);

        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void renderMenu(UiMode mode) {
        if (mode == currentMode) {
            return;
        }

        if (mode == UiMode.INVENTORY) {
            refreshInventoryView();
        }

        View newContainer = getContainerForMode(mode);
        View oldContainer = getContainerForMode(currentMode);

        if (oldContainer != null && oldContainer.getVisibility() == View.VISIBLE) {
            Animation out = AnimationUtils.loadAnimation(this, R.anim.menu_out);
            oldContainer.startAnimation(out);
            oldContainer.setVisibility(View.GONE);
        }

        if (newContainer != null) {
            newContainer.setVisibility(View.VISIBLE);
            Animation in = AnimationUtils.loadAnimation(this, R.anim.menu_in);
            newContainer.startAnimation(in);
        }

        currentMode = mode;
    }

    private View getContainerForMode(UiMode mode) {
        if (mode == null) return null;
        switch (mode) {
            case MAIN:
                return mainMenuContainer;
            case MOVE:
                return moveMenuContainer;
            case INVENTORY:
                return inventoryMenuContainer;
            case MAGIC:
                return magicMenuContainer;
            default:
                return null;
        }
    }

    private void animateButtonPress(View view) {
        view.animate()
                .scaleX(0.97f)
                .scaleY(0.97f)
                .setDuration(80)
                .withEndAction(() -> view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(140)
                        .start())
                .start();
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void updateUiFromEngine() {
        if (engine == null) return;

        Player player = engine.getPlayer();

        hpBar.setMax(player.getMaxHealth());
        hpBar.setProgress(player.getHealth());
        manaBar.setMax(player.getMaxMana());
        manaBar.setProgress(player.getMana());

        hpValue.setText(player.getHealth() + "/" + player.getMaxHealth());
        manaValue.setText(player.getMana() + "/" + player.getMaxMana());

        Floor floor = engine.getCurrentFloor();
        subtitleText.setText("Этаж " + floor.getIndex() + ": " + floor.getName());

        renderMinimap(engine.getCurrentFloorGrid());

        btnAttack.setEnabled(true);
        btnMagic.setEnabled(true);
        btnAttack.setAlpha(1f);
        btnMagic.setAlpha(1f);
    }

    private void refreshInventoryView() {
        Player player = engine.getPlayer();
        inventoryList.setOrientation(LinearLayout.HORIZONTAL);

        Item weapon = player.getWeapon();
        Item helmet = player.getHelmet();
        Item body = player.getBody();
        Item legs = player.getLegs();
        Item boots = player.getBoots();

        equipmentWeapon.setText("🗡 Оружие: " + (weapon != null ? weapon.getName() : "нет"));
        equipmentHelmet.setText("🪖 Голова: " + (helmet != null ? helmet.getName() : "нет"));

        String bodyEmoji = body != null && body.getId().contains("rogue") ? "🧥" : "🛡";
        String bodyPrefix = body != null && body.getId().contains("rogue") ? "Куртка: " : "Доспех: ";
        equipmentBody.setText(bodyEmoji + " Тело: " + (body != null ? bodyPrefix + body.getName() : "нет"));
        equipmentLegs.setText("👖 Ноги: " + (legs != null ? legs.getName() : "нет"));
        equipmentBoots.setText("🥾 Ботинки: " + (boots != null ? boots.getName() : "нет"));

        equipmentDamage.setText("⚔ Урон: " + player.getTotalDamage());
        equipmentDefense.setText("🛡 Защита: " + player.getTotalDefense());
        equipmentCoins.setText("🪙 Монеты: " + player.getCoins());

        inventoryList.removeAllViews();
        if (player.getInventory().isEmpty()) {
            inventoryEmpty.setVisibility(View.VISIBLE);
            return;
        }
        inventoryEmpty.setVisibility(View.GONE);

        for (Item item : player.getInventory()) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.VERTICAL);
            row.setBackgroundResource(R.drawable.bg_panel);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin = dpToPx(6);
            params.rightMargin = dpToPx(8);
            row.setPadding(dpToPx(10), dpToPx(8), dpToPx(10), dpToPx(8));
            row.setLayoutParams(params);
            row.setMinimumWidth(dpToPx(180));

            TextView title = new TextView(this);
            title.setText(item.getIcon() + " " + item.getName());
            title.setTextSize(15f);
            title.setTextColor(ContextCompat.getColor(this, R.color.arcane_text_primary));
            row.addView(title);

            TextView desc = new TextView(this);
            desc.setText(item.getDescription());
            desc.setTextSize(13f);
            desc.setTextColor(ContextCompat.getColor(this, R.color.arcane_text_secondary));
            desc.setPadding(0, dpToPx(2), 0, dpToPx(6));
            row.addView(desc);

            Button action = new Button(this);
            action.setBackgroundResource(R.drawable.bg_button_arcane);
            action.setTextColor(ContextCompat.getColor(this, R.color.arcane_text_primary));
            action.setText(getActionLabel(item));
            action.setOnClickListener(v -> {
                animateButtonPress(v);
                handleItemAction(item);
            });
            row.addView(action);

            inventoryList.addView(row);
        }
    }

    private String getActionLabel(Item item) {
        if (item.isConsumable()) {
            return "ИСПОЛЬЗОВАТЬ";
        }
        if (item.isArmor() || item.isWeapon()) {
            return "ЭКИПИРОВАТЬ";
        }
        if (item.getType() == Item.ItemType.KEY) {
            return "КЛЮЧ";
        }
        return "ПРЕДМЕТ";
    }

    private void handleItemAction(Item item) {
        appendAction("ИНВЕНТАРЬ");
        String result = engine.useItem(item);
        appendLog(result);
        updateUiFromEngine();
        refreshInventoryView();
    }

    private void openInventoryMenu() {
        appendAction("ИНВЕНТАРЬ");
        renderMenu(UiMode.INVENTORY);
        refreshInventoryView();
    }

    private void renderMinimap(Room[][] grid) {
        if (grid == null || engine == null) return;

        int rows = grid.length;
        int cols = grid[0].length;

        minimapGrid.removeAllViews();
        minimapGrid.setRowCount(rows);
        minimapGrid.setColumnCount(cols);

        Room current = engine.getCurrentRoom();

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                Room room = grid[y][x];
                View cell = new View(this);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = dpToPx(20);
                params.height = dpToPx(20);
                params.setMargins(dpToPx(1), dpToPx(1), dpToPx(1), dpToPx(1));
                params.rowSpec = GridLayout.spec(y);
                params.columnSpec = GridLayout.spec(x);
                cell.setLayoutParams(params);

                if (room == null) {
                    cell.setBackgroundResource(R.drawable.bg_minimap_empty);
                } else if (room == current) {
                    cell.setBackgroundResource(R.drawable.bg_minimap_current);
                } else if (room.isBossRoom() && room.isBossRevealed() && !room.isDiscovered()) {
                    cell.setBackgroundResource(R.drawable.bg_minimap_boss_revealed);
                } else if (!room.isDiscovered()) {
                    cell.setBackgroundResource(R.drawable.bg_minimap_unknown);
                } else if (room.isBossRoom()) {
                    cell.setBackgroundResource(R.drawable.bg_minimap_boss);
                } else if (room.isStartRoom()) {
                    cell.setBackgroundResource(R.drawable.bg_minimap_start);
                } else {
                    cell.setBackgroundResource(R.drawable.bg_minimap_discovered);
                }

                minimapGrid.addView(cell);
            }
        }
    }
}