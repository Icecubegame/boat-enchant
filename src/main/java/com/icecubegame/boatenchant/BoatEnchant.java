package com.icecubegame.boatenchant;

import com.icecubegame.boatenchant.attachment.BoatEnchantmentAttachment;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;


@Mod(BoatEnchant.MODID)
public class BoatEnchant {
    public static final String MODID = "boatenchant";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BoatEnchant(IEventBus modEventBus, ModContainer modContainer) {
        BoatEnchantmentAttachment.register(modEventBus);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }

}

