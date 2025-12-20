package ru.samsung.itschool.mdev.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView gameLog;
    private EditText commandInput;
    private Button sendButton;

    private CommandParser parser;

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
        gameLog = findViewById(R.id.gameLog);
        commandInput = findViewById(R.id.commandInput);
        sendButton = findViewById(R.id.sendButton);

        // Создаём игровой движок и парсер
        GameEngine engine = new GameEngine();
        parser = new CommandParser(engine);

        // Обработчик кнопки
        sendButton.setOnClickListener(v -> {
            String cmd = commandInput.getText().toString().trim();
            if (!cmd.isEmpty()) {
                appendLog("> " + cmd + "\n");
                String result = parser.handle(cmd);
                appendLog(result + "\n");
                commandInput.setText("");
            }
        });
    }

    private void appendLog(String text) {
        gameLog.append(text);
    }
}