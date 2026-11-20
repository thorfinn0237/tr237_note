package tr237.tr237_note.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import tr237.tr237_note.Tr237_note;
import tr237.tr237_note.managers.NoteManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NoteCommand implements CommandExecutor, TabCompleter {

    private final Tr237_note plugin;
    private final NoteManager noteManager;

    public NoteCommand(Tr237_note plugin, NoteManager noteManager) {
        this.plugin = plugin;
        this.noteManager = noteManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "Usage: /note <set <text>|view|clear>");
            return true;
        }

        String sub = args[0].toLowerCase();
        switch (sub) {
            case "set" -> {
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Please write your note text: /note set <text>");
                    return true;
                }

                String text = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).trim();
                int maxLength = noteManager.getMaxLength();
                if (text.length() > maxLength) {
                    player.sendMessage(ChatColor.RED + "Note is too long! Maximum length is " + maxLength + " characters.");
                    return true;
                }

                noteManager.setNote(player.getUniqueId(), text);
                player.sendMessage(ChatColor.GREEN + "Your personal note has been saved.");
                return true;
            }
            case "view" -> {
                String note = noteManager.getNote(player.getUniqueId());
                if (note == null) {
                    player.sendMessage(ChatColor.YELLOW + "You do not have a saved note yet.");
                    return true;
                }
                player.sendMessage(ChatColor.GOLD + "Your Note: " + ChatColor.WHITE + note);
                return true;
            }
            case "clear" -> {
                if (noteManager.clearNote(player.getUniqueId())) {
                    player.sendMessage(ChatColor.GREEN + "Your personal note has been cleared.");
                } else {
                    player.sendMessage(ChatColor.YELLOW + "You have no note to clear.");
                }
                return true;
            }
            case "reload" -> {
                if (!player.hasPermission("tr237note.admin") && !player.isOp()) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to reload the plugin.");
                    return true;
                }
                plugin.reloadConfig();
                noteManager.reloadData();
                player.sendMessage(ChatColor.GREEN + "Configuration and notes reloaded.");
                return true;
            }
            default -> {
                player.sendMessage(ChatColor.RED + "Unknown subcommand. Usage: /note <set <text>|view|clear>");
                return true;
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("note")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            List<String> options = new ArrayList<>(Arrays.asList("set", "view", "clear"));
            if (sender.hasPermission("tr237note.admin") || sender.isOp()) {
                options.add("reload");
            }

            String input = args[0].toLowerCase();
            List<String> matches = new ArrayList<>();
            for (String option : options) {
                if (option.startsWith(input)) {
                    matches.add(option);
                }
            }
            return matches;
        }

        return Collections.emptyList();
    }
}