package net.lawliet.companionNotification.config;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ExperienceNeededType implements StringRepresentable {
    NONE("NONE"),
    EXACT("EXACT"),
    RANGE("RANGE");

    private final String name;

    ExperienceNeededType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @NotNull
    @Override
    public String getSerializedName() {
        return this.name;
    }
}
