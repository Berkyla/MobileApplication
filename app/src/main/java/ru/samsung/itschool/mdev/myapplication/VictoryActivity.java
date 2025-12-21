package ru.samsung.itschool.mdev.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VictoryActivity extends AppCompatActivity {

    private SaveManager saveManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_victory);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.victory_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        saveManager = new SaveManager(this);
        saveManager.clear();

        Button btnMenu = findViewById(R.id.btnVictoryMenu);
        Button btnNewGame = findViewById(R.id.btnVictoryNewGame);

        btnMenu.setOnClickListener(v -> goToMenu());
        btnNewGame.setOnClickListener(v -> startNewGame());
    }

    private void goToMenu() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void startNewGame() {
        saveManager.clear();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mode", "new");
        startActivity(intent);
        finish();
    }
}