package io.github.vmzakharov.ecdataframe.ui;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPanel
extends JPanel
{
    public ButtonPanel()
    {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(Box.createHorizontalGlue());
    }

    public void addButton(JButton button)
    {
        this.add(Box.createHorizontalStrut(10));
        this.add(button);
//        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
    }
}
