package clientgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ParticipantListListener extends MouseAdapter {
	private JList list;
	private JPopupMenu menu;

	public ParticipantListListener(JList list) {
		this.list = list;
		this.menu = new JPopupMenu();

		JMenuItem pm = new JMenuItem("Send Private Message");
		pm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("skicka pm!");
			}
		});

		JMenuItem file = new JMenuItem("Send File");
		file.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("skicka fil!");
			}
		});
		menu.add(pm);
		menu.add(file);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			list.setSelectedIndex(list.locationToIndex(e.getPoint()));
			System.out.println(list.getSelectedIndex());
			menu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

}
