/*

  Copyright (C) 2003-2009 Smardec. All rights reserved.

  http://www.smardec.com

*/

package com.smardec.license4j.demo;

import com.smardec.asc.hyperlink.JHyperlinkLabel;
import com.smardec.asc.splashscreen.JSplashScreen;
import com.smardec.license4j.License;
import com.smardec.license4j.LicenseManager;
import com.smardec.license4j.LicenseUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;


/**
 * License4J GUI demo.
 */
public class DemoFrame extends JFrame {
    private final ImageIcon imageSplash = new ImageIcon(DemoFrame.class.getClassLoader().getResource("com/smardec/license4j/demo/img/splash.gif"));

    private static class JBlackLabel extends JLabel {
        public JBlackLabel(String text) {
            super(text);
        }

        public Color getForeground() {
            return Color.black;
        }
    }

    public static void main(String[] args) {
        DemoFrame demoFrame = new DemoFrame();
        demoFrame.showSplash(3000);
        demoFrame.setSize(660, 550);
        centerOnScreen(demoFrame);
        demoFrame.show();
    }

    private JButton jButtonPrevious = new JButton("< Previous");
    private JButton jButtonNext = new JButton("Next >");
    private CardLayout cardLayout = new CardLayout();
    private JPanel jPanelCards = new JPanel(cardLayout);
    private int currentCard = 0;
    private static final int totalCards = 4;
    private boolean keysGenerated = false;

    public DemoFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("License4J Demo");
        setIconImage(new ImageIcon(getClass().getClassLoader().getResource("com/smardec/license4j/demo/img/logo.gif")).getImage());
        initControls();
    }

    public void showSplash(int displayTime) {
        final JSplashScreen splash = new JSplashScreen(this);
        splash.setImage(imageSplash);
        splash.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        splash.setCloseOnClick(true);
        splash.setMinimumDisplayTime(0);
        splash.setDisplayTime(displayTime);
        splash.setHyperlinkRectangle(new Rectangle(13, 226, 108, 12));
        splash.setUrl("http://www.smardec.com");
        splash.showSplash();
    }

    public void initControls() {
        jPanelCards.add(getIntroPanel(), "panel_1");
        jPanelCards.add(getKeysPanel(), "panel_2");
        jPanelCards.add(getLicensePanel(), "panel_3");
        jPanelCards.add(getLastPanel(), "panel_4");

        JPanel jPanelButtons = new JPanel(new GridLayout(1, 2, 5, 5));
        jButtonPrevious.setMnemonic('P');
        jButtonNext.setMnemonic('N');
        jButtonPrevious.setEnabled(false);
        jButtonPrevious.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.previous(jPanelCards);
                currentCard--;
                setNavigationButtonsState();
            }
        });
        jButtonNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentCard == totalCards - 1)
                    System.exit(0);
                cardLayout.next(jPanelCards);
                currentCard++;
                setNavigationButtonsState();
            }
        });
        jPanelButtons.add(jButtonPrevious);
        jPanelButtons.add(jButtonNext);
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.add(Box.createHorizontalGlue(), BorderLayout.CENTER);
        jPanel.add(jPanelButtons, BorderLayout.EAST);

        getContentPane().setLayout(new GridBagLayout());
        constrain(getContentPane(), jPanelCards,
                0, 0, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST, 1, 1,
                5, 5, 0, 5);
        constrain(getContentPane(), jPanel,
                0, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST, 0, 0,
                5, 5, 5, 5);
    }

    private JPanel getIntroPanel() {
        JPanel jPanel = new JPanel(new GridBagLayout());
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Introduction");
        titledBorder.setTitleColor(Color.black);
        jPanel.setBorder(titledBorder);
        JLabel jLabelIntro = new JBlackLabel("<html>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;License4J is a pure Java library for creating and validating licenses." +
                " <br><br>&nbsp;&nbsp;&nbsp;&nbsp;The key concept is features of a license. You can easily add any feature to your license file," +
                " i.e. name of the product, version, expiry date, number of executable instances, customer's name, company," +
                " IP address, etc." +
                " Feature's value can be string or any serializable Java object." +
                " You can have unsigned features - features that are not used in the signature generation/verification" +
                " and therefore can change their values leaving the license valid." +
                " <br>&nbsp;&nbsp;&nbsp;&nbsp;Also the library provides methods for signing arbitrary content that should not be changed by the user." +
                " <br><br>Advantages:" +
                " <br>&nbsp;&nbsp;-&nbsp;&nbsp;Easy to use" +
                " <br>&nbsp;&nbsp;-&nbsp;&nbsp;Straightforward API" +
                " <br>&nbsp;&nbsp;-&nbsp;&nbsp;Use of public key standard methods" +
                " <br>&nbsp;&nbsp;-&nbsp;&nbsp;Complete documentation" +
                " <br>&nbsp;&nbsp;-&nbsp;&nbsp;Compatible with JDK 1.3, JDK 1.4, JDK 1.5 and JDK 1.6" +
                " <br>&nbsp;&nbsp;-&nbsp;&nbsp;Backward compatibility" +
                " <br>&nbsp;&nbsp;-&nbsp;&nbsp;Free email support" +
                " <br>&nbsp;&nbsp;-&nbsp;&nbsp;Free one year updates and upgrades" +
                " <br>&nbsp;&nbsp;-&nbsp;&nbsp;No royalty or runtime fees" +
                "</html>");
        constrain(jPanel, jLabelIntro,
                0, 0, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST, 1, 1,
                0, 5, 5, 5);
        return jPanel;
    }

    private JPanel getKeysPanel() {
        JLabel jLabelIntro = new JBlackLabel("<html>" +
                "At first you should generate private and public keys." +
                " The private key should not be disclosed as it is used to create new license files." +
                " The public key is used to validate license files." +
                " The public key is a one-way function of the private key so it's almost impossible to compute the private key from the public one." +
                "</html>");
        JLabel jLabelPublicKey = new JBlackLabel("Public key:");
        JLabel jLabelPrivateKey = new JBlackLabel("Private key:");
        final JTextArea jTextAreaPublicKey = new JTextArea();
        jTextAreaPublicKey.setEditable(false);
        jTextAreaPublicKey.setLineWrap(true);
        jTextAreaPublicKey.setRows(5);
        final JTextArea jTextAreaPrivateKey = new JTextArea();
        jTextAreaPrivateKey.setEditable(false);
        jTextAreaPrivateKey.setLineWrap(true);
        jTextAreaPrivateKey.setRows(5);
        final JButton jButtonGenerateKeys = new JButton("Generate keys");
        jButtonGenerateKeys.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] keys = LicenseUtil.createKeyPair();
                jTextAreaPublicKey.setText(keys[0]);
                jTextAreaPrivateKey.setText(keys[1]);
                LicenseManager.setPublicKey(keys[0]);
                LicenseManager.setPrivateKey(keys[1]);
                keysGenerated = true;
                setNavigationButtonsState();
                jButtonGenerateKeys.setEnabled(false);
            }
        });

        JPanel jPanel = new JPanel(new GridBagLayout());
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Generating keys");
        titledBorder.setTitleColor(Color.black);
        jPanel.setBorder(titledBorder);

        constrain(jPanel, jLabelIntro,
                0, 0, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST, 1, 0,
                0, 5, 5, 5);
        constrain(jPanel, jLabelPublicKey,
                0, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST, 1, 0,
                0, 5, 0, 5);
        constrain(jPanel, new JScrollPane(jTextAreaPublicKey),
                0, 2, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST, 1, 1,
                5, 5, 0, 5);
        constrain(jPanel, jLabelPrivateKey,
                1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST, 1, 0,
                0, 5, 0, 5);
        constrain(jPanel, new JScrollPane(jTextAreaPrivateKey),
                1, 2, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST, 1, 1,
                5, 5, 0, 5);
        constrain(jPanel, jButtonGenerateKeys,
                0, 3, 2, 1, GridBagConstraints.NONE, GridBagConstraints.NORTHEAST, 1, 0,
                5, 5, 5, 5);
        return jPanel;
    }

    private JPanel getLicensePanel() {
        JLabel jLabelIntro = new JBlackLabel("<html>" +
                "Here you can create sample license." +
                " You can edit the features below and preview/edit the resulting license." +
                "</html>");
        JLabel jLabelFeatures = new JBlackLabel("Features:");
        final JTextArea jTextAreaFeatures = new JTextArea("# Here you can define features\n" +
                "# that will be in the license\n\n" +
                "Customer=Customer name\n" +
                "Company=Company name\n" +
                "Feature1=true\n" +
                "Feature2=false\n" +
                "Expire=10-10-2009");
        JLabel jLabelLicense = new JBlackLabel("License:");
        final JTextArea jTextAreaLicense = new JTextArea();
        JButton jButtonCreateLicense = new JButton("Create license");
        final JButton jButtonValidateLicense = new JButton("Validate license");
        jButtonValidateLicense.setEnabled(false);

        jButtonCreateLicense.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String features = jTextAreaFeatures.getText();
                    InputStream inputStream = new ByteArrayInputStream(features.getBytes());
                    License license = LicenseManager.loadLicense(inputStream);
                    StringWriter writer = new StringWriter();
                    LicenseManager.saveLicense(license, writer);
                    writer.close();
                    jTextAreaLicense.setText(writer.toString());
                    jTextAreaLicense.setCaretPosition(0);
                } catch (Exception exception) {}
                jButtonValidateLicense.setEnabled(true);
            }
        });
        jButtonValidateLicense.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String licenseText = jTextAreaLicense.getText();
                    InputStream inputStream = new ByteArrayInputStream(licenseText.getBytes());
                    License license = LicenseManager.loadLicense(inputStream);
                    if (LicenseManager.isValid(license))
                        JOptionPane.showMessageDialog(DemoFrame.this, "License is valid.", "", JOptionPane.INFORMATION_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(DemoFrame.this, "License is not valid.", "", JOptionPane.ERROR_MESSAGE);
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(DemoFrame.this, "License is not valid.", "", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel jPanel = new JPanel(new GridBagLayout());
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Licensing");
        titledBorder.setTitleColor(Color.black);
        jPanel.setBorder(titledBorder);

        constrain(jPanel, jLabelIntro,
                0, 0, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST, 1, 0,
                0, 5, 5, 5);
        constrain(jPanel, jLabelFeatures,
                0, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST, 1, 0,
                0, 5, 0, 5);
        constrain(jPanel, jLabelLicense,
                1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST, 1, 0,
                0, 0, 0, 5);
        JPanel jPanelTextAreas = new JPanel(new GridLayout(1, 2, 5, 5));
        jPanelTextAreas.add(new JScrollPane(jTextAreaFeatures));
        jPanelTextAreas.add(new JScrollPane(jTextAreaLicense));
        constrain(jPanel, jPanelTextAreas,
                0, 2, 2, 1, GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST, 1, 1,
                5, 5, 0, 5);
        JPanel jPanelButtons = new JPanel(new GridLayout(1, 2, 5, 5));
        jPanelButtons.add(jButtonCreateLicense);
        jPanelButtons.add(jButtonValidateLicense);
        constrain(jPanel, jPanelButtons,
                0, 3, 2, 1, GridBagConstraints.NONE, GridBagConstraints.NORTHEAST, 0, 0,
                5, 5, 5, 5);
        return jPanel;
    }

    private JPanel getLastPanel() {
        JPanel jPanelInfo = new JPanel(new GridBagLayout());
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Information");
        titledBorder.setTitleColor(Color.black);
        jPanelInfo.setBorder(titledBorder);
        constrain(jPanelInfo, new JBlackLabel("Links:"),
                0, 0, 1, 1, GridBagConstraints.NONE, GridBagConstraints.NORTHWEST, 0, 0,
                0, 5, 0, 0);
        constrain(jPanelInfo, new JBlackLabel("   Smardec web site:"),
                0, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.NORTHWEST, 0, 0,
                0, 8, 0, 0);
        constrain(jPanelInfo, new JHyperlinkLabel("http://www.smardec.com", "http://www.smardec.com"),
                1, 1, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST, 1.0, 0,
                0, 8, 0, 0);
        constrain(jPanelInfo, new JBlackLabel("   License4J web page:"),
                0, 2, 1, 1, GridBagConstraints.NONE, GridBagConstraints.NORTHWEST, 0, 0,
                5, 8, 0, 0);
        constrain(jPanelInfo, new JHyperlinkLabel("http://www.smardec.com/products/license4j.html", "http://www.smardec.com/products/license4j.html"),
                1, 2, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST, 1.0, 0,
                5, 8, 0, 0);
        constrain(jPanelInfo, new JBlackLabel("   The demo uses Advanced Swing Components:"),
                0, 3, 1, 1, GridBagConstraints.NONE, GridBagConstraints.NORTHWEST, 0, 0,
                5, 8, 0, 0);
        constrain(jPanelInfo, new JHyperlinkLabel("http://www.smardec.com/products/asc.html", "http://www.smardec.com/products/asc.html"),
                1, 3, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST, 1.0, 0,
                5, 8, 0, 0);
        constrain(jPanelInfo, new JBlackLabel("   Obfuscation by Allatori Java Obfuscator:"),
                0, 4, 1, 1, GridBagConstraints.NONE, GridBagConstraints.NORTHWEST, 0, 0,
                5, 8, 0, 0);
        constrain(jPanelInfo, new JHyperlinkLabel("http://www.allatori.com", "http://www.allatori.com"),
                1, 4, 1, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.NORTHWEST, 1.0, 0,
                5, 8, 0, 0);
        JLabel jLabel = new JBlackLabel("<html>" +
                "More examples can be found at 'samples/console'<br>" +
                "Source code of all examples can be found at 'samples/src'<br>" +
                "API documentation can be found at 'api'<br><br>" +
                "If you have any questions or comments, please email license4j@smardec.com" +
                "</html>");
        constrain(jPanelInfo, jLabel,
                0, 5, 2, 1, GridBagConstraints.NONE, GridBagConstraints.NORTHWEST, 0, 0,
                20, 8, 0, 0);
        JLabel jLabelCopyright = new JBlackLabel("<html>" +
                "Copyright (C) 2003-2009 Smardec. All rights reserved.</html>");
        constrain(jPanelInfo, jLabelCopyright,
                0, 6, 2, 1, GridBagConstraints.HORIZONTAL, GridBagConstraints.SOUTHWEST, 1, 1,
                5, 8, 5, 5);
        return jPanelInfo;
    }

    private void setNavigationButtonsState() {
        if (currentCard == 0)
            jButtonPrevious.setEnabled(false);
        else
            jButtonPrevious.setEnabled(true);
        if (currentCard == totalCards - 1)
            jButtonNext.setText("Exit");
        else
            jButtonNext.setText("Next >");
        if (currentCard == 1 && !keysGenerated)
            jButtonNext.setEnabled(false);
        else
            jButtonNext.setEnabled(true);
    }


    private static void constrain(Container container, Component component, int x, int y, int width, int height, int fill, int anchor, double weightx, double weighty, int insetTop, int insetLeft, int insetBottom, int insetRight) {
        Insets insets = new Insets(insetTop, insetLeft, insetBottom, insetRight);
        GridBagConstraints gbc = new GridBagConstraints(x, y, width, height, weightx, weighty, anchor, fill, insets, 0, 0);
        LayoutManager lm = container.getLayout();
        GridBagLayout gbl = (GridBagLayout) lm;
        gbl.setConstraints(component, gbc);
        container.add(component);
    }

    private static void centerOnScreen(Component component) {
        if (component == null)
            return;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension compSize = component.getSize();
        if (compSize.height > screenSize.height)
            compSize.height = screenSize.height;
        if (compSize.width > screenSize.width)
            compSize.width = screenSize.width;
        component.setSize(compSize);
        component.setLocation((screenSize.width - compSize.width) / 2, (screenSize.height - compSize.height) / 2);
    }
}
