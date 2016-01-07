/*
 * Copyright (C) 2004 NNL Technology AB
 * Visit www.infonode.net for information about InfoNode(R) 
 * products and how to contact NNL Technology AB.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, 
 * MA 02111-1307, USA.
 */


//$Id: TabAreaComponentsProperties.java,v 1.15 2005/02/16 11:28:15 jesper Exp $

package net.infonode.tabbedpanel;

import net.infonode.gui.hover.HoverListener;
import net.infonode.properties.gui.util.ComponentProperties;
import net.infonode.properties.gui.util.ShapedPanelProperties;
import net.infonode.properties.propertymap.*;
import net.infonode.properties.types.BooleanProperty;
import net.infonode.properties.types.HoverListenerProperty;

/**
 * TabAreaComponentsProperties holds all visual properties for the area in a
 * tabbed panel's tab area where the tab area components (scroll buttons, tab
 * drop down list and components set by calling setTabAreaComponents in a tabbed
 * panel) are shown. TabbedPanelProperties contains TabAreaComponentsProperties.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.15 $
 * @see TabbedPanel
 * @see TabbedPanelProperties
 * @since ITP 1.1.0
 */
public class TabAreaComponentsProperties extends PropertyMapContainer {
  /**
   * A property group for all properties in TabAreaComponentsProperties
   */
  public static final PropertyMapGroup PROPERTIES = new PropertyMapGroup("Tab Area Properties",
                                                                         "Properties for the TabbedPanel class.");

  /**
   * Stretch enabled property
   *
   * @see #setStretchEnabled
   * @see #getStretchEnabled
   */
  public static final BooleanProperty STRETCH_ENABLED = new BooleanProperty(PROPERTIES, "Stretch Enabled", "Stretch components to be as high as tabs if tabs are higher than components.",
                                                                            PropertyMapValueHandler.INSTANCE);

  /**
   * Properties for the component
   *
   * @see #getComponentProperties
   */
  public static final PropertyMapProperty COMPONENT_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                         "Component Properties",
                                                                                         "Properties for tab area components area.",
                                                                                         ComponentProperties.PROPERTIES);

  /**
   * Properties for the shaped panel
   *
   * @see #getShapedPanelProperties
   * @since ITP 1.2.0
   */
  public static final PropertyMapProperty SHAPED_PANEL_PROPERTIES = new PropertyMapProperty(PROPERTIES,
                                                                                            "Shaped Panel Properties",
                                                                                            "Properties for shaped tab area components area.",
                                                                                            ShapedPanelProperties.PROPERTIES);

  /**
   * Hover listener property
   *
   * @see #setHoverListener
   * @see #getHoverListener
   * @since ITP 1.3.0
   */
  public static final HoverListenerProperty HOVER_LISTENER = new HoverListenerProperty(PROPERTIES,
                                                                                       "Hover Listener",
                                                                                       "Hover Listener to be used for tracking mouse hovering over the tab area components area.",
                                                                                       PropertyMapValueHandler.INSTANCE);

  /**
   * Constructs an empty TabAreaComponentsProperties object
   */
  public TabAreaComponentsProperties() {
    super(PROPERTIES);
  }

  /**
   * Constructs a TabAreaComponentsProperties object with the given object as
   * property storage
   *
   * @param object object to store properties in
   */
  public TabAreaComponentsProperties(PropertyMap object) {
    super(object);
  }

  /**
   * Constructs a TabAreaComponentsProperties object that inherits its properties
   * from the given TabAreaComponentsProperties object
   *
   * @param inheritFrom TabAreaComponentsProperties object to inherit properties from
   */
  public TabAreaComponentsProperties(TabAreaComponentsProperties inheritFrom) {
    super(PropertyMapFactory.create(inheritFrom.getMap()));
  }

  /**
   * Adds a super object from which property values are inherited.
   *
   * @param superObject the object from which to inherit property values
   * @return this
   */
  public TabAreaComponentsProperties addSuperObject(TabAreaComponentsProperties superObject) {
    getMap().addSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Removes the last added super object.
   *
   * @return this
   */
  public TabAreaComponentsProperties removeSuperObject() {
    getMap().removeSuperMap();
    return this;
  }

  /**
   * Removes the given super object.
   *
   * @param superObject super object to remove
   * @return this
   * @since ITP 1.3.0
   */
  public TabAreaComponentsProperties removeSuperObject(TabAreaComponentsProperties superObject) {
    getMap().removeSuperMap(superObject.getMap());
    return this;
  }

  /**
   * Gets if components should be stretched to same height as tabs if tabs are
   * higher than components.
   *
   * @return true if stretch is enabled, otherwise false
   */
  public boolean getStretchEnabled() {
    return STRETCH_ENABLED.get(getMap());
  }

  /**
   * Sets if components should be stretched to same height as tabs if tabs are
   * higher than components.
   *
   * @param enabled true for stretch, otherwise false
   * @return this TabAreaComponentsProperties
   */
  public TabAreaComponentsProperties setStretchEnabled(boolean enabled) {
    STRETCH_ENABLED.set(getMap(), enabled);
    return this;
  }

  /**
   * Gets the component properties
   *
   * @return component properties
   */
  public ComponentProperties getComponentProperties() {
    return new ComponentProperties(COMPONENT_PROPERTIES.get(getMap()));
  }

  /**
   * Gets the shaped panel properties
   *
   * @return shaped panel properties
   * @since ITP 1.2.0
   */
  public ShapedPanelProperties getShapedPanelProperties() {
    return new ShapedPanelProperties(SHAPED_PANEL_PROPERTIES.get(getMap()));
  }

  /**
   * <p>Sets the hover listener that will be triggered when the tab area components area is hoverd by the mouse.</p>
   *
   * <p>The tabbed panel that the hovered tab area components area is part of will be the source of the hover event
   * sent to the hover listener.</p>
   *
   * @param listener the hover listener
   * @return this TabAreaComponentsProperties
   * @since ITP 1.3.0
   */
  public TabAreaComponentsProperties setHoverListener(HoverListener listener) {
    HOVER_LISTENER.set(getMap(), listener);
    return this;
  }

  /**
   * <p>Gets the hover listener that will be triggered when the tab area components area is hovered by the mouse.</p>
   *
   * <p>The tabbed panel that the hovered tab area components area is part of will be the source of the hover event
   * sent to the hover listener.</p>
   *
   * @return the hover listener
   * @since ITP 1.3.0
   */
  public HoverListener getHoverListener() {
    return HOVER_LISTENER.get(getMap());
  }
}