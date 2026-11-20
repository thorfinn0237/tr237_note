package tr237.tr237_note.storages;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tr237.tr237_note.Tr237_note;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NoteStorage {

    private final Tr237_note plugin;
    private File file;
    private FileConfiguration config;

    public NoteStorage(Tr237_note plugin) {
        this.plugin = plugin;
        setupFile();
    }

    private void setupFile() {
        if (!plugin.getDataFolder().exists() && !plugin.getDataFolder().mkdirs()) {
            plugin.getLogger().warning("Could not create plugin data folder.");
        }

        file = new File(plugin.getDataFolder(), "notes.yml");
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    plugin.getLogger().warning("Could not create notes.yml.");
                }
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create notes.yml: " + e.getMessage());
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public Map<UUID, String> loadNotes() {
        Map<UUID, String> notes = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("notes");
        if (section == null) {
            return notes;
        }

        for (String key : section.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                String text = section.getString(key);
                if (text != null && !text.isBlank()) {
                    notes.put(uuid, text);
                }
            } catch (IllegalArgumentException ignored) {
                plugin.getLogger().warning("Skipped invalid UUID in notes.yml: " + key);
            }
        }
        return notes;
    }

    public void saveNotes(Map<UUID, String> notes) {
        config.set("notes", null);
        for (Map.Entry<UUID, String> entry : notes.entrySet()) {
            config.set("notes." + entry.getKey(), entry.getValue());
        }
        save();
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save notes.yml: " + e.getMessage());
        }
    }
}