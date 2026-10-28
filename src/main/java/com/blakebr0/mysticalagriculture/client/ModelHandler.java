package com.blakebr0.mysticalagriculture.client;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.crop.CropTextures;
import com.blakebr0.mysticalagriculture.init.ModItems;
import com.blakebr0.mysticalagriculture.item.ExperienceCapsuleItem;
import com.blakebr0.mysticalagriculture.item.SoulJarItem;
import com.blakebr0.mysticalagriculture.item.tool.EssenceBowItem;
import com.blakebr0.mysticalagriculture.item.tool.EssenceCrossbowItem;
import com.blakebr0.mysticalagriculture.item.tool.EssenceFishingRodItem;
import com.blakebr0.mysticalagriculture.registry.CropRegistry;
import com.google.common.base.Stopwatch;
import io.github.xponer2.translator.client.ItemModelProperties;
import io.github.xponer2.translator.client.ModelEvent;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import io.github.xponer2.translator.client.ModelData;
import io.github.xponer2.translator.util.BuiltInRegistries;
import io.github.xponer2.translator.util.Platform;

public final class ModelHandler {
    private static final ResourceLocation MISSING_NO = new ResourceLocation("minecraft", "missingno");

    @SubscribeEvent
    public void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
        for (int i = 0; i < 8; i++) {
            event.register(ModelEvent.standalone(MysticalAgriculture.resource("block/mystical_resource_crop_" + i)));
            event.register(ModelEvent.standalone(MysticalAgriculture.resource("block/mystical_mob_crop_" + i)));
        }

        for (var type : CropRegistry.getInstance().getTypes()) {
            event.register(ModelEvent.standalone(Platform.withSuffix(CropTextures.FLOWER_INGOT_BLANK, "_" + type.getName())));
            event.register(ModelEvent.standalone(Platform.withSuffix(CropTextures.FLOWER_ROCK_BLANK, "_" + type.getName())));
            event.register(ModelEvent.standalone(Platform.withSuffix(CropTextures.FLOWER_DUST_BLANK, "_" + type.getName())));
            event.register(ModelEvent.standalone(Platform.withSuffix(CropTextures.FLOWER_FACE_BLANK, "_" + type.getName())));
        }

        event.register(ModelEvent.standalone(CropTextures.ESSENCE_INGOT_BLANK));
        event.register(ModelEvent.standalone(CropTextures.ESSENCE_ROCK_BLANK));
        event.register(ModelEvent.standalone(CropTextures.ESSENCE_DUST_BLANK));
        event.register(ModelEvent.standalone(CropTextures.ESSENCE_GEM_BLANK));
        event.register(ModelEvent.standalone(CropTextures.ESSENCE_TALL_GEM_BLANK));
        event.register(ModelEvent.standalone(CropTextures.ESSENCE_DIAMOND_BLANK));
        event.register(ModelEvent.standalone(CropTextures.ESSENCE_QUARTZ_BLANK));
        event.register(ModelEvent.standalone(CropTextures.ESSENCE_FLAME_BLANK));
        event.register(ModelEvent.standalone(CropTextures.ESSENCE_ROD_BLANK));

        event.register(ModelEvent.standalone(CropTextures.SEED_BLANK));
    }

    @SubscribeEvent
    public void onModifyBakingResults(ModelEvent.ModifyBakingResult event) {
        var stopwatch = Stopwatch.createStarted();
        var registry = event.getModels();
        var cropModels = new HashMap<ResourceLocation, IBakedModel[]>();

        for (var cropType : CropRegistry.getInstance().getTypes()) {
            cropModels.put(cropType.getId(), IntStream.range(0, 7)
                    .mapToObj(i -> registry.get(ModelEvent.standalone(Platform.withSuffix(cropType.getStemModel(), "_" + i))))
                    .toArray(IBakedModel[]::new));
        }

        for (var crop : CropRegistry.getInstance().getCrops()) {
            var textures = crop.getTextures();
            var crops = crop.getCropBlock();
            var cropId = BuiltInRegistries.BLOCK.getKey(crops);

            {
                for (int i = 0; i < 7; i++) {
                    var location = new ModelResourceLocation(cropId, "age=" + i);
                    var bakedModel = registry.get(location);

                    if (bakedModel == null || bakedModel.getParticleTexture().getIconName().equals(MISSING_NO.getPath())) {
                        var type = crop.getType().getId();
                        registry.replace(location, cropModels.get(type)[i]);
                    }
                }

                var location = new ModelResourceLocation(cropId, "age=7");
                var bakedModel = registry.get(location);

                if (bakedModel == null || bakedModel.getParticleTexture().getIconName().equals(MISSING_NO.getPath())) {
                    var flower = textures.getFlowerTexture();
                    var type = crop.getType().getId();
                    var texture = new ResourceLocation(type.getNamespace(), flower.getPath() + "_" + type.getPath());
                    var model = getBakedModel(texture, registry);

                    registry.replace(location, model);
                }
            }

            var essence = crop.getEssenceItem();
            var essenceId = BuiltInRegistries.ITEM.getKey(essence);

            {
                var location = ModelEvent.inventory(essenceId);
                var bakedModel = registry.get(location);

                if (bakedModel == null || bakedModel.getParticleTexture().getIconName().equals(MISSING_NO.getPath())) {
                    var texture = textures.getEssenceTexture();
                    var model = getBakedModel(texture, registry);

                    registry.replace(location, model);
                }
            }

            var seeds = crop.getSeedsItem();
            var seedsId = BuiltInRegistries.ITEM.getKey(seeds);

            {
                var location = ModelEvent.inventory(seedsId);
                var bakedModel = registry.get(location);

                if (bakedModel == null || bakedModel.getParticleTexture().getIconName().equals(MISSING_NO.getPath())) {
                    var texture = textures.getSeedTexture();
                    var model = getBakedModel(texture, registry);

                    registry.replace(location, model);
                }
            }
        }

        stopwatch.stop();

        MysticalAgriculture.LOGGER.info("Model replacement took {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    public static void onClientSetup(FMLInitializationEvent event) {
        Platform.run(() -> {
            ItemModelProperties.register(ModItems.EXPERIENCE_CAPSULE.get(), new ResourceLocation("fill"), ExperienceCapsuleItem.getFillPropertyGetter());
            ItemModelProperties.register(ModItems.SOUL_JAR.get(), new ResourceLocation("fill"), SoulJarItem.getFillPropertyGetter());
            ItemModelProperties.register(ModItems.INFERIUM_BOW.get(), new ResourceLocation("pull"), EssenceBowItem.getPullPropertyGetter());
            ItemModelProperties.register(ModItems.INFERIUM_BOW.get(), new ResourceLocation("pulling"), EssenceBowItem.getPullingPropertyGetter());
            ItemModelProperties.register(ModItems.INFERIUM_CROSSBOW.get(), new ResourceLocation("pull"), EssenceCrossbowItem.getPullPropertyGetter());
            ItemModelProperties.register(ModItems.INFERIUM_CROSSBOW.get(), new ResourceLocation("pulling"), EssenceCrossbowItem.getPullingPropertyGetter());
            ItemModelProperties.register(ModItems.INFERIUM_CROSSBOW.get(), new ResourceLocation("charged"), EssenceCrossbowItem.getChargedPropertyGetter());
            ItemModelProperties.register(ModItems.INFERIUM_CROSSBOW.get(), new ResourceLocation("firework"), EssenceCrossbowItem.getFireworkPropertyGetter());
            ItemModelProperties.register(ModItems.INFERIUM_FISHING_ROD.get(), new ResourceLocation("cast"), EssenceFishingRodItem.getCastPropertyGetter());
            ItemModelProperties.register(ModItems.PRUDENTIUM_BOW.get(), new ResourceLocation("pull"), EssenceBowItem.getPullPropertyGetter());
            ItemModelProperties.register(ModItems.PRUDENTIUM_BOW.get(), new ResourceLocation("pulling"), EssenceBowItem.getPullingPropertyGetter());
            ItemModelProperties.register(ModItems.PRUDENTIUM_CROSSBOW.get(), new ResourceLocation("pull"), EssenceCrossbowItem.getPullPropertyGetter());
            ItemModelProperties.register(ModItems.PRUDENTIUM_CROSSBOW.get(), new ResourceLocation("pulling"), EssenceCrossbowItem.getPullingPropertyGetter());
            ItemModelProperties.register(ModItems.PRUDENTIUM_CROSSBOW.get(), new ResourceLocation("charged"), EssenceCrossbowItem.getChargedPropertyGetter());
            ItemModelProperties.register(ModItems.PRUDENTIUM_CROSSBOW.get(), new ResourceLocation("firework"), EssenceCrossbowItem.getFireworkPropertyGetter());
            ItemModelProperties.register(ModItems.PRUDENTIUM_FISHING_ROD.get(), new ResourceLocation("cast"), EssenceFishingRodItem.getCastPropertyGetter());
            ItemModelProperties.register(ModItems.TERTIUM_BOW.get(), new ResourceLocation("pull"), EssenceBowItem.getPullPropertyGetter());
            ItemModelProperties.register(ModItems.TERTIUM_BOW.get(), new ResourceLocation("pulling"), EssenceBowItem.getPullingPropertyGetter());
            ItemModelProperties.register(ModItems.TERTIUM_CROSSBOW.get(), new ResourceLocation("pull"), EssenceCrossbowItem.getPullPropertyGetter());
            ItemModelProperties.register(ModItems.TERTIUM_CROSSBOW.get(), new ResourceLocation("pulling"), EssenceCrossbowItem.getPullingPropertyGetter());
            ItemModelProperties.register(ModItems.TERTIUM_CROSSBOW.get(), new ResourceLocation("charged"), EssenceCrossbowItem.getChargedPropertyGetter());
            ItemModelProperties.register(ModItems.TERTIUM_CROSSBOW.get(), new ResourceLocation("firework"), EssenceCrossbowItem.getFireworkPropertyGetter());
            ItemModelProperties.register(ModItems.TERTIUM_FISHING_ROD.get(), new ResourceLocation("cast"), EssenceFishingRodItem.getCastPropertyGetter());
            ItemModelProperties.register(ModItems.IMPERIUM_BOW.get(), new ResourceLocation("pull"), EssenceBowItem.getPullPropertyGetter());
            ItemModelProperties.register(ModItems.IMPERIUM_BOW.get(), new ResourceLocation("pulling"), EssenceBowItem.getPullingPropertyGetter());
            ItemModelProperties.register(ModItems.IMPERIUM_CROSSBOW.get(), new ResourceLocation("pull"), EssenceCrossbowItem.getPullPropertyGetter());
            ItemModelProperties.register(ModItems.IMPERIUM_CROSSBOW.get(), new ResourceLocation("pulling"), EssenceCrossbowItem.getPullingPropertyGetter());
            ItemModelProperties.register(ModItems.IMPERIUM_CROSSBOW.get(), new ResourceLocation("charged"), EssenceCrossbowItem.getChargedPropertyGetter());
            ItemModelProperties.register(ModItems.IMPERIUM_CROSSBOW.get(), new ResourceLocation("firework"), EssenceCrossbowItem.getFireworkPropertyGetter());
            ItemModelProperties.register(ModItems.IMPERIUM_FISHING_ROD.get(), new ResourceLocation("cast"), EssenceFishingRodItem.getCastPropertyGetter());
            ItemModelProperties.register(ModItems.SUPREMIUM_BOW.get(), new ResourceLocation("pull"), EssenceBowItem.getPullPropertyGetter());
            ItemModelProperties.register(ModItems.SUPREMIUM_BOW.get(), new ResourceLocation("pulling"), EssenceBowItem.getPullingPropertyGetter());
            ItemModelProperties.register(ModItems.SUPREMIUM_CROSSBOW.get(), new ResourceLocation("pull"), EssenceCrossbowItem.getPullPropertyGetter());
            ItemModelProperties.register(ModItems.SUPREMIUM_CROSSBOW.get(), new ResourceLocation("pulling"), EssenceCrossbowItem.getPullingPropertyGetter());
            ItemModelProperties.register(ModItems.SUPREMIUM_CROSSBOW.get(), new ResourceLocation("charged"), EssenceCrossbowItem.getChargedPropertyGetter());
            ItemModelProperties.register(ModItems.SUPREMIUM_CROSSBOW.get(), new ResourceLocation("firework"), EssenceCrossbowItem.getFireworkPropertyGetter());
            ItemModelProperties.register(ModItems.SUPREMIUM_FISHING_ROD.get(), new ResourceLocation("cast"), EssenceFishingRodItem.getCastPropertyGetter());
            ItemModelProperties.register(ModItems.AWAKENED_SUPREMIUM_BOW.get(), new ResourceLocation("pull"), EssenceBowItem.getPullPropertyGetter());
            ItemModelProperties.register(ModItems.AWAKENED_SUPREMIUM_BOW.get(), new ResourceLocation("pulling"), EssenceBowItem.getPullingPropertyGetter());
            ItemModelProperties.register(ModItems.AWAKENED_SUPREMIUM_CROSSBOW.get(), new ResourceLocation("pull"), EssenceCrossbowItem.getPullPropertyGetter());
            ItemModelProperties.register(ModItems.AWAKENED_SUPREMIUM_CROSSBOW.get(), new ResourceLocation("pulling"), EssenceCrossbowItem.getPullingPropertyGetter());
            ItemModelProperties.register(ModItems.AWAKENED_SUPREMIUM_CROSSBOW.get(), new ResourceLocation("charged"), EssenceCrossbowItem.getChargedPropertyGetter());
            ItemModelProperties.register(ModItems.AWAKENED_SUPREMIUM_CROSSBOW.get(), new ResourceLocation("firework"), EssenceCrossbowItem.getFireworkPropertyGetter());
            ItemModelProperties.register(ModItems.AWAKENED_SUPREMIUM_FISHING_ROD.get(), new ResourceLocation("cast"), EssenceFishingRodItem.getCastPropertyGetter());
        });
    }

    @Nullable // check for both standalone and inventory variants just in case
    private static IBakedModel getBakedModel(ResourceLocation location, Map<ModelResourceLocation, IBakedModel> registry) {
        var path = ModelEvent.standalone(location);
        var model = registry.get(path);

        if (model != null)
            return model;

        path = ModelEvent.inventory(location);
        model = registry.get(path);

        return model;
    }
}
