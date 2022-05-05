package net.fabricmc.bomb.block;

import net.fabricmc.bomb.entity.Tnt1024xEntity;
import net.fabricmc.bomb.entity.Tnt4xEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

;

public class Tnt1024xBlock extends BombBlock {
    public static final BooleanProperty UNSTABLE = Properties.UNSTABLE;

    public Tnt1024xBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)this.getDefaultState().with(UNSTABLE, false));
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        if (world.isClient) {
            return;
        }
        Tnt1024xEntity entity = new Tnt1024xEntity(world, (double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5, explosion.getCausingEntity());
        int i = entity.getFuse();
        entity.setFuse((short)(world.random.nextInt(i / 4) + i / 8));
        world.spawnEntity(entity);
    }

    @Override
    public void primeExplosion(World world, BlockPos pos, @Nullable LivingEntity igniter) {
        if (world.isClient) {
            return;
        }
        Tnt1024xEntity entity = new Tnt1024xEntity(world, (double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5, igniter);
        world.spawnEntity(entity);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
        world.emitGameEvent((Entity)igniter, GameEvent.PRIME_FUSE, pos);
    }
}