/******************************************************************************
 * Copyright (C) 2015 Luis Amesty                                             *
 * Copyright (C) 2015 AMERP Consulting                                        *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 ******************************************************************************/
package org.amerp.amncallouts;

/**
 * @author luisamesty
 *
 */
import java.util.List;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

public class ScriptEngineTest {
  public static void main(String[] args) {
    ScriptEngineManager manager = new ScriptEngineManager();

    List<ScriptEngineFactory> factories = manager.getEngineFactories();
    for (ScriptEngineFactory factory : factories) {
      System.out.println("Engine name (full): " + factory.getEngineName());
      System.out.println("Engine version: " + factory.getEngineVersion());
      System.out.println("Supported extensions:");
      List<String> extensions = factory.getExtensions();
      for (String extension : extensions)
        System.out.println("  " + extension);
      System.out.println("Language name: " + factory.getLanguageName());
      System.out.println("Language version: " + factory.getLanguageVersion());
      System.out.println("Supported MIME types:");
      List<String> mimetypes = factory.getMimeTypes();
      for (String mimetype : mimetypes)
        System.out.println("  " + mimetype);
      System.out.println("Supported short names:");
      List<String> shortnames = factory.getNames();
      for (String shortname : shortnames)
        System.out.println("  " + shortname);
      System.out.println();
    }
  }
}