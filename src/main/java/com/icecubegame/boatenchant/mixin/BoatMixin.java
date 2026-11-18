package com.icecubegame.boatenchant.mixin;

import com.icecubegame.boatenchant.attachment.BoatEnchantmentAttachment;
import com.icecubegame.boatenchant.enchantment.BoatEnchantment;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(AbstractBoat.class)
public abstract class BoatMixin extends VehicleEntity {
	@Shadow
	private float landFriction;

	private BoatMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@ModifyVariable(
			method = "floatBoat",
			name = "invFriction",
			at = @At(value = "STORE", ordinal = 5))
	private float boatEnchant$floatBoat_ON_LAND(float invFriction) {
		BoatEnchantmentAttachment attachment = this.getData(BoatEnchantmentAttachment.BOAT_ENCHANTMENTS);
		if (attachment.getHasLand()) {
			return Math.max(0.9F, invFriction);
		}
		return invFriction;
	}

	@ModifyVariable(
			method = "floatBoat",
			name = "invFriction",
			at = @At(value = "STORE", ordinal = 1))
	private float boatEnchant$floatBoat_IN_WATER(float invFriction) {
		BoatEnchantmentAttachment attachment = this.getData(BoatEnchantmentAttachment.BOAT_ENCHANTMENTS);
		if (attachment.getHasCurseLand()) {
			float modifiedInvFriction = landFriction;
			if (this.getControllingPassenger() instanceof Player) {
				this.landFriction /= 2.0F;
			}
			return modifiedInvFriction;
		}
		return invFriction;
	}

	@Override
	public float maxUpStep() {
		BoatEnchantmentAttachment attachment = this.getData(BoatEnchantmentAttachment.BOAT_ENCHANTMENTS);
		return (attachment.getHasStep() && this.getControllingPassenger() instanceof Player) ? 1.0f : 0.0f;
	}

	@ModifyReturnValue(method = "getPickResult", at = @At("RETURN"))
	private ItemStack boatEnchant$getPickResult(ItemStack original) {
		BoatEnchantmentAttachment attachment = this.getData(BoatEnchantmentAttachment.BOAT_ENCHANTMENTS);
		if (attachment.getHasLand()) {
			Optional<Holder.Reference<Enchantment>> entry = this.registryAccess().get(BoatEnchantment.LAND);
			entry.ifPresent(enchant -> original.enchant(enchant, 1));
		}

		if (attachment.getHasCurseLand()) {
			Optional<Holder.Reference<Enchantment>> entry = this.registryAccess().get(BoatEnchantment.CURSE_LAND);
			entry.ifPresent(enchant -> original.enchant(enchant, 1));
		}

		if (attachment.getHasStep()) {
			Optional<Holder.Reference<Enchantment>> entry = this.registryAccess().get(BoatEnchantment.STEP);
			entry.ifPresent(enchant -> original.enchant(enchant, 1));
		}

		return original;
	}

	@Override
	public void destroy(final ServerLevel level, final Item dropItem) {
		this.kill(level);
		if (level.getGameRules().get(GameRules.ENTITY_DROPS)) {
			ItemStack itemStack = new ItemStack(dropItem);

			BoatEnchantmentAttachment attachment = this.getData(BoatEnchantmentAttachment.BOAT_ENCHANTMENTS);
			if (attachment.getHasLand()) {
				Optional<Holder.Reference<Enchantment>> entry = this.registryAccess().get(BoatEnchantment.LAND);
				entry.ifPresent(enchant -> itemStack.enchant(enchant, 1));
			}

			if (attachment.getHasCurseLand()) {
				Optional<Holder.Reference<Enchantment>> entry = this.registryAccess().get(BoatEnchantment.CURSE_LAND);
				entry.ifPresent(enchant -> itemStack.enchant(enchant, 1));
			}

			if (attachment.getHasStep()) {
				Optional<Holder.Reference<Enchantment>> entry = this.registryAccess().get(BoatEnchantment.STEP);
				entry.ifPresent(enchant -> itemStack.enchant(enchant, 1));
			}

			itemStack.set(DataComponents.CUSTOM_NAME, this.getCustomName());
			this.spawnAtLocation(level, itemStack);
		}
	}

	@Deprecated
	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void boatEnchant$readAdditionalSaveData(CallbackInfo ci, @Local(argsOnly = true) ValueInput input) {
		this.readOldEnchantData(input);
	}

	@Deprecated
	@Unique
	private void readOldEnchantData(final ValueInput input) {
		BoatEnchantmentAttachment attachment = this.getData(BoatEnchantmentAttachment.BOAT_ENCHANTMENTS);
		Optional<Boolean> hasLandEnchant = input.read("hasLandEnchant", Codec.BOOL);
		hasLandEnchant.ifPresent(has -> attachment.setHasLand(has));
		Optional<Boolean> hasCurseLandEnchant = input.read("hasCurseLandEnchant", Codec.BOOL);
		hasCurseLandEnchant.ifPresent(has -> attachment.setHasCurseLand(has));
		Optional<Boolean> hasStepEnchant = input.read("hasStepEnchant", Codec.BOOL);
		hasStepEnchant.ifPresent(has -> attachment.setHasStep(has));
	}
}