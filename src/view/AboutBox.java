package view;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import org.jdesktop.application.Action;

/**
 * Zeigt erweiterte Informationen zur Anwendung sowie verwendete Technologien.
 * 
 * @author Dimitri Wegner
 */
public class AboutBox extends javax.swing.JDialog {

    private static final long serialVersionUID = -2080092741932127919L;
    final private static String LIBRARIES_HTML = "/resources/libraries.htm";

    /**
     * Erstellt eine Infobox mit den Informationen zu Tournament
     *
     * @param parent Fenster in dem der Dialog liegt
     */
    public AboutBox(java.awt.Frame parent) {
        super(parent);
        initComponents();
        getRootPane().setDefaultButton(closeButton);

        // HTML laden
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                URL lib = this.getClass().getResource(LIBRARIES_HTML);

                try {
                    libsTextPane.setPage(lib);
                } catch (IOException ex) {
                    Logger.getLogger(AboutBox.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    /**
     * Info Fenster schliessen
     */
    @Action
    public void closeAboutBox() {
        dispose();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        closeButton = new javax.swing.JButton();
        javax.swing.JLabel appTitleLabel = new javax.swing.JLabel();
        javax.swing.JLabel appVersionLabel = new javax.swing.JLabel();
        javax.swing.JLabel vendorLabel = new javax.swing.JLabel();
        javax.swing.JLabel appVendorLabel = new javax.swing.JLabel();
        javax.swing.JLabel appHomepageLabel = new javax.swing.JLabel();
        javax.swing.JLabel appDescLabel = new javax.swing.JLabel();
        javax.swing.JLabel imageLabel = new javax.swing.JLabel();
        lblUsedLibsAndRes = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        libsTextPane = new javax.swing.JTextPane();
        lblDeveloperCaption = new javax.swing.JLabel();
        lblDeveloper = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(control.MainApplication.class).getContext().getResourceMap(AboutBox.class);
        setTitle(resourceMap.getString("title")); // NOI18N
        setBackground(resourceMap.getColor("aboutBox.background")); // NOI18N
        setModal(true);
        setName("aboutBox"); // NOI18N
        setResizable(false);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(control.MainApplication.class).getContext().getActionMap(AboutBox.class, this);
        closeButton.setAction(actionMap.get("closeAboutBox")); // NOI18N
        closeButton.setText(resourceMap.getString("closeButton.text")); // NOI18N
        closeButton.setName("closeButton"); // NOI18N

        appTitleLabel.setFont(appTitleLabel.getFont().deriveFont(appTitleLabel.getFont().getStyle() | java.awt.Font.BOLD, appTitleLabel.getFont().getSize()+4));
        appTitleLabel.setText(resourceMap.getString("Application.title")); // NOI18N
        appTitleLabel.setName("appTitleLabel"); // NOI18N

        appVersionLabel.setName("appVersionLabel"); // NOI18N

        vendorLabel.setFont(vendorLabel.getFont().deriveFont(vendorLabel.getFont().getStyle() | java.awt.Font.BOLD));
        vendorLabel.setText(resourceMap.getString("vendorLabel.text")); // NOI18N
        vendorLabel.setName("vendorLabel"); // NOI18N

        appVendorLabel.setText(resourceMap.getString("Application.vendor")); // NOI18N
        appVendorLabel.setName("appVendorLabel"); // NOI18N

        appHomepageLabel.setName("appHomepageLabel"); // NOI18N

        appDescLabel.setText(resourceMap.getString("appDescLabel.text")); // NOI18N
        appDescLabel.setName("appDescLabel"); // NOI18N

        imageLabel.setIcon(resourceMap.getIcon("imageLabel.icon")); // NOI18N
        imageLabel.setName("imageLabel"); // NOI18N

        lblUsedLibsAndRes.setFont(lblUsedLibsAndRes.getFont().deriveFont(lblUsedLibsAndRes.getFont().getStyle() | java.awt.Font.BOLD));
        lblUsedLibsAndRes.setText(resourceMap.getString("lblUsedLibsAndRes.text")); // NOI18N
        lblUsedLibsAndRes.setName("lblUsedLibsAndRes"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        libsTextPane.setEditable(false);
        libsTextPane.setText(resourceMap.getString("libsTextPane.text")); // NOI18N
        libsTextPane.setName("libsTextPane"); // NOI18N
        jScrollPane1.setViewportView(libsTextPane);

        lblDeveloperCaption.setFont(lblDeveloperCaption.getFont().deriveFont(lblDeveloperCaption.getFont().getStyle() | java.awt.Font.BOLD));
        lblDeveloperCaption.setText(resourceMap.getString("lblDeveloperCaption.text")); // NOI18N
        lblDeveloperCaption.setName("lblDeveloperCaption"); // NOI18N

        lblDeveloper.setText(resourceMap.getString("lblDeveloper.text")); // NOI18N
        lblDeveloper.setName("lblDeveloper"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(imageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDeveloperCaption)
                            .addComponent(vendorLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblDeveloper, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(appVersionLabel)
                                    .addComponent(appHomepageLabel)))
                            .addComponent(appVendorLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))
                        .addGap(41, 41, 41))
                    .addComponent(appTitleLabel)
                    .addComponent(appDescLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                    .addComponent(closeButton, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblUsedLibsAndRes))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(appTitleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(appDescLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(appVersionLabel)
                        .addGap(26, 26, 26)
                        .addComponent(appHomepageLabel))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(vendorLabel)
                            .addComponent(appVendorLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDeveloperCaption)
                            .addComponent(lblDeveloper))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblUsedLibsAndRes)
                .addGap(4, 4, 4)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(closeButton)
                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDeveloper;
    private javax.swing.JLabel lblDeveloperCaption;
    private javax.swing.JLabel lblUsedLibsAndRes;
    private javax.swing.JTextPane libsTextPane;
    // End of variables declaration//GEN-END:variables
}
