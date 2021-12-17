package com.young.commons.file;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Locale;
import java.util.Objects;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;

public class Files {
    private static final String[] SI_UNITS = {"B", "kB", "MB", "GB", "TB", "PB", "EB"};
    private static final String[] BINARY_UNITS = {"B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB"};

    /**
     * @param bytes
     * @param useSIUnits
     * @param locale
     * @return
     */
    public static String humanReadableByteCount(final long bytes, final boolean useSIUnits, final Locale locale) {
        final String[] units = useSIUnits ? SI_UNITS : BINARY_UNITS;
        final int base = useSIUnits ? 1000 : 1024;

        // When using the smallest unit no decimal point is needed, because it's the exact number.
        if (bytes < base) {
            return bytes + " " + units[0];
        }

        final int exponent = (int) (Math.log(bytes) / Math.log(base));
        final String unit = units[exponent];
        return String.format(locale, "%.1f %s", bytes / Math.pow(base, exponent), unit);
    }

    public static String humanReadableByteCount(final long bytes, boolean useSIUnits) {
        return humanReadableByteCount(bytes, useSIUnits, Locale.getDefault());
    }

    public static void deleteFolder(final String path) throws IOException {
        final Path directory = Paths.get(path);

        deleteFolder(directory);
    }

    public static void ensureDirectoryExists(final Path directory) throws IOException {
        if (java.nio.file.Files.exists(directory)) {
            if (!java.nio.file.Files.isDirectory(directory)) {
                throw new NotDirectoryException(directory.toString());
            }
        } else {
            java.nio.file.Files.createDirectories(directory);
        }
    }

    /**
     * A variant of {@link #deleteFolder(Path)}, which ignores missing files. Inspired from {@link
     * java.nio.file.Files#deleteIfExists(Path)} which is implemented in much the same way. To be preferred over
     * {@link #deleteFolder(Path)} preceded by a {@link java.nio.file.Files#exists(Path, LinkOption...)}, as that is
     * more prone to race conditions.
     *
     * @param folder the directory to delete (if or any of its files exists)
     * @throws IOException on failure to scan the directory and/or delete the file
     */
    public static void deleteFolderIfExists(final Path folder) throws IOException {
        try {
            java.nio.file.Files.walkFileTree(folder, new FolderDeleter(java.nio.file.Files::deleteIfExists));
        } catch (final NoSuchFileException ignored) {
            // ignored
        }
    }

    public static void deleteFolder(final Path folder) throws IOException {
        java.nio.file.Files.walkFileTree(folder, new FolderDeleter(java.nio.file.Files::delete));
    }

    public static void copySnapshot(final Path runtimeDirectory, final Path snapshotDirectory)
        throws Exception {
        java.nio.file.Files.walkFileTree(snapshotDirectory, new SnapshotCopier(snapshotDirectory, runtimeDirectory));
    }

    @FunctionalInterface
    private interface FileDeleter {
        void delete(Path path) throws IOException;
    }

    public static final class SnapshotCopier extends SimpleFileVisitor<Path> {

        private final Path targetPath;
        private final Path sourcePath;

        SnapshotCopier(final Path sourcePath, final Path targetPath) {
            this.sourcePath = sourcePath;
            this.targetPath = targetPath;
        }

        @Override
        public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
            throws IOException {
            final Path newDirectory = targetPath.resolve(sourcePath.relativize(dir));
            try {
                java.nio.file.Files.copy(dir, newDirectory);
            } catch (final FileAlreadyExistsException ioException) {
                return SKIP_SUBTREE; // skip processing
            }

            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) {
            final Path newFile = targetPath.resolve(sourcePath.relativize(file));

            try {
                java.nio.file.Files.copy(file, newFile);
            } catch (final IOException ioException) {
            }

            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(final Path file, final IOException exc) {
            return CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) {
            return CONTINUE;
        }
    }

    private static final class FolderDeleter extends SimpleFileVisitor<Path> {
        private final FileDeleter deleter;

        private FolderDeleter(final FileDeleter deleter) {
            this.deleter = Objects.requireNonNull(deleter);
        }

        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
            throws IOException {
            deleter.delete(file);
            return CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(final Path dir, final IOException exc)
            throws IOException {
            deleter.delete(dir);
            return CONTINUE;
        }
    }
}
