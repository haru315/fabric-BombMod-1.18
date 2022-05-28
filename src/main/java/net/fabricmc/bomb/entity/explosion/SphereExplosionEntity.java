package net.fabricmc.bomb.entity.explosion;

import net.fabricmc.bomb.BombMod;
import net.fabricmc.bomb.explosion.CircleMapGenerator;
import net.fabricmc.bomb.explosion.SphereMapGenerator;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SphereExplosionEntity extends Entity {


    public SphereMapGenerator sphereMG;
    public int strength;

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
    }

    @Override
    public void tick() {
        if (sphereMG == null){
            this.discard();
            return;
        }
        for(int i=0;i<16;i++){
            if (sphereMG.getSize() > 100){
                this.discard();
                return;
            }
            sphereMG.sizeUp();

            for (SphereMapGenerator.Mass m : sphereMG.getMass()){
                BlockPos blockPos3 = new BlockPos((int)this.prevX+m.x, (int)this.prevY+m.y, (int)this.prevZ+m.z);
                world.setBlockState(blockPos3, Blocks.AIR.getDefaultState());
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
