// OO jDREW - An Object Oriented extension of the Java Deductive Reasoning Engine for the Web
// Copyright (C) 2005 Marcel Ball
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

package org.ruleml.oojdrew.BottomUp.Builtins;

import java.util.Vector;

import org.ruleml.oojdrew.Builtins.Builtin;
import org.ruleml.oojdrew.util.DefiniteClause;

/**
 * This class allows the use of generic built-ins (Classes that properly
 * implement the jdrew.oo.builtins.Builtin Interface) within the Bottom-Up (BU)
 * engine.
 *
 * Generic built-ins must be wrapped for use in each engine to enable access
 * proper access to the required data structures.
 *
 * <p>Title: OO jDREW</p>
 *
 * <p>Description: Reasoning Engine for the Semantic Web - Supporting OO RuleML
 * 0.88</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * @author Marcel A. Ball
 * @version 0.89
 */
public class BUBuiltin {
        /**
         * A reference to the object that implements the generic built-in
         * relation. This object must implement the jdrew.oo.builtins.Builtin
         * Interface correctly.
         */
        Builtin builtin;

    /**
     * Constructs a new empty Bottom-up built-in. This is provided only for
     * compatability reasons.
     */
    public BUBuiltin() {
        builtin = null;
    }

    /**
     * Constructs a new Bottom-Up built-in. The user should pass an instance of
     * the Class that implements the generic built-in relation.
     *
     * @param b Builtin A generic built-in implementation class, this must
     * implement the jdrew.oo.builtins.Builtin Interface.
     */
    public BUBuiltin(Builtin b) {
        builtin = b;
    }

    /**
     * Gets the integer symbol code for the built-in relation symbol. This
     * method simply calls the getSymbol() method of the generic built-in
     * implementation that is referenced by this BUBuiltin object.
     *
     * @return int The integer code for the built-in relation symbol
     */
    public int getSymbol() {
        return builtin.getSymbol();
    }

    /**
     * Builds the resulting fact from the call to this built-in if it exists.
     * This method extracts the appropriate Term object that represents the call
     * to the built-in relation and passes to the generic built-in's
     * builtResult() method.
     *
     * @param dc DefiniteClause The clause that contains the call to the
     * built-in
     *
     * @param term int The index into the atoms of the clause for the call to
     * the built-in relation represented by this BUBuiltin object.
     *
     * @return Vector A vector containing a fact that will unify with the call
     * to the built-in if it should succeed. null or a fact that will not unify
     * (null is preferred as it is more efficient) should be returned if the
     * call to built-in relation should not succeed.
     */
    public Vector buildResult(DefiniteClause dc, int term) {
        Vector v = new Vector();
        DefiniteClause dc2 = builtin.buildResult(dc.atoms[term]);
        if(dc2 != null)
            v.add(dc2);
        return v;
    }

}
