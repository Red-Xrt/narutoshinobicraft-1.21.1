package narutoshinobicraft.common.jutsu.helpers.gameplay;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import narutoshinobicraft.common.resources.ResourceLocateDameSource;

@SuppressWarnings("null")
public class JutsuDamageTypes {
    public static final ResourceKey<DamageType> NINJUTSU = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocateDameSource.Ninjutsu);
    public static final ResourceKey<DamageType> SENJUTSU = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocateDameSource.Senjutsu);

    public static DamageSource causeJutsuDamage(Level level, Entity directEntity, @Nullable Entity causingEntity){
        return level.damageSources().source(NINJUTSU, directEntity, causingEntity);
    }

    public static DamageSource causeSenjutsuDamage(Level level, Entity directEntity, @Nullable Entity causingEntity){
        return level.damageSources().source(SENJUTSU, directEntity, causingEntity);
    }

    public static boolean isDamageSourceNinjutsu(DamageSource source){
        return source.is(NINJUTSU);
    }

    public static boolean isDamageSourceSenjutsu(DamageSource source){
        return source.is(SENJUTSU);
    }

    public static boolean isDamageSourceJutsu(DamageSource source){
        return isDamageSourceNinjutsu(source) || isDamageSourceSenjutsu(source);
    }
}

