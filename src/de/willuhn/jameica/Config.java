/**********************************************************************
 * $Source: /cvsroot/jameica/jameica/src/de/willuhn/jameica/Attic/Config.java,v $
 * $Revision: 1.2 $
 * $Date: 2003/10/29 00:41:26 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/
package de.willuhn.jameica;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import de.bb.util.XmlFile;
import de.willuhn.jameica.rmi.LocalServiceData;
import de.willuhn.jameica.rmi.RemoteServiceData;

public class Config
{

  private XmlFile xml = new XmlFile();

  private Hashtable remoteServices  = new Hashtable();
  private Hashtable localServices   = new Hashtable();
  

  private int rmiPort;
  private Locale defaultLanguage = new Locale("de_DE");


  protected Config(String fileName) throws FileNotFoundException
  {
    init(fileName);
  }

  private void init(String fileName) throws FileNotFoundException
  {
    if (fileName == null)
      fileName = "cfg/config.xml";

    FileInputStream file = null;
    try {
      file = new FileInputStream(fileName);
    }
    catch (FileNotFoundException e)
    {
      fileName = "cfg/config.xml";
      // mhh, Path invalid. Try default path
      try {
        file = new FileInputStream(fileName);
      }
      catch (FileNotFoundException e2)
      {
      }
    }
    if (file == null)
      throw new FileNotFoundException("alert: config file " + fileName + " not found.");

    xml.read(file);
    readServices();
  }



  private void readServices()
  {
    Enumeration e = xml.getSections("/config/services/").elements();
  
    String key;
    String type;
    String name;
    Application.getLog().info("loading service configuration");
  
    while (e.hasMoreElements())
    {
      key = (String) e.nextElement();
      type = xml.getString(key,"type",null);
      name = xml.getString(key,"name",null);
  
  
  
      // process remote hubs
      if (key.startsWith("/config/services/remoteservice")) 
      {
        Application.getLog().info("  found remote service \"" + name + "\"");
        remoteServices.put(name,new RemoteServiceData(xml,key));
      }
  
      // process local hubs
      if (key.startsWith("/config/services/localservice")) 
      {
        Application.getLog().info("  found local service \"" + name + "\"");
        localServices.put(name,new LocalServiceData(xml,key));
      }
  
    }
    Application.getLog().info("done");
    ////////////////////////////////////////////
  
  
  
    // Read default language
    String _defaultLanguage = xml.getContent("/config/defaultlanguage");
    Application.getLog().info("choosen language: " + _defaultLanguage);
    try {
      ResourceBundle.getBundle("lang/messages",new Locale(_defaultLanguage));
      defaultLanguage = new Locale(_defaultLanguage);
    }
    catch (Exception ex)
    {
      Application.getLog().info("  not found. fallback to default language: " + defaultLanguage.toString());
    }

    // Read rmi port
    try {
      rmiPort = Integer.parseInt(xml.getContent("/config/rmiport"));
    }
    catch (NumberFormatException nfe)
    {
      rmiPort = 1099;
    }
  }


  public RemoteServiceData getRemoteServiceData(String name)
  {
    return (RemoteServiceData) remoteServices.get(name);
  }
  
  public LocalServiceData getLocalServiceData(String name)
  {
    return (LocalServiceData) localServices.get(name);
  }

  public Enumeration getLocalServiceNames()
  {
    return localServices.keys();
  }

  public Enumeration getRemoteServiceNames()
  {
    return remoteServices.keys();
  }
  
  public int getRmiPort()
  {
    return rmiPort;
  }

  public Locale getLocale()
  {
    return defaultLanguage;
  }


}


/*********************************************************************
 * $Log: Config.java,v $
 * Revision 1.2  2003/10/29 00:41:26  willuhn
 * *** empty log message ***
 *
 * Revision 1.1  2003/10/23 21:49:46  willuhn
 * initial checkin
 *
 **********************************************************************/
