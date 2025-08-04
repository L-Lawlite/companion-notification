package net.lawliet.companionNotification.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.companion.CompanionEggHunt;
import iskallia.vault.core.vault.companion.HuntInstance;
import iskallia.vault.core.vault.objective.ArchitectObjective;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.VaultDifficulty;
import iskallia.vault.world.data.PlayerCompanionEggData;
import iskallia.vault.world.data.PlayerVaultStatsData;
import iskallia.vault.world.data.WorldSettings;
import net.lawliet.companionNotification.config.ExperienceNeededType;
import net.lawliet.companionNotification.config.ServerConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static iskallia.vault.core.vault.companion.CompanionEggHunt.getDifficultyMultiplier;

@Mixin(value = CompanionEggHunt.class, remap = false)
public class CompanionEggHuntMixin {

    @Definition(id = "THRESHOLD", field = "Liskallia/vault/core/vault/companion/HuntInstance;THRESHOLD:Liskallia/vault/core/data/key/FieldKey;")
    @Definition(id = "set", method = "Liskallia/vault/core/vault/companion/HuntInstance;set(Liskallia/vault/core/data/key/FieldKey;Ljava/lang/Object;)Liskallia/vault/core/data/DataObject;")
    @Expression("?.set(THRESHOLD,?)")
    @Inject(method = "createHunt", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER))
    public void addCompanionNotification(ServerPlayer player, Vault vault, CallbackInfo ci, @Local(ordinal = 0) HuntInstance hunt) {
        companion_notification$sendMessageToPlayer(player, "A companion is watching you!", ChatFormatting.GREEN);

        if (ServerConfigs.SHOW_EXPERIENCE_MESSAGE_TYPE.get().equals(ExperienceNeededType.EXACT)) {
            companion_notification$sendMessageToPlayer(player, "Egg threshold is " + hunt.get(HuntInstance.THRESHOLD), ChatFormatting.BLUE);
        } else if (ServerConfigs.SHOW_EXPERIENCE_MESSAGE_TYPE.get().equals(ExperienceNeededType.RANGE)) {
            companion_notification$sendMessageToPlayer(player, "Egg threshold in range %s to %s".formatted(companion_notification$minThreshold(player,vault), companion_notification$maxThreshold(player,vault)), ChatFormatting.BLUE);
        }

        if (ServerConfigs.SHOW_DISCOUNT.get()) {
            float discount = PlayerCompanionEggData.get().get(player.getUUID()).getDiscount();
            companion_notification$sendMessageToPlayer(player, "Pity value is %s%%".formatted(discount * 100), ChatFormatting.BLUE);
        }
    }

    @Unique
    private int companion_notification$maxThreshold(ServerPlayer player, Vault vault) {
        return companion_notification$baseThreshold(player,vault,1.0F);
    }

    @Unique
    private int companion_notification$minThreshold(ServerPlayer player, Vault vault) {
        return companion_notification$baseThreshold(player,vault, 0.0F);
    }

    @Unique
    private int companion_notification$baseThreshold(ServerPlayer player, Vault vault, float randomNumber) {
        int vaultLevel = PlayerVaultStatsData.get(player.getServer()).getVaultStats(player.getUUID()).getVaultLevel();
        int base = vaultLevel * 1000;
        float discount = PlayerCompanionEggData.get().get(player.getUUID()).getDiscount();
        int modifierCount = (int) vault.get(Vault.MODIFIERS).getModifiers().stream().filter((mod) -> ModConfigs.VAULT_CRYSTAL_CATALYST.isGood(mod.getId())).count();
        int inscriptionCount = vault.get(Vault.OBJECTIVES).getAll(ArchitectObjective.class).stream().findFirst().map((obj) -> obj.get(ArchitectObjective.ROOM_ENTRIES).getTotalCount()).orElse(0);
        VaultDifficulty difficulty = WorldSettings.get(player.getLevel()).getPlayerDifficulty(player.getUUID());
        float multiplier = getDifficultyMultiplier(difficulty);
        float inscriptionAddition = (float)inscriptionCount * (float)base * 0.1F;
        float modifierAddition = (float)modifierCount * (float)base * 0.2F;
        float randomAddition = randomNumber * 0.25F * (float)base;
        return Mth.floor(((float)base + inscriptionAddition + modifierAddition + randomAddition) * multiplier * (1.0F - discount));
    }



    @Unique
    private void companion_notification$sendMessageToPlayer(ServerPlayer player, String message, ChatFormatting chatFormatting) {
        player.sendMessage((new TextComponent(message).withStyle(chatFormatting)), Util.NIL_UUID);
    }
}
