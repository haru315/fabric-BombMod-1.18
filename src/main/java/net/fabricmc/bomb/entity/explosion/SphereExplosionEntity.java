package net.fabricmc.bomb.entity.explosion;

import net.fabricmc.bomb.BombMod;
import net.fabricmc.bomb.explosion.CircleMapGenerator;
import net.fabricmc.bomb.explosion.SphereMapGenerator;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.List;

public class SphereExplosionEntity extends Entity {

    protected DamageSource damageSource;
    protected SphereMapGenerator sphereMG;
    protected int strength = 100;

    public SphereExplosionEntity(World world) {
        this(BombMod.SPHEREE_EXPLOSION_ENTITY, world);
    }

    public SphereExplosionEntity(EntityType<? extends SphereExplosionEntity> type, World world) {
        super(type, world);
    }

    public SphereExplosionEntity(World world, double x, double y, double z) {
        this(BombMod.SPHEREE_EXPLOSION_ENTITY, world);
        this.setPosition(x, y, z);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.damageSource = DamageSource.explosion((Explosion) null);
    }

    @Override
    public void tick() {
        if (sphereMG == null){
            this.discard();
            return;
        }
        for(int i=0;i<1;i++){
            if (sphereMG.getSize() > this.strength){
                this.discard();
                return;
            }
            sphereMG.sizeUp();

            for (SphereMapGenerator.Mass m : sphereMG.getMassList()){
                BlockPos blockPos3 = new BlockPos((int)this.prevX+m.x, (int)this.prevY+m.y, (int)this.prevZ+m.z);

                if(!world.isAir(blockPos3)){
                    if (sphereMG.getSize() < this.strength -1 ){
                        world.setBlockState(blockPos3, Blocks.AIR.getDefaultState(), Block.FORCE_STATE + Block.NOTIFY_LISTENERS);
                    }else {
                        world.setBlockState(blockPos3, Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
    }

    public static SphereExplosionEntity statFac(World world, double x, double y, double z, int r) {

        SphereExplosionEntity mk4 = new SphereExplosionEntity(world);
        mk4.sphereMG = new SphereMapGenerator();
        mk4.setPosition(x, y, z);
        mk4.prevX = x;
        mk4.prevY = y;
        mk4.prevZ = z;
        return mk4;
    }


    @Override
    protected void initDataTracker() {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
}
