package io.yooksi.timelib.mixin;

import java.util.Iterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SChunkDataPacket;
import net.minecraft.tileentity.PistonTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

@SuppressWarnings("unused")
@Mixin(ClientPlayNetHandler.class)
public class ClientPlayNetworkHandler_onChunkDataMixin {

	@Shadow private ClientWorld world;

	@Inject(method = "handleChunkData",
			locals = LocalCapture.CAPTURE_FAILHARD,
			require = 0, at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/world/ClientWorld;getTileEntity" +
					"(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/tileentity/TileEntity;",
			shift = At.Shift.AFTER)
	)
	@SuppressWarnings("rawtypes")
	private void recreateMovingPistons(SChunkDataPacket packet, CallbackInfo ci,
									   int i, int j, Chunk chunk, Iterator var5,
									   CompoundNBT tag, BlockPos blockPos) {

		TileEntity tileEntity = world.getTileEntity(blockPos);
		if (tileEntity == null && "minecraft:piston".equals(tag.getString("id")))
		{
			BlockState blockState = world.getBlockState(blockPos);
			if (blockState.getBlock() == Blocks.MOVING_PISTON)
			{
				tag.putFloat("progress", Math.min(tag.getFloat("progress") + 0.5F, 1.0F));
				tileEntity = new PistonTileEntity();
				tileEntity.read(tag);
				world.setTileEntity(blockPos, tileEntity);
				tileEntity.updateContainingBlockInfo();
			}
		}
	}
}
