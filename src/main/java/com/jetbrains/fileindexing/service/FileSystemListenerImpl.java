package com.jetbrains.fileindexing.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Slf4j
public class FileSystemListenerImpl implements FileSystemListener {
    private final WatchService watchService;
    private final ExecutorService executorService;

    @SneakyThrows
    public FileSystemListenerImpl() {
        this.watchService = FileSystems.getDefault().newWatchService();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void listenFilesChanges(List<File> watchingFiles, Consumer<File> createdOrUpdate, Consumer<File> deleted) {
        for (File file : watchingFiles) {
            if (file.isDirectory()) {
                registerDirectory(file.toPath());
            } else {
                registerParentDirectoryForFile(file.toPath());
            }
        }

        CompletableFuture.runAsync(() -> {
            while (true) {
                try {
                    WatchKey key = watchService.take();
                    Path dir = (Path) key.watchable();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        @SuppressWarnings("unchecked")
                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        Path filename = ev.context();
                        Path filePath = dir.resolve(filename);
                        File file = filePath.toFile();
                        if (shouldHandleFileChange(file, watchingFiles)) {
                            if (kind == StandardWatchEventKinds.ENTRY_CREATE || kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                                if (file.isFile()) {
                                    createdOrUpdate.accept(file);
                                } else if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                                    registerDirectory(filePath);
                                    createdOrUpdate.accept(file);
                                }
                            } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                                deleted.accept(file);
                            }
                        }
                    }
                    key.reset();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, executorService);
    }

    private void registerDirectory(Path dirPath) {
        try {
            dirPath.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);
            log.info("Watching directory: " + dirPath);

            try (Stream<Path> paths = Files.walk(dirPath)) {
                paths.filter(Files::isDirectory)
                        .forEach(path -> {
                            try {
                                path.register(watchService,
                                        StandardWatchEventKinds.ENTRY_CREATE,
                                        StandardWatchEventKinds.ENTRY_MODIFY,
                                        StandardWatchEventKinds.ENTRY_DELETE);
                            } catch (IOException e) {
                                log.error("Failed to register directory: " + path, e);
                            }
                        });
            }
        } catch (IOException e) {
            log.error("Failed to register directory: '{}'", dirPath, e);
        }
    }

    private void registerParentDirectoryForFile(Path filePath) {
        try {
            Path parentDir = filePath.getParent();
            if (parentDir != null) {
                parentDir.register(watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_MODIFY,
                        StandardWatchEventKinds.ENTRY_DELETE);
                log.info("Watching file: " + filePath);
            }
        } catch (IOException e) {
            log.error("Failed to register file: '{}'", filePath, e);
        }
    }

    private boolean shouldHandleFileChange(File file, List<File> watchingFiles) {
        for (File watchingFile : watchingFiles) {
            if (watchingFile.isDirectory() && file.toPath().startsWith(watchingFile.toPath())) {
                return true;
            } else if (file.equals(watchingFile)) {
                return true;
            }
        }
        return false;
    }
}
