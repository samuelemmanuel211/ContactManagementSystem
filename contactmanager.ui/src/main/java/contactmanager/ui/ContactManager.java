package contactmanager.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    public void createContactListPanel() {
        contactListPanel = new JPanel(new BorderLayout());
        styleCardPanel(contactListPanel);

        JLabel header = new JLabel("CONTACT LIST VIEW", SwingConstants.CENTER);
        styleHeaderLabel(header);
        contactListPanel.add(header, BorderLayout.NORTH);

        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(new Color(230, 230, 230));

        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        contactListPanel.add(scrollPane, BorderLayout.CENTER);

        refreshContactListPanel(listContainer);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(new Color(240, 240, 240));

        JButton addNewContactBtn = new JButton("Add New Contact");
        styleButton(addNewContactBtn);
        addNewContactBtn.addActionListener(e -> {
            clearFormFields();
            selectedContactIndex = -1; // Indicate new contact
            cardLayout.show(mainPanel, "CONTACT_FORM");
        });

        JButton viewDetailBtn = new JButton("View Detail");
        styleButton(viewDetailBtn);
        viewDetailBtn.addActionListener(e -> {
            if (selectedContactIndex >= 0 && selectedContactIndex < contacts.size()) {
                showContactDetails(selectedContactIndex);
                cardLayout.show(mainPanel, "CONTACT_DETAILS");
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a contact by clicking on its name first.",
                        "No Contact Selected",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        bottomPanel.add(addNewContactBtn);
        bottomPanel.add(viewDetailBtn);
        contactListPanel.add(bottomPanel, BorderLayout.SOUTH);

    }

    private void refreshContactListPanel(JPanel listContainer) {
        listContainer.removeAll();

        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);

            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            rowPanel.setBackground(new Color(230, 230, 230));
            rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            // Store the row's index for later reference.
            rowPanel.putClientProperty("index", i);

            // Create a unified mouse adapter for hover and click.
            MouseAdapter adapter = new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    // Only change to hover color if this row is not selected.
                    if (!isRowSelected(rowPanel)) {
                        rowPanel.setBackground(new Color(210, 210, 210));
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!isRowSelected(rowPanel)) {
                        rowPanel.setBackground(new Color(230, 230, 230));
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    // Set this row as selected and update all row colors.
                    Integer idx = (Integer) rowPanel.getClientProperty("index");
                    selectedContactIndex = (idx != null ? idx : -1);
                    updateRowPanelColors(listContainer);
                }
            };
            addHoverListenerRecursively(rowPanel, adapter);

            // Contact name label
            JLabel nameLabel = new JLabel(contact.getName());
            nameLabel.setPreferredSize(new Dimension(100, 20));
            nameLabel.setForeground(Color.BLACK);
            nameLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            // Clicking the label also selects the row.
            nameLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectedContactIndex = contacts.indexOf(contact);
                    updateRowPanelColors(listContainer);
                }
            });

            // Edit button
            JButton editButton = new JButton("Edit");
            styleButton(editButton);
            int finalI1 = i;
            editButton.addActionListener(e -> {
                selectedContactIndex = finalI1;
                formNameField.setText(contact.getName());
                formPhoneField.setText(contact.getPhone());
                formEmailField.setText(contact.getEmail());
                cardLayout.show(mainPanel, "CONTACT_FORM");
                updateRowPanelColors(listContainer);
            });

        }


    }
}
