package com.icecubegame.boatenchant.client.mixin;

import com.icecubegame.boatenchant.client.BoatRenderStateMixinAccessor;
import net.minecraft.client.renderer.entity.state.BoatRenderState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BoatRenderState.class)
public class BoatRenderStateMixin implements BoatRenderStateMixinAccessor {
    private boolean isEnchanted;

    @Override
    public boolean getIsEnchanted() {
        return isEnchanted;
    }

    @Override
    public void setIsEnchanted(boolean isEnchanted) {
        this.isEnchanted = isEnchanted;
    }
}
