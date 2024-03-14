package com.chat;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

public class FileLabel extends JLabel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private FileMessage fileMessage;

    public FileLabel(FileMessage fileMessage) {
        this.fileMessage = fileMessage;

        // TODO: style FileLabel
        this.setText(this.fileMessage.getSender() + " send a file:<U>" + this.fileMessage.getName() + "</U>");
        this.setFont(new Font("consolas", Font.ITALIC | Font.ITALIC, 14));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                fileLabelMouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                fileLabelMouseExited(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    fileLabelMousePressed(e);
                }
                catch(IOException exception) {
                    JOptionPane.showMessageDialog(null, "Can't save file:\n" + exception.getMessage());
                }
            }
        });
    }

    private void fileLabelMouseEntered(MouseEvent e) {
        if(this.isEnabled()) {
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            this.setFont(new Font("consolas", Font.ITALIC | Font.BOLD, 15));
        }
    }

    private void fileLabelMouseExited(MouseEvent e) {
        if(this.isEnabled()) {
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            this.setFont(new Font("consolas", Font.ITALIC | Font.PLAIN, 14));
        }
    }

    private void fileLabelMousePressed(MouseEvent e) throws IOException {
        if(e.getButton() == MouseEvent.BUTTON1) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
            fileChooser.setSelectedFile(new File(this.fileMessage.getName()));
            fileChooser.showSaveDialog(this);

            File dir = fileChooser.getSelectedFile();
            if(dir != null) {
                FileOutputStream fos = new FileOutputStream(dir);
                fos.write(this.fileMessage.getData());
                fos.close();
            }
        }
    }
}
