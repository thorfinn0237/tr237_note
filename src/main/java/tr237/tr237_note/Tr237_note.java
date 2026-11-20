package tr237.tr237_note;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import tr237.tr237_note.commands.NoteCommand;
import tr237.tr237_note.managers.NoteManager;
import tr237.tr237_note.storages.NoteStorage;

import java.util.Objects;

public final class Tr237_note extends JavaPlugin {

    private NoteManager noteManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        NoteStorage noteStorage = new NoteStorage(this);
        this.noteManager = new NoteManager(this, noteStorage);

        NoteCommand noteCommand = new NoteCommand(this, this.noteManager);
        PluginCommand command = Objects.requireNonNull(getCommand("note"), "Command 'note' missing from plugin.yml");
        command.setExecutor(noteCommand);
        command.setTabCompleter(noteCommand);

        getLogger().info("tr237_note v1.0 enabled successfully.");
    }

    @Override
    public void onDisable() {
        if (this.noteManager != null) {
            this.noteManager.shutdown();
        }
        getLogger().info("tr237_note disabled cleanly.");
    }
}