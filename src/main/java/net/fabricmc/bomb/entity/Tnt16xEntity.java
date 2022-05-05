package net.fabricmc.bomb.entity;

import net.fabricmc.bomb.BombMod;
import net.fabricmc.bomb.explosion.BigExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Tnt16xEntity extends Tnt4xEntity {

    public Tnt16xEntity(EntityType<? extends Tnt16xEntity> type, World world) {
        super(type, world);
        this.inanimate = true;
    }

    public Tnt16xEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(BombMod.TNT_16x_ENTITY, world);
        this.setPosition(x, y, z);
        this.setVelocity(0.0f, 0.2f, 0.0f);
        this.setFuse(80);
        this.size = 16.0f;
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.causingEntity = igniter;
    }
}