package ru.hubsmc.ru.hubschesterton;

public enum Permissions {

    HELP("chesterton.help"),
    RELOAD("chesterton.reload"),
    OPEN("chesterton.send");

    private final String perm;

    Permissions(String perm) {
        this.perm = perm;
    }

    public String getPermission() {
        return perm;
    }

}

