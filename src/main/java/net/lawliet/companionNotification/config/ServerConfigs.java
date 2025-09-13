package net.lawliet.companionNotification.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<ExperienceNeededType> SHOW_EXPERIENCE_MESSAGE_TYPE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SHOW_DISCOUNT;

    static {
        BUILDER.push("Server Config for Vault Hunters Companion Notification Mod");

        SHOW_EXPERIENCE_MESSAGE_TYPE = BUILDER
                .comment(
                        "Configure whether should show the experience needed or not!",
                        "NONE - Doesn't show any message related to experience needed.",
                        "EXACT - Shows exact amount of experience needed before the companion egg spawns",
                        "RANGE - Shows a range of experience needed to spawn egg"
                )
                .defineEnum("EXPERIENCE NEEDED MESSAGE TYPE", ExperienceNeededType.NONE);

        SHOW_DISCOUNT = BUILDER.comment(
                "Configures whether to show discount amount or not"
                )
                .define("SHOW PITY AMOUNT", false);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
