package narutoshinobicraft.common.registry;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import narutoshinobicraft.NarutoShinobiCraft;
import narutoshinobicraft.common.data.attachments.DeathTracker;
import narutoshinobicraft.common.data.attachments.PlayerProcess;
import narutoshinobicraft.common.data.attachments.PlayerVariants;
import narutoshinobicraft.common.data.attachments.PlayersChakra;

@SuppressWarnings("null")
public class AttachmentRegistry {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, NarutoShinobiCraft.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayersChakra>> PLAYER_CHAKRA = ATTACHMENT_TYPES.register("chakra", () -> AttachmentType.builder(() -> new PlayersChakra(20, 20)).serialize(PlayersChakra.CODEC).copyOnDeath().build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerProcess>> PLAYER_PROCESS = ATTACHMENT_TYPES.register("playerprocess", () -> AttachmentType.builder(() -> new PlayerProcess(40d)).serialize(PlayerProcess.CODEC).copyOnDeath().build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<DeathTracker>> PLAYER_DEATH = ATTACHMENT_TYPES.register("playerdeath", () -> AttachmentType.builder(() -> new DeathTracker()).build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerVariants>> PLAYER_VARIANTS = ATTACHMENT_TYPES.register("playervariants", () -> AttachmentType.builder(() -> new PlayerVariants(false)).serialize(PlayerVariants.CODEC).copyOnDeath().build());
}

