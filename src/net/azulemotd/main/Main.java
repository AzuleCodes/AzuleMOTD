package net.azulemotd.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;

import net.azulemotd.cmd.Cmd_Motd;

public class Main extends JavaPlugin implements Listener {
	public static Main instance;

	public void onEnable() {
		instance = this;

		PluginManager manager = getServer().getPluginManager();
		getCommand("motd").setExecutor(new Cmd_Motd()); //registra o comando "motd"

		manager.registerEvents(this, this); //registra os eventos relacionados ao motd.
		saveDefaultConfig();
		//Dependencia não encontrada
		if (getServer().getPluginManager().getPlugin("ProtocolLib") == null) {		 
			getServer().getConsoleSender().sendMessage("Erro! ProtocolLib não encontrado.");
		}
		getServer().getConsoleSender().sendMessage("AzuleMOTD iniciado com êxito.");
		//Fake Version
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Status.Server.OUT_SERVER_INFO) {
			@Override
			public void onPacketSending(PacketEvent event) {
				if (getConfig().getString("FAKEVERSION.Enabled").equalsIgnoreCase("true")) {
					if (Bukkit.hasWhitelist()) {
						WrappedServerPing ping = event.getPacket().getServerPings().read(0);
						ping.setVersionProtocol(999);
						ping.setVersionName(getConfig().getString("FAKEVERSION.Fake02").replace("&", "§"));
					}
					if (getConfig().getString("FAKEVERSION.WhitelistOnly").equalsIgnoreCase("false")) {
						WrappedServerPing ping = event.getPacket().getServerPings().read(0);
						ping.setVersionProtocol(999);
						ping.setVersionName(getConfig().getString("FAKEVERSION.Fake01").replace("&", "§"));
					}
				}
				else {
					return;
				}
			}
		});

		//Hover Text
		final List<WrappedGameProfile> names = new ArrayList<WrappedGameProfile>();
		for (String str : getConfig().getStringList("HOVERTEXT"))names.add(new WrappedGameProfile("1", ChatColor.translateAlternateColorCodes('&', str))); 
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL,
				Arrays.asList(PacketType.Status.Server.OUT_SERVER_INFO), ListenerOptions.ASYNC) {
			@Override
			public void onPacketSending(PacketEvent event) {
				if (getConfig().getString("HOVERENABLED").equalsIgnoreCase("true")) {
					event.getPacket().getServerPings().read(0).setPlayers(names);
				}
			}
		});
	}

	//Motd
	@EventHandler
	public void motd(ServerListPingEvent e) {
		e.setMotd(Main.getInstance().getConfig().getString("MOTD.Motd01").replace("&", "§").replace("{nl}", "\n"));
		if (Bukkit.hasWhitelist())
			e.setMotd(Main.getInstance().getConfig().getString("MOTD.Motd02").replace("&", "§").replace("{nl}", "\n"));
	}

	public void onDisable() {
		getServer().getConsoleSender().sendMessage("AzuleMOTD! Configurações salvas.");
		saveDefaultConfig();
	}

	public static Main getInstance() {
		return instance;
	}
}
