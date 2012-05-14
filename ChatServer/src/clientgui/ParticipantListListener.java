package clientgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ParticipantListListener extends MouseAdapter {
	private JList list;
	private JPopupMenu menu;

	public ParticipantListListener(final ChatWindow window, final JList list) {
		this.list = list;
		this.menu = new JPopupMenu();

		JMenuItem pm = new JMenuItem("Send Private Message");
		pm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				window.joinPrivateRoom((String) list.getSelectedValue());
			}
		});

		JMenuItem file = new JMenuItem("Send File");
		file.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int val = fc.showOpenDialog(list);
				
				if (val == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					window.sendFile((String) list.getSelectedValue(), f);
				}				
			}
		});
		menu.add(pm);
		menu.add(file);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			list.setSelectedIndex(list.locationToIndex(e.getPoint()));
			menu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

}
