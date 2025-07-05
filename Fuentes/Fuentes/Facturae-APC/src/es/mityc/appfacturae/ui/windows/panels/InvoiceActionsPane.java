/**
 * Copyright 2015 Ministerio de Industria, Energía y Turismo
 *
 * Este fichero es parte de "Facturae-APC".
 *
 * Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por la Comisión Europea– versiones posteriores de la EUPL (la Licencia);
 * Solo podrá usarse esta obra si se respeta la Licencia.
 *
 * Puede obtenerse una copia de la Licencia en:
 *
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL»,
 * SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas.
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia.
 */
package es.mityc.appfacturae.ui.windows.panels;

import java.awt.event.MouseAdapter;

import javax.swing.JButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.mityc.appfacturae.ui.components.TranslucentPanel;
import es.mityc.appfacturae.ui.components.TransparentTabbedPane;
import es.mityc.appfacturae.ui.windows.MainWindow;
import es.mityc.appfacturae.utils.constants.Constants;

public class InvoiceActionsPane extends TransparentTabbedPane {
	
	private static Log logger = LogFactory.getLog(InvoiceActionsPane.class);
	
	public InvoiceActionsPane(){
		
		logger.info("TabbedPane");
		initComponents();
		
	}

	private void initComponents(){
		jPanelDraft = new TranslucentPanel();
        jButtonDSee = new javax.swing.JButton();
        jButtonDAtt = new javax.swing.JButton();
        jButtonDEdi = new javax.swing.JButton();
        jButtonDSig = new javax.swing.JButton();
        jButtonDXML = new javax.swing.JButton();
        jPanelIssued = new TranslucentPanel();
        jButtonIAtt = new javax.swing.JButton();
        jButtonIRec = new javax.swing.JButton();
        jButtonISen = new javax.swing.JButton();
        jButtonIXML = new javax.swing.JButton();
        jButtonISee = new javax.swing.JButton();
        jPanelSent = new TranslucentPanel();
        jButtonSSee = new javax.swing.JButton();
        jButtonSAtt = new javax.swing.JButton();
        jButtonSRec = new javax.swing.JButton();
        jButtonSRSe = new javax.swing.JButton();
        jButtonSXML = new javax.swing.JButton();
        jButtonSCancel = new javax.swing.JButton();
        jPanelReceived = new TranslucentPanel();
        jButtonRSee = new javax.swing.JButton();
        jButtonRAtt = new javax.swing.JButton();
        jButtonRXML = new javax.swing.JButton();
        jPanelCanceled = new TranslucentPanel();
        jButtonCSee =  new javax.swing.JButton();
        jButtonCAtt =  new javax.swing.JButton();
        jButtonCXML =  new javax.swing.JButton();
        jPanelRecDraft = new TranslucentPanel();
        jButtonRDSee = new javax.swing.JButton();
        jButtonRDAtt = new javax.swing.JButton();
        jButtonRDEdi = new javax.swing.JButton();
        jButtonRDSig = new javax.swing.JButton();
        jButtonRDXML = new javax.swing.JButton();
        jButtonRDSeeR = new javax.swing.JButton();
        jPanelRecIssued = new TranslucentPanel();
        jButtonRISee = new javax.swing.JButton();
        jButtonRIAtt = new javax.swing.JButton();
        jButtonRISen = new javax.swing.JButton();
        jButtonRISeeR = new javax.swing.JButton();
        jButtonRIXML = new javax.swing.JButton();
        jPanelRecReceived = new TranslucentPanel();
        jButtonRSSee = new javax.swing.JButton();
        jButtonRSAtt = new javax.swing.JButton();
        jButtonRSRSe = new javax.swing.JButton();
        jButtonRSXML = new javax.swing.JButton();
        jButtonRSSeeR = new javax.swing.JButton();
        jButtonRSCancel = new javax.swing.JButton();
        jPanelRecCanceled = new TranslucentPanel();
        jButtonRCSee =  new javax.swing.JButton();
        jButtonRCAtt =  new javax.swing.JButton();
        jButtonRCXML =  new javax.swing.JButton();
        
        jButtonDSee.setFont(Constants.TITLE_FONT);
        jButtonDSee.setText(Constants.LANG.getString("Visualize")); 
        jButtonDSee.setForeground(Constants.FONT_COLOR);
        jButtonDSee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonDSee.setBorderPainted(false);
        jButtonDSee.setContentAreaFilled(false);
        jButtonDSee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).chargeInvoice(true);
            }
        });
        jButtonDSee.addMouseListener(new ButtonCursor());

        jButtonDAtt.setFont(Constants.TITLE_FONT);
        jButtonDAtt.setText(Constants.LANG.getString("Attachments")); 
        jButtonDAtt.setForeground(Constants.FONT_COLOR);
        jButtonDAtt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonDAtt.setBorderPainted(false);
        jButtonDAtt.setContentAreaFilled(false);
        jButtonDAtt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeAttachment();
            }
        });
        jButtonDAtt.addMouseListener(new ButtonCursor());

        jButtonDEdi.setFont(Constants.TITLE_FONT);
        jButtonDEdi.setText(Constants.LANG.getString("Edit")); 
        jButtonDEdi.setForeground(Constants.FONT_COLOR);
        jButtonDEdi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonDEdi.setBorderPainted(false);
        jButtonDEdi.setContentAreaFilled(false);
        jButtonDEdi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).chargeInvoice(null);
            }
        });
        jButtonDEdi.addMouseListener(new ButtonCursor());

        jButtonDSig.setFont(Constants.TITLE_FONT);
        jButtonDSig.setText(Constants.LANG.getString("Sign")); 
        jButtonDSig.setForeground(Constants.FONT_COLOR);
        jButtonDSig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonDSig.setBorderPainted(false);
        jButtonDSig.setContentAreaFilled(false);
        jButtonDSig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).signInvoice(false);
            }
        });
        jButtonDSig.addMouseListener(new ButtonCursor());

        jButtonDXML.setFont(Constants.TITLE_FONT);
        jButtonDXML.setText(Constants.LANG.getString("SeeXML")); 
        jButtonDXML.setForeground(Constants.FONT_COLOR);
        jButtonDXML.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonDXML.setBorderPainted(false);
        jButtonDXML.setContentAreaFilled(false);
        jButtonDXML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeXML();
            }
        });
        jButtonDXML.addMouseListener(new ButtonCursor());

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanelDraft);
        jPanelDraft.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(30, 30, 30)
                .add(jButtonDSee)
                .add(35, 35, 35)
                .add(jButtonDAtt)
                .add(35, 35, 35)
                .add(jButtonDEdi)
                .add(35, 35, 35)
                .add(jButtonDSig)
                .add(35, 35, 35)
                .add(jButtonDXML)
                .add(30, 30, 30))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(jButtonDXML)
                    .add(jButtonDSee)
                    .add(jButtonDAtt)
                    .add(jButtonDSig)
                    .add(jButtonDEdi))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        this.addTab(Constants.LANG.getString("DraftStr"), jPanelDraft); 

        jButtonIAtt.setFont(Constants.TITLE_FONT);
        jButtonIAtt.setText(Constants.LANG.getString("Attachments")); 
        jButtonIAtt.setForeground(Constants.FONT_COLOR);
        jButtonIAtt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonIAtt.setBorderPainted(false);
        jButtonIAtt.setContentAreaFilled(false);
        jButtonIAtt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeAttachment();
            }
        });
        jButtonIAtt.addMouseListener(new ButtonCursor());

        jButtonIRec.setFont(Constants.TITLE_FONT);
        jButtonIRec.setText(Constants.LANG.getString("Correct")); 
        jButtonIRec.setForeground(Constants.FONT_COLOR);
        jButtonIRec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonIRec.setBorderPainted(false);
        jButtonIRec.setContentAreaFilled(false);
        jButtonIRec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).chargeInvoice(false);
            }
        });
        jButtonIRec.addMouseListener(new ButtonCursor());

        jButtonISen.setFont(Constants.TITLE_FONT);
        jButtonISen.setText(Constants.LANG.getString("Send")); 
        jButtonISen.setForeground(Constants.FONT_COLOR);
        jButtonISen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonISen.setBorderPainted(false);
        jButtonISen.setContentAreaFilled(false);
        jButtonISen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).sendInvoice();
            }
        });
        jButtonISen.addMouseListener(new ButtonCursor());

        jButtonIXML.setFont(Constants.TITLE_FONT);
        jButtonIXML.setText(Constants.LANG.getString("SeeXML")); 
        jButtonIXML.setForeground(Constants.FONT_COLOR);
        jButtonIXML.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonIXML.setBorderPainted(false);
        jButtonIXML.setContentAreaFilled(false);
        jButtonIXML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeXML();
            }
        });
        jButtonIXML.addMouseListener(new ButtonCursor());

        jButtonISee.setFont(Constants.TITLE_FONT);
        jButtonISee.setText(Constants.LANG.getString("Visualize")); 
        jButtonISee.setForeground(Constants.FONT_COLOR);
        jButtonISee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonISee.setBorderPainted(false);
        jButtonISee.setContentAreaFilled(false);
        jButtonISee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).chargeInvoice(true);
            }
        });
        jButtonISee.addMouseListener(new ButtonCursor());

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanelIssued);
        jPanelIssued.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(30, 30, 30)
                .add(jButtonISee)
                .add(30, 30, 30)
                .add(jButtonIAtt)
                .add(35, 35, 35)
                .add(jButtonIRec)
                .add(30, 30, 30)
                .add(jButtonISen)
                .add(25, 25, 25)
                .add(jButtonIXML)
                .add(30, 30, 30))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(jButtonIXML)
                    .add(jButtonIAtt)
                    .add(jButtonISee)
                    .add(jButtonIRec)
                    .add(jButtonISen))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        this.addTab(Constants.LANG.getString("Issued"), jPanelIssued); 

        jButtonSSee.setFont(Constants.TITLE_FONT);
        jButtonSSee.setText(Constants.LANG.getString("Visualize")); 
        jButtonSSee.setForeground(Constants.FONT_COLOR);
        jButtonSSee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonSSee.setBorderPainted(false);
        jButtonSSee.setContentAreaFilled(false);
        jButtonSSee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).chargeInvoice(true);
            }
        });
        jButtonSSee.addMouseListener(new ButtonCursor());

        jButtonSAtt.setFont(Constants.TITLE_FONT);
        jButtonSAtt.setText(Constants.LANG.getString("Attachments")); 
        jButtonSAtt.setForeground(Constants.FONT_COLOR);
        jButtonSAtt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonSAtt.setBorderPainted(false);
        jButtonSAtt.setContentAreaFilled(false);
        jButtonSAtt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeAttachment();
            }
        });
        jButtonSAtt.addMouseListener(new ButtonCursor());

        jButtonSRec.setFont(Constants.TITLE_FONT);
        jButtonSRec.setText(Constants.LANG.getString("Correct")); 
        jButtonSRec.setForeground(Constants.FONT_COLOR);
        jButtonSRec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonSRec.setBorderPainted(false);
        jButtonSRec.setContentAreaFilled(false);
        jButtonSRec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).chargeInvoice(false);
            }
        });
        jButtonSRec.addMouseListener(new ButtonCursor());

        jButtonSRSe.setFont(Constants.TITLE_FONT);
        jButtonSRSe.setText(Constants.LANG.getString("Resend")); 
        jButtonSRSe.setForeground(Constants.FONT_COLOR);
        jButtonSRSe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonSRSe.setBorderPainted(false);
        jButtonSRSe.setContentAreaFilled(false);
        jButtonSRSe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).sendInvoice();
            }
        });
        jButtonSRSe.addMouseListener(new ButtonCursor());

        jButtonSXML.setFont(Constants.TITLE_FONT);
        jButtonSXML.setText(Constants.LANG.getString("SeeXML")); 
        jButtonSXML.setForeground(Constants.FONT_COLOR);
        jButtonSXML.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonSXML.setBorderPainted(false);
        jButtonSXML.setContentAreaFilled(false);
        jButtonSXML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeXML();
            }
        });
        jButtonSXML.addMouseListener(new ButtonCursor());
        
        jButtonSCancel.setFont(Constants.TITLE_FONT);
        jButtonSCancel.setText(Constants.LANG.getString("CancelInvoice")); 
        jButtonSCancel.setForeground(Constants.FONT_COLOR);
        jButtonSCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonSCancel.setBorderPainted(false);
        jButtonSCancel.setContentAreaFilled(false);
        jButtonSCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).cancelInvoice();
            }
        });
        jButtonSCancel.addMouseListener(new ButtonCursor());

        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanelSent);
        jPanelSent.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .add(5, 5, 5)
                .add(jButtonSSee)
                .add(5, 5, 5)
                .add(jButtonSAtt)
                .add(5, 5, 5)
                .add(jButtonSRec)
                .add(5, 5, 5)
                .add(jButtonSRSe)
                .add(5, 5, 5)
                .add(jButtonSXML)
                .add(5, 5, 5)
                .add(jButtonSCancel)
                .add(5, 5, 5))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                	.add(jButtonSCancel)
                	.add(jButtonSXML)
                    .add(jButtonSSee)
                    .add(jButtonSAtt)
                    .add(jButtonSRec)
                    .add(jButtonSRSe))
                .addContainerGap(22, Short.MAX_VALUE))
        );


        this.addTab(Constants.LANG.getString("Sent"), jPanelSent); 

        jButtonRSee.setFont(Constants.TITLE_FONT);
        jButtonRSee.setText(Constants.LANG.getString("Visualize")); 
        jButtonRSee.setForeground(Constants.FONT_COLOR);
        jButtonRSee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRSee.setBorderPainted(false);
        jButtonRSee.setContentAreaFilled(false);
        jButtonRSee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).chargeInvoice(true);
            }
        });
        jButtonRSee.addMouseListener(new ButtonCursor());

        jButtonRAtt.setFont(Constants.TITLE_FONT);
        jButtonRAtt.setText(Constants.LANG.getString("Attachments")); 
        jButtonRAtt.setForeground(Constants.FONT_COLOR);
        jButtonRAtt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRAtt.setBorderPainted(false);
        jButtonRAtt.setContentAreaFilled(false);
        jButtonRAtt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeAttachment();
            }
        });
        jButtonRAtt.addMouseListener(new ButtonCursor());

        jButtonRXML.setFont(Constants.TITLE_FONT);
        jButtonRXML.setText(Constants.LANG.getString("SeeXML")); 
        jButtonRXML.setForeground(Constants.FONT_COLOR);
        jButtonRXML.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRXML.setBorderPainted(false);
        jButtonRXML.setContentAreaFilled(false);
        jButtonRXML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeXML();
            }
        });
        jButtonRXML.addMouseListener(new ButtonCursor());

        org.jdesktop.layout.GroupLayout jPanel10Layout = new org.jdesktop.layout.GroupLayout(jPanelReceived);
        jPanelReceived.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .add(70, 70, 70)
                .add(jButtonRSee)
                .add(120, 120, 120)
                .add(jButtonRAtt)
                .add(120, 120, 120)
                .add(jButtonRXML)
                .add(70, 70, 70))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .add(21, 21, 21)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(jButtonRXML)
                    .add(jButtonRSee)
                    .add(jButtonRAtt))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        this.addTab(Constants.LANG.getString("ReceivedStr"), jPanelReceived); 

        jButtonCSee.setFont(Constants.TITLE_FONT);
        jButtonCSee.setText(Constants.LANG.getString("Visualize")); 
        jButtonCSee.setForeground(Constants.FONT_COLOR);
        jButtonCSee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonCSee.setBorderPainted(false);
        jButtonCSee.setContentAreaFilled(false);
        jButtonCSee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).chargeInvoice(true);
            }
        });
        jButtonCSee.addMouseListener(new ButtonCursor());

        jButtonCAtt.setFont(Constants.TITLE_FONT);
        jButtonCAtt.setText(Constants.LANG.getString("Attachments")); 
        jButtonCAtt.setForeground(Constants.FONT_COLOR);
        jButtonCAtt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonCAtt.setBorderPainted(false);
        jButtonCAtt.setContentAreaFilled(false);
        jButtonCAtt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeAttachment();
            }
        });
        jButtonCAtt.addMouseListener(new ButtonCursor());

        jButtonCXML.setFont(Constants.TITLE_FONT);
        jButtonCXML.setText(Constants.LANG.getString("SeeXML")); 
        jButtonCXML.setForeground(Constants.FONT_COLOR);
        jButtonCXML.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonCXML.setBorderPainted(false);
        jButtonCXML.setContentAreaFilled(false);
        jButtonCXML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeXML();
            }
        });
        jButtonCXML.addMouseListener(new ButtonCursor());

        org.jdesktop.layout.GroupLayout jPanelCanceledLayout = new org.jdesktop.layout.GroupLayout(jPanelCanceled);
        jPanelCanceled.setLayout(jPanelCanceledLayout);
        jPanelCanceledLayout.setHorizontalGroup(
        		jPanelCanceledLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelCanceledLayout.createSequentialGroup()
                .add(70, 70, 70)
                .add(jButtonCSee)
                .add(120, 120, 120)
                .add(jButtonCAtt)
                .add(120, 120, 120)
                .add(jButtonCXML)
                .add(70, 70, 70))
        );
        jPanelCanceledLayout.setVerticalGroup(
        		jPanelCanceledLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelCanceledLayout.createSequentialGroup()
                .add(21, 21, 21)
                .add(jPanelCanceledLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(jButtonCXML)
                    .add(jButtonCSee)
                    .add(jButtonCAtt)
                    )
                .addContainerGap(21, Short.MAX_VALUE))
        );

        this.addTab(Constants.LANG.getString("Canceled"), jPanelCanceled);
        
        jButtonRDSee.setFont(Constants.TITLE_FONT);
        jButtonRDSee.setText(Constants.LANG.getString("Visualize")); 
        jButtonRDSee.setForeground(Constants.FONT_COLOR);
        jButtonRDSee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRDSee.setBorderPainted(false);
        jButtonRDSee.setContentAreaFilled(false);
        jButtonRDSee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).chargeInvoice(true);
            }
        });
        jButtonRDSee.addMouseListener(new ButtonCursor());

        jButtonRDAtt.setFont(Constants.TITLE_FONT);
        jButtonRDAtt.setText(Constants.LANG.getString("Attachments")); 
        jButtonRDAtt.setForeground(Constants.FONT_COLOR);
        jButtonRDAtt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRDAtt.setBorderPainted(false);
        jButtonRDAtt.setContentAreaFilled(false);
        jButtonRDAtt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeAttachment();
            }
        });
        jButtonRDAtt.addMouseListener(new ButtonCursor());

        jButtonRDEdi.setFont(Constants.TITLE_FONT);
        jButtonRDEdi.setText(Constants.LANG.getString("Edit")); 
        jButtonRDEdi.setForeground(Constants.FONT_COLOR);
        jButtonRDEdi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRDEdi.setBorderPainted(false);
        jButtonRDEdi.setContentAreaFilled(false);
        jButtonRDEdi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).chargeInvoice(false);
            }
        });
        jButtonRDEdi.addMouseListener(new ButtonCursor());

        jButtonRDSig.setFont(Constants.TITLE_FONT);
        jButtonRDSig.setText(Constants.LANG.getString("Sign")); 
        jButtonRDSig.setForeground(Constants.FONT_COLOR);
        jButtonRDSig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRDSig.setBorderPainted(false);
        jButtonRDSig.setContentAreaFilled(false);
        jButtonRDSig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).signInvoice(true);
            }
        });
        jButtonRDSig.addMouseListener(new ButtonCursor());

        jButtonRDXML.setFont(Constants.TITLE_FONT);
        jButtonRDXML.setText(Constants.LANG.getString("SeeXML")); 
        jButtonRDXML.setForeground(Constants.FONT_COLOR);
        jButtonRDXML.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRDXML.setBorderPainted(false);
        jButtonRDXML.setContentAreaFilled(false);
        jButtonRDXML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeXML();
            }
        });
        jButtonRDXML.addMouseListener(new ButtonCursor());
        
        jButtonRDSeeR.setFont(Constants.TITLE_FONT);
        jButtonRDSeeR.setText(Constants.LANG.getString("CorrectedOptions")); 
        jButtonRDSeeR.setForeground(Constants.FONT_COLOR);
        jButtonRDSeeR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRDSeeR.setBorderPainted(false);
        jButtonRDSeeR.setContentAreaFilled(false);
        jButtonRDSeeR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeCorrections();
            }
        });
        jButtonRDSeeR.addMouseListener(new ButtonCursor());

        org.jdesktop.layout.GroupLayout jPanel11Layout = new org.jdesktop.layout.GroupLayout(jPanelRecDraft);
        jPanelRecDraft.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel11Layout.createSequentialGroup()
                .add(8, 8, 8)
                .add(jButtonRDSee)
                .add(3, 3, 3)
                .add(jButtonRDAtt)
                .add(3, 3, 3)
                .add(jButtonRDEdi)
                .add(8, 8, 8)
                .add(jButtonRDSig)
                .add(8, 8, 8)
                .add(jButtonRDXML)
                .add(3, 3, 3)
                .add(jButtonRDSeeR)
                .add(8, 8, 8))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel11Layout.createSequentialGroup()
                .add(21, 21, 21)
                .add(jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(jButtonRDSee)
                    .add(jButtonRDSeeR)
                    .add(jButtonRDXML)
                    .add(jButtonRDSig)
                    .add(jButtonRDEdi)
                    .add(jButtonRDAtt))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        this.addTab(Constants.LANG.getString("CorrectiveDraft"), jPanelRecDraft); 

        jButtonRISee.setFont(Constants.TITLE_FONT);
        jButtonRISee.setText(Constants.LANG.getString("Visualize")); 
        jButtonRISee.setForeground(Constants.FONT_COLOR);
        jButtonRISee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRISee.setBorderPainted(false);
        jButtonRISee.setContentAreaFilled(false);
        jButtonRISee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).chargeInvoice(true);
            }
        });
        jButtonRISee.addMouseListener(new ButtonCursor());
        
        jButtonRIAtt.setFont(Constants.TITLE_FONT);
        jButtonRIAtt.setText(Constants.LANG.getString("Attachments")); 
        jButtonRIAtt.setForeground(Constants.FONT_COLOR);
        jButtonRIAtt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRIAtt.setBorderPainted(false);
        jButtonRIAtt.setContentAreaFilled(false);
        jButtonRIAtt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeAttachment();
            }
        });
        jButtonRIAtt.addMouseListener(new ButtonCursor());

        jButtonRISen.setFont(Constants.TITLE_FONT);
        jButtonRISen.setText(Constants.LANG.getString("Send")); 
        jButtonRISen.setForeground(Constants.FONT_COLOR);
        jButtonRISen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRISen.setBorderPainted(false);
        jButtonRISen.setContentAreaFilled(false);
        jButtonRISen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).sendInvoice();
            }
        });
        jButtonRISen.addMouseListener(new ButtonCursor());

        jButtonRISeeR.setFont(Constants.TITLE_FONT);
        jButtonRISeeR.setText(Constants.LANG.getString("CorrectedOptions")); 
        jButtonRISeeR.setForeground(Constants.FONT_COLOR);
        jButtonRISeeR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRISeeR.setBorderPainted(false);
        jButtonRISeeR.setContentAreaFilled(false);
        jButtonRISeeR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeCorrections();
            }
        });
        jButtonRISeeR.addMouseListener(new ButtonCursor());

        jButtonRIXML.setFont(Constants.TITLE_FONT);
        jButtonRIXML.setText(Constants.LANG.getString("SeeXML")); 
        jButtonRIXML.setForeground(Constants.FONT_COLOR);
        jButtonRIXML.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRIXML.setBorderPainted(false);
        jButtonRIXML.setContentAreaFilled(false);
        jButtonRIXML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeXML();
            }
        });
        jButtonRIXML.addMouseListener(new ButtonCursor());

        org.jdesktop.layout.GroupLayout jPanel12Layout = new org.jdesktop.layout.GroupLayout(jPanelRecIssued);
        jPanelRecIssued.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel12Layout.createSequentialGroup()
            	.add(23, 23, 23)
                .add(jButtonRISee)
                .add(20, 20, 20)
                .add(jButtonRIAtt)
                .add(20, 20, 20)
                .add(jButtonRISen)
                .add(20, 20, 20)
                .add(jButtonRIXML)
                .add(20, 20, 20)
                .add(jButtonRISeeR)
                .add(20, 20, 20))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel12Layout.createSequentialGroup()
                .add(21, 21, 21)
                .add(jPanel12Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(jButtonRIAtt)
                    .add(jButtonRISeeR)
                    .add(jButtonRIXML)
                    .add(jButtonRISen)
                    .add(jButtonRISee))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        this.addTab(Constants.LANG.getString("CorrectiveIssued"), jPanelRecIssued); 

        jButtonRSSee.setFont(Constants.TITLE_FONT);
        jButtonRSSee.setText(Constants.LANG.getString("Visualize")); 
        jButtonRSSee.setForeground(Constants.FONT_COLOR);
        jButtonRSSee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRSSee.setBorderPainted(false);
        jButtonRSSee.setContentAreaFilled(false);
        jButtonRSSee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).chargeInvoice(true);
            }
        });
        jButtonRSSee.addMouseListener(new ButtonCursor());

        jButtonRSAtt.setFont(Constants.TITLE_FONT);
        jButtonRSAtt.setText(Constants.LANG.getString("Attachments")); 
        jButtonRSAtt.setForeground(Constants.FONT_COLOR);
        jButtonRSAtt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRSAtt.setBorderPainted(false);
        jButtonRSAtt.setContentAreaFilled(false);
        jButtonRSAtt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeAttachment();
            }
        });
        jButtonRSAtt.addMouseListener(new ButtonCursor());

        jButtonRSRSe.setFont(Constants.TITLE_FONT);
        jButtonRSRSe.setText(Constants.LANG.getString("Resend")); 
        jButtonRSRSe.setForeground(Constants.FONT_COLOR);
        jButtonRSRSe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRSRSe.setBorderPainted(false);
        jButtonRSRSe.setContentAreaFilled(false);
        jButtonRSRSe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).sendInvoice();
            }
        });
        jButtonRSRSe.addMouseListener(new ButtonCursor());

        jButtonRSXML.setFont(Constants.TITLE_FONT);
        jButtonRSXML.setText(Constants.LANG.getString("SeeXML")); 
        jButtonRSXML.setForeground(Constants.FONT_COLOR);
        jButtonRSXML.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRSXML.setBorderPainted(false);
        jButtonRSXML.setContentAreaFilled(false);
        jButtonRSXML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeXML();
            }
        });
        jButtonRSXML.addMouseListener(new ButtonCursor());

        jButtonRSSeeR.setFont(Constants.TITLE_FONT);
        jButtonRSSeeR.setText(Constants.LANG.getString("CorrectedOptions")); 
        jButtonRSSeeR.setForeground(Constants.FONT_COLOR);
        jButtonRSSeeR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRSSeeR.setBorderPainted(false);
        jButtonRSSeeR.setContentAreaFilled(false);
        jButtonRSSeeR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeCorrections();
            }
        });
        jButtonRSSeeR.addMouseListener(new ButtonCursor());

        jButtonRSCancel.setFont(Constants.TITLE_FONT);
        jButtonRSCancel.setText(Constants.LANG.getString("CancelInvoice")); 
        jButtonRSCancel.setForeground(Constants.FONT_COLOR);
        jButtonRSCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRSCancel.setBorderPainted(false);
        jButtonRSCancel.setContentAreaFilled(false);
        jButtonRSCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).cancelInvoice();
            }
        });
        jButtonRSCancel.addMouseListener(new ButtonCursor());
        
        org.jdesktop.layout.GroupLayout jPanel13Layout = new org.jdesktop.layout.GroupLayout(jPanelRecReceived);
        jPanelRecReceived.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel13Layout.createSequentialGroup()
            	.add(5, 5, 5)
                .add(jButtonRSSee)
            	.add(5, 5, 5)
                .add(jButtonRSAtt)
                .add(5, 5, 5)
                .add(jButtonRSRSe)
                .add(5, 5, 5)
                .add(jButtonRSXML)
                .add(5, 5, 5)
                .add(jButtonRSSeeR)
                .add(5, 5, 5)
                .add(jButtonRSCancel)
                .add(5, 5, 5))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel13Layout.createSequentialGroup()
                .add(21, 21, 21)
                .add(jPanel13Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
            		.add(jButtonRSCancel)
            		.add(jButtonRSAtt)
                    .add(jButtonRSSeeR)
                    .add(jButtonRSXML)
                    .add(jButtonRSRSe)
                    .add(jButtonRSSee))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        this.addTab(Constants.LANG.getString("CorrectiveSent"), jPanelRecReceived); 
        
        jButtonRCSee.setFont(Constants.TITLE_FONT);
        jButtonRCSee.setText(Constants.LANG.getString("Visualize")); 
        jButtonRCSee.setForeground(Constants.FONT_COLOR);
        jButtonRCSee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRCSee.setBorderPainted(false);
        jButtonRCSee.setContentAreaFilled(false);
        jButtonRCSee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).chargeInvoice(true);
            }
        });
        jButtonRCSee.addMouseListener(new ButtonCursor());

        jButtonRCAtt.setFont(Constants.TITLE_FONT);
        jButtonRCAtt.setText(Constants.LANG.getString("Attachments")); 
        jButtonRCAtt.setForeground(Constants.FONT_COLOR);
        jButtonRCAtt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRCAtt.setBorderPainted(false);
        jButtonRCAtt.setContentAreaFilled(false);
        jButtonRCAtt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeAttachment();
            }
        });
        jButtonRCAtt.addMouseListener(new ButtonCursor());

        jButtonRCXML.setFont(Constants.TITLE_FONT);
        jButtonRCXML.setText(Constants.LANG.getString("SeeXML")); 
        jButtonRCXML.setForeground(Constants.FONT_COLOR);
        jButtonRCXML.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/optionButton.gif"))); 
        jButtonRCXML.setBorderPainted(false);
        jButtonRCXML.setContentAreaFilled(false);
        jButtonRCXML.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).seeXML();
            }
        });
        jButtonRCXML.addMouseListener(new ButtonCursor());

        org.jdesktop.layout.GroupLayout jPanelRecCanceledLayout = new org.jdesktop.layout.GroupLayout(jPanelRecCanceled);
        jPanelRecCanceled.setLayout(jPanelRecCanceledLayout);
        jPanelRecCanceledLayout.setHorizontalGroup(
        		jPanelRecCanceledLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelRecCanceledLayout.createSequentialGroup()
                .add(70, 70, 70)
                .add(jButtonRCSee)
                .add(120, 120, 120)
                .add(jButtonRCAtt)
                .add(120, 120, 120)
                .add(jButtonRCXML)
                .add(70, 70, 70))
        );
        jPanelRecCanceledLayout.setVerticalGroup(
        		jPanelRecCanceledLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelRecCanceledLayout.createSequentialGroup()
                .add(21, 21, 21)
                .add(jPanelRecCanceledLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                    .add(jButtonRCXML)
                    .add(jButtonRCSee)
                    .add(jButtonRCAtt)
                    )
                .addContainerGap(21, Short.MAX_VALUE))
        );

        this.addTab(Constants.LANG.getString("CorrectiveCanceled"), jPanelRecCanceled);
        
        this.setEnabled(false);
        this.setForeground(Constants.FONT_COLOR);
        this.setFont(new java.awt.Font("Arial", 0, 12));
	}
    
    private class ButtonCursor extends MouseAdapter {
    	public void mouseEntered(java.awt.event.MouseEvent evt) {
    		((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        }
        public void mouseExited(java.awt.event.MouseEvent evt) {
        	((MainWindow)((JButton)evt.getSource()).getTopLevelAncestor()).setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
        }
    }
    
	// JPanel
    private javax.swing.JPanel jPanelDraft;
    private javax.swing.JPanel jPanelIssued;
    private javax.swing.JPanel jPanelSent;
    private javax.swing.JPanel jPanelReceived;
    private javax.swing.JPanel jPanelCanceled;
    private javax.swing.JPanel jPanelRecDraft;
    private javax.swing.JPanel jPanelRecIssued;
    private javax.swing.JPanel jPanelRecReceived;
    private javax.swing.JPanel jPanelRecCanceled;
    // JButton
    private javax.swing.JButton jButtonDSee;
    private javax.swing.JButton jButtonDAtt;
    private javax.swing.JButton jButtonDEdi;
    private javax.swing.JButton jButtonDSig;
    private javax.swing.JButton jButtonDXML;
    private javax.swing.JButton jButtonISee;
    private javax.swing.JButton jButtonIAtt;
    private javax.swing.JButton jButtonIRec;
    private javax.swing.JButton jButtonISen;
    private javax.swing.JButton jButtonIXML;
    private javax.swing.JButton jButtonSSee;
    private javax.swing.JButton jButtonSAtt;
    private javax.swing.JButton jButtonSRec;
    private javax.swing.JButton jButtonSRSe;
    private javax.swing.JButton jButtonSXML;
    private javax.swing.JButton jButtonSCancel;
    private javax.swing.JButton jButtonRSee;
    private javax.swing.JButton jButtonRAtt;
    private javax.swing.JButton jButtonRXML;
    private javax.swing.JButton jButtonCSee;
    private javax.swing.JButton jButtonCAtt;
    private javax.swing.JButton jButtonCXML;
    private javax.swing.JButton jButtonRDSee;
    private javax.swing.JButton jButtonRDAtt;
    private javax.swing.JButton jButtonRDEdi;
    private javax.swing.JButton jButtonRDSig;
    private javax.swing.JButton jButtonRDXML;
    private javax.swing.JButton jButtonRDSeeR;
    private javax.swing.JButton jButtonRISee;
    private javax.swing.JButton jButtonRIAtt;
    private javax.swing.JButton jButtonRISen;
    private javax.swing.JButton jButtonRIXML;
    private javax.swing.JButton jButtonRISeeR;
    private javax.swing.JButton jButtonRSSee;
    private javax.swing.JButton jButtonRSAtt;
    private javax.swing.JButton jButtonRSRSe;
    private javax.swing.JButton jButtonRSXML;
    private javax.swing.JButton jButtonRSSeeR;
    private javax.swing.JButton jButtonRSCancel;
    private javax.swing.JButton jButtonRCSee;
    private javax.swing.JButton jButtonRCAtt;
    private javax.swing.JButton jButtonRCXML;
}