package io.github.gaming32.bingo.fabric.datagen.goal;

import io.github.gaming32.bingo.conditions.BlockPatternCondition;
import io.github.gaming32.bingo.data.BingoGoal;
import io.github.gaming32.bingo.data.BingoTags;
import io.github.gaming32.bingo.data.tags.BingoItemTags;
import io.github.gaming32.bingo.triggers.CompleteMapTrigger;
import io.github.gaming32.bingo.triggers.EnchantedItemTrigger;
import io.github.gaming32.bingo.triggers.EquipItemTrigger;
import io.github.gaming32.bingo.triggers.ItemBrokenTrigger;
import io.github.gaming32.bingo.triggers.ItemPickedUpTrigger;
import io.github.gaming32.bingo.util.BlockPattern;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.FilledBucketTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemUsedOnLocationTrigger;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.PlayerInteractTrigger;
import net.minecraft.advancements.critereon.TameAnimalTrigger;
import net.minecraft.advancements.critereon.UsedTotemTrigger;
import net.minecraft.advancements.critereon.UsingItemTrigger;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;

import java.util.List;
import java.util.function.Consumer;

public class HardGoalProvider extends DifficultyGoalProvider {
    public HardGoalProvider(Consumer<BingoGoal> goalAdder) {
        super(3, "hard/", goalAdder);
    }

    @Override
    public void addGoals() {
        addGoal(BingoGoal.builder(id("level_10_enchant"))
            .criterion("enchant", EnchantedItemTrigger.builder().requiredLevels(MinMaxBounds.Ints.atLeast(10)).build())
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD)
            .name(Component.translatable("bingo.goal.level_10_enchant"))
            .icon(new ItemStack(Items.ENCHANTING_TABLE, 10)));
        addGoal(BingoGoal.builder(id("milk_mooshroom"))
            .criterion("obtain", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
                ItemPredicate.Builder.item().of(Items.BUCKET),
                EntityPredicate.wrap(EntityPredicate.Builder.entity().of(EntityType.MOOSHROOM).build())))
            .tags(BingoTags.ACTION, BingoTags.RARE_BIOME, BingoTags.OVERWORLD)
            .name(Component.translatable("bingo.goal.milk_mooshroom"))
            .icon(Items.MOOSHROOM_SPAWN_EGG)
            .infrequency(2));
        addGoal(BingoGoal.builder(id("shear_mooshroom"))
            .criterion("obtain", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
                ItemPredicate.Builder.item().of(Items.SHEARS),
                EntityPredicate.wrap(EntityPredicate.Builder.entity().of(EntityType.MOOSHROOM).build())))
            .tags(BingoTags.ACTION, BingoTags.RARE_BIOME, BingoTags.OVERWORLD)
            .name(Component.translatable("bingo.goal.shear_mooshroom"))
            .icon(Items.COW_SPAWN_EGG)
            .infrequency(2));
        addGoal(obtainItemGoal(id("sea_lantern"), Items.SEA_LANTERN, 2, 5)
            .tags(BingoTags.OVERWORLD, BingoTags.OCEAN));
        addGoal(obtainItemGoal(id("sponge"), Items.SPONGE)
            .tooltip(Component.translatable("bingo.goal.sponge.tooltip"))
            .tags(BingoTags.OCEAN, BingoTags.OVERWORLD));
        addGoal(BingoGoal.builder(id("listen_to_music"))
            .criterion("obtain", ItemUsedOnLocationTrigger.TriggerInstance.itemUsedOnBlock(
                LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(Blocks.JUKEBOX).build()),
                ItemPredicate.Builder.item().of(ItemTags.MUSIC_DISCS)))
            .tags(BingoTags.ITEM)
            .name(Component.translatable("bingo.goal.listen_to_music"))
            .icon(Items.JUKEBOX));
        addGoal(obtainSomeItemsFromTag(id("flowers"), Items.AZURE_BLUET, BingoItemTags.FLOWERS, "bingo.goal.flowers", 11, 14)
            .antisynergy("flowers")
            .infrequency(3)
            .tags(BingoTags.RARE_BIOME, BingoTags.OVERWORLD));
        addGoal(obtainItemGoal(id("diamond_block"), Items.DIAMOND_BLOCK, 2, 4)
            .infrequency(2));
        addGoal(BingoGoal.builder(id("zombified_piglin_sword"))
            .criterion("pickup", ItemPickedUpTrigger.TriggerInstance.pickedUpFrom(
                ItemPredicate.Builder.item().of(Items.GOLDEN_SWORD).build(),
                EntityPredicate.Builder.entity().of(EntityType.ZOMBIFIED_PIGLIN).build()
            ))
            .reactant("pacifist")
            .tags(BingoTags.ITEM, BingoTags.COMBAT, BingoTags.NETHER)
            .name(Component.translatable("bingo.goal.zombified_piglin_sword"))
            .icon(Items.GOLDEN_SWORD));
        // TODO: finish by launching foreworks of n different colors
        addGoal(BingoGoal.builder(id("nametag_enderman"))
            .criterion("nametag", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
                ItemPredicate.Builder.item().of(Items.NAME_TAG),
                EntityPredicate.wrap(EntityPredicate.Builder.entity().of(EntityType.ENDERMAN).build())))
            .tags(BingoTags.ACTION, BingoTags.COMBAT, BingoTags.OVERWORLD)
            .name(Component.translatable("bingo.goal.nametag_enderman"))
            .icon(Items.NAME_TAG));
        // TODO: finish on top of blaze spawner
        addGoal(obtainSomeItemsFromTag(id("wool"), Items.PURPLE_WOOL, ItemTags.WOOL, "bingo.goal.wool", 12, 14)
            .antisynergy("wool_color")
            .infrequency(4)
            .tags(BingoTags.COLOR, BingoTags.OVERWORLD));
        addGoal(obtainSomeItemsFromTag(id("terracotta"), Items.PURPLE_TERRACOTTA, ItemTags.TERRACOTTA, "bingo.goal.terracotta", 12, 14)
            .reactant("use_furnace")
            .antisynergy("terracotta_color")
            .infrequency(4)
            .tags(BingoTags.COLOR, BingoTags.OVERWORLD));
        addGoal(obtainSomeItemsFromTag(id("glazed_terracotta"), Items.PURPLE_GLAZED_TERRACOTTA, BingoItemTags.GLAZED_TERRACOTTA, "bingo.goal.glazed_terracotta", 11, 14)
            .reactant("use_furnace")
            .antisynergy("glazed_terracotta_color")
            .infrequency(4)
            .tags(BingoTags.COLOR, BingoTags.OVERWORLD));
        addGoal(obtainSomeItemsFromTag(id("concrete"), Items.PURPLE_CONCRETE, BingoItemTags.CONCRETE, "bingo.goal.concrete", 12, 14)
            .antisynergy("concrete_color")
            .infrequency(4)
            .tags(BingoTags.COLOR, BingoTags.OVERWORLD));
        addGoal(bedRowGoal(id("bed_row"), 11, 14));
        addGoal(BingoGoal.builder(id("poison_parrot"))
            .criterion("poison", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(
                ItemPredicate.Builder.item().of(Items.COOKIE),
                EntityPredicate.wrap(EntityPredicate.Builder.entity().of(EntityType.PARROT).build())))
            .tags(BingoTags.ACTION, BingoTags.RARE_BIOME, BingoTags.OVERWORLD)
            .name(Component.translatable("bingo.goal.poison_parrot"))
            .icon(Items.COOKIE)
            .infrequency(2)
            .reactant("pacifist"));
        addGoal(BingoGoal.builder(id("tame_parrot"))
            .criterion("tame", TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().of(EntityType.PARROT).build()))
            .tags(BingoTags.ACTION, BingoTags.RARE_BIOME, BingoTags.OVERWORLD)
            .name(Component.translatable("bingo.goal.tame_parrot"))
            .icon(Items.PARROT_SPAWN_EGG)
            .infrequency(2));
        addGoal(BingoGoal.builder(id("ice_on_magma"))
            .criterion("obtain", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(
                AnyOfCondition.anyOf(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.ICE),
                    LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.MAGMA_BLOCK)),
                BlockPatternCondition.builder().aisle("i", "m")
                    .where('i', BlockPredicate.Builder.block().of(Blocks.ICE).build())
                    .where('m', BlockPredicate.Builder.block().of(Blocks.MAGMA_BLOCK).build())
                    .rotations(BlockPattern.Rotations.NONE)))
            .tags(BingoTags.ITEM, BingoTags.BUILD, BingoTags.OVERWORLD)
            .name(Component.translatable("bingo.goal.ice_on_magma"))
            .icon(Items.BASALT));
        addGoal(obtainLevelsGoal(id("levels"), 27, 37));
        addGoal(blockCubeGoal(id("ice_cube"), Blocks.ICE, BlockTags.ICE, Blocks.ICE.getName()));
        // TODO: finish on top of stairway to heaven
        addGoal(BingoGoal.builder(id("kill_ghast_in_overworld"))
            .criterion("murder", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(EntityType.GHAST)
                .located(LocationPredicate.inDimension(Level.OVERWORLD))))
            .name(Component.translatable("bingo.goal.kill_ghast_in_overworld"))
            .tags(BingoTags.ACTION, BingoTags.NETHER, BingoTags.OVERWORLD)
            .icon(Items.GHAST_TEAR)
            .reactant("pacifist"));
        addGoal(obtainItemGoal(id("enchanted_golden_apple"), Items.ENCHANTED_GOLDEN_APPLE));

        addGoal(BingoGoal.builder(id("never_wear_armor_or_use_shields"))
            .criterion("equip", EquipItemTrigger.builder().newItem(ItemPredicate.Builder.item().of(BingoItemTags.ARMOR).build()).build())
            .criterion("use", new UsingItemTrigger.TriggerInstance(ContextAwarePredicate.ANY, ItemPredicate.Builder.item().of(BingoItemTags.SHIELDS).build()))
            .requirements(List.of("equip", "use"))
            .tags(BingoTags.NEVER)
            .name(Component.translatable("bingo.goal.never_wear_armor_or_use_shields"))
            .tooltip(Component.translatable("bingo.goal.never_wear_armor_or_use_shields.tooltip"))
            .icon(makeItemWithGlint(Items.SHIELD))
            .antisynergy("never_wear_armor")
            .catalyst("wear_armor"));
        // TODO: kill mob that is wearing full armor
        // TODO: enchant 5 items
        addGoal(BingoGoal.builder(id("never_use_buckets"))
            .criterion("filled_bucket", FilledBucketTrigger.TriggerInstance.filledBucket(ItemPredicate.ANY))
            .criterion("placed_block", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(MatchTool.toolMatches(ItemPredicate.Builder.item().of(BingoItemTags.BUCKETS))))
            .criterion("use_on_entity", PlayerInteractTrigger.TriggerInstance.itemUsedOnEntity(ItemPredicate.Builder.item().of(BingoItemTags.BUCKETS), ContextAwarePredicate.ANY))
            .criterion("consume", ConsumeItemTrigger.TriggerInstance.usedItem(ItemPredicate.Builder.item().of(BingoItemTags.BUCKETS).build()))
            .requirements(List.of("filled_bucket", "placed_block", "use_on_entity", "consume"))
            .tags(BingoTags.NEVER)
            .catalyst("use_buckets")
            .name(Component.translatable("bingo.goal.never_use_buckets"))
            .icon(Items.BUCKET));
        addGoal(obtainItemGoal(id("conduit"), Items.CONDUIT)
            .tags(BingoTags.OCEAN, BingoTags.OVERWORLD));
        // TODO: 2-5 types of dead coral blocks
        addGoal(obtainItemGoal(id("sea_pickle"), Items.SEA_PICKLE, 16, 32)
            .tags(BingoTags.OCEAN, BingoTags.RARE_BIOME, BingoTags.OVERWORLD));
        // TODO: didn't want to live in the same world as death message
        addGoal(obtainItemGoal(id("cookie"), Items.COOKIE)
            .tags(BingoTags.OVERWORLD));
        // TODO: grow full jungle tree
        addGoal(obtainItemGoal(id("prismarine_shard"), Items.PRISMARINE_SHARD, 2, 10)
            .infrequency(2)
            .tags(BingoTags.OCEAN, BingoTags.OVERWORLD));
        addGoal(obtainItemGoal(id("jungle_log"), Items.JUNGLE_LOG, 16, 32)
            .infrequency(4)
            .tags(BingoTags.RARE_BIOME, BingoTags.OVERWORLD));
        addGoal(obtainItemGoal(id("jungle_wood"), Items.JUNGLE_WOOD, 11, 20)
            .infrequency(4)
            .tags(BingoTags.RARE_BIOME, BingoTags.OVERWORLD));
        addGoal(obtainItemGoal(id("stripped_jungle_wood"), Items.STRIPPED_JUNGLE_WOOD, 11, 20)
            .reactant("axe_use")
            .infrequency(4)
            .tags(BingoTags.RARE_BIOME, BingoTags.OVERWORLD));
        addGoal(obtainItemGoal(id("stripped_jungle_log"), Items.STRIPPED_JUNGLE_LOG, 11, 20)
            .reactant("axe_use")
            .infrequency(4)
            .tags(BingoTags.RARE_BIOME, BingoTags.OVERWORLD));
        // TODO: different diamond items
        // TODO: destroy mob spawner
        addGoal(obtainItemGoal(id("popped_chorus_fruit"), Items.POPPED_CHORUS_FRUIT, 32, 64)
            .tags(BingoTags.END));
        // TODO: get villager into the end
        addGoal(obtainItemGoal(id("dragon_breath"), Items.DRAGON_BREATH, 5, 16)
            .tags(BingoTags.COMBAT, BingoTags.END));
        addGoal(obtainItemGoal(id("dragon_egg"), Items.DRAGON_EGG)
            .tags(BingoTags.COMBAT, BingoTags.END));
        addGoal(BingoGoal.builder(id("complete_full_size_map"))
            .criterion("complete", CompleteMapTrigger.TriggerInstance.completeMap(
                MinMaxBounds.Ints.atLeast(MapItemSavedData.MAX_SCALE)
            ))
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD)
            .name(Component.translatable("bingo.goal.complete_full_size_map"))
            .icon(Items.FILLED_MAP)
            .antisynergy("complete_map"));
        // TODO: be killed by a villager
        addGoal(BingoGoal.builder(id("pop_totem"))
            .criterion("totem", UsedTotemTrigger.TriggerInstance.usedTotem(Items.TOTEM_OF_UNDYING))
            .name(Component.translatable("bingo.goal.pop_totem"))
            .icon(Items.TOTEM_OF_UNDYING)
            .tags(BingoTags.ITEM, BingoTags.OVERWORLD));
        // TODO: every type of sword
        // TODO: every type of pickaxe
        addGoal(BingoGoal.builder(id("pacifist"))
            .criterion("kill", KilledTrigger.TriggerInstance.playerKilledEntity())
            .tags(BingoTags.NEVER, BingoTags.STAT)
            .name(Component.translatable("bingo.goal.pacifist"))
            .tooltip(Component.translatable("bingo.goal.pacifist.tooltip"))
            .icon(Items.DIAMOND_SWORD)
            .catalyst("pacifist"));
        // TODO: finish by scaffolding tower then removing it
        // TODO: feed panda cake
        // TODO: breed pandas
        // TODO: disarm pillager
        // TODO: stun ravager
        // TODO: hero of the village
        // TODO: gail ocelot's trust
        addGoal(obtainItemGoal(id("ender_eye"), Items.ENDER_EYE, 12, 12)
            .reactant("pacifist")
            .tags(BingoTags.NETHER, BingoTags.COMBAT)
            .tooltip(Component.translatable("bingo.goal.ender_eye.hard.tooltip")));
        addGoal(obtainItemGoal(id("netherite_ingot"), Items.NETHERITE_INGOT)
            .tags(BingoTags.NETHER));
        addGoal(obtainItemGoal(id("wither_skeleton_skull"), Items.WITHER_SKELETON_SKULL)
            .reactant("pacifist")
            .tags(BingoTags.NETHER, BingoTags.COMBAT, BingoTags.RARE_BIOME));
        addGoal(obtainItemGoal(id("gilded_blackstone"), Items.GILDED_BLACKSTONE)
            .tags(BingoTags.NETHER, BingoTags.RARE_BIOME));
        // TODO: make compass point to lodestone
        // TODO: give piglin brute enchanted axe
        addGoal(BingoGoal.builder(id("6x6scaffolding"))
            .criterion("obtain", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SCAFFOLDING),
                BlockPatternCondition.builder().aisle(
                    "########",
                    "#      #",
                    "#      #",
                    "#      #",
                    "#      #",
                    "#      #",
                    "#      #"
                ).where('#', BlockPredicate.Builder.block().of(Blocks.SCAFFOLDING).build())))
            .tags(BingoTags.OVERWORLD, BingoTags.BUILD)
            .name(Component.translatable("bingo.goal.6x6scaffolding"))
            .tooltip(Component.translatable("bingo.goal.6x6scaffolding.tooltip"))
            .tooltipIcon(new ResourceLocation("bingo:textures/gui/tooltips/6x6scaffolding.png"))
            .icon(Items.SCAFFOLDING));
        addGoal(obtainItemGoal(id("honey_block"), Items.HONEY_BLOCK, 2, 5)
            .setAntisynergy("honey")
            .infrequency(2)
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD));
        addGoal(obtainItemGoal(id("honeycomb_block"), Items.HONEYCOMB_BLOCK, 6, 15)
            .setAntisynergy("honeycomb")
            .infrequency(2)
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD));
        // TODO: kill a wandering trader
        // TODO: cure a zombie villager
        // TODO: throw mending book into lava
        // TODO: never smelt with furnaces
        // TODO: throw huge nether fungus in overworld
        // TODO: 32-64 dirt, netherrack and end stone
        addGoal(BingoGoal.builder(id("tame_mule"))
            .criterion("obtain", TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().of(EntityType.MULE).build()))
            .name(Component.translatable("bingo.goal.tame_mule"))
            .icon(Items.MULE_SPAWN_EGG)
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD));
        addGoal(BingoGoal.builder(id("carrot_stick_to_rod"))
            .criterion("break", ItemBrokenTrigger.TriggerInstance.itemBroken(ItemPredicate.Builder.item().of(Items.CARROT_ON_A_STICK)))
            .tags(BingoTags.ACTION, BingoTags.OVERWORLD)
            .name(Component.translatable(
                "bingo.goal.carrot_stick_to_rod",
                Items.CARROT_ON_A_STICK.getDescription(),
                Items.FISHING_ROD.getDescription()
            ))
            .icon(Items.CARROT_ON_A_STICK));
        addGoal(obtainItemGoal(id("skull_banner_pattern"), Items.SKULL_BANNER_PATTERN)
            .tags(BingoTags.NETHER, BingoTags.COMBAT, BingoTags.RARE_BIOME, BingoTags.OVERWORLD)
            .name(Component.translatable(
                "bingo.goal.skull_banner_pattern",
                Component.translatable("item.minecraft.skull_banner_pattern.desc"),
                Component.translatable("item.minecraft.skull_banner_pattern")
            ))
            .icon(makeBannerWithPattern(Items.WHITE_BANNER, BannerPatterns.SKULL, DyeColor.BLACK)));
        addGoal(obtainItemGoal(id("turtle_helmet"), Items.TURTLE_HELMET)
            .tags(BingoTags.OCEAN, BingoTags.OVERWORLD));
    }
}