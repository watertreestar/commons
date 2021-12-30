package com.young.commons.file;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Locale;
import java.util.Objects;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;

public class Files {
    private static final String[] SI_UNITS = {"B", "kB", "MB", "GB", "TB", "PB", "EB"};
    private static final String[] BINARY_UNITS = {"B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB"};

    private static final int BUFFER_SIZE = 16 * 1024;

    /**
     * Read input stream as bytes
     *
     * @param inputStream
     * @return byte array from input stream.
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[16 * 1024];

        int bytesRead = inputStream.read(buffer);
        while (bytesRead != -1) {
            outputStream.write(buffer, 0, bytesRead);
            bytesRead = inputStream.read(buffer);
        }

        return outputStream.toByteArray();
    }

    /**
     * Read a file as string
     *
     * @param filePath
     * @return string from a file path.
     * @throws IOException
     */
    public static String readFileAsString(String filePath) throws IOException {
        byte[] buffer = new byte[(int) getFile(filePath).length()];
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(getFile(filePath)));
            bis.read(buffer);
        } finally {
            if (bis != null) {
                bis.close();
            }
        }
        return new String(buffer);
    }

    /**
     * Write string to a file
     *
     * @param content
     * @param filePath
     * @throws Exception
     */
    public static void writeStringToFile(String content, String filePath)
        throws Exception {
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(getFile(filePath)));
            bos.write(content.getBytes());
            bos.flush();
        } finally {
            if (bos != null) {
                bos.close();
            }
        }
    }

    /**
     * Get file from a file path.
     *
     * @param filePath
     * @return the file object of specified file path
     * @throws IOException
     */
    public static File getFile(String filePath) throws IOException {
        return new File(filePath);
    }

    /**
     * Copy a file
     *
     * @param src
     * @param dst
     * @throws IOException
     */
    public static void copy(File src, File dst) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE);
            byte[] buffer = new byte[BUFFER_SIZE];
            while (in.read(buffer) > 0) {
                out.write(buffer);
            }
        } finally {
            if (null != in) {
                in.close();
            }
            if (null != out) {
                out.close();
            }
        }
    }

    /**
     * Get a file suffix name
     *
     * @param file
     * @return file suffix name.
     */
    public static String suffix(File file) {
        String name = file.getName();
        int pos = name.lastIndexOf(".");
        return name.substring(pos);
    }

    /**
     * Get a file suffix name
     *
     * @param fileName
     * @return file suffix name
     */
    public static String suffix(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos);
    }

    /**
     * Delete a file according to a file path
     *
     * @param filePath
     * @return true if and only if the file or directory is successfully deleted; false otherwise
     * @throws IOException
     */
    public static boolean delete(String filePath) throws IOException {
        File file = getFile(filePath);
        return file.delete();
    }


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
