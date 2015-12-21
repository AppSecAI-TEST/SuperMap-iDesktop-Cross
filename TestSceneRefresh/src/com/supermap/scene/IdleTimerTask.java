package com.supermap.scene;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;

public class IdleTimerTask extends TimerTask {

	private static boolean initWorkspace = false;
	@Override
	public void run() {
		try {
			if (!initWorkspace) {
				Test.sceneFrame.sceneControl.getScene().setWorkspace(Test.workspace);
				initWorkspace = true;
			}
//			// ˢ����������
//			Test.sceneFrame.refreshToolbar();
		} catch (Exception ex) {
			
		} finally {
			// ִ����ˢ�º�������ʱ����׼����һ��ˢ��
	        new Timer().schedule(new IdleTimerTask(), 1000);  
		}
	}

}
