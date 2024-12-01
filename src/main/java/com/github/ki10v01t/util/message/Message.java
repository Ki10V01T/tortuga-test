package com.github.ki10v01t.util.message;

import java.io.Serializable;

import com.github.ki10v01t.util.Command;

public abstract class Message implements Serializable{
    private Command command;
    private String text;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Command getCommand() {
        return command;
    }

    public String getText() {
        return text;
    }
}
