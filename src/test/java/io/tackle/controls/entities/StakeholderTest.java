/*
 * Copyright © 2021 Konveyor (https://konveyor.io/)
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
 */
package io.tackle.controls.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class StakeholderTest {
    @Test
    public void testEquals() {
        Stakeholder x = new Stakeholder();
        x.id = 0L;
        Stakeholder y = new Stakeholder();
        y.id = 0L;
        Stakeholder z = new Stakeholder();
        z.id = 0L;
        // Reflexive
        assertEquals(x, x);
        // Symmetric
        assertEquals(x.equals(y), y.equals(x));
        // Transitive
        assertEquals(x.equals(y) && y.equals(z), x.equals(z));
        // Consistent
        assertEquals(x, y);
        y.email = "y";
        y.displayName = "y";
        assertEquals(x, y);
        // Non-nullity
        assertFalse(x.equals(null));

        assertNotEquals( "test", z);
    }

    @Test
    public void testHashCode() {
        Stakeholder x = new Stakeholder();
        x.id = 0L;
        int initial = x.hashCode();
        x.email = "x";
        x.displayName = "x";
        assertEquals(initial, x.hashCode());
        Stakeholder y = new Stakeholder();
        y.id = 0L;
        assertEquals(x, y);
        assertEquals(x.hashCode(), y.hashCode());
    }

}
