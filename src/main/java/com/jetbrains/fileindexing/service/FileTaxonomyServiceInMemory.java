package com.jetbrains.fileindexing.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class FileTaxonomyServiceInMemory implements FileTaxonomyService {

    private final Map<String, Node> nodes = new HashMap<>();

    @Override
    public void addFile(File file) {
        Node parentNode = getOrCreateNode(file.getParentFile());
        parentNode.getChildren().put(file.getName(), new Node(false, null, file.getName(), parentNode));
    }

    @Override
    public void delete(File file) {
        Node parentNode = getOrCreateNode(file.getParentFile());
        parentNode.getChildren().remove(file.getName());
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
        for (String folderName : folderNames) {
            if (StringUtils.isNotBlank(folderName)) {
                Map<String, Node> children = currentNode == null ? nodes : currentNode.getChildren();
                Node finalCurrentNode = currentNode;
                children.computeIfAbsent(folderName, s -> new Node(true, new HashMap<>(), s, finalCurrentNode));
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
            children = node.getChildren();
        }
        if (node != null && node.isDirectory()) {
            visitChildNodes(node, childFile);
        }
    }

    private void visitChildNodes(Node node, Consumer<File> childFile) {
        node.getChildren().forEach((key, child) -> {
            if (child.isDirectory()) {
                visitChildNodes(child, childFile);
            } else {
                childFile.accept(nodeToFile(child));
            }
        });
    }

    private File nodeToFile(Node node) {
        StringBuilder pathBuilder = new StringBuilder(File.separator);
        List<String> path = new ArrayList<>();
        while (node != null) {
            path.add(node.getName());
            node = node.getParent();
        }
        Collections.reverse(path);
        String filePath = pathBuilder.append(StringUtils.join(path, File.separator)).toString();
        return new File(filePath);
    }

    @Getter
    @RequiredArgsConstructor
    private static class Node {
        private final boolean isDirectory;
        private final Map<String, Node> children;
        private final String name;
        private final Node parent;
    }
}
