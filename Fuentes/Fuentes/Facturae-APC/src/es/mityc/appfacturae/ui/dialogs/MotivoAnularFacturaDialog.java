package es.mityc.appfacturae.ui.dialogs;

import java.awt.Color;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.mityc.appfacturae.ui.utils.GUIUtils;
import es.mityc.appfacturae.utils.constants.Constants;

public class MotivoAnularFacturaDialog extends JDialog {

	private static final long serialVersionUID = -7509105407360802274L;

	private static Log logger = LogFactory.getLog(MotivoAnularFacturaDialog.class);

//	private static MotivoAnularFacturaDialog motivoWindow = null;

	private Image imgLogoApp = null;

	private JPanel mainPanel;

	private JLabel jLabelMotivo;

	private JTextField jTextFieldMotivo;

	private JButton jButtonAccept;

	private JButton jButtonCancel;
	
	private GUIUtils guiutils;
	
	private Frame parent;
	
	private boolean acceptClicked;

//	/**
//	 * Singleton pattern
//	 */
//	static public MotivoAnularFacturaDialog getInstance() {
//		if (motivoWindow == null)
//			motivoWindow = new MotivoAnularFacturaDialog();
//
//		return motivoWindow;
//	}

	public MotivoAnularFacturaDialog(Frame parent, boolean modal) throws HeadlessException {
		super(parent, modal);

		/** Loading images */
		try {
			imgLogoApp = ImageIO.read(this.getClass().getResourceAsStream("/images/logoapp.jpg"));
		} catch (IOException ioe) {
			logger.error("The Facturae logo could not be loaded: " + ioe.getMessage());
		}

		guiutils = new GUIUtils();
		initComponentes();

		this.pack();
		this.setSize(330, 110);
		this.setResizable(false);
		if (imgLogoApp != null)
			this.setIconImage(imgLogoApp);		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.parent = parent;
		setLocationRelativeTo(parent);
		this.setVisible(true);
	}

	private void initComponentes() {

		mainPanel = new JPanel();
		jLabelMotivo = new JLabel();
		jTextFieldMotivo = new JTextField();
		acceptClicked = false;
		initButtons();

		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowListener() {
			public void windowClosing(WindowEvent e) {
				jButtonCancelActionPerformed();
			}

			public void windowActivated(WindowEvent e) {
			}

			public void windowClosed(WindowEvent e) {
			}

			public void windowDeactivated(WindowEvent e) {
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
			}

			public void windowOpened(WindowEvent e) {
			}
		});

		jTextFieldMotivo.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent arg0) {
				((JTextField) arg0.getSource()).setBackground(Color.white);
				((JTextField) arg0.getSource()).setForeground(Constants.FONT_COLOR);
			}
		});

		setTitle(Constants.LANG.getString("CancelInvoiceWindowTitle"));

		mainPanel.setBackground(Constants.BKG_MAIN_COLOR);

		jLabelMotivo.setFont(Constants.FONT_PLAIN);
		jLabelMotivo.setForeground(Constants.FONT_COLOR);
		jLabelMotivo.setText(Constants.LANG.getString("Reason"));

		jTextFieldMotivo.setFont(Constants.FONT_PLAIN);
		jTextFieldMotivo.setBackground(Color.WHITE);
		jTextFieldMotivo.setHorizontalAlignment(SwingConstants.RIGHT);

		org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
		mainPanel.setLayout(mainPanelLayout);
		mainPanelLayout.setHorizontalGroup(mainPanelLayout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(mainPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
								mainPanelLayout
										.createSequentialGroup()
										.add(jLabelMotivo).add(10, 10, 10)
										.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
										.add(jTextFieldMotivo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 260,
												org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))))
				.add(mainPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
								mainPanelLayout.createSequentialGroup().add(120, 120, 120).add(jButtonAccept, GroupLayout.PREFERRED_SIZE, 35,
										GroupLayout.PREFERRED_SIZE).add(20,20,20).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
										.add(jButtonCancel, GroupLayout.PREFERRED_SIZE, 35,
												GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))));
		mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				mainPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.add(mainPanelLayout
								.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
								.add(jLabelMotivo)
								.add(jTextFieldMotivo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.add(10,10,10)
						.add(mainPanelLayout
								.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
								.add(jButtonAccept, GroupLayout.PREFERRED_SIZE, 32,
										GroupLayout.PREFERRED_SIZE)
								.add(jButtonCancel, GroupLayout.PREFERRED_SIZE, 32,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)));
		this.add(mainPanel);

	}

	private void initButtons() {

		jButtonCancel = new JButton();
		jButtonAccept = new JButton();

		jButtonAccept.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/button_accept.jpg")));
		jButtonAccept.setSize(30, 30);
		jButtonAccept.setToolTipText(Constants.LANG.getString("Accept"));
		jButtonAccept.setBorderPainted(false);
		jButtonAccept.setContentAreaFilled(false);
		jButtonAccept.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				MotivoAnularFacturaDialog.this.mouseEntered(evt);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				MotivoAnularFacturaDialog.this.mouseExited(evt);
			}
		});
		jButtonAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if(guiutils.showConfirmCancelInvoice() == JOptionPane.YES_OPTION){
					acceptClicked = true;
					setVisible(false);
					dispose();
				}
			}
		});

		jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/button_cancel.jpg")));
		jButtonCancel.setToolTipText(Constants.LANG.getString("Cancel"));
		jButtonCancel.setBorderPainted(false);
		jButtonCancel.setContentAreaFilled(false);
		jButtonCancel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				MotivoAnularFacturaDialog.this.mouseEntered(evt);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				MotivoAnularFacturaDialog.this.mouseExited(evt);
			}
		});
		jButtonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jButtonCancelActionPerformed();
			}
		});
	}
	
//	private void jButtonAcceptActionPerformed() {
//		jTextFieldMotivo.setText("");
//		setVisible(false);
//		dispose();
//	}
	
	private void jButtonCancelActionPerformed() {
		acceptClicked = false;
		jTextFieldMotivo.setText("");
		setVisible(false);
		dispose();
	}

	private void mouseEntered(java.awt.event.MouseEvent evt) {
		this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
	}

	private void mouseExited(java.awt.event.MouseEvent evt) {
		this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
	}

	public String getMotivo() {
		return jTextFieldMotivo.getText();
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	public boolean isAcceptClicked() {
		return acceptClicked;
	}
}
