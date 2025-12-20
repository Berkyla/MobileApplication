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
    private Button btnMove;
    private Button btnInventory;

    private Button btnMoveForward;
    private Button btnMoveBack;
    private Button btnMoveLeft;
    private Button btnMoveRight;
    private Button btnReturnFromMove;

    private Button btnInventoryBack;

    private CommandParser parser;

    private LinearLayout mainMenuContainer;
    private LinearLayout moveMenuContainer;
    private LinearLayout inventoryMenuContainer;
    private TextView subtitleText;
    private ProgressBar hpBar;
    private ProgressBar manaBar;
    private TextView hpValue;
    private TextView manaValue;
    private GridLayout minimapGrid;
    private GameEngine engine;

    private UiMode currentMode = null;

    private enum UiMode {
        MAIN,
        MOVE,
        INVENTORY
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
        subtitleText = findViewById(R.id.subtitleText);
        hpBar = findViewById(R.id.hpBar);
        manaBar = findViewById(R.id.manaBar);
        hpValue = findViewById(R.id.hpValue);
        manaValue = findViewById(R.id.manaValue);
        minimapGrid = findViewById(R.id.minimapGrid);

        btnHelp = findViewById(R.id.btnHelp);
        btnLook = findViewById(R.id.btnLook);
        btnAttack = findViewById(R.id.btnAttack);
        btnMove = findViewById(R.id.btnMove);
        btnInventory = findViewById(R.id.btnInventory);

        btnMoveForward = findViewById(R.id.btnMoveForward);
        btnMoveBack = findViewById(R.id.btnMoveBack);
        btnMoveLeft = findViewById(R.id.btnMoveLeft);
        btnMoveRight = findViewById(R.id.btnMoveRight);
        btnReturnFromMove = findViewById(R.id.btnReturnFromMove);

        btnInventoryBack = findViewById(R.id.btnInventoryBack);

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

        btnMove.setOnClickListener(v -> {
            animateButtonPress(v);
            appendAction("ИДТИ");
            renderMenu(UiMode.MOVE);
        });

        btnInventory.setOnClickListener(v -> {
            animateButtonPress(v);
            appendAction("ИНВЕНТАРЬ");
            appendLog("Инвентарь пока не реализован");
            renderMenu(UiMode.INVENTORY);
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