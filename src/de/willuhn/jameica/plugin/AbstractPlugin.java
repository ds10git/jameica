/**********************************************************************
 * $Source: /cvsroot/jameica/jameica/src/de/willuhn/jameica/plugin/AbstractPlugin.java,v $
 * $Revision: 1.5 $
 * $Date: 2004/11/17 19:02:24 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/
package de.willuhn.jameica.plugin;

import java.io.File;

import de.willuhn.jameica.system.Application;
import de.willuhn.util.ApplicationException;

/**
 * Abstrakte Basis-Klasse aller Plugins.
 * Jedes Plugin muss diese Klasse erweitern, damit es beim Start von
 * Jameica erkannt wird.
 * @author willuhn
 */
public abstract class AbstractPlugin
{

	private File file = null;
	private PluginResources res = null;
	private Manifest manifest = null;
	
	/**
	 * ct.
   * @param file Das File, in dem sich das Plugin befindet.
   * Ist i.d.R. das Jar des Plugins selbst.
   */
  public AbstractPlugin(File file)
	{
		this.file = file;
		this.res = new PluginResources(this);
		this.manifest = Application.getPluginLoader().getPluginContainer(this.getClass()).getManifest();
	}

	/**
	 * Liefert ein File-Objekt, welches das Plugin enthaelt.
	 * Befindet sich das Plugin in einem Jar, wird dieses
	 * zurueckgegeben, sonst das Verzeichnis, in dem sich das
	 * Plugin befindet.
   * @return File, in dem sich das Plugin befindet.
   */
  protected final File getFile()
	{
		return file;
	}

	/**
	 * Liefert ein Objekt mit Resourcen, auf die das Plugin zugreifen kann.
   * @return Resource-Pack.
   */
  public final PluginResources getResources()
	{
		return res;
	}

	/**
	 * Liefert das Manifest des Plugins.
   * @return Manifest.
   */
  public final Manifest getManifest()
	{
		return manifest;
	}

  /**
	 * Diese Funktion wird beim Start der Anwendung ausgefuehrt. Hier kann die Plugin-
	 * Implementierung also diverse Dinge durchfuehren, die es beim Start gern
	 * automatisch durchgefuehrt haben moechte.
	 * Nur wenn die Funktion fehlerfrei durchlaeuft, wird das Plugin aktiviert.
	 * Andernfalls wird der Text der geworfenen Exception dem Benutzer auf der
	 * Start-Seite von Jameica angezeigt. Von daher empfiehlt es sich, verstaendliche
	 * Formulierungen fuer ggf aufgetretene Fehler zu verwenden.
	 * Hinweis: Diese Funktion wird von Jameica <b>vor</b> dem Initialisieren
	 * der Services aufgerufen.
   * @throws ApplicationException muss geworfen werden, wenn das Plugin nicht aktiviert werden soll.
	 */
	public abstract void init() throws ApplicationException;


	/**
	 * Diese Funktion wird beim Start der Anwendung aufgerufen, wenn das Plugin
	 * zum ersten mal gestartet wird. Die install() Funktion wird solange bei
	 * jedem Start aufgerufen, bis sie fehlerfrei durchlaeuft.
	 * Andernfalls wird der Text der geworfenen Exception dem Benutzer auf der
	 * Start-Seite von Jameica angezeigt. Von daher empfiehlt es sich, verstaendliche
	 * Formulierungen fuer ggf aufgetretene Fehler zu verwenden.
	 * Hinweis: Diese Funktion wird von Jameica <b>vor</b> dem Initialisieren
	 * der Services aufgerufen.
   * @throws ApplicationException muss geworfen werden, wenn die Installation fehlschlug und das Plugin nicht aktiviert werden soll.
	 */
	public abstract void install() throws ApplicationException;

	/**
	 * Diese Funktion wird beim Start der Anwendung genau dann aufgerufen, wenn
	 * das Plugin bereits erfolgreich installiert wurde, jedoch jetzt in einer
	 * anderen Version vorliegt als die vorherige. Sie wird solange bei jedem Start
	 * aufgerufen, bis sie fehlerfrei durchlaeuft.
	 * Andernfalls wird der Text der geworfenen Exception dem Benutzer auf der
	 * Start-Seite von Jameica angezeigt. Von daher empfiehlt es sich, verstaendliche
	 * Formulierungen fuer ggf aufgetretene Fehler zu verwenden.
	 * Hinweis: Diese Funktion wird von Jameica <b>vor</b> dem Initialisieren
	 * der Services aufgerufen.
	 * @param oldVersion Version, die vorher installiert war.
   * @throws ApplicationException muss geworfen werden, wenn das Update fehlschlug und das Plugin nicht aktiviert werden soll.
	 */
	public abstract void update(double oldVersion) throws ApplicationException;

	/**
	 * Diese Funktion wird beim Beenden der Anwendung ausgefuehrt.
	 */
	public abstract void shutDown();
	
	/**
	 * Liefert die Namen aller Services, die dieses Plugin anbietet.
	 * Hintergrund: Ein Plugin kann verschiedene Services verwenden,
	 * Ein solcher Service kann von verschiedenster Art sein. Ob dies
	 * nun eine Datenquelle ist, ueber die Objekte geladen und gespeichert
	 * werden koennen, ein LDAP-Server oder irgendein Service zur Anbindung
	 * von Daten sei dahingestellt. Entscheidend ist lediglich, dass dieser
	 * Service das Interface <code>Service</code> implementiert und somit
	 * netzwerktauglich ist. Laeuft Jameica im Client-Mode, dann werden
	 * keine lokalen Services verwendet sondern Remote-Services vom jeweils
	 * konfigurierten Server.
	 * Daher: Services muessen generell ueber die ServiceFactory
	 * via <code>ServiceFactory.lookup(AbstractPlugin,String)</code> geholt werden.
	 * Nur sie weiss, wo sich der Service befindet - lokal oder remote. 
	 * <br>Wenn das Plugin keine Services verwendet oder anbietet, kann
	 * die Funktion als Dummy implementiert werden, indem sie generell <code>null</code>
	 * zurueckliefert. 
   * @return Liste aller Services.
   */
  public abstract String[] getServiceNames();
	
	/**
	 * Liefert die Klasse des Service zum genannten Namen.
	 * Diese Funktion wird von der ServiceFactory in der Methode
	 * <code>lookup(AbstractPlugin,String)</code> aufgerufen, um
	 * die Klasse zu ermitteln, fuer welche die Instanz erzeugt werden soll.
   * @param serviceName Name des Services.
   * @return die Klasse des Service.
   */
  public abstract Class getService(String serviceName);


}

/*********************************************************************
 * $Log: AbstractPlugin.java,v $
 * Revision 1.5  2004/11/17 19:02:24  willuhn
 * *** empty log message ***
 *
 * Revision 1.4  2004/10/11 15:39:21  willuhn
 * *** empty log message ***
 *
 * Revision 1.3  2004/10/08 00:19:19  willuhn
 * *** empty log message ***
 *
 * Revision 1.2  2004/07/21 23:54:54  willuhn
 * @C massive Refactoring ;)
 *
 * Revision 1.1  2004/07/21 20:08:45  willuhn
 * @C massive Refactoring ;)
 *
 * Revision 1.19  2004/06/30 20:58:39  willuhn
 * *** empty log message ***
 *
 * Revision 1.18  2004/04/19 22:05:27  willuhn
 * *** empty log message ***
 *
 * Revision 1.17  2004/04/14 23:53:44  willuhn
 * *** empty log message ***
 *
 * Revision 1.16  2004/04/14 22:16:43  willuhn
 * *** empty log message ***
 *
 * Revision 1.15  2004/03/18 01:24:47  willuhn
 * @C refactoring
 *
 * Revision 1.14  2004/03/16 23:59:40  willuhn
 * @N 2 new Input fields
 *
 * Revision 1.13  2004/03/06 18:24:24  willuhn
 * @D javadoc
 *
 * Revision 1.12  2004/03/03 22:27:11  willuhn
 * @N help texts
 * @C refactoring
 *
 * Revision 1.11  2004/02/25 23:11:57  willuhn
 * *** empty log message ***
 *
 * Revision 1.10  2004/02/09 13:06:33  willuhn
 * @C added support for uncompressed plugins
 *
 * Revision 1.9  2004/01/08 20:50:32  willuhn
 * @N database stuff separated from jameica
 *
 * Revision 1.8  2004/01/06 20:32:59  willuhn
 * *** empty log message ***
 *
 * Revision 1.7  2004/01/05 18:27:13  willuhn
 * *** empty log message ***
 *
 * Revision 1.6  2004/01/03 18:08:05  willuhn
 * @N Exception logging
 * @C replaced bb.util xml parser with nanoxml
 *
 * Revision 1.5  2003/12/30 17:44:54  willuhn
 * *** empty log message ***
 *
 * Revision 1.4  2003/12/30 02:10:57  willuhn
 * @N updateChecker
 *
 * Revision 1.3  2003/12/29 17:44:10  willuhn
 * *** empty log message ***
 *
 * Revision 1.2  2003/12/29 17:11:49  willuhn
 * *** empty log message ***
 *
 * Revision 1.1  2003/12/29 16:29:47  willuhn
 * @N javadoc
 *
 **********************************************************************/