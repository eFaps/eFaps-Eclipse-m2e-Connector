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
import java.util.Set;

import org.apache.maven.plugin.MojoExecution;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;
import org.sonatype.plexus.build.incremental.BuildContext;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class EFapsBuildParticipant
    extends MojoExecutionBuildParticipant
{

    /**
     * @param _execution execution context
     */
    public EFapsBuildParticipant(final MojoExecution _execution)
    {
        super(_execution, true);
    }

    @Override
    public Set<IProject> build(final int kind,
                               final IProgressMonitor monitor)
        throws Exception
    {
        final Set<IProject> result = super.build(kind, monitor);
        final IMaven maven = MavenPlugin.getMaven();
        final BuildContext buildContext = getBuildContext();

        // tell m2e builder to refresh generated files
        final File generated = maven.getMojoParameterValue(getSession(), getMojoExecution(), "outputDirectory",
                        File.class);
        if (generated != null) {
            buildContext.refresh(generated);
        }
        return result;
    }
}
