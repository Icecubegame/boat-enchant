package com.icecubegame.boatenchant.mixin;

import com.icecubegame.boatenchant.BoatEnchant;
import com.icecubegame.boatenchant.BoatMixinAccessor;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
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
    private AbstractBoat boatEnchant$getBoat(AbstractBoat boat, @Local Level level, @Local ItemStack itemStack) {

        if (boat != null) {
            ResourceKey<Enchantment> LAND_ENCHANT_ID = ResourceKey.create(Registries.ENCHANTMENT, BoatEnchant.id("land"));
            Optional<Holder.Reference<Enchantment>> entry = level.registryAccess().get(LAND_ENCHANT_ID);
            if (entry.isPresent()) {
                Holder.Reference<Enchantment> enchant = entry.get();
                int levels =  EnchantmentHelper.getItemEnchantmentLevel(enchant, itemStack);
                if (levels > 0) {
                    if (boat instanceof BoatMixinAccessor) {
                        ((BoatMixinAccessor)boat).setHasLandEnchant(true);
                    }
                }
            }
        }

        return boat;
    }
}