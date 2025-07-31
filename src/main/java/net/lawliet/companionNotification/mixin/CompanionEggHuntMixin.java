package net.lawliet.companionNotification.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import iskallia.vault.core.data.key.FieldKey;
import iskallia.vault.core.vault.Vault;
import iskallia.vault.core.vault.companion.CompanionEggHunt;
import iskallia.vault.core.vault.companion.HuntInstance;
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

    @Definition(id = "THRESHOLD", field = "Liskallia/vault/core/vault/companion/HuntInstance;THRESHOLD:Liskallia/vault/core/data/key/FieldKey;")
    @Definition(id = "get", method = "Liskallia/vault/core/vault/companion/HuntInstance;get(Liskallia/vault/core/data/key/FieldKey;)Ljava/lang/Object;")
    @Expression("?.get(THRESHOLD)")
    @WrapOperation(method = "tickServer", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 0))
    public Object thresholdCondition(HuntInstance instance, FieldKey fieldKey, Operation<Object> original) {
            return  instance.get(fieldKey).equals(0) ? 1 : instance.get(fieldKey);
    }
}
