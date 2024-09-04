# File Indexing Library

## Overview

This Java library provides a service for indexing text files. It allows for specifying the files and directories to be indexed and querying files that contain a given word. The library supports concurrent access and responds to changes in the watched part of the filesystem. It is designed to be extensible for different search strategies.

## Features

- **File Indexing:** Indexes text files and directories for efficient searching.
- **Concurrent Access:** Supports multiple threads for reading and writing operations.
- **Event-Driven Updates:** Reacts to file system events such as creation, modification, and deletion to keep the index up-to-date.

## Installation

To use this library in your project, you can clone the repository and build the project using Maven.

```bash
git clone https://github.com/yourusername/file-indexing-library.git
cd file-indexing-library
mvn clean install
```

## Usage

Here is a small example of how to use the library to index files and search for terms:

```java
import com.jetbrains.fileindexing.config.Config;
import com.jetbrains.fileindexing.processor.FileSearch;
import com.jetbrains.fileindexing.search.TextContainsSearchStrategy;
import com.jetbrains.fileindexing.utils.Status;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

public class FileIndexingExample {

    public static void main(String[] args) {
        // Create a temporary directory to watch
        File watchingFolder = new File("path to firectory");
        FileSearch fileSearch = FileSearch.builder().config(
                        Config.builder()
                                .searchStrategy(new TextContainsSearchStrategy())
                                .watchingFolders(List.of(watchingFolder))
                                .build())
                .build();

        // Wait for the index to be built
        while (fileSearch.getStatus().equals(Status.INDEXING)) {
            // Waiting for the index to be built
        }

        // Search for the term "interface"
        List<File> results = fileSearch.search("interface");
        System.out.println("Files containing 'interface': " + results.size());
    }
}
```

### Event-Based Indexing

The library automatically updates the index in response to file system changes. To use this feature, initialize the `FileSystemListener`:

```java
import com.jetbrains.fileindexing.service.FileSystemListener;
import com.jetbrains.fileindexing.service.FileSystemListenerImpl;

public class FileIndexingWithEvents {
    public static void main(String[] args) {
        // Initialize file system listener
        FileSystemListener listener = new FileSystemListenerImpl();
        
        // Start listening to file changes
        listener.listenFilesChanges();
    }
}
```

## Testing

Unit tests are provided to ensure the correctness of the library. You can run the tests using Maven:

```bash
mvn test
```

## Contributing

Contributions are welcome! If you'd like to contribute, please fork the repository and submit a pull request.

## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.

## Acknowledgments

This project was developed as part of a larger system for efficient file indexing and searching, with a focus on scalability and real-time event handling.
