package com.icecubegame.boatenchant.mixin;

import com.icecubegame.boatenchant.BoatEnchant;
import com.icecubegame.boatenchant.BoatMixinAccessor;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.function.Supplier;

@Mixin(AbstractBoat.class)
public abstract class BoatMixin extends VehicleEntity implements BoatMixinAccessor {
	private static final EntityDataAccessor<Boolean> DATA_ID_HAS_LAND_ENCHANT = SynchedEntityData.defineId(AbstractBoat.class, EntityDataSerializers.BOOLEAN);

	private BoatMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@ModifyVariable(
			method = "floatBoat",
			name = "invFriction",
			at = @At(value = "STORE", ordinal = 5))
	private float boatEnchant$floatBoat(float invFriction) {
		if (getHasLandEnchant()) {
			return 0.9F;
		}
		return invFriction;
	}

	@ModifyReturnValue(method = "getPickResult", at = @At("RETURN"))
	private ItemStack boatEnchant$getPickResult(ItemStack original) {

		if (getHasLandEnchant()) {
			ResourceKey<Enchantment> LAND_ENCHANT_ID = ResourceKey.create(Registries.ENCHANTMENT, BoatEnchant.id("land"));
			Optional<Holder.Reference<Enchantment>> entry = this.registryAccess().get(LAND_ENCHANT_ID);
			entry.ifPresent(enchant -> original.enchant(enchant, 1));
		}

		return original;
	}

	@Override
	public void destroy(final ServerLevel level, final Item dropItem) {
		this.kill(level);
		if (level.getGameRules().get(GameRules.ENTITY_DROPS)) {
			ItemStack itemStack = new ItemStack(dropItem);

			if (getHasLandEnchant()) {
				ResourceKey<Enchantment> LAND_ENCHANT_ID = ResourceKey.create(Registries.ENCHANTMENT, BoatEnchant.id("land"));
				Optional<Holder.Reference<Enchantment>> entry = this.registryAccess().get(LAND_ENCHANT_ID);
                entry.ifPresent(enchant -> itemStack.enchant(enchant, 1));
			}

			itemStack.set(DataComponents.CUSTOM_NAME, this.getCustomName());
			this.spawnAtLocation(level, itemStack);
		}
	}

	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	private void boatEnchant$defineSynchedData(CallbackInfo ci, @Local SynchedEntityData.Builder entityData) {
		entityData.define(DATA_ID_HAS_LAND_ENCHANT, false);
	}

	@Override
	public void setHasLandEnchant(Boolean inputRight) {
		entityData.set(DATA_ID_HAS_LAND_ENCHANT, inputRight);
	}

	public Boolean getHasLandEnchant() {
		return entityData.get(DATA_ID_HAS_LAND_ENCHANT);
	}
}