/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/. */

package pt.webdetails.cda;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.platform.api.engine.IPluginLifecycleListener;
import org.pentaho.platform.api.engine.PluginLifecycleException;
import pt.webdetails.cda.cache.CacheScheduleManager;
import pt.webdetails.cda.utils.PluginHibernateUtil;
import pt.webdetails.cda.utils.Util;

/**
 * This class inits Cda plugin within the bi-platform
 * @author gorman
 *
 */
public class CdaLifecycleListener implements IPluginLifecycleListener
{

  static Log logger = LogFactory.getLog(CacheScheduleManager.class);


  public void init() throws PluginLifecycleException
  {
    // boot cda
    CdaBoot.getInstance().start();
    PluginHibernateUtil.initialize();
  }


  public void loaded() throws PluginLifecycleException
  {
    ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
    try
    {
      Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
      
      try {
        CdaEngine.init(new PentahoCdaEnvironment());
      } catch (InitializationException ie) {
        throw new PluginLifecycleException("Error initializing CDA Engine", ie);
      }
      
      
      CacheScheduleManager.getInstance().coldInit();
    }
    catch (Exception e)
    {
      logger.error(Util.getExceptionDescription(e));
    }
    finally
    {
      Thread.currentThread().setContextClassLoader(contextCL);
    }
  }


  public void unLoaded() throws PluginLifecycleException
  {
  }
}
