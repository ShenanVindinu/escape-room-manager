package com.escaperoom.cli;

import com.escaperoom.command.Command;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * JAVA REFLECTION SHOWCASE.
 *
 * Instead of hardcoding a switch/if-chain menu that lists every Command
 * class by name, this class scans the com.escaperoom.command package
 * at runtime, finds every concrete class implementing the Command
 * interface, and instantiates each one via its no-arg constructor
 * (Class.newInstance()/getDeclaredConstructor().newInstance()).
 *
 * The practical benefit: to add a new CLI action, a developer only has
 * to create a new class implementing Command in that package - nothing
 * here, or anywhere else, needs to change (Open/Closed in practice).
 */
public class CommandLineInterface {

    private static final String COMMAND_PACKAGE = "com.escaperoom.command";
    private final List<Command> commands = new ArrayList<>();

    public CommandLineInterface() {
        discoverCommands();
    }

    /** Scans the command package on the classpath and instantiates every Command found. */
    private void discoverCommands() {
        try {
            String path = COMMAND_PACKAGE.replace('.', '/');
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources(path);

            List<Class<?>> classes = new ArrayList<>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (resource.getProtocol().equals("file")) {
                    // IMPORTANT: use toURI(), not getFile(). getFile() returns the
                    // raw URL-encoded path (spaces become "%20"), which breaks
                    // File.exists() whenever the project sits under a folder with
                    // spaces in its name (e.g. "OneDrive", "Program Files", or any
                    // multi-word folder name). toURI() decodes it correctly.
                    classes.addAll(findClassesInDirectory(new File(resource.toURI()), COMMAND_PACKAGE));
                } else if (resource.getProtocol().equals("jar")) {
                    classes.addAll(findClassesInJar(resource, path));
                }
            }

            for (Class<?> clazz : classes) {
                // Skip the interface itself and anything abstract.
                if (Command.class.isAssignableFrom(clazz)
                        && !clazz.isInterface()
                        && !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                    Command instance = (Command) clazz.getDeclaredConstructor().newInstance();
                    commands.add(instance);
                }
            }

            // Stable, predictable ordering for the menu.
            commands.sort((a, b) -> a.getDescription().compareTo(b.getDescription()));

        } catch (Exception e) {
            System.out.println("Failed to discover commands via reflection: " + e.getMessage());
        }
    }

    private List<Class<?>> findClassesInDirectory(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) return classes;

        for (File file : directory.listFiles()) {
            if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().replace(".class", "");
                classes.add(Class.forName(className));
            }
        }
        return classes;
    }

    private List<Class<?>> findClassesInJar(URL resource, String path) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));

        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.startsWith(path) && name.endsWith(".class")) {
                    String className = name.replace('/', '.').replace(".class", "");
                    classes.add(Class.forName(className));
                }
            }
        }
        return classes;
    }

    /** Main input loop: print menu, read choice, execute chosen Command. */
    public void run() {
        System.out.println("=========================================");
        System.out.println(" Escape Room Booking & Session Manager");
        System.out.println("=========================================");
        System.out.println("(" + commands.size() + " commands auto-discovered via reflection)");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = ConsoleIO.prompt("\nChoose an option (0 to exit): ");

            if (choice.equals("0")) {
                running = false;
                System.out.println("Goodbye!");
                continue;
            }

            try {
                int index = Integer.parseInt(choice) - 1;
                if (index >= 0 && index < commands.size()) {
                    System.out.println();
                    commands.get(index).execute();
                } else {
                    System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    private void printMenu() {
        System.out.println();
        for (int i = 0; i < commands.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + commands.get(i).getDescription());
        }
        System.out.println("  0. Exit");
    }
}
