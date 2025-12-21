package ru.samsung.itschool.mdev.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainMenuActivity extends AppCompatActivity {

    private SaveManager saveManager;
    private Button btnContinue;
    private Button btnNewGame;
    private TextView versionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.menu_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        saveManager = new SaveManager(this);
        btnContinue = findViewById(R.id.btnContinue);
        btnNewGame = findViewById(R.id.btnNewGame);
        versionText = findViewById(R.id.versionText);

        versionText.setText("v1.0");

        btnNewGame.setOnClickListener(v -> startNewGame());
        btnContinue.setOnClickListener(v -> startContinue());

        refreshButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshButtons();
    }

    private void refreshButtons() {
        boolean hasSave = saveManager.hasSave();
        btnContinue.setEnabled(hasSave);
        btnContinue.setAlpha(hasSave ? 1f : 0.5f);
    }

    private void startNewGame() {
        saveManager.clear();
        launchGame("new");
    }

    private void startContinue() {
        if (!saveManager.hasSave()) {
            return;
        }
        launchGame("continue");
    }

    private void launchGame(String mode) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("mode", mode);
        startActivity(intent);
    }
}