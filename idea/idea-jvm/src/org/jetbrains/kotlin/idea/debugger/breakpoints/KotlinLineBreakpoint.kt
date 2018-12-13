/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.debugger.breakpoints

import com.intellij.debugger.engine.DebugProcess
import com.intellij.debugger.ui.breakpoints.LineBreakpoint
import com.intellij.openapi.project.Project
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointProperties
import com.sun.jdi.ReferenceType
import org.jetbrains.java.debugger.breakpoints.properties.JavaLineBreakpointProperties
import org.jetbrains.kotlin.idea.debugger.safeAllLineLocations
import org.jetbrains.kotlin.idea.debugger.safeLineNumber

class KotlinLineBreakpoint(
    project: Project?,
    xBreakpoint: XBreakpoint<out XBreakpointProperties<*>>?
) : LineBreakpoint<JavaLineBreakpointProperties>(project, xBreakpoint) {
    override fun processClassPrepare(debugProcess: DebugProcess?, classType: ReferenceType?) {
        val lineIndex = (lineIndex.takeIf { it >= 0 } ?: return super.processClassPrepare(debugProcess, classType)) + 1
        val clazz = classType ?: return super.processClassPrepare(debugProcess, classType)

        if (clazz.safeAllLineLocations().any { it.safeLineNumber() == lineIndex }) {
            super.processClassPrepare(debugProcess, classType)
        }
    }
}