package io.yooksi.timelib.mixin;

import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class World_advanceTimeMixin extends CapabilityProvider<World> {

	protected World_advanceTimeMixin(Class<World> baseClass) {
		super(baseClass);
	}

	@Shadow protected WorldInfo worldInfo;

	@Shadow public abstract void setDayTime(long time);

	@Inject(method = "advanceTime", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/world/World;setDayTime(J)V"),
			cancellable = true
	)
	private void onAdvanceTime(CallbackInfo ci) {

		 this.setDayTime(this.worldInfo.getDayTime() + 1L);
		 ci.cancel();
	}
}
