package com.icecubegame.boatenchant.mixin;

import com.icecubegame.boatenchant.BoatEnchant;
import com.icecubegame.boatenchant.BoatMixinAccessor;
import com.icecubegame.boatenchant.enchantment.BoatEnchantment;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(AbstractBoat.class)
public abstract class BoatMixin extends VehicleEntity implements BoatMixinAccessor {
	@Unique
    private static final EntityDataAccessor<Boolean> DATA_ID_HAS_LAND_ENCHANT = SynchedEntityData.defineId(AbstractBoat.class, EntityDataSerializers.BOOLEAN);
	@Unique
	private static final EntityDataAccessor<Boolean> DATA_ID_HAS_STEP_ENCHANT = SynchedEntityData.defineId(AbstractBoat.class, EntityDataSerializers.BOOLEAN);

	private BoatMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@ModifyVariable(
			method = "floatBoat",
			name = "invFriction",
			at = @At(value = "STORE", ordinal = 5))
	private float boatEnchant$floatBoat(float invFriction) {
		if (getHasLandEnchant()) {
			return Math.max(0.9F, invFriction);
		}
		return invFriction;
	}

	@ModifyReturnValue(method = "getPickResult", at = @At("RETURN"))
	private ItemStack boatEnchant$getPickResult(ItemStack original) {

		if (getHasLandEnchant()) {
			Optional<Holder.Reference<Enchantment>> entry = this.registryAccess().get(BoatEnchantment.LAND);
			entry.ifPresent(enchant -> original.enchant(enchant, 1));
		}

		if (getHasStepEnchant()) {
			Optional<Holder.Reference<Enchantment>> entry = this.registryAccess().get(BoatEnchantment.STEP);
			entry.ifPresent(enchant -> original.enchant(enchant, 1));
		}

		return original;
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void boatEnchant$addAdditionalSaveData(CallbackInfo ci, @Local ValueOutput output) {
		this.writeEnchantData(output, this.getHasLandEnchant(), this.getHasStepEnchant());
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void boatEnchant$readAdditionalSaveData(CallbackInfo ci, @Local ValueInput input) {
		this.readEnchantData(input);
	}

	@Unique
    private void readEnchantData(final ValueInput input) {
		Boolean hasLandEnchant = input.read("hasLandEnchant", Codec.BOOL).orElse(false);
		setHasLandEnchant(hasLandEnchant);
		Boolean hasStepEnchant = input.read("hasStepEnchant", Codec.BOOL).orElse(false);
		setHasStepEnchant(hasStepEnchant);
	}

	@Unique
    private void writeEnchantData(final ValueOutput output, final Boolean hasLandEnchant, final Boolean hasStepEnchant) {
		output.store("hasLandEnchant", Codec.BOOL, hasLandEnchant);
		output.store("hasStepEnchant", Codec.BOOL, hasStepEnchant);
	}

	@Override
	public void destroy(final ServerLevel level, final Item dropItem) {
		this.kill(level);
		if (level.getGameRules().get(GameRules.ENTITY_DROPS)) {
			ItemStack itemStack = new ItemStack(dropItem);

			if (getHasLandEnchant()) {
				Optional<Holder.Reference<Enchantment>> entry = this.registryAccess().get(BoatEnchantment.LAND);
                entry.ifPresent(enchant -> itemStack.enchant(enchant, 1));
			}

			if (getHasStepEnchant()) {
				Optional<Holder.Reference<Enchantment>> entry = this.registryAccess().get(BoatEnchantment.STEP);
				entry.ifPresent(enchant -> itemStack.enchant(enchant, 1));
			}

			itemStack.set(DataComponents.CUSTOM_NAME, this.getCustomName());
			this.spawnAtLocation(level, itemStack);
		}
	}

	@Override
	public float maxUpStep() {
		return (getHasStepEnchant() && this.getControllingPassenger() instanceof Player) ? 1.0f : 0.0f;
	}

	@Inject(method = "defineSynchedData", at = @At("TAIL"))
	private void boatEnchant$defineSynchedData(CallbackInfo ci, @Local(argsOnly = true) SynchedEntityData.Builder entityData) {
		entityData.define(DATA_ID_HAS_LAND_ENCHANT, false);
		entityData.define(DATA_ID_HAS_STEP_ENCHANT, false);
	}

	@Override
    public void setHasLandEnchant(Boolean hasLand) {
		entityData.set(DATA_ID_HAS_LAND_ENCHANT, hasLand);
	}
	@Override
    public Boolean getHasLandEnchant() {
		return entityData.get(DATA_ID_HAS_LAND_ENCHANT);
	}

	@Override
    public void setHasStepEnchant(Boolean hasStep) {
		entityData.set(DATA_ID_HAS_STEP_ENCHANT, hasStep);
	}
	@Override
    public Boolean getHasStepEnchant() {
		return entityData.get(DATA_ID_HAS_STEP_ENCHANT);
	}
}