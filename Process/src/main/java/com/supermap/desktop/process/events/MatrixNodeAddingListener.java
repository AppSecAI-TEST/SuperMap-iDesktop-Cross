package com.supermap.desktop.process.events;

import java.util.EventListener;

/**
 * Created by highsad on 2017/6/21.
 */
public interface MatrixNodeAddingListener<T extends Object> extends EventListener {
	void matrixNodeAdding(MatrixNodeAddingEvent<T> e);
}
