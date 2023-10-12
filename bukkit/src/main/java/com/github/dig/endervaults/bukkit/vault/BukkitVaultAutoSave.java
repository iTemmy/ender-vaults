package com.github.dig.endervaults.bukkit.vault;

import com.github.dig.endervaults.api.VaultPluginProvider;
import com.github.dig.endervaults.api.storage.DataStorage;
import com.github.dig.endervaults.api.vault.Vault;
import com.github.dig.endervaults.api.vault.VaultRegistry;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

@Log
public class BukkitVaultAutoSave implements Runnable {

    private final VaultRegistry registry = VaultPluginProvider.getPlugin().getRegistry();
    private final DataStorage dataStorage = VaultPluginProvider.getPlugin().getDataStorage();

    @Override
    public void run() {
        log.log(Level.INFO, "[EnderVaults] Starting auto save of all registered vaults.");

        Set<UUID> owners = new HashSet<>(registry.getAllOwners());

        owners.forEach(ownerUUID -> {
            Map<UUID, Vault> vaults = registry.get(ownerUUID);
            if (vaults.isEmpty()) {
                return;
            }
            vaults.values().forEach(vault -> {
                try {
                    dataStorage.save(vault);
                } catch (IOException e) {
                    log.log(Level.SEVERE,
                            "[EnderVaults] Unable to save vault " + vault.getId() + " for player " + ownerUUID + ".", e);
                }
            });
        });

        log.log(Level.INFO, "[EnderVaults] Successfully saved all registered vaults.");
    }
}
