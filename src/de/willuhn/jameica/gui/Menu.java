/**********************************************************************
 * $Source: /cvsroot/jameica/jameica/src/de/willuhn/jameica/gui/Menu.java,v $
 * $Revision: 1.3 $
 * $Date: 2003/10/29 00:41:26 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by bbv AG
 * All rights reserved
 *
 **********************************************************************/
package de.willuhn.jameica;

import java.lang.reflect.Constructor;
import java.util.Enumeration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;

import de.bb.util.XmlFile;

/**
 * @author willuhn
 */
public class Menu
{

  private XmlFile xml;
  private final org.eclipse.swt.widgets.Menu bar;


  protected Menu()
  {

    bar = new org.eclipse.swt.widgets.Menu(GUI.shell,SWT.BAR);
		GUI.shell.setMenuBar(bar);

    xml  = new XmlFile();
    xml.read(getClass().getResourceAsStream("/menu.xml"));

    // add elements
    Enumeration e = xml.getSections("/menu/").elements();
    while (e.hasMoreElements())
    {
      String key = (String) e.nextElement();
      new MenuCascade(key);
    }
  }

  class MenuCascade {

    private String path;

    MenuCascade(String key)
    {
      final MenuItem cascade = new MenuItem(bar,SWT.CASCADE);
      String text = I18N.tr(xml.getString(key,"name",null));
      cascade.setText(text);
      final org.eclipse.swt.widgets.Menu submenu = new org.eclipse.swt.widgets.Menu(GUI.shell, SWT.DROP_DOWN);
      cascade.setMenu(submenu);
      Enumeration e = xml.getSections(key).elements();
      while (e.hasMoreElements())
      {
        String ckey = (String) e.nextElement();
        new MenuElement(submenu, ckey);
      }

    }
  }

  class MenuElement {

    MenuElement(org.eclipse.swt.widgets.Menu parent,String ckey)
    {
      final MenuItem item = new MenuItem(parent,SWT.CASCADE);
      final String s = ckey;
      item.addListener (SWT.Selection, new Listener()
      {
        public void handleEvent(org.eclipse.swt.widgets.Event event)
        {
					String c = xml.getString(s,"class",null);
					try {
						Class clazz = (Class) Class.forName(c);
						Constructor ct = clazz.getConstructor(new Class[]{Event.class});
						ct.setAccessible(true);
						ct.newInstance(new Object[] {event});
					}
					catch (Exception e)
					{
						Application.getLog().warn("Class " + c + " not found");
					}
        }
      });
      String text = I18N.tr(xml.getString(s,"name",null));
      item.setText(text);
      // closeitem.setAccelerator(SWT.ALT + SWT.F4);
    }
  }

}

/*********************************************************************
 * $Log: Menu.java,v $
 * Revision 1.3  2003/10/29 00:41:26  willuhn
 * *** empty log message ***
 *
 **********************************************************************/
