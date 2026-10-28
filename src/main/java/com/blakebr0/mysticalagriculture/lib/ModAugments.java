package com.blakebr0.mysticalagriculture.lib;

import com.blakebr0.mysticalagriculture.MysticalAgriculture;
import com.blakebr0.mysticalagriculture.api.registry.IAugmentRegistry;
import com.blakebr0.mysticalagriculture.api.tinkering.Augment;
import com.blakebr0.mysticalagriculture.augment.AbsorptionAugment;
import com.blakebr0.mysticalagriculture.augment.AttackAOEAugment;
import com.blakebr0.mysticalagriculture.augment.BlindnessResistanceAugment;
import com.blakebr0.mysticalagriculture.augment.FireResistanceAugment;
import com.blakebr0.mysticalagriculture.augment.FlightAugment;
import com.blakebr0.mysticalagriculture.augment.HasteAugment;
import com.blakebr0.mysticalagriculture.augment.HealthBoostAugment;
import com.blakebr0.mysticalagriculture.augment.HungerResistanceAugment;
import com.blakebr0.mysticalagriculture.augment.JumpBoostAugment;
import com.blakebr0.mysticalagriculture.augment.LuckAugment;
import com.blakebr0.mysticalagriculture.augment.MiningAOEAugment;
import com.blakebr0.mysticalagriculture.augment.MiningFatigueResistanceAugment;
import com.blakebr0.mysticalagriculture.augment.NauseaResistanceAugment;
import com.blakebr0.mysticalagriculture.augment.NightVisionAugment;
import com.blakebr0.mysticalagriculture.augment.NoFallDamageAugment;
import com.blakebr0.mysticalagriculture.augment.PathingAOEAugment;
import com.blakebr0.mysticalagriculture.augment.PoisonResistanceAugment;
import com.blakebr0.mysticalagriculture.augment.SlowFallingAugment;
import com.blakebr0.mysticalagriculture.augment.SlownessResistanceAugment;
import com.blakebr0.mysticalagriculture.augment.SpeedAugment;
import com.blakebr0.mysticalagriculture.augment.StepAssistAugment;
import com.blakebr0.mysticalagriculture.augment.StrengthAugment;
import com.blakebr0.mysticalagriculture.augment.TillingAOEAugment;
import com.blakebr0.mysticalagriculture.augment.WaterBreathingAugment;
import com.blakebr0.mysticalagriculture.augment.WeaknessResistanceAugment;
import com.blakebr0.mysticalagriculture.augment.WitherResistanceAugment;

public final class ModAugments {
    public static final Augment ABSORPTION_I = new AbsorptionAugment(MysticalAgriculture.resource("absorption_i"), 1, 0);
    public static final Augment LUCK_I = new LuckAugment(MysticalAgriculture.resource("luck_i"), 1, 0);
    public static final Augment HEALTH_BOOST_I = new HealthBoostAugment(MysticalAgriculture.resource("health_boost_i"), 1, 1);
    public static final Augment PATHING_AOE_I = new PathingAOEAugment(MysticalAgriculture.resource("pathing_aoe_i"), 1, 1);
    public static final Augment NAUSEA_RESISTANCE = new NauseaResistanceAugment(MysticalAgriculture.resource("nausea_resistance"), 1);

    public static final Augment NIGHT_VISION = new NightVisionAugment(MysticalAgriculture.resource("night_vision"), 2);
    public static final Augment WATER_BREATHING = new WaterBreathingAugment(MysticalAgriculture.resource("water_breathing"), 2);
    public static final Augment ABSORPTION_II = new AbsorptionAugment(MysticalAgriculture.resource("absorption_ii"), 2,  1);
    public static final Augment JUMP_BOOST_I = new JumpBoostAugment(MysticalAgriculture.resource("jump_boost_i"), 2, 0);
    public static final Augment HEALTH_BOOST_II = new HealthBoostAugment(MysticalAgriculture.resource("health_boost_ii"), 2, 2);
    public static final Augment SPEED_I = new SpeedAugment(MysticalAgriculture.resource("speed_i"), 2, 1);
    public static final Augment MINING_AOE_I = new MiningAOEAugment(MysticalAgriculture.resource("mining_aoe_i"), 2, 1);
    public static final Augment TILLING_AOE_I = new TillingAOEAugment(MysticalAgriculture.resource("tilling_aoe_i"), 2, 1);
    public static final Augment PATHING_AOE_II = new PathingAOEAugment(MysticalAgriculture.resource("pathing_aoe_ii"), 2, 2);
    public static final Augment BLINDNESS_RESISTANCE = new BlindnessResistanceAugment(MysticalAgriculture.resource("blindness_resistance"), 2);

    public static final Augment ABSORPTION_III = new AbsorptionAugment(MysticalAgriculture.resource("absorption_iii"), 3, 2);
    public static final Augment LUCK_II = new LuckAugment(MysticalAgriculture.resource("luck_ii"), 3, 1);
    public static final Augment FIRE_RESISTANCE = new FireResistanceAugment(MysticalAgriculture.resource("fire_resistance"), 3);
    public static final Augment JUMP_BOOST_II = new JumpBoostAugment(MysticalAgriculture.resource("jump_boost_ii"), 3, 1);
    public static final Augment STEP_ASSIST = new StepAssistAugment(MysticalAgriculture.resource("step_assist"), 3);
    public static final Augment HEALTH_BOOST_III = new HealthBoostAugment(MysticalAgriculture.resource("health_boost_iii"), 3, 3);
    public static final Augment STRENGTH_I = new StrengthAugment(MysticalAgriculture.resource("strength_i"), 3, 1);
    public static final Augment SPEED_II = new SpeedAugment(MysticalAgriculture.resource("speed_ii"), 3, 2);
    public static final Augment HASTE_I = new HasteAugment(MysticalAgriculture.resource("haste_i"), 3, 0);
    public static final Augment NO_FALL_DAMAGE = new NoFallDamageAugment(MysticalAgriculture.resource("no_fall_damage"), 3);
    public static final Augment SLOW_FALLING = new SlowFallingAugment(MysticalAgriculture.resource("slow_falling"), 3);
    public static final Augment MINING_AOE_II = new MiningAOEAugment(MysticalAgriculture.resource("mining_aoe_ii"), 3, 2);
    public static final Augment ATTACK_AOE_I = new AttackAOEAugment(MysticalAgriculture.resource("attack_aoe_i"), 3, 1);
    public static final Augment TILLING_AOE_II = new TillingAOEAugment(MysticalAgriculture.resource("tilling_aoe_ii"), 3, 2);
    public static final Augment PATHING_AOE_III = new PathingAOEAugment(MysticalAgriculture.resource("pathing_aoe_iii"), 3, 3);
    public static final Augment WEAKNESS_RESISTANCE = new WeaknessResistanceAugment(MysticalAgriculture.resource("weakness_resistance"), 3);
    public static final Augment SLOWNESS_RESISTANCE = new SlownessResistanceAugment(MysticalAgriculture.resource("slowness_resistance"), 3);

    public static final Augment ABSORPTION_IV = new AbsorptionAugment(MysticalAgriculture.resource("absorption_iv"), 4, 3);
    public static final Augment POISON_RESISTANCE = new PoisonResistanceAugment(MysticalAgriculture.resource("poison_resistance"), 4);
    public static final Augment JUMP_BOOST_III = new JumpBoostAugment(MysticalAgriculture.resource("jump_boost_iii"), 4, 2);
    public static final Augment HEALTH_BOOST_IV = new HealthBoostAugment(MysticalAgriculture.resource("health_boost_iv"), 4, 4);
    public static final Augment STRENGTH_II = new StrengthAugment(MysticalAgriculture.resource("strength_ii"), 4, 2);
    public static final Augment SPEED_III = new SpeedAugment(MysticalAgriculture.resource("speed_iii"), 4, 3);
    public static final Augment HASTE_II = new HasteAugment(MysticalAgriculture.resource("haste_ii"), 4, 1);
    public static final Augment MINING_AOE_III = new MiningAOEAugment(MysticalAgriculture.resource("mining_aoe_iii"), 4, 3);
    public static final Augment ATTACK_AOE_II = new AttackAOEAugment(MysticalAgriculture.resource("attack_aoe_ii"), 4, 2);
    public static final Augment TILLING_AOE_III = new TillingAOEAugment(MysticalAgriculture.resource("tilling_aoe_iii"), 4, 3);
    public static final Augment PATHING_AOE_IV = new PathingAOEAugment(MysticalAgriculture.resource("pathing_aoe_iv"), 4, 4);
    public static final Augment MINING_FATIGUE_RESISTANCE = new MiningFatigueResistanceAugment(MysticalAgriculture.resource("mining_fatigue_resistance"), 4);
    public static final Augment HUNGER_RESISTANCE = new HungerResistanceAugment(MysticalAgriculture.resource("hunger_resistance"), 4);

    public static final Augment ABSORPTION_V = new AbsorptionAugment(MysticalAgriculture.resource("absorption_v"), 5, 4);
    public static final Augment LUCK_III = new LuckAugment(MysticalAgriculture.resource("luck_iii"), 5, 2);
    public static final Augment WITHER_RESISTANCE = new WitherResistanceAugment(MysticalAgriculture.resource("wither_resistance"), 5);
    public static final Augment HEALTH_BOOST_V = new HealthBoostAugment(MysticalAgriculture.resource("health_boost_v"), 5, 5);
    public static final Augment STRENGTH_III = new StrengthAugment(MysticalAgriculture.resource("strength_iii"), 5, 4);
    public static final Augment HASTE_III = new HasteAugment(MysticalAgriculture.resource("haste_iii"), 5, 2);
    public static final Augment FLIGHT = new FlightAugment(MysticalAgriculture.resource("flight"), 5);
    public static final Augment MINING_AOE_IV = new MiningAOEAugment(MysticalAgriculture.resource("mining_aoe_iv"), 5, 4);
    public static final Augment ATTACK_AOE_III = new AttackAOEAugment(MysticalAgriculture.resource("attack_aoe_iii"), 5, 3);
    public static final Augment TILLING_AOE_IV = new TillingAOEAugment(MysticalAgriculture.resource("tilling_aoe_iv"), 5, 4);

    public static void onRegisterAugments(IAugmentRegistry registry) {
        registry.register(ABSORPTION_I);
        registry.register(LUCK_I);
        registry.register(HEALTH_BOOST_I);
        registry.register(PATHING_AOE_I);
        registry.register(NAUSEA_RESISTANCE);

        registry.register(NIGHT_VISION);
        registry.register(WATER_BREATHING);
        registry.register(ABSORPTION_II);
        registry.register(JUMP_BOOST_I);
        registry.register(HEALTH_BOOST_II);
        registry.register(SPEED_I);
        registry.register(MINING_AOE_I);
        registry.register(TILLING_AOE_I);
        registry.register(PATHING_AOE_II);
        registry.register(BLINDNESS_RESISTANCE);

        registry.register(ABSORPTION_III);
        registry.register(LUCK_II);
        registry.register(FIRE_RESISTANCE);
        registry.register(JUMP_BOOST_II);
        registry.register(STEP_ASSIST);
        registry.register(HEALTH_BOOST_III);
        registry.register(STRENGTH_I);
        registry.register(SPEED_II);
        registry.register(HASTE_I);
        registry.register(NO_FALL_DAMAGE);
        registry.register(SLOW_FALLING);
        registry.register(MINING_AOE_II);
        registry.register(ATTACK_AOE_I);
        registry.register(TILLING_AOE_II);
        registry.register(PATHING_AOE_III);
        registry.register(WEAKNESS_RESISTANCE);
        registry.register(SLOWNESS_RESISTANCE);

        registry.register(ABSORPTION_IV);
        registry.register(POISON_RESISTANCE);
        registry.register(JUMP_BOOST_III);
        registry.register(HEALTH_BOOST_IV);
        registry.register(STRENGTH_II);
        registry.register(SPEED_III);
        registry.register(HASTE_II);
        registry.register(MINING_AOE_III);
        registry.register(ATTACK_AOE_II);
        registry.register(TILLING_AOE_III);
        registry.register(PATHING_AOE_IV);
        registry.register(MINING_FATIGUE_RESISTANCE);
        registry.register(HUNGER_RESISTANCE);

        registry.register(ABSORPTION_V);
        registry.register(LUCK_III);
        registry.register(WITHER_RESISTANCE);
        registry.register(HEALTH_BOOST_V);
        registry.register(STRENGTH_III);
        registry.register(HASTE_III);
        registry.register(FLIGHT);
        registry.register(MINING_AOE_IV);
        registry.register(ATTACK_AOE_III);
        registry.register(TILLING_AOE_IV);
    }
}
