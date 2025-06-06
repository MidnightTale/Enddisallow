package fun.mntale.enddisallow;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Enddisallow extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPortalEyeInsert(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getItem() == null || event.getItem().getType() != Material.ENDER_EYE) return;

        Block block = event.getClickedBlock();
        if (block.getType() != Material.END_PORTAL_FRAME) return;

        BlockData data = block.getBlockData();
        if (!(data instanceof EndPortalFrame frame)) return;

        if (!frame.hasEye()) {
            event.setCancelled(true);

            Player player = event.getPlayer();

            getServer().getRegionScheduler().run(this, player.getLocation(), (ScheduledTask task) -> {
                player.sendActionBar(Component.text("You cannot activate the End Portal.", NamedTextColor.RED));
                player.playSound(
                        player.getLocation(),
                        Sound.BLOCK_NOTE_BLOCK_BASS,
                        SoundCategory.PLAYERS,
                        1.0f,
                        0.6f
                );
            });
        }
    }
}
