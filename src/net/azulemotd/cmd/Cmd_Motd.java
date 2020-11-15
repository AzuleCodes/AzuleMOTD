package net.azulemotd.cmd;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import net.azulemotd.main.ConfigAPI;
import net.azulemotd.main.Main;

public class Cmd_Motd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String arg2, String[] args) {
		if (cmd.getName().equalsIgnoreCase("motd")) {
			if (!s.hasPermission("azulemotd.admin")) {
				s.sendMessage("§c§lErro! §cVocê não possuí permisão para utilizar este comando.");
				return false;
			}
			if (args.length == 0) {
				s.sendMessage("");
				s.sendMessage(" §6§lMOTD - §fInformações sobre este comando:");
				s.sendMessage(" §7(/motd)");
				s.sendMessage("");
				s.sendMessage(" §f/motd update §6- §7Atualiza a configuração.");
				s.sendMessage("");
				return false;
			}
			if (args.length == 1 && args[0].equalsIgnoreCase("update")) {
				Main.getInstance().reloadConfig();
				s.sendMessage("§a§lYAY! §aConfiguração atualizada com êxito.");
			}
		}
		return false;
	}

}
