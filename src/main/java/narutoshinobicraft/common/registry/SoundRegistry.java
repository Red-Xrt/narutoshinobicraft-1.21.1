package narutoshinobicraft.common.registry;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import narutoshinobicraft.NarutoShinobiCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@SuppressWarnings("null")
public final class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, NarutoShinobiCraft.MODID);
    private static final Map<String, DeferredHolder<SoundEvent, SoundEvent>> BY_ID = new HashMap<>();

    public static final DeferredHolder<SoundEvent, SoundEvent> AMATERASU = register("Amaterasu");
    public static final DeferredHolder<SoundEvent, SoundEvent> SHINRATENSEI = register("ShinraTensei");
    public static final DeferredHolder<SoundEvent, SoundEvent> CHIBAKUTENSEI = register("ChibakuTensei");
    public static final DeferredHolder<SoundEvent, SoundEvent> BYAKUGAN = register("byakugan");
    public static final DeferredHolder<SoundEvent, SoundEvent> FURRYROAR = register("FurryRoar");
    public static final DeferredHolder<SoundEvent, SoundEvent> KAMUISFX = register("KamuiSFX");
    public static final DeferredHolder<SoundEvent, SoundEvent> MONSTERGROWL = register("MonsterGrowl");
    public static final DeferredHolder<SoundEvent, SoundEvent> KOH_SPAWN = register("KoH_spawn");
    public static final DeferredHolder<SoundEvent, SoundEvent> HAKKEROKUJUUYONSHOU = register("HakkeRokujuuyonShou");
    public static final DeferredHolder<SoundEvent, SoundEvent> HAKKESHOKAITEN = register("HakkeshoKaiten");
    public static final DeferredHolder<SoundEvent, SoundEvent> HAKKEKUSHO = register("HakkeKusho");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAJINAGURI = register("MajiNaguri");
    public static final DeferredHolder<SoundEvent, SoundEvent> HAND_SHOOT = register("hand_shoot");
    public static final DeferredHolder<SoundEvent, SoundEvent> NAGIHARAI = register("nagiharai");
    public static final DeferredHolder<SoundEvent, SoundEvent> SOUND_80GODSPUNCH = register("80GodsPunch");
    public static final DeferredHolder<SoundEvent, SoundEvent> KAGUYA_FINALTSB = register("Kaguya_FinalTSB");
    public static final DeferredHolder<SoundEvent, SoundEvent> DOJUTSU_ACTIVATE = register("dojutsu_activate");
    public static final DeferredHolder<SoundEvent, SoundEvent> BANSHOTENIN = register("BanshoTenin");
    public static final DeferredHolder<SoundEvent, SoundEvent> SEKIZO = register("sekizo");
    public static final DeferredHolder<SoundEvent, SoundEvent> YAGAI = register("yagai");
    public static final DeferredHolder<SoundEvent, SoundEvent> HIRUDORA = register("hirudora");
    public static final DeferredHolder<SoundEvent, SoundEvent> HOWL_YOUTH = register("howl_youth");
    public static final DeferredHolder<SoundEvent, SoundEvent> CROW_CALL = register("crow_call");
    public static final DeferredHolder<SoundEvent, SoundEvent> GENKAIHAKURINOJUTSU = register("genkaihakurinojutsu");
    public static final DeferredHolder<SoundEvent, SoundEvent> SUITON_SUIRYUUDAN = register("suiton_suiryuudan");
    public static final DeferredHolder<SoundEvent, SoundEvent> KIRIGAKURENOJUTSU = register("kirigakurenojutsu");
    public static final DeferredHolder<SoundEvent, SoundEvent> RASENSHURIKEN = register("rasenshuriken");
    public static final DeferredHolder<SoundEvent, SoundEvent> WIND = register("wind");
    public static final DeferredHolder<SoundEvent, SoundEvent> RASENSHURIKEN_EXPLODE = register("rasenshuriken_explode");
    public static final DeferredHolder<SoundEvent, SoundEvent> POOF = register("poof");
    public static final DeferredHolder<SoundEvent, SoundEvent> RASENGAN_START = register("rasengan_start");
    public static final DeferredHolder<SoundEvent, SoundEvent> SHARINGANSFX = register("sharingansfx");
    public static final DeferredHolder<SoundEvent, SoundEvent> GENJUTSU = register("genjutsu");
    public static final DeferredHolder<SoundEvent, SoundEvent> LASER_LONG = register("laser_long");
    public static final DeferredHolder<SoundEvent, SoundEvent> KAGEBUNSHIN = register("kagebunshin");
    public static final DeferredHolder<SoundEvent, SoundEvent> SUIRONOJUTSU = register("suironojutsu");
    public static final DeferredHolder<SoundEvent, SoundEvent> BULLET = register("bullet");
    public static final DeferredHolder<SoundEvent, SoundEvent> BULLET_IMPACT = register("bullet_impact");
    public static final DeferredHolder<SoundEvent, SoundEvent> ROCKS = register("rocks");
    public static final DeferredHolder<SoundEvent, SoundEvent> RINBO_HENGOKU = register("rinbo_hengoku");
    public static final DeferredHolder<SoundEvent, SoundEvent> GROUND_CHARGE = register("ground_charge");
    public static final DeferredHolder<SoundEvent, SoundEvent> JUTSU = register("jutsu");
    public static final DeferredHolder<SoundEvent, SoundEvent> HIDING_IN_ASH = register("hiding_in_ash");
    public static final DeferredHolder<SoundEvent, SoundEvent> BIJUDAMA = register("bijudama");
    public static final DeferredHolder<SoundEvent, SoundEvent> TENGAISHINSEI = register("tengaishinsei");
    public static final DeferredHolder<SoundEvent, SoundEvent> SANDO_NO_JUTSU = register("sando_no_jutsu");
    public static final DeferredHolder<SoundEvent, SoundEvent> YOMINUMA = register("yominuma");
    public static final DeferredHolder<SoundEvent, SoundEvent> SUIKODANNOJUTSU = register("suikodannojutsu");
    public static final DeferredHolder<SoundEvent, SoundEvent> DAIBAKUSUISHOHA = register("daibakusuishoha");
    public static final DeferredHolder<SoundEvent, SoundEvent> SABAKUSOSO = register("sabakusoso");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPIKED = register("spiked");
    public static final DeferredHolder<SoundEvent, SoundEvent> ICE_SHOOT = register("ice_shoot");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAKYOHYOSHO = register("makyohyosho");
    public static final DeferredHolder<SoundEvent, SoundEvent> ICE_FORMATION = register("ice_formation");
    public static final DeferredHolder<SoundEvent, SoundEvent> ICE_SHOOT_SMALL = register("ice_shoot_small");
    public static final DeferredHolder<SoundEvent, SoundEvent> C3 = register("c3");
    public static final DeferredHolder<SoundEvent, SoundEvent> KATSU = register("katsu");
    public static final DeferredHolder<SoundEvent, SoundEvent> HAND_PRESS = register("hand_press");
    public static final DeferredHolder<SoundEvent, SoundEvent> CHAINS = register("chains");
    public static final DeferredHolder<SoundEvent, SoundEvent> KYUUBI_HOWL = register("kyuubi_howl");
    public static final DeferredHolder<SoundEvent, SoundEvent> KYUUBI_DEATH = register("kyuubi_death");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOVEMENT = register("movement");
    public static final DeferredHolder<SoundEvent, SoundEvent> LASERCIRCUS = register("lasercircus");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOKUJIN_NO_JUTSU = register("mokujin_no_jutsu");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOOD_CLICK = register("wood_click");
    public static final DeferredHolder<SoundEvent, SoundEvent> KAIRIKIMUSO = register("kairikimuso");
    public static final DeferredHolder<SoundEvent, SoundEvent> MINDTRANSFER = register("mindtransfer");
    public static final DeferredHolder<SoundEvent, SoundEvent> SHADOW_SFX = register("shadow_sfx");
    public static final DeferredHolder<SoundEvent, SoundEvent> CHARGING_CHAKRA = register("charging_chakra");
    public static final DeferredHolder<SoundEvent, SoundEvent> CHARGE_GROUND = register("charge_ground");
    public static final DeferredHolder<SoundEvent, SoundEvent> EXPLOSION = register("explosion");
    public static final DeferredHolder<SoundEvent, SoundEvent> BONECRACK = register("bonecrack");
    public static final DeferredHolder<SoundEvent, SoundEvent> KATON_GOKAMEKEKU = register("katon_gokamekeku");
    public static final DeferredHolder<SoundEvent, SoundEvent> FLAMETHROW = register("flamethrow");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOODSPAWN = register("woodspawn");
    public static final DeferredHolder<SoundEvent, SoundEvent> ELECTRICITY = register("electricity");
    public static final DeferredHolder<SoundEvent, SoundEvent> CHIDORI = register("chidori");
    public static final DeferredHolder<SoundEvent, SoundEvent> BUGS = register("bugs");
    public static final DeferredHolder<SoundEvent, SoundEvent> WOODGROW = register("woodgrow");
    public static final DeferredHolder<SoundEvent, SoundEvent> AMATERASU2 = register("amaterasu2");
    public static final DeferredHolder<SoundEvent, SoundEvent> THROWPUNCH = register("throwpunch");
    public static final DeferredHolder<SoundEvent, SoundEvent> TENSEIBLASTCHARGE = register("tenseiblastcharge");
    public static final DeferredHolder<SoundEvent, SoundEvent> LASER = register("laser");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAGATAMA_SPIN = register("magatama_spin");
    public static final DeferredHolder<SoundEvent, SoundEvent> LASER_SHORT = register("laser_short");
    public static final DeferredHolder<SoundEvent, SoundEvent> INTONRAIHA = register("intonraiha");
    public static final DeferredHolder<SoundEvent, SoundEvent> RASENGAN_DURING = register("rasengan_during");
    public static final DeferredHolder<SoundEvent, SoundEvent> WINDECHO = register("windecho");
    public static final DeferredHolder<SoundEvent, SoundEvent> OPENGATE = register("opengate");
    public static final DeferredHolder<SoundEvent, SoundEvent> EIGHTGATESRELEASE = register("eightgatesrelease");
    public static final DeferredHolder<SoundEvent, SoundEvent> WATERBLAST = register("waterblast");
    public static final DeferredHolder<SoundEvent, SoundEvent> KUCHIYOSENOJUTSU = register("kuchiyosenojutsu");
    public static final DeferredHolder<SoundEvent, SoundEvent> DRAGON_ROAR = register("dragon_roar");
    public static final DeferredHolder<SoundEvent, SoundEvent> LIGHTNING_SHOOT = register("lightning_shoot");
    public static final DeferredHolder<SoundEvent, SoundEvent> KIRIN_DIALOG = register("kirin_dialog");
    public static final DeferredHolder<SoundEvent, SoundEvent> SHINSUSENJU = register("shinsusenju");
    public static final DeferredHolder<SoundEvent, SoundEvent> SNAKE_HISS = register("snake_hiss");
    public static final DeferredHolder<SoundEvent, SoundEvent> DINGDING = register("dingding");

    private SoundRegistry() {}

    private static DeferredHolder<SoundEvent, SoundEvent> register(String id) {
        String path = normalizeId(id);
        DeferredHolder<SoundEvent, SoundEvent> holder = SOUND_EVENTS.register(
            path,
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(NarutoShinobiCraft.MODID, path))
        );
        BY_ID.put(path, holder);
        return holder;
    }

    private static String normalizeId(String id) {
        return id.toLowerCase(Locale.ROOT);
    }

    public static DeferredHolder<SoundEvent, SoundEvent> get(String id) {
        return BY_ID.get(normalizeId(id));
    }
}

