package com.codingame.game;

public class Ability {
    public enum Type {
        SPEED, SET_ROCK, SET_PAPER, SET_SCISSORS;
    }

    private Type type;
    private int cooldown;

    public Ability(Type type) {
        this.type = type;
        this.cooldown = 0;
    }

    public Ability(Ability p) {
        this.type = p.type;
        this.cooldown = 0;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public void tickCooldown() {
        if (cooldown > 0) {
            cooldown--;
        }
    }
}
