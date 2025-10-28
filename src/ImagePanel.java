import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    private Image backguardStage;

    public ImagePanel(Image backguardStage) {
        this.backguardStage = backguardStage;
    }

    public void setBackguardImage(Image newImage) {
        this.backguardStage = newImage;

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backguardStage != null) {
            g.drawImage(backguardStage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
