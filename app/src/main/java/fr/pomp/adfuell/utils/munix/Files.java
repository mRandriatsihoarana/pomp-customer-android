package fr.pomp.adfuell.utils.munix;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by munix on 07/04/16.
 */
public class Files {

    /**
     * Elimina un directorio de manera recursiva
     *
     * @param dir
     * @return true si ha podido eliminarlo, false en caso contrario
     */
    public static boolean deleteDirectoryRecursive( File dir ) {
        if ( dir != null && dir.isDirectory() ) {
            String[] children = dir.list();
            for ( int i = 0; i < children.length; i++ ) {
                boolean success = deleteDirectoryRecursive( new File( dir, children[i] ) );
                if ( !success ) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * Lee un recurso raw como archivo de texto
     *
     * @param ctx
     * @param resId
     * @return String
     */
    public static String readRawAsTextFile( Context ctx, int resId ) {
        InputStream inputStream = ctx.getResources().openRawResource( resId );

        InputStreamReader inputreader = new InputStreamReader( inputStream );
        BufferedReader buffreader = new BufferedReader( inputreader );
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while ( ( line = buffreader.readLine() ) != null ) {
                text.append( line );
                text.append( '\n' );
            }
        } catch ( IOException e ) {
            return null;
        }
        return text.toString();
    }

    public static String readRawAsTextFile( int resId ) {
        return readRawAsTextFile( MunixUtilities.context, resId );
    }

    /**
     * Retorna un byte array de un fichero
     *
     * @param file
     * @return byte[]
     * @throws IOException
     */
    public static byte[] getBytesFromFile( File file ) throws IOException {
        InputStream is = new FileInputStream( file );
        long length = file.length();

        if ( length > Integer.MAX_VALUE ) {
            throw new IOException( "File is too large" );
        }

        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while ( offset < bytes.length && ( numRead = is.read( bytes, offset, bytes.length - offset ) ) >= 0 ) {
            offset += numRead;
        }

        if ( offset < bytes.length ) {
            throw new IOException( "Could not completely read file " + file.getName() );
        }
        is.close();
        return bytes;
    }

    /**
     * Lee un archivo desde su ruta y devuelve el contenido como String
     *
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    public static String readFileAsString( String path ) throws FileNotFoundException {
        File file = new File( path );
        StringBuilder fileContents = new StringBuilder( (int) file.length() );
        Scanner scanner = new Scanner( file );
        String lineSeparator = java.lang.System.getProperty( "line.separator" );

        try {
            while ( scanner.hasNextLine() ) {
                fileContents.append( scanner.nextLine() + lineSeparator );
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }

    /**
     * Devuelve el contenido de un File como String
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static String readFileAsString( File file ) throws FileNotFoundException {
        return readFileAsString( file.getAbsolutePath() );
    }

    /**
     * Copia de un archivo a otro
     *
     * @param srcFile
     * @param destFile
     * @param preserveFileDate
     * @throws IOException
     */
    public static void copyFile( File srcFile, File destFile, boolean preserveFileDate ) throws IOException {
        if ( srcFile == null ) {
            throw new NullPointerException( "Source must not be null" );
        } else if ( destFile == null ) {
            throw new NullPointerException( "Destination must not be null" );
        } else if ( !srcFile.exists() ) {
            throw new FileNotFoundException( "Source \'" + srcFile + "\' does not exist" );
        } else if ( srcFile.isDirectory() ) {
            throw new IOException( "Source \'" + srcFile + "\' exists but is a directory" );
        } else if ( srcFile.getCanonicalPath().equals( destFile.getCanonicalPath() ) ) {
            throw new IOException( "Source \'" + srcFile + "\' and destination \'" + destFile + "\' are the same" );
        } else {
            File parentFile = destFile.getParentFile();
            if ( parentFile != null && !parentFile.mkdirs() && !parentFile.isDirectory() ) {
                throw new IOException( "Destination \'" + parentFile + "\' directory cannot be created" );
            } else if ( destFile.exists() && !destFile.canWrite() ) {
                throw new IOException( "Destination \'" + destFile + "\' exists but is read-only" );
            } else {
                if ( destFile.exists() && destFile.isDirectory() ) {
                    throw new IOException( "Destination \'" + destFile + "\' exists but is a directory" );
                } else {
                    FileInputStream fis = null;
                    FileOutputStream fos = null;
                    FileChannel input = null;
                    FileChannel output = null;

                    try {
                        fis = new FileInputStream( srcFile );
                        fos = new FileOutputStream( destFile );
                        input = fis.getChannel();
                        output = fos.getChannel();
                        long size = input.size();
                        long pos = 0L;

                        for ( long count = 0L; pos < size; pos += output.transferFrom( input, pos, count ) ) {
                            count = size - pos > 31457280L ? 31457280L : size - pos;
                        }
                    } finally {
                        try {
                            if ( output != null ) {
                                output.close();
                            }

                            if ( fos != null ) {
                                fos.close();
                            }

                            if ( input != null ) {
                                input.close();
                            }

                            if ( fis != null ) {
                                fis.close();
                            }
                        } catch ( IOException var2 ) {
                        }
                    }

                    if ( srcFile.length() != destFile.length() ) {
                        throw new IOException( "Failed to copy full contents from \'" + srcFile + "\' to \'" + destFile + "\'" );
                    } else {
                        if ( preserveFileDate ) {
                            destFile.setLastModified( srcFile.lastModified() );
                        }
                    }
                }
            }
        }
    }


    /**
     * Mueve el contenido de un directorio a otro
     *
     * @param src
     * @param destDir
     * @param createDestDir
     * @throws IOException
     */
    public static void moveDirectoryToDirectory( File src, File destDir, boolean createDestDir ) throws IOException {
        if ( src == null ) {
            throw new NullPointerException( "Source must not be null" );
        } else if ( destDir == null ) {
            throw new NullPointerException( "Destination directory must not be null" );
        } else {
            if ( !destDir.exists() && createDestDir ) {
                destDir.mkdirs();
            }

            if ( !destDir.exists() ) {
                throw new FileNotFoundException( "Destination directory \'" + destDir + "\' does not exist [createDestDir=" + createDestDir + "]" );
            } else if ( !destDir.isDirectory() ) {
                throw new IOException( "Destination \'" + destDir + "\' is not a directory" );
            } else {
                File finalDestDir = new File( destDir, src.getName() );

                boolean rename = src.renameTo( finalDestDir );
                if ( !rename ) {
                    if ( finalDestDir.getCanonicalPath().startsWith( src.getCanonicalPath() ) ) {
                        throw new IOException( "Cannot move directory: " + src + " to a subdirectory of itself: " + destDir );
                    }

                    copyDirectory( src, finalDestDir );
                    deleteDirectory( src );
                    if ( src.exists() ) {
                        throw new IOException( "Failed to delete original directory \'" + src + "\' after copy to \'" + finalDestDir + "\'" );
                    }
                }
            }
        }
    }

    /**
     * Comprueba si un archivo es un enlace simbólico de *nix
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static boolean isSymlink( File file ) throws IOException {
        if ( file == null ) {
            throw new NullPointerException( "File must not be null" );
        } else {
            File fileInCanonicalDir = null;
            if ( file.getParent() == null ) {
                fileInCanonicalDir = file;
            } else {
                File canonicalDir = file.getParentFile().getCanonicalFile();
                fileInCanonicalDir = new File( canonicalDir, file.getName() );
            }

            return !fileInCanonicalDir.getCanonicalFile().equals( fileInCanonicalDir.getAbsoluteFile() );
        }
    }

    /**
     * Elimina un directorio
     *
     * @param directory
     * @throws IOException
     */
    public static void deleteDirectory( File directory ) throws IOException {
        if ( directory.exists() ) {
            if ( !isSymlink( directory ) ) {
                cleanDirectory( directory );
            }

            if ( !directory.delete() ) {
                String message = "Unable to delete directory " + directory + ".";
                throw new IOException( message );
            }
        }
    }

    /**
     * Vacía un directorio eliminando todos los archivos
     *
     * @param directory
     * @throws IOException
     */
    public static void cleanDirectory( File directory ) throws IOException {
        String var9;
        if ( !directory.exists() ) {
            var9 = directory + " does not exist";
            throw new IllegalArgumentException( var9 );
        } else if ( !directory.isDirectory() ) {
            var9 = directory + " is not a directory";
            throw new IllegalArgumentException( var9 );
        } else {
            File[] files = directory.listFiles();
            if ( files == null ) {
                throw new IOException( "Failed to list contents of " + directory );
            } else {
                IOException exception = null;
                File[] arr$ = files;
                int len$ = files.length;

                for ( int i$ = 0; i$ < len$; ++i$ ) {
                    File file = arr$[i$];

                    try {
                        forceDelete( file );
                    } catch ( IOException var8 ) {
                        exception = var8;
                    }
                }

                if ( null != exception ) {
                    throw exception;
                }
            }
        }
    }

    /**
     * Elimina un archivo
     *
     * @param file
     * @throws IOException
     */
    public static void forceDelete( File file ) throws IOException {
        if ( file.isDirectory() ) {
            deleteDirectory( file );
        } else {
            boolean filePresent = file.exists();
            if ( !file.delete() ) {
                if ( !filePresent ) {
                    throw new FileNotFoundException( "File does not exist: " + file );
                }

                String message = "Unable to delete file: " + file;
                throw new IOException( message );
            }
        }
    }

    /**
     * Copia un directorio
     *
     * @param srcDir
     * @param destDir
     * @throws IOException
     */
    public static void copyDirectory( File srcDir, File destDir ) throws IOException {
        copyDirectory( srcDir, destDir, true );
    }

    /**
     * Copia un directorio
     *
     * @param srcDir
     * @param destDir
     * @param preserveFileDate
     * @throws IOException
     */
    public static void copyDirectory( File srcDir, File destDir, boolean preserveFileDate ) throws IOException {
        copyDirectory( srcDir, destDir, (FileFilter) null, preserveFileDate );
    }

    /**
     * Copia un directorio
     *
     * @param srcDir
     * @param destDir
     * @param filter
     * @throws IOException
     */
    public static void copyDirectory( File srcDir, File destDir, FileFilter filter ) throws IOException {
        copyDirectory( srcDir, destDir, filter, true );
    }

    /**
     * Copia un directorio
     *
     * @param srcDir
     * @param destDir
     * @param filter
     * @param preserveFileDate
     * @throws IOException
     */
    public static void copyDirectory( File srcDir, File destDir, FileFilter filter, boolean preserveFileDate ) throws IOException {
        if ( srcDir == null ) {
            throw new NullPointerException( "Source must not be null" );
        } else if ( destDir == null ) {
            throw new NullPointerException( "Destination must not be null" );
        } else if ( !srcDir.exists() ) {
            throw new FileNotFoundException( "Source \'" + srcDir + "\' does not exist" );
        } else if ( !srcDir.isDirectory() ) {
            throw new IOException( "Source \'" + srcDir + "\' exists but is not a directory" );
        } else if ( srcDir.getCanonicalPath().equals( destDir.getCanonicalPath() ) ) {
            throw new IOException( "Source \'" + srcDir + "\' and destination \'" + destDir + "\' are the same" );
        } else {
            ArrayList exclusionList = null;
            if ( destDir.getCanonicalPath().startsWith( srcDir.getCanonicalPath() ) ) {
                File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles( filter );
                if ( srcFiles != null && srcFiles.length > 0 ) {
                    exclusionList = new ArrayList( srcFiles.length );
                    File[] arr$ = srcFiles;
                    int len$ = srcFiles.length;

                    for ( int i$ = 0; i$ < len$; ++i$ ) {
                        File srcFile = arr$[i$];
                        File copiedFile = new File( destDir, srcFile.getName() );
                        exclusionList.add( copiedFile.getCanonicalPath() );
                    }
                }
            }

            doCopyDirectory( srcDir, destDir, filter, preserveFileDate, exclusionList );
        }
    }

    /**
     * Mueve un archivo
     *
     * @param srcFile
     * @param destFile
     * @throws IOException
     */
    public static void moveFile( File srcFile, File destFile ) throws IOException {
        if ( srcFile == null ) {
            throw new NullPointerException( "Source must not be null" );
        } else if ( destFile == null ) {
            throw new NullPointerException( "Destination must not be null" );
        } else if ( !srcFile.exists() ) {
            throw new FileNotFoundException( "Source \'" + srcFile + "\' does not exist" );
        } else if ( srcFile.isDirectory() ) {
            throw new IOException( "Source \'" + srcFile + "\' is a directory" );
        } else if ( destFile.exists() ) {
            throw new IOException( "Destination \'" + destFile + "\' already exists" );
        } else if ( destFile.isDirectory() ) {
            throw new IOException( "Destination \'" + destFile + "\' is a directory" );
        } else {
            boolean rename = srcFile.renameTo( destFile );
            if ( !rename ) {
                copyFile( srcFile, destFile, true );
                if ( !srcFile.delete() ) {
                    forceDelete( destFile );
                    throw new IOException( "Failed to delete original file \'" + srcFile + "\' after copy to \'" + destFile + "\'" );
                }
            }
        }
    }

    /**
     * Mueve un archivo a un directorio
     * @param srcFile
     * @param destDir
     * @param createDestDir
     * @throws IOException
     */
    public static void moveFileToDirectory(File srcFile, File destDir, boolean createDestDir) throws IOException {
        if(srcFile == null) {
            throw new NullPointerException("Source must not be null");
        } else if(destDir == null) {
            throw new NullPointerException("Destination directory must not be null");
        } else {
            if(!destDir.exists() && createDestDir) {
                destDir.mkdirs();
            }

            if(!destDir.exists()) {
                throw new FileNotFoundException("Destination directory \'" + destDir + "\' does not exist [createDestDir=" + createDestDir + "]");
            } else if(!destDir.isDirectory()) {
                throw new IOException("Destination \'" + destDir + "\' is not a directory");
            } else {
                moveFile(srcFile, new File(destDir, srcFile.getName()));
            }
        }
    }


    /////////////////////////////////7
    // Private methods
    private static void doCopyDirectory( File srcDir, File destDir, FileFilter filter, boolean preserveFileDate, List<String> exclusionList ) throws IOException {
        File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles( filter );
        if ( srcFiles == null ) {
            throw new IOException( "Failed to list contents of " + srcDir );
        } else {
            if ( destDir.exists() ) {
                if ( !destDir.isDirectory() ) {
                    throw new IOException( "Destination \'" + destDir + "\' exists but is not a directory" );
                }
            } else if ( !destDir.mkdirs() && !destDir.isDirectory() ) {
                throw new IOException( "Destination \'" + destDir + "\' directory cannot be created" );
            }

            if ( !destDir.canWrite() ) {
                throw new IOException( "Destination \'" + destDir + "\' cannot be written to" );
            } else {
                File[] arr$ = srcFiles;
                int len$ = srcFiles.length;

                for ( int i$ = 0; i$ < len$; ++i$ ) {
                    File srcFile = arr$[i$];
                    File dstFile = new File( destDir, srcFile.getName() );
                    if ( exclusionList == null || !exclusionList.contains( srcFile.getCanonicalPath() ) ) {
                        if ( srcFile.isDirectory() ) {
                            doCopyDirectory( srcFile, dstFile, filter, preserveFileDate, exclusionList );
                        } else {
                            copyFile( srcFile, dstFile, preserveFileDate );
                        }
                    }
                }

                if ( preserveFileDate ) {
                    destDir.setLastModified( srcDir.lastModified() );
                }
            }
        }
    }
}
