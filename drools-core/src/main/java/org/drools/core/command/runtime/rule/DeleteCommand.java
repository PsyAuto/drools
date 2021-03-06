/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.core.command.runtime.rule;

import org.drools.core.command.impl.GenericCommand;
import org.drools.core.command.impl.KnowledgeCommandContext;
import org.drools.core.common.DisconnectedFactHandle;
import org.kie.internal.command.Context;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.NONE)
public class DeleteCommand
        implements GenericCommand<Void> {

    private DisconnectedFactHandle handle;

    public DeleteCommand() {
    }

    public DeleteCommand(FactHandle handle) {
        this.handle = DisconnectedFactHandle.newFrom( handle );
    }

    public Void execute(Context context) {
        KieSession ksession = ((KnowledgeCommandContext) context).getKieSession();
        ksession.getEntryPoint( handle.getEntryPointId() ).retract( handle );
        return null;
    }

    public FactHandle getFactHandle() {
        return this.handle;
    }

    @XmlAttribute(name="fact-handle", required=true)
    public void setFactHandleFromString(String factHandleId) {
        handle = new DisconnectedFactHandle(factHandleId);
    }

    public String getFactHandleFromString() {
        return handle.toExternalForm();
    }

    public String toString() {
        return "session.retract( " + handle + " );";
    }

}
