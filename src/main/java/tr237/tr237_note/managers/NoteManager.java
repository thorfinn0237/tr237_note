package tr237.tr237_note.managers;

import tr237.tr237_note.Tr237_note;
import tr237.tr237_note.storages.NoteStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NoteManager {

    private final Tr237_note plugin;
    private final NoteStorage storage;
    private final Map<UUID, String> playerNotes = new HashMap<>();

    public NoteManager(Tr237_note plugin, NoteStorage storage) {
        this.plugin = plugin;
        this.storage = storage;
        reloadData();
    }

    public void reloadData() {
        playerNotes.clear();
        playerNotes.putAll(storage.loadNotes());
    }

    public void setNote(UUID uuid, String text) {
        playerNotes.put(uuid, text);
        storage.saveNotes(playerNotes);
    }

    public String getNote(UUID uuid) {
        return playerNotes.get(uuid);
    }

    public boolean hasNote(UUID uuid) {
        return playerNotes.containsKey(uuid);
    }

    public boolean clearNote(UUID uuid) {
        if (playerNotes.remove(uuid) != null) {
            storage.saveNotes(playerNotes);
            return true;
        }
        return false;
    }

    public int getMaxLength() {
        return Math.max(10, plugin.getConfig().getInt("settings.max-note-length", 120));
    }

    public void shutdown() {
        storage.saveNotes(playerNotes);
    }
}