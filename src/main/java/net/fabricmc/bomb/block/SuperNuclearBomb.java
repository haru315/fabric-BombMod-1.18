package net.fabricmc.bomb.block;

import net.fabricmc.bomb.entity.NuclearBombEntity;
import net.fabricmc.bomb.entity.SuperNuclearBombEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

public class SuperNuclearBomb extends BombBlock {
    public static final BooleanProperty UNSTABLE = Properties.UNSTABLE;

    public SuperNuclearBomb(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)this.getDefaultState().with(UNSTABLE, false));
    }

    @Override
    public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
        if (world.isClient) {
            return;
        }
        SuperNuclearBombEntity entity = new SuperNuclearBombEntity(world, (double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5, explosion.getCausingEntity());
        int i = entity.getFuse();
        entity.setFuse((short)(world.random.nextInt(i / 4) + i / 8));
        world.spawnEntity(entity);
    }

    @Override
    public void primeExplosion(World world, BlockPos pos, @Nullable LivingEntity igniter) {
        if (world.isClient) {
            return;
        }
        SuperNuclearBombEntity entity = new SuperNuclearBombEntity(world, (double)pos.getX() + 0.5, pos.getY(), (double)pos.getZ() + 0.5, igniter);
        world.spawnEntity(entity);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
        world.emitGameEvent((Entity)igniter, GameEvent.PRIME_FUSE, pos);
    }
}