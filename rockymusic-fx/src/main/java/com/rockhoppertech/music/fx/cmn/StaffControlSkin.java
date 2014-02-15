package com.rockhoppertech.music.fx.cmn;

/*
 * #%L
 * Rocky Music FX
 * %%
 * Copyright (C) 1996 - 2014 Rockhopper Technologies
 * %%
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
 * #L%
 */

import javafx.scene.Node;
import javafx.scene.control.Skin;

/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
// public class StaffControlSkin extends SkinBase<StaffControl, BehaviorBase>
// implements Skin<StaffControl>{

public class StaffControlSkin implements Skin<StaffControl> {

    private StaffControl staffControl;

    /**
     * @param staffControl
     */
    public StaffControlSkin(StaffControl staffControl) {
        this.staffControl = staffControl;
    }

    /**
     * Called by a Skinnable when the Skin is replaced on the Skinnable. This
     * method allows a Skin to implement any logic necessary to clean up itself
     * after the Skin is no longer needed. It may be used to release native
     * resources. The methods {@link #getSkinnable()} and {@link #getNode()}
     * should return null following a call to dispose. Calling dispose twice has
     * no effect.
     */
    // @Override
    public void dispose() {
        // remove listeners
        // set controls to null
    }

    // @Override
    public Node getNode() {
        return staffControl.getParent();
    }

    // @Override
    public StaffControl getSkinnable() {
        return this.staffControl;
    }

}
