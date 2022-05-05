package net.fabricmc.bomb.explosion;

import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Random;

public abstract class BombExplosion {
    protected static final ExplosionBehavior DEFAULT_BEHAVIOR = new ExplosionBehavior();
    protected final Random random = new Random();
    protected final World world;
    protected final DamageSource damageSource;
    protected float power;

    protected boolean createFire = false;

    protected final BlockPos pos;
    protected final double x;
    protected final double y;
    protected final double z;
    protected final Map<PlayerEntity, Vec3d> affectedPlayers = Maps.newHashMap();


    public BombExplosion(World world, Entity entity, double x, double y, double z, float power) {
        this(world,entity,null,x,y,z,power);
    }

    public BombExplosion(World world, Entity entity, @Nullable DamageSource damageSource, double x, double y, double z, float power) {
        this.world = world;
        this.power = power;
        this.pos = new BlockPos(x,y,z);
        this.x = x;
        this.y = y;
        this.z = z;
        this.damageSource = DamageSource.explosion((Explosion) null);
    }

    public void Explosion(){
        this.world.createExplosion(null, this.x, this.y, this.z, this.power, Explosion.DestructionType.BREAK);
    }

    public void setCreateFire(boolean createFire) {
        this.createFire = createFire;
    }
}
