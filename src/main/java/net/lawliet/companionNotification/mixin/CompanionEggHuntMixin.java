package net.lawliet.companionNotification.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.companion.CompanionEggHunt;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CompanionEggHunt.class, remap = false)
public class CompanionEggHuntMixin {

    @Definition(id = "THRESHOLD", field = "Liskallia/vault/core/vault/companion/HuntInstance;THRESHOLD:Liskallia/vault/core/data/key/FieldKey;")
    @Definition(id = "set", method = "Liskallia/vault/core/vault/companion/HuntInstance;set(Liskallia/vault/core/data/key/FieldKey;Ljava/lang/Object;)Liskallia/vault/core/data/DataObject;")
    @Expression("?.set(THRESHOLD,?)")
    @Inject(method = "createHunt", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER))
    public void addCompanionNotification(ServerPlayer player, Vault vault, CallbackInfo ci) {
        player.sendMessage((new TextComponent("Companion is watching you!").withStyle(ChatFormatting.GREEN)), Util.NIL_UUID);
    }
}
