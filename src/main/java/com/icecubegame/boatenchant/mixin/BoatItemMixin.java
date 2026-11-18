package com.icecubegame.boatenchant.mixin;

import com.icecubegame.boatenchant.attachment.BoatEnchantmentAttachment;
import com.icecubegame.boatenchant.enchantment.BoatEnchantment;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;


import java.util.Optional;


@Mixin(BoatItem.class)
public abstract class BoatItemMixin {
    @ModifyReturnValue(method = "getBoat", at = @At("RETURN"))
    private AbstractBoat boatEnchant$getBoat(AbstractBoat boat, @Local(argsOnly = true) Level level, @Local(argsOnly = true) ItemStack itemStack) {
        BoatEnchantmentAttachment attachment = boat.getData(BoatEnchantmentAttachment.BOAT_ENCHANTMENTS);
        if (boat != null) {
            Optional<Holder.Reference<Enchantment>> entry;
            entry = level.registryAccess().get(BoatEnchantment.LAND);
            if (entry.isPresent()) {
                Holder.Reference<Enchantment> enchant = entry.get();
                int levels =  EnchantmentHelper.getItemEnchantmentLevel(enchant, itemStack);
                if (levels > 0) {
                    attachment.setHasLand(true);
                }
            }

            entry = level.registryAccess().get(BoatEnchantment.CURSE_LAND);
            if (entry.isPresent()) {
                Holder.Reference<Enchantment> enchant = entry.get();
                int levels =  EnchantmentHelper.getItemEnchantmentLevel(enchant, itemStack);
                if (levels > 0) {
                    attachment.setHasCurseLand(true);
                }
            }

            entry = level.registryAccess().get(BoatEnchantment.STEP);
            if (entry.isPresent()) {
                Holder.Reference<Enchantment> enchant = entry.get();
                int levels =  EnchantmentHelper.getItemEnchantmentLevel(enchant, itemStack);
                if (levels > 0) {
                    attachment.setHasStep(true);
                }
            }
        }

        return boat;
    }
}