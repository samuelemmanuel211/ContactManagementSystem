module src {
    requires contactmanager.model;
    requires contactmanager.ui;
    requires contactmanager.app;
    requires org.junit.jupiter.api;  // JUnit 5 for testing
    requires java.desktop;
    requires junit;  // Required for Swing UI testing
    exports contactmanager.test; // Exporting the test package
}