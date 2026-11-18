package com.icecubegame.boatenchant.mixin;

import com.icecubegame.boatenchant.BoatRenderStateMixinAccessor;
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
