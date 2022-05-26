package net.fabricmc.bomb;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.bomb.block.*;
import net.fabricmc.bomb.entity.*;
import net.fabricmc.bomb.entity.explosion.SphereExplosionEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BombMod implements ModInitializer {

    // ModID文字列(小文字のみ)
    public static final String MOD_ID = "bomb";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    // ブロック登録
    public static final Block TNT4X_BLOCK = new Tnt4xBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    public static final Block TNT16X_BLOCK = new Tnt16xBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    public static final Block TNT64X_BLOCK = new Tnt64xBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    public static final Block TNT256X_BLOCK = new Tnt256xBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    public static final Block TNT1024X_BLOCK = new Tnt1024xBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    public static final Block TNT4096X_BLOCK = new Tnt4096xBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    public static final Block TNT16384X_BLOCK = new Tnt16384xBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f));

    public static final Block NUCLEAR_BOMB = new NuclearBomb(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    public static final Block SUPER_NUCLEAR_BOMB = new SuperNuclearBomb(FabricBlockSettings.of(Material.METAL).strength(4.0f));

    public static final Block NOVA_BLOCK = new NovaBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    public static final Block SUPER_NOVA_BLOCK = new SuperNovaBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f));
    public static final Block HYPER_NOVA_BLOCK = new HyperNovaBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f));


    // エンティティ登録
    // TNT_x
    public static final EntityType<Tnt4xEntity> TNT_4x_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "tnt_4x_entity"),
            FabricEntityTypeBuilder.<Tnt4xEntity>create(SpawnGroup.CREATURE, Tnt4xEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );
    public static final EntityType<Tnt16xEntity> TNT_16x_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "tnt_16x_entity"),
            FabricEntityTypeBuilder.<Tnt16xEntity>create(SpawnGroup.CREATURE, Tnt16xEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );
    public static final EntityType<Tnt64xEntity> TNT_64x_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "tnt_64x_entity"),
            FabricEntityTypeBuilder.<Tnt64xEntity>create(SpawnGroup.CREATURE, Tnt64xEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );
    public static final EntityType<Tnt256xEntity> TNT_256x_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "tnt_256x_entity"),
            FabricEntityTypeBuilder.<Tnt256xEntity>create(SpawnGroup.CREATURE, Tnt256xEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );
    public static final EntityType<Tnt1024xEntity> TNT_1024x_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "tnt_1024x_entity"),
            FabricEntityTypeBuilder.<Tnt1024xEntity>create(SpawnGroup.CREATURE, Tnt1024xEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );
    public static final EntityType<Tnt4096xEntity> TNT_4096x_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "tnt_4096x_entity"),
            FabricEntityTypeBuilder.<Tnt4096xEntity>create(SpawnGroup.CREATURE, Tnt4096xEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );
    public static final EntityType<Tnt16384xEntity> TNT_16384x_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "tnt_16384x_entity"),
            FabricEntityTypeBuilder.<Tnt16384xEntity>create(SpawnGroup.CREATURE, Tnt16384xEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );

    // NuclearBombs
    public static final EntityType<NuclearBombEntity> NUCLEAR_BOMB_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "nuclear_bomb_entity"),
            FabricEntityTypeBuilder.<NuclearBombEntity>create(SpawnGroup.CREATURE, NuclearBombEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );
    public static final EntityType<SuperNuclearBombEntity> SUPER_NUCLEAR_BOMB_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "super_nuclear_bomb_entity"),
            FabricEntityTypeBuilder.<SuperNuclearBombEntity>create(SpawnGroup.CREATURE, SuperNuclearBombEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );

    // NovaBombs
    public static final EntityType<NovaEntity> NOVA_BOMB_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "nova_bomb_entity"),
            FabricEntityTypeBuilder.<NovaEntity>create(SpawnGroup.CREATURE, NovaEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );
    public static final EntityType<SuperNovaEntity> SUPER_NOVA_BOMB_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "super_nova_bomb_entity"),
            FabricEntityTypeBuilder.<SuperNovaEntity>create(SpawnGroup.CREATURE, SuperNovaEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );
    public static final EntityType<HyperNovaEntity> HYPER_NOVA_BOMB_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "hyper_nova_bomb_entity"),
            FabricEntityTypeBuilder.<HyperNovaEntity>create(SpawnGroup.CREATURE, HyperNovaEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );

    // Explosion Entity
    public static final EntityType<SphereExplosionEntity> SPHEREE_EXPLOSION_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(MOD_ID, "sphere_explosion_entity"),
            FabricEntityTypeBuilder.<SphereExplosionEntity>create(SpawnGroup.CREATURE, SphereExplosionEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );


    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "tnt4x_block"), TNT4X_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "tnt4x_block"), new BlockItem(TNT4X_BLOCK, new FabricItemSettings().group(ModItemGroup.BOMB_GROUP)));
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "tnt16x_block"), TNT16X_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "tnt16x_block"), new BlockItem(TNT16X_BLOCK, new FabricItemSettings().group(ModItemGroup.BOMB_GROUP)));
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "tnt64x_block"), TNT64X_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "tnt64x_block"), new BlockItem(TNT64X_BLOCK, new FabricItemSettings().group(ModItemGroup.BOMB_GROUP)));
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "tnt256x_block"), TNT256X_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "tnt256x_block"), new BlockItem(TNT256X_BLOCK, new FabricItemSettings().group(ModItemGroup.BOMB_GROUP)));
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "tnt1024x_block"), TNT1024X_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "tnt1024x_block"), new BlockItem(TNT1024X_BLOCK, new FabricItemSettings().group(ModItemGroup.BOMB_GROUP)));
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "tnt4096x_block"), TNT4096X_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "tnt4096x_block"), new BlockItem(TNT4096X_BLOCK, new FabricItemSettings().group(ModItemGroup.BOMB_GROUP)));
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "tnt16384x_block"), TNT16384X_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "tnt16384x_block"), new BlockItem(TNT16384X_BLOCK, new FabricItemSettings().group(ModItemGroup.BOMB_GROUP)));

        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "nuclear_bomb"), NUCLEAR_BOMB);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "nuclear_bomb"), new BlockItem(NUCLEAR_BOMB, new FabricItemSettings().group(ModItemGroup.BOMB_GROUP)));
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "super_nuclear_bomb"), SUPER_NUCLEAR_BOMB);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "super_nuclear_bomb"), new BlockItem(SUPER_NUCLEAR_BOMB, new FabricItemSettings().group(ModItemGroup.BOMB_GROUP)));;

        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "nova_bomb"), NOVA_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "nova_bomb"), new BlockItem(NOVA_BLOCK, new FabricItemSettings().group(ModItemGroup.BOMB_GROUP)));
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "super_nova_bomb"), SUPER_NOVA_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "super_nova_bomb"), new BlockItem(SUPER_NOVA_BLOCK, new FabricItemSettings().group(ModItemGroup.BOMB_GROUP)));
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "hyper_nova_bomb"), HYPER_NOVA_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "hyper_nova_bomb"), new BlockItem(HYPER_NOVA_BLOCK, new FabricItemSettings().group(ModItemGroup.BOMB_GROUP)));
    }
}
