package com.icecubegame.boatenchant.attachment;

import com.icecubegame.boatenchant.BoatEnchant;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public class BoatEnchantmentAttachment {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, BoatEnchant.MODID);

    public static final Supplier<AttachmentType<BoatEnchantmentAttachment>> BOAT_ENCHANTMENTS = ATTACHMENT_TYPES.register(
            "boat_enchantments",
            () -> AttachmentType
                    .builder(BoatEnchantmentAttachment::new)
                    .serialize(new Serializer())
                    .sync(new SyncHandler())
                    .build()
    );

    public static void register(IEventBus modBus) {
        ATTACHMENT_TYPES.register(modBus);
    }

    private boolean hasLand;
    private boolean hasCurseLand;
    private boolean hasStep;

    public BoatEnchantmentAttachment() {
        this.hasLand = false;
        this.hasCurseLand = false;
        this.hasStep = false;
    }

    public boolean getHasLand() {
        return hasLand;
    }

    public void setHasLand (boolean hasLand) {
        this.hasLand = hasLand;
    }

    public boolean getHasCurseLand() {
        return hasCurseLand;
    }

    public void setHasCurseLand (boolean hasCurseLand) {
        this.hasCurseLand = hasCurseLand;
    }

    public boolean getHasStep() {
        return hasStep;
    }

    public void setHasStep (boolean hasStep) {
        this.hasStep = hasStep;
    }

    public static class Serializer implements IAttachmentSerializer<BoatEnchantmentAttachment> {

        @Override
        public BoatEnchantmentAttachment read(IAttachmentHolder holder, ValueInput input) {
            BoatEnchantmentAttachment attachment = holder.getData(BOAT_ENCHANTMENTS);
            attachment.setHasLand(input.read("has_land", Codec.BOOL).orElse(false));
            attachment.setHasCurseLand(input.read("has_curse_land", Codec.BOOL).orElse(false));
            attachment.setHasStep(input.read("has_step", Codec.BOOL).orElse(false));
            return attachment;
        }

        @Override
        public boolean write(BoatEnchantmentAttachment attachment, ValueOutput output) {
            output.store("has_land", Codec.BOOL, attachment.getHasLand());
            output.store("has_curse_land", Codec.BOOL, attachment.getHasCurseLand());
            output.store("has_step", Codec.BOOL, attachment.getHasStep());
            return true;
        }
    }

    public static class SyncHandler implements AttachmentSyncHandler<BoatEnchantmentAttachment> {

        @Override
        public void write(RegistryFriendlyByteBuf buf, BoatEnchantmentAttachment attachment, boolean initialSync) {
            buf.writeBoolean(attachment.getHasLand());
            buf.writeBoolean(attachment.getHasCurseLand());
            buf.writeBoolean(attachment.getHasStep());
        }

        @Override
        public @Nullable BoatEnchantmentAttachment read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable BoatEnchantmentAttachment previousValue) {
            BoatEnchantmentAttachment attachment = holder.getData(BOAT_ENCHANTMENTS);
            attachment.setHasLand(buf.readBoolean());
            attachment.setHasCurseLand(buf.readBoolean());
            attachment.setHasStep(buf.readBoolean());
            return attachment;
        }
    }
}
