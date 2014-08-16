package messengerclient;

import java.util.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ServerSettings extends javax.swing.JFrame{
	
	//public static String SERVER_ADDRESS;//="27.147.218.56";
    //public static int SERVER_PORT;//=12345;
    
	public ServerSettings()
	{
		//myPanel.setLayout(new BorderLayout());
		initComponents();
	}
	 @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myPanel = new javax.swing.JPanel();
        lb_status = new javax.swing.JLabel();
        butt_ok = new javax.swing.JButton();
        butt_cancel = new javax.swing.JButton();
        lb_ip = new javax.swing.JLabel();
        tf_ip = new javax.swing.JTextField();
      	lb_port = new javax.swing.JLabel();
      	tf_port = new javax.swing.JTextField();
      		
		butt_ok.setText("Ok");
        butt_cancel.setText("Cancel");
        setTitle("Server Settings");
        lb_ip.setText("Address:");
		tf_ip.setText("");
		lb_port.setText("Port:");
		tf_port.setText("");	
		
        lb_status.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		//lb_status.setText("Please set the server IP/PORT.");
		
		
		butt_ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butt_okActionPerformed(evt);
            }
        });
        
        butt_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butt_cancelActionPerformed(evt);
            }
        });
		
		
        javax.swing.GroupLayout myPanelLayout = new javax.swing.GroupLayout(myPanel);
        myPanel.setLayout(myPanelLayout);
        myPanelLayout.setHorizontalGroup(
            myPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
        );
        myPanelLayout.setVerticalGroup(
            myPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

		
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup()		
            .addComponent(myPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
            .addGap(10, 10, 10)
            .addComponent(lb_ip) 
            .addGap(10, 10, 10)		         
            .addComponent(tf_ip, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
            .addGap(10, 10, 10)
            .addComponent(lb_port) 
            .addGap(10, 10, 10)		         
            .addComponent(tf_port, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))		
            .addGroup(layout.createSequentialGroup()
            .addGap(50, 50, 50)
            .addComponent(butt_ok) 
            .addGap(10, 10, 10)		         
            .addComponent(butt_cancel))	
            .addGap(30, 30, 30)					
            .addComponent(lb_status))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(myPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                //.addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lb_ip)
                .addComponent(tf_ip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))	
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(lb_port)
                .addComponent(tf_port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))	
                .addGap(10, 10, 10)		
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(butt_ok)
                .addComponent(butt_cancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))	
                .addGap(10, 10, 10)
                .addComponent(lb_status)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    private void butt_okActionPerformed(java.awt.event.ActionEvent evt)
    {
        // javax.swing.JOptionPane.showMessageDialog(this,"Please enter your Server Address ");
        //serverSettings = new ServerSettings();
        //serverSettings.setLocation(550,100);
        this.setVisible(false);
        
    }
    private void butt_cancelActionPerformed(java.awt.event.ActionEvent evt)
    {
        // javax.swing.JOptionPane.showMessageDialog(this,"Please enter your Server Address ");
        //serverSettings = new ServerSettings();
        //serverSettings.setLocation(550,100);
        this.setVisible(false);
        this.tf_ip.setText("");
        this.tf_port.setText("");
    }
    
    public javax.swing.JLabel lb_status;
    public javax.swing.JLabel lb_ip;
    public javax.swing.JTextField tf_ip;
    public javax.swing.JLabel lb_port;
    public javax.swing.JTextField tf_port;
    private javax.swing.JPanel myPanel;
    private javax.swing.JButton butt_ok;
    private javax.swing.JButton butt_cancel;
    
    
    
}
