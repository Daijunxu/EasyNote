/**
 *
 */
package notes.gui.main.component;

import notes.data.cache.Property;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Defines the dialog and event listener for system's information.
 *
 * @author Rui Du
 * @version 1.0
 */
public class AboutDialog extends JDialog {

    private static final long serialVersionUID = -4869669171177951394L;
    private JButton okButton = new JButton(new AbstractAction("OK") {
        private static final long serialVersionUID = 8804876275155669513L;

        public void actionPerformed(ActionEvent e) {
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNavigation();
            }
            setVisible(false);
        }
    });

    /**
     * Constructs an instance of {@code AboutDialog}.
     */
    public AboutDialog() {
        super(MainPanel.get(), "About EasyNote", true);
        setIconImage(new ImageIcon("./resources/images/book.gif").getImage());
        MainPanel frame = MainPanel.get();

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
        getContentPane().add(dialogPanel);

        JPanel aboutPanel = new JPanel();
        aboutPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        aboutPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 5, 10, 5); // Top, left, bottom, right.
        JLabel label1 = new JLabel("EasyNote - An Easy Tool to Manage Reading Notes.");
        label1.setFont(new Font("Times", Font.BOLD, 12));
        label1.setForeground(Color.BLACK);
        aboutPanel.add(label1, c);

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(10, 5, 10, 5);
        JLabel label2 = new JLabel("Vension: 1.0");
        label2.setFont(new Font("Times", Font.BOLD, 12));
        label2.setForeground(Color.BLACK);
        aboutPanel.add(label2, c);

        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(10, 5, 10, 5);
        JLabel label3 = new JLabel("Copyright (c) 2012 Rui Du.  All rights reserved.");
        label3.setFont(new Font("Times", Font.BOLD, 12));
        label3.setForeground(Color.BLACK);
        aboutPanel.add(label3, c);

        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(20, 5, 0, 5);
        ImageIcon image = new ImageIcon("./resources/images/author.gif");
        JLabel imageLabel = new JLabel(image);
        aboutPanel.add(imageLabel, c);

        dialogPanel.add(aboutPanel);

        final JEditorPane pane = new JEditorPane(
                "text/html",
                "<center><b><font face='Times' size='3'>Visit the author's <a href=\"http://www.linkedin.com/pub/rui-du/2b/b33/96b\">LinkedIn</a>.</font></b></center>");
        pane.setEditable(false);
        pane.setBackground(getBackground());
        pane.setBorder(null);
        pane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent event) {
                if (HyperlinkEvent.EventType.ACTIVATED == event.getEventType()) {
                    JEditorPane pane = (JEditorPane) event.getSource();
                    String url = pane.getText();
                    url = url.substring(url.indexOf("http"), url.lastIndexOf("\""));
                    try {
                        Desktop.getDesktop().browse(new URI(url));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        dialogPanel.add(pane);

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(okButton);
        buttons.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        dialogPanel.add(buttons);

        pack();
        setLocationRelativeTo(frame);
        setResizable(false);
        setVisible(true);
    }
}
