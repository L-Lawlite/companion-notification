package net.lawliet.companionNotification.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<ExperienceNeededType> SHOW_EXPERIENCE_MESSAGE_TYPE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_DISCOUNT;

    static {
        BUILDER.push("Common Config for Vault Hunters Companion Notification Mod");

        SHOW_EXPERIENCE_MESSAGE_TYPE = BUILDER
                .comment("Configure whether should show the experience needed or not!")
                .comment("Can take values of NONE,EXACT,RANGE")
                .comment("NONE - Doesnt show any message related to experience needed.")
                .comment("EXACT - Shows exact amount of experience needed before the companion egg spawns")
                .comment("RANGE - Shows a range of experience needed to spawn egg")
                .comment("Default value - NONE")
                .defineEnum("EXPERIENCE NEEDED MESSAGE TYPE", ExperienceNeededType.NONE);
        SHOW_DISCOUNT = BUILDER.comment("Configures whether to show discount amount or not")
                .comment("Default value - false")
                .define("SHOW PITY AMOUNT", false);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
