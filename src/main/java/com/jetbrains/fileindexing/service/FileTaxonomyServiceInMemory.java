package com.jetbrains.fileindexing.service;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class FileTaxonomyServiceInMemory implements FileTaxonomyService {

    private Map<String, Node> nodes = new HashMap<>();

    @Override
    public void addFile(File file) {
        Node parentNode = getOrCreateNode(file.getParentFile());
        parentNode.children().put(file.getName(), new Node(false, null, file.getName()));
    }

    @Override
    public void delete(File file) {
        Node parentNode = getOrCreateNode(file.getParentFile());
        parentNode.children().remove(file.getName());
    }

    @SneakyThrows
    @Override
    public void addFolder(File folder) {
        Files.walk(folder.toPath()).forEach(path -> {
            File file = path.toFile();
            if(file.isDirectory()) {
                getOrCreateNode(file);
            } else {
                addFile(file);
            }
        });
    }

    private Node getOrCreateNode(File file) {
        String[] folderNames = file.getAbsolutePath().split(Pattern.quote(File.separator));
        Node currentNode = null;
        for (int i = 0; i < folderNames.length; ++i) {
            String folderName = folderNames[i];
            if (StringUtils.isNotBlank(folderName)) {
                Map<String, Node> children = currentNode == null ? nodes : currentNode.children();
                children.computeIfAbsent(folderName, s -> new Node(true, new HashMap<>(), s));
                currentNode = children.get(folderName);
            }
        }
        return currentNode;
    }

    @Override
    public void visitFiles(File folder, Consumer<File> childFile) {
        String[] folderNames = folder.getAbsolutePath().split(Pattern.quote(File.separator));
        Map<String, Node> children = nodes;
        Node node = null;
        for (String folderName : folderNames) {
            if(StringUtils.isBlank(folderName)) {
                continue;
            }
            node = children.get(folderName);
            if (node == null) {
                break;
            }
            children = node.children();
        }
        if (node != null) {
            visitNodes(node, childFile);
        }
    }

    private void visitNodes(Node node, Consumer<File> childFile) {
        node.children().forEach((key, child) -> {
            if (child.isDirectory()) {
                visitNodes(child, childFile);
            } else {
                childFile.accept(nodeToFile(child));
            }
        });
    }

    private File nodeToFile(Node node) {
        StringBuilder pathBuilder = new StringBuilder(File.separator);
        List<String> path = new ArrayList<>();
        while (node != null) {
            path.add(node.name());
        }
        Collections.reverse(path);
        String filePath = pathBuilder.append(StringUtils.join(path, File.separator)).toString();
        return new File(filePath);
    }

    private record Node(boolean isDirectory, Map<String, Node> children, String name) {

    }
}
