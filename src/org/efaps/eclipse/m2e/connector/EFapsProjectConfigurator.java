/*
 * Copyright 2003 - 2011 The eFaps Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Revision:        $Rev$
 * Last Changed:    $Date$
 * Last Changed By: $Author$
 */

package org.efaps.eclipse.m2e.connector;

import java.io.File;

import org.apache.maven.plugin.MojoExecution;
import org.codehaus.plexus.util.DirectoryScanner;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.configurator.AbstractBuildParticipant;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.eclipse.m2e.jdt.AbstractJavaProjectConfigurator;
import org.eclipse.m2e.jdt.IClasspathDescriptor;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class EFapsProjectConfigurator
    extends AbstractJavaProjectConfigurator
{

    @Override
    public AbstractBuildParticipant getBuildParticipant(final IMavenProjectFacade _projectFacade,
                                                        final MojoExecution _execution,
                                                        final IPluginExecutionMetadata _executionMetadata)
    {
        return new EFapsBuildParticipant(_execution);
    }

    @Override
    public void configureRawClasspath(final ProjectConfigurationRequest request,
                                      final IClasspathDescriptor classpath,
                                      final IProgressMonitor monitor)
        throws CoreException
    {
        final IProject project = request.getProject();
        final IMavenProjectFacade facade = request.getMavenProjectFacade();
        // search for folder that are in the src folder and are named "ESJP" and add them to the classpath
        final DirectoryScanner ds = new DirectoryScanner();
        final String[] includes = new String[] { "**src\\**\\ESJP" };
        final String[] excludes = new String[] { "**\\.svn\\**" };
        ds.setBasedir(facade.getPomFile().getParent());
        ds.setIncludes(includes);
        ds.setExcludes(excludes);
        ds.setCaseSensitive(true);
        ds.scan();
        final String[] dirs = ds.getIncludedDirectories();
        for (final String dir : dirs) {
            final Path path = new Path(project.getFullPath().toString() + File.separator + dir);
            if (!classpath.containsPath(path)) {
                classpath.addSourceEntry(path, facade.getOutputLocation(), true);
            }
        }
    }
}
