package net.fabricmc.bomb.entity;

import net.fabricmc.bomb.BombMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class Tnt256xEntity extends Tnt4xEntity {

    public Tnt256xEntity(EntityType<? extends Tnt256xEntity> type, World world) {
        super(type, world);
        this.inanimate = true;
    }

    public Tnt256xEntity(World world, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(BombMod.TNT_256x_ENTITY, world);
        this.setPosition(x, y, z);
        this.setVelocity(0.0f, 0.2f, 0.0f);
        this.setFuse(80);
        this.size = 64.0f;
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.causingEntity = igniter;
    }
}