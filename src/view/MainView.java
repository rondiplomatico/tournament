package view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;
import javax.swing.WindowConstants;

import model.Team;
import model.Tournament;
import model.User;
import model.enums.TournamentState;
import model.enums.TournamentType;

import org.jdesktop.application.Action;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.observablecollections.ObservableCollections;

import planning.view.TournamentPlanView;
import printing.HTMLGenerator;
import tools.PDFFilter;
import tools.TournamentPDF;
import view.forms.ChangePasswortDialog;
import view.forms.ChooseLeadersForm;
import view.forms.ConfirmSignUpForm;
import view.forms.CreateTournamentPlanForm;
import view.forms.DisqualifyPanel;
import view.forms.LateSignUpForm;
import view.forms.SignUpPanel;
import view.forms.TournamentForm;
import view.tools.ToolBarStates;
import control.MainApplication;
import control.ProgressLogger;
import control.TournamentManager;
import control.UserManager;

/**
 * Hauptansicht der Anwendung
 * 
 * @author Maike Dudek, Paul Lubos
 */
public class MainView extends FrameView {

	private static final int FILTER_ALL = 0;
	private static final int FILTER_MY = 1;
	private static final int FILTER_PUBLISHED = 2;
	private static final int FILTER_RUNNING = 3;
	private static final int FILTER_FINISHED = 4;
	/**
	 * Liste der Turniere
	 */
	protected List<Tournament> tournamentList;
	private Tournament selectedTournament;

	/**
	 * 
	 * Erzeugt die Hauptansicht von Tournament
	 * 
	 * @param app
	 *            SingleFrameApplication zur Erweiterung der JFrame
	 *            Eigenschaften
	 */
	public MainView(SingleFrameApplication app) {
		super(app);

		tournamentList = ObservableCollections
				.observableList(new ArrayList<Tournament>(TournamentManager
						.getInstance().getAllTournaments()));

		initComponents();

		initExtraHTMLButtons();

		//
		// Listener zum korrekten Schließen der Anwendung.
		//

		getFrame().setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		getFrame().addWindowListener(new java.awt.event.WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				MainApplication.getApplication().quit(
						new ActionEvent(this, 0, "Window Close (X)"));
			}
		});

		// Logging-List setzen
		ProgressLogger.getInstance().setTextPane(progressTextPane);
	}

	private void initExtraHTMLButtons() {
		javax.swing.ActionMap actionMap = org.jdesktop.application.Application
				.getInstance(control.MainApplication.class).getContext()
				.getActionMap(MainView.class, this);

		btnprintgroup = new javax.swing.JButton();
		btnprintgroup.setAction(actionMap.get("printGroup")); // NOI18N
		btnprintgroup.setName("printGroup"); // NOI18N
		tbMain.add(btnprintgroup);

		btnprintPlan = new javax.swing.JButton();
		btnprintPlan.setAction(actionMap.get("printPlan")); // NOI18N
		btnprintPlan.setName("printPlan"); // NOI18N
		tbMain.add(btnprintPlan);

		btnprintfinal = new javax.swing.JButton();
		btnprintfinal.setAction(actionMap.get("printFinal")); // NOI18N
		btnprintfinal.setName("printFinal"); // NOI18N
		tbMain.add(btnprintfinal);
	}

	/**
	 * 
	 * Gibt die Liste der Turniere zurück
	 * 
	 * @return Liste aller Turniere
	 */
	public List<Tournament> getTournamentList() {
		return tournamentList;
	}

	/**
	 * Get the value of selectedTournament
	 * 
	 * @return the value of selectedTournament
	 */
	public Tournament getSelectedTournament() {
		return selectedTournament;
	}

	/**
	 * Set the value of selectedTournament
	 * 
	 * @param selectedTournament
	 *            new value of selectedTournament
	 */
	public void setSelectedTournament(Tournament selectedTournament) {
		this.selectedTournament = selectedTournament;
	}

	/**
	 * Setzt Inhaltsbereich von der MainView.
	 * 
	 * @param panel
	 *            Panel, welches angezeigt werden soll.
	 */
	public void setContentPanel(ContentPanel panel) {
		contentScrollPane.setViewportView(panel);
	}

	private JFileChooser getPDFFileChooser() {
		if (pdfFileChooser == null) {
			pdfFileChooser = new JFileChooser();
			pdfFileChooser.addChoosableFileFilter(new PDFFilter());
			pdfFileChooser.setAcceptAllFileFilterUsed(false);
			pdfFileChooser.setMultiSelectionEnabled(false);
			pdfFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}
		return pdfFileChooser;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

		menuBar = new javax.swing.JMenuBar();
		JMenu fileMenu = new javax.swing.JMenu();
		jMenuItem1 = new javax.swing.JMenuItem();
		mnuLogout = new javax.swing.JMenuItem();
		javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
		menuAthlete = new javax.swing.JMenu();
		miSignUp = new javax.swing.JMenuItem();
		miSignOff = new javax.swing.JMenuItem();
		miTournamentPlan = new javax.swing.JMenuItem();
		menuReferee = new javax.swing.JMenu();
		miEnterResults = new javax.swing.JMenuItem();
		menuLeader = new javax.swing.JMenu();
		miLateSignUp = new javax.swing.JMenuItem();
		miConfirmSignUp = new javax.swing.JMenuItem();
		miChooseReferee = new javax.swing.JMenuItem();
		miCreateTournamentPlan = new javax.swing.JMenuItem();
		miDisqualify = new javax.swing.JMenuItem();
		miFinishTournament = new javax.swing.JMenuItem();
		miCreateResultlist = new javax.swing.JMenuItem();
		miCreateDocuments = new javax.swing.JMenuItem();
		menuManager = new javax.swing.JMenu();
		miCreateTournament = new javax.swing.JMenuItem();
		miPublishTournament = new javax.swing.JMenuItem();
		miChooseLeader = new javax.swing.JMenuItem();
		miFinishSignUpTime = new javax.swing.JMenuItem();
		miCancelTournament = new javax.swing.JMenuItem();
		javax.swing.JMenu helpMenu = new javax.swing.JMenu();
		javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
		statusPanel = new javax.swing.JPanel();
		javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
		lblLoggedInUser = new javax.swing.JLabel();
		lblDate = new javax.swing.JLabel();
		tbMain = new javax.swing.JToolBar();
		tbAthlete = new javax.swing.JToolBar();
		btnSignUp = new javax.swing.JButton();
		btnSignOff = new javax.swing.JButton();
		btnTournamentPlan = new javax.swing.JButton();
		tbReferee = new javax.swing.JToolBar();
		btnEnterResults = new javax.swing.JButton();
		tbLeader = new javax.swing.JToolBar();
		btnConfirmSignUp = new javax.swing.JButton();
		btnLateSignUp = new javax.swing.JButton();
		btnChooseReferee = new javax.swing.JButton();
		btnCreateTournamentPlan = new javax.swing.JButton();
		btnDisqualify = new javax.swing.JButton();
		btnFinishTournament = new javax.swing.JButton();
		btnCreateResultlist = new javax.swing.JButton();
		btnCreateDocuments = new javax.swing.JButton();
		tbManager = new javax.swing.JToolBar();
		btnCreateTournament = new javax.swing.JButton();
		btnPublishTournament = new javax.swing.JButton();
		btnChooseLeader = new javax.swing.JButton();
		btnFinishSignUpTime = new javax.swing.JButton();
		btnCancelTournament = new javax.swing.JButton();
		tbUser = new javax.swing.JToolBar();
		btnLogout = new javax.swing.JButton();
		btnQuit = new javax.swing.JButton();
		mainSplitPane = new javax.swing.JSplitPane();
		splitPane = new javax.swing.JSplitPane();
		tournamentListPanel = new javax.swing.JPanel();
		cboxFilter = new javax.swing.JComboBox();
		tournamentListscrollPane = new javax.swing.JScrollPane();
		tournamentListView = new javax.swing.JList();
		lblFilter = new javax.swing.JLabel();
		lblTournaments = new javax.swing.JLabel();
		contentScrollPane = new javax.swing.JScrollPane();
		mainPanel = new javax.swing.JPanel();
		progressScrollBar = new javax.swing.JScrollPane();
		progressTextPane = new javax.swing.JTextPane();

		menuBar.setName("menuBar"); // NOI18N

		org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
				.getInstance(control.MainApplication.class).getContext()
				.getResourceMap(MainView.class);
		fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
		fileMenu.setName("fileMenu"); // NOI18N

		javax.swing.ActionMap actionMap = org.jdesktop.application.Application
				.getInstance(control.MainApplication.class).getContext()
				.getActionMap(MainView.class, this);
		jMenuItem1.setAction(actionMap.get("showChangePassDialog")); // NOI18N
		jMenuItem1.setName("jMenuItem1"); // NOI18N
		fileMenu.add(jMenuItem1);

		mnuLogout.setAction(actionMap.get("logoutAction")); // NOI18N
		mnuLogout.setName("mnuLogout"); // NOI18N
		fileMenu.add(mnuLogout);

		exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
		exitMenuItem.setName("exitMenuItem"); // NOI18N
		fileMenu.add(exitMenuItem);

		menuBar.add(fileMenu);

		menuAthlete.setText(resourceMap.getString("menuAthlete.text")); // NOI18N
		menuAthlete.setName("menuAthlete"); // NOI18N

		miSignUp.setAction(actionMap.get("signUpAction")); // NOI18N
		miSignUp.setName("miSignUp"); // NOI18N
		menuAthlete.add(miSignUp);

		miSignOff.setAction(actionMap.get("signOffAction")); // NOI18N
		miSignOff.setName("miSignOff"); // NOI18N
		menuAthlete.add(miSignOff);

		miTournamentPlan.setAction(actionMap.get("showTournamentPlanAction")); // NOI18N
		miTournamentPlan
				.setText(resourceMap.getString("miTournamentPlan.text")); // NOI18N
		miTournamentPlan.setName("miTournamentPlan"); // NOI18N
		menuAthlete.add(miTournamentPlan);

		menuBar.add(menuAthlete);

		menuReferee.setText(resourceMap.getString("menuReferee.text")); // NOI18N
		menuReferee.setName("menuReferee"); // NOI18N

		miEnterResults.setAction(actionMap.get("enterResultsAction")); // NOI18N
		miEnterResults.setText(resourceMap.getString("miEnterResults.text")); // NOI18N
		miEnterResults.setName("miEnterResults"); // NOI18N
		menuReferee.add(miEnterResults);

		menuBar.add(menuReferee);

		menuLeader.setText(resourceMap.getString("menuLeader.text")); // NOI18N
		menuLeader.setName("menuLeader"); // NOI18N

		miLateSignUp.setAction(actionMap.get("lateSignUpAction")); // NOI18N
		miLateSignUp.setText(resourceMap.getString("miLateSignUp.text")); // NOI18N
		miLateSignUp.setName("miLateSignUp"); // NOI18N
		menuLeader.add(miLateSignUp);

		miConfirmSignUp.setAction(actionMap.get("confirmSignUpAction")); // NOI18N
		miConfirmSignUp.setText(resourceMap.getString("miConfirmSignUp.text")); // NOI18N
		miConfirmSignUp.setName("miConfirmSignUp"); // NOI18N
		menuLeader.add(miConfirmSignUp);

		miChooseReferee.setAction(actionMap.get("chooseRefereeAction")); // NOI18N
		miChooseReferee.setText(resourceMap.getString("miChooseReferee.text")); // NOI18N
		miChooseReferee.setName("miChooseReferee"); // NOI18N
		menuLeader.add(miChooseReferee);

		miCreateTournamentPlan.setAction(actionMap
				.get("createTournamentPlanAction")); // NOI18N
		miCreateTournamentPlan.setName("miCreateTournamentPlan"); // NOI18N
		menuLeader.add(miCreateTournamentPlan);

		miDisqualify.setAction(actionMap.get("disqualifyAction")); // NOI18N
		miDisqualify.setName("miDisqualify"); // NOI18N
		menuLeader.add(miDisqualify);

		miFinishTournament.setAction(actionMap.get("finishTournamentAction")); // NOI18N
		miFinishTournament.setText(resourceMap
				.getString("miFinishTournament.text")); // NOI18N
		miFinishTournament.setName("miFinishTournament"); // NOI18N
		menuLeader.add(miFinishTournament);

		miCreateResultlist.setAction(actionMap.get("createResultlistAction")); // NOI18N
		miCreateResultlist.setText(resourceMap
				.getString("miCreateResultlist.text")); // NOI18N
		miCreateResultlist.setName("miCreateResultlist"); // NOI18N
		menuLeader.add(miCreateResultlist);

		miCreateDocuments.setAction(actionMap.get("createDocumentsAction")); // NOI18N
		miCreateDocuments.setText(resourceMap
				.getString("miCreateDocuments.text")); // NOI18N
		miCreateDocuments.setName("miCreateDocuments"); // NOI18N
		menuLeader.add(miCreateDocuments);

		menuBar.add(menuLeader);

		menuManager.setText(resourceMap.getString("menuManager.text")); // NOI18N
		menuManager.setName("menuManager"); // NOI18N

		miCreateTournament.setAction(actionMap.get("showTournamentCreateForm")); // NOI18N
		miCreateTournament.setName("miCreateTournament"); // NOI18N
		menuManager.add(miCreateTournament);

		miPublishTournament.setAction(actionMap.get("publishTournamentAction")); // NOI18N
		miPublishTournament.setText(resourceMap
				.getString("miPublishTournament.text")); // NOI18N
		miPublishTournament.setName("miPublishTournament"); // NOI18N
		menuManager.add(miPublishTournament);

		miChooseLeader.setAction(actionMap.get("chooseLeaderAction")); // NOI18N
		miChooseLeader.setText(resourceMap.getString("miChooseLeader.text")); // NOI18N
		miChooseLeader.setName("miChooseLeader"); // NOI18N
		menuManager.add(miChooseLeader);

		miFinishSignUpTime.setAction(actionMap.get("finishSignUpTimeAction")); // NOI18N
		miFinishSignUpTime.setText(resourceMap
				.getString("miFinishSignUpTime.text")); // NOI18N
		miFinishSignUpTime.setName("miFinishSignUpTime"); // NOI18N
		menuManager.add(miFinishSignUpTime);

		miCancelTournament.setAction(actionMap.get("cancelTournamentAction")); // NOI18N
		miCancelTournament.setName("miCancelTournament"); // NOI18N
		menuManager.add(miCancelTournament);

		menuBar.add(menuManager);

		helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
		helpMenu.setName("helpMenu"); // NOI18N

		aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
		aboutMenuItem.setName("aboutMenuItem"); // NOI18N
		helpMenu.add(aboutMenuItem);

		menuBar.add(helpMenu);

		statusPanel.setMinimumSize(new java.awt.Dimension(0, 0));
		statusPanel.setName("statusPanel"); // NOI18N

		statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

		lblLoggedInUser.setName("lblLoggedInUser"); // NOI18N

		lblDate.setText(resourceMap.getString("lblDate.text")); // NOI18N
		lblDate.setName("lblDate"); // NOI18N

		javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(
				statusPanel);
		statusPanel.setLayout(statusPanelLayout);
		statusPanelLayout
				.setHorizontalGroup(statusPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(statusPanelSeparator,
								javax.swing.GroupLayout.DEFAULT_SIZE, 800,
								Short.MAX_VALUE)
						.addGroup(
								statusPanelLayout
										.createSequentialGroup()
										.addGap(2, 2, 2)
										.addComponent(lblLoggedInUser)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												779, Short.MAX_VALUE)
										.addComponent(lblDate).addGap(3, 3, 3)));
		statusPanelLayout
				.setVerticalGroup(statusPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								statusPanelLayout
										.createSequentialGroup()
										.addComponent(
												statusPanelSeparator,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												2,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGroup(
												statusPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																statusPanelLayout
																		.createSequentialGroup()
																		.addGap(2,
																				2,
																				2)
																		.addComponent(
																				lblLoggedInUser))
														.addGroup(
																statusPanelLayout
																		.createSequentialGroup()
																		.addGap(1,
																				1,
																				1)
																		.addComponent(
																				lblDate)))
										.addGap(11, 11, 11)));

		tbMain.setBorder(null);
		tbMain.setRollover(true);
		tbMain.setName("tbMain"); // NOI18N

		tbAthlete.setRollover(true);
		tbAthlete.setName("tbAthlete"); // NOI18N

		btnSignUp.setAction(actionMap.get("signUpAction")); // NOI18N
		btnSignUp.setFocusable(false);
		btnSignUp.setHideActionText(true);
		btnSignUp.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnSignUp.setName("btnSignUp"); // NOI18N
		btnSignUp.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbAthlete.add(btnSignUp);

		btnSignOff.setAction(actionMap.get("signOffAction")); // NOI18N
		btnSignOff.setFocusable(false);
		btnSignOff.setHideActionText(true);
		btnSignOff.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnSignOff.setName("btnSignOff"); // NOI18N
		btnSignOff.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbAthlete.add(btnSignOff);

		btnTournamentPlan.setAction(actionMap.get("showTournamentPlanAction")); // NOI18N
		btnTournamentPlan.setFocusable(false);
		btnTournamentPlan.setHideActionText(true);
		btnTournamentPlan
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnTournamentPlan.setName("btnTournamentPlan"); // NOI18N
		btnTournamentPlan
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbAthlete.add(btnTournamentPlan);

		tbMain.add(tbAthlete);

		tbReferee.setRollover(true);
		tbReferee.setName("tbReferee"); // NOI18N

		btnEnterResults.setAction(actionMap.get("enterResultsAction")); // NOI18N
		btnEnterResults.setFocusable(false);
		btnEnterResults.setHideActionText(true);
		btnEnterResults
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnEnterResults.setName("btnEnterResults"); // NOI18N
		btnEnterResults
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbReferee.add(btnEnterResults);

		tbMain.add(tbReferee);

		tbLeader.setRollover(true);
		tbLeader.setName("tbLeader"); // NOI18N

		btnConfirmSignUp.setAction(actionMap.get("confirmSignUpAction")); // NOI18N
		btnConfirmSignUp.setFocusable(false);
		btnConfirmSignUp.setHideActionText(true);
		btnConfirmSignUp
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnConfirmSignUp.setName("btnConfirmSignUp"); // NOI18N
		btnConfirmSignUp
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbLeader.add(btnConfirmSignUp);

		btnLateSignUp.setAction(actionMap.get("lateSignUpAction")); // NOI18N
		btnLateSignUp.setFocusable(false);
		btnLateSignUp.setHideActionText(true);
		btnLateSignUp
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnLateSignUp.setName("btnLateSignUp"); // NOI18N
		btnLateSignUp
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbLeader.add(btnLateSignUp);

		btnChooseReferee.setAction(actionMap.get("chooseRefereeAction")); // NOI18N
		btnChooseReferee.setFocusable(false);
		btnChooseReferee.setHideActionText(true);
		btnChooseReferee
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnChooseReferee.setName("btnChooseReferee"); // NOI18N
		btnChooseReferee
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbLeader.add(btnChooseReferee);

		btnCreateTournamentPlan.setAction(actionMap
				.get("createTournamentPlanAction")); // NOI18N
		btnCreateTournamentPlan.setFocusable(false);
		btnCreateTournamentPlan.setHideActionText(true);
		btnCreateTournamentPlan
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnCreateTournamentPlan.setName("btnCreateTournamentPlan"); // NOI18N
		btnCreateTournamentPlan
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbLeader.add(btnCreateTournamentPlan);

		btnDisqualify.setAction(actionMap.get("disqualifyAction")); // NOI18N
		btnDisqualify.setFocusable(false);
		btnDisqualify.setHideActionText(true);
		btnDisqualify
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnDisqualify.setName("btnDisqualify"); // NOI18N
		btnDisqualify
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbLeader.add(btnDisqualify);

		btnFinishTournament.setAction(actionMap.get("finishTournamentAction")); // NOI18N
		btnFinishTournament.setFocusable(false);
		btnFinishTournament.setHideActionText(true);
		btnFinishTournament
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnFinishTournament.setName("btnFinishTournament"); // NOI18N
		btnFinishTournament
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbLeader.add(btnFinishTournament);

		btnCreateResultlist.setAction(actionMap.get("createResultlistAction")); // NOI18N
		btnCreateResultlist.setFocusable(false);
		btnCreateResultlist.setHideActionText(true);
		btnCreateResultlist
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnCreateResultlist.setName("btnCreateResultlist"); // NOI18N
		btnCreateResultlist
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbLeader.add(btnCreateResultlist);

		btnCreateDocuments.setAction(actionMap.get("createDocumentsAction")); // NOI18N
		btnCreateDocuments.setFocusable(false);
		btnCreateDocuments.setHideActionText(true);
		btnCreateDocuments
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnCreateDocuments.setName("btnCreateDocuments"); // NOI18N
		btnCreateDocuments
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbLeader.add(btnCreateDocuments);

		tbMain.add(tbLeader);

		tbManager.setRollover(true);
		tbManager.setName("tbManager"); // NOI18N

		btnCreateTournament
				.setAction(actionMap.get("showTournamentCreateForm")); // NOI18N
		btnCreateTournament.setFocusable(false);
		btnCreateTournament.setHideActionText(true);
		btnCreateTournament
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnCreateTournament.setName("btnCreateTournament"); // NOI18N
		btnCreateTournament
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbManager.add(btnCreateTournament);

		btnPublishTournament
				.setAction(actionMap.get("publishTournamentAction")); // NOI18N
		btnPublishTournament.setFocusable(false);
		btnPublishTournament.setHideActionText(true);
		btnPublishTournament
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnPublishTournament.setName("btnPublishTournament"); // NOI18N
		btnPublishTournament
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbManager.add(btnPublishTournament);

		btnChooseLeader.setAction(actionMap.get("chooseLeaderAction")); // NOI18N
		btnChooseLeader.setFocusable(false);
		btnChooseLeader.setHideActionText(true);
		btnChooseLeader
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnChooseLeader.setName("btnChooseLeader"); // NOI18N
		btnChooseLeader
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbManager.add(btnChooseLeader);

		btnFinishSignUpTime.setAction(actionMap.get("finishSignUpTimeAction")); // NOI18N
		btnFinishSignUpTime.setFocusable(false);
		btnFinishSignUpTime.setHideActionText(true);
		btnFinishSignUpTime
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnFinishSignUpTime.setName("btnFinishSignUpTime"); // NOI18N
		btnFinishSignUpTime
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbManager.add(btnFinishSignUpTime);

		btnCancelTournament.setAction(actionMap.get("cancelTournamentAction")); // NOI18N
		btnCancelTournament.setFocusable(false);
		btnCancelTournament.setHideActionText(true);
		btnCancelTournament
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnCancelTournament.setName("btnCancelTournament"); // NOI18N
		btnCancelTournament
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbManager.add(btnCancelTournament);

		tbMain.add(tbManager);

		tbUser.setRollover(true);
		tbUser.setName("tbUser"); // NOI18N
		tbUser.setPreferredSize(new java.awt.Dimension(100, 33));

		btnLogout.setAction(actionMap.get("logoutAction")); // NOI18N
		btnLogout.setFocusable(false);
		btnLogout.setHideActionText(true);
		btnLogout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnLogout.setName("btnLogout"); // NOI18N
		btnLogout.setPreferredSize(new java.awt.Dimension(24, 24));
		btnLogout.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbUser.add(btnLogout);

		btnQuit.setAction(actionMap.get("quit")); // NOI18N
		btnQuit.setFocusable(false);
		btnQuit.setHideActionText(true);
		btnQuit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		btnQuit.setName("btnQuit"); // NOI18N
		btnQuit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		tbUser.add(btnQuit);

		tbMain.add(tbUser);

		mainSplitPane.setBorder(null);
		mainSplitPane.setDividerLocation(400);
		mainSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
		mainSplitPane.setResizeWeight(1.0);
		mainSplitPane.setName("mainSplitPane"); // NOI18N

		splitPane.setDividerLocation(200);
		splitPane.setName("splitPane"); // NOI18N
		splitPane.setPreferredSize(new java.awt.Dimension(1587, 624));

		tournamentListPanel.setName("tournamentListPanel"); // NOI18N

		cboxFilter.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"Alle", "Meine", "Aktuell ausgeschriebene", "Laufende",
				"Beendete" }));
		cboxFilter.setAction(actionMap.get("chooseFilter")); // NOI18N
		cboxFilter.setName("cboxFilter"); // NOI18N

		tournamentListscrollPane.setName("tournamentListscrollPane"); // NOI18N

		tournamentListView
				.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		tournamentListView.setName("tournamentListView"); // NOI18N

		org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty
				.create("${tournamentList}");
		org.jdesktop.swingbinding.JListBinding jListBinding = org.jdesktop.swingbinding.SwingBindings
				.createJListBinding(
						org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
						this, eLProperty, tournamentListView);
		jListBinding.setDetailBinding(org.jdesktop.beansbinding.ELProperty
				.create("${name}"));
		bindingGroup.addBinding(jListBinding);
		org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings
				.createAutoBinding(
						org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
						this, org.jdesktop.beansbinding.ELProperty
								.create("${selectedTournament}"),
						tournamentListView,
						org.jdesktop.beansbinding.BeanProperty
								.create("selectedElement"));
		bindingGroup.addBinding(binding);

		tournamentListView
				.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							javax.swing.event.ListSelectionEvent evt) {
						tournamentListViewValueChanged(evt);
					}
				});
		tournamentListscrollPane.setViewportView(tournamentListView);

		lblFilter.setName("lblFilter"); // NOI18N

		lblTournaments.setLabelFor(tournamentListView);
		lblTournaments.setText(resourceMap.getString("lblTournaments.text")); // NOI18N
		lblTournaments.setName("lblTournaments"); // NOI18N

		javax.swing.GroupLayout tournamentListPanelLayout = new javax.swing.GroupLayout(
				tournamentListPanel);
		tournamentListPanel.setLayout(tournamentListPanelLayout);
		tournamentListPanelLayout
				.setHorizontalGroup(tournamentListPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								tournamentListPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												tournamentListPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																tournamentListscrollPane,
																javax.swing.GroupLayout.Alignment.TRAILING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																179,
																Short.MAX_VALUE)
														.addComponent(lblFilter)
														.addComponent(
																lblTournaments)
														.addComponent(
																cboxFilter, 0,
																179,
																Short.MAX_VALUE))
										.addContainerGap()));
		tournamentListPanelLayout
				.setVerticalGroup(tournamentListPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								tournamentListPanelLayout
										.createSequentialGroup()
										.addComponent(lblFilter)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(lblTournaments)
										.addGap(3, 3, 3)
										.addComponent(
												cboxFilter,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												tournamentListscrollPane,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												338, Short.MAX_VALUE)
										.addContainerGap()));

		splitPane.setLeftComponent(tournamentListPanel);

		contentScrollPane.setName("contentScrollPane"); // NOI18N

		mainPanel.setName("mainPanel"); // NOI18N

		javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(
				mainPanel);
		mainPanel.setLayout(mainPanelLayout);
		mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 1362,
				Short.MAX_VALUE));
		mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 620,
				Short.MAX_VALUE));

		contentScrollPane.setViewportView(mainPanel);

		splitPane.setRightComponent(contentScrollPane);

		mainSplitPane.setLeftComponent(splitPane);

		progressScrollBar.setAutoscrolls(true);
		progressScrollBar.setName("progressScrollBar"); // NOI18N

		progressTextPane.setEditable(false);
		progressTextPane.setName("progressTextPane"); // NOI18N
		progressScrollBar.setViewportView(progressTextPane);

		mainSplitPane.setRightComponent(progressScrollBar);

		setComponent(mainSplitPane);
		setMenuBar(menuBar);
		setStatusBar(statusPanel);
		setToolBar(tbMain);

		bindingGroup.bind();
	}// </editor-fold>//GEN-END:initComponents

	/**
	 * Listener für die Turnierliste
	 * 
	 * @param evt
	 */
	private void tournamentListViewValueChanged(
			javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_tournamentListViewValueChanged
		tournamentSelected();
	}// GEN-LAST:event_tournamentListViewValueChanged

	/**
	 * Methode zum Updaten des MainViews. Hier werden die ToolBar(inklusive
	 * Buttons), die MenuItems und die Turnierinformationen (der TournamentView)
	 * entsprechend des aktuell ausgewählten Turniers und des Aktuell
	 * eingeloggten Users angepasst
	 * 
	 * @param t
	 *            Turnier
	 */
	private void tournamentSelected() {
		if (selectedTournament == null) {
			return;
		}

		updateMainToolBar();

		TournamentView tournamentinfo = new TournamentView(selectedTournament);
		setContentPanel(tournamentinfo);
	}

	/**
	 * Gibt true zurück, falls ein User in einem Turnier vorangemeldet ist
	 * 
	 * @param t
	 *            ausgewähltes Turnier
	 * @param u
	 *            User (aktuell eingeloggter User)
	 * @return true, falls der User im ausgewählten Turnier vorangemeldet ist
	 */
	public boolean isTournamentPlayer(Tournament t, User u) {
		for (Team team : t.getTeams()) {
			for (User user : team.getPlayers()) {
				if (user == u) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Methode zum Updaten der MainToolBar und der MenuBar. Hier werden die
	 * MainToolBar(also AthleteToolBar, RefereeToolBar, LeaderToolBar und
	 * ManagerToolBar inkl. der jeweiligen Buttons), und die MenuBar inkl der
	 * jeweiligen MenuItem entsprechend des aktuell ausgewählten Turniers und
	 * des aktuell eingeloggten Users angepasst
	 */
	public void updateMainToolBar() {
		updateAthleteToolBar();
		updateRefereeToolBar();
		updateLeaderToolBar();
		updateManagerToolBar();

	}

	/**
	 * Methode zum Updaten der AthleteToolBar und des menuAthlete inkl. der
	 * jeweiligen Buttons bzw. MenuItems entsprechend des aktuell ausgewählten
	 * Turniers und des aktuell eingeloggten Users angepasst.
	 */
	public void updateAthleteToolBar() {
		Tournament t = selectedTournament;
		User u = UserManager.getInstance().getLoggedinUser();

		tbAthlete.setVisible(true);
		menuAthlete.setVisible(true);

		if (t != null) {
			int s = t.getState().ordinal();

			if (!isTournamentPlayer(t, u)) {
				setSignUpActionEnabled(ToolBarStates.athleteStates[s][ToolBarStates.AthleteActions.signUp
						.ordinal()]);
				setSignOffActionEnabled(false);

			} else {
				setSignUpActionEnabled(false);
				setSignOffActionEnabled(ToolBarStates.athleteStates[s][ToolBarStates.AthleteActions.signOff
						.ordinal()]);
			}

			setTournamentPlanEnabled(ToolBarStates.athleteStates[s][ToolBarStates.AthleteActions.showTournamentPlan
					.ordinal()]);
		}

	}

	/**
	 * Methode zum Updaten der RefereeToolBar und des menuReferee inkl. der
	 * jeweiligen Buttons bzw. MenuItems entsprechend des aktuell ausgewählten
	 * Turniers und des aktuell eingeloggten Users angepasst.
	 */
	public void updateRefereeToolBar() {
		Tournament t = getSelectedTournament();
		User u = UserManager.getInstance().getLoggedinUser();

		setEnterResultsEnabled(false);

		if (u.isReferee() || u.isManager()) {
			tbReferee.setVisible(true);
			menuReferee.setVisible(true);
		} else {
			tbReferee.setVisible(false);
			menuReferee.setVisible(false);
		}

		if (t != null) {
			int s = t.getState().ordinal();

			boolean isLeader = UserManager.getInstance().isLeader(t);

			if (isLeader) {
				tbReferee.setVisible(true);
				menuReferee.setVisible(true);

			}

			if (isLeader || u.isManager()
					|| (u.isReferee() && t.getReferees().contains(u))) {
				setEnterResultsEnabled(ToolBarStates.refereeStates[s][ToolBarStates.RefereeActions.enterResults
						.ordinal()]);
			}

		}

	}

	/**
	 * Methode zum Updaten der LeaderToolBar und des menuLeader inkl. der
	 * jeweiligen Buttons bzw. MenuItems entsprechend des aktuell ausgewählten
	 * Turniers und des aktuell eingeloggten Users angepasst.
	 */
	public void updateLeaderToolBar() {
		Tournament t = getSelectedTournament();
		User u = UserManager.getInstance().getLoggedinUser();

		setConfirmSignUpEnabled(false);
		setLateSignUpEnabled(false);
		setChooseRefereeEnabled(false);
		setCreateTournamentPlanEnabled(false);
		setDisqualifyEnabled(false);
		setFinishTournamentEnabled(false);
		setCreateResultlistEnabled(false);
		setCreateDocumentsEnabled(false);

		if (u.isManager()) {
			tbLeader.setVisible(true);
			menuLeader.setVisible(true);
		} else {
			tbLeader.setVisible(false);
			menuLeader.setVisible(false);
		}

		if (t != null) {
			int s = t.getState().ordinal();

			boolean isLeader = UserManager.getInstance().isLeader(t);

			if (isLeader) {
				tbLeader.setVisible(true);
				menuLeader.setVisible(true);
			}

			if (isLeader || u.isManager()) {
				setConfirmSignUpEnabled(ToolBarStates.leaderStates[s][ToolBarStates.LeaderActions.confirmSignUp
						.ordinal()]);
				setLateSignUpEnabled(ToolBarStates.leaderStates[s][ToolBarStates.LeaderActions.lateSignUp
						.ordinal()]);
				setChooseRefereeEnabled(ToolBarStates.leaderStates[s][ToolBarStates.LeaderActions.chooseReferee
						.ordinal()]);
				setCreateTournamentPlanEnabled(ToolBarStates.leaderStates[s][ToolBarStates.LeaderActions.createTournamentPlan
						.ordinal()]);
				setDisqualifyEnabled(ToolBarStates.leaderStates[s][ToolBarStates.LeaderActions.disqualify
						.ordinal()]);
				setFinishTournamentEnabled(ToolBarStates.leaderStates[s][ToolBarStates.LeaderActions.finishTournament
						.ordinal()]);

				setCreateResultlistEnabled(ToolBarStates.leaderStates[s][ToolBarStates.LeaderActions.createResultlist
						.ordinal()]);

				setCreateDocumentsEnabled(ToolBarStates.leaderStates[s][ToolBarStates.LeaderActions.createDocuments
						.ordinal()]);
			}

		}

	}

	/**
	 * Methode zum Updaten der ManagerToolBar und des menuManager inkl. der
	 * jeweiligen Buttons bzw. MenuItems entsprechend des aktuell ausgewählten
	 * Turniers und des aktuell eingeloggten Users angepasst.
	 */
	public void updateManagerToolBar() {
		Tournament t = getSelectedTournament();
		User u = UserManager.getInstance().getLoggedinUser();

		setPublishTournamentEnabled(false);
		setChooseLeaderEnabled(false);
		setFinishSignUpTimeEnabled(false);
		setCancelTournamentEnabled(false);

		if (u.isManager()) {
			tbManager.setVisible(true);
			menuManager.setVisible(true);

			if (t != null) {
				int s = t.getState().ordinal();
				setPublishTournamentEnabled(ToolBarStates.managerStates[s][ToolBarStates.ManagerActions.publishTournament
						.ordinal()]);
				setChooseLeaderEnabled(ToolBarStates.managerStates[s][ToolBarStates.ManagerActions.chooseLeader
						.ordinal()]);
				setFinishSignUpTimeEnabled(ToolBarStates.managerStates[s][ToolBarStates.ManagerActions.finishSignUpTime
						.ordinal()]);
				setCancelTournamentEnabled(ToolBarStates.managerStates[s][ToolBarStates.ManagerActions.cancelTournament
						.ordinal()]);
			}

		} else {
			tbManager.setVisible(false);
			menuManager.setVisible(false);
		}
	}

	/**
	 * Methode zum Zurücksetzen des MainViews. Nach ausführen dieser Methode ist
	 * in der ToolBar nur noch Ausloggen aktiviert und es erscheinen nur die
	 * Menüs Datei und Hilfe.
	 */
	public void resetMainView() {
		assert (UserManager.getInstance().isLoggedin());

		// Willkommensseite anzeigens
		setContentPanel(new StartPanel());

		// Turnierliste
		tournamentList.clear();
		tournamentList.addAll(TournamentManager.getInstance()
				.getTournamentsByUser(
						UserManager.getInstance().getLoggedinUser()));
		cboxFilter.setSelectedIndex(FILTER_ALL);
		tournamentListView.setSelectedIndex(-1);

		// Toolbars aktualisieren
		updateMainToolBar();

		// Progress reseten
		ProgressLogger.getInstance().reset();

		// Statusbar aktualisieren
		User u = UserManager.getInstance().getLoggedinUser();
		StringBuffer buff = new StringBuffer("Eingeloggt als: ");
		buff.append(u.getUsername());
		if (u.isManager()) {
			buff.append(" in der Rolle eines Turnierverwalters");
		} else if (u.isReferee()) {
			buff.append(" in der Rolle eines Schiedsrichters");
		}
		lblLoggedInUser.setText(buff.toString());
		lblDate.setText("Aktuelle Uhrzeit: "
				+ SimpleDateFormat.getInstance().format(new Date()));
	}

	/**
	 * Zeigt AboutDialog mit Informationen über die Anwendung an.
	 */
	@Action
	public void showAboutBox() {
		if (aboutBox == null) {
			JFrame mainFrame = MainApplication.getApplication().getMainFrame();
			aboutBox = new AboutBox(mainFrame);
			aboutBox.setLocationRelativeTo(mainFrame);
		}

		MainApplication.getApplication().show(aboutBox);
	}

	/**
	 * Der aktuell eingeloggte Benutzer wird ausgeloggt und der LoginDialog wird
	 * angezeigt.
	 */
	@Action
	public void logoutAction() {
		UserManager.getInstance().logout();

		MainApplication app = MainApplication.getApplication();

		app.hide(this);
		app.showLoginDialog();
		resetMainView();
		app.show(this);
	}

	/**
	 * Method to initialize the components which were not made by the GUI editor
	 */
	public void initMyComponents() {
		tournamentListView.setCellRenderer(new ListCellRenderer() {

			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				return new JLabel(((Tournament) value).getName());
			}
		});

	}

	/** Zeigt das Turnier-Erstellen-Formular. */
	@Action
	public void showTournamentCreateForm() {
		TournamentForm form = new TournamentForm();
		form.addCallbackListener(new IContentCallback() {

			public void processFinished(boolean success) {
				// Erfolg
				if (success) {
					// Setze Turnier-Filter auf Alle, Aktion wird automatisch
					// aufgerufen, dabei wird auch die Turnierliste aktualisiert
					cboxFilter.setSelectedIndex(FILTER_ALL);

					ProgressLogger.getInstance().log(
							"Turnier wurde erfolgreich erstellt.");

					// Zu dem letzten Index runterscrollen und auswählen
					int last = tournamentList.size() - 1;
					tournamentListView.scrollRectToVisible(tournamentListView
							.getCellBounds(last - 1, last));
					tournamentListView.setSelectedIndex(last);

				} else { // Kein Erfolg
					setContentPanel(new StartPanel());
				}
			}
		});

		setContentPanel(form);
	}

	/**
	 * Action zum Voranmelden eines Benutzers für das ausgewählten Turnier Falls
	 * das zuvor ausgewählte Turnier ein SinglePlayer Turnier ist, wird
	 * lediglich eine Bestätigung ausgegeben. Ansonsten wird das
	 * Voranmelde-Formular aufgerufen.
	 */
	@Action(enabledProperty = "signUpActionEnabled")
	public void signUpAction() {
		final Tournament t = getSelectedTournament();
		User u = UserManager.getInstance().getLoggedinUser();

		if (t.getType().equals(TournamentType.SinglePlayer)) {
			// überprüfen ob anzahl der teilnehmer < anzahl der erlaubte
			// teilnehmer
			// dann user hinzufügen
			// sonst fehler
			if (t.getTeams().size() < t.getMaxParticipatingTeams()) {
				TournamentManager.getInstance().signup(t, u);

				// Bestätigung
				JOptionPane.showMessageDialog(getFrame(), getResourceMap()
						.getString("optionPane.signUp.msg", t.getName()));
				ProgressLogger.getInstance().log(
						"Erfolgreich zum Turnier " + t.getName()
								+ " angemeldet.");
				updateMainToolBar();
			} else {
				JOptionPane.showMessageDialog(getFrame(), getResourceMap()
						.getString("optionPane.signUpFail.msg", t.getName()));
				ProgressLogger
						.getInstance()
						.log("Error: Die maximale Anzahl der Teilnehmer wurde erreicht. Sie können sich nicht voranmelden");

			}
			// wenn multiplayer-turnier
		} else {
			SignUpPanel signup = new SignUpPanel(t);
			setContentPanel(signup);
			signup.addCallbackListener(new IContentCallback() {

				public void processFinished(boolean success) {
					if (success) {
						JOptionPane.showMessageDialog(
								getFrame(),
								getResourceMap().getString(
										"optionPane.signUp.msg", t.getName()));

						setContentPanel(new TournamentView(t));
						updateMainToolBar();
					} else {
						ProgressLogger.getInstance().log("Aktion abgebrochen.");
						setContentPanel(new TournamentView(t));
					}
				}
			});
		}
	}

	private boolean signUpActionEnabled = false;

	/**
	 * Überprüft, ob die Voranmelde-Action aktiviert ist (und somt alle Buttons,
	 * MenuItems etc. die mit der Voranmelde-Action verbunden sind)
	 * 
	 * @return true, falls die Voranmelde-Action aktiviert ist
	 */
	public boolean isSignUpActionEnabled() {
		return signUpActionEnabled;
	}

	/**
	 * Aktiviert (bei true) bzw. deaktiviert (bei false) die Voranmelde-Action
	 * (und somt alle Buttons, MenuItems etc. die mit der Voranmelde-Action
	 * verbunden sind).
	 * 
	 * @param b
	 *            boolean
	 */
	public void setSignUpActionEnabled(boolean b) {
		boolean old = isSignUpActionEnabled();
		this.signUpActionEnabled = b;
		firePropertyChange("signUpActionEnabled", old, isSignUpActionEnabled());
	}

	/**
	 * Action zum Abmelden eines Benutzers für das ausgewählten Turnier
	 */
	@Action(enabledProperty = "signOffActionEnabled")
	public void signOffAction() {
		Tournament t = getSelectedTournament();

		TournamentManager.getInstance().signoff(t,
				UserManager.getInstance().getLoggedinUser());

		JOptionPane.showMessageDialog(
				getFrame(),
				getResourceMap().getString("optionPane.signOff.msg",
						t.getName()));

		ProgressLogger.getInstance().log(
				"Du wurdest vom Turnier '" + t.getName() + "' abgemeldet.");
		setContentPanel(new TournamentView(t));
		updateMainToolBar();
	}

	private boolean signOffActionEnabled = false;

	/**
	 * Überprüft, ob die Abmelde-Action aktiviert ist (und somt alle Buttons,
	 * MenuItems etc. die mit der Abmelde-Action verbunden sind)
	 * 
	 * @return true, falls die Abmelde-Action aktiviert ist
	 */
	public boolean isSignOffActionEnabled() {
		return signOffActionEnabled;
	}

	/**
	 * Aktiviert (bei true) bzw. deaktiviert (bei false) die Abmelde-Action (und
	 * somt alle Buttons, MenuItems etc. die mit der Abmelde-Action verbunden
	 * sind).
	 * 
	 * @param b
	 *            boolean
	 */
	public void setSignOffActionEnabled(boolean b) {
		boolean old = isSignOffActionEnabled();
		this.signOffActionEnabled = b;
		firePropertyChange("signOffActionEnabled", old,
				isSignOffActionEnabled());
	}

	/**
	 * Action zum Anzeigen des aktuellen Turnierplans für das ausgewählten
	 * Turnier
	 */
	@Action(enabledProperty = "tournamentPlanEnabled")
	public void showTournamentPlanAction() {
		new TournamentPlanView(getSelectedTournament().getTournamentPlan(),
				null);
	}

	private boolean tournamentPlanEnabled = false;

	/**
	 * Überprüft, ob die Turnierplan-Anzeige-Action aktiviert ist (und somt alle
	 * Buttons, MenuItems etc. die mit der Turnierplan-Anzeige-Action verbunden
	 * sind)
	 * 
	 * @return true, falls die Turnierplan-Anzeige-Action aktiviert ist
	 */
	public boolean isTournamentPlanEnabled() {
		return tournamentPlanEnabled;
	}

	/**
	 * Aktiviert (bei true) bzw. deaktiviert (bei false) die
	 * Turnierplan-Anzeige-Action (und somt alle Buttons, MenuItems etc. die mit
	 * der Turnierplan-Anzeige-Action verbunden sind).
	 * 
	 * @param b
	 *            boolean
	 */
	public void setTournamentPlanEnabled(boolean b) {
		boolean old = isTournamentPlanEnabled();
		this.tournamentPlanEnabled = b;
		firePropertyChange("tournamentPlanEnabled", old,
				isTournamentPlanEnabled());
	}

	/**
	 * Action zum Ausschreiben eines Turniers
	 */
	@Action(enabledProperty = "publishTournamentEnabled")
	public void publishTournamentAction() {
		Tournament t = getSelectedTournament();

		ResourceMap rm = getResourceMap();

		// Testen, ob das Turnier schon ausgeschrieben wurde und nur die
		// PDF exportiert werden soll. Falls ja, dann Ende.
		if (t.getState() == TournamentState.published) {
			int retval = JOptionPane
					.showConfirmDialog(
							getFrame(),
							"Das Turniere ist bereits "
									+ "ausgeschrieben. Wollen Sie die Ausschreibungspdf nochmal "
									+ "exportieren?", "PDF exportieren",
							JOptionPane.YES_NO_OPTION);

			if (retval == JOptionPane.YES_OPTION) {
				// Dialog zeigen, falls nicht bestätigt, Ende.
				JFileChooser fileChooser = getPDFFileChooser();
				fileChooser.setName("Ausschreibung.pdf");
				if (fileChooser.showSaveDialog(getFrame()) != JFileChooser.APPROVE_OPTION) {
					return;
				}

				// .pdf dranhängen, falls nicht da.
				File file = fileChooser.getSelectedFile();
				if (!file.getName().toLowerCase().endsWith(".pdf")) {
					file = new File(file.getPath() + ".pdf");
				}

				// PDF erstellen
				try {
					TournamentPDF.createPublishPDF(t, file.getPath(),
							UserManager.getInstance().getLoggedinUser()
									.getName());
				} catch (Exception ignore) {
					JOptionPane
							.showMessageDialog(getFrame(),
									"Etwas ist beim Erstellen der PDF-Datei schiefgelaufen.");
					return;
				}

				// PDF anzeigen
				try {
					TournamentPDF.showDocument(file.getPath());
				} catch (IOException ignore) {
					JOptionPane
							.showMessageDialog(getFrame(),
									"Es ist keine Anwendung installiert, um PDF-Dateien zu betrachten.");
				}

				// Aufräumen
				fileChooser.setSelectedFile(null);
			}

			return;
		}

		//
		// Turnier auschreiben
		//

		// Bestätigen, ob man ausschreiben soll.
		int retval = JOptionPane.showConfirmDialog(getFrame(),
				rm.getString("optionPane.publishTournament.msg", t.getName()),
				rm.getString("optionPane.publishTournament.caption"),
				JOptionPane.YES_NO_OPTION);

		// Falls Bestätigung negativ, Ende.
		if (retval == JOptionPane.NO_OPTION) {
			return;
		}

		// Dialog zeigen, falls nicht bestätigt, Ende.
		JFileChooser fileChooser = getPDFFileChooser();
		fileChooser.setName("Ausschreibung.pdf");
		if (fileChooser.showSaveDialog(getFrame()) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		// .pdf dranhängen, falls nicht da.
		File file = fileChooser.getSelectedFile();
		if (!file.getName().toLowerCase().endsWith(".pdf")) {
			file = new File(file.getPath() + ".pdf");
		}

		// Publish
		TournamentManager.getInstance().publishTournament(t);

		// PDF erstellen
		try {
			TournamentPDF.createPublishPDF(t, file.getPath(), UserManager
					.getInstance().getLoggedinUser().getName());
		} catch (Exception ignore) {
			System.out.println(ignore.getClass().toString());

			JOptionPane.showMessageDialog(getFrame(),
					"Etwas ist beim Erstellen der PDF-Datei schiefgelaufen.");

			return;
		}

		// PDF anzeigen
		try {
			TournamentPDF.showDocument(file.getPath());
		} catch (IOException ignore) {
			JOptionPane.showMessageDialog(getFrame(),
					"Es ist kein PDF-Viewer zum Betrachten der generierten PDF-Datei "
							+ "installiert");
		}

		// Progress loggen
		ProgressLogger.getInstance().log(
				"Turnier '" + t.getName() + "' wurde ausgeschrieben.");

		// Aufräumen
		fileChooser.setSelectedFile(null);
		setContentPanel(new TournamentView(t));
		updateMainToolBar();
	}

	private boolean publishTournamentEnabled = false;

	/**
	 * Überprüft, ob die TurnierAusschreiben-Action aktiviert ist (und somt alle
	 * Buttons, MenuItems etc. die mit der TurnierAusschreiben-Action verbunden
	 * sind)
	 * 
	 * @return true, falls die TurnierAusschreiben-Action aktiviert ist
	 */
	public boolean isPublishTournamentEnabled() {
		return publishTournamentEnabled;
	}

	/**
	 * Aktiviert (bei true) bzw. deaktiviert (bei false) die
	 * TurnierAusschreiben-Action (und somt alle Buttons, MenuItems etc. die mit
	 * der TurnierAusschreiben-Action verbunden sind).
	 * 
	 * @param b
	 *            boolean
	 */
	public void setPublishTournamentEnabled(boolean b) {
		boolean old = isPublishTournamentEnabled();
		this.publishTournamentEnabled = b;
		firePropertyChange("publishTournamentEnabled", old,
				isPublishTournamentEnabled());
	}

	/**
	 * Action zum Eintragen von Ergebnissen für das ausgewählten Turnier
	 */
	@Action(enabledProperty = "enterResultsEnabled")
	public void enterResultsAction() {
		new TournamentPlanView(getSelectedTournament().getTournamentPlan(),
				UserManager.getInstance().getLoggedinUser());
	}

	private boolean enterResultsEnabled = false;

	/**
	 * Überprüft, ob die ErgebnisseEintragen-Action aktiviert ist (und somt alle
	 * Buttons, MenuItems etc. die mit der ErgebnisseEintragen-Action verbunden
	 * sind)
	 * 
	 * @return true, falls die ErgebnisseEintragen-Action aktiviert ist
	 */
	public boolean isEnterResultsEnabled() {
		return enterResultsEnabled;
	}

	/**
	 * Aktiviert (bei true) bzw. deaktiviert (bei false) die
	 * ErgebnisseEintragen-Action (und somt alle Buttons, MenuItems etc. die mit
	 * der ErgebnisseEintragen-Action verbunden sind).
	 * 
	 * @param b
	 *            boolean
	 */
	public void setEnterResultsEnabled(boolean b) {
		boolean old = isEnterResultsEnabled();
		this.enterResultsEnabled = b;
		firePropertyChange("enterResultsEnabled", old, isEnterResultsEnabled());
	}

	/**
	 * Action zum Disqualifizieren eines Teams
	 */
	@Action(enabledProperty = "disqualifyEnabled")
	public void disqualifyAction() {
		final Tournament t = getSelectedTournament();

		DisqualifyPanel disqualifyPanel = new DisqualifyPanel(t);
		setContentPanel(disqualifyPanel);
		disqualifyPanel.addCallbackListener(new IContentCallback() {

			public void processFinished(boolean success) {
				if (success) {
					setContentPanel(new TournamentView(t));
				} else {
					ProgressLogger.getInstance().log("Aktion abgebrochen.");
					setContentPanel(new TournamentView(t));
				}
			}
		});

	}

	private boolean disqualifyEnabled = false;

	/**
	 * Überprüft, ob die Disqualifizieren-Action aktiviert ist (und somt alle
	 * Buttons, MenuItems etc. die mit der Disqualifizieren-Action verbunden
	 * sind)
	 * 
	 * @return true, falls die Disqualifizieren-Action aktiviert ist
	 */
	public boolean isDisqualifyEnabled() {
		return disqualifyEnabled;
	}

	/**
	 * Aktiviert (bei true) bzw. deaktiviert (bei false) die
	 * Disqualifizieren-Action (und somt alle Buttons, MenuItems etc. die mit
	 * der Disqualifizieren-Action verbunden sind).
	 * 
	 * @param b
	 *            boolean
	 */
	public void setDisqualifyEnabled(boolean b) {
		boolean old = isDisqualifyEnabled();
		this.disqualifyEnabled = b;
		firePropertyChange("disqualifyEnabled", old, isDisqualifyEnabled());
	}

	/**
	 * Action zum Beenden eines Turniers
	 */
	@Action(enabledProperty = "finishTournamentEnabled")
	public void finishTournamentAction() {
		Tournament t = getSelectedTournament();

		// Prüfen, ob noch Spiele gespielt werden müssen.
		if (!t.getTournamentPlan().allMatchesFinished()) {
			JOptionPane.showMessageDialog(this.getFrame(),
					"Sie können das Turnier nicht beenden, "
							+ "da noch nicht alle Spiele ausgetragen wurden!");
			return;
		}

		ResourceMap rm = getResourceMap();

		int retval = JOptionPane.showConfirmDialog(getFrame(),
				rm.getString("optionPane.finishTournament.msg", t.getName()),
				rm.getString("optionPane.finishTournament.caption"),
				JOptionPane.YES_NO_OPTION);

		if (retval == JOptionPane.YES_OPTION) {
			TournamentManager.getInstance().finishTournament(t);
			ProgressLogger.getInstance().log(
					"Turnier " + t.getName() + " wurde beendet.");
			setContentPanel(new TournamentView(t));
			updateMainToolBar();
		}

	}

	private boolean finishTournamentEnabled = false;

	/**
	 * Überprüft, ob die TurnierBeeenden-Action aktiviert ist (und somt alle
	 * Buttons, MenuItems etc. die mit der TurnierBeeenden-Action verbunden
	 * sind)
	 * 
	 * @return true, falls die TurnierBeeenden-Action aktiviert ist
	 */
	public boolean isFinishTournamentEnabled() {
		return finishTournamentEnabled;
	}

	/**
	 * Aktiviert (bei true) bzw. deaktiviert (bei false) die
	 * TurnierBeeenden-Action (und somt alle Buttons, MenuItems etc. die mit der
	 * TurnierBeeenden-Action verbunden sind).
	 * 
	 * @param b
	 *            boolean
	 */
	public void setFinishTournamentEnabled(boolean b) {
		boolean old = isFinishTournamentEnabled();
		this.finishTournamentEnabled = b;
		firePropertyChange("finishTournamentEnabled", old,
				isFinishTournamentEnabled());
	}

	/**
	 * Action zum Erstellen der Ergebnislisten nach einem Turnier
	 */
	@Action(enabledProperty = "createResultlistEnabled")
	public void createResultlistAction() {

		Tournament t = getSelectedTournament();

		// Prüfen, ob das Turnier finale Runde hat.
		if (!t.getTournamentPlan().hasFinalRound()) {
			JOptionPane.showMessageDialog(getFrame(),
					"Für Turniere ohne Finalrunde können "
							+ "keine Ergebnislisten erstellt werden.");
			return;
		}

		// Dateiaufwahldialog anzeigen, falls nicht bestätigt, Ende.
		JFileChooser fileChooser = getPDFFileChooser();
		fileChooser.setName("Ergebnisliste.pdf");
		if (fileChooser.showSaveDialog(getFrame()) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		// .pdf dranhängen, falls nicht da.
		File file = fileChooser.getSelectedFile();
		if (!file.getName().toLowerCase().endsWith(".pdf")) {
			file = new File(file.getPath() + ".pdf");
		}

		// PDF erstellen
		try {
			TournamentPDF.createResultListPDF(t, file.getPath());
		} catch (Exception ignore) {
			JOptionPane.showMessageDialog(getFrame(),
					"Etwas ist beim Erstellen der PDF-Datei schiefgelaufen.");
			return;
		}

		// PDF anzeigen
		try {
			TournamentPDF.showDocument(file.getPath());
		} catch (IOException ignore) {
			JOptionPane
					.showMessageDialog(getFrame(),
							"Es ist keine Anwendung installiert, um PDF-Dateien zu betrachten.");
		}

		// Progress loggen
		ProgressLogger.getInstance().log(
				"Ergebnisliste für das Turnier " + t.getName()
						+ " wurden erstellt");

		// Aufräumen
		fileChooser.setSelectedFile(null);
		updateMainToolBar();
	}

	private boolean createResultlistEnabled = false;

	/**
	 * Überprüft, ob die ErgebnislistenErstellen-Action aktiviert ist (und somt
	 * alle Buttons, MenuItems etc. die mit der ErgebnislistenErstellen-Action
	 * verbunden sind)
	 * 
	 * @return true, falls die ErgebnislistenErstellen-Action aktiviert ist
	 */
	public boolean isCreateResultlistEnabled() {
		return createResultlistEnabled;
	}

	/**
	 * Aktiviert (bei true) bzw. deaktiviert (bei false) die
	 * ErgebnislistenErstellen-Action (und somt alle Buttons, MenuItems etc. die
	 * mit der ErgebnislistenErstellen-Action verbunden sind).
	 * 
	 * @param b
	 *            boolean
	 */
	public void setCreateResultlistEnabled(boolean b) {

		boolean old = isCreateResultlistEnabled();
		this.createResultlistEnabled = b;
		firePropertyChange("createResultlistEnabled", old,
				isCreateResultlistEnabled());
	}

	/**
	 * Action zum Erstellen der Urkunden für ein Turnier.
	 */
	@Action(enabledProperty = "createDocumentsEnabled")
	public void createDocumentsAction() {

		Tournament t = getSelectedTournament();

		// Prüfen, ob das Turnier finale Runde hat.
		if (!t.getTournamentPlan().hasFinalRound()) {
			JOptionPane.showMessageDialog(getFrame(),
					"Für Turniere ohne Finalrunde können "
							+ "keine Urkunden erstellt werden.");
			return;
		}

		// Dateiaufwahldialog anzeigen, falls nicht bestätigt, Ende.
		JFileChooser fileChooser = getPDFFileChooser();
		fileChooser.setName("Urkunden.pdf");
		if (fileChooser.showSaveDialog(getFrame()) != JFileChooser.APPROVE_OPTION) {
			return;
		}

		// .pdf dranhängen, falls nicht da.
		File file = fileChooser.getSelectedFile();
		if (!file.getName().toLowerCase().endsWith(".pdf")) {
			file = new File(file.getPath() + ".pdf");
		}

		// PDF erstellen
		try {
			TournamentPDF.createDocumentPDF(t, file.getPath());
		} catch (Exception ignore) {
			System.out.println(ignore.getClass().toString());
			System.out.println(ignore.getMessage());

			JOptionPane.showMessageDialog(getFrame(),
					"Etwas ist beim Erstellen der PDF-Datei schiefgelaufen.");
			return;
		}

		// PDF anzeigen
		try {
			TournamentPDF.showDocument(file.getPath());
		} catch (IOException ignore) {
			JOptionPane
					.showMessageDialog(getFrame(),
							"Es ist keine Anwendung installiert, um PDF-Dateien zu betrachten.");
		}

		// Progress loggen
		ProgressLogger.getInstance().log(
				"Urkunden für das Turnier '" + t.getName()
						+ "' wurden erstellt");

		// Aufräumen
		fileChooser.setSelectedFile(null);
		updateMainToolBar();
	}

	private boolean createDocumentsEnabled = false;

	/**
	 * Überprüft, ob die UrkundenErstellen-Action aktiviert ist (und somt alle
	 * Buttons, MenuItems etc. die mit der UrkundenErstellen-Action verbunden
	 * sind)
	 * 
	 * @return true, falls die UrkundenErstellen-Action aktiviert ist
	 */
	public boolean isCreateDocumentsEnabled() {
		return createDocumentsEnabled;
	}

	/**
	 * Aktiviert (bei true) bzw. deaktiviert (bei false) die
	 * UrkundenErstellen-Action (und somt alle Buttons, MenuItems etc. die mit
	 * der UrkundenErstellen-Action verbunden sind).
	 * 
	 * @param b
	 *            boolean
	 */
	public void setCreateDocumentsEnabled(boolean b) {
		boolean old = isCreateDocumentsEnabled();
		this.createDocumentsEnabled = b;
		firePropertyChange("createDocumentsEnabled", old,
				isCreateDocumentsEnabled());
	}

	/**
	 * Action zum Nachmelden von Sportlern bzw. Teams durch den/die
	 * Turnierleiter
	 */
	@Action(enabledProperty = "lateSignUpEnabled")
	public void lateSignUpAction() {
		final Tournament t = getSelectedTournament();
		LateSignUpForm lateSignup = new LateSignUpForm(t);
		setContentPanel(lateSignup);
		lateSignup.addCallbackListener(new IContentCallback() {

			public void processFinished(boolean success) {
				if (success) {
					setContentPanel(new TournamentView(t));
				} else {
					ProgressLogger.getInstance().log("Aktion abgebrochen.");
					setContentPanel(new TournamentView(t));
				}
			}
		});
	}

	private boolean lateSignUpEnabled = false;

	/**
	 * Überprüft, ob die Nachmelde-Action aktiviert ist (und somt alle Buttons,
	 * MenuItems etc. die mit der Nachmelde-Action verbunden sind)
	 * 
	 * @return true, falls die Nachmelde-Action aktiviert ist
	 */
	public boolean isLateSignUpEnabled() {
		return lateSignUpEnabled;
	}

	/**
	 * Aktiviert (bei true) bzw. deaktiviert (bei false) die Nachmelde-Action
	 * (und somt alle Buttons, MenuItems etc. die mit der Nachmelde-Action
	 * verbunden sind).
	 * 
	 * @param b
	 *            boolean
	 */
	public void setLateSignUpEnabled(boolean b) {
		boolean old = isLateSignUpEnabled();
		this.lateSignUpEnabled = b;
		firePropertyChange("lateSignUpEnabled", old, isLateSignUpEnabled());
	}

	/**
	 * Action zum Bestaetigen der Voranmeldung eines Sportlers/Teams
	 */
	@Action(enabledProperty = "confirmSignUpEnabled")
	public void confirmSignUpAction() {
		final Tournament t = getSelectedTournament();
		ConfirmSignUpForm confirmSignup = new ConfirmSignUpForm(t);
		setContentPanel(confirmSignup);
		confirmSignup.addCallbackListener(new IContentCallback() {

			public void processFinished(boolean success) {
				if (success) {
					ProgressLogger.getInstance().log(
							"Teilnehmer wurde(n) erfolgreich bestätigt.");
					setContentPanel(new TournamentView(t));
				} else {
					ProgressLogger.getInstance().log("Aktion abgebrochen.");
					setContentPanel(new TournamentView(t));
				}
			}
		});
	}

	private boolean confirmSignUpEnabled = false;

	/**
	 * Überprüft, ob die VoranmeldungBestaetigen-Action aktiviert ist (und somt
	 * alle Buttons, MenuItems etc. die mit der VoranmeldungBestaetigen-Action
	 * verbunden sind)
	 * 
	 * @return true, falls die VoranmeldungBestaetigen-Action aktiviert ist
	 */
	public boolean isConfirmSignUpEnabled() {
		return confirmSignUpEnabled;
	}

	/**
	 * Aktiviert (bei true) bzw. deaktiviert (bei false) die
	 * VoranmeldungBestaetigen-Action (und somt alle Buttons, MenuItems etc. die
	 * mit der VoranmeldungBestaetigen-Action verbunden sind).
	 * 
	 * @param b
	 *            boolean
	 */
	public void setConfirmSignUpEnabled(boolean b) {
		boolean old = isConfirmSignUpEnabled();
		this.confirmSignUpEnabled = b;
		firePropertyChange("confirmSignUpEnabled", old,
				isConfirmSignUpEnabled());
	}

	/**
	 * Action zum Beenden des Meldezeitraums
	 */
	@Action(enabledProperty = "finishSignUpTimeEnabled")
	public void finishSignUpTimeAction() {
		Tournament t = getSelectedTournament();

		ResourceMap rm = getResourceMap();

		int retval = JOptionPane.showConfirmDialog(getFrame(),
				rm.getString("optionPane.finishSignUpTime.msg", t.getName()),
				rm.getString("optionPane.finishSignUpTime.msg"),
				JOptionPane.YES_NO_OPTION);

		if (retval == JOptionPane.YES_OPTION) {
			TournamentManager.getInstance().finishSignUpTournament(t);
			ProgressLogger.getInstance().log(
					"Meldezeitraum für das Turnier " + t.getName()
							+ " wurde beendet.");

			// Status hat sich verändert, updaten
			setContentPanel(new TournamentView((t)));

			updateMainToolBar();
		}
	}

	private boolean finishSignUpTimeEnabled = false;

	/**
	 * Überprüft, ob die MeldezeitraumBeenden-Action aktiviert ist (und somt
	 * alle Buttons, MenuItems etc. die mit der MeldezeitraumBeenden-Action
	 * verbunden sind)
	 * 
	 * @return true, falls die MeldezeitraumBeenden-Action aktiviert ist
	 */
	public boolean isFinishSignUpTimeEnabled() {
		return finishSignUpTimeEnabled;
	}

	/**
	 * Aktiviert (bei true) bzw. deaktiviert (bei false) die
	 * MeldezeitraumBeenden-Action (und somt alle Buttons, MenuItems etc. die
	 * mit der MeldezeitraumBeenden-Action verbunden sind).
	 * 
	 * @param b
	 *            boolean
	 */
	public void setFinishSignUpTimeEnabled(boolean b) {
		boolean old = isFinishSignUpTimeEnabled();
		this.finishSignUpTimeEnabled = b;
		firePropertyChange("finishSignUpTimeEnabled", old,
				isFinishSignUpTimeEnabled());
	}

	/**
	 * Action zum Zuweisen von Schiedsrichtern zu einem Turnier
	 */
	@Action(enabledProperty = "chooseRefereeEnabled")
	public void chooseRefereeAction() {
		final Tournament t = getSelectedTournament();
		ConfirmSignUpForm confirmSignup = new ConfirmSignUpForm(t);
		setContentPanel(confirmSignup);
		confirmSignup.addCallbackListener(new IContentCallback() {

			public void processFinished(boolean success) {
				if (success) {
					ProgressLogger.getInstance().log(
							"Teilnehmer wurde(n) erfolgreich bestätigt.");
					setContentPanel(new TournamentView(t));
					updateMainToolBar();
				} else {
					ProgressLogger.getInstance().log("Aktion abgebrochen.");
					setContentPanel(new TournamentView(t));
					updateMainToolBar();
				}
			}
		});
	}

	private boolean chooseRefereeEnabled = false;

	/**
	 * Überprüft, ob die SchiedsrichterZuweisen-Action aktiviert ist (und somt
	 * alle Buttons, MenuItems etc. die mit der SchiedsrichterZuweisen-Action
	 * verbunden sind)
	 * 
	 * @return true, falls die SchiedsrichterZuweisen-Action aktiviert ist
	 */
	public boolean isChooseRefereeEnabled() {
		return chooseRefereeEnabled;
	}

	/**
	 * Aktiviert (bei true) bzw. deaktiviert (bei false) die
	 * SchiedsrichterZuweisen-Action (und somt alle Buttons, MenuItems etc. die
	 * mit der SchiedsrichterZuweisen-Action verbunden sind).
	 * 
	 * @param b
	 *            boolean
	 */
	public void setChooseRefereeEnabled(boolean b) {
		boolean old = isChooseRefereeEnabled();
		this.chooseRefereeEnabled = b;
		firePropertyChange("chooseRefereeEnabled", old,
				isChooseRefereeEnabled());
	}

	/**
	 * Action zum Erstellen des Turnierplans/Spielplans
	 */
	@Action(enabledProperty = "createTournamentPlanEnabled")
	public void createTournamentPlanAction() {
		final Tournament t = getSelectedTournament();

		CreateTournamentPlanForm tplan = new CreateTournamentPlanForm(t);
		setContentPanel(tplan);
		tplan.addCallbackListener(new IContentCallback() {

			public void processFinished(boolean success) {
				if (success) {
					JOptionPane.showMessageDialog(
							getFrame(),
							getResourceMap().getString(
									"optionPane.createTournamentPlane.msg",
									t.getName()));
					ProgressLogger
							.getInstance()
							.log("Turnierplan für "
									+ t.getName()
									+ " wurde erstellt und das Turnier gestartet.");

					setContentPanel(new TournamentView(t));
					updateMainToolBar();
				}
			}
		});
	}

	private boolean createTournamentPlanEnabled = false;

	/**
	 * Überprüft, ob die TurnierplanErstellen-Action aktiviert ist (und somt
	 * alle Buttons, MenuItems etc. die mit der TurnierplanErstellen-Action
	 * verbunden sind)
	 * 
	 * @return true, falls die TurnierplanErstellen-Action aktiviert ist
	 */
	public boolean isCreateTournamentPlanEnabled() {
		return createTournamentPlanEnabled;
	}

	/**
	 * Aktiviert (bei true) bzw. deaktiviert (bei false) die
	 * TurnierplanErstellen-Action (und somt alle Buttons, MenuItems etc. die
	 * mit der TurnierplanErstellen-Action verbunden sind).
	 * 
	 * @param b
	 *            boolean
	 */
	public void setCreateTournamentPlanEnabled(boolean b) {
		boolean old = isCreateTournamentPlanEnabled();
		this.createTournamentPlanEnabled = b;
		firePropertyChange("createTournamentPlanEnabled", old,
				isCreateTournamentPlanEnabled());
	}

	/**
	 * Action zum Bestimmen der Turnierleiter
	 */
	@Action(enabledProperty = "chooseLeaderEnabled")
	public void chooseLeaderAction() {
		final Tournament t = getSelectedTournament();
		ChooseLeadersForm form = new ChooseLeadersForm(t);
		setContentPanel(form);
		form.addCallbackListener(new IContentCallback() {

			public void processFinished(boolean success) {
				if (success) {
					ProgressLogger.getInstance().log(
							"Turnierleiter erfolgreich dem Turnier"
									+ t.getName() + " zugewiesen.");
					setContentPanel(new TournamentView(t));
					updateMainToolBar();
				} else {
					ProgressLogger.getInstance().log("Aktion abgebrochen.");
					setContentPanel(new TournamentView(t));
					updateMainToolBar();
				}
			}
		});
	}

	private boolean chooseLeaderEnabled = false;

	/**
	 * Überprüft, ob die TurnierleiterBestimmen-Action aktiviert ist (und somt
	 * alle Buttons, MenuItems etc. die mit der TurnierleiterBestimmen-Action
	 * verbunden sind)
	 * 
	 * @return true, falls die TurnierleiterBestimmen-Action aktiviert ist
	 */
	public boolean isChooseLeaderEnabled() {
		return chooseLeaderEnabled;
	}

	/**
	 * Aktiviert (bei true) bzw. deaktiviert (bei false) die
	 * TurnierleiterBestimmen-Action (und somt alle Buttons, MenuItems etc. die
	 * mit der TurnierleiterBestimmen-Action verbunden sind).
	 * 
	 * @param b
	 *            boolean
	 */
	public void setChooseLeaderEnabled(boolean b) {
		boolean old = isChooseLeaderEnabled();
		this.chooseLeaderEnabled = b;
		firePropertyChange("chooseLeaderEnabled", old, isChooseLeaderEnabled());
	}

	/**
	 * Action zum Absagen eines Turniers
	 */
	@Action(enabledProperty = "cancelTournamentEnabled")
	public void cancelTournamentAction() {
		Tournament t = getSelectedTournament();

		ResourceMap rm = getResourceMap();

		int retval = JOptionPane.showConfirmDialog(getFrame(),
				rm.getString("optionPane.cancelTournament.msg", t.getName()),
				rm.getString("optionPane.cancelTournament.caption"),
				JOptionPane.YES_NO_OPTION);

		if (retval == JOptionPane.YES_OPTION) {
			TournamentManager.getInstance().cancelTournament(t);
			ProgressLogger.getInstance().log(
					"Turnier" + t.getName() + " abgesagt.");

			// Status hat sich verändert, aktualisieren
			setComponent(new TournamentView(t));

			updateMainToolBar();
		}
	}

	private boolean cancelTournamentEnabled = false;

	/**
	 * Überprüft, ob die TurnierAbsagen-Action aktiviert ist (und somt alle
	 * Buttons, MenuItems etc. die mit der TurnierAbsagen-Action verbunden sind)
	 * 
	 * @return true, falls die TurnierAbsagen-Action aktiviert ist
	 */
	public boolean isCancelTournamentEnabled() {
		return cancelTournamentEnabled;
	}

	/**
	 * Aktiviert (bei true) bzw. deaktiviert (bei false) die
	 * TurnierAbsagen-Action (und somt alle Buttons, MenuItems etc. die mit der
	 * TurnierAbsagen-Action verbunden sind).
	 * 
	 * @param b
	 *            boolean
	 */
	public void setCancelTournamentEnabled(boolean b) {
		boolean old = isCancelTournamentEnabled();
		this.cancelTournamentEnabled = b;
		firePropertyChange("cancelTournamentEnabled", old,
				isCancelTournamentEnabled());
	}

	/** Wird beim Auswählen eines Filters aufgerufen. */
	@Action
	public void chooseFilter() {
		tournamentList.clear();

		switch (cboxFilter.getSelectedIndex()) {
		case FILTER_ALL:
			tournamentList.addAll(TournamentManager.getInstance()
					.getTournamentsByUser(
							UserManager.getInstance().getLoggedinUser()));
			break;
		case FILTER_MY:
			tournamentList.addAll(TournamentManager.getInstance()
					.getUsersTournaments(
							UserManager.getInstance().getLoggedinUser()));
			break;
		case FILTER_PUBLISHED:
			tournamentList.addAll(TournamentManager.getInstance()
					.getPublishedTournaments());
			break;
		case FILTER_RUNNING:
			tournamentList.addAll(TournamentManager.getInstance()
					.getRunningTournaments());
			break;
		case FILTER_FINISHED:
			tournamentList.addAll(TournamentManager.getInstance()
					.getFinishedTournaments());
		}
	}

	/**
	 * Zeigt einen Dialog zum Ändern des Passwortes von dem aktuell eingeloggte
	 * Benutzer.
	 */
	@Action
	public void showChangePassDialog() {
		ChangePasswortDialog d = new ChangePasswortDialog(getFrame(),
				UserManager.getInstance().getLoggedinUser());
		d.setVisible(true);

		if (d.hasPassChanged()) {
			ProgressLogger.getInstance().log(
					"Passwort wurde erfolgreich geändert.");
		}
	}

	@Action
	public void printPlan() {
		HTMLGenerator g = new HTMLGenerator();
		Tournament t = getSelectedTournament();
		if (t != null && t.getTournamentPlan() != null) {
			g.writeAndOpen(g.getPlan(t.getTournamentPlan()), "plan.html");
		}
	}

	@Action
	public void printGroup() {
		HTMLGenerator g = new HTMLGenerator();
		Tournament t = getSelectedTournament();
		if (t != null && t.getTournamentPlan() != null) {
			g.writeAndOpen(g.getPlan(t.getTournamentPlan()), "groups.html");
		}
	}

	@Action
	public void printFinal() {
		HTMLGenerator g = new HTMLGenerator();
		Tournament t = getSelectedTournament();
		if (t != null && t.getTournamentPlan() != null) {
			g.writeAndOpen(g.getRanking(t.getTournamentPlan()), "ranking.html");
		}
	}

	/*
	 * private ResourceMap getResourceMap() { return
	 * MainApplication.getApplication
	 * ().getContext().getResourceMap(MainView.class); }
	 */

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnCancelTournament;
	private javax.swing.JButton btnChooseLeader;
	private javax.swing.JButton btnChooseReferee;
	private javax.swing.JButton btnConfirmSignUp;
	private javax.swing.JButton btnCreateDocuments;
	private javax.swing.JButton btnCreateResultlist;
	private javax.swing.JButton btnCreateTournament;
	private javax.swing.JButton btnCreateTournamentPlan;
	private javax.swing.JButton btnDisqualify;
	private javax.swing.JButton btnEnterResults, btnprintPlan, btnprintfinal,
			btnprintgroup;
	private javax.swing.JButton btnFinishSignUpTime;
	private javax.swing.JButton btnFinishTournament;
	private javax.swing.JButton btnLateSignUp;
	private javax.swing.JButton btnLogout;
	private javax.swing.JButton btnPublishTournament;
	private javax.swing.JButton btnQuit;
	private javax.swing.JButton btnSignOff;
	private javax.swing.JButton btnSignUp;
	private javax.swing.JButton btnTournamentPlan;
	private javax.swing.JComboBox cboxFilter;
	private javax.swing.JScrollPane contentScrollPane;
	private javax.swing.JMenuItem jMenuItem1;
	private javax.swing.JLabel lblDate;
	private javax.swing.JLabel lblFilter;
	private javax.swing.JLabel lblLoggedInUser;
	private javax.swing.JLabel lblTournaments;
	private javax.swing.JPanel mainPanel;
	private javax.swing.JSplitPane mainSplitPane;
	private javax.swing.JMenu menuAthlete;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenu menuLeader;
	private javax.swing.JMenu menuManager;
	private javax.swing.JMenu menuReferee;
	private javax.swing.JMenuItem miCancelTournament;
	private javax.swing.JMenuItem miChooseLeader;
	private javax.swing.JMenuItem miChooseReferee;
	private javax.swing.JMenuItem miConfirmSignUp;
	private javax.swing.JMenuItem miCreateDocuments;
	private javax.swing.JMenuItem miCreateResultlist;
	private javax.swing.JMenuItem miCreateTournament;
	private javax.swing.JMenuItem miCreateTournamentPlan;
	private javax.swing.JMenuItem miDisqualify;
	private javax.swing.JMenuItem miEnterResults;
	private javax.swing.JMenuItem miFinishSignUpTime;
	private javax.swing.JMenuItem miFinishTournament;
	private javax.swing.JMenuItem miLateSignUp;
	private javax.swing.JMenuItem miPublishTournament;
	private javax.swing.JMenuItem miSignOff;
	private javax.swing.JMenuItem miSignUp;
	private javax.swing.JMenuItem miTournamentPlan;
	private javax.swing.JMenuItem mnuLogout;
	private javax.swing.JScrollPane progressScrollBar;
	private javax.swing.JTextPane progressTextPane;
	private javax.swing.JSplitPane splitPane;
	private javax.swing.JPanel statusPanel;
	private javax.swing.JToolBar tbAthlete;
	private javax.swing.JToolBar tbLeader;
	private javax.swing.JToolBar tbMain;
	private javax.swing.JToolBar tbManager;
	private javax.swing.JToolBar tbReferee;
	private javax.swing.JToolBar tbUser;
	private javax.swing.JPanel tournamentListPanel;
	private javax.swing.JList tournamentListView;
	private javax.swing.JScrollPane tournamentListscrollPane;
	private org.jdesktop.beansbinding.BindingGroup bindingGroup;
	// End of variables declaration//GEN-END:variables
	private JDialog aboutBox;
	private JFileChooser pdfFileChooser;
}
