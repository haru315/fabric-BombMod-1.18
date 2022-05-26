package net.fabricmc.bomb.entity.explosion;

import net.fabricmc.bomb.BombMod;
import net.fabricmc.bomb.explosion.ExplosionNukeRay;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.World;

public class SphereExplosionEntity extends Entity {


    ExplosionNukeRay explosion;
    public int strength;
    //How many rays should be created
    public int count;
    //How many rays are calculated per tick
    public int speed;
    public int length;

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

    long start;
    long lastUpdate;
    @Override
    public void tick() {
        if (strength == 0) {

            this.discard();
            return;
        }

        if (explosion == null) {
            explosion = new ExplosionNukeRay(this.world, (int) this.getX(), (int) this.getY(), (int) this.getZ(), this.strength, this.count, this.length);
            start = System.currentTimeMillis();
            lastUpdate = System.currentTimeMillis();
        }
        if (!explosion.isAusf3Complete) {
            explosion.collectTipMk4_5(1024 * 10);

            if(System.currentTimeMillis() - lastUpdate > 10000L) {
                lastUpdate = System.currentTimeMillis();
                System.out.println("GspNum: ["+explosion.getGspNum()+"/"+explosion.getGspNumMax()+"]");
            }
        } else if (explosion.getStoredSize() > 0) {
            explosion.processTip(1024);

            if(System.currentTimeMillis() - lastUpdate > 10000L) {
                lastUpdate = System.currentTimeMillis();
                System.out.println("StoredSize: ["+explosion.getStoredSize() +"]");
            }
        } else {
            this.discard();
        }
    }

    public static SphereExplosionEntity statFac(World world, double x, double y, double z, int r) {

        SphereExplosionEntity mk4 = new SphereExplosionEntity(world);
        mk4.strength = r;
        mk4.count = (int)(2.5 * Math.PI * Math.pow(mk4.strength,2));
        mk4.length = mk4.strength / 2;
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
