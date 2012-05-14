package clientgui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import client.Client;

import message.PartMessage;

public class TabComponent extends JPanel {

	private final JTabbedPane pane;
	private Client client;
	
	public TabComponent(String name, final JTabbedPane pane, Client client) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));

		this.pane = pane;
		this.client = client;
		
		setOpaque(false);

		JLabel label = new JLabel(name);
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		add(label);

		JButton button = new TabButton();
		add(button);

		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
	}

	private class TabButton extends JButton implements ActionListener {

		private TabButton() {
			int size = 17;
			setPreferredSize(new Dimension(size, size));

			setContentAreaFilled(false);
			setFocusable(false);
			setBorder(BorderFactory.createEtchedBorder());
			setBorderPainted(false);
			addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					Component component = e.getComponent();
					if (component instanceof AbstractButton) {
						AbstractButton button = (AbstractButton) component;
						button.setBorderPainted(true);
					}
				}

				public void mouseExited(MouseEvent e) {
					Component component = e.getComponent();
					if (component instanceof AbstractButton) {
						AbstractButton button = (AbstractButton) component;
						button.setBorderPainted(false);
					}
				}
			});
			setRolloverEnabled(true);

			addActionListener(this);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();

			g2.setStroke(new BasicStroke(2));
			g2.setColor(Color.BLACK);

			int delta = 6;
			g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight()
					- delta - 1);
			g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight()
					- delta - 1);
			g2.dispose();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String room = null;
			int i = pane.indexOfTabComponent(TabComponent.this);
			if (i != -1) {
				room = pane.getTitleAt(i);
				pane.remove(i);
			}
			PartMessage msg = new PartMessage();
			msg.setRoom(room.substring(1));
			client.sendMessage(msg);
		}

	}

}