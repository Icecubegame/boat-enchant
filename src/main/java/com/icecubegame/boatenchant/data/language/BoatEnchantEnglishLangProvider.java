package com.icecubegame.boatenchant.data.language;

import com.icecubegame.boatenchant.BoatEnchant;
import com.icecubegame.boatenchant.enchantment.BoatEnchantment;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.data.LanguageProvider;


public class BoatEnchantEnglishLangProvider extends LanguageProvider {
    public BoatEnchantEnglishLangProvider(PackOutput dataOutput) {
        super(dataOutput, BoatEnchant.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.addEnchantment(BoatEnchantment.LAND, "Land");
        this.addEnchantment(BoatEnchantment.CURSE_LAND, "Curse of Land");
        this.addEnchantment(BoatEnchantment.STEP, "Step");
    }

    private void addEnchantment(ResourceKey<Enchantment> enchant, String name) {
        this.add("enchantment." + enchant.identifier().toLanguageKey(), name);
    }
}