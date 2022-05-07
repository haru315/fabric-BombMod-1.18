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

public class BigSphereExplosion extends BombExplosion {
    protected int mode = 0;
    protected float attenuationVar1 = 0.22500001f;
    protected float attenuationVar2 = 0.3F;
    protected boolean particles = true;

    public BigSphereExplosion(World world, Entity entity, double x, double y, double z, float power) {
        super(world,entity, x, y, z, power);
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
        float f = randomRay(this.power,this.mode);
        double d4 = this.x;
        double d6 = this.y;
        double d8 = this.z;
        BlockPos blockPos = new BlockPos(d4, d6, d8);
        for(; f > 0.0F; f -= this.attenuationVar1)
        {
            blockPos = new BlockPos(d4, d6, d8);
            BlockState blockState = world.getBlockState(blockPos);
            FluidState fluidState = this.world.getFluidState(blockPos);
            float f3 = 0.0F;
            if(blockState.getMaterial() != Material.AIR)
            {
                if(!blockState.getMaterial().isLiquid()) {
                    Optional<Float> optional = DEFAULT_BEHAVIOR.getBlastResistance(null, this.world, blockPos, blockState, fluidState);
                    if (optional.isPresent()) {
                        f3 = optional.get().floatValue();
                    }
                    f -= attenuation(f3,this.mode);
                }
                if(f > 0.0F){
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

    //
    public void ExplosionA(){
        int i = (int)(4 * Math.PI * Math.pow(this.power,2));
        this.mode = 0;
        this.attenuationVar1 = 0.22500001F;
        this.attenuationVar2 = 0.3F;
        this.effect();
        if (!this.world.isClient) {
            this.Explosion(i);
        }
    }

    //
    public void ExplosionB(){
        int i = (int)(4 * Math.PI * Math.pow(this.power,2));
        this.mode = 1;
        this.attenuationVar1 = 0.275F;
        this.attenuationVar2 = 0.45F;
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

        for(double[] gss :generateGss(smoothness)){
            double[] pos = spherical2cartesian(gss[0],gss[1]);
            set = makeRay(pos[0], pos[2], pos[1], set);
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

    // レイ、ランダム関数
    public float randomRay(float f0, int i){
        float f1 = switch (i) {
            case 0 -> f0 * (0.7f + this.world.random.nextFloat() * 0.6f);
            case 1 -> f0 * (1.0F + this.world.random.nextFloat() * 0.02F);
            default -> f0;
        };
        return f1;
    }

    // ブロック減衰関数
    public float attenuation(float f0, int i){
        float f1 = 0;
        switch (i) {
            case 0 :
                f1 = (f0 + 0.3f) * 0.3f;
                break;
            case 1 :
                if(f0 > 64F) f0 /= 16F;
                f1 = ((f0 / 3.0F) + 0.3F) * 0.3F;
                break;
            default :
                f1 = f0;
        }
        return f1;
    }
}
