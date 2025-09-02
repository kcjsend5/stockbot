package io.github.kcjsend5.stockbot.type;

public enum Role {
    USER("ROLE_USER"),
    MANAGER("ROLE_MANAGER"),
    DEVELOPER("ROLE_DEVELOPER");

    private final String name;

    Role(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
