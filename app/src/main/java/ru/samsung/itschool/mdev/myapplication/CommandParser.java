package ru.samsung.itschool.mdev.myapplication;

public class CommandParser {

    private GameEngine engine;

    public CommandParser(GameEngine engine) {
        this.engine = engine;
    }

    public String handle(String input) {
        return engine.process(input);
    }
}