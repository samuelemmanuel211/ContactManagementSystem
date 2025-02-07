package contactmanager.ui;

import contactmanager.model.Contact;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ContactManager extends JFrame {

    //instance variable refactored out
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

        setTitle("Contact Management System");

        contacts = new ArrayList<>();

        /*preparing Card layout and main panel*/
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(240, 240, 240));

        createContactListPanel();
        createContactDetailPanel();
        createContactFormPanel();

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

    //Here were are creating contact list panel
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


//Here is Refreshing contact list panel
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
                public void mouseClicked(MouseEvent e) {
                    // Set this row as selected and update all row colors.
                    Integer idx = (Integer) rowPanel.getClientProperty("index");
                    selectedContactIndex = (idx != null ? idx : -1);
                    updateRowPanelColors(listContainer);
                }
            };
            
            // Contact name label
            JLabel nameLabel = new JLabel(contact.getName());
            nameLabel.setPreferredSize(new Dimension(250, 20));
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            nameLabel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
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

            // Delete button (red) with confirmation dialog

            JButton deleteButton = new JButton("Delete");
            deleteButton.setBackground(Color.RED);
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setFocusPainted(false);
            deleteButton.setFont(new Font("Arial", Font.BOLD, 13));
            deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            int finalI = i;
            deleteButton.addActionListener(e -> {
                int result = JOptionPane.showConfirmDialog(
                        ContactManager.this,
                        "Are you sure you want to delete this record?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );
                if (result == JOptionPane.YES_OPTION) {
                    contacts.remove(finalI);
                    // If the deleted row was selected, reset selection.
                    if(finalI == selectedContactIndex) {
                        selectedContactIndex = -1;
                    }
                    refreshContactListPanel(listContainer);
                }
            });

            rowPanel.add(nameLabel);
            rowPanel.add(editButton);
            rowPanel.add(deleteButton);
            rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowPanel.getPreferredSize().height));
            listContainer.add(rowPanel);
            // Add a 10px vertical gap after each row.
            listContainer.add(Box.createVerticalStrut(10));

        }
        listContainer.revalidate();
        listContainer.repaint();

    }


    //method for updating row panel
    private void updateRowPanelColors(JPanel listContainer) {
        Component[] comps = listContainer.getComponents();
        for (Component comp : comps) {
            if (comp instanceof JPanel) {
                JPanel row = (JPanel) comp;
                Integer idx = (Integer) row.getClientProperty("index");
                if (idx != null && idx == selectedContactIndex) {
                    row.setBackground(new Color(180, 180, 180));
                } else {
                    row.setBackground(new Color(230, 230, 230));
                }
            }
        }
        listContainer.repaint();
    }

    private boolean isRowSelected(JPanel rowPanel) {
        Integer idx = (Integer) rowPanel.getClientProperty("index");
        return (idx != null && idx == selectedContactIndex);
    }

//Here we're creating Contact Detail panel

    private void createContactDetailPanel(){
        contactDetailPanel = new JPanel(new BorderLayout());
        styleCardPanel(contactDetailPanel);

        JLabel header = new JLabel("CONTACT DETAILS VIEW", SwingConstants.CENTER);
        styleHeaderLabel(header);
        contactDetailPanel.add(header, BorderLayout.NORTH);

        JPanel detailContainer = new JPanel();
        detailContainer.setLayout(new BoxLayout(detailContainer, BoxLayout.Y_AXIS));
        detailContainer.setBackground(new Color(230, 230, 230));
        detailContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        detailNameLabel  = new JLabel("Name: ");
        detailPhoneLabel = new JLabel("Telephone Number: ");
        detailEmailLabel = new JLabel("Email Address: ");
        styleDetailLabel(detailNameLabel);
        styleDetailLabel(detailPhoneLabel);
        styleDetailLabel(detailEmailLabel);

        detailContainer.add(detailNameLabel);
        detailContainer.add(Box.createVerticalStrut(10));
        detailContainer.add(detailPhoneLabel);
        detailContainer.add(Box.createVerticalStrut(10));
        detailContainer.add(detailEmailLabel);

        contactDetailPanel.add(detailContainer, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(new Color(240, 240, 240));

        JButton backBtn = new JButton("Back To List");
        styleButton(backBtn);
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "CONTACT_LIST"));

        JButton listViewBtn = new JButton("Contact List View");
        styleButton(listViewBtn);
        listViewBtn.addActionListener(e -> cardLayout.show(mainPanel, "CONTACT_LIST"));

        bottomPanel.add(backBtn);
        bottomPanel.add(listViewBtn);

        contactDetailPanel.add(bottomPanel, BorderLayout.SOUTH);
    }


//Method to show Contact Details
    private void showContactDetails(int index) {
        if (index < 0 || index >= contacts.size())
            return;
        Contact c = contacts.get(index);
        detailNameLabel.setText("Name: " + c.getName());
        detailPhoneLabel.setText("Telephone Number: " + c.getPhone());
        detailEmailLabel.setText("Email Address: " + c.getEmail());
    }

  //Here we create contact form panel
    private void createContactFormPanel(){
        contactFormPanel = new JPanel(new BorderLayout());
        styleCardPanel(contactFormPanel);

        JLabel header = new JLabel("CONTACT CREATION FORM", SwingConstants.CENTER);
        styleHeaderLabel(header);
        contactFormPanel.add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBackground(new Color(230, 230, 230));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("Name:");
        styleDetailLabel(nameLabel);
        formNameField = new JTextField();
        formPanel.add(nameLabel);
        formPanel.add(formNameField);

        JLabel phoneLabel = new JLabel("Tel No:");
        styleDetailLabel(phoneLabel);
        formPhoneField = new JTextField();
        formPanel.add(phoneLabel);
        formPanel.add(formPhoneField);

        JLabel emailLabel = new JLabel("Email:");
        styleDetailLabel(emailLabel);
        formEmailField = new JTextField();
        formPanel.add(emailLabel);
        formPanel.add(formEmailField);

        contactFormPanel.add(formPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(new Color(240, 240, 240));

        JButton saveBtn = new JButton("Save Contact");
        styleButton(saveBtn);
        saveBtn.addActionListener(e -> {
            String name  = formNameField.getText().trim();
            String phone = formPhoneField.getText().trim();
            String email = formEmailField.getText().trim();

            //function to check all the fills whether are empty
            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "All fields must be filled out before saving.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (selectedContactIndex == -1) {
                contacts.add(new Contact(name, phone, email));
            } else {
                Contact c = contacts.get(selectedContactIndex);
                c.setName(name);
                c.setPhone(phone);
                c.setEmail(email);
            }

            cardLayout.show(mainPanel, "CONTACT_LIST");
            refreshContactListPanel((JPanel)((JScrollPane)contactListPanel.getComponent(1)).getViewport().getView());
        });

        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn);
        cancelBtn.addActionListener(e -> cardLayout.show(mainPanel, "CONTACT_LIST"));

        bottomPanel.add(saveBtn);
        bottomPanel.add(cancelBtn);
        contactFormPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void clearFormFields() {
        formNameField.setText("");
        formPhoneField.setText("");
        formEmailField.setText("");
    }

    private void styleCardPanel(JPanel panel) {
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(180, 180, 180), 2),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        );
    }

    private void styleHeaderLabel(JLabel label) {
        label.setOpaque(true);
        label.setBackground(new Color(70, 130, 180)); // SteelBlue
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    }

    private void styleDetailLabel(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(Color.BLACK);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180)); // SteelBlue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

}
