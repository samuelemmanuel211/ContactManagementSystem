package contactmanager.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ContactManager extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JPanel contactListPanel;
    private JPanel contactDetailPanel;
    private JPanel contactFormPanel;

    private ArrayList<Contact> contacts;

    private int selectedContactIndex = -1;

    private JTextField formNameField;
    private JTextField formPhoneField;
    private JTextField formEmailField;

    private JLabel detailNameLabel;
    private JLabel detailPhoneLabel;
    private JLabel detailEmailLabel;


    public ContactManager() {

        super("Contact Management System");

        contacts = new ArrayList<>();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(240, 240, 240));

        mainPanel.add(contactListPanel, "CONTACT_LIST");
        mainPanel.add(contactDetailPanel, "CONTACT_DETAILS");
        mainPanel.add(contactFormPanel, "CONTACT_FORM");

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 450);
        setLocationRelativeTo(null);
        setVisible(true);
    }


}
