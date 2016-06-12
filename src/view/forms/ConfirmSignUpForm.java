/*
 * AddRefereeForm.java
 *
 * Created on 04.03.2009, 11:35:54
 */
package view.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import model.Team;
import model.Tournament;
import model.User;

import org.jdesktop.application.Action;
import org.jdesktop.observablecollections.ObservableCollections;

import view.ContentPanel;
import control.TournamentManager;
import control.UserManager;

/**
 * ContentPanel welches das innere Fenster darstellt in welchem man einem
 * Turnier Schiedsrichter bestätigen kann und voranmeldungen von Teams und
 * Spielern bestätigen kann
 * 
 * @author dima,Maike Dudek,Paul Lubos,Daniel
 */
public class ConfirmSignUpForm extends ContentPanel {

	private static final long serialVersionUID = -671501919667320454L;

	/**
	 * Tree zum anzeigen der Teams und Players.
	 */
	public MyJTree myTree1;
	/**
	 * das tournament wird gespeichert under dieser variable
	 */
	public Tournament t;
	private User transUser; // User zum Speichern von DragDrop
	private HashSet<User> confirmedPlayers;

	/**
	 * Creates new ConfirmSignUpForm ...test constructor
	 */
	public ConfirmSignUpForm() {

		t = new Tournament("name", 1, 10, "hallo", 10, 1, new Date());

		// this.t=t;

		int num = 4;
		int cnt = 0;
		List<Team> teams = new ArrayList<Team>(num);
		for (int x = 0; x < num; x++) {
			Team team = new Team("Team " + (x + 1));
			for (int y = 0; y < 3; y++) {
				User u = new User("User " + (cnt++), "");
				team.addPlayer(u);
			}
			teams.add(team);
		}
		myTree1 = new MyJTree(new MyModel(teams));
		myTree1.setCellRenderer(new MyTreeCellRenderer());
		initComponents();
		initLists();

	}

	/**
	 * Creates new form ConfirmSignUpForm
	 * 
	 * @param t
	 *            Turnier für das die Anmeldungen bestätigt werden sollen
	 */
	public ConfirmSignUpForm(Tournament t) {

		this.t = t;
		confirmedPlayers = new HashSet<User>();
		List<Team> listofteams = t.getTeams();
		for (Team team : listofteams) {
			confirmedPlayers.addAll(team.getConfirmedPlayers());
		}

		myTree1 = new MyJTree(new MyModel(t.getTeams()));
		myTree1.setCellRenderer(new MyTreeCellRenderer());

		selectedReferees = new ArrayList<User>();
		refereeList = ObservableCollections
				.observableList(new ArrayList<User>());
		refereeList.addAll(UserManager.getInstance().getReferees());
		assignedRefereeList = ObservableCollections
				.observableList(new ArrayList<User>());
		assignedRefereeList.addAll(t.getReferees());

		// Referee, die schon als Spieler vorangemeldet sind, entfernen.
		refereeList.removeAll(t.getPlayers());

		// Bereits bestätigte Referees entfernen.
		refereeList.removeAll(assignedRefereeList);

		initComponents();
	}

	/**
	 * 
	 * Treemodel zum anzeigen des Trees aus Spielern und Teams.
	 * 
	 * @author Paul, Daniel Class MyModel
	 * 
	 */
	private class MyModel implements TreeModel {

		/**
		 * Der rootknoten der ein List<Team> ist
		 */
		private List<Team> root;
		/**
		 * die liste der Treemodellistener
		 */
		private List<TreeModelListener> listeners;

		/**
		 * Constructor
		 * 
		 * @param teams
		 *            eine liste von teams
		 */
		public MyModel(List<Team> teams) {
			listeners = new ArrayList<TreeModelListener>();
			root = teams;
		}

		/**
		 * fügt dem model einen treemodellistener hinzu
		 * 
		 * @param l
		 *            der Treemodellistener
		 */
		@Override
		public void addTreeModelListener(TreeModelListener l) {
			listeners.add(l);
		}

		/**
		 * Methode gibt das kind eines knotens wieder
		 * 
		 * @param index
		 *            der index des kinds
		 * @param parent
		 *            der parentknoten
		 */
		@Override
		public Object getChild(Object parent, int index) {
			if (parent.equals(root)) {
				return root.get(index);
			} else if (parent instanceof Team) {
				Team t = (Team) parent;
				return t.getPlayers().get(index);
			}
			return null;
		}

		/**
		 * gibt die anzahl der kinder wieder
		 * 
		 * @param parent
		 *            parentnode
		 * @return die anzahl der kinder
		 */
		@Override
		public int getChildCount(Object parent) {
			if (parent.equals(root)) {
				return root.size();
			} else if (parent instanceof Team) {
				Team t = (Team) parent;
				return t.getPlayers().size();
			}
			return 0;
		}

		/**
		 * Gibt den index des kindknotens wieder
		 * 
		 * @param parent
		 *            Parent
		 * @param child
		 *            Kindknoten
		 * @return der Index
		 */
		@Override
		public int getIndexOfChild(Object parent, Object child) {
			if (parent == null || child == null) {
				return -1;
			}
			if (parent.equals(root)) {
				return root.indexOf(child);
			} else if (parent instanceof Team) {
				Team t = (Team) parent;
				return t.getPlayers().indexOf(child);
			}
			return -1;
		}

		/**
		 * Gibt die root des Baums wieder.
		 */
		@Override
		public Object getRoot() {
			return root;
		}

		/**
		 * Gibt wieder ob das Object ein blatt im tree ist.
		 * 
		 * @param node
		 *            Das Object
		 */
		@Override
		public boolean isLeaf(Object node) {
			return node instanceof User;
		}

		/**
		 * fübt dem TreeModel inen TreeModelListener hinzu
		 * 
		 * @param l
		 *            der TreeModelListener
		 */
		@Override
		public void removeTreeModelListener(TreeModelListener l) {
			listeners.remove(l);
		}

		@Override
		public void valueForPathChanged(TreePath path, Object newValue) {
			// Hier evtl. sowas??! nicht sicher.
			/*
			 * for (TreeModelListener l:listeners) {
			 * l.treeStructureChanged(null); }
			 */
		}
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

		jSplitPane1 = new javax.swing.JSplitPane();
		jPanel1 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		treeTeamsPlayers = myTree1;
		jLabel1 = new javax.swing.JLabel();
		btnConfirm = new javax.swing.JButton();
		btnCancelConfirm = new javax.swing.JButton();
		jPanel2 = new javax.swing.JPanel();
		jScrollPane2 = new javax.swing.JScrollPane();
		jlistReferees = new javax.swing.JList();
		jScrollPane3 = new javax.swing.JScrollPane();
		jlistConfirmedReferees = new javax.swing.JList();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		btnUp = new javax.swing.JButton();
		btnDown = new javax.swing.JButton();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jLabel5 = new javax.swing.JLabel();

		setName("Form"); // NOI18N

		jSplitPane1.setName("jSplitPane1"); // NOI18N

		jPanel1.setName("jPanel1"); // NOI18N

		jScrollPane1.setName("jScrollPane1"); // NOI18N

		treeTeamsPlayers.setName("treeTeamsPlayers"); // NOI18N
		treeTeamsPlayers.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				treeTeamsPlayersMouseClicked(evt);
			}
		});
		treeTeamsPlayers
				.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
					public void valueChanged(
							javax.swing.event.TreeSelectionEvent evt) {
						treeTeamsPlayersValueChanged(evt);
					}
				});
		jScrollPane1.setViewportView(treeTeamsPlayers);

		org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application
				.getInstance(control.MainApplication.class).getContext()
				.getResourceMap(ConfirmSignUpForm.class);
		jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
		jLabel1.setName("jLabel1"); // NOI18N

		javax.swing.ActionMap actionMap = org.jdesktop.application.Application
				.getInstance(control.MainApplication.class).getContext()
				.getActionMap(ConfirmSignUpForm.class, this);
		btnConfirm.setAction(actionMap.get("confirmSignUpAction")); // NOI18N
		btnConfirm.setText(resourceMap.getString("btnConfirm.text")); // NOI18N
		btnConfirm.setName("btnConfirm"); // NOI18N

		btnCancelConfirm.setAction(actionMap.get("unconfirmSignUpAction")); // NOI18N
		btnCancelConfirm
				.setText(resourceMap.getString("btnCancelConfirm.text")); // NOI18N
		btnCancelConfirm.setName("btnCancelConfirm"); // NOI18N

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addComponent(
																				btnConfirm)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				btnCancelConfirm))
														.addComponent(
																jScrollPane1,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																274,
																Short.MAX_VALUE)
														.addComponent(
																jLabel1,
																javax.swing.GroupLayout.Alignment.TRAILING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																274,
																Short.MAX_VALUE))
										.addContainerGap()));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addComponent(jLabel1)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jScrollPane1,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												383, Short.MAX_VALUE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																btnConfirm)
														.addComponent(
																btnCancelConfirm))
										.addContainerGap()));

		jSplitPane1.setLeftComponent(jPanel1);

		jPanel2.setName("jPanel2"); // NOI18N

		jScrollPane2.setName("jScrollPane2"); // NOI18N

		jlistReferees
				.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		jlistReferees.setDragEnabled(true);
		jlistReferees.setName("jlistReferees"); // NOI18N

		org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty
				.create("${refereeList}");
		org.jdesktop.swingbinding.JListBinding jListBinding = org.jdesktop.swingbinding.SwingBindings
				.createJListBinding(
						org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
						this, eLProperty, jlistReferees);
		jListBinding.setDetailBinding(org.jdesktop.beansbinding.ELProperty
				.create("${name}"));
		bindingGroup.addBinding(jListBinding);
		org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings
				.createAutoBinding(
						org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
						this, org.jdesktop.beansbinding.ELProperty
								.create("${selectedReferees}"), jlistReferees,
						org.jdesktop.beansbinding.BeanProperty
								.create("selectedElements"));
		bindingGroup.addBinding(binding);

		jlistReferees
				.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							javax.swing.event.ListSelectionEvent evt) {
						jlistRefereesValueChanged(evt);
					}
				});
		jScrollPane2.setViewportView(jlistReferees);

		jScrollPane3.setName("jScrollPane3"); // NOI18N

		jlistConfirmedReferees.setDragEnabled(true);
		jlistConfirmedReferees.setName("jlistConfirmedReferees"); // NOI18N

		eLProperty = org.jdesktop.beansbinding.ELProperty
				.create("${assignedRefereeList}");
		jListBinding = org.jdesktop.swingbinding.SwingBindings
				.createJListBinding(
						org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
						this, eLProperty, jlistConfirmedReferees);
		jListBinding.setDetailBinding(org.jdesktop.beansbinding.ELProperty
				.create("${name}"));
		bindingGroup.addBinding(jListBinding);
		binding = org.jdesktop.beansbinding.Bindings
				.createAutoBinding(
						org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
						this, org.jdesktop.beansbinding.ELProperty
								.create("${selectedAssignedReferees}"),
						jlistConfirmedReferees,
						org.jdesktop.beansbinding.BeanProperty
								.create("selectedElements"));
		bindingGroup.addBinding(binding);

		jlistConfirmedReferees
				.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
					public void valueChanged(
							javax.swing.event.ListSelectionEvent evt) {
						jlistConfirmedRefereesValueChanged(evt);
					}
				});
		jScrollPane3.setViewportView(jlistConfirmedReferees);

		jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
		jLabel2.setName("jLabel2"); // NOI18N

		jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
		jLabel3.setName("jLabel3"); // NOI18N

		jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
		jLabel4.setName("jLabel4"); // NOI18N

		btnUp.setAction(actionMap.get("moveUpAction")); // NOI18N
		btnUp.setText(resourceMap.getString("btnUp.text")); // NOI18N
		btnUp.setName("btnUp"); // NOI18N

		btnDown.setAction(actionMap.get("moveDownAction")); // NOI18N
		btnDown.setText(resourceMap.getString("btnDown.text")); // NOI18N
		btnDown.setName("btnDown"); // NOI18N

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(
				jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout
				.setHorizontalGroup(jPanel2Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel2Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel2Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																jPanel2Layout
																		.createSequentialGroup()
																		.addGroup(
																				jPanel2Layout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.TRAILING)
																						.addGroup(
																								javax.swing.GroupLayout.Alignment.LEADING,
																								jPanel2Layout
																										.createSequentialGroup()
																										.addComponent(
																												jLabel3)
																										.addPreferredGap(
																												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																										.addComponent(
																												btnUp)
																										.addPreferredGap(
																												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																										.addComponent(
																												btnDown))
																						.addComponent(
																								jLabel4,
																								javax.swing.GroupLayout.Alignment.LEADING))
																		.addGap(17,
																				17,
																				17))
														.addGroup(
																jPanel2Layout
																		.createSequentialGroup()
																		.addComponent(
																				jLabel2)
																		.addContainerGap(
																				284,
																				Short.MAX_VALUE))
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																jPanel2Layout
																		.createSequentialGroup()
																		.addGroup(
																				jPanel2Layout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.TRAILING)
																						.addComponent(
																								jScrollPane3,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								389,
																								Short.MAX_VALUE)
																						.addComponent(
																								jScrollPane2,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								389,
																								Short.MAX_VALUE))
																		.addContainerGap()))));
		jPanel2Layout
				.setVerticalGroup(jPanel2Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel2Layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(jLabel4)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(jLabel2)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(
												jScrollPane2,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												jPanel2Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(btnUp)
														.addComponent(btnDown)
														.addComponent(jLabel3))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jScrollPane3,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												248, Short.MAX_VALUE)
										.addContainerGap()));

		jSplitPane1.setRightComponent(jPanel2);

		jButton1.setAction(actionMap.get("confirmRefereeAction")); // NOI18N
		jButton1.setName("jButton1"); // NOI18N

		jButton2.setAction(actionMap.get("cancelAction")); // NOI18N
		jButton2.setName("jButton2"); // NOI18N

		jLabel5.setFont(jLabel5.getFont().deriveFont(
				jLabel5.getFont().getStyle() | java.awt.Font.BOLD,
				jLabel5.getFont().getSize() + 3));
		jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
		jLabel5.setName("jLabel5"); // NOI18N

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
														jSplitPane1,
														javax.swing.GroupLayout.Alignment.TRAILING)
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.TRAILING)
																				.addGroup(
																						layout.createSequentialGroup()
																								.addComponent(
																										jLabel5)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																										234,
																										Short.MAX_VALUE))
																				.addGroup(
																						layout.createSequentialGroup()
																								.addComponent(
																										jButton1)
																								.addPreferredGap(
																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
																.addComponent(
																		jButton2)))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGap(9, 9, 9)
								.addComponent(jLabel5)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(jSplitPane1,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										501,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jButton2)
												.addComponent(jButton1))
								.addContainerGap(
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));

		bindingGroup.bind();
	}// </editor-fold>//GEN-END:initComponents

	/**
	 * Methode die prüft ob die selection im tree sich geändert hat.
	 * 
	 */
	private void treeTeamsPlayersValueChanged(
			javax.swing.event.TreeSelectionEvent evt) {// GEN-FIRST:event_treeTeamsPlayersValueChanged
		Object o = treeTeamsPlayers.getSelectionPath().getLastPathComponent();

		if (o != null && o instanceof User) {
			User u = (User) o;
			Team t = getTeamUserIsIn(u);
			if (t != null) {
				boolean b = this.confirmedPlayers.contains(u);
				setConfirmSignUpActionEnabled(!b);
				setUnconfirmSignUpActionEnabled(b);
			}
		} else if (o != null && o instanceof Team) {
			Team t = (Team) o;
			boolean b = confirmedPlayers.containsAll(t.getPlayers());
			setConfirmSignUpActionEnabled(!b);
			setUnconfirmSignUpActionEnabled(b);
		} else {
			setConfirmSignUpActionEnabled(false);
			setUnconfirmSignUpActionEnabled(false);
		}
	}// GEN-LAST:event_treeTeamsPlayersValueChanged

	private void treeTeamsPlayersMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_treeTeamsPlayersMouseClicked

		if (evt.getButton() == MouseEvent.BUTTON3 && evt.getClickCount() == 1) {
			confirmSignUpAction();

		}
		if (evt.getButton() == MouseEvent.BUTTON2 && evt.getClickCount() == 1) {
			unconfirmSignUpAction();

		}
	}// GEN-LAST:event_treeTeamsPlayersMouseClicked

	private void jlistConfirmedRefereesValueChanged(
			javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_jlistConfirmedRefereesValueChanged
		setMoveUpActionEnabled(jlistConfirmedReferees.getSelectedValue() != null);
	}// GEN-LAST:event_jlistConfirmedRefereesValueChanged

	private void jlistRefereesValueChanged(
			javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_jlistRefereesValueChanged
		setMoveDownActionEnabled(jlistReferees.getSelectedValue() != null);
	}// GEN-LAST:event_jlistRefereesValueChanged

	/**
	 * Unterklasse vom JTree um die Darstellung von Teams und Usern zu
	 * ermöglichen
	 * 
	 * @author Paul
	 */
	public class MyJTree extends JTree {

		private static final long serialVersionUID = -8926808743137072249L;

		/**
		 * Constructor
		 */
		private MyJTree() {
			super();
		}

		/**
		 * Contructor mit übergebenem Datenmodel
		 * 
		 * @param myModel
		 *            das DatenModel der Klasse TreeModel
		 */
		private MyJTree(TreeModel myModel) {
			super(myModel);
		}

		/**
		 * Überschriebene Methode von JTree. Sie sorgt dafür, dass , wenn ein
		 * User oder ein Team im Tree ist, die getName Funktion ausgeführt wird.
		 * 
		 * 
		 * Called by the renderers to convert the specified value to text. This
		 * implementation returns <code>value.toString</code>, ignoring all
		 * other arguments. To control the conversion, subclass this method and
		 * use any of the arguments you need.
		 * 
		 * @param value
		 *            the <code>Object</code> to convert to text
		 * @param selected
		 *            true if the node is selected
		 * @param expanded
		 *            true if the node is expanded
		 * @param leaf
		 *            true if the node is a leaf node
		 * @param row
		 *            an integer specifying the node's display row, where 0 is
		 *            the first row in the display
		 * @param hasFocus
		 *            true if the node has the focus
		 * @return the <code>String</code> representation of the node's value
		 */
		@Override
		public String convertValueToText(Object value, boolean selected,
				boolean expanded, boolean leaf, int row, boolean hasFocus) {
			String ret = "";
			if (value instanceof User) {

				ret = ((User) value).getName();
			} else if (value instanceof Team) {
				ret = ((Team) value).getName();
			} else {
				ret = t.getName();
				;
			}
			return ret;

		}
	}

	/**
	 * Methode zum initialisieren der Komponenten. Macht das was der GUI Editor
	 * nicht macht.
	 */
	@SuppressWarnings("serial")
	public void initLists() {
		// Vorhandene Schiedsrichter
		List<User> lReferees = UserManager.getInstance().getReferees();
		DefaultListModel listModel = new DefaultListModel();
		for (User u : lReferees) {
			listModel.addElement(u);
		}
		jlistReferees.setModel(listModel);

		jlistReferees.setCellRenderer(new DefaultListCellRenderer() {

			/**
			 * Methode die der Liste sagt wie man ein objekt darstellt
			 */
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				if (value instanceof User) {
					setText(((User) value).getName());
				} else {
					setText(value.toString());
				}
				Color background;
				Color foreground;
				// check if this cell is selected
				if (isSelected) {
					background = Color.PINK;
					foreground = Color.WHITE;
				} // unselected, and not the DnD drop location
				else {
					background = Color.WHITE;
					foreground = Color.BLACK;
				}
				;

				setBackground(background);
				setForeground(foreground);
				return this;
			}
		});
		jlistReferees.revalidate();
		// Mehrere Elemente gleichzeitig selektierbar
		jlistReferees.getSelectionModel().setSelectionMode(
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		// Drag&Drop Aktivieren
		jlistReferees.setDragEnabled(true);
		jlistReferees.setDropMode(DropMode.ON_OR_INSERT);
		jlistReferees.setTransferHandler(new TransferHandler() {

			/**
			 * Datentypen die Importiert werden können
			 */
			@Override
			public boolean canImport(TransferHandler.TransferSupport info) {
				JList.DropLocation dl = (JList.DropLocation) info
						.getDropLocation();
				if (dl.getIndex() == -1) {
					return false;
				}
				return true;
			}

			@Override
			public boolean importData(TransferHandler.TransferSupport info) {
				if (!info.isDrop()) { // Wurde nicht abgelegt
					transUser = null;
					return false;
				}
				JList.DropLocation dl = (JList.DropLocation) info
						.getDropLocation();
				DefaultListModel listModel = (DefaultListModel) jlistReferees
						.getModel();
				int index = dl.getIndex();
				boolean insert = dl.isInsert();
				// An richtigen stelle einfügen
				if (insert) {
					listModel.add(index, transUser);
				} else {
					listModel.set(index, transUser);
				}
				return true;
			}

			@Override
			public int getSourceActions(JComponent c) { // Woherkommt die
														// Aktion?
				return COPY;
			}

			// @Override
			public Transferable createTransferable(JComponent c) {
				transUser = (User) jlistReferees.getSelectedValue(); // Objekt
																		// zwischenspeichern
				return new StringSelection("");
			}
		});
		// Angemeldete Schiedsrichter
		List<User> lCReferees = t.getReferees();
		DefaultListModel listModelC = new DefaultListModel();
		for (User u : lCReferees) {
			listModelC.addElement(u);
		}
		jlistConfirmedReferees.setModel(listModelC);
		jlistConfirmedReferees.setCellRenderer(new DefaultListCellRenderer() {

			/**
			 * Methode die der Liste sagt wie man ein objekt darstellt
			 */
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {

				if (value instanceof User) {
					setText(((User) value).getName());
				} else {
					setText(value.toString());
				}
				Color background;
				Color foreground;
				// check if this cell is selected
				if (isSelected) {
					background = Color.PINK;
					foreground = Color.WHITE;
				} // unselected, and not the DnD drop location
				else {
					background = Color.WHITE;
					foreground = Color.BLACK;
				}
				;

				setBackground(background);
				setForeground(foreground);
				return this;
			}
		});
		jlistConfirmedReferees.revalidate();
		// Mehrere Elemente gleichzeitig selektierbar
		jlistConfirmedReferees.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		// Drag&Drop Aktivieren
		jlistConfirmedReferees.setDragEnabled(true);
		jlistConfirmedReferees.setDropMode(DropMode.ON_OR_INSERT);
		jlistConfirmedReferees.setTransferHandler(new TransferHandler() {

			/**
			 * Datentypen die Importiert werden können
			 */
			@Override
			public boolean canImport(TransferHandler.TransferSupport info) {
				JList.DropLocation dl = (JList.DropLocation) info
						.getDropLocation();
				if (dl.getIndex() == -1) {
					return false;
				}
				return true;
			}

			@Override
			public boolean importData(TransferHandler.TransferSupport info) {
				if (!info.isDrop()) { // Wurde nicht abgelegt
					transUser = null;
					return false;
				}
				JList.DropLocation dl = (JList.DropLocation) info
						.getDropLocation();
				DefaultListModel listModel = (DefaultListModel) jlistConfirmedReferees
						.getModel();
				int index = dl.getIndex();
				boolean insert = dl.isInsert();
				// An richtigen stelle einfügen

				if (insert) {
					listModel.add(index, "lala" + transUser);
				} else {
					listModel.set(index, transUser);
				}
				// listConfirmedReferees.remove();
				return true;
			}

			@Override
			public int getSourceActions(JComponent c) { // Woherkommt die
														// Aktion?
				return COPY;
			}

			// @Override
			public Transferable createTransferable(JComponent c) {
				transUser = (User) jlistReferees.getSelectedValue(); // Objekt
																		// zwischenspeichern
				return new StringSelection("");
			}
		});

	}

	private boolean confirmSignUpActionEnabled = false;

	/**
	 * Methode zum festelen ob confirmSignUpActionEnabled true oder false ist
	 * 
	 * @return confirmSignUpActionEnabled
	 */
	public boolean isConfirmSignUpActionEnabled() {
		return confirmSignUpActionEnabled;
	}

	/**
	 * Setter für confrimsignupactionenabled
	 * 
	 * @param b
	 *            boolean true oder false
	 */
	public void setConfirmSignUpActionEnabled(boolean b) {
		boolean old = isConfirmSignUpActionEnabled();
		this.confirmSignUpActionEnabled = b;
		firePropertyChange("confirmSignUpActionEnabled", old,
				isConfirmSignUpActionEnabled());
	}

	private boolean unconfirmSignUpActionEnabled = false;

	/**
	 * Gibt den Status wieder ob Meldungzurücknehmen aktiviert ist
	 * 
	 * @return Status
	 */
	public boolean isUnconfirmSignUpActionEnabled() {
		return unconfirmSignUpActionEnabled;
	}

	/**
	 * Setzt den Status ob Meldungzurücknehmen aktiviert ist
	 * 
	 * @param b
	 *            Status
	 */
	public void setUnconfirmSignUpActionEnabled(boolean b) {
		boolean old = isUnconfirmSignUpActionEnabled();
		this.unconfirmSignUpActionEnabled = b;
		firePropertyChange("unconfirmSignUpActionEnabled", old,
				isUnconfirmSignUpActionEnabled());
	}

	private boolean moveDownActionEnabled = false;

	/**
	 * Gibt den Status wieder ob Hinunterbewegen aktiviert ist
	 * 
	 * @return Status
	 */
	public boolean isMoveDownActionEnabled() {
		return moveDownActionEnabled;
	}

	/**
	 * Setzt den Status ob Hinunterbewegen aktiviert ist
	 * 
	 * @param b
	 *            Status
	 */
	public void setMoveDownActionEnabled(boolean b) {
		boolean old = isMoveDownActionEnabled();
		this.moveDownActionEnabled = b;
		firePropertyChange("moveDownActionEnabled", old,
				isMoveDownActionEnabled());
	}

	private boolean moveUpActionEnabled = false;

	/**
	 * Gibt den Status wieder ob Hinaufbewegen aktiviert ist
	 * 
	 * @return Status
	 */
	public boolean isMoveUpActionEnabled() {
		return moveUpActionEnabled;
	}

	/**
	 * Setzt den Status ob Hinaufbewegen aktiviert ist
	 * 
	 * @param b
	 *            Status
	 */
	public void setMoveUpActionEnabled(boolean b) {
		boolean old = isMoveUpActionEnabled();
		this.moveUpActionEnabled = b;
		firePropertyChange("moveUpActionEnabled", old, isMoveUpActionEnabled());
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnCancelConfirm;
	private javax.swing.JButton btnConfirm;
	private javax.swing.JButton btnDown;
	private javax.swing.JButton btnUp;
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JSplitPane jSplitPane1;
	private javax.swing.JList jlistConfirmedReferees;
	private javax.swing.JList jlistReferees;
	private javax.swing.JTree treeTeamsPlayers;
	private org.jdesktop.beansbinding.BindingGroup bindingGroup;

	// End of variables declaration//GEN-END:variables

	/**
	 * Klasse MyTreeCellRenderer Ist underklasse von DefaultTreeCelLRenderer
	 * Dient der schönen Darstellung eines Teamtrees
	 */
	private class MyTreeCellRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = 1756520085224797964L;

		/**
		 * Methode zum erstellen eines renderer. Dient der Darstellung eines
		 * Jtree.
		 * 
		 * @param tree
		 *            the Jtree
		 * @param value
		 *            object to draw
		 * @param sel
		 *            is item selected
		 * @param expanded
		 *            is item expanded
		 * @param leaf
		 *            is item a leaf
		 * @param row
		 *            of the item
		 * @param hasFocus
		 *            does item have focus
		 * @return This renderer.
		 * 
		 */
		public Component getTreeCellRendererComponent(JTree tree, Object value,
				boolean sel, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {

			super.getTreeCellRendererComponent(tree, value, sel, expanded,
					leaf, row, hasFocus);
			if (leaf && value instanceof User) {
				Team t = getTeamUserIsIn((User) value);
				if (t != null && isUserConfirmed((User) value, t)) {
					setBackground(Color.GREEN);
					setForeground(Color.GREEN);
				}

			} else if (value instanceof Team) {
				Team thisteam = (Team) value;

				// Zähle die Anzahl der bereits bestätigten Benutzer im Team
				int confirmed = 0;
				for (User p : thisteam.getPlayers()) {
					confirmed += confirmedPlayers.contains(p) ? 1 : 0;

					// Falls die untere Grenze erreicht wurde, richtige Farbe
					// setzen.
					if (confirmed == t.getRequiredPlayersPerTeam()) {
						setBackground(Color.GREEN);
						setForeground(Color.GREEN);
					}
				}
			} else {
				setToolTipText(null); // no tool tip
			}

			return this;
		}

		/**
		 * Method to check if the User is confirmed.
		 * 
		 * @param u
		 *            the user
		 * @param t
		 *            the team the user is in
		 * @return is the user confirmed?
		 */
		public boolean isUserConfirmed(User u, Team t) {

			return confirmedPlayers.contains(u);

		}
	}

	/**
	 * Action die die voranmeldung des users bestätigt.
	 */
	@Action(enabledProperty = "confirmSignUpActionEnabled")
	public void confirmSignUpAction() {
		if (treeTeamsPlayers.getSelectionPath() == null) {
			// wird nichts gemacht
		} else if (treeTeamsPlayers.getSelectionPath().getLastPathComponent() instanceof User) {
			User u = (User) treeTeamsPlayers.getSelectionPath()
					.getLastPathComponent();
			confirmedPlayers.add(u);
		} else if (treeTeamsPlayers.getSelectionPath().getLastPathComponent() instanceof Team) {
			Team team = (Team) treeTeamsPlayers.getSelectionPath()
					.getLastPathComponent();
			confirmedPlayers.addAll(team.getPlayers());
		}
		treeTeamsPlayers.repaint();
	}

	/**
	 * Method to get the Team the user is in.
	 * 
	 * @param u
	 *            the User
	 * @return the Team the user is in
	 */
	public Team getTeamUserIsIn(User u) {
		// List<Team> lt = teamS;
		List<Team> lt = t.getTeams();
		for (Team t : lt) {
			if (t.getPlayers().contains(u)) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Liste der Schiedsrichter
	 */
	protected List<User> refereeList;

	/**
	 * Get the value of refereeList
	 * 
	 * @return the value of refereeList
	 */
	public List<User> getRefereeList() {
		return refereeList;
	}

	/**
	 * dem Turnier zugewiesene Schiedsrichter
	 */
	protected List<User> assignedRefereeList;

	/**
	 * Get the value of assignedRefereeList
	 * 
	 * @return the value of assignedRefereeList
	 */
	public List<User> getAssignedRefereeList() {
		return assignedRefereeList;
	}

	/**
	 * Ausgewählte Schiedsrichter
	 */
	protected List<User> selectedReferees;

	/**
	 * Get the value of selectedReferees
	 * 
	 * @return the value of selectedReferees
	 */
	public List<User> getSelectedReferees() {
		return selectedReferees;
	}

	/**
	 * Set the value of selectedReferees
	 * 
	 * @param selectedReferees
	 *            new value of selectedReferees
	 */
	public void setSelectedReferees(List<User> selectedReferees) {
		this.selectedReferees = selectedReferees;
	}

	/**
     *
     */
	@Action
	public void confirmRefereeAction() {

		for (User u : confirmedPlayers) {
			Team team = getTeamUserIsIn(u);
			if (team != null) {
				if (!team.getConfirmedPlayers().contains(u)) {
					TournamentManager.getInstance().confirmPlayer(team, u);
				}
			}
		}
		List<Team> teams = t.getTeams();
		ArrayList<User> alUser = new ArrayList<User>();
		for (Team team : teams) {
			List<User> teamConfirmedPlayers = team.getConfirmedPlayers();
			for (User u : teamConfirmedPlayers) {
				if (!confirmedPlayers.contains(u)) {
					alUser.add(u);
				}
			}
		}
		for (User u : alUser) {
			Team team = getTeamUserIsIn(u);
			TournamentManager.getInstance().confirmPlayer(team, u, false);
		}
		TournamentManager.getInstance().setReferees(t, assignedRefereeList);

		fireCallbackEvent(true);
	}

	/**
     *
     */
	@Action
	public void cancelAction() {
		fireCallbackEvent(false);
	}

	/**
	 * Bestätigung zurücknehmen
	 */
	@Action(enabledProperty = "unconfirmSignUpActionEnabled")
	public void unconfirmSignUpAction() {

		if (treeTeamsPlayers.getSelectionPath() == null) {
		} else if (treeTeamsPlayers.getSelectionPath().getLastPathComponent() instanceof User) {
			User u = (User) treeTeamsPlayers.getSelectionPath()
					.getLastPathComponent();
			confirmedPlayers.remove(u);
		} else if (treeTeamsPlayers.getSelectionPath().getLastPathComponent() instanceof Team) {
			Team team = (Team) treeTeamsPlayers.getSelectionPath()
					.getLastPathComponent();
			confirmedPlayers.removeAll(team.getPlayers());
		}
		treeTeamsPlayers.repaint();

	}

	/**
	 * Hinunterbewegen
	 */
	@Action(enabledProperty = "moveDownActionEnabled")
	public void moveDownAction() {
		assignedRefereeList.add(selectedReferees.get(0));
		refereeList.remove(selectedReferees.get(0));
	}

	/**
	 * Hinaufbewegen
	 */
	@Action(enabledProperty = "moveUpActionEnabled")
	public void moveUpAction() {
		refereeList.add(selectedAssignedReferees.get(0));
		assignedRefereeList.remove(selectedAssignedReferees.get(0));
	}

	/**
	 * Markierte hinzugefügte Schiedsrichter
	 */
	protected List<User> selectedAssignedReferees;

	/**
	 * Get the value of selectedAssignedReferees
	 * 
	 * @return the value of selectedAssignedReferees
	 */
	public List<User> getSelectedAssignedReferees() {
		return selectedAssignedReferees;
	}

	/**
	 * Set the value of selectedAssignedReferees
	 * 
	 * @param selectedAssignedReferees
	 *            new value of selectedAssignedReferees
	 */
	public void setSelectedAssignedReferees(List<User> selectedAssignedReferees) {
		this.selectedAssignedReferees = selectedAssignedReferees;
	}
}
