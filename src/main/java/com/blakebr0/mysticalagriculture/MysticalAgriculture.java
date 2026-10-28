package com.blakebr0.mysticalagriculture;

import com.blakebr0.mysticalagriculture.api.MysticalAgricultureAPI;
import com.blakebr0.mysticalagriculture.api.MysticalAgricultureConfigValues;
import com.blakebr0.mysticalagriculture.init.ModArmorMaterials;
import com.blakebr0.mysticalagriculture.init.ModBlocks;
import com.blakebr0.mysticalagriculture.init.ModConditionSerializers;
import com.blakebr0.mysticalagriculture.init.ModCreativeModeTabs;
import com.blakebr0.mysticalagriculture.init.ModDataComponentTypes;
import com.blakebr0.mysticalagriculture.init.ModIngredientTypes;
import com.blakebr0.mysticalagriculture.init.ModItems;
import com.blakebr0.mysticalagriculture.init.ModMenuTypes;
import com.blakebr0.mysticalagriculture.init.ModRecipeSerializers;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import com.blakebr0.mysticalagriculture.init.ModTileEntities;
import com.blakebr0.mysticalagriculture.registry.AugmentRegistry;
import com.blakebr0.mysticalagriculture.registry.CropRegistry;
import com.blakebr0.mysticalagriculture.registry.MobSoulTypeRegistry;
import com.blakebr0.mysticalagriculture.registry.PluginRegistry;
import com.blakebr0.cucumber.Cucumber;
import io.github.xponer2.translator.Translator;
import io.github.xponer2.translator.recipe.RecipeLoader;
import com.blakebr0.mysticalagriculture.compat.TOPCompat;
import com.blakebr0.mysticalagriculture.config.ModConfigs;
import com.blakebr0.mysticalagriculture.crafting.DynamicRecipeManager;
import com.blakebr0.mysticalagriculture.crafting.EssenceVesselColorManager;
import com.blakebr0.mysticalagriculture.handler.AugmentHandler;
import com.blakebr0.mysticalagriculture.handler.ExperienceCapsuleHandler;
import com.blakebr0.mysticalagriculture.handler.MobDropHandler;
import com.blakebr0.mysticalagriculture.handler.MobSoulHandler;
import com.blakebr0.mysticalagriculture.handler.RegisterCapabilityHandler;
import com.blakebr0.mysticalagriculture.handler.TinkerableHandler;
import com.blakebr0.mysticalagriculture.network.NetworkHandler;
import com.blakebr0.mysticalagriculture.util.RecipeIngredientCache;
import com.blakebr0.mysticalagriculture.world.ModWorldGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = MysticalAgriculture.MOD_ID, name = MysticalAgricultureBuild.MOD_NAME, version = MysticalAgricultureBuild.VERSION,
		dependencies = "required-after:" + Translator.MOD_ID + "@[" + Translator.VERSION + ",);required-after:cucumber@[" + Cucumber.VERSION + ",)", acceptedMinecraftVersions = "[1.12.2]")
public final class MysticalAgriculture {
	public static final String MOD_ID = "mysticalagriculture";
	public static final String NAME = "Mystical Agriculture";
	public static final Logger LOGGER = LogManager.getLogger(NAME);

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ModConfigs.COMMON.register("mysticalagriculture-common.cfg");

		// 1.21 mod constructor: registries (filled in the 1.12.2 registry events) and the crop/augment registration
		MinecraftForge.EVENT_BUS.register(new ModBlocks());
		MinecraftForge.EVENT_BUS.register(new ModItems());
		ModArmorMaterials.REGISTRY.register(null);
		ModCreativeModeTabs.REGISTRY.register(null);
		ModTileEntities.REGISTRY.register(null);
		ModDataComponentTypes.REGISTRY.register(null);
		ModMenuTypes.REGISTRY.register(null);
		ModIngredientTypes.REGISTRY.register(null);
		ModConditionSerializers.REGISTRY.register(null);
		ModRecipeTypes.REGISTRY.register(null);
		ModRecipeSerializers.REGISTRY.register(null);

		initAPI();
		PluginRegistry.getInstance().loadPlugins();
		// payloads are registered when Cucumber fires RegisterPayloadHandlersEvent (init)
		MinecraftForge.EVENT_BUS.register(new NetworkHandler());
		// Cucumber fires RegisterCapabilitiesEvent in init
		MinecraftForge.EVENT_BUS.register(new RegisterCapabilityHandler());

		// 1.21 common setup
		MinecraftForge.EVENT_BUS.register(new MobDropHandler());
		MinecraftForge.EVENT_BUS.register(new MobSoulHandler());
		MinecraftForge.EVENT_BUS.register(new ExperienceCapsuleHandler());
		MinecraftForge.EVENT_BUS.register(new AugmentHandler());
		MinecraftForge.EVENT_BUS.register(new TinkerableHandler());
		MinecraftForge.EVENT_BUS.register(DynamicRecipeManager.INSTANCE);
		MinecraftForge.EVENT_BUS.register(EssenceVesselColorManager.INSTANCE);
		MinecraftForge.EVENT_BUS.register(RecipeIngredientCache.INSTANCE);
		// 1.21 TagsUpdatedEvent (SERVER_DATA_LOAD): recipes and tags are loaded in Cucumber's post init
		RecipeLoader.onLoaded(manager -> RecipeIngredientCache.INSTANCE.onRecipesLoaded());

		if (event.getSide().isClient()) {
			com.blakebr0.mysticalagriculture.client.ModClient.preInit();
		}
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		// 1.21 common setup: the registries are complete
		CropRegistry.getInstance().onCommonSetup();
		AugmentRegistry.getInstance().onCommonSetup();
		MobSoulTypeRegistry.getInstance().onCommonSetup();

		ModWorldGenerator.register();
		if (event.getSide().isClient()) {
			com.blakebr0.mysticalagriculture.client.ModClient.init(event);
		}

		// 1.21 reload listeners: the data colors from the mod jars, the resource pack colors on the client
		EssenceVesselColorManager.INSTANCE.load();
		if (event.getSide().isClient()) {
			com.blakebr0.mysticalagriculture.client.EssenceVesselColorManager.register();
		}

		// 1.21 InterModEnqueueEvent: IMC sent during init is processed before postInit
		if (ModConfigs.isTheOneProbeInstalled()) {
			TOPCompat.onInterModEnqueue();
		}
	}

	private static void initAPI() {
		try {
			var api = MysticalAgricultureAPI.class;

			var cropRegistry = api.getDeclaredField("cropRegistry");
			var augmentRegistry = api.getDeclaredField("augmentRegistry");
			var soulTypeRegistry = api.getDeclaredField("soulTypeRegistry");

			cropRegistry.setAccessible(true);
			cropRegistry.set(null, CropRegistry.getInstance());
			augmentRegistry.setAccessible(true);
			augmentRegistry.set(null, AugmentRegistry.getInstance());
			soulTypeRegistry.setAccessible(true);
			soulTypeRegistry.set(null, MobSoulTypeRegistry.getInstance());

			var configValues = api.getDeclaredField("configValues");

			configValues.setAccessible(true);
			configValues.set(null, new MysticalAgricultureConfigValues(
					ModConfigs.INFERIUM_DROP_CHANCE,
					ModConfigs.FERTILIZED_ESSENCE_DROP_CHANCE,
					ModConfigs.SECONDARY_SEED_DROPS,
					ModConfigs.REQUIRES_EFFECTIVE_FARMLAND,
					ModConfigs.ENCHANTABLE_SUPREMIUM_TOOLS,
					ModConfigs.UNBREAKABLE_SUPREMIUM_ARMOR,
					ModConfigs.FAKE_PLAYER_WATERING
			));
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalStateException("Couldn't set up the Mystical Agriculture API", e);
		}
	}

	public static ResourceLocation resource(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
