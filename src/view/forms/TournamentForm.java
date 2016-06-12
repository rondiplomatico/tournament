/*
 * TournamentForm.java
 *
 * Created on 03.03.2009, 17:40:55
 * @author Dimitri Wegner
 */
package view.forms;

import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JOptionPane;

import model.Tournament;
import model.enums.TournamentType;

import org.jdesktop.application.Action;
import org.jdesktop.observablecollections.ObservableCollections;

import view.ContentPanel;
import control.MainApplication;
import control.TournamentManager;

/**
 * Formular zum Erstellen eines neuen Turniers.
 *
 * @author Dimitri Wegner
 */
public class TournamentForm extends ContentPanel {

	private static final long serialVersionUID = 8697763076119568696L;
	private List<String> fieldList;
	private List<String> selectedFields;

	/** Erstellt eine neue TournamentForm */
	public TournamentForm() {
		// Feldliste initialisieren
		fieldList = ObservableCollections
				.observableList(new ArrayList<String>());

		// Komponenten initialisieren
		initComponents();

		//
		// Defaultwerte für Startzeitpunke.
		// Ablauf = eine Woche ab heute
		// Start = zwei Wochen ab heute
		//
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		try {
			expireDatePicker.setDate(cal.getTime());
			cal.add(Calendar.WEEK_OF_YEAR, 1);
			startDatePicker.setDate(cal.getTime());
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}

		// Schon am Anfang validieren
		validateForm();
	}

	//
	// Getter und Setter
	//
	/**
	 * Get the value of fieldList
	 *
	 * @return the value of fieldList
	 */
	public List<String> getFieldList() {
		return fieldList;
	}

	/**
	 * Set the value of fieldList
	 *
	 * @param fieldList
	 *            new value of fieldList
	 */
	public void setFieldList(List<String> fieldList) {
		this.fieldList = fieldList;
	}

	/**
	 * Get the value of selectedFields
	 *
	 * @return the value of selectedFields
	 */
	public List<String> getSelectedFields() {
		return selectedFields;
	}

	/**
	 * Set the value of selectedFields
	 *
	 * @param selectedFields
	 *            new value of selectedFields
	 */
	public void setSelectedFields(List<String> selectedFields) {
		this.selectedFields = selectedFields;
	}

	//
	// Controller-Methoden
	//
	/**
	 * Überprüft die Benutzer-Eingaben. Zeigt dabei entsprechende Fehler.
	 *
	 * @return Sind alle Eingaben richtig, so wird true zurückgegeben, sonst
	 *         false.
	 */
	private boolean validateForm() {
		boolean valid = true;

		messagePanel.clear();

		// Bezeichnung
		if (txtLabel.getText().isEmpty()) {
			messagePanel.addMessage("Bezeichnung ist leer");
			valid = false;
		}

		// Sportart
		if (txtSportKind.getText().isEmpty()) {
			messagePanel.addMessage("Sportart ist leer.");
			valid = false;
		}

		// Austragungsort
		if (txtLocation.getText().isEmpty()) {
			messagePanel.addMessage("Austragungsort ist leer.");
			valid = false;
		}

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date today = cal.getTime();

		// Startdatum
		if (startDatePicker.getDate().before(today)) {
			messagePanel.addMessage("Startdatum liegt in der Vergangenheit.");
			valid = false;
		}

		// Anmeldungsablaufdatum
		if (expireDatePicker.getDate().before(today)) {
			messagePanel
					.addMessage("Abmeldungsablaufdatum liegt in der Vergangenheit.");
			valid = false;
		} else if (startDatePicker.getDate().before(expireDatePicker.getDate())) {
			messagePanel
					.addMessage("Startdatum liegt vor dem Abmeldungsablaufdatum.");
			valid = false;
		}

		// Felder
		if (fieldList.isEmpty()) {
			messagePanel.addMessage("Kein Feld wurde festgelegt.");
			valid = false;
		}

		messagePanel.renderText();
		validate();

		return valid;
	}

	/** Turnier Erstellen Aktion */
	@Action(enabledProperty = "tournamentCreateEnabled")
	public void createTournament() {
		Tournament t = new Tournament(txtLabel.getText(),
				(Float) spnFee.getValue(), (Integer) spnMaxPart.getValue(),
				txtPartCond.getText(), (Integer) spnStdGameDuration.getValue(),
				0, startDatePicker.getDate());

		t.setSportKind(txtSportKind.getText());
		t.setLateRegistrationFee((Float) spnLateRegFee.getValue());
		t.setExpireDate(expireDatePicker.getDate());
		t.setLocation(txtLocation.getText());

		if (cmbType.getSelectedIndex() == 0) {
			t.setType(TournamentType.SinglePlayer);
		} else {
			t.setType(TournamentType.MultiPlayer);
			t.setRequiredPlayersPerTeam((Integer) spnMaxPlayers.getValue());
		}

		// Felder setzen
		String[] fields = new String[fieldList.size()];
		fieldList.toArray(fields);
		t.setFields(fields);

		TournamentManager.getInstance().createTournament(t);

		// Rückmeldung
		fireCallbackEvent(true);
	}

	private boolean tournamentCreateActionEnabled = false;

	/**
	 * Gibt wieder o ein Turnier erstellt wird
	 *
	 * @return Stauts ob Turnier erstellt wieder
	 */
	public boolean isTournamentCreateEnabled() {
		return tournamentCreateActionEnabled;
	}

	/**
	 * Setzt ob ein Turnier erstellt wieder
	 *
	 * @param b
	 *            Turniererstellmodus aktiviert werden
	 */
	public void setTournamentCreateEnabled(boolean b) {
		boolean old = isTournamentCreateEnabled();
		this.tournamentCreateActionEnabled = b;
		firePropertyChange("tournamentCreateEnabled", old,
				isTournamentCreateEnabled());
	}

	/** Rückmeldung beim Abbrechen */
	@Action
	public void cancelAction() {
		fireCallbackEvent(false);
	}

	/** Aktualisiert Enabled-Eigenschaft von spnMaxPlayers */
	@Action
	public void chooseTournamentTypeAction() {
		spnMaxPlayers.setEnabled(cmbType.getSelectedIndex() == 1);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

		lblHeader = new javax.swing.JLabel();
		messagePanel = new view.tools.MessagePanel();
		panelTournament = new javax.swing.JPanel();
		lblLabel = new javax.swing.JLabel();
		lblType = new javax.swing.JLabel();
		txtLabel = new javax.swing.JTextField();
		cmbType = new javax.swing.JComboBox();
		panelObligatory = new javax.swing.JPanel();
		lblSportKind = new javax.swing.JLabel();
		lblLocation = new javax.swing.JLabel();
		lblStartDate = new javax.swing.JLabel();
		lblExpireDate = new javax.swing.JLabel();
		lblMaxPart = new javax.swing.JLabel();
		lblMaxPlayerProTeam = new javax.swing.JLabel();
		lblFee = new javax.swing.JLabel();
		lblLateRegFee = new javax.swing.JLabel();
		lblMatchDuration = new javax.swing.JLabel();
		txtSportKind = new javax.swing.JTextField();
		txtLocation = new javax.swing.JTextField();
		expireDatePicker = new com.michaelbaranov.microba.calendar.DatePicker();
		startDatePicker = new com.michaelbaranov.microba.calendar.DatePicker();
		spnMaxPart = new javax.swing.JSpinner();
		spnMaxPlayers = new javax.swing.JSpinner();
		spnFee = new javax.swing.JSpinner();
		spnLateRegFee = new javax.swing.JSpinner();
		spnStdGameDuration = new javax.swing.JSpinner();
		panelPartCond = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		txtPartCond = new javax.swing.JTextPane();
		panelFields = new javax.swing.JPanel();
		txtFieldName = new javax.swing.JTextField();
		lblFieldName = new javax.swing.JLabel();
		btnAddField = new javax.swing.JButton();
		fieldsScrollPane = new javax.swing.JScrollPane();
		fieldListView = new javax.swing.JList();
		btnRemoveField = new javax.swing.JButton();
		jButton1 = new javax.swing.JButton();
		btnConfirm = new javax.swing.JButton();
		btnCancel = new javax.swing.JButton();

		setName("Form"); // NOI18N

		lblHeader.setFont(lblHeader.getFont().deriveFont(
				lblHeader.getFont().getStyle() | java.awt.Font.BOLD,
				lblHeader.getFont().getSize() + 3));
		org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
				.getInstance(control.MainApplication.class).getContext()
				.getResourceMap(TournamentForm.class);
		lblHeader.setText(resourceMap.getString("lblHeader.text")); // NOI18N
		lblHeader.setName("lblHeader"); // NOI18N

		messagePanel.setName("messagePanel"); // NOI18N

		panelTournament.setBorder(javax.swing.BorderFactory
				.createTitledBorder(resourceMap
						.getString("panelTournament.border.title"))); // NOI18N
		panelTournament.setName("panelTournament"); // NOI18N

		lblLabel.setText(resourceMap.getString("lblLabel.text")); // NOI18N
		lblLabel.setName("lblLabel"); // NOI18N

		lblType.setText(resourceMap.getString("lblType.text")); // NOI18N
		lblType.setName("lblType"); // NOI18N

		txtLabel.setName("txtLabel"); // NOI18N
		txtLabel.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				TournamentForm.this.focusLost(evt);
			}
		});

		cmbType.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"Einzelspieler-Turnier", "Mehrspieler-Turnier" }));
		javax.swing.ActionMap actionMap = org.jdesktop.application.Application
				.getInstance(control.MainApplication.class).getContext()
				.getActionMap(TournamentForm.class, this);
		cmbType.setAction(actionMap.get("chooseTournamentTypeAction")); // NOI18N
		cmbType.setName("cmbType"); // NOI18N

		javax.swing.GroupLayout panelTournamentLayout = new javax.swing.GroupLayout(
				panelTournament);
		panelTournament.setLayout(panelTournamentLayout);
		panelTournamentLayout
				.setHorizontalGroup(panelTournamentLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								panelTournamentLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												panelTournamentLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(lblType)
														.addComponent(lblLabel))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												panelTournamentLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(cmbType,
																0, 183,
																Short.MAX_VALUE)
														.addComponent(
																txtLabel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																183,
																Short.MAX_VALUE))
										.addContainerGap()));
		panelTournamentLayout
				.setVerticalGroup(panelTournamentLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								panelTournamentLayout
										.createSequentialGroup()
										.addGroup(
												panelTournamentLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																txtLabel,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(lblLabel))
										.addGap(9, 9, 9)
										.addGroup(
												panelTournamentLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(lblType)
														.addComponent(
																cmbType,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		panelObligatory.setBorder(javax.swing.BorderFactory
				.createTitledBorder(resourceMap
						.getString("panelObligatory.border.title"))); // NOI18N
		panelObligatory.setName("panelObligatory"); // NOI18N

		lblSportKind.setText(resourceMap.getString("lblSportKind.text")); // NOI18N
		lblSportKind.setName("lblSportKind"); // NOI18N

		lblLocation.setText(resourceMap.getString("lblLocation.text")); // NOI18N
		lblLocation.setName("lblLocation"); // NOI18N

		lblStartDate.setLabelFor(startDatePicker);
		lblStartDate.setText(resourceMap.getString("lblStartDate.text")); // NOI18N
		lblStartDate.setName("lblStartDate"); // NOI18N

		lblExpireDate.setLabelFor(expireDatePicker);
		lblExpireDate.setText(resourceMap.getString("lblExpireDate.text")); // NOI18N
		lblExpireDate.setName("lblExpireDate"); // NOI18N

		lblMaxPart.setText(resourceMap.getString("lblMaxPart.text")); // NOI18N
		lblMaxPart.setName("lblMaxPart"); // NOI18N

		lblMaxPlayerProTeam.setText(resourceMap
				.getString("lblMaxPlayerProTeam.text")); // NOI18N
		lblMaxPlayerProTeam.setName("lblMaxPlayerProTeam"); // NOI18N

		lblFee.setText(resourceMap.getString("lblFee.text")); // NOI18N
		lblFee.setName("lblFee"); // NOI18N

		lblLateRegFee.setText(resourceMap.getString("lblLateRegFee.text")); // NOI18N
		lblLateRegFee.setName("lblLateRegFee"); // NOI18N

		lblMatchDuration
				.setText(resourceMap.getString("lblMatchDuration.text")); // NOI18N
		lblMatchDuration.setName("lblMatchDuration"); // NOI18N

		txtSportKind.setName("txtSportKind"); // NOI18N
		txtSportKind.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				TournamentForm.this.focusLost(evt);
			}
		});

		txtLocation.setName("txtLocation"); // NOI18N
		txtLocation.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				TournamentForm.this.focusLost(evt);
			}
		});

		expireDatePicker.setName("expireDatePicker"); // NOI18N
		expireDatePicker.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				TournamentForm.this.focusLost(evt);
			}
		});
		expireDatePicker
				.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
					public void propertyChange(
							java.beans.PropertyChangeEvent evt) {
						datePickerPropChanged(evt);
					}
				});

		startDatePicker.setName("startDatePicker"); // NOI18N
		startDatePicker.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				TournamentForm.this.focusLost(evt);
			}
		});
		startDatePicker
				.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
					public void propertyChange(
							java.beans.PropertyChangeEvent evt) {
						datePickerPropChanged(evt);
					}
				});

		spnMaxPart.setModel(new javax.swing.SpinnerNumberModel(Integer
				.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
		spnMaxPart.setName("spnMaxPart"); // NOI18N

		spnMaxPlayers.setModel(new javax.swing.SpinnerNumberModel(Integer
				.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
		spnMaxPlayers.setEnabled(false);
		spnMaxPlayers.setName("spnMaxPlayers"); // NOI18N

		spnFee.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f),
				Float.valueOf(0.0f), null, Float.valueOf(1.0f)));
		spnFee.setName("spnFee"); // NOI18N

		spnLateRegFee
				.setModel(new javax.swing.SpinnerNumberModel(Float
						.valueOf(0.0f), Float.valueOf(0.0f), null, Float
						.valueOf(1.0f)));
		spnLateRegFee.setName("spnLateRegFee"); // NOI18N

		spnStdGameDuration.setModel(new javax.swing.SpinnerNumberModel(Integer
				.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(10)));
		spnStdGameDuration.setName("spnStdGameDuration"); // NOI18N

		javax.swing.GroupLayout panelObligatoryLayout = new javax.swing.GroupLayout(
				panelObligatory);
		panelObligatory.setLayout(panelObligatoryLayout);
		panelObligatoryLayout
				.setHorizontalGroup(panelObligatoryLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								panelObligatoryLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												panelObligatoryLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																panelObligatoryLayout
																		.createSequentialGroup()
																		.addGroup(
																				panelObligatoryLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(
																								lblStartDate)
																						.addComponent(
																								lblLateRegFee)
																						.addComponent(
																								lblSportKind)
																						.addComponent(
																								lblFee)
																						.addComponent(
																								lblMaxPlayerProTeam,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								220,
																								javax.swing.GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								lblMaxPart)
																						.addComponent(
																								lblExpireDate)
																						.addComponent(
																								lblLocation))
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addGroup(
																				panelObligatoryLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(
																								txtSportKind,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								193,
																								Short.MAX_VALUE)
																						.addComponent(
																								spnMaxPart,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								193,
																								Short.MAX_VALUE)
																						.addComponent(
																								spnMaxPlayers,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								193,
																								Short.MAX_VALUE)
																						.addComponent(
																								spnFee,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								193,
																								Short.MAX_VALUE)
																						.addComponent(
																								spnLateRegFee,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								193,
																								Short.MAX_VALUE)
																						.addComponent(
																								spnStdGameDuration,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								193,
																								Short.MAX_VALUE)
																						.addComponent(
																								startDatePicker,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								193,
																								Short.MAX_VALUE)
																						.addComponent(
																								expireDatePicker,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								193,
																								Short.MAX_VALUE)
																						.addComponent(
																								txtLocation,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								193,
																								Short.MAX_VALUE)))
														.addComponent(
																lblMatchDuration))
										.addContainerGap()));
		panelObligatoryLayout
				.setVerticalGroup(panelObligatoryLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								panelObligatoryLayout
										.createSequentialGroup()
										.addGroup(
												panelObligatoryLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																lblSportKind,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																20,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																txtSportKind,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(5, 5, 5)
										.addGroup(
												panelObligatoryLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																txtLocation,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																lblLocation))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												panelObligatoryLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																expireDatePicker,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																lblExpireDate))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												panelObligatoryLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																lblStartDate)
														.addComponent(
																startDatePicker,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												panelObligatoryLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																spnMaxPart,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																lblMaxPart))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												panelObligatoryLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(
																lblMaxPlayerProTeam)
														.addComponent(
																spnMaxPlayers,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												panelObligatoryLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(lblFee)
														.addComponent(
																spnFee,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												panelObligatoryLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																lblLateRegFee)
														.addComponent(
																spnLateRegFee,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												panelObligatoryLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																lblMatchDuration)
														.addComponent(
																spnStdGameDuration,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap()));

		panelPartCond.setBorder(javax.swing.BorderFactory
				.createTitledBorder(resourceMap
						.getString("panelPartCond.border.title"))); // NOI18N
		panelPartCond.setName("panelPartCond"); // NOI18N

		jScrollPane1.setName("jScrollPane1"); // NOI18N

		txtPartCond.setName("txtPartCond"); // NOI18N
		jScrollPane1.setViewportView(txtPartCond);

		javax.swing.GroupLayout panelPartCondLayout = new javax.swing.GroupLayout(
				panelPartCond);
		panelPartCond.setLayout(panelPartCondLayout);
		panelPartCondLayout
				.setHorizontalGroup(panelPartCondLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								panelPartCondLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												jScrollPane1,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												706, Short.MAX_VALUE)
										.addContainerGap()));
		panelPartCondLayout
				.setVerticalGroup(panelPartCondLayout.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING).addGroup(
						panelPartCondLayout
								.createSequentialGroup()
								.addComponent(jScrollPane1,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										54, Short.MAX_VALUE).addContainerGap()));

		panelFields.setBorder(javax.swing.BorderFactory
				.createTitledBorder(resourceMap
						.getString("panelFields.border.title"))); // NOI18N
		panelFields.setName("panelFields"); // NOI18N

		txtFieldName.setName("txtFieldName"); // NOI18N
		txtFieldName
				.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
					public void propertyChange(
							java.beans.PropertyChangeEvent evt) {
						txtFieldNamePropertyChange(evt);
					}
				});
		txtFieldName.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent evt) {
				txtFieldNameKeyTyped(evt);
			}
		});

		lblFieldName.setText(resourceMap.getString("lblFieldName.text")); // NOI18N
		lblFieldName.setName("lblFieldName"); // NOI18N

		btnAddField.setAction(actionMap.get("addFieldAction")); // NOI18N
		btnAddField.setName("btnAddField"); // NOI18N

		fieldsScrollPane.setName("fieldsScrollPane"); // NOI18N

		fieldListView.setName("fieldListView"); // NOI18N

		org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty
				.create("${fieldList}");
		org.jdesktop.swingbinding.JListBinding jListBinding = org.jdesktop.swingbinding.SwingBindings
				.createJListBinding(
						org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
						this, eLProperty, fieldListView);
		bindingGroup.addBinding(jListBinding);
		org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings
				.createAutoBinding(
						org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
						this, org.jdesktop.beansbinding.ELProperty
								.create("${selectedFields}"), fieldListView,
						org.jdesktop.beansbinding.BeanProperty
								.create("selectedElements"));
		bindingGroup.addBinding(binding);

		fieldListView
				.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							javax.swing.event.ListSelectionEvent evt) {
						fieldListViewValueChanged(evt);
					}
				});
		fieldsScrollPane.setViewportView(fieldListView);

		btnRemoveField.setAction(actionMap.get("removeFieldAction")); // NOI18N
		btnRemoveField.setName("btnRemoveField"); // NOI18N

		jButton1.setAction(actionMap.get("genFieldNamesAction")); // NOI18N
		jButton1.setName("jButton1"); // NOI18N

		javax.swing.GroupLayout panelFieldsLayout = new javax.swing.GroupLayout(
				panelFields);
		panelFields.setLayout(panelFieldsLayout);
		panelFieldsLayout
				.setHorizontalGroup(panelFieldsLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								panelFieldsLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												panelFieldsLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																fieldsScrollPane,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																251,
																Short.MAX_VALUE)
														.addGroup(
																panelFieldsLayout
																		.createSequentialGroup()
																		.addComponent(
																				lblFieldName)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				txtFieldName,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				104,
																				Short.MAX_VALUE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				btnAddField))
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																panelFieldsLayout
																		.createSequentialGroup()
																		.addComponent(
																				jButton1)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				btnRemoveField)))
										.addContainerGap()));
		panelFieldsLayout
				.setVerticalGroup(panelFieldsLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								panelFieldsLayout
										.createSequentialGroup()
										.addGroup(
												panelFieldsLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																lblFieldName)
														.addComponent(
																txtFieldName,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																btnAddField))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												fieldsScrollPane,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												99, Short.MAX_VALUE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												panelFieldsLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																btnRemoveField)
														.addComponent(jButton1))));

		btnConfirm.setAction(actionMap.get("createTournament")); // NOI18N
		btnConfirm.setText(resourceMap.getString("btnConfirm.text")); // NOI18N
		btnConfirm.setName("btnConfirm"); // NOI18N

		btnCancel.setAction(actionMap.get("cancelAction")); // NOI18N
		btnCancel.setName("btnCancel"); // NOI18N

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														messagePanel,
														javax.swing.GroupLayout.Alignment.TRAILING,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														738, Short.MAX_VALUE)
												.addComponent(lblHeader)
												.addGroup(
														layout.createSequentialGroup()
																.addGap(506,
																		506,
																		506)
																.addComponent(
																		btnConfirm)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		btnCancel))
												.addGroup(
														layout.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																false)
																.addComponent(
																		panelPartCond,
																		javax.swing.GroupLayout.Alignment.LEADING,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)
																.addGroup(
																		javax.swing.GroupLayout.Alignment.LEADING,
																		layout.createSequentialGroup()
																				.addGroup(
																						layout.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																								.addComponent(
																										panelFields,
																										javax.swing.GroupLayout.DEFAULT_SIZE,
																										javax.swing.GroupLayout.DEFAULT_SIZE,
																										Short.MAX_VALUE)
																								.addComponent(
																										panelTournament,
																										javax.swing.GroupLayout.PREFERRED_SIZE,
																										javax.swing.GroupLayout.DEFAULT_SIZE,
																										javax.swing.GroupLayout.PREFERRED_SIZE))
																				.addPreferredGap(
																						javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(
																						panelObligatory,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						javax.swing.GroupLayout.PREFERRED_SIZE))))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(lblHeader)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(messagePanel,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										0, Short.MAX_VALUE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.TRAILING)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		panelTournament,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		panelFields,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.PREFERRED_SIZE))
												.addComponent(
														panelObligatory,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(panelPartCond,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(btnCancel)
												.addComponent(btnConfirm))
								.addContainerGap(25, Short.MAX_VALUE)));

		bindingGroup.bind();
	}// </editor-fold>//GEN-END:initComponents

	/** (De)Aktiviert hinzufügen-Button. */
	private void txtFieldNameKeyTyped(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_txtFieldNameKeyTyped
		setAddFieldActionEnabled(!txtFieldName.getText().trim().isEmpty());
	}// GEN-LAST:event_txtFieldNameKeyTyped

	private void txtFieldNamePropertyChange(java.beans.PropertyChangeEvent evt) {// GEN-FIRST:event_txtFieldNamePropertyChange

	}// GEN-LAST:event_txtFieldNamePropertyChange

	/** (De)Aktiviert entfernen-Button. */
	private void fieldListViewValueChanged(
			javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_fieldListViewValueChanged
		setRemoveFieldActionEnabled(!fieldListView.isSelectionEmpty());
	}// GEN-LAST:event_fieldListViewValueChanged

	/**
	 * FocusLost-Event von allen obligatorischen Feldern zum Validieren.
	 *
	 * @param evt
	 *            Event
	 */
	// GEN-FIRST:event_focusLost
	private void focusLost(java.awt.event.FocusEvent evt) {
		setTournamentCreateEnabled(validateForm());
	}// GEN-LAST:event_focusLost

	/**
	 * Zum Validieren von DatePickern, die eine besondere Behandlung benötigen.
	 *
	 * @param evt
	 *            Event
	 */
	// GEN-FIRST:event_datePickerPropChanged
	private void datePickerPropChanged(java.beans.PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("date")) {
			setTournamentCreateEnabled(validateForm());
		}
	}// GEN-LAST:event_datePickerPropChanged

	/** Fügt den eingegebenen Feldnamen in die Liste ein. */
	@Action(enabledProperty = "addFieldActionEnabled")
	public void addFieldAction() {
		fieldList.add(txtFieldName.getText().trim());
		Collections.sort(fieldList);

		setTournamentCreateEnabled(validateForm());
	}

	/** Entfernt die aktuelle Auswahl aus der Feldliste. */
	@Action(enabledProperty = "removeFieldActionEnabled")
	public void removeFieldAction() {
		fieldList.removeAll(selectedFields);

		setTournamentCreateEnabled(validateForm());
	}

	//
	// Helfermethoden
	//
	private boolean removeFieldActionEnabled = false;

	/**
	 * Ob ein Feld entfernt wird
	 * 
	 * @return Status ob ein Feld entfernt wird
	 */
	public boolean isRemoveFieldActionEnabled() {
		return removeFieldActionEnabled;
	}

	/**
	 * Setzt ob ein Feld gelöscht wird
	 * 
	 * @param b
	 *            Status on ein Feld gelöscht wird
	 */
	public void setRemoveFieldActionEnabled(boolean b) {
		boolean old = isRemoveFieldActionEnabled();
		this.removeFieldActionEnabled = b;
		firePropertyChange("removeFieldActionEnabled", old,
				isRemoveFieldActionEnabled());
	}

	private boolean addFieldActionEnabled = false;

	/**
	 * Gibt den Status wieder ob ein Feld hinzugefügt werden soll
	 *
	 * @return Status ob ein Feld hinzugefügt wird
	 */
	public boolean isAddFieldActionEnabled() {
		return addFieldActionEnabled;
	}

	/**
	 * Setzt den Status ob ein Feld hinzugefügt werden soll
	 * 
	 * @param b
	 *            Status
	 */
	public void setAddFieldActionEnabled(boolean b) {
		boolean old = isAddFieldActionEnabled();
		this.addFieldActionEnabled = b;
		firePropertyChange("addFieldActionEnabled", old,
				isAddFieldActionEnabled());
	}

	/**
	 * Generiert so viele Feldnamen wieviele der Benutzer zu generieren eingibt.
	 */
	@Action
	public void genFieldNamesAction() {
		String input = JOptionPane.showInputDialog(MainApplication
				.getApplication().getMainView().getFrame(),
				"Geben Sie bitte die Anzahl der zu generierenden Feldernamen. Diese "
						+ "werden anschließend in die Liste hinzugefügt.", 5);
		try {
			int n = Integer.valueOf(input);

			char A = 'A';
			for (int i = 0; i < n; i++) {
				fieldList.add("Feld_" + A);
				A++;
			}
		} catch (NumberFormatException ignore) {
			JOptionPane.showMessageDialog(MainApplication.getApplication()
					.getMainView().getFrame(), "Falsche Eingabe!");
		}

		setTournamentCreateEnabled(validateForm());
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnAddField;
	private javax.swing.JButton btnCancel;
	private javax.swing.JButton btnConfirm;
	private javax.swing.JButton btnRemoveField;
	private javax.swing.JComboBox cmbType;
	private com.michaelbaranov.microba.calendar.DatePicker expireDatePicker;
	private javax.swing.JList fieldListView;
	private javax.swing.JScrollPane fieldsScrollPane;
	private javax.swing.JButton jButton1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JLabel lblExpireDate;
	private javax.swing.JLabel lblFee;
	private javax.swing.JLabel lblFieldName;
	private javax.swing.JLabel lblHeader;
	private javax.swing.JLabel lblLabel;
	private javax.swing.JLabel lblLateRegFee;
	private javax.swing.JLabel lblLocation;
	private javax.swing.JLabel lblMatchDuration;
	private javax.swing.JLabel lblMaxPart;
	private javax.swing.JLabel lblMaxPlayerProTeam;
	private javax.swing.JLabel lblSportKind;
	private javax.swing.JLabel lblStartDate;
	private javax.swing.JLabel lblType;
	private view.tools.MessagePanel messagePanel;
	private javax.swing.JPanel panelFields;
	private javax.swing.JPanel panelObligatory;
	private javax.swing.JPanel panelPartCond;
	private javax.swing.JPanel panelTournament;
	private javax.swing.JSpinner spnFee;
	private javax.swing.JSpinner spnLateRegFee;
	private javax.swing.JSpinner spnMaxPart;
	private javax.swing.JSpinner spnMaxPlayers;
	private javax.swing.JSpinner spnStdGameDuration;
	private com.michaelbaranov.microba.calendar.DatePicker startDatePicker;
	private javax.swing.JTextField txtFieldName;
	private javax.swing.JTextField txtLabel;
	private javax.swing.JTextField txtLocation;
	private javax.swing.JTextPane txtPartCond;
	private javax.swing.JTextField txtSportKind;
	private org.jdesktop.beansbinding.BindingGroup bindingGroup;
	// End of variables declaration//GEN-END:variables
}
