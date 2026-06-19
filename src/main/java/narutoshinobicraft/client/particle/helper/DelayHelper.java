package narutoshinobicraft.client.particle.helper;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@EventBusSubscriber(modid = "narutoshinobicraft", value = Dist.CLIENT)
public class DelayHelper {
    private static final Queue<DelayedTask> PENDING_TASKS = new ConcurrentLinkedQueue<>();
    private static final List<DelayedTask> ACTIVE_TASKS = new ArrayList<>();

    public static void runAfter(int ticks, Runnable action) {
        PENDING_TASKS.add(new DelayedTask(ticks, action));
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (!PENDING_TASKS.isEmpty()) {
            ACTIVE_TASKS.add(PENDING_TASKS.poll());
        }

        ACTIVE_TASKS.removeIf(task -> {
            task.ticksRemaining--;
            if (task.ticksRemaining <= 0) {
                task.action.run();
                return true;
            }
            return false;
        });
    }

    private static class DelayedTask {
        int ticksRemaining;
        Runnable action;
        DelayedTask(int ticks, Runnable action) { this.ticksRemaining = ticks; this.action = action; }
    }
}
