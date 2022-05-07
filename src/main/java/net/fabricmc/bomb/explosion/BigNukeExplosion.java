package net.fabricmc.bomb.explosion;

import com.google.common.collect.Sets;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Set;

public class BigNukeExplosion extends BombExplosion {
    protected int mode = 0;
    protected float size;
    protected float attenuationVar1 = 0.22500001f;
    protected float attenuationVar2 = 0.3F;
    protected boolean particles = true;

    public BigNukeExplosion(World world, Entity entity, double x, double y, double z, float power, float size) {
        super(world,entity, x, y, z, power);
        this.size = size;
    }

    // 一般化螺旋集合を生成する
    private double[][] generateGss(int n){
        double[][] gss = new double[n][2];
        // 天底の点の座標を代入
        gss[0][0] = Math.PI;
        gss[0][1] = 0.0;

        for (int i=1; i < n; i++){
            int k = i + 1;
            double hk = -1.0 + 2.0 * (k - 1.0) / (n - 1.0);
            gss[i][0] = Math.acos(hk);

            double prev_lon = gss[i - 1][1];
            double lon = prev_lon + 3.6 / Math.sqrt(n) / Math.sqrt(1.0 - hk * hk);
            gss[i][1] = lon % (Math.PI * 2);
        }
        // 天頂の点の座標を代入
        gss[n - 1][0] = 0.0;
        gss[n - 1][1] = 0.0;

        return gss;
    }

    //球面座標を直交座標に変換する
    private double[] spherical2cartesian(double col, double lon){
        double d0 = Math.sin(col) * Math.cos(lon);
        double d1 = Math.sin(col) * Math.sin(lon);
        double d2 = Math.cos(col);
        return new double[]{d0, d1, d2};
    }


    private Set makeRay(double d0, double d1, double d2, Set set)
    {
        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
        d0 /= d3;
        d1 /= d3;
        d2 /= d3;
        float f0 = this.power;
        double d4 = this.x;
        double d6 = this.y;
        double d8 = this.z;
        BlockPos blockPos = new BlockPos(d4, d6, d8);
        for(float f1 = 0;(f1 < this.size && f0 > 0.0F); f1 += this.attenuationVar1)
        {
            blockPos = new BlockPos(d4, d6, d8);
            BlockState blockState = world.getBlockState(blockPos);
            FluidState fluidState = this.world.getFluidState(blockPos);

            double fac = 100 - ((double) f1) / ((double) this.size) * 100;
            fac *= 0.07D;

            if(blockState.getMaterial() != Material.AIR)
            {
                if(!blockState.getMaterial().isLiquid()) {
                    Optional<Float> optional = DEFAULT_BEHAVIOR.getBlastResistance(null, this.world, blockPos, blockState, fluidState);
                    if (optional.isPresent()) {
                        f0 -= Math.pow(optional.get().floatValue(), 7.5D - fac);;
                    }
                }
                if(f0 > 0.0F){
                    set.add(blockPos);
                }
            }else if (this.createFire){
                if (this.world.getBlockState(blockPos.down()).isOpaqueFullCube(this.world, blockPos.down())) {
                    set.add(blockPos);
                }
            }

            d4 += d0 * (double)this.attenuationVar2;
            d6 += d1 * (double)this.attenuationVar2;
            d8 += d2 * (double)this.attenuationVar2;
        }
        return set;
    }

    //BigNukeExplosion
    public void Explosion(){
        int i = (int)(4 * Math.PI * Math.pow(this.size,2));
        this.mode = 0;
        this.attenuationVar1 = 0.3F;
        this.attenuationVar2 = 0.3F;
        this.effect();
        if (!this.world.isClient) {
            this.Explosion(i);
        }
    }


    //
    public void Explosion(int smoothness){
        long start = System.currentTimeMillis();
        long lastUpdate = System.currentTimeMillis();
        Set<BlockPos> set = Sets.newHashSet();

        double[][] gss = generateGss(smoothness);
        for(int i = 0; i < gss.length ; i++){
            double[] pos = spherical2cartesian(gss[i][0],gss[i][1]);
            set = makeRay(pos[0], pos[2], pos[1], set);

            if(System.currentTimeMillis() - lastUpdate > 10000L) {
                lastUpdate = System.currentTimeMillis();
                System.out.println("Gss directed ray: [" + i + "/" + smoothness + "]");
            }
        }

        int size = set.size();
        int e = 0;
        for (BlockPos blockPos3 : set) {
            if(System.currentTimeMillis() - lastUpdate > 10000L) {
                lastUpdate = System.currentTimeMillis();
                System.out.println("Block:["+e+"/"+size+"]");
            }
            e++;
            world.setBlockState(blockPos3, Blocks.AIR.getDefaultState());
            if (this.createFire) {
                if (this.world.getBlockState(blockPos3.down()).isOpaqueFullCube(this.world, blockPos3.down())) {
                    this.world.setBlockState(blockPos3, AbstractFireBlock.getState(this.world, blockPos3));
                }
            }
        }
    }

    // エフェクト・パーティクル
    public void effect(){
        if (this.world.isClient) {
            this.world.playSound(this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0f, (1.0f + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2f) * 0.7f, false);
        }
        if (particles) {
            if (this.power > 2.0f) {
                this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0, 0.0, 0.0);
            } else {
                this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0, 0.0, 0.0);
            }
        }
    }
}
