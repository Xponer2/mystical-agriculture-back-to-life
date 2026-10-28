package com.blakebr0.mysticalagriculture.block;

import com.blakebr0.cucumber.block.BaseBlock;

import net.minecraft.block.SoundType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class WitherproofBlock extends BaseBlock {
    public WitherproofBlock() {
        super( p -> p
            .strength(20.0F, 2000.0F)
            .sound(SoundType.STONE)
            .requiresCorrectToolForDrops()
        );
    }

    @Override
    public void onExplosionDestroy(World level, BlockPos pos, Explosion explosion) { }

    @Override
    public boolean canDropFromExplosion(Explosion explosion) {
        return false;
    }
}
