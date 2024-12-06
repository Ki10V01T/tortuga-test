package com.github.ki10v01t.util.message;

import java.io.Serializable;

public class Message implements Serializable{
    //private static final long serialVersionUID = 1L;
    private Command command;
    private Type type;
    private String text; 
    private Integer row;
    private Integer column;

    private Message(MessageBuilder messageBuilder) {
        this.command = messageBuilder.command;
        this.type = messageBuilder.type;
        this.text = messageBuilder.text;
        this.row = messageBuilder.row;
        this.column = messageBuilder.column;
    }

    public static class MessageBuilder {
        private Command command;
        private Type type;
        private String text;
        private Integer row;
        private Integer column;

        public MessageBuilder(Type type) {
            this.type = type;
        }

        public MessageBuilder setCommand(Command command) {
            this.command = command;
            return this;
        }
        public MessageBuilder setText(String text) {
            this.text = text;
            return this;
        }
        public MessageBuilder setColumn(Integer column) {
            this.column = column;
            return this;
        }
    
        public MessageBuilder setRow(Integer row) {
            this.row = row;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }

    public Type getType() {
        return type;
    }

    public Command getCommand() {
        return command;
    }

    public String getText() {
        return text;
    }

    public Integer getColumn() {
        return column;
    }

    public Integer getRow() {
        return row;
    }
}
