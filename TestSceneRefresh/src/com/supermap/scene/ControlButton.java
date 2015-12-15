package com.supermap.scene;

import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.JButton;

public class ControlButton extends JButton {
	
	private static final long serialVersionUID = 1L;

	public ControlButton() {
		super();
	}
	
	public ControlButton(String text) {
		super(text);
	}
	/**
	 * 
	 * �����ʼ��ͼ�������
	 * @author zhaosy
	 */
	public ControlButton(String text,Icon icon){
		super(text,icon);
	}
	public ControlButton(Icon icon) {
		super(icon);
	}

	protected void processKeyEvent(KeyEvent event) {
		super.processKeyEvent(event);
		if (event.getKeyCode() == KeyEvent.VK_ENTER) {
			doClick();
		}
	}
}
