/*
 * ChangePasswortDialog.java
 *
 * Created on 12.03.2009, 18:11:27
 */
package view.forms;

import control.UserManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.User;
import org.jdesktop.application.Action;
import tools.SimpleSHA1;

/**
 * Dialog zum Ändern des Passwortes von einem Benutzer.
 * Der Benutzer wird beim Erstellen übergeben.
 *
 * @author Dimitri Wegner
 */
public class ChangePasswortDialog extends javax.swing.JDialog {

	private static final long serialVersionUID = -7070603239214601256L;
	
	private User user;
    private boolean passChanged = false;

    /**
     * Creates new form ChangePasswortDialog
     *
     * @param parent Fenster in dem der Dialog liegt
     * @param u User für den das Passwort geändert werden soll
     */
    public ChangePasswortDialog(java.awt.Frame parent, User u) {
        super(parent);
        user = u;
        initComponents();
        setLocationRelativeTo(parent);

        // Am Anfang schon validieren
        validateForm();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        messagePanel = new view.tools.MessagePanel();
        lblOldPass = new javax.swing.JLabel();
        lblNewPass = new javax.swing.JLabel();
        lblNewPass2 = new javax.swing.JLabel();
        txtNewPass2 = new javax.swing.JPasswordField();
        txtNewPass = new javax.swing.JPasswordField();
        txtOldPass = new javax.swing.JPasswordField();
        btnConfirm = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(control.MainApplication.class).getContext().getResourceMap(ChangePasswortDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setModal(true);
        setName("Form"); // NOI18N
        setResizable(false);

        messagePanel.setName("messagePanel"); // NOI18N

        lblOldPass.setText(resourceMap.getString("lblOldPass.text")); // NOI18N
        lblOldPass.setName("lblOldPass"); // NOI18N

        lblNewPass.setText(resourceMap.getString("lblNewPass.text")); // NOI18N
        lblNewPass.setName("lblNewPass"); // NOI18N

        lblNewPass2.setText(resourceMap.getString("lblNewPass2.text")); // NOI18N
        lblNewPass2.setName("lblNewPass2"); // NOI18N

        txtNewPass2.setName("txtNewPass2"); // NOI18N
        txtNewPass2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAction(evt);
            }
        });
        txtNewPass2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                onInput(evt);
            }
        });

        txtNewPass.setName("txtNewPass"); // NOI18N
        txtNewPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAction(evt);
            }
        });
        txtNewPass.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                onInput(evt);
            }
        });

        txtOldPass.setName("txtOldPass"); // NOI18N
        txtOldPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAction(evt);
            }
        });
        txtOldPass.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                onInput(evt);
            }
        });

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(control.MainApplication.class).getContext().getActionMap(ChangePasswortDialog.class, this);
        btnConfirm.setAction(actionMap.get("confirmAction")); // NOI18N
        btnConfirm.setName("btnConfirm"); // NOI18N

        btnCancel.setAction(actionMap.get("cancelAction")); // NOI18N
        btnCancel.setName("btnCancel"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(messagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblOldPass)
                        .addGap(63, 63, 63)
                        .addComponent(txtOldPass, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNewPass2)
                            .addComponent(lblNewPass))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNewPass2, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                            .addComponent(txtNewPass, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(162, 162, 162)
                        .addComponent(btnConfirm)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(messagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblOldPass)
                    .addComponent(txtOldPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNewPass)
                    .addComponent(txtNewPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNewPass2)
                    .addComponent(txtNewPass2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnConfirm))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void onInput(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_onInput
        setConfirmActionEnabled(validateForm());
}//GEN-LAST:event_onInput

    private void onAction(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAction
        setConfirmActionEnabled(validateForm());
    }//GEN-LAST:event_onAction

    /**
     * Get the value of passChanged
     *
     * @return the value of passChanged
     */
    public boolean hasPassChanged() {
        return passChanged;
    }

    /** Abbrechen-Aktion */
    @Action
    public void cancelAction() {
        dispose();
    }

    /** Bestätigen-Aktion */
    @Action(enabledProperty = "confirmActionEnabled")
    public void confirmAction() {
        UserManager.getInstance().changePasswort(user,
            String.valueOf(txtOldPass.getPassword()),
            String.valueOf(txtNewPass.getPassword()));

        passChanged = true;

        dispose();
    }
    private boolean confirmActionEnabled = false;

    /**
     *  Ob der Bestätigen getätigt wurde
     * @return Status
     */
    public boolean isConfirmActionEnabled() {
        return confirmActionEnabled;
    }

    /**
     * Setzt den Status ob Bestätigen getätigt wueder
     * @param b Status
     */
    public void setConfirmActionEnabled(boolean b) {
        boolean old = isConfirmActionEnabled();
        this.confirmActionEnabled = b;
        firePropertyChange("confirmActionEnabled", old, isConfirmActionEnabled());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JLabel lblNewPass;
    private javax.swing.JLabel lblNewPass2;
    private javax.swing.JLabel lblOldPass;
    private view.tools.MessagePanel messagePanel;
    private javax.swing.JPasswordField txtNewPass;
    private javax.swing.JPasswordField txtNewPass2;
    private javax.swing.JPasswordField txtOldPass;
    // End of variables declaration//GEN-END:variables

    /**
     * Validiert die Form, zeigt Dabei entsprechende Fehlernachrichten.
     * 
     * @return true, falls alle Eingaben richtig sind, sonst false.
     */
    private boolean validateForm() {
        boolean valid = true;

        messagePanel.clear();

        // Altes Pass
        try {
            if (!user.getPassword().equals(
                SimpleSHA1.SHA1(String.valueOf(txtOldPass.getPassword())))) {
                messagePanel.addMessage("Das alte Passwort ist falsch.");
                valid = false;
            }
        } catch (Exception ex) {
            Logger.getLogger(ChangePasswortDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Neue Passwörter
        String newPass1 = String.valueOf(txtNewPass.getPassword());
        String newPass2 = String.valueOf(txtNewPass2.getPassword());

        if (newPass1.trim().length() < 3) {
            messagePanel.addMessage(
                "Das neue Passwort muss mindestens die Länge 3 haben.");
            valid = false;
        } else if (!newPass1.equals(newPass2)) {
            messagePanel.addMessage(
                "Die eingebenen Passwörter stimmen nicht überein.");
            valid = false;
        }

        // Rendern
        messagePanel.renderText();
        pack();

        return valid;
    }
}
